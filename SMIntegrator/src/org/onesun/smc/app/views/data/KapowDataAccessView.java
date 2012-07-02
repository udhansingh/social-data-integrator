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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.onesun.commons.swing.cursors.DefaultCusor;
import org.onesun.smc.api.ProviderFactory;
import org.onesun.smc.api.WebProvider;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.app.AppIcons;
import org.onesun.smc.app.handlers.RequestUpdateHandler;
import org.onesun.smc.app.views.dialogs.SetterDialog;
import org.onesun.smc.core.connection.properties.KapowConnectionProperties;
import org.onesun.smc.core.connection.properties.WebConnectionProperties;
import org.onesun.smc.core.listeners.KapowStreamingListener;
import org.onesun.smc.core.metadata.FilterMetadata;
import org.onesun.smc.core.providers.web.kapow.KapowObject;
import org.onesun.smc.core.resources.WebResource;
import org.onesun.smc.core.services.handler.ConnectionHandler;
import org.onesun.smc.core.services.handler.DataHandler;

import com.kapowtech.robosuite.api.java.repository.construct.Attribute;
import com.kapowtech.robosuite.api.java.repository.construct.Type;
import com.kapowtech.robosuite.api.java.rql.construct.RQLObject;

public class KapowDataAccessView extends AbstractDataAccessView {
	/**
	 * 
	 */
	private static final long				serialVersionUID		= 7628767091382680614L;
	private SetterDialog					setterDialog			= new SetterDialog(AppCommonsUI.MAIN_WINDOW);
	private static final String				START_SAMPLING_LABEL	= "Start Sampling";
	private static final String				STOP_SAMPLING_LABEL		= "Stop Sampling";
	
	private JButton							refreshButton 			= new JButton(AppIcons.getIcon("refresh"));
	private JComboBox<WebResource>			resourceComboBox		= new JComboBox<WebResource>(AppCommonsUI.WEB_RESOURCE_COMBOBOX_MODEL);
	private JTextArea						dataTextArea			= new JTextArea();
	private JScrollPane						scrollPane				= new JScrollPane(dataTextArea);
	private WebResource						resource				= null;
	
	private List<RQLObject>					results					= Collections.synchronizedList(new ArrayList<RQLObject>());
	
	public KapowDataAccessView(){
		super();
	}
	
	@Override
	public JPanel getViewPanel(){
		return this;
	}

	private KapowStreamingListener executor = new KapowStreamingListener(
			new DataHandler() {
				@Override
				public void flush(Object object) {
					if(object != null && object instanceof RQLObject){
						RQLObject rqlObject = (RQLObject)object;
						results.add(rqlObject);
						
						Object o = resource.getObject();
						KapowObject kapowObject = null;

						if(o != null && o instanceof KapowObject){
							kapowObject = (KapowObject)o;
						}

						if(kapowObject != null) {
							Type[] types = kapowObject.getReturnedTypes();

							for(Type type : types){
								String parentXml = "<result-object>";
	
								Attribute[] attributes = type.getAttributes();
								for(Attribute attribute : attributes){
									String attributeName = attribute.getName();
									String childXml = "<" + attributeName + ">";
									Object vo = rqlObject.get(attributeName);
									if(vo instanceof String){
										childXml += (String)vo;
									}
									childXml += "</" + attributeName + ">";
									
									parentXml += (childXml);
								}
	
								parentXml += "</result-object>";
								
								dataTextArea.append(parentXml + "\n");
							}
						}
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
		panel.add(refreshButton, BorderLayout.WEST);
		panel.add(resourceComboBox, BorderLayout.CENTER);

		contentPanel.add(panel, BorderLayout.CENTER);
		
		JLabel label = new JLabel("Response Text");
		responsePanel.add(label,  BorderLayout.NORTH);
		responsePanel.add(scrollPane, BorderLayout.CENTER);
		
		resourceComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object o = e.getItem();
				
				if(o != null && o instanceof WebResource){
					resource = (WebResource)o;
					
					AppCommons.TASKLET.setResource(resource);
		    		AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
		    		AppCommonsUI.MODEL_TEXTAREA.invalidate();
				}
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
					}
				});
				setterDialog.setVisible(true);
			}
		});
		
		validateButton.setText(START_SAMPLING_LABEL);
		
		validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(resource == null && resourceComboBox.getItemCount() > 0){
					resource = (WebResource) resourceComboBox.getSelectedItem();
				}
				
				// TODO: Set Query Params
				
				if(validateButton.getText().compareTo(START_SAMPLING_LABEL) == 0){
					results.clear();
					
					validateButton.setText(STOP_SAMPLING_LABEL);
					
					DefaultCusor.startWaitCursor(rootPanel);
					
					executor.setConnectionProperties((KapowConnectionProperties) AppCommons.TASKLET.getConnectionProperties());
					// TODO: Set the right parameters for Kapow
					executor.setProjectName(null);
					executor.setRobotName(null);
					
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
					AppCommons.TASKLET.setResource(resource);
		    		AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
		    		AppCommonsUI.MODEL_TEXTAREA.invalidate();
				}
			}
		});
		
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WebConnectionProperties c = (WebConnectionProperties)AppCommons.TASKLET.getConnectionProperties();

				WebProvider provider = (WebProvider)ProviderFactory.getProvider(c.getIdentity(), "KAPOW");

				if(provider != null){
					// Requires a connection reference
					provider.setConnection(c);

					// login if required
					provider.validate();

					AppCommonsUI.WEB_RESOURCE_COMBOBOX_MODEL.removeAllElements();

					provider.refreshMetadata();
					List<WebResource> resources = provider.getResources();

					WebResource r = new WebResource();
					AppCommonsUI.WEB_RESOURCE_COMBOBOX_MODEL.addElement(r);

					if(resources != null){
						for(WebResource resource : resources){
							AppCommonsUI.WEB_RESOURCE_COMBOBOX_MODEL.addElement(resource);
						}
					}
				}

				resourceComboBox.invalidate();
			}
		});
	}
	
	@Override
	protected void postInit(){
	}
}
