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
package org.onesun.smc.app.views.connection.properties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.smc.api.ConnectionProperties;
import org.onesun.smc.api.ConnectionPropertiesPanel;
import org.onesun.smc.api.ProviderFactory;
import org.onesun.smc.api.ServiceProvider;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppMessages;
import org.onesun.smc.core.connection.properties.DataSiftConnectionProperties;

public class DataSiftConnectionPropertiesView extends ConnectionPropertiesPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8926936146403943618L;

	private JTextField usernameTextField = new JTextField();

	private JTextField apiKeyTextField = new JTextField();

	private JTextField streamHashTextField = new JTextField();

	private JTextField streamConsumerTypeTextField = new JTextField();

	public DataSiftConnectionPropertiesView(){
		super();
		onInitComplete();
	}
	
	@Override
	public void init(){
		List<ServiceProvider> providers = ProviderFactory.getProviderNamesByCategory("DATASIFT");
		
		for(ServiceProvider provider : providers){
			if(providers.size() == 1){
				identityComboBox.addItem(provider.getIdentity());
			}
		}
	}

	@Override
	protected void initControls(){
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

		if(saveButton != null) {
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String connectionName = connectionNameTextField.getText();
					String username = usernameTextField.getText();
					String apiKey = apiKeyTextField.getText();
					String streamHash = streamHashTextField.getText();
					String streamConsumerType = streamConsumerTypeTextField.getText();

					try {
						if((connectionName == null && username == null) || connectionName.isEmpty()){
							JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_INVALID_CONNECTION);
							return;
						}
						else {
							DataSiftConnectionProperties connection = new DataSiftConnectionProperties();
							connection.setName(connectionName);
							connection.setUsername(username);
							connection.setApiKey(apiKey);
							connection.setStreamHash(streamHash);
							connection.setStreamConsumerType(streamConsumerType);
							connection.setIdentity("DataSift");

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

		final JPanel panel = new JPanel(new SpringLayout());
		JLabel label = null;

		label = new JLabel("Connection Name", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		connectionNameTextField.setPreferredSize(new Dimension(400, 24));
		label.setLabelFor(connectionNameTextField);
		panel.add(connectionNameTextField);

		label = new JLabel("Service", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		identityComboBox.setPreferredSize(new Dimension(500, 24));
		label.setLabelFor(identityComboBox);
		panel.add(identityComboBox);

		label = new JLabel("Username", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		usernameTextField.setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(usernameTextField);
		panel.add(usernameTextField);

		label = new JLabel("Api Key", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		apiKeyTextField.setPreferredSize(new Dimension(100, 24));
		label.setLabelFor(apiKeyTextField);
		panel.add(apiKeyTextField);

		label = new JLabel("Stream Hash", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		streamHashTextField.setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(streamHashTextField);
		panel.add(streamHashTextField);

		label = new JLabel("Stream Consumer Type", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		streamConsumerTypeTextField.setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(streamConsumerTypeTextField);
		panel.add(streamConsumerTypeTextField);

		int rows = 12;
		if(overrideSaveButton == true){
			JPanel buttonPanel = new JPanel(new BorderLayout(15, 15));
			buttonPanel.add(saveButton, BorderLayout.EAST);
			panel.add(buttonPanel);

			rows = 13;
		}

		SpringLayoutUtils.makeCompactGrid(panel, rows, 1, 5, 5,	5, 5);

		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(panel, BorderLayout.NORTH);

		this.add(containerPanel);
	}

	@Override
	public void updateFields(ConnectionProperties connector) {
		if(connector instanceof DataSiftConnectionProperties){
			DataSiftConnectionProperties connection = (DataSiftConnectionProperties)connector;

			final String provider = connection.getIdentity();
			if(identityComboBox != null)			identityComboBox.setSelectedItem(provider);

			connectionNameTextField.setText(connection.getName());
			usernameTextField.setText(connection.getUsername());
			apiKeyTextField.setText(connection.getApiKey());
			streamHashTextField.setText(connection.getStreamHash());
			streamConsumerTypeTextField.setText(connection.getStreamConsumerType());
		}
	}

	public void resetFields() {
		connectionNameTextField.setText("");
		usernameTextField.setText("");
		apiKeyTextField.setText("");
		streamHashTextField.setText("");
		streamConsumerTypeTextField.setText("");
	}
}
