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
package org.onesun.smc.app.views.data;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.onesun.commons.swing.cursors.DefaultCusor;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.app.handlers.RequestUpdateHandler;
import org.onesun.smc.app.views.dialogs.SetterDialog;
import org.onesun.smc.core.connection.properties.TwitterStreamingConnectionProperties;
import org.onesun.smc.core.metadata.FilterMetadata;
import org.onesun.smc.core.model.Parameter;
import org.onesun.smc.core.resources.StreamingResource;
import org.onesun.smc.core.services.handler.ConnectionHandler;
import org.onesun.smc.core.services.handler.DataHandler;
import org.onesun.smc.core.services.streaming.TwitterStreamingListener;

public class TwitterStreamingDataAccessView extends AbstractDataAccessView {
	/**
	 * 
	 */
	private static final long				serialVersionUID		= 7628767091382680614L;
	private SetterDialog					setterDialog			= new SetterDialog(AppCommonsUI.MAIN_WINDOW);
	private static final String				START_SAMPLING_LABEL	= "Start Sampling";
	private static final String				STOP_SAMPLING_LABEL		= "Stop Sampling";
	
	private JComboBox<StreamingResource>	urlComboBox				= new JComboBox<StreamingResource>(AppCommonsUI.STREAMING_RESOURCE_COMBOBOX_MODEL);;
	private JTextArea						dataTextArea			= new JTextArea();
	private JScrollPane						scrollPane				= new JScrollPane(dataTextArea);
	private StreamingResource				resource				= null;
	
	private List<String>					results					= Collections.synchronizedList(new ArrayList<String>());
	
	public TwitterStreamingDataAccessView(){
		super();
		
		validateButton.setText(START_SAMPLING_LABEL);
	}
	
	private TwitterStreamingListener executor = new TwitterStreamingListener(
			new DataHandler() {
				@Override
				public void flush(Object object) {
					if(object instanceof String){
						String text = (String)object;
						
						dataTextArea.append(text + "\n");
						results.add(text);
					}
				}
			},
			
			new ConnectionHandler(){
				@Override
				public void setStatusCode(int statusCode) {
					String statusText =  "Status: " + statusCode;
					setStatus(statusText);
				}
			}
	);
	@Override
	protected void preInit(){
	}
	
	@Override
	protected void init(){
		dataTextArea.setEditable(false);
		
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(urlComboBox, BorderLayout.CENTER);

		contentPanel.add(panel, BorderLayout.CENTER);
		
		JLabel label = new JLabel("Response Text");
		responsePanel.add(label,  BorderLayout.NORTH);
		responsePanel.add(scrollPane, BorderLayout.CENTER);
		
		urlComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object o = e.getItem();
				
				if(o instanceof StreamingResource){
					resource = (StreamingResource)o;
					
					AppCommons.TASKLET.setResource(resource);
					
					AppCommons.TASKLET.setResource(resource);
		    		AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
		    		AppCommonsUI.MODEL_TEXTAREA.invalidate();
				}
			}
		});
		
		if(AppCommonsUI.STREAMING_RESOURCE_COMBOBOX_MODEL.getSize() > 0){
			urlComboBox.setSelectedIndex(0);
		}
		
		setterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setterDialog.getView().initParamsModel();
				setterDialog.setRequestUpdateHandler(new RequestUpdateHandler() {
					@Override
					public void update(final FilterMetadata fm) {
						filterMetadata = fm;
					}
				});
				setterDialog.setVisible(true);
			}
		});
		
		validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(resource == null && urlComboBox.getItemCount() > 0){
					resource = urlComboBox.getItemAt(0);
				}
				
				String url = resource.getUrl();
				StreamingResource r = null;
				try {
					URI uri = new URI(url);
					String resourceName = uri.getHost().replace(".", "-") + uri.getPath().replace("/", "-");
					r = new StreamingResource(resourceName, resource.getVerb(), url);
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
						String value = param.getDefaultValue();

						if(value != null && value.trim().length() > 0){
							if(en.startsWith("$")){
								url = url.replace(en, value);
							}
							else {
								if(start == true){
									params = "?" + en + "=" + value;
									start = false;
								}
								else {
									params += "&" + en + "=" + value;
								}
							}
						}
					}
				}
				r.setParameters(params);
				
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
					r.setHeaders(headers);
				}
				
				// Set Payload
				Parameter payloadObject = null;
				String payload = null;
				if(filterMetadata != null){
					payloadObject = filterMetadata.getPayload();
				}
				if(payloadObject != null){
					payload = payloadObject.getDefaultValue();
				}
				if(payload != null && payload.trim().length() > 0){
					r.setPayload(payload);
				}
				
				if(validateButton.getText().compareTo(START_SAMPLING_LABEL) == 0){
					results.clear();
					
					validateButton.setText(STOP_SAMPLING_LABEL);
					
					DefaultCusor.startWaitCursor(rootPanel);
					
					executor.setConnection((TwitterStreamingConnectionProperties) AppCommons.TASKLET.getConnection());
					executor.setResource(r);
					
					setStatus("");
					dataTextArea.setText("");
					
					executor.start();
				}
				else if(validateButton.getText().compareTo(STOP_SAMPLING_LABEL) == 0){
					validateButton.setText(START_SAMPLING_LABEL);

					DefaultCusor.stopWaitCursor(rootPanel);
					executor.stop();

					AppCommons.RESPONSE_OBJECT = results;
					
					// Update the meta-model
					AppCommons.TASKLET.setFilterMetadata(filterMetadata);
					AppCommons.TASKLET.setResource(r);
		    		AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
		    		AppCommonsUI.MODEL_TEXTAREA.invalidate();
				}
			}
		});
	}
	
	@Override
	protected void postInit(){
	}
}
