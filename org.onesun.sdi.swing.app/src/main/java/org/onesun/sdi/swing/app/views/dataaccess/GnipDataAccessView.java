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

import org.onesun.commons.swing.cursors.DefaultCusor;
import org.onesun.sdi.core.metadata.FilterMetadata;
import org.onesun.sdi.core.model.connection.GnipConnection;
import org.onesun.sdi.core.resources.StreamingResource;
import org.onesun.sdi.sil.client.GnipStreamingClient;
import org.onesun.sdi.sil.handler.ConnectionHandler;
import org.onesun.sdi.sil.handler.DataHandler;
import org.onesun.sdi.swing.app.AppCommons;
import org.onesun.sdi.swing.app.AppCommonsUI;
import org.onesun.sdi.swing.app.handlers.RequestUpdateHandler;
import org.onesun.sdi.swing.app.views.dialogs.SetterDialog;

public class GnipDataAccessView extends AbstractDataAccessView {
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
	
	private List<String>					results					= Collections.synchronizedList(new ArrayList<String>());
	
	@Override
	public JPanel getViewPanel(){
		return this;
	}

	public GnipDataAccessView(){
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
					// Nothing to do as of now
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
				if(validateButton.getText().compareTo(START_SAMPLING_LABEL) == 0){
					results.clear();
					
					validateButton.setText(STOP_SAMPLING_LABEL);
					
					DefaultCusor.startWaitCursor(rootPanel);
					
					executor.setConnectionProperties((GnipConnection) AppCommons.TASKLET.getConnectionProperties());
					
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
	
	private GnipStreamingClient executor = new GnipStreamingClient(
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
}
