package org.onesun.smc.app.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.core.model.Tasklet;

public class TaskletsView extends JPanel {
	private static final long serialVersionUID = 3550296359531493880L;
	private DefaultListModel<Tasklet> model = new DefaultListModel<Tasklet>();
	private JTextField taskNameTextField = new JTextField();
	private JList<Tasklet> taskletsList = new JList<Tasklet>(model);
	public TaskletsView(){
		super();
		
		this.setLayout(new BorderLayout(5, 5));
		
		initControls();
	}
	
	private void initControls(){
		JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
		
		JPanel subPanel = new JPanel(new BorderLayout(5, 5));
		subPanel.add(new JLabel("Unique Task Name"), BorderLayout.WEST);
		subPanel.add(taskNameTextField, BorderLayout.CENTER);
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new SaveActionListener());
		subPanel.add(saveButton, BorderLayout.EAST);
		mainPanel.add(subPanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(AppCommonsUI.MODEL_TEXTAREA);
		subPanel = new JPanel(new BorderLayout(5, 5));
		subPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel taskletsPanel = new JPanel(new BorderLayout(5, 5));
		subPanel.add(taskletsPanel, BorderLayout.EAST);
		
		mainPanel.add(subPanel, BorderLayout.CENTER);
	
		this.add(mainPanel, BorderLayout.CENTER);
		
		subPanel = new JPanel(new BorderLayout(5, 5));
		subPanel.add(new JLabel("Saved Tasklets"), BorderLayout.NORTH);
		subPanel.add(new JScrollPane(taskletsList), BorderLayout.CENTER);
		
		this.add(subPanel, BorderLayout.EAST);
	}
	
	private class SaveActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String name = taskNameTextField.getText().trim();
			
			if(name != null && name.length() > 0){
				AppCommons.TASKLET.setName(name);
				
				AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
				AppCommonsUI.MODEL_TEXTAREA.invalidate();
				
				Tasklet tasklet = (Tasklet)AppCommons.TASKLET.clone();
				model.addElement(tasklet);
				
				tasklet.save(AppCommons.PATH_TO_TASKLETS + name + ".task");
			}
		}
	}
}
