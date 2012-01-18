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
package org.onesun.smc.app.views.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.onesun.smc.app.handlers.UserSelection;

public class MetadataSelectionDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8615404465470079746L;
	
	private Frame parent = null;
	private String[] values = {};
	
	private UserSelection callback = null;
	
	private String[] selectedValues = {};
	private JList<String> list = new JList<String>(values);
	private JScrollPane scrollPane = new JScrollPane(list);
	
	public void setValues(String[] values){
		this.values = values;
		
		list.setListData(values);
		list.invalidate();
	}
	
	public MetadataSelectionDialog(Frame parent, UserSelection callback) {
		super(parent, true);
		
		this.callback = callback;
		
		this.parent = parent;
		
		this.setLayout(new BorderLayout(5, 5));
		
		setTitle("Select columns");
		setSize(450, 525);
		setResizable(false);
		setVisible(false);
		
		scrollPane.setPreferredSize(new Dimension(350, 225));
		this.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
		panel.add(okButton);
		this.add(panel, BorderLayout.SOUTH);
		
		UserActionListener userActionListener = new UserActionListener();
		okButton.addActionListener(userActionListener);
	}

	public void setVisible(boolean b) {
		if (b) {
			final Dimension parentSize = parent.getSize();
			final Point location = parent.getLocation();
			
			final int x = (parentSize.width - this.getWidth()) / 2;
			final int y = (parentSize.height - this.getHeight()) / 2;
			
			this.setLocation(location.x + x, location.y + y);
		}
		
		super.setVisible(b);
	}

	public void addNotify() {
		// Record the size of the window prior to calling parents addNotify.
		Dimension size = getSize();

		super.addNotify();

		if (frameSizeAdjusted)
			return;
		frameSizeAdjusted = true;

		// Adjust size of frame according to the insets
		Insets insets = getInsets();
		setSize(insets.left + insets.right + size.width, insets.top
				+ insets.bottom + size.height);
	}

	// Used by addNotify
	boolean frameSizeAdjusted = false;

	JButton okButton = new JButton("Ok");

	class UserActionListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Object object = event.getSource();
			if (object == okButton)
				Ok_actionPerformed(event);
		}
	}

	/**
	 * Called when ok is clicked.
	 * 
	 * @param event
	 */
	void Ok_actionPerformed(ActionEvent event) {
		List<String> sl = list.getSelectedValuesList();
		
		if(sl != null && sl.size() > 0){
			selectedValues = new String[sl.size()];
			
			int index = 0;
			for(String s : sl){
				selectedValues[index] = s;
				index++;
			}
			
			if(callback != null){
				callback.onReturn(selectedValues);
			}
		}
		
		setVisible(false);
	}
}