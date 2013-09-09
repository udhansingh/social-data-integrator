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
package org.onesun.sdi.swing.app.views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

import org.json.JSONException;
import org.onesun.commons.swing.JTableUtils;
import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.commons.swing.cursors.DefaultCusor;
import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.sdi.core.api.Connection;
import org.onesun.sdi.core.api.DataProfiler;
import org.onesun.sdi.core.api.DataReader;
import org.onesun.sdi.core.api.Resource;
import org.onesun.sdi.core.api.ServiceProvider;
import org.onesun.sdi.core.data.profiler.SimpleDataProfiler;
import org.onesun.sdi.core.data.reader.FacetedDataReader;
import org.onesun.sdi.core.data.reader.JSONDataReader;
import org.onesun.sdi.core.data.reader.XMLDataReader;
import org.onesun.sdi.core.metadata.Metadata;
import org.onesun.sdi.core.resources.WebResource;
import org.onesun.sdi.spi.api.ProviderFactory;
import org.onesun.sdi.spi.web.kapow.KapowDataReader;
import org.onesun.sdi.spi.web.kapow.KapowObject;
import org.onesun.sdi.swing.app.AppCommons;
import org.onesun.sdi.swing.app.AppCommonsUI;
import org.onesun.sdi.swing.app.AppMessages;
import org.onesun.sdi.swing.app.handlers.UITask;
import org.onesun.sdi.swing.app.model.DatasetModel;
import org.onesun.sdi.swing.app.views.shared.ColoredTable;

import com.kapowtech.robosuite.api.java.rql.construct.RQLObject;

public class DataPreviewTableView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7796284674232922679L;

	private DataPreviewTableView rootPanel = this;

	private ColoredTable dataTable = new ColoredTable();
	private JScrollPane scrollPane = new JScrollPane(dataTable);
	private JLabel rowCountLabel = new JLabel("Rows: 0, Columns: 0");
	private UITask metadataRefeshCompleted = null;
	
	public void setMetadataRefeshCompleted(UITask metadataRefeshCompleted) {
		this.metadataRefeshCompleted = metadataRefeshCompleted;
	}

	public DataPreviewTableView(){
		super();
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		createControls();

		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
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

	public void generateDataPreview(Boolean profiling) {
		Connection cp = AppCommons.TASKLET.getConnectionProperties();

		if(cp == null){
			JOptionPane.showMessageDialog(rootPanel, AppMessages.INFORMATION_CHOOSE_A_CONNECTION);
			return;
		}

		String providerName = cp.getIdentity();
		ServiceProvider providerInstance = null;
		if(providerName != null){
			providerInstance = ProviderFactory.getProvider(providerName);
		}

		if(providerInstance != null && providerInstance.isResponseRequired() == true){
			if(AppCommons.RESPONSE_OBJECT == null){
				JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_NO_PREVIEW_NEEDS_DATA_AND_METADATA);

				return;
			}
		}

		DefaultCusor.startWaitCursor(rootPanel);
		DataReader dataReader = null;

		Resource resource = AppCommons.TASKLET.getResource();
		Metadata metadata = AppCommons.TASKLET.getMetadata();

		if((metadata == null) || (metadata != null && metadata.isDiscovered() == false)){
			if(providerInstance.getCategory().compareToIgnoreCase("KAPOW") == 0){
				WebResource webResource = (WebResource)resource;
				WebResource clone = (WebResource)webResource.clone();

				Object returnObject = AppCommons.RESPONSE_OBJECT;

				if(returnObject instanceof List){
					@SuppressWarnings("unchecked")
					List<RQLObject> rqlObjects = (List<RQLObject>) returnObject;
					
					dataReader = new KapowDataReader((KapowObject)clone.getObject(), rqlObjects);
				}
			}
			else if(providerInstance.getCategory().compareToIgnoreCase("TWITTER_STREAMING") == 0){
				Object returnObject = AppCommons.RESPONSE_OBJECT;

				if(returnObject instanceof List){
					@SuppressWarnings("unchecked")
					List<String> list = (List<String>) returnObject;
					
					dataReader = new FacetedDataReader(list);
				}
			}
		}
		else if((metadata != null && metadata.isDiscovered() == true)){
			// Apply XPath Rules
			TextFormat textFormat = resource.getTextFormat();

			try{
				if(textFormat == TextFormat.JSON){
					dataTable.setDataType(TextFormat.JSON);
					dataReader = new JSONDataReader((String)AppCommons.RESPONSE_OBJECT);
				}
				else if(textFormat == TextFormat.XML) {
					dataTable.setDataType(TextFormat.XML);
					dataReader = new XMLDataReader((String)AppCommons.RESPONSE_OBJECT);
				}
				else if(textFormat == TextFormat.FACETED_JSON) {
					dataTable.setDataType(TextFormat.FACETED_JSON);
					dataReader = new FacetedDataReader(AppCommons.RESPONSE_OBJECT);
				}
			} catch (JSONException ex) {
				ex.printStackTrace();
			}finally {
			}
		}

		dataReader.setMetadata(metadata);
		dataReader.initialize();
		dataReader.load();

		List<Map<String, String>> data = dataReader.getData();

		if(profiling == true){
			DataProfiler profiler = new SimpleDataProfiler();
			profiler.setData(data);
			profiler.setMetadata(metadata);
			profiler.execute();
		}
		
		AppCommonsUI.PREVIEW_DATASET_MODEL = new DatasetModel();
		AppCommonsUI.PREVIEW_DATASET_MODEL.setMetadata(metadata);
		AppCommonsUI.PREVIEW_DATASET_MODEL.setData(data);
		
		if(metadataRefeshCompleted != null){
			metadataRefeshCompleted.execute(metadata);
		}

		dataTable.setModel(AppCommonsUI.PREVIEW_DATASET_MODEL);

		rowCountLabel.setText("Rows: " + data.size() + ", Columns: " + metadata.size());
		rowCountLabel.invalidate();

		JTableUtils.packColumns(dataTable, 2);

		dataTable.invalidate();
		dataTable.validate();
		scrollPane.invalidate();

		DefaultCusor.stopWaitCursor(rootPanel);
	}

	private void createControls(){
		dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		dataTable.setAutoscrolls(true);

		JPanel panel = null;
		JLabel label = null;

		panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		panel = new JPanel(new SpringLayout());
		label = new JLabel("Data Preview", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));

		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		panel.add(label);
		label.setLabelFor(scrollPane);
		panel.add(scrollPane);
		SpringLayoutUtils.makeCompactGrid(panel, 2, 1, 5, 5,	5, 5);
		this.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(rowCountLabel);
		this.add(panel);
	}
}
