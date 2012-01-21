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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.onesun.commons.StreamUtils;
import org.onesun.commons.swing.ExtensionFileFilter;
import org.onesun.commons.swing.cursors.DefaultCusor;
import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.smc.api.Connector;
import org.onesun.smc.api.Resource;
import org.onesun.smc.app.AppMessages;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.core.connectors.FileSystemConnector;
import org.onesun.smc.core.resources.FileResource;

public class FileDataAccessView extends AbstractDataAccessView {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 7628767091382680614L;
	private FileDataAccessView		rootPanel			= this;
	private JFileChooser			chooser				= new JFileChooser();
	private JTextField				urlTextField		= new JTextField();
	private JTextArea				dataTextArea		= new JTextArea();
	private JScrollPane				scrollPane			= new JScrollPane(dataTextArea);
	
	public FileDataAccessView(){
		super();
	}
	
	@Override
	protected void preInit(){
	}
	
	@Override
	protected void init(){
		dataTextArea.setEditable(false);
		
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.add(urlTextField, BorderLayout.CENTER);
		
		contentPanel.add(panel, BorderLayout.CENTER);
		
		JLabel label = new JLabel("Response Text");
		responsePanel.add(label,  BorderLayout.NORTH);
		responsePanel.add(scrollPane, BorderLayout.CENTER);
		
		setterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Connector connection = AppCommons.BUSINESS_OBJECT.getConnection();
				
				if(connection != null){
					FileSystemConnector c = (FileSystemConnector)connection;
					String path = c.getPath();
					List<String> filterList = c.getFilter();
					
					chooser.setDialogTitle("Select a File");
					
					if(path != null && path.trim().length() > 0){
						chooser.setCurrentDirectory(new java.io.File(path));
					}
					else {
						chooser.setCurrentDirectory(new java.io.File("/"));
					}
					
					if(filterList != null && filterList.size() > 0){
						String[] filterArray = new String[filterList.size()];
						
						int index = 0;
						for(String value : filterList){
							filterArray[index] = value.trim();
							index++;
						}
						
						chooser.setFileFilter(new ExtensionFileFilter("Data Files", filterArray));
					}
				}
				
				int status = chooser.showOpenDialog(rootPanel);
				if(status == JFileChooser.APPROVE_OPTION){
					urlTextField.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		
		validateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = urlTextField.getText();
				
				if(fileName == null || (fileName != null && fileName.trim().length() == 0)){
					JOptionPane.showMessageDialog(rootPanel, AppMessages.ERROR_BAD_FILE_PATH);
					return;
				}
				
				DefaultCusor.startWaitCursor(rootPanel);
				
				Resource resource = new FileResource(fileName);
				File file = new File(fileName);
//				String[] tokens = fileName.split(File.separator);
				resource.setResourceName(file.getName());
//				if(tokens != null && tokens.length > 0){
//					tokens[tokens.length - 1]); 
//				}
				
				try{
					resource.setObject(StreamUtils.streamToString(file));
					resource.checkFormat();
					TextFormat textFormat = resource.getTextFormat();
					String statusText = "Data Format: " + textFormat.name();
					setStatus(statusText);
					
					final String response = resource.getFormattedText();
					
					if(response != null){
						dataTextArea.setText(response);
						AppCommons.RESPONSE_OBJECT = response;
					}
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				} 
				finally{
				}
				
				// Update the meta-model
				AppCommons.BUSINESS_OBJECT.setResource(resource);
	    		AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.BUSINESS_OBJECT.toJSON());
	    		AppCommonsUI.MODEL_TEXTAREA.invalidate();
				
	    		rootPanel.invalidate();
	    		rootPanel.repaint();
	    		
				DefaultCusor.stopWaitCursor(rootPanel);
			}
		});
	}
	
	@Override
	protected void postInit(){
	}
}
