package org.onesun.smc.app.views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WorkFlowView extends JPanel {
	private static final long serialVersionUID = 6213410716736655566L;

	private JPanel container = new JPanel();
	
	public WorkFlowView(){
		super();
		
		this.setSize(500, 500);
		this.setLayout(new BorderLayout(5, 5));
		
		container.setSize(500, 500);
		container.setLayout(null);
		
		initControls();
	}
	
	private void addComponentsToPane(Container pane) {
        pane.setLayout(null);
 
        JButton b1 = new JButton("one");
        JButton b2 = new JButton("two");
        JButton b3 = new JButton("three");
 
        pane.add(b1);
        pane.add(b2);
        pane.add(b3);
 
        Insets insets = pane.getInsets();
        Dimension size = b1.getPreferredSize();
        b1.setBounds(250 + insets.left, 5 + insets.top,
                     size.width, size.height);
        size = b2.getPreferredSize();
        b2.setBounds(500 + insets.left, 40 + insets.top,
                     size.width, size.height);
        size = b3.getPreferredSize();
        b3.setBounds(150 + insets.left, 15 + insets.top,
                     size.width + 50, size.height + 20);
    }
	
	private void initControls(){
		this.add(container, BorderLayout.CENTER);
		
		addComponentsToPane(container);
		
	}
}
