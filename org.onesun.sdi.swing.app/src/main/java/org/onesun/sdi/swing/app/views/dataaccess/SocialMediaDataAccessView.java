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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

public class SocialMediaDataAccessView extends AbstractDataAccessView {
	/**
	 * 
	 */
	private static final long 			serialVersionUID	= 7628767091382680614L;
	
	private SetterDialog				setterDialog		= new SetterDialog(AppCommonsUI.MAIN_WINDOW);
	private JComboBox<RESTResource>		urlComboBox			= new JComboBox<RESTResource>(AppCommonsUI.REST_RESOURCE_COMBOBOX_MODEL);
	private JTextArea					dataTextArea		= new JTextArea();
	private JScrollPane					scrollPane			= new JScrollPane(dataTextArea);
	private JTextField					verbTextField		= new JTextField();
	
	public SocialMediaDataAccessView(){
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
		// UI Object Initialization
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				if(urlComboBox != null && urlComboBox.getModel().getSize() > 0){
					int index = urlComboBox.getSelectedIndex();
					
					if(index < 0){
						urlComboBox.setSelectedIndex(0);
					}
				}
			}

			@Override
			public void componentResized(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
		
		verbTextField.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
		verbTextField.setPreferredSize(new Dimension(80, 24));
		verbTextField.setEditable(false);
		
		dataTextArea.setEditable(false);
		dataTextArea.setWrapStyleWord(true);
		
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(verbTextField, BorderLayout.WEST);
		panel.add(urlComboBox, BorderLayout.CENTER);

		contentPanel.add(panel, BorderLayout.CENTER);
		
		JLabel label = new JLabel("Response Text");
		responsePanel.add(label,  BorderLayout.NORTH);
		responsePanel.add(scrollPane, BorderLayout.CENTER);
		
		// User Interactions
		urlComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object o = e.getItem();
				
				if(o instanceof RESTResource && urlComboBox.getSelectedIndex() > 0){
					// reset value
					RESTResource resource = (RESTResource)o;
					Verb v = resource.getVerb();
					
					String verbName = (v == null) ? "" : v.name();
					verbTextField.setText(verbName);
					
					AppCommons.TASKLET.setResource(resource);
				}
				else {
					verbTextField.setText("");
					AppCommons.TASKLET.setResource(null);
				}
				
				verbTextField.invalidate();
				
				AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
	    		AppCommonsUI.MODEL_TEXTAREA.invalidate();
			}
		});
		
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
				RESTResource resource = (RESTResource)urlComboBox.getSelectedItem();
				
				if(resource == null) return;
				RESTResource clone = (RESTResource) resource.clone();

				Connection cp = AppCommons.TASKLET.getConnectionProperties();

				if(AppCommons.AUTHENTICATOR == null && clone.isAccessTokenRequired() == true){
					if(cp == null){
						JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_TOKEN_MISSING);
						return;
					}
				}
				
				String url = clone.getUrl();
				if(url != null && url.trim().length() <= 0) return;
				
				DefaultCusor.startWaitCursor(rootPanel);
				
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
				clone.setParameters(params);
				
				// Update URL
				clone.setUrl(url);
				
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
					clone.setHeaders(headers);
				}
				
				// Set Payload
				Parameter payloadObject = null;
				if(filterMetadata != null) {
					payloadObject = filterMetadata.getPayload();
				}
				String payload = null;
				if(payloadObject != null){
					payload = payloadObject.getDefaultValue();
				}
				if(payload != null && payload.trim().length() > 0){
					resource.setPayload(payload);
				}
				
				ServiceProvider provider = ProviderFactory.getProvider(cp.getIdentity().toLowerCase(), "SOCIAL_MEDIA");
				
				RESTClient listener = new RESTClient(provider, clone, AppCommons.AUTHENTICATION);
				listener.setConnection(cp);
				if(AppCommons.AUTHENTICATION.compareTo("OAUTH") == 0 && AppCommons.AUTHENTICATOR != null){
					listener.setOAuthService(AppCommons.AUTHENTICATOR.getService());
					listener.setAccessToken(AppCommons.AUTHENTICATOR.getAccessToken());
				}
				listener.execute();
				
				// reset this
				clone.setParameters(null);
				

				try{
					clone.setObject(listener.getResponseBody());
					clone.checkFormat();
					TextFormat textFormat = clone.getTextFormat();

					String statusText =  "Status: " + listener.getResponseCode() + "; Data Format: " + textFormat.name();
					setStatus(statusText);
					
					final String response = clone.getFormattedText();
					
					if(response != null){
						dataTextArea.setText(response);
						AppCommons.RESPONSE_OBJECT = response;
					}
				} finally{
				}
				
				// Update the meta-model
				AppCommons.TASKLET.setFilterMetadata(filterMetadata);
				AppCommons.TASKLET.setResource(clone);
	    		AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
	    		AppCommonsUI.MODEL_TEXTAREA.invalidate();
				
				dataTextArea.invalidate();
				scrollPane.invalidate();
				
				DefaultCusor.stopWaitCursor(rootPanel);
			}
		});
	}
	
	@Override
	protected void postInit(){
	}
}
