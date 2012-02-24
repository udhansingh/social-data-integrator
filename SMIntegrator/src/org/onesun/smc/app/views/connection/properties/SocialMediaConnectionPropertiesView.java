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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;

import org.apache.log4j.Logger;
import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.commons.swing.cursors.DefaultCusor;
import org.onesun.smc.api.ConnectionProperties;
import org.onesun.smc.api.ConnectionPropertiesPanel;
import org.onesun.smc.api.ProviderFactory;
import org.onesun.smc.api.ServiceProvider;
import org.onesun.smc.api.SocialMediaProvider;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.app.AppMessages;
import org.onesun.smc.core.connection.properties.SocialMediaConnectionProperties;
import org.onesun.smc.core.resources.RESTResource;
import org.onesun.smc.core.services.auth.Authenticator;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;

public class SocialMediaConnectionPropertiesView extends ConnectionPropertiesPanel {
	private static Logger logger = Logger.getLogger(SocialMediaConnectionPropertiesView.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 8926936146403943618L;

	private JTextField accessSecretTextField = new JTextField();

	private JTextField accessTokenTextField = new JTextField();

	private JButton apiKeyButton = new JButton("Browse...");

	private JTextField apiKeyTextField = new JTextField();
	private JList<String> apiScopeList = new JList<String>();

	private JScrollPane apiScopeScrollPane = new JScrollPane(apiScopeList);

	private JTextField apiSecretTextField = new JTextField();

	public JList<String> getApiScopeList(){
		return apiScopeList;
	}
	
	public SocialMediaConnectionPropertiesView(){
		super();
		onInitComplete();
	}
	
	@Override
	public void init(){
		// fill up provider names
		List<ServiceProvider> socialProviders = ProviderFactory.getProviderNamesByCategory("SOCIAL_MEDIA");
		
		identityComboBox.addItem("");
		for(ServiceProvider p : socialProviders){
			String identity = p.getIdentity();
			identityComboBox.addItem(identity);
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
				AppCommons.TASKLET.getConnection().setName(connectionNameTextField.getText().trim());
			}
		});

		identityComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {

				DefaultListModel<String> scopeModel = new DefaultListModel<String>();
				Object o = e.getItem();

				if(o instanceof String){
					String identity = (String)o;

					// preset UI bindings
					if(apiScopeList != null) apiScopeList.setEnabled(false);

					AppCommons.AUTHENTICATOR = null;

					final String oldText = connectionNameTextField.getText();


					AppCommonsUI.REST_RESOURCE_COMBOBOX_MODEL.removeAllElements();

					// update UI bindings
					SocialMediaProvider entry = (SocialMediaProvider)ProviderFactory.getProvider(identity);

					if(entry == null) return;

					String text = " New" + entry.getIdentity() + "Connection";

					if(oldText == null || (oldText != null && oldText.trim().length() == 0)){
						connectionNameTextField.setText(text);
					}

					apiKeyButton.setToolTipText(entry.getDeveloperUrl());

					List<RESTResource> resources = entry.getResources();

					// Add a null entry
					RESTResource nullResource = new RESTResource();
					nullResource.setVerb(null);
					AppCommonsUI.REST_RESOURCE_COMBOBOX_MODEL.addElement(nullResource);

					if(resources != null){
						for(RESTResource resource : resources){
							AppCommonsUI.REST_RESOURCE_COMBOBOX_MODEL.addElement(resource);
						}
					}

					resetFields();

					List<String> apiScopes = entry.getApiScopeList();
					if(apiScopes != null){
						// Enable it only if there are values to display
						if(apiScopeList != null) apiScopeList.setEnabled(true);

						for(String scopeItem : apiScopes){
							scopeModel.addElement(scopeItem);
						}
					}

					if(apiScopeList != null) {
						apiScopeList.setModel(scopeModel);
						apiScopeList.invalidate();
					}
				}
			}
		});

		apiKeyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String item = (String)identityComboBox.getSelectedItem();

				SocialMediaProvider entry = (SocialMediaProvider)ProviderFactory.getProvider(item.toLowerCase());

				if(entry == null) return;

				String developerReference = entry.getDeveloperUrl();

				if(developerReference != null && developerReference.length() > 0){
					try {
						Desktop.getDesktop().browse(new URI(developerReference));
					} catch (IOException ex) {
					} catch (URISyntaxException ex) {
					}
				}
				else {
					JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_PROVIDER_NOT_SELECTED);

					return;
				}
			}
		});

		apiScopeList.setVisibleRowCount(-1);
		apiScopeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		apiScopeList.setSize(new Dimension(200, 50));

		apiScopeScrollPane.setPreferredSize(new Dimension(250, 50));

		authorizeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultCusor.startWaitCursor(rootPanel);

				String providerName = ((String)identityComboBox.getSelectedItem());
				if((providerName == null) || (providerName != null) && (providerName.trim().length() == 0)){
					JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_PROVIDER_NOT_SELECTED);

					DefaultCusor.stopWaitCursor(rootPanel);
					return;
				}

				final String key = apiKeyTextField.getText();
				final String secret = apiSecretTextField.getText();

				List<String> values = apiScopeList.getSelectedValuesList();
				String scope = null;

				if(values.size() > 0){
					scope = values.toString();

					scope = scope.replace("[", "");
					scope = scope.replace("]", "");
					scope = scope.trim();
				}

				logger.info("provider: " + providerName + "\n" +
						"key: " + key + "\n" +
						"secret: " + secret + "\n" +
						"scope: " + scope);

				if((key == null || key.length() <= 0) && (secret == null || secret.length() <= 0)){

					JOptionPane.showMessageDialog(rootPanel, "You must provide Key and Secret");
					return;
				}

				SocialMediaProvider entry = (SocialMediaProvider)ProviderFactory.getProvider(providerName);
				if(entry == null) return;

				SocialMediaConnectionProperties connection = new SocialMediaConnectionProperties();
				connection.setApiKey(key);
				connection.setApiSecret(secret);
				connection.setIdentity(providerName);
				connection.toScopeList(scope);

				AppCommons.AUTHENTICATOR = new Authenticator(entry, connection, AppCommons.HTTP_CONNECTION_TIMEOUT);

				try {
					AppCommons.AUTHENTICATOR.authorize();
					Token accessToken = AppCommons.AUTHENTICATOR.getAccessToken();
					accessTokenTextField.setText(accessToken.getToken());
					accessTokenTextField.invalidate();

					accessSecretTextField.setText(accessToken.getSecret());
					accessSecretTextField.invalidate();
				}
				catch(OAuthException oae){
					String message = oae.getMessage();

					if(message.toLowerCase().contains("response body is incorrect")){
						if(providerName.compareToIgnoreCase("twitter") == 0){
							message = AppMessages.ERROR_SYSTEM_CLOCK_IS_NOT_CURRENT;
						}
						else if(providerName.compareToIgnoreCase("linkedin") == 0){
							message = AppMessages.ERROR_SYSTEM_CLOCK_IS_NOT_CURRENT;
						}
					}

					JOptionPane.showMessageDialog(rootPanel, "Problem encountered while authenticating you with '" 
							+ providerName + "'" + "\n" + message);
				}
				catch(NullPointerException npe){
					JOptionPane.showMessageDialog(rootPanel, "HTTP timeout value is too low, increase it in Applicaiton Preferences");
				}finally {
					DefaultCusor.stopWaitCursor(rootPanel);
				}
			}
		});

		accessTokenTextField.setEditable(false);

		accessSecretTextField.setEditable(false);

		if(saveButton != null) {
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int failed = 0;

					String data = null;
					SocialMediaConnectionProperties connection = new SocialMediaConnectionProperties();

					String newData = null;
					data = connectionNameTextField.getText();
					newData = (data.trim().length() > 0) ? data : null;
					if(newData == null){
						failed++;
					}
					else {
						connection.setName( newData );
					}

					data = (String)identityComboBox.getSelectedItem();
					newData = (data.trim().length() > 0) ? data : null;
					if(newData == null){
						failed++;
					}
					else {
						connection.setIdentity( newData );
					}

					data = apiKeyTextField.getText();
					newData = (data.trim().length() > 0) ? data : null;
					if(newData == null){
						failed++;
					}
					else {
						connection.setApiKey( newData );
					}

					data = apiSecretTextField.getText();
					newData = (data.trim().length() > 0) ? data : null;
					if(newData == null){
						failed++;
					}
					else {
						connection.setApiSecret( newData );
					}

					List<String> list = apiScopeList.getSelectedValuesList();
					if(list != null && list.size() > 0){
						connection.setApiScope( list );
					}

					data = accessTokenTextField.getText();
					newData = (data.trim().length() > 0) ? data : null;
					if(newData == null){
						logger.error("Empty Access Token");
					}
					else {
						connection.setAccessToken( newData );
					}

					data = accessSecretTextField.getText();
					newData = (data.trim().length() > 0) ? data : null;
					if(newData == null){
						logger.error("Empty Access Secret");
					}
					connection.setAccessSecret( newData );

					if(failed >= 2){
						JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_INVALID_OAUTH_DETAILS);
						return;
					}

					connectionWriter.saveToFile(connection);
				}
			});
		}

		final JPanel panel = new JPanel(new SpringLayout());
		JLabel label = null;

		label = new JLabel("Connection Name", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		connectionNameTextField.setPreferredSize(new Dimension(500, 24));
		label.setLabelFor(connectionNameTextField);
		panel.add(connectionNameTextField);

		label = new JLabel("Service", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		identityComboBox.setPreferredSize(new Dimension(500, 24));
		label.setLabelFor(identityComboBox);
		panel.add(identityComboBox);

		BorderLayout layout = new BorderLayout();
		layout.setHgap(5);
		JPanel subPanel = new JPanel(layout);
		label = new JLabel("Application Key", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		label.setLabelFor(subPanel);
		subPanel.add(apiKeyTextField, BorderLayout.CENTER);
		subPanel.add(apiKeyButton, BorderLayout.EAST);
		panel.add(subPanel);

		label = new JLabel("Application Secret", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		apiSecretTextField.setPreferredSize(new Dimension(500, 24));
		label.setLabelFor(apiSecretTextField);
		panel.add(apiSecretTextField);

		label = new JLabel("Scope", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		apiScopeList.setSize(new Dimension(500, 150));
		apiScopeScrollPane.setPreferredSize(new Dimension(500, 150));
		label.setLabelFor(apiScopeScrollPane);
		panel.add(apiScopeScrollPane);

		JPanel buttonPanel = new JPanel(new BorderLayout(15, 15));
		buttonPanel.add(authorizeButton, BorderLayout.EAST);
		panel.add(buttonPanel);

		label = new JLabel("Access Token", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		accessTokenTextField.setPreferredSize(new Dimension(500, 24));
		label.setLabelFor(accessTokenTextField);
		panel.add(accessTokenTextField);

		label = new JLabel("Access Secret", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		accessSecretTextField.setPreferredSize(new Dimension(500, 24));
		label.setLabelFor(accessSecretTextField);
		panel.add(accessSecretTextField);

		int controlCount = 15;

		if(overrideSaveButton == true){
			buttonPanel = new JPanel(new BorderLayout(15, 15));
			buttonPanel.add(saveButton, BorderLayout.EAST);
			panel.add(buttonPanel);

			controlCount = 16;
		}


		SpringLayoutUtils.makeCompactGrid(panel, controlCount, 1, 5, 5,	5, 5);

		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(panel, BorderLayout.NORTH);

		this.add(containerPanel);
	}

	@Override
	public void updateFields(ConnectionProperties connector) {
		if(connector instanceof SocialMediaConnectionProperties){
			SocialMediaConnectionProperties connection = (SocialMediaConnectionProperties)connector;

			final String provider = connection.getIdentity();
			if(identityComboBox != null)			identityComboBox.setSelectedItem(provider);

			if(connectionNameTextField != null)		connectionNameTextField.setText(connection.getName());

			if(apiScopeList != null){
				ListModel<String> model = apiScopeList.getModel();
				List<String> list = connection.getApiScope();

				if(list != null && model != null){
					int[] indices = new int[list.size()];

					// select all
					if(list.size() == model.getSize()) {
						for(int i = 0; i < list.size(); i++){
							indices[i] = i;
						}
					}
					// select only items in scope
					else if(list.size() > 0){
						int j = 0;

						for(int i = 0; i < model.getSize(); i++){
							String element = (String)model.getElementAt(i);

							for(String item : list){
								if(item.compareTo(element) == 0){
									indices[j] = i;

									j++;
								}
							}
						}
					}

					apiScopeList.setSelectedIndices(indices);
				}
			}

			if(apiKeyTextField != null)				apiKeyTextField.setText(connection.getApiKey());
			if(apiSecretTextField != null) 			apiSecretTextField.setText(connection.getApiSecret());
			if(accessTokenTextField != null) 		accessTokenTextField.setText(connection.getAccessToken());
			if(accessSecretTextField != null) 		accessSecretTextField.setText(connection.getAccessSecret());
		}
	}

	public void resetFields() {
		if(connectionNameTextField != null) 	connectionNameTextField.setText("");

		if(identityComboBox != null) 			identityComboBox.setSelectedItem(0);
		if(apiScopeList != null) 				apiScopeList.setSelectedIndex(-1);

		if(apiKeyTextField != null)				apiKeyTextField.setText("");
		if(apiSecretTextField != null) 			apiSecretTextField.setText("");
		if(accessTokenTextField != null) 		accessTokenTextField.setText("");
		if(accessSecretTextField != null) 		accessSecretTextField.setText("");
	}

	public JList<String> getApiScopeComboBox() {
		return apiScopeList;
	}
}
