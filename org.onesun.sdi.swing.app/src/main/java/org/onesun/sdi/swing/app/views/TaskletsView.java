package org.onesun.sdi.swing.app.views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.onesun.sdi.core.model.Tasklet;
import org.onesun.sdi.sil.data.HSQLDBService;
import org.onesun.sdi.sil.runtime.DataExtractionAgent;
import org.onesun.sdi.swing.app.AppCommons;
import org.onesun.sdi.swing.app.AppCommonsUI;

public class TaskletsView extends JPanel {
	private static final long serialVersionUID = 3550296359531493880L;
	private JTextField taskNameTextField = new JTextField();
	private JList<Tasklet> taskletsList = new JList<Tasklet>(AppCommonsUI.TASKLETS_MODEL);
	public TaskletsView(){
		super();
		
		this.setLayout(new BorderLayout(5, 5));
		
		initControls();
	}
	
	private void initControls(){
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
		
		JPanel subPanel = new JPanel(new BorderLayout(5, 5));
		subPanel.add(new JLabel("Unique Task Name"), BorderLayout.WEST);
		subPanel.add(taskNameTextField, BorderLayout.CENTER);
		
		JButton saveButton = new JButton("Save");
		JButton executeButton = new JButton("Execute");
		
		saveButton.addActionListener(new SaveActionListener());
		executeButton.addActionListener(new ExecuteActionListener());
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonsPanel.add(saveButton);
		buttonsPanel.add(executeButton);
		
		
		subPanel.add(buttonsPanel, BorderLayout.EAST);
		mainPanel.add(subPanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(AppCommonsUI.MODEL_TEXTAREA);
		subPanel = new JPanel(new BorderLayout(5, 5));
		subPanel.add(scrollPane, BorderLayout.CENTER);
		
		JPanel taskletsPanel = new JPanel(new BorderLayout(5, 5));
		subPanel.add(taskletsPanel, BorderLayout.EAST);
		
		mainPanel.add(subPanel, BorderLayout.CENTER);
	
		splitPane.add(mainPanel);
		
		subPanel = new JPanel(new BorderLayout(5, 5));
		subPanel.add(new JLabel("Saved Tasklets"), BorderLayout.NORTH);
		subPanel.add(new JScrollPane(taskletsList), BorderLayout.CENTER);
		
		taskletsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		taskletsList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent event) {
				int index = event.getFirstIndex();
				
				if(event.getValueIsAdjusting() == false){
					AppCommons.TASKLET = taskletsList.getModel().getElementAt(index);
					
					AppCommonsUI.MODEL_TEXTAREA.setText(AppCommons.TASKLET.toXML());
					AppCommonsUI.MODEL_TEXTAREA.invalidate();
				}
			}
		});
		
		splitPane.add(subPanel);
		splitPane.setDividerSize(5);
		
		double weight = splitPane.getResizeWeight(); // 0.0 by default
	    weight = .85D;
	    splitPane.setResizeWeight(weight);
	    
		this.add(splitPane, BorderLayout.CENTER);
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
				AppCommonsUI.TASKLETS_MODEL.addElement(tasklet);
				
				tasklet.save(AppCommons.PATH_TO_TASKLETS + name + ".task");
			}
		}
	}
	private class ExecuteActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			Tasklet t = AppCommons.TASKLET;
			
			DataExtractionAgent dea = new DataExtractionAgent();
			// TODO: Determine tablename to be used
			dea.setDataService(new HSQLDBService(null));
			dea.setCached(true);
			dea.setTasklet(t);
			
			try {
				dea.init(null);
				dea.execute();
				dea.deinit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
