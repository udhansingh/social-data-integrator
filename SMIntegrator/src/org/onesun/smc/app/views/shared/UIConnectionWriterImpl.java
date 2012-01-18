package org.onesun.smc.app.views.shared;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.onesun.smc.api.Connector;
import org.onesun.smc.api.ConnectionsFactory;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;

public class UIConnectionWriterImpl implements UIConnectionWriter {
	@Override
	public void saveToFile(Connector connection) {
		try {
			connection.save(AppCommons.PATH_TO_CONNECTIONS);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		updateTreeView(connection);
	}

	private void updateTreeView(Connector connection) {
		JTree tree = AppCommonsUI.CONNECTION_MANAGER_TREE_VIEW.getTree();
		
		TreePath nodePath = tree.getSelectionPath();
		TreePath parentPath = nodePath.getParentPath();
		
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)parentPath.getLastPathComponent();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)nodePath.getLastPathComponent();
		DefaultTreeModel treeModel = (DefaultTreeModel)tree.getModel();
		
		if(parent != null){
			Object object = node.getUserObject();
			
			if(object instanceof Connector){
				// Check if the connection exists by that name
				Connector existing = (Connector)object;
				boolean isRemoveable = false;

				if(existing != null){
					if(existing.getName().compareToIgnoreCase(connection.getName()) == 0){
						isRemoveable = true;
					}
				}

				// Remove/update existing connection
				if(isRemoveable == true){
					treeModel.removeNodeFromParent(node);
				}

				// Add a new connection
				node = new DefaultMutableTreeNode(connection);
				treeModel.insertNodeInto(node, parent, parent.getChildCount());

				tree.setSelectionPath(new TreePath(node.getPath()));
			}
			else {
				// new creation; add to the current node
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(connection);
				
				treeModel.insertNodeInto(newNode, node, node.getChildCount());
				tree.setSelectionPath(new TreePath(node.getPath()));
			}
		}
		
		
		tree.revalidate();
		tree.invalidate();
		
		ConnectionsFactory.addConnection(connection);
	}
}
