package com.lunarsky.minipos.ui;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lunarsky.minipos.common.exception.EntityNotFoundException;
import com.lunarsky.minipos.interfaces.PersistenceId;
import com.lunarsky.minipos.model.AppData;
import com.lunarsky.minipos.model.dto.RoleDTO;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RoleUpdateDialog extends VBox {
	private static final Logger log = LogManager.getLogger();
	
	private static String WINDOW_TITLE_ADD_ROLE = "Add Role";
	private static String WINDOW_TITLE_UPDATE_ROLE = "Update Role";
	
	private final AppData appData;
	
	@FXML
	private TextField nameTextField;
	@FXML
	private CheckBox canVoidCheckBox;
	@FXML
	private CheckBox canManageUsersCheckBox;

	
	private final Stage stage;
	private RoleDTO role;
	
	// role can be null to create a new Role
	public RoleUpdateDialog(Stage parentStage, RoleDTO role) {
		assert(null != parentStage);
		
		this.appData = AppData.getInstance();
		this.role = role;

		stage = new Stage();
		stage.initOwner(parentStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setTitle((null == role) ? WINDOW_TITLE_ADD_ROLE : WINDOW_TITLE_UPDATE_ROLE); 
				
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RoleUpdateDialog.fxml"));
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
		stage.showAndWait();
	}
	
	//Can be null if canceled
	public RoleDTO getRole() {
		return role;
	}
	
	@FXML
	private void initialize() {
		initializeControls(getRole());
	}
	
	private void initializeControls(final RoleDTO role) {
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

	
	@FXML
	private void handleSave(ActionEvent event) {

		Task<RoleDTO> task = new Task<RoleDTO>() {
			final RoleDTO role = createRoleFromControls();
			@Override
			protected RoleDTO call() throws EntityNotFoundException {
				return appData.getServerConnector().saveRole(role);
			}
			@Override
			protected void succeeded() {
				setRole(getValue());
				log.debug("SaveRole() Succeeded");
				getStage().close();
			}
			@Override
			protected void failed() {
				final Throwable t = getException();
				log.debug("SaveRole() Failed");
				log.catching(Level.ERROR, t);
				ExceptionDialog.create(AlertType.ERROR, "Could not Save Role", t).show();
			}
		};
		
		Thread thread = new Thread(task);
		log.debug("Creating SaveRole() Task {}",thread);
		thread.start();
	}

	@FXML
	private void handleCancel(ActionEvent event) {
		//TODO cancel save task ?
		getStage().close();
	}
	
	private RoleDTO createRoleFromControls() {
		final PersistenceId id = (null==role)?null:role.getId();
		final String name = nameTextField.getText();
		final boolean canVoid = canVoidCheckBox.isSelected();
		final boolean canManageUsers = canManageUsersCheckBox.isSelected();
		
		final RoleDTO updatedRole = new RoleDTO(id,name,canVoid,canManageUsers);
		return updatedRole;
	}
	
	private void setRole(final RoleDTO role) {
		this.role = role;
	}
	
	private Stage getStage() {
		assert(stage!=null);
		return stage;
	}
	
}
