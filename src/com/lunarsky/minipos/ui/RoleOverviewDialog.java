package com.lunarsky.minipos.ui;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.RoleDTO;
import com.lunarsky.minipos.util.ErrorMessage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
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
	private ObservableList<RoleDTO> roleList;
	private RoleDTO selectedRole;
	private ChangeListener<RoleDTO> selectedItemChangeListener;
	
	
	@FXML
	private ListView<RoleDTO> roleListView;
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
	
	
	public RoleOverviewDialog(final Stage parentStage) {
		assert(null!=parentStage);
		
		this.appData = AppData.getInstance();
		
		UiUtil.createDialog(parentStage,WINDOW_TITLE,this,"RoleOverviewDialog.fxml");

	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
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
			ListCell<RoleDTO> cell = new ListCell<RoleDTO>() {
				@Override
				protected void updateItem(RoleDTO role, boolean empty) {
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
		
		selectedItemChangeListener = new ChangeListener<RoleDTO>() {
			@Override
			public void changed(ObservableValue<? extends RoleDTO> observable, RoleDTO oldValue, RoleDTO newValue) {
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
		Task<List<RoleDTO>> task = new Task<List<RoleDTO>>() {
			@Override
			protected List<RoleDTO> call() {
				List<RoleDTO> roles = appData.getServerConnector().getRoles();
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
		final RoleDTO role = getSelectedRole();
		updateRole(role);
	}

	@FXML
	private void handleDuplicate() {		
		final RoleDTO role = getSelectedRole().duplicate();
		log.debug("Duplicate Role {}",role);
		updateRole(role);
	}
	
	private void updateRole(final RoleDTO role) {
		log.debug("Update role {}",role);
		
		final RoleUpdateDialog dialog = new RoleUpdateDialog(getStage(),role);
		dialog.getStage().showAndWait();
		final RoleDTO updatedRole = dialog.getRole();
		
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
			private final RoleDTO role = getSelectedRole();
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
	
	private void setSelectedRole(RoleDTO role) {
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

	private RoleDTO getSelectedRole() {
		return selectedRole;
	}
	
}
