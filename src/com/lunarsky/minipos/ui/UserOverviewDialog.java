package com.lunarsky.minipos.ui;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.User;
import com.lunarsky.minipos.util.ErrorMessage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UserOverviewDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Users";
	
	private final AppData appData;
	private final Stage stage;
	
	private ObservableList<User> userList;
	private User selectedUser;
	private ChangeListener<User> selectedItemChangeListener;
	
	
	@FXML
	private ListView<User> userListView;
	@FXML
	private TextField nameTextField;
	@FXML
	private PasswordField passwordPasswordField;
	@FXML
	private TextField roleTextField;
	@FXML
	private Button editButton;
	@FXML
	private Button duplicateButton;
	@FXML
	private Button deleteButton;
	
	
	public UserOverviewDialog(final AppData appData, final Stage parentStage) {
		assert(null != appData);
		assert(null != parentStage);
		
		this.appData = appData;
		
		stage = new Stage();
		//stage.initOwner(parentStage);
		//stage.initModality(Modality.WINDOW_MODAL);
		stage.setTitle(WINDOW_TITLE); 
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserOverviewDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
        	loader.load();
        } catch (IOException e) {
        	throw new RuntimeException(e);
        }
		
		Scene scene = new Scene(this);
		stage.setScene(scene);
	}
	
	public void showAndWait() {
		getStage().showAndWait();
	}
	
	@FXML
	private void initialize() {
		
		initializeControls();
		initializeBindings();
		initializeListeners();
		initializeAsync();
		
	}

	private void initializeControls() {
		
		userListView.setCellFactory((listCell) -> {
			ListCell<User> cell = new ListCell<User>() {
				@Override
				protected void updateItem(User user, boolean empty) {
					super.updateItem(user,empty);
					if(null!=user) {
						setText(user.getName());
					} else {
						setText("");
					}
				}
			};
			return cell;
		});
		
	}
	
	private void initializeBindings() {
		editButton.disableProperty().bind(userListView.getSelectionModel().selectedItemProperty().isNull());
		duplicateButton.disableProperty().bind(userListView.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.disableProperty().bind(userListView.getSelectionModel().selectedItemProperty().isNull());
	}
	
	private void initializeListeners() {
		
		getStage().setOnCloseRequest((event) -> { close(); });
		
		selectedItemChangeListener = new ChangeListener<User>() {
			@Override
			public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
				setSelectedUser(newValue);
			}
		};
		
		userListView.getSelectionModel().selectedItemProperty().addListener(selectedItemChangeListener);
		
	}
	
	private void releaseListeners() {
		userListView.getSelectionModel().selectedItemProperty().removeListener(selectedItemChangeListener);		
	}
	
	private void releaseBindings() {
		editButton.disableProperty().unbind();
		duplicateButton.disableProperty().unbind();
		deleteButton.disableProperty().unbind();
	}
	
	private void initializeAsync() {
		Task<List<User>> task = new Task<List<User>>() {
			@Override
			protected List<User> call() {
				final List<User> users = appData.getServerConnector().getUsers();
				return users;
			}
			@Override
			protected void succeeded() {
				log.debug("GetUsers() Succeeded");
				final List<User> list = getValue();
				
				//TODO REMOVE
				for(User user: list) {
					log.debug("Adding User: {}",user);
				}
				
				userList = FXCollections.observableArrayList(list);
				Collections.sort(userList);
				userListView.setItems(userList);
			}
			@Override 
			protected void failed() {
				log.error("GetUsers() Failed");
				final Throwable throwable = getException();
				log.catching(Level.ERROR,throwable);
				ExceptionDialog.create(AlertType.ERROR,"Could not retrieve Users",throwable);

			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Starting GetUsers() Task {}",thread);
		thread.start();		
	}
	
	@FXML
	private void handleAdd() {
		updateUser(null);
	}	
	
	@FXML
	private void handleEdit() {
		final User user = getSelectedUser();
		updateUser(user);
	}

	@FXML
	private void handleDuplicate() {		
		final User user = getSelectedUser().duplicate();
		log.debug("Duplicate User {}",user);
		updateUser(user);
	}
	
	private void updateUser(final User user) {
		log.debug("Update user {}",user);
		
		UserUpdateDialog dialog = new UserUpdateDialog(appData,getStage(),user);
		dialog.showAndWait();
		
		final User updatedUser = dialog.getUser();
	
		//The user was updated
		if(updatedUser != user) {
			userList.remove(user);
			userList.add(updatedUser);
			FXCollections.sort(userList);
			log.debug("Updated User ");
			userListView.getSelectionModel().select(updatedUser);
		}

	}

	@FXML
	private void handleDelete() {

		Task<Void> task = new Task<Void>() {
			private final User user = getSelectedUser();
			@Override
			protected Void call() throws EntityNotFoundException {
				appData.getServerConnector().deleteUser(user.getId());
				return null;
			}
			@Override
			protected void succeeded() {
				log.debug("DeleteUser() Succeeded");
				userList.remove(user);
			}
			@Override
			protected void failed() {
				final Throwable t = getException();
				log.error("DeleteUser() Failed");
				log.catching(Level.ERROR, t);
				ExceptionDialog.create(AlertType.ERROR, "Could not Delete User", t).show();
			}
			
		};
		
		userListView.getSelectionModel().select(null);
		
		Thread thread = new Thread(task);
		log.debug("Starting DeleteUser() Task {}",thread);
		thread.start();
	}
	
	@FXML
	private void handleOK() {
		log.debug("Closing Dialog");
		getStage().close();
	}
	
	private void close() {
		
		releaseBindings();
		releaseListeners();
		
	}
	
	private void setSelectedUser(User user) {
	
 		this.selectedUser = user;
		if(null==user) {
			nameTextField.setText("");
			passwordPasswordField.setText("");
			roleTextField.setText("");					
			
		} else {
			nameTextField.setText(user.getName());
			passwordPasswordField.setText(user.getPassword());
			roleTextField.setText(user.getRole().getName());
		}
	}

	private User getSelectedUser() {
		return selectedUser;
	}
	
	private Stage getStage() {
		assert(null!=stage);
		return stage;
	}
	
}
