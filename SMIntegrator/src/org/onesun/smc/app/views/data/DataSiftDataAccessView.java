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

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;

import org.datasift.Definition;
import org.datasift.EInvalidData;
import org.datasift.IStreamConsumerEvents;
import org.datasift.Interaction;
import org.datasift.StreamConsumer;
import org.datasift.User;
import org.onesun.commons.swing.cursors.DefaultCusor;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.app.handlers.RequestUpdateHandler;
import org.onesun.smc.app.views.dialogs.SetterDialog;
import org.onesun.smc.core.connection.properties.DataSiftConnectionProperties;
import org.onesun.smc.core.metadata.FilterMetadata;
import org.onesun.smc.core.resources.StreamingResource;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DataSiftDataAccessView extends AbstractDataAccessView {
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
	private StreamConsumer 					consumer				= null;
	
	@Override
	public JPanel getViewPanel(){
		return this;
	}
	
	private class StreamConsumerEventHandler implements IStreamConsumerEvents {
		// DataSift handlers
		@Override
		public void onDeleted(StreamConsumer c, Interaction i) throws EInvalidData {
		}

		@Override
		public void onError(StreamConsumer c, String message) throws EInvalidData {
			setStatus(message);
		}

		private Element toElement(Document document, Interaction i) throws ParserConfigurationException {
			Element parent = document.createElement("item");
			
			Element interaction = document.createElement("interaction");
			Element author = document.createElement("author");
			
			// Interaction Info.
			try{
				Element element = document.createElement("content");
				element.setTextContent(i.getStringVal("interaction.content"));
				interaction.appendChild(element);
			} catch (DOMException e) {
			} catch (EInvalidData e) {
			}
			
//			try{
//				Element element = document.createElement("geo");
//				element.setTextContent(i.getStringVal("interaction.geo"));
//				interaction.appendChild(element);
//			} catch (DOMException e) {
//			} catch (EInvalidData e) {
//			}
			
			try{
				Element element = document.createElement("link");
				element.setTextContent(i.getStringVal("interaction.link"));
				interaction.appendChild(element);
			} catch (DOMException e) {
			} catch (EInvalidData e) {
			}
			
			try{
				Element element = document.createElement("sample");
				element.setTextContent(Double.toString(i.getDoubleVal("interaction.sample")));
				interaction.appendChild(element);
			} catch (DOMException e) {
			} catch (EInvalidData e) {
			}
			
			try{
				Element element = document.createElement("source");
				element.setTextContent(i.getStringVal("interaction.source"));
				interaction.appendChild(element);
			} catch (DOMException e) {
			} catch (EInvalidData e) {
			}
			
			try{
				Element element = document.createElement("title");
				element.setTextContent(i.getStringVal("interaction.title"));
				interaction.appendChild(element);
			} catch (DOMException e) {
			} catch (EInvalidData e) {
			}
			
			try{
				Element element = document.createElement("type");
				element.setTextContent(i.getStringVal("interaction.type"));
				interaction.appendChild(element);
			} catch (DOMException e) {
			} catch (EInvalidData e) {
			}
			
			// Author Info
			try{
				Element element = document.createElement("avatar");
				element.setTextContent(i.getStringVal("interaction.author.avatar"));
				author.appendChild(element);
			} catch (DOMException e) {
			} catch (EInvalidData e) {
			}

			try{
				Element element = document.createElement("id");
				element.setTextContent(Integer.toString(i.getIntVal("interaction.author.id")));
				author.appendChild(element);
			} catch (DOMException e) {
			} catch (EInvalidData e) {
			}

			try{
				Element element = document.createElement("link");
				element.setTextContent(i.getStringVal("interaction.author.link"));
				author.appendChild(element);
			} catch (DOMException e) {
			} catch (EInvalidData e) {
			}

			
			try{
				Element element = document.createElement("name");
				element.setTextContent(i.getStringVal("interaction.author.name"));
				author.appendChild(element);
			} catch (DOMException e) {
			} catch (EInvalidData e) {
			}

			
			try{
				Element element = document.createElement("username");
				element.setTextContent(i.getStringVal("interaction.author.username"));
				author.appendChild(element);
			} catch (DOMException e) {
			} catch (EInvalidData e) {
			}

			// Add to parent node
			parent.appendChild(interaction);
			parent.appendChild(author);
			
			return parent;
		}
		
		@Override
		public void onInteraction(StreamConsumer c, Interaction i) throws EInvalidData {
			String text = i.toString();

			dataTextArea.append(text + "\n");
			dataTextArea.invalidate();

			results.add(text);
		}

		@Override
		public void onStopped(StreamConsumer c, String message) {
			setStatus(message);
		}

		@Override
		public void onWarning(StreamConsumer c, String message) throws EInvalidData {
			setStatus(message);
		}
	}
	
	public DataSiftDataAccessView(){
		super();
	}
	
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
		
		validateButton.setText(START_SAMPLING_LABEL);
		
		validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(resource == null && urlComboBox.getItemCount() > 0){
					resource = urlComboBox.getItemAt(0);
				}
				
				if(validateButton.getText().compareTo(START_SAMPLING_LABEL) == 0){
					results.clear();
					
					validateButton.setText(STOP_SAMPLING_LABEL);
					
					DefaultCusor.startWaitCursor(rootPanel);
					
					setStatus("");
					dataTextArea.setText("");
					
					try {
						DataSiftConnectionProperties cp = (DataSiftConnectionProperties)AppCommons.TASKLET.getConnectionProperties();
						
						User user = new User(cp.getUsername(), cp.getApiKey());
	
						consumer = user.getConsumer(StreamConsumer.TYPE_HTTP, cp.getStreamHash(), new StreamConsumerEventHandler());
						consumer.consume();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(validateButton.getText().compareTo(STOP_SAMPLING_LABEL) == 0){
					validateButton.setText(START_SAMPLING_LABEL);

					DefaultCusor.stopWaitCursor(rootPanel);
					try {
						consumer.stop();
					} catch (EInvalidData e) {
						e.printStackTrace();
					}

					AppCommons.RESPONSE_OBJECT = results;
					
					// Update the meta-model
					AppCommons.TASKLET.setFilterMetadata(filterMetadata);
					AppCommons.TASKLET.setResource(null);	// TODO: review later
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
