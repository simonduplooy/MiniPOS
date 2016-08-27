package com.lunarsky.minipos.ui;

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
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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

	private RoleDTO role;
	
	// role can be null to create a new Role
	public RoleUpdateDialog(Stage parentStage, RoleDTO role) {
		assert(null != parentStage);
		
		this.appData = AppData.getInstance();
		this.role = role;

		final String title = (null == role) ? WINDOW_TITLE_ADD_ROLE : WINDOW_TITLE_UPDATE_ROLE;
		final Stage stage = UiUtil.createDialogStage(parentStage,title); 
		final Scene scene = new Scene(this);
		stage.setScene(scene);
		UiUtil.loadRootConstructNode(this,"RoleUpdateDialog.fxml");
	}
	
	public Stage getStage() {
		return (Stage)getScene().getWindow();
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
	
}
