/*
Copyright 2011 Udayakumar Dhansingh (Udy)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package org.onesun.smc.app.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;
import org.onesun.commons.swing.JTreeUtils;
import org.onesun.smc.api.ConnectionsFactory;
import org.onesun.smc.api.Connector;
import org.onesun.smc.api.ConnectorPanel;
import org.onesun.smc.api.ConnectorView;
import org.onesun.smc.api.ConnectorViewsFactory;
import org.onesun.smc.api.ProviderFactory;
import org.onesun.smc.api.ServiceProvider;
import org.onesun.smc.api.SocialMediaProvider;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.app.AppIcons;
import org.onesun.smc.app.views.connectors.SocialMediaConnectorView;
import org.onesun.smc.core.connectors.SocialMediaConnector;
import org.onesun.smc.core.model.Authentication;


public class ConnectionsView extends JPanel {
	private static Logger logger = Logger.getLogger(ConnectionsView.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5836711108052102194L;

	private ConnectionsView rootPanel = this;

	private JPanel container = new JPanel(new BorderLayout(5, 5));

	private static final String ROOT_NODE_LABEL = "Connections";

	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(ROOT_NODE_LABEL);

	private DefaultTreeModel treeModel = new DefaultTreeModel(rootNode){
		private static final long serialVersionUID = 1697210829026982959L;

		@Override 
		public boolean isLeaf(Object node){
			boolean status = super.isLeaf(node);

			Object uo = null;

			if(node != null && node instanceof DefaultMutableTreeNode){
				uo = ((DefaultMutableTreeNode)node).getUserObject();
			}

			if(uo != null && uo instanceof ServiceProvider){
				status = false;
			}

			return status;
		}
	};

	private JTree tree = new JTree(treeModel);
	private ControlBar controlBar = new ControlBar(); 
	private TreeSelectionListener listener = null;
	private JScrollPane treeScrollPane = new JScrollPane(tree);

	class NodeRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 5056816853541487185L;

		public Component getTreeCellRendererComponent(
				JTree tree,
				Object value,
				boolean sel,
				boolean expanded,
				boolean leaf,
				int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(
					tree, value, sel,
					expanded, leaf, row,
					hasFocus);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
			Object object = node.getUserObject();

			if (leaf) {
				if(object instanceof SocialMediaConnector){
					SocialMediaConnector c = (SocialMediaConnector)object;
					setText(c.getName());

					ImageIcon icon = AppIcons.getIcon(c.getIdentity().toLowerCase());
					if(icon != null){
						setIcon(icon);
					}
					else {
						logger.info("Cannot get icon for SocialMediaConnector: " + c.getIdentity());
					}
				}
				// Inherited objects before the parent
				else if(object instanceof Connector){
					Connector c = (Connector)object;
					setText(c.getName());

					ImageIcon icon = AppIcons.getIcon(c.getIdentity().toLowerCase());
					if(icon != null){
						setIcon(icon);
					}
					else {
						logger.info("Cannot get icon for other Connector: " + c.getIdentity());
					}
				}
				else if(object instanceof ServiceProvider){
					ServiceProvider p = (ServiceProvider)object;

					if(p != null){
						String name = p.getIdentity();
						setText(name);

						ImageIcon icon = AppIcons.getIcon(name.toLowerCase());
						if(icon != null){
							setIcon(icon);
						}
						else {
							logger.info("Cannot get icon for ServiceProvider: " + name);
						}
					}
				}
			} else {
				if(object instanceof ServiceProvider){
					ServiceProvider p = (ServiceProvider)object;

					if(p != null){
						setText(p.getIdentity());
					}
				}
			}

			return this;
		}
	}

	public ConnectionsView(){
		this.setLayout(new BorderLayout(5, 5));

		initModel();

		createControls();

		JTreeUtils.expandAll(tree, true);
	}

	private class ConnectionTreeSelectionListener implements TreeSelectionListener{
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
					tree.getLastSelectedPathComponent();

			if (node == null) {
				return;
			}

			Object object = node.getUserObject();
			container.removeAll();

			controlBar.getAddButton().setEnabled(false);
			controlBar.getDeleteButton().setEnabled(false);

			Connector c = null;

			if (object != null) {
				if(object instanceof Connector){
					c = (Connector)object;
				}
				else if(object instanceof ServiceProvider){
					controlBar.getAddButton().setEnabled(true);
				}
			}

			String connectionName = null;
			if(c != null){
				connectionName = c.getName();
				AppCommonsUI.MAIN_WINDOW.setTitle(AppCommons.APPLICATION_TITLE + " - " + connectionName);
			}
			else {
				AppCommonsUI.MAIN_WINDOW.setTitle(AppCommons.APPLICATION_TITLE);
			}

			renderConnectionDetailView(connectionName);

			container.validate();

			rootPanel.revalidate();
			rootPanel.repaint();
		}

		private void renderConnectionDetailView(String connectionName){
			AppCommons.BUSINESS_OBJECT.reset();

			Connector connection = ConnectionsFactory.getConnectionByName(connectionName);
			AppCommons.BUSINESS_OBJECT.setConnection(connection);
			AppCommons.AUTHENTICATION = null;

			if(connection == null){
				container.add(ConnectorViewsFactory.DEFAULT_CONNECTION_VIEW, BorderLayout.CENTER);
			}
			else {
				String category = connection.getCategory();
				Authentication authentication = connection.getAuthentication();
				AppCommons.AUTHENTICATION = authentication;
				JPanel panel = null;

				controlBar.getDeleteButton().setEnabled(true);

				if(category != null && category.trim().length() > 0){
					ConnectorView view = ConnectorViewsFactory.getViewByCategory(category);

					if(view != null){
						view.init();
						panel = view.getView();
						view.updateFields(connection);
					}
				}

				if(panel != null){
					container.add(panel, BorderLayout.CENTER);
				}
			}

			// Update the meta-model
			AppCommons.BUSINESS_OBJECT.setConnection(connection);
			AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.BUSINESS_OBJECT.toJSON());
		}
	}

	private void createControls() {
		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(controlBar, BorderLayout.NORTH);
		containerPanel.add(container, BorderLayout.CENTER);
		this.add(containerPanel, BorderLayout.CENTER);

		container.add(ConnectorViewsFactory.DEFAULT_CONNECTION_VIEW, BorderLayout.CENTER);

		JPanel mainPanel = new JPanel(new BorderLayout(5,5));

		treeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		treeScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		treeScrollPane.setPreferredSize(new Dimension(300, 300));

		mainPanel.add(treeScrollPane, BorderLayout.CENTER);

		this.add(mainPanel, BorderLayout.WEST);

		listener = new ConnectionTreeSelectionListener();

		tree.setCellRenderer(new NodeRenderer());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(listener);
		tree.setAutoscrolls(true);
	}

	private void initModel(){
		Map<String, Connector> connections = ConnectionsFactory.getConnections();

		for(String providerName :  ProviderFactory.getNames()){
			// Skip empty/null names
			if(providerName == null || (providerName != null && providerName.trim().length() == 0)){
				continue;
			}

			ServiceProvider serviceProvider = ProviderFactory.getProvider(providerName);

			if(serviceProvider == null) continue;

			DefaultMutableTreeNode folder = new DefaultMutableTreeNode(serviceProvider);
			rootNode.add(folder);

			String category = serviceProvider.getCategory();

			// connections by provider
			for(String connectionName : connections.keySet()){
				Connector connection = connections.get(connectionName);

				if(connection.getCategory() == category){
					if(serviceProvider.getIdentity().compareToIgnoreCase(connection.getIdentity()) == 0){
						DefaultMutableTreeNode node = new DefaultMutableTreeNode(connection);
						folder.add(node);
					}
				}
			}
		}
	}

	public JTree getTree() {
		return tree;
	}

	public class ControlBar extends JToolBar {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8483370091383807403L;
		private JButton deleteButton = null;
		private JButton addButton = null;

		public JButton getAddButton(){
			return addButton;
		}

		public JButton getDeleteButton(){
			return deleteButton;
		}

		public ControlBar(){
			super(JToolBar.HORIZONTAL);

			this.setFloatable(false);
			this.putClientProperty("JToolBar.isRollover", Boolean.TRUE);

			createControls();
		}

		private void createControls(){
			ImageIcon addIcon = AppIcons.getIcon("add");
			addButton = new JButton(addIcon); 
			addButton.setEnabled(false);
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							tree.getLastSelectedPathComponent();

					if (node == null) {
						return;
					}

					Object object = node.getUserObject();

					ConnectorView view = null;
					if(object != null){
						if(object instanceof SocialMediaProvider){
							view = ConnectorViewsFactory.newConnectorViewByName("Social Media");
							
							SocialMediaProvider p = (SocialMediaProvider)object;

							if(view instanceof SocialMediaConnectorView){
								SocialMediaConnectorView v = (SocialMediaConnectorView)view;
								v.init();
								
								// fill up provider names
								List<String> scopeList = p.getApiScopeList();
								if(scopeList != null){
									DefaultListModel<String> model = new DefaultListModel<String>();
									
									for(String text : scopeList){
										model.addElement(text);
									}
									
									v.getApiScopeList().setModel(model);
								}
								
								JComboBox<String> cb = v.getIdentityComboBox();
								cb.setSelectedItem(p.getIdentity());
							}
						}
						else if(object instanceof ServiceProvider){
							ServiceProvider p = (ServiceProvider)object;
							view = ConnectorViewsFactory.newConnectorViewByCategory(p.getCategory());
							view.init();
						}

						
						container.removeAll();
						if(view != null){
							Connector connector = AppCommons.BUSINESS_OBJECT.getConnection();
							if(connector != null){
								view.updateFields(connector);
							}
							
							ConnectorPanel v = view.getView();
							if(v != null){
								container.add(v, BorderLayout.CENTER);
							}
						}
						container.validate();

						rootPanel.revalidate();
						rootPanel.repaint();
					}
				}
			});

			ImageIcon deleteIcon = AppIcons.getIcon("delete");
			deleteButton = new JButton(deleteIcon); 
			deleteButton.setEnabled(false);
			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							tree.getLastSelectedPathComponent();

					if (node == null) {
						return;
					}

					Object object = node.getUserObject();
					String connectionName = null;

					if(object != null && object instanceof Connector) {
						Connector c = (Connector)object;
						connectionName = c.getName();
					}

					if(connectionName != null){
						Connector connection = ConnectionsFactory.getConnectionByName(connectionName);

						container.removeAll();

						ConnectionsFactory.deleteConnection(connection, AppCommons.PATH_TO_CONNECTIONS);
						treeModel.removeNodeFromParent(node);

						container.validate();

						rootPanel.revalidate();
						rootPanel.repaint();
					}
				}
			});

			this.add(addButton);
			this.add(deleteButton);
		}
	}
}
