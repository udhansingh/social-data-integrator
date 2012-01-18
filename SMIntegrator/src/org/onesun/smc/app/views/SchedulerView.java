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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.onesun.commons.swing.SpringLayoutUtils;
import org.onesun.smc.app.AppIcons;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.core.model.Schedule;

public class SchedulerView  extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2344550068092238246L;

//	private SchedulerView rootPanel = this;
	
	private JTextField nameTextField = new JTextField();
	private JTextField secondsTextField = new JTextField("0");
	private JTextField minutesTextField = new JTextField("30");
	private JTextField hoursTextField = new JTextField("*");
	private JTextField dayOfMonthTextField = new JTextField("*");
	private JTextField monthTextField = new JTextField("*");
	private JTextField dayOfWeekTextField = new JTextField("*");
	private JTextField yearTextField = new JTextField("*");
	private JButton saveScheduleButton = new JButton("Schedule");
	
	public SchedulerView(){
		initControls();
	}
	
	private void initControls(){
		JPanel panel = new JPanel(new SpringLayout());
		JPanel subPanel = null;
		JLabel label = null;
		
		label = new JLabel("Name", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		nameTextField.setPreferredSize(new Dimension(150, 24));
		subPanel = new JPanel(new BorderLayout(5, 5));
		subPanel.add(nameTextField, BorderLayout.CENTER);
		ImageIcon helpIcon = AppIcons.getIcon("help");
		JButton helpButton = new JButton(helpIcon);
		subPanel.add(helpButton, BorderLayout.EAST);
		label.setLabelFor(subPanel);
		panel.add(subPanel);
		
		label = new JLabel("Seconds", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		secondsTextField.setPreferredSize(new Dimension(150, 24));
		subPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		subPanel.add(secondsTextField);
		subPanel.add(new JLabel("Allowed Values:[0-59], Special Characters:[,-*/]")).setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(subPanel);
		panel.add(subPanel);
		
		label = new JLabel("Minutes", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		minutesTextField.setPreferredSize(new Dimension(150, 24));
		subPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		subPanel.add(minutesTextField);
		subPanel.add(new JLabel("Allowed Values:[0-59], Special Characters:[,-*/]")).setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(subPanel);
		panel.add(subPanel);
		
		label = new JLabel("Hours", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		hoursTextField.setPreferredSize(new Dimension(150, 24));
		subPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		subPanel.add(hoursTextField);
		subPanel.add(new JLabel("Allowed Values:[0-23], Special Characters:[,-*/]")).setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(subPanel);
		panel.add(subPanel);
		
		label = new JLabel("Day of month", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		dayOfMonthTextField.setPreferredSize(new Dimension(150, 24));
		subPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		subPanel.add(dayOfMonthTextField);
		subPanel.add(new JLabel("Allowed Values:[1-31], Special Characters:[,-*?/LW]")).setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(subPanel);
		panel.add(subPanel);
		
		label = new JLabel("Month", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		monthTextField.setPreferredSize(new Dimension(150, 24));
		subPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		subPanel.add(monthTextField);
		subPanel.add(new JLabel("Allowed Values:[1-12 or JAN-DEC], Special Characters:[,-*/]")).setPreferredSize(new Dimension(300, 24));
		// SpringLayoutUtils.makeCompactGrid(subPanel, 1, 2, 5, 5,	5, 5);
		label.setLabelFor(subPanel);
		panel.add(subPanel);
		
		label = new JLabel("Day of Week", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		dayOfWeekTextField.setPreferredSize(new Dimension(150, 24));
		subPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		subPanel.add(dayOfWeekTextField);
		subPanel.add(new JLabel("Allowed Values:[1-7 or SUN-SAT], Special Characters:[,-*?/L#]")).setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(subPanel);
		panel.add(subPanel);
		
		label = new JLabel("Year", JLabel.LEADING);
		label.setPreferredSize(new Dimension(150, 24));
		panel.add(label);
		yearTextField.setPreferredSize(new Dimension(150, 24));
		subPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		subPanel.add(yearTextField);
		subPanel.add(new JLabel("Allowed Values:[empty, 1970-2099], Special Characters:[,-*/]")).setPreferredSize(new Dimension(300, 24));
		label.setLabelFor(subPanel);
		panel.add(subPanel);
		
		subPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		subPanel.add(saveScheduleButton);
		panel.add(subPanel);
		
		SpringLayoutUtils.makeCompactGrid(panel, 17, 1, 5, 5,	5, 5);
		
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.NORTH);
		
		// Default Values
//		nameTextField.setText(UUID.randomUUID().toString());
//		secondsTextField.setText("");
//		minutesTextField.setText("");
//		hoursTextField.setText("");
//		dayOfMonthTextField.setText("");
//		monthTextField.setText("");
//		dayOfWeekTextField.setText("");
//		yearTextField.setText("");
		
		// Handlers
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger"));
				} catch (IOException ex) {
				} catch (URISyntaxException ex) {
				}
			}
		});
		
		saveScheduleButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Schedule task = new Schedule();
				task.setName(nameTextField.getText());
				task.setSeconds(secondsTextField.getText());
				task.setMinutes(minutesTextField.getText());
				task.setHours(hoursTextField.getText());
				task.setDayOfMonth(dayOfMonthTextField.getText());
				task.setMonth(monthTextField.getText());
				task.setDayOfWeek(dayOfWeekTextField.getText());
				task.setYear(yearTextField.getText());
				
				// Update the meta-model
				AppCommons.BUSINESS_OBJECT.setSchedule(task);
	    		AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.BUSINESS_OBJECT.toJSON());
			}
		});
	}
}
