package com.lunarsky.minipos.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityInUseException;
import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.UserDTO;
import com.lunarsky.minipos.model.ui.User;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UserOverviewDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Users";
	private static final String ERROR_TEXT_USER_NOT_FOUND = "User does not exist";
	private static final String ERROR_TEXT_USER_IN_USE = "User still in use by something";
	
	private final AppData appData;
	private ObservableList<User> userList;
	
	
	@FXML
	private ListView<User> userListView;
	@FXML
	private TextField nameTextField;
	@FXML
	private PasswordField passwordPasswordField;
	@FXML
	private Button addButton;
	
	
	public UserOverviewDialog(final Stage parentStage) {
		assert(null != parentStage);
		
		this.appData = AppData.getInstance();
		
		UiUtil.createDialog(parentStage,WINDOW_TITLE,this,"UserOverviewDialog.fxml");
	}

	
	/**************************************************************************
	 * Initialize
	 **************************************************************************/
	@FXML
	private void initialize() {
		
		initializeMembers();
		initializeControls();
		initializeBindings();
		initializeListeners();
		initializeAsync();
		
	}

	private void initializeMembers() {
		
	}
	
	private void initializeControls() {
		
		nameTextField.setText("");
		passwordPasswordField.setText("");	
		
		userListView.setCellFactory((listCell) -> {
			ListCell<User> cell = new ListCell<User>() {
				@Override
				protected void updateItem(User user, boolean empty) {
					super.updateItem(user,empty);
					if(null != user) {
						setText(user.getName());
						final ContextMenu menu = createContextMenu();
						setContextMenu(menu);
					} else {
						setText("");
						setContextMenu(null);
					}
				}
			};
			return cell;
		});
		
	}
	
	private void initializeBindings() {

	}
	
	private void initializeListeners() {
		
		getStage().setOnCloseRequest((event) -> { close(); });
		
		userListView.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> handleSelectedUserChanged(newValue));
		
	}
	
	private void initializeAsync() {
		
		Task<List<UserDTO>> task = new Task<List<UserDTO>>() {
			@Override
			protected List<UserDTO> call() {
				final List<UserDTO> users = appData.getServerConnector().getUsers();
				return users;
			}
			@Override
			protected void succeeded() {
				log.debug("getUsers() Succeeded");
				final List<UserDTO> dtoList = getValue();
				
				final List<User> list = new ArrayList<User>();
				for(UserDTO userDTO: dtoList) {
					final User user = new User(userDTO); 
					log.debug("Adding User: {}",user);
					list.add(user);
				}
				
				userList = FXCollections.observableArrayList(list);
				Collections.sort(userList);
				userListView.setItems(userList);
			}
			@Override 
			protected void failed() {
				log.error("getUsers() Failed");
				final Throwable throwable = getException();
				log.catching(Level.ERROR,throwable);
				throw new RuntimeException(throwable);
			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Starting getUsers() Task {}",thread);
		thread.start();		
	}

	
	/**************************************************************************
	 * Getters & Setters
	 **************************************************************************/
	public Stage getStage() {
		return (Stage)getScene().getWindow();
	}
	
	private User getSelectedUser() {
		return userListView.getSelectionModel().getSelectedItem();
	}
	
	private void setSelectedUser(final User user) {
		if(null != user) {
			userListView.getSelectionModel().select(user);			
		} else {
			userListView.getSelectionModel().clearSelection();
		}
	}
	
	/**************************************************************************
	 * Event Handlers
	 **************************************************************************/

	private void handleSelectedUserChanged(final User user) {
		log.debug("handleSelectedUserChanged() {}",user);

		if(null != user) {
			nameTextField.setText(user.getName());
			passwordPasswordField.setText(user.getPassword());
		} else {
			nameTextField.setText("");
			passwordPasswordField.setText("");			
		}
	}
	
	@FXML
	private void handleAdd() {
		final User user = updateUser(null);
		if(null != user) {
			userList.add(user);
			setSelectedUser(user);
			FXCollections.sort(userList);
		}
	}	
	
	private void handleUpdate() {
		final User user = getSelectedUser();
		final User updatedUser = updateUser(user);
		if(null != updatedUser) {
			userList.remove(user);
			userList.add(updatedUser);
			FXCollections.sort(userList);
			setSelectedUser(updatedUser);
		}
	}

	private void handleDuplicate() {		
		final User user = getSelectedUser().duplicate();
		log.debug("Duplicate User {}",user);
		final User updatedUser = updateUser(user);
		if(null != updatedUser) {
			userList.add(updatedUser);
			FXCollections.sort(userList);
			setSelectedUser(updatedUser);
		}
	}
	
	private void handleDelete() {
		
		Task<Void> task = new Task<Void>() {
			private final User user = getSelectedUser();
			@Override
			protected Void call() {
				appData.getServerConnector().deleteUser(user.getId());
				return null;
			}
			@Override
			protected void succeeded() {
				log.debug("deleteUser() Succeeded");
				userList.remove(user);
				setSelectedUser(null);
			}
			@Override
			protected void failed() {
				final Throwable throwable = getException();
				log.debug("deleteUser() Failed {}",throwable);
				final Alert alert;
				if(throwable instanceof EntityInUseException) {
					alert = new ExceptionAlert(AlertType.INFORMATION,ERROR_TEXT_USER_IN_USE,throwable);
				} else if(throwable instanceof EntityNotFoundException) {
					alert = new ExceptionAlert(AlertType.ERROR,ERROR_TEXT_USER_NOT_FOUND,throwable);
				} else {
					log.catching(throwable);
					alert = new ExceptionAlert(AlertType.ERROR,UiConst.UNKNOWN_EXCEPTION,throwable);
				}
				alert.showAndWait();
			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Starting deleteUser() Task {}",thread);
		thread.start();
	}
	
	@FXML
	private void handleBack() {
		log.debug("handleBack");
		close();
	}
	

	/**************************************************************************
	 * Utilities
	 **************************************************************************/
	private User updateUser(final User user) {
		log.debug("Update user {}",user);
		
		UserUpdateDialog dialog = new UserUpdateDialog(getStage(),user);
		dialog.getStage().showAndWait();
		
		if(dialog.wasSaved()) {
			final User updatedUser = dialog.getUser();
			return updatedUser;
		}
		
		return null;		
	}

	
	private ContextMenu createContextMenu() {
		
		final ContextMenu menu = new ContextMenu();
		
		MenuItem item = new MenuItem(UiConst.CONTEXT_MENU_TEXT_UPDATE);
		item.setOnAction((event) -> handleUpdate());
		menu.getItems().add(item);
		item = new MenuItem(UiConst.CONTEXT_MENU_TEXT_DUPLICATE);
		item.setOnAction((event) -> handleDuplicate());
		menu.getItems().add(item);		
		item = new MenuItem(UiConst.CONTEXT_MENU_TEXT_DELETE);
		item.setOnAction((event) -> handleDelete());
		menu.getItems().add(item);
		
		return menu;
	}
	
	private void close() {
		log.debug("close()");
		getStage().close();
	}
	
}
