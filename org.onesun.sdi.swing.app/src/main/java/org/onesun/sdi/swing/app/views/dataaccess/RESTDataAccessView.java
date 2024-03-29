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
package org.onesun.sdi.swing.app.views.dataaccess;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.onesun.commons.swing.cursors.DefaultCusor;
import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.sdi.core.api.Connection;
import org.onesun.sdi.core.api.ServiceProvider;
import org.onesun.sdi.core.metadata.FilterMetadata;
import org.onesun.sdi.core.model.Parameter;
import org.onesun.sdi.core.resources.RESTResource;
import org.onesun.sdi.sil.client.RESTClient;
import org.onesun.sdi.spi.api.ProviderFactory;
import org.onesun.sdi.swing.app.AppCommons;
import org.onesun.sdi.swing.app.AppCommonsUI;
import org.onesun.sdi.swing.app.AppMessages;
import org.onesun.sdi.swing.app.handlers.RequestUpdateHandler;
import org.onesun.sdi.swing.app.views.dialogs.SetterDialog;
import org.scribe.model.Verb;

public class RESTDataAccessView extends AbstractDataAccessView {
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 7628767091382680614L;
	
	private SetterDialog				setterDialog		= new SetterDialog(AppCommonsUI.MAIN_WINDOW);
	private static final String[]		restMethods			= {"", "GET", "POST", "PUT", "DELETE"};
	private JComboBox<String>			methodComboBox		= new JComboBox<String>(restMethods);
	private JTextField					urlTextField		= new JTextField();
	private JTextArea					dataTextArea		= new JTextArea();
	private JScrollPane					scrollPane			= new JScrollPane(dataTextArea);

	private Verb						verb				= Verb.GET;

	public RESTDataAccessView(){
		super();
	}
	
	@Override
	protected void preInit(){
	}
	
	@Override
	public JPanel getViewPanel(){
		return this;
	}

	@Override
	protected void init(){
		dataTextArea.setWrapStyleWord(true);
		dataTextArea.setEditable(false);
		
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(methodComboBox, BorderLayout.WEST);
		panel.add(urlTextField, BorderLayout.CENTER);
		
		contentPanel.add(panel, BorderLayout.CENTER);
		
		JLabel label = new JLabel("Response Text");
		responsePanel.add(label,  BorderLayout.NORTH);
		responsePanel.add(scrollPane, BorderLayout.CENTER);
		
		setterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setterDialog.getView().initParamsModel();
				setterDialog.setRequestUpdateHandler(new RequestUpdateHandler() {
					@Override
					public void update(final FilterMetadata fm) {
						filterMetadata = fm;

						AppCommons.TASKLET.setFilterMetadata(fm);
						AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
			    		AppCommonsUI.MODEL_TEXTAREA.invalidate();

					}
				});
				setterDialog.setVisible(true);
			}
		});
		
		validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String url = urlTextField.getText();
				
				if(url == null || (url != null && url.trim().length() == 0)){
					JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_BAD_URL);
					return;
				}
				else {
					url = url.trim();
					
					if(url.startsWith("http://") == false && url.startsWith("https://") == false){
						JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_BAD_HTTP_PROTOCOL);
						return;
					}
				}
				
				DefaultCusor.startWaitCursor(rootPanel);

				if(methodComboBox.getSelectedIndex() > 0){
					verb = Verb.valueOf((String)methodComboBox.getSelectedItem());
				}
				
				RESTResource resource = null;
				try {
					URI uri = new URI(url);
					String resourceName = uri.getHost().replace(".", "-") + uri.getPath().replace("/", "-");
					resource = new RESTResource(resourceName, verb, url);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				
				// Set Query Params
				String params = null;
				Collection<Parameter> requestParams = null;
				if(filterMetadata != null) {
					requestParams = filterMetadata.paramValues();
				}
				if(requestParams != null){
					boolean start = true;
					for(Parameter param : requestParams){
						String en = param.getExternalName();
						if(en.startsWith("$")){
							url = url.replace(en, param.getDefaultValue());
						}
						else {
							if(start == true){
								params = "?" + en + "=" + param.getDefaultValue();
								start = false;
							}
							else {
								params += "&" + en + "=" + param.getDefaultValue();
							}
						}
					}
				}
				resource.setParameters(params);
				
				// Update URL
				resource.setUrl(url);
				
				// Set Headers
				Collection<Parameter> requestHeaders = null;
				if(filterMetadata != null){
					requestHeaders = filterMetadata.headerValues();
				}
				if(requestHeaders != null){
					Map<String, String> headers = new HashMap<String, String>();
					for(Parameter header : requestHeaders){
						headers.put(header.getExternalName(), header.getDefaultValue());
					}
					resource.setHeaders(headers);
				}
				
				// Set Payload
				Parameter payloadObject = filterMetadata.getPayload();
				String payload = null;
				if(payloadObject != null){
					payload = payloadObject.getDefaultValue();
				}
				if(payload != null && payload.trim().length() > 0){
					resource.setPayload(payload);
				}
				
				Connection cp = AppCommons.TASKLET.getConnectionProperties();
				ServiceProvider provider = ProviderFactory.getProvider(cp.getIdentity().toLowerCase(), cp.getCategory());
				
				RESTClient listener = new RESTClient(provider, resource, AppCommons.AUTHENTICATION);
				
				listener.setConnection(cp);
				if(AppCommons.AUTHENTICATION.compareTo("OAUTH") == 0) {
					if(AppCommons.AUTHENTICATOR != null){
						listener.setOAuthService(AppCommons.AUTHENTICATOR.getService());
						listener.setAccessToken(AppCommons.AUTHENTICATOR.getAccessToken());
					}
				}
				else {
					resource.setAccessTokenRequired(false);
				}

				listener.execute();
				
				
				resource.setObject(listener.getResponseBody());
				resource.checkFormat();
				
				TextFormat textFormat = resource.getTextFormat();
				
				String statusText =  "Status: " + listener.getResponseCode() + "; Data Format: " + textFormat.name();
				setStatus(statusText);
				
				final String response = resource.getFormattedText();
				
				if(response != null){
					dataTextArea.setText(response);
					AppCommons.RESPONSE_OBJECT = response;
				}
				
				scrollPane.invalidate();
				
				// Update the meta-model
				AppCommons.TASKLET.setFilterMetadata(filterMetadata);
				AppCommons.TASKLET.setResource(resource);
	    		AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
				
				DefaultCusor.stopWaitCursor(rootPanel);
			}
		});
	}
	
	@Override
	protected void postInit(){
	}
}
