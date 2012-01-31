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
package org.onesun.smc.app.views.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.onesun.commons.swing.ClipboardUtils;
import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppIcons;
import org.onesun.smc.app.handlers.UserAction;
import org.onesun.smc.app.handlers.UserActionListener;

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

	void onOkButtonClicked(ActionEvent event) {
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
		
		String uclassifyLicense = uclassifyLicenseKeyTextField.getText().trim();
		uclassifyLicense = (uclassifyLicense != null && uclassifyLicense.length() > 0) ? uclassifyLicense : AppCommons.UCLASSIFY_READ_ACCESS_KEY;
		
		String openCalaisLicense = openCalasisLicenseKeyTextField.getText().trim();
		openCalaisLicense = (openCalaisLicense != null && openCalaisLicense.length() > 0) ? openCalaisLicense : AppCommons.OPENCALAIS_LICENSE_KEY;
		
		AppCommons.HTTP_CONNECTION_TIMEOUT				= timeout;
		AppCommons.OAUTH_CALLBACK_SERVER_PORT			= port;
		AppCommons.OAUTH_CALLBACK_SERVER_NAME			= hostname;
		AppCommons.OAUTH_CALLBACK_SERVER_PROTOCOL		= protocol;
		AppCommons.OAUTH_CALLBACK_SERVER_CONTEXT		= context;
		AppCommons.UCLASSIFY_READ_ACCESS_KEY			= uclassifyLicense;
		AppCommons.OPENCALAIS_LICENSE_KEY				= openCalaisLicense;
		 
		AppCommons.saveConfiguration();
		
		// Reset objects
		AppCommons.setup();
	}
}