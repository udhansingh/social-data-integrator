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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class ChartDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8615404465470079746L;
	
	private Frame parent = null;
	
	public ChartDialog(Frame parent, JFreeChart moodChart, JFreeChart sentimentChart) {
		super(parent, true);
		
		this.parent = parent;
		
		this.setLayout(new BorderLayout(5, 5));
		
		setTitle("Chart");
		setSize(350, 225);
		setResizable(false);
		setVisible(false);
		
		JPanel chartPanel = new JPanel(new GridLayout(1, 2));
		
		chartPanel.add(new ChartPanel(moodChart));
		chartPanel.add(new ChartPanel(sentimentChart));
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
		panel.add(okButton);
		
		this.add(chartPanel, BorderLayout.CENTER);
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
		setVisible(false);
	}
}