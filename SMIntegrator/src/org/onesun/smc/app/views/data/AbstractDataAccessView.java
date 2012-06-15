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
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.onesun.smc.api.DataAccessView;
import org.onesun.smc.core.metadata.FilterMetadata;

public abstract class AbstractDataAccessView extends JPanel implements DataAccessView {
	/**
	 * 
	 */
	private static final long 			serialVersionUID	= 9223315328195152836L;
	
	protected AbstractDataAccessView	rootPanel			= this;
	protected JButton					setterButton		= new JButton("Set...");
	protected JButton					validateButton		= new JButton("Validate");
	protected JPanel					contentPanel		= new JPanel(new BorderLayout(5, 5));
	protected JPanel					responsePanel		= new JPanel(new BorderLayout(5, 5));
	protected JTextField				statusTextField		= new JTextField();
	protected JPanel					statusPanel			= new JPanel(new BorderLayout(5, 5));
	
	protected FilterMetadata			filterMetadata		= null;
	
	@Override
	public abstract JPanel getViewPanel();
	
	public AbstractDataAccessView(){
		super();
	}

	@Override
	public final void initialize(){
		this.setLayout(new BorderLayout(5, 5));
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(setterButton);
		panel.add(validateButton);
		contentPanel.setPreferredSize(new Dimension(500, 33));
		contentPanel.add(panel, BorderLayout.EAST);
		this.add(contentPanel, BorderLayout.NORTH);
		
		this.add(responsePanel, BorderLayout.CENTER);
		
		statusTextField.setEditable(false);
		statusPanel.add(statusTextField, BorderLayout.CENTER);
		this.add(statusPanel, BorderLayout.SOUTH);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				preInit();

				init();

				postInit();

				finish();
			}
		});
	}

	protected abstract void preInit();
	
	protected abstract void init();
	
	protected abstract void postInit();
	
	private final void finish(){
		rootPanel.revalidate();
		rootPanel.repaint();
	}
	
	protected final void setStatus(String statusText){
		statusTextField.setText(statusText);
	}
}
