package org.onesun.sdi.swing.app;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

import org.onesun.commons.swing.MainWindow;
import org.onesun.sdi.core.model.Tasklet;
import org.onesun.sdi.core.resources.RESTResource;
import org.onesun.sdi.core.resources.StreamingResource;
import org.onesun.sdi.core.resources.WebResource;
import org.onesun.sdi.swing.app.model.DatasetModel;
import org.onesun.sdi.swing.app.views.ConnectionsView;

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
