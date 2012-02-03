package org.onesun.smc.app.views;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.onesun.smc.api.DataService;
import org.onesun.smc.api.DataServicesFactory;

public class WorkFlowView extends JPanel {
	private static final long serialVersionUID = 6213410716736655566L;
	
	private JPanel workflowPanel = new JPanel();
	private JPanel servicesPanel = new JPanel(new BorderLayout(5, 5));
	
	private JList<String> servicesList = new JList<String>(DataServicesFactory.getServiceNames());
	private JScrollPane servicesScrollPane = new JScrollPane(servicesList);
	
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, workflowPanel, servicesPanel);

	public WorkFlowView(){
		super();
		
		this.setLayout(new BorderLayout(5, 5));
		
		initControls();
	}
	
	private void initControls(){
		servicesPanel.add(new JLabel("Registered Services"), BorderLayout.NORTH);
		servicesPanel.add(servicesScrollPane, BorderLayout.CENTER);
		
		splitPane.setDividerSize(5);
		this.add(splitPane, BorderLayout.CENTER);
		
		double weight = splitPane.getResizeWeight(); // 0.0 by default
	    weight = .85D;
	    splitPane.setResizeWeight(weight);
	}
}
