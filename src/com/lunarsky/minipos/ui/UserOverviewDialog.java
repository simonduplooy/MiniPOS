package com.lunarsky.minipos.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	private final AppData appData;
	private ObservableList<User> userList;
	private User selectedUser;
	private WeakChangeListener selectedUserChangeListener;
	
	
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
		
		selectedUser = new User("","");
		
		final ChangeListener<User> changeListener = new ChangeListener<User>() {
			@Override
			public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
				setSelectedUser(newValue);
			}
		};
		
		selectedUserChangeListener = new WeakChangeListener<User>(changeListener);
	}
	
	private void initializeControls() {
		
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
					}
				}
			};
			return cell;
		});
		
	}
	
	private void initializeBindings() {
		nameTextField.textProperty().bind(selectedUser.nameProperty());
		passwordPasswordField.textProperty().bind(selectedUser.passwordProperty());
	}
	
	private void initializeListeners() {
		
		getStage().setOnCloseRequest((event) -> { close(); });
		
		userListView.getSelectionModel().selectedItemProperty().addListener(getSelectedUserChangeListener());
		
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
	
	private void setSelectedUser(final User user) {
		assert(null != user);
		log.debug("setSelectedUser() {}",user);
		selectedUser.set(user);
	}

	private User getSelectedUser() {
		assert(null != selectedUser);
		return selectedUser;
	}
	
	private List<User> getUserList() {
		assert(null != userList);
		return userList;
	}
	
	private WeakChangeListener<User> getSelectedUserChangeListener() {
		assert(null != selectedUserChangeListener);
		return selectedUserChangeListener;
	}
	
	/**************************************************************************
	 * Event Handlers
	 **************************************************************************/

	@FXML
	private void handleAdd() {
		final User user = updateUser(null);
		getUserList().add(user);
	}	
	
	private void handleUpdate() {
		final User user = getSelectedUser();
		final User updatedUser = updateUser(user);
		if(null != updatedUser) {
			user.set(updatedUser);
			//TODO the list should be updated
		}
	}

	private void handleDuplicate() {		
		final User user = getSelectedUser().duplicate();
		log.debug("Duplicate User {}",user);
		updateUser(user);
	}
	
	private void handleDelete() {
		
		final User user = getSelectedUser();
		log.debug("handleDelete() {}",user);
		
		
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
				userListView.getSelectionModel().select(null);
			}
			@Override
			protected void failed() {
				final Throwable throwable = getException();
				log.error("deleteUser() Failed");
				log.catching(throwable);
				final Alert alert = new ExceptionAlert(AlertType.ERROR,ERROR_TEXT_USER_NOT_FOUND,throwable);
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
	 * Event Handlers
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

	/**************************************************************************
	 * Utilities
	 **************************************************************************/
	
	private ContextMenu createContextMenu() {
		//TODO UI TEXT SHOULD BE STATIC
		final ContextMenu menu = new ContextMenu();
		
		MenuItem item = new MenuItem("Update");
		item.setOnAction((event) -> handleUpdate());
		menu.getItems().add(item);
		item = new MenuItem("Duplicate");
		item.setOnAction((event) -> handleDuplicate());
		menu.getItems().add(item);		
		item = new MenuItem("Delete");
		item.setOnAction((event) -> handleDelete());
		menu.getItems().add(item);
		
		return menu;
	}
	
	private void close() {
		log.debug("close()");
		getStage().close();
	}
	
}
