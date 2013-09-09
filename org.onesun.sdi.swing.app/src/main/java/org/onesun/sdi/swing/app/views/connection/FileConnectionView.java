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
package org.onesun.sdi.swing.app.views.connection;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.sdi.core.api.Connection;
import org.onesun.sdi.core.model.connection.FileSystemConnection;
import org.onesun.sdi.swing.app.AppCommons;
import org.onesun.sdi.swing.app.AppMessages;
import org.onesun.sdi.swing.app.api.ConnectionViewPanel;

public class FileConnectionView extends ConnectionViewPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8926936146403943618L;

	private JTextField pathTextField = new JTextField();

	private JButton chooseFolderButton = new JButton("Browse...");

	private JTextField filterTextField = new JTextField();

	private JFileChooser chooser = new JFileChooser();

	public FileConnectionView(){
		super();
		onInitComplete();
	}
	
	@Override
	public void init(){
		chooser.setCurrentDirectory(new java.io.File("/"));
		chooser.setDialogTitle("Select a directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	@Override
	protected void initControls(){
		final JPanel panel = new JPanel(new SpringLayout());
		JLabel label = null;

		label = new JLabel("Connection Name", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		connectionNameTextField.setPreferredSize(new Dimension(400, 24));
		label.setLabelFor(connectionNameTextField);
		panel.add(connectionNameTextField);

		label = new JLabel("Path", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		JPanel pathSetterPanel = new JPanel(new BorderLayout(5, 5));
		pathTextField.setPreferredSize(new Dimension(300, 24));
		pathSetterPanel.add(pathTextField, BorderLayout.CENTER);
		pathSetterPanel.add(chooseFolderButton, BorderLayout.EAST);
		label.setLabelFor(pathSetterPanel);
		panel.add(pathSetterPanel);

		label = new JLabel("Filter", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		filterTextField.setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(filterTextField);
		panel.add(filterTextField);

		int rows = 6;
		if(overrideSaveButton == true){
			JPanel buttonPanel = new JPanel(new BorderLayout(15, 15));
			buttonPanel.add(saveButton, BorderLayout.EAST);
			panel.add(buttonPanel);

			rows = 7;
		}

		SpringLayoutUtils.makeCompactGrid(panel, rows, 1, 5, 5,	5, 5);

		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(panel, BorderLayout.NORTH);

		this.add(containerPanel);
		
		
		connectionNameTextField.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
			}
		});

		connectionNameTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppCommons.TASKLET.getConnectionProperties().setName(connectionNameTextField.getText().trim());
			}
		});

		chooseFolderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int status = chooser.showOpenDialog(rootPanel);
				if(status == JFileChooser.APPROVE_OPTION){
					pathTextField.setText(chooser.getCurrentDirectory().getAbsolutePath());
				}
			}
		});

		if(saveButton != null) {
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String connectionName = connectionNameTextField.getText();
					String path = pathTextField.getText();
					String filter = filterTextField.getText();

					try {
						if((connectionName == null && path == null) || connectionName.isEmpty()){
							JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_INVALID_CONNECTION);
							return;
						}
						else {
							FileSystemConnection connection = new FileSystemConnection();
							connection.setName(connectionName);
							connection.setPath(path);
							connection.setFilter(filter);
							connection.setIdentity("File System");

							connectionWriter.saveToFile(connection);
						}
					}
					catch(NullPointerException npe){
						JOptionPane.showMessageDialog(rootPanel, "Could not save connection!");
					}finally {
					}
				}
			});
		}
	}

	@Override
	public void updateFields(Connection connector) {
		if(connector instanceof FileSystemConnection){
			FileSystemConnection connection = (FileSystemConnection)connector;

			connectionNameTextField.setText(connection.getName());
			pathTextField.setText(connection.getPath());

			List<String> filterList = connection.getFilter();
			if(filterList != null){
				String arrayString = filterList.toString();
				filterTextField.setText(arrayString.replace("]", "").replace("[", ""));
			}
		}
	}

	public void resetFields() {
		connectionNameTextField.setText("");
		pathTextField.setText("");
		filterTextField.setText("");		
	}
}
