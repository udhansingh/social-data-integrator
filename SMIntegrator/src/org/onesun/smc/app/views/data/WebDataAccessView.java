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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.onesun.commons.swing.cursors.DefaultCusor;
import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.smc.api.ProviderFactory;
import org.onesun.smc.api.WebProvider;
import org.onesun.smc.app.AppIcons;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.app.handlers.RequestUpdateHandler;
import org.onesun.smc.app.views.dialogs.SetterDialog;
import org.onesun.smc.core.connectors.WebConnector;
import org.onesun.smc.core.metadata.FilterMetadata;
import org.onesun.smc.core.providers.web.kapow.KapowObject;
import org.onesun.smc.core.resources.WebResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.kapowtech.robosuite.api.java.repository.construct.Attribute;
import com.kapowtech.robosuite.api.java.repository.construct.Type;
import com.kapowtech.robosuite.api.java.rql.RQLResult;
import com.kapowtech.robosuite.api.java.rql.construct.RQLObject;
import com.kapowtech.robosuite.api.java.rql.construct.RQLObjects;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class WebDataAccessView extends AbstractDataAccessView {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 7628767091382680614L;
	private SetterDialog			setterDialog		= new SetterDialog(AppCommonsUI.MAIN_WINDOW);
	private JComboBox<WebResource>	resourceComboBox	= new JComboBox<WebResource>(AppCommonsUI.WEB_RESOURCE_COMBOBOX_MODEL);
	private JTextArea				dataTextArea		= new JTextArea();
	private JScrollPane				scrollPane			= new JScrollPane(dataTextArea);
	private JButton					refreshButton 		= new JButton(AppIcons.getIcon("refresh"));

	public WebDataAccessView(){
		super();
	}

	@Override
	protected void preInit(){
	}

	@Override
	protected  void init(){
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(refreshButton, BorderLayout.WEST);
		panel.add(resourceComboBox, BorderLayout.CENTER);

		contentPanel.add(panel, BorderLayout.CENTER);

		JLabel label = new JLabel("Response Text");
		responsePanel.add(label,  BorderLayout.NORTH);
		responsePanel.add(scrollPane, BorderLayout.CENTER);

		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WebConnector c = (WebConnector)AppCommons.BUSINESS_OBJECT.getConnection();

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

		resourceComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(resourceComboBox.getSelectedIndex() > 0){
					WebResource resource = (WebResource)resourceComboBox.getSelectedItem();

					AppCommons.BUSINESS_OBJECT.setResource(resource);
					AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.BUSINESS_OBJECT.toJSON());
					AppCommonsUI.MODEL_TEXTAREA.invalidate();
				}
			}
		});

		dataTextArea.setEditable(false);

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
				DefaultCusor.startWaitCursor(rootPanel);

				WebResource r = (WebResource)resourceComboBox.getSelectedItem();
				WebResource resource = (WebResource)r.clone();
				if(resource == null) return;

				WebConnector c = (WebConnector)AppCommons.BUSINESS_OBJECT.getConnection();
				WebProvider provider = (WebProvider)ProviderFactory.getProvider(c.getIdentity(), "KAPOW");

				// TODO Set Headers, Parameters, ...
				Object object = null;

				if(provider != null){
					object = provider.execute(resource);

					if(object instanceof String){
						resource.setObject(object);
						resource.checkFormat();

						TextFormat textFormat = resource.getTextFormat();

						String statusText = "Data Format: " + textFormat.name();
						setStatus(statusText);

						final String response = resource.getFormattedText();

						if(response != null){
							dataTextArea.setText(response);
							AppCommons.RESPONSE_OBJECT = response;
						}
					}
					else if(object instanceof RQLResult){
						RQLResult retultObject = (RQLResult)object;

						Object o = resource.getObject();
						KapowObject kapowObject = null;

						if(o instanceof KapowObject){
							kapowObject = (KapowObject)o;
						}

						try {
							if(object != null){
								Type[] types = kapowObject.getReturnedTypes();

								DocumentBuilderFactory documentBuilderFactory = 
										DocumentBuilderFactory.newInstance();
								DocumentBuilder documentBuilder = 
										documentBuilderFactory.newDocumentBuilder();
								Document document = documentBuilder.newDocument();;
								Element root = document.createElement("root");

								for(Type type : types){
									String modelName = type.getTypeName();

									Element typeElement = document.createElement(modelName + "-Objects");
									
									RQLObjects objects = retultObject.getOutputObjectsByName(modelName);
									if(objects.size() > 0){
										for(int index = 0; index < objects.size(); index++){
											RQLObject rqlObject = (RQLObject) objects.get(index);

											Element parent = document.createElement("Object");
											

											Attribute[] attributes = type.getAttributes();
											for(Attribute attribute : attributes){
												String attributeName = attribute.getName();

												Element child = document.createElement(attributeName);
												Object vo = rqlObject.get(attributeName);
												if(vo instanceof String){
													child.setTextContent((String)vo);
												}
												
												parent.appendChild(child);
											}

											typeElement.appendChild(parent);
										}
									}

									root.appendChild(typeElement);
								}

								document.appendChild(root);

								OutputFormat format = new OutputFormat(document);
								format.setIndenting(true);

								ByteArrayOutputStream os = new ByteArrayOutputStream();
								
								XMLSerializer serializer = new XMLSerializer(os, format);
								serializer.serialize(document);
								
								os.close();
								
								String buffer = os.toString("UTF-8");
								
								dataTextArea.setText(buffer);
								AppCommons.RESPONSE_OBJECT = retultObject;
							}
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
						}
					}
				}

				dataTextArea.setWrapStyleWord(true);

				// Update the meta-model
				AppCommons.BUSINESS_OBJECT.setResource(r);
				AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.BUSINESS_OBJECT.toJSON());
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
