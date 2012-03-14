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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.onesun.commons.swing.JTableUtils;
import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.commons.swing.cursors.DefaultCusor;
import org.onesun.smc.api.DataService;
import org.onesun.smc.api.DataServicesFactory;
import org.onesun.smc.api.TextAnalysisService;
import org.onesun.smc.app.AppIcons;
import org.onesun.smc.app.AppMessages;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.app.handlers.UserSelection;
import org.onesun.smc.app.model.DatasetModel;
import org.onesun.smc.app.views.dialogs.ChartDialog;
import org.onesun.smc.app.views.dialogs.MetadataSelectionDialog;
import org.onesun.smc.core.metadata.Metadata;

public class DataServicesView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7628767091382680614L;
	private DataServicesView rootPanel = this;

	private JTable dataTable = new JTable();
	private DatasetModel datasetModel = null;
	private JScrollPane scrollPane = new JScrollPane(dataTable);
	private JButton copyDataButton = new JButton("Copy Data");
	private JButton executeButton = new JButton("Execute");
	private JButton computeMetricsButton = new JButton("Compute Metrics");
	private JLabel rowCountLabel = new JLabel("Rows: 0, Columns: 0");
	private JPanel containerPanel = new JPanel(new GridLayout(1, 2, 15, 5));
	
	private JList<String> dataServicesList = new JList<String>();
	private JList<String> selectedColumnList = new JList<String>();
	private String[] selectedColumnNames = null;

	public DataServicesView(){
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		createControls();

		addControlsToPanel();
	}

	private MetadataSelectionDialog metadataSelectionDialog = new MetadataSelectionDialog(AppCommonsUI.MAIN_WINDOW, new UserSelection() {
		@Override
		public void onReturn(String[] selectedValues) {
			selectedColumnList.setListData(selectedValues);
			
			selectedColumnNames = selectedValues;
		}
	});

	private void createControls(){
		
		copyDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					datasetModel = new DatasetModel();

					final Metadata origMetadata = AppCommonsUI.PREVIEW_DATASET_MODEL.getMetadata();
					Metadata metadata = new Metadata();
					// copy metadata
					for(String key : origMetadata.keySet()){
						metadata.put(key, origMetadata.get(key));
					}

					final List<Map<String, String>> origData = AppCommonsUI.PREVIEW_DATASET_MODEL.getData();
					List<Map<String, String>> data = new ArrayList<Map<String, String>>();
					// copy data
					for(Map<String, String> datum : origData){

						Map<String, String> record = new TreeMap<String, String>();

						for(String key : datum.keySet()){
							record.put(key, datum.get(key));
						}

						data.add(record);
					}
					
					datasetModel.setMetadata(metadata);
					datasetModel.setData(data);
					dataTable.setModel(datasetModel);
					rowCountLabel.setText("Rows: " + datasetModel.getRowCount() + ", Columns: " + datasetModel.getColumnCount());

					JTableUtils.packColumns(dataTable, 2);
					rowCountLabel.invalidate();
					dataTable.invalidate();
					dataTable.validate();
					scrollPane.invalidate();
				}				
		});
		
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(new JLabel("Enrichment Services"), BorderLayout.NORTH);
		dataServicesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		dataServicesList.setListData(DataServicesFactory.getServiceNames());
		dataServicesList.setPreferredSize(new Dimension(400, 300));
		JScrollPane listScrollPane = new JScrollPane(dataServicesList);
		panel.add(listScrollPane, BorderLayout.CENTER);
		containerPanel.add(panel);

		panel = new JPanel(new BorderLayout(5, 5));
		JPanel labelAndControlPanel = new JPanel(new BorderLayout(5, 5));
		labelAndControlPanel.add(new JLabel("Columns to be enriched"), BorderLayout.CENTER);
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JButton selectButton = new JButton(AppIcons.getIcon("select"));
		controlPanel.add(selectButton);
		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean columnsAvailable = false;
				if(datasetModel != null){
					Metadata metadata = datasetModel.getMetadata();
					if(metadata != null){
						columnsAvailable = true;
						int size = metadata.keySet().size();
						String[] values = new String[size];

						int index = 0;
						for(String key : metadata.keySet()){
							values[index] = key;
							index++;
						}

						metadataSelectionDialog.setValues(values);
					}
				}

				if(columnsAvailable == true){
					metadataSelectionDialog.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_NO_METADATA);
					return;
				}
			}
		});
		JButton clearButton = new JButton(AppIcons.getIcon("clear"));
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedColumnList.setListData(new String[]{});
				selectedColumnList.invalidate();
			}
		});
		controlPanel.add(clearButton);
		labelAndControlPanel.add(controlPanel, BorderLayout.EAST);
		panel.add(labelAndControlPanel, BorderLayout.NORTH);
		selectedColumnList.setPreferredSize(new Dimension(400, 300));
		listScrollPane = new JScrollPane(selectedColumnList);
		panel.add(listScrollPane, BorderLayout.CENTER);
		containerPanel.add(panel);

		executeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(AppCommons.TASKLET.getConnectionProperties() == null){
					JOptionPane.showMessageDialog(rootPanel, AppMessages.INFORMATION_CHOOSE_A_CONNECTION);
					return;
				}
				if(AppCommonsUI.PREVIEW_DATASET_MODEL == null || AppCommonsUI.PREVIEW_DATASET_MODEL.getRowCount() <= 0){
					JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_NO_DATA_TO_ENTRICH);
					return;
				}

				List<String> selectedServiceNames = dataServicesList.getSelectedValuesList();

				if((selectedServiceNames != null && selectedServiceNames.size() == 0) || (selectedColumnNames.length == 0)){
					JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_MISSING_DATA_SERVICE_AND_COLUMNS);
					return;
				}

				DefaultCusor.startWaitCursor(rootPanel);

				List<Map<String, String>> data = datasetModel.getData();
				Metadata metadata = datasetModel.getMetadata();
				
				for(String name : selectedServiceNames){
					DataService service = DataServicesFactory.getDataService(name);
					
					if(service instanceof TextAnalysisService){
						TextAnalysisService taService = (TextAnalysisService)service;
						
						List<Object> objects = new ArrayList<Object>();
						for(Map<String, String> datum : data){
							objects.add(datum);
						}
						taService.setData(objects);
						taService.setMetadata(metadata);
						taService.setColumns(selectedColumnNames);
						
						taService.execute();
					}
				}
				
				rowCountLabel.setText("Rows: " + data.size() + ", Columns: " + metadata.size());
				rowCountLabel.invalidate();

				datasetModel = new DatasetModel();
				datasetModel.setMetadata(metadata);
				datasetModel.setData(data);

				datasetModel.fireTableStructureChanged();
				datasetModel.fireTableDataChanged();
				dataTable.setModel(datasetModel);

				JTableUtils.packColumns(dataTable, 2);

				dataTable.invalidate();
				dataTable.validate();
				scrollPane.invalidate();

				DefaultCusor.stopWaitCursor(rootPanel);
			}
		});
		
		computeMetricsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(datasetModel == null || datasetModel.getRowCount() == 0){
					JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_NO_DATA);
					
					return;
				}
				
				List<Map<String, String>> data = datasetModel.getData();
				int upsetCount = 0;
				int happyCount = 0;

				int negativeCount = 0;
				int positiveCount = 0;
				
				for(Map<String, String> datum : data){
					for(String k : datum.keySet()){
						if(k.compareTo("uclassify_sentiment") == 0){
							String text = datum.get(k);
							
							String[] tokens = text.replace("{", "").replace("}", "").split(", ");
							for(String token : tokens){
								String[] nvp = token.split("=");
								
								Double score = Double.parseDouble(nvp[1]);
								
								if(nvp[0].compareToIgnoreCase("negative") == 0 && score > 50.0){
									negativeCount++;
								}
								
								if(nvp[0].compareToIgnoreCase("positive") == 0 && score >= 50.0){
									positiveCount++;
								}
							}
						}
						else if(k.compareTo("uclassify_mood") == 0){
							String text = datum.get(k);
							
							String[] tokens = text.replace("{", "").replace("}", "").split(", ");
							for(String token : tokens){
								String[] nvp = token.split("=");
								
								Double score = Double.parseDouble(nvp[1]);
								
								if(nvp[0].compareToIgnoreCase("upset") == 0 && score > 50.0){
									upsetCount++;
								}
								if(nvp[0].compareToIgnoreCase("happy") == 0 && score >= 50.0){
									happyCount++;
								}
							}
						}
					}
					
				}

				DefaultPieDataset moodDataset = new DefaultPieDataset();
				moodDataset.setValue("upset", upsetCount);
				moodDataset.setValue("happy", happyCount);
				
				DefaultPieDataset sentimentDataset = new DefaultPieDataset();
				sentimentDataset.setValue("negative", negativeCount);
				sentimentDataset.setValue("positive", positiveCount);
				
				JFreeChart moodChart = ChartFactory.createPieChart("How is mood?", moodDataset, true, true, true);
				JFreeChart sentimentChart = ChartFactory.createPieChart("How is sentiment?", sentimentDataset, true, true, true);
				
				ChartDialog cd = new ChartDialog(AppCommonsUI.MAIN_WINDOW, moodChart, sentimentChart);
				cd.setSize(new Dimension(900, 500));
				cd.setVisible(true);
			}
		});
	}

	private void addControlsToPanel(){
		dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		dataTable.setAutoscrolls(true);

		JPanel panel = null;

		panel = new JPanel(new SpringLayout());
		panel.add(copyDataButton);
		SpringLayoutUtils.makeCompactGrid(panel, 1, 1, 5, 5,	5, 5);
		this.add(panel);
		
		panel = new JPanel(new SpringLayout());
		panel.add(containerPanel);
		SpringLayoutUtils.makeCompactGrid(panel, 1, 1, 5, 5,	5, 5);
		this.add(panel);

		panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.add(executeButton);
		panel.add(computeMetricsButton);
		this.add(panel);

		panel = new JPanel(new SpringLayout());
		JLabel label = new JLabel("Enriched Data", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		scrollPane.setPreferredSize(new Dimension(250, 900));

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
