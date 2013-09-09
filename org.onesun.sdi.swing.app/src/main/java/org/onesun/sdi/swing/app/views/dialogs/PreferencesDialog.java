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
package org.onesun.sdi.swing.app.views.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.onesun.commons.swing.ClipboardUtils;
import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.sdi.swing.app.AppCommons;
import org.onesun.sdi.swing.app.AppIcons;
import org.onesun.sdi.swing.app.handlers.UserAction;
import org.onesun.sdi.swing.app.handlers.UserActionListener;

public class PreferencesDialog extends AbstractDialog {
	private static final long serialVersionUID = 8615404465470079746L;

	public PreferencesDialog(Frame parent) {
		super(parent, "Preferences", true);
		
		setSize(600, 400);
		setResizable(false);
		setVisible(false);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.add("General", createGeneralPage());
		tabbedPane.add("OAuth Handler", createOAuthCallbackServerPage());
		tabbedPane.add("Proxy Settings", createProxySettingsPage());
		tabbedPane.add("Thirdparty License", createThirdpartyLicensePage());
		contentContainer.add(tabbedPane, BorderLayout.CENTER);
		
		JButton closeButton = new JButton("Close");
		buttonContainer.add(closeButton);
		UserActionListener listener = null;

		listener = new UserActionListener(okButton, new UserAction() {
			@Override
			public void execute(ActionEvent event) {
				onOkButtonClicked(event);
				
				setVisible(false);
			}
		});
		okButton.addActionListener(listener);
		
		listener = new UserActionListener(closeButton, new UserAction() {
			@Override
			public void execute(ActionEvent event) {
				setVisible(false);
			}
		});
		closeButton.addActionListener(listener);
	}
	
	public JPanel createThirdpartyLicensePage(){
		JPanel panel = null;
		JLabel label = null;
				
		panel = new JPanel(new SpringLayout());
		label = new JLabel("UClassify Read Access Key", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		uclassifyLicenseKeyTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(uclassifyLicenseKeyTextField);
		uclassifyLicenseKeyTextField.setText(AppCommons.UCLASSIFY_READ_ACCESS_KEY);
		panel.add(uclassifyLicenseKeyTextField);
		
		label = new JLabel("OpenCalais License Key", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		openCalasisLicenseKeyTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(openCalasisLicenseKeyTextField);
		openCalasisLicenseKeyTextField.setText(AppCommons.OPENCALAIS_LICENSE_KEY);
		panel.add(openCalasisLicenseKeyTextField);
		
		
		SpringLayoutUtils.makeCompactGrid(panel, 4, 1, 5, 5,	5, 5);
		
		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(panel, BorderLayout.NORTH);
		
		return containerPanel;
	}
	
	private JTextField smcDirTextField = new JTextField();
	private JTextField httpTimeoutTextField = new JTextField();
	private JTextField uclassifyLicenseKeyTextField = new JTextField();
	private JTextField openCalasisLicenseKeyTextField = new JTextField();

	public JPanel createGeneralPage(){
		JPanel panel = null;
		JLabel label = null;
				
		panel = new JPanel(new SpringLayout());
		label = new JLabel("Global HTTP Timeout", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		httpTimeoutTextField.setText(Integer.toString(AppCommons.HTTP_CONNECTION_TIMEOUT));
		httpTimeoutTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(httpTimeoutTextField);
		panel.add(httpTimeoutTextField);
		
		label = new JLabel("Application root directory", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		smcDirTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(smcDirTextField);
		panel.add(smcDirTextField);
		smcDirTextField.setEditable(false);
		smcDirTextField.setText(AppCommons.PATH_TO_APP_HOME);
		
		
		SpringLayoutUtils.makeCompactGrid(panel, 4, 1, 5, 5,	5, 5);
		
		JPanel containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(panel, BorderLayout.NORTH);
		
		return containerPanel;
	}

	private JTextField protocolTextField = new JTextField();
	private JTextField hostnameTextField = new JTextField();
	private JTextField portTextField = new JTextField();
	private JTextField callbackContextTextField = new JTextField();
	
	public JPanel createOAuthCallbackServerPage(){
		JPanel panel = null;
		JLabel label = null;
		
		panel = new JPanel(new SpringLayout());
		label = new JLabel("Protocol", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		protocolTextField.setText(AppCommons.OAUTH_CALLBACK_SERVER_PROTOCOL);
		protocolTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(protocolTextField);
		panel.add(protocolTextField);

		label = new JLabel("Hostname", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		hostnameTextField.setText(AppCommons.OAUTH_CALLBACK_SERVER_NAME);
		hostnameTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(hostnameTextField);
		panel.add(hostnameTextField);
		
		label = new JLabel("Port", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		portTextField.setText(Integer.toString(AppCommons.OAUTH_CALLBACK_SERVER_PORT));
		portTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(portTextField);
		panel.add(portTextField);
		
		label = new JLabel("Callback Context", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		callbackContextTextField.setText(AppCommons.OAUTH_CALLBACK_SERVER_CONTEXT);
		callbackContextTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(callbackContextTextField);
		panel.add(callbackContextTextField);
		
		SpringLayoutUtils.makeCompactGrid(panel, 8, 1, 5, 5,	5, 5);
		
		JButton copyCallbackUrlButton = new JButton("Copy to Clipboard", AppIcons.getIcon("copy"));
		copyCallbackUrlButton.setToolTipText("Copy Callback URL to Clipboard");
		copyCallbackUrlButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String selection = 
					protocolTextField.getText().trim() + 
					"://" +
					hostnameTextField.getText().trim() +
					":" +
					portTextField.getText().trim() +
					callbackContextTextField.getText().trim();
				
				ClipboardUtils.copyToClipboard(selection);
			}
		});
		
		JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
		containerPanel.add(panel, BorderLayout.CENTER);
		
		panel = new JPanel(new BorderLayout(5, 5));
		panel.add(copyCallbackUrlButton, BorderLayout.EAST);
		
		containerPanel.add(panel, BorderLayout.SOUTH);
		
		return containerPanel;
	}

	private JCheckBox enableProxyCheckBox = new JCheckBox();
	private JTextField httpProxyHostnameTextField = new JTextField();
	private JTextField httpProxyPortTextField = new JTextField();
	private JTextField httpProxyUsernameTextField = new JTextField();
	private JTextField httpProxyPasswordTextField = new JTextField();

	public JPanel createProxySettingsPage(){
		JPanel panel = null;
		JLabel label = null;
		
		panel = new JPanel(new SpringLayout());
		label = new JLabel("Enable Proxy", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		enableProxyCheckBox.setSelected(AppCommons.PROXY_CONFIGURATION.isEnabled());
		enableProxyCheckBox.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(enableProxyCheckBox);
		panel.add(enableProxyCheckBox);

		label = new JLabel("Hostname", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		httpProxyHostnameTextField.setText(AppCommons.PROXY_CONFIGURATION.getHostname());
		httpProxyHostnameTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(httpProxyHostnameTextField);
		panel.add(httpProxyHostnameTextField);
		
		label = new JLabel("Port Number", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		httpProxyPortTextField.setText(Integer.toString(AppCommons.PROXY_CONFIGURATION.getPort()));
		httpProxyPortTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(httpProxyPortTextField);
		panel.add(httpProxyPortTextField);
		
		label = new JLabel("Username", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		httpProxyUsernameTextField.setText(AppCommons.PROXY_CONFIGURATION.getUsername());
		httpProxyUsernameTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(httpProxyUsernameTextField);
		panel.add(httpProxyUsernameTextField);
		
		label = new JLabel("Pasword", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		httpProxyPasswordTextField.setText(AppCommons.PROXY_CONFIGURATION.getPassword());
		httpProxyPasswordTextField.setPreferredSize(new Dimension(250, 24));
		label.setLabelFor(httpProxyPasswordTextField);
		panel.add(httpProxyPasswordTextField);
		
		SpringLayoutUtils.makeCompactGrid(panel, 10, 1, 5, 5,	5, 5);
		
		JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
		containerPanel.add(panel, BorderLayout.CENTER);
		
		return containerPanel;
	}
	
	void onOkButtonClicked(ActionEvent event) {
		// Generic Settings
		int timeout = Integer.parseInt(httpTimeoutTextField.getText().trim());
		if(timeout <= 10000){
			timeout = AppCommons.HTTP_CONNECTION_TIMEOUT;
		}
		
		int port = Integer.parseInt(portTextField.getText().trim());
		if(port <= 0){
			port = AppCommons.OAUTH_CALLBACK_SERVER_PORT;
		}
		
		String hostname = hostnameTextField.getText().trim();
		hostname = (hostname != null && hostname.length() > 0) ? hostname : AppCommons.OAUTH_CALLBACK_SERVER_NAME;
		
		String protocol = protocolTextField.getText().trim();
		protocol = (protocol != null && protocol.length() > 0) ? protocol : AppCommons.OAUTH_CALLBACK_SERVER_PROTOCOL;
		
		String context = callbackContextTextField.getText().trim();
		context = (context != null && context.length() > 0) ? context : AppCommons.OAUTH_CALLBACK_SERVER_CONTEXT;
		
		// License Strings
		String uclassifyLicense = uclassifyLicenseKeyTextField.getText().trim();
		uclassifyLicense = (uclassifyLicense != null && uclassifyLicense.length() > 0) ? uclassifyLicense : AppCommons.UCLASSIFY_READ_ACCESS_KEY;
		
		String openCalaisLicense = openCalasisLicenseKeyTextField.getText().trim();
		openCalaisLicense = (openCalaisLicense != null && openCalaisLicense.length() > 0) ? openCalaisLicense : AppCommons.OPENCALAIS_LICENSE_KEY;
		
		// Proxy Server Settings
		String httpProxyEnabled = enableProxyCheckBox.getText().trim();
		httpProxyEnabled = (httpProxyEnabled != null && httpProxyEnabled.length() > 0) ? httpProxyEnabled : Boolean.toString(AppCommons.PROXY_CONFIGURATION.isEnabled());
		
		String httpProxyHostname = httpProxyHostnameTextField.getText().trim();
		httpProxyHostname = (httpProxyHostname != null && httpProxyHostname.length() > 0) ? httpProxyHostname : AppCommons.PROXY_CONFIGURATION.getHostname();
		
		String httpProxyPort = httpProxyPortTextField.getText().trim();
		httpProxyPort = (httpProxyPort != null && httpProxyPort.length() > 0) ? httpProxyPort : Integer.toString(AppCommons.PROXY_CONFIGURATION.getPort());
		
		String httpProxyUsername = httpProxyUsernameTextField.getText().trim();
		httpProxyUsername = (httpProxyUsername != null && httpProxyUsername.length() > 0) ? httpProxyUsername : AppCommons.PROXY_CONFIGURATION.getUsername();
		
		String httpProxyPassword = httpProxyPasswordTextField.getText().trim();
		httpProxyPassword = (httpProxyPassword != null && httpProxyPassword.length() > 0) ? httpProxyPassword : AppCommons.PROXY_CONFIGURATION.getPassword();

		
		AppCommons.HTTP_CONNECTION_TIMEOUT				= timeout;
		AppCommons.OAUTH_CALLBACK_SERVER_PORT			= port;
		AppCommons.OAUTH_CALLBACK_SERVER_NAME			= hostname;
		AppCommons.OAUTH_CALLBACK_SERVER_PROTOCOL		= protocol;
		AppCommons.OAUTH_CALLBACK_SERVER_CONTEXT		= context;
		AppCommons.UCLASSIFY_READ_ACCESS_KEY			= uclassifyLicense;
		AppCommons.OPENCALAIS_LICENSE_KEY				= openCalaisLicense;
		
		AppCommons.PROXY_CONFIGURATION.setEnabled(Boolean.parseBoolean(httpProxyEnabled));
		AppCommons.PROXY_CONFIGURATION.setHostname(httpProxyHostname);
		AppCommons.PROXY_CONFIGURATION.setPort(Integer.parseInt(httpProxyPort));
		AppCommons.PROXY_CONFIGURATION.setUsername(httpProxyUsername);
		AppCommons.PROXY_CONFIGURATION.setPassword(httpProxyPassword);
		 
		AppCommons.saveConfiguration();
		
		// Reset objects
		AppCommons.setup();
	}
}