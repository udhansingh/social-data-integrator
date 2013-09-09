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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;
import org.onesun.commons.swing.JTableUtils;
import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.sdi.core.api.DataReader;
import org.onesun.sdi.core.api.MetadataReader;
import org.onesun.sdi.core.data.reader.JSONDataReader;
import org.onesun.sdi.core.data.reader.XMLDataReader;
import org.onesun.sdi.core.metadata.JSONMetadataReader;
import org.onesun.sdi.core.metadata.Metadata;
import org.onesun.sdi.core.metadata.XMLMetadataReader;
import org.onesun.sdi.swing.app.views.shared.ColoredTable;


public class ColoredTableView extends JFrame implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2839380193105378466L;
	
	public boolean isValid = false;
	final static boolean 	DEBUG = false;
	TextFormat dataType;
	
		
	ColoredTable dataTable;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		final JFrame mainFrame = new JFrame();
		mainFrame.setLocationRelativeTo(null);
		//JFrame.setDefaultLookAndFeelDecorated(true); 
		
		final JButton FileButton = new JButton("No file selected");
		FileButton.setPreferredSize(new Dimension(40, 20));
		
		final JFileChooser fileChooser = new JFileChooser();
		mainFrame.add(FileButton,BorderLayout.PAGE_START);
		
		final JTextArea textArea = new JTextArea(20,50);
		//mainFrame.add(textArea);
		textArea.setMinimumSize(new Dimension(600,800));
		JScrollPane scrollPane = new JScrollPane(textArea);
	    mainFrame.add(scrollPane,BorderLayout.CENTER);
	    
	    final JButton ProcessButton = new JButton("Process text as JSON");
	    ProcessButton.setPreferredSize(new Dimension(40, 20));
	    mainFrame.add(ProcessButton,BorderLayout.PAGE_END);
	    
	    ProcessButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				new ColoredTableView(textArea.getText(),TextFormat.JSON);
				mainFrame.pack();
			}
		});
		
		FileButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION){
					File dataFile = fileChooser.getSelectedFile();
					fileChooser.setCurrentDirectory(dataFile.getParentFile());
					FileButton.setText(dataFile.getAbsolutePath());
					try {
						BufferedReader reader = new BufferedReader(new FileReader(dataFile));
						String jsonString = "";
						String line = null;
						while((line = reader.readLine()) != null)
							jsonString += line;
						textArea.setText(jsonString);
						new ColoredTableView(jsonString,TextFormat.JSON);
						mainFrame.pack();
						
						reader.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle("JSON Data Viewer");
		mainFrame.validate();
		mainFrame.pack();
		mainFrame.setVisible(true);
		
	}
		
	public ColoredTableView(String dataString, TextFormat dataType){
		super();
		dataTable = new ColoredTable();
		
		this.setLayout(new BorderLayout(5, 5));
		
		setTitle("Data Viewer");
		this.dataType = dataType;
		
		ROTableModel tableModel =  new ROTableModel();
		dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		dataTable.setModel(tableModel);
		dataTable.setDataType(dataType);
		
		
		MetadataReader metadataReader = null;
		DataReader dataReader = null;
		try {
			if(dataType == TextFormat.JSON){
				metadataReader = new JSONMetadataReader(dataString);
				dataReader = new JSONDataReader(dataString);
			}
			else if(dataType == TextFormat.XML){
				metadataReader = new XMLMetadataReader(dataString);
				dataReader = new XMLDataReader(dataString);
			}
			
			metadataReader.initialize();
			metadataReader.generateMetadata();
			Metadata metadata = metadataReader.getMetadata();
			for(String columnName : metadata.keySet()){
				tableModel.addColumn(columnName);
			}
			
			dataReader.setMetadata(metadata);
			dataReader.initialize();
			dataReader.load();
			List<Map<String,String>> data = dataReader.getData();
			for(Map<String,String> row : data){
				tableModel.addRow(row.values().toArray());
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.dispose();
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.dispose();
			return;
		}
		JTableUtils.packColumns(dataTable, 2);
		
		JScrollPane scrollPane = new JScrollPane(dataTable);
		this.add(scrollPane, BorderLayout.CENTER);
	    dataTable.addMouseListener(this);
	    dataTable.validate();
	
	    this.setVisible(true);
	    this.pack();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getClickCount() == 4){
			JTable table = (JTable)e.getSource();
			int col = table.getSelectedColumn();
			int row = table.getSelectedRow();
			ColoredTableView dataFrame = new ColoredTableView((String) table.getValueAt(row,col),dataType);
			dataFrame.setTitle(table.getColumnName(col));
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private class ROTableModel extends DefaultTableModel{
		
		private static final long serialVersionUID = 8860433276461630554L;
		ROTableModel(){
			super();
		}
		public boolean isCellEditable(int row,int column){
			return false;
		}
	}
}
