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
package org.onesun.smc.app.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;
import org.onesun.commons.swing.JTableUtils;
import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.commons.swing.cursors.DefaultCusor;
import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.smc.api.ConnectionProperties;
import org.onesun.smc.api.DataPreviewer;
import org.onesun.smc.api.MetadataReader;
import org.onesun.smc.api.ProviderFactory;
import org.onesun.smc.api.Resource;
import org.onesun.smc.api.ServiceProvider;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.app.AppMessages;
import org.onesun.smc.app.model.MetadataTableModel;
import org.onesun.smc.core.metadata.FacetedMetadata;
import org.onesun.smc.core.metadata.FilterMetadata;
import org.onesun.smc.core.metadata.JSONMetadataReader;
import org.onesun.smc.core.metadata.MasterMetadataMerger;
import org.onesun.smc.core.metadata.Metadata;
import org.onesun.smc.core.metadata.XMLMetadataReader;
import org.onesun.smc.core.model.MetaObject;
import org.onesun.smc.core.model.Parameter;
import org.onesun.smc.core.providers.web.kapow.KapowObject;
import org.onesun.smc.core.resources.WebResource;

import com.kapowtech.robosuite.api.java.repository.construct.Attribute;
import com.kapowtech.robosuite.api.java.repository.construct.Type;

public class MetadataTableView extends JPanel {
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(MetadataTableView.class);
	private static final long serialVersionUID = -7796284674232922679L;

	private MetadataTableView rootPanel = this;

	private MetadataTableModel model = null;
	private JTable table = new JTable(model);
	private JScrollPane scrollPane = new JScrollPane(table);
	private JTextField nodeNameTextField = new JTextField();
	private JButton discoverSchemaButton = new JButton("Discover Schema");
	private JButton mergeSchemaButton = new JButton("Merge To Master");

	private JLabel facetedLabel = new JLabel("Pattern");
	private JPanel primaryButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
	private JPanel secondaryPanel = new JPanel(new BorderLayout(5, 5));

	private JCheckBox isFaceted = new JCheckBox();
	private JComboBox<String> schemaFacets = new JComboBox<String>();
	private FacetedMetadata facetedMetadata = null;
	private Metadata metadata = null;
	private DataPreviewer dataPreviewer = null;

	public MetadataTableView(){
		super();
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		createControls();

		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				if(AppCommons.AUTHENTICATION.compareTo("TWITTER_STREAMING") == 0){
					schemaFacets.setEnabled(true);
				}
				else {
					schemaFacets.setEnabled(false);
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
	}

	private void createControls(){
		Dimension viewPort = new Dimension(250, 900);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setAutoscrolls(true);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(true);
		table.getTableHeader().setReorderingAllowed(false);

		JPanel panel = null;
		JLabel label = null;

		panel = new JPanel(new BorderLayout(5, 5));
		label = new JLabel("XPath to item (Optional)");
		label.setPreferredSize(new Dimension(200, 24));
		panel.add(label, BorderLayout.WEST);
		nodeNameTextField.setPreferredSize(new Dimension(500, 24));
		panel.add(nodeNameTextField, BorderLayout.CENTER);
		this.add(panel);

		panel = new JPanel(new BorderLayout(5, 5));
		label = new JLabel("Is data assorted?");
		label.setPreferredSize(new Dimension(200, 24));
		panel.add(label, BorderLayout.WEST);
		panel.add(isFaceted, BorderLayout.CENTER);
		isFaceted.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				AbstractButton abstractButton = (AbstractButton)e.getSource();
				ButtonModel buttonModel = abstractButton.getModel();
				boolean pressed = buttonModel.isPressed();
				boolean selected = buttonModel.isSelected();

				if(pressed == true && selected == true){
					primaryButtonsPanel.remove(mergeSchemaButton);
					facetedLabel.setPreferredSize(new Dimension(200, 24));
					secondaryPanel.add(facetedLabel, BorderLayout.WEST);
					secondaryPanel.add(schemaFacets, BorderLayout.CENTER);
					secondaryPanel.add(mergeSchemaButton, BorderLayout.EAST);
				}
				else if(pressed == true && selected == false){
					facetedLabel.setPreferredSize(new Dimension(200, 24));
					secondaryPanel.remove(facetedLabel);
					secondaryPanel.remove(schemaFacets);
					secondaryPanel.remove(mergeSchemaButton);
					primaryButtonsPanel.add(mergeSchemaButton);
				}

				rootPanel.revalidate();
				rootPanel.repaint();
			}
		});
		primaryButtonsPanel.add(discoverSchemaButton);
		primaryButtonsPanel.add(mergeSchemaButton);
		panel.add(primaryButtonsPanel, BorderLayout.EAST);
		this.add(panel);

		// Optional Panel (programatically triggered)
		this.add(secondaryPanel);

		panel = new JPanel(new SpringLayout());
		label = new JLabel("Schema (Flattened)", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		scrollPane.setPreferredSize(viewPort);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel.add(label);
		label.setLabelFor(scrollPane);
		panel.add(scrollPane);
		SpringLayoutUtils.makeCompactGrid(panel, 2, 1, 5, 5,	5, 5);
		this.add(panel);

		schemaFacets.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object o = e.getItem();

				if(o instanceof String){
					String facet = (String)o;

					if(facetedMetadata != null){
						model = new MetadataTableModel();
						metadata = facetedMetadata.getMetadata(facet);
						
						model.setMetadata(metadata);
						model.fireTableDataChanged();

						table.setModel(model);

						JTableUtils.packColumns(table, 2);
						JTableUtils.packRows(table, 2);

						table.invalidate();
						scrollPane.invalidate();
						scrollPane.repaint();

						// Update the meta-model
						AppCommons.TASKLET.setMetadata(metadata);
						AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
						AppCommonsUI.MODEL_TEXTAREA.invalidate();
					}
				}
			}
		});

		mergeSchemaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String providerName = AppCommons.TASKLET.getConnection().getIdentity();
				String resourceName = AppCommons.TASKLET.getResource().getResourceName();

				String folderName = null;
				if(AppCommons.AUTHENTICATION.compareTo("TWITTER_STREAMING") == 0){
					folderName = resourceName;
					resourceName = (String)schemaFacets.getSelectedItem();

					Metadata m = facetedMetadata.getMetadata(resourceName);
					MasterMetadataMerger merger = new MasterMetadataMerger(providerName, folderName, resourceName, AppCommons.PATH_TO_MASTER_METADATA);
					merger.merge(m);
				}
				else {
					if(providerName == null){
						providerName = "General";
					}

					MasterMetadataMerger merger = new MasterMetadataMerger(providerName, null, resourceName, AppCommons.PATH_TO_MASTER_METADATA);
					merger.merge(metadata);
				}
			}
		});


		discoverSchemaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppCommons.TASKLET.setMetadata(null);
				
				ConnectionProperties connection = AppCommons.TASKLET.getConnection();

				if(connection == null){
					JOptionPane.showMessageDialog(rootPanel, AppMessages.INFORMATION_CHOOSE_A_CONNECTION);
					return;
				}

				String providerName = connection.getIdentity();
				ServiceProvider providerInstance = null;
				if(providerName != null){
					providerInstance = ProviderFactory.getProvider(providerName);
				}

				if(providerInstance != null && providerInstance.isResponseRequired() == true){
					if(AppCommons.RESPONSE_OBJECT == null){
						JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_NO_DATA_TO_EXTRACT_METADATA);
						return;
					}
				}

				DefaultCusor.startWaitCursor(rootPanel);

				model = new MetadataTableModel();

				MetadataReader metadataReader = null;
				if(connection.getCategory().compareTo("KAPOW") == 0){
					if(providerInstance != null && providerInstance.isResponseRequired() == false){
						metadata = new Metadata();
						metadata.setDiscovered(false);

						WebResource resource = (WebResource)AppCommons.TASKLET.getResource();
						if(resource != null){
							KapowObject object = null;

							Object o = resource.getObject();
							if(o instanceof KapowObject){
								object = (KapowObject)o;
							}
							else {
								logger.info(o);
							}

							if(object != null){
								Type[] types = object.getReturnedTypes();
								for(Type type : types){
									Attribute[] attributes = type.getAttributes();

									for(Attribute attribute : attributes){
										String xpath = type.getTypeName() + "/" + attribute.getName();
										String name = attribute.getName();

										MetaObject mo  = new MetaObject();
										mo.setPath(xpath);
										mo.setName(name);

										metadata.put(xpath, mo);
									}
								}
							}
						}
					}
				}
				else if(connection.getCategory().compareTo("TWITTER_STREAMING") == 0) {
					facetedMetadata = new FacetedMetadata();
					facetedMetadata.setObject(AppCommons.RESPONSE_OBJECT.toString());
					facetedMetadata.analyze();

					Set<String> facets = facetedMetadata.getFacets();
					String[] facetsArray = new String[facets.size()];
					int index = 0;
					for(String facet : facets){
						facetsArray[index] = facet;
						index++;

						Metadata m = facetedMetadata.getMetadata(facet);
						m.setUrl(AppCommons.TASKLET.getResource().getUrl());
						m.setVerb(AppCommons.TASKLET.getResource().getVerb().name());
					}

					ComboBoxModel<String> model = new DefaultComboBoxModel<String>(facetsArray);
					schemaFacets.setModel(model);
				}
				else {
					Resource resource = AppCommons.TASKLET.getResource();
					TextFormat textFormat = TextFormat.UNKNOWN;

					if(resource != null){
						textFormat = resource.getTextFormat();
					}

					// Init metadata reader
					if(textFormat == TextFormat.JSON){
						metadataReader = new JSONMetadataReader(AppCommons.RESPONSE_OBJECT.toString());
					}
					else if (textFormat == TextFormat.XML){
						metadataReader = new XMLMetadataReader(AppCommons.RESPONSE_OBJECT.toString());
					}
					else {
						DefaultCusor.stopWaitCursor(rootPanel);

						return;
					}

					// Fill other details
					String nodeName = nodeNameTextField.getText();
					if(nodeName != null && nodeName.trim().length() > 0){
						metadataReader.beginProcessingAt(nodeName);
					}

					metadataReader.setLeafNodesOnly(false);
					metadataReader.setDepth(3);

					try{
						metadataReader.initialize();
						metadataReader.generateMetadata();
					} catch (Exception ex) {
						ex.printStackTrace();
					}finally{
					}

					metadata = metadataReader.getMetadata();
					metadata.setName(AppCommons.TASKLET.getResource().getResourceName());
					metadata.setNodeName(nodeName);
					metadata.setUrl(AppCommons.TASKLET.getResource().getUrl());
					metadata.setVerb(AppCommons.TASKLET.getResource().getVerb().name());

					metadata.compact();

					// Fill FilterMetadata
					FilterMetadata fm = AppCommons.TASKLET.getFilterMetadata();
					final String suffix = "social.media.internal.metadata.mapping.";
					if(fm != null){
						for(Parameter o : fm.paramValues()){
							String name = o.getInternalName();
							String xpath = suffix + "param." + name;

							MetaObject mo  = new MetaObject();
							mo.setPath(xpath);
							mo.setName(name);

							metadata.put(xpath, mo);
						}

						for(Parameter o : fm.headerValues()){
							String name = o.getInternalName();
							String xpath = suffix + "header." + name;

							MetaObject mo  = new MetaObject();
							mo.setPath(xpath);
							mo.setName(name);

							metadata.put(xpath, mo);
						}


						Parameter o = fm.getPayload();
						String payload = null;
						if(o != null){
							payload = o.getDefaultValue();
						}

						if(payload != null){
							String name = o.getInternalName();
							String xpath = suffix + "payload." + name;

							MetaObject mo = new MetaObject();
							mo.setPath(xpath);
							mo.setName(name);

							metadata.put(xpath, mo);
						}
					}
				}

				updateView();
				
				boolean status = model.getRowCount() > 0;
				if(status == true && dataPreviewer != null){
					dataPreviewer.generate();
				}
				
				DefaultCusor.stopWaitCursor(rootPanel);
			}
		});
	}

	public void updateView(){
		model = new MetadataTableModel();
		model.setMetadata(metadata);
		table.setModel(model);

		// Update Renderer
/*		
		int vColIndex = 2;
		TableColumn col = table.getColumnModel().getColumn(vColIndex);

		DataType[] dataTypes = DataTypeFactory.getDataTypesArray();
		
		col.setCellEditor(new ComboBoxEditor<DataType>(dataTypes));
		col.setCellRenderer(new ComboBoxRenderer<DataType>(dataTypes));
		
		// Ignore while processing
		vColIndex = 4;
		col = table.getColumnModel().getColumn(vColIndex);

		Boolean[] booleanArray = {true, false};

		col.setCellEditor(new ComboBoxEditor<Boolean>(booleanArray));
		col.setCellRenderer(new ComboBoxRenderer<Boolean>(booleanArray));
		*/
		
		//				copySchemaButton.setEnabled(status);

		// Update the meta-model
		AppCommons.TASKLET.setMetadata(metadata);
		AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
		AppCommonsUI.MODEL_TEXTAREA.invalidate();

		JTableUtils.packColumns(table, 2);
		JTableUtils.packRows(table, 2);

		table.invalidate();
		scrollPane.invalidate();
		scrollPane.repaint();
	}
	
	public DataPreviewer getDataPreviewer() {
		return dataPreviewer;
	}

	public void setDataPreviewer(DataPreviewer dataPreviewer) {
		this.dataPreviewer = dataPreviewer;
	}

//*	
	private static class ComboBoxRenderer<T> extends JComboBox<T> implements TableCellRenderer {
		private static final long serialVersionUID = 8017092061769520797L;

		public ComboBoxRenderer(T[] items) {
			super(items);
		}

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}

			// Select the current value
			setSelectedItem(value);
			
			return this;
		}
	}

	private static class ComboBoxEditor<T> extends DefaultCellEditor {
		private static final long serialVersionUID = 5104344847656280260L;

		public ComboBoxEditor(T[] items) {
			super(new JComboBox<T>(items));
		}
	}
// */
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
}