package com.lunarsky.minipos.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.Role;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RoleOverviewDialog extends BorderPane {
	private static final Logger log = LogManager.getLogger();
	
	private static final String WINDOW_TITLE = "Roles";
	
	private final AppData appData;
	private final Stage stage;
	
	private ObservableList<Role> roleList;
	private Role selectedRole;
	private ChangeListener<Role> selectedItemChangeListener;
	
	
	@FXML
	private ListView<Role> roleListView;
	@FXML
	private TextField nameTextField;
	@FXML
	private CheckBox canVoidCheckBox;
	@FXML
	private CheckBox canManageUsersCheckBox;
	@FXML
	private Button editButton;
	@FXML
	private Button duplicateButton;
	@FXML
	private Button deleteButton;
	
	
	public RoleOverviewDialog(final AppData appData, final Stage parentStage) {
		assert(null!=appData);
		assert(null!=parentStage);
		
		this.appData = appData;
		
		stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setTitle(WINDOW_TITLE); 
		
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RoleOverviewDialog.fxml"));
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
		
		roleListView.setCellFactory((listCell) -> {
			ListCell<Role> cell = new ListCell<Role>() {
				@Override
				protected void updateItem(Role role, boolean empty) {
					super.updateItem(role,empty);
					if(null!=role) {
						setText(role.getName());
					} else {
						setText("");
					}
				}
			};
			return cell;
		});
		
	}
	
	private void initializeBindings() {
		editButton.disableProperty().bind(roleListView.getSelectionModel().selectedItemProperty().isNull());
		duplicateButton.disableProperty().bind(roleListView.getSelectionModel().selectedItemProperty().isNull());
		deleteButton.disableProperty().bind(roleListView.getSelectionModel().selectedItemProperty().isNull());
		
	}
	
	private void initializeListeners() {
		
		getStage().setOnCloseRequest((event) -> { close(); });
		
		selectedItemChangeListener = new ChangeListener<Role>() {
			@Override
			public void changed(ObservableValue<? extends Role> observable, Role oldValue, Role newValue) {
				setSelectedRole(newValue);
			}
		};
		
		roleListView.getSelectionModel().selectedItemProperty().addListener(selectedItemChangeListener);
		
	}
	
	private void releaseListeners() {
		roleListView.getSelectionModel().selectedItemProperty().removeListener(selectedItemChangeListener);		
	}
	
	private void releaseBindings() {
		editButton.disableProperty().unbind();
		duplicateButton.disableProperty().unbind();
		deleteButton.disableProperty().unbind();
	}
	
	private void initializeAsync() {
		Task<List<Role>> task = new Task<List<Role>>() {
			@Override
			protected List<Role> call() {
				List<Role> roles = appData.getServerConnector().getRoles();
				return roles;
			}
			@Override
			protected void succeeded() {
				log.debug("GetRoles() Succeeded");
				roleList = FXCollections.observableList(getValue());
				Collections.sort(roleList);
				roleListView.setItems(roleList);
			}
			@Override
			protected void failed() {
				log.error("GetRoles() Failed");
				final Throwable throwable = getException();
				log.catching(Level.ERROR,throwable);
				ExceptionDialog.create(AlertType.ERROR,"Could not retrieve Roles",throwable);

			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Starting GetRoles() Task {}",thread);
		thread.start();		
	}
	
	@FXML
	private void handleAdd() {
		updateRole(null);
	}	
	
	@FXML
	private void handleEdit() {
		final Role role = getSelectedRole();
		updateRole(role);
	}

	@FXML
	private void handleDuplicate() {		
		final Role role = getSelectedRole().duplicate();
		log.debug("Duplicate Role {}",role);
		updateRole(role);
	}
	
	private void updateRole(final Role role) {
		log.debug("Update role {}",role);
		
		RoleUpdateDialog dialog = null;
		try {
			dialog = new RoleUpdateDialog(appData,getStage(),role);
		} catch (Exception e) {
			log.catching(Level.ERROR, e);
			ExceptionDialog.create(AlertType.ERROR, ErrorMessage.ERROR_CREATING_DIALOG_TEXT, e).show();
		}
		
		dialog.showAndWait();
		final Role updatedRole = dialog.getRole();
		
		//The role was updated
		if(updatedRole != role) {
			roleList.remove(role);
			roleList.add(updatedRole);
			FXCollections.sort(roleList);
			roleListView.getSelectionModel().select(updatedRole);
		}
	}

	@FXML
	private void handleDelete() {

		Task<Void> task = new Task<Void>() {
			private final Role role = getSelectedRole();
			@Override
			protected Void call() throws EntityNotFoundException {
				appData.getServerConnector().deleteRole(role.getId());
				return null;
			}
			@Override
			protected void succeeded() {
				log.debug("DeleteRole() Succeeded");
				roleList.remove(role);
			}
			@Override
			protected void failed() {
				final Throwable t = getException();
				log.error("DeleteRole() Failed");
				log.catching(Level.ERROR, t);
				ExceptionDialog.create(AlertType.ERROR, "Could not Delete Role", t).show();
			}
			
		};
		
		//roleListView.getSelectionModel().select(null);
		roleListView.getSelectionModel().clearSelection();
		
		Thread thread = new Thread(task);
		log.debug("Starting DeleteRole() Task {}",thread);
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
	
	private void setSelectedRole(Role role) {
		this.selectedRole = role;
		if(null==role) {
			nameTextField.setText("");
			canVoidCheckBox.setSelected(false);
			canManageUsersCheckBox.setSelected(false);						
		} else {
			nameTextField.setText(role.getName());
			canVoidCheckBox.setSelected(role.getCanVoid());
			canManageUsersCheckBox.setSelected(role.getCanManageUsers());			
		}
	}

	private Role getSelectedRole() {
		return selectedRole;
	}
	
	private Stage getStage() {
		assert(null!=stage);
		return stage;
	}
	
}
