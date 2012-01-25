package org.onesun.smc.app;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

import org.onesun.commons.swing.MainWindow;
import org.onesun.smc.app.model.DatasetModel;
import org.onesun.smc.app.views.ConnectionsView;
import org.onesun.smc.core.model.Tasklet;
import org.onesun.smc.core.resources.RESTResource;
import org.onesun.smc.core.resources.StreamingResource;
import org.onesun.smc.core.resources.WebResource;

public class AppCommonsUI {
	public static MainWindow 								MAIN_WINDOW							= new MainWindow(AppCommons.APPLICATION_TITLE, null);
	public static ConnectionsView 							CONNECTION_MANAGER_TREE_VIEW		= null;

	public static DefaultComboBoxModel<RESTResource> 		REST_RESOURCE_COMBOBOX_MODEL 		= new DefaultComboBoxModel<RESTResource>();
	public static DefaultComboBoxModel<WebResource> 		WEB_RESOURCE_COMBOBOX_MODEL 		= new DefaultComboBoxModel<WebResource>();
	public static DefaultComboBoxModel<StreamingResource> 	STREAMING_RESOURCE_COMBOBOX_MODEL 	= new DefaultComboBoxModel<StreamingResource>();
	public static DatasetModel 								PREVIEW_DATASET_MODEL 				= null;
	
	public static DefaultListModel<Tasklet> 				TASKLETS_MODEL						= new DefaultListModel<Tasklet>();
	
	public static JTextArea 								MODEL_TEXTAREA						= new JTextArea();
	
	static {
		MODEL_TEXTAREA.setEditable(false);
	}
}
