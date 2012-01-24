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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import org.onesun.smc.api.Filter;
import org.onesun.smc.api.FilterFactory;
import org.onesun.smc.api.Resource;
import org.onesun.smc.app.AppIcons;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.model.RequestParamModel;
import org.onesun.smc.core.metadata.FilterMetadata;
import org.onesun.smc.core.model.RequestParamObject;
import org.onesun.smc.core.model.Tasklet;

public class SetterView  extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2344550068092238246L;

	private SetterView rootPanel = this;

	private RequestParamModel paramsModel = new RequestParamModel();
	private JTable paramsTable = new JTable(paramsModel);
	private JScrollPane paramsScrollPane = new JScrollPane(paramsTable);

	private RequestParamModel headerModel = new RequestParamModel();
	private JTable headerTable = new JTable(headerModel);
	private JScrollPane headerScrollPane = new JScrollPane(headerTable);

	private JTextArea requestBodyTextArea = new JTextArea();
	private JScrollPane requestBodyScrollPane = new JScrollPane(requestBodyTextArea);


	public List<RequestParamObject> getParams(){
		return paramsModel.getData();
	}

	public List<RequestParamObject> getHeaders(){
		return headerModel.getData();
	}

	public String getRequestBody(){
		return requestBodyTextArea.getText();
	}

	public SetterView(){
		this.setLayout(new BorderLayout(5, 5));
		initControls();

	}

	public void initParamsModel(){
		Tasklet bobj = AppCommons.TASKLET;
		Resource r = null;
		paramsModel.removeAll();

		if(bobj != null){
			r = bobj.getResource();
		}

		Filter f = null;
		String name = null;
		if(r != null){
			name = r.getResourceName();
			f = FilterFactory.getFilterByIdentity(bobj.getConnection().getIdentity());
		}

		if(f != null && name != null){
			FilterMetadata fm = f.get(name);
			if(fm != null){
				bobj.setFilterMetadata(fm);

				for(String k : fm.paramsKeySet()){
					paramsModel.add(fm.getParamObject(k));
				}
			}
		}
		
		paramsTable.invalidate();
		rootPanel.invalidate();
		rootPanel.repaint();
	}

	private void initControls(){
		JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));

		// First
		JPanel subPanel = new JPanel(new BorderLayout(5, 5));
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		JButton insertButton = new JButton(AppIcons.getIcon("insertRow"));
		insertButton.setToolTipText("Append a request parameter");
		JButton deleteButton = new JButton(AppIcons.getIcon("deleteRow"));
		deleteButton.setToolTipText("Delete selected request parameter");

		insertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RequestParamObject o = new RequestParamObject("", "", "");
				paramsModel.add(o);

				paramsTable.invalidate();

				rootPanel.revalidate();
				rootPanel.repaint();
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = paramsTable.getSelectedRow();
				if(index >= 0){
					getParams().remove(index);

					rootPanel.revalidate();
					rootPanel.repaint();
				}
			}
		});
		buttonsPanel.add(insertButton);
		buttonsPanel.add(deleteButton);
		JPanel headerPanel = new JPanel(new GridLayout(1, 2));
		headerPanel.add(new JLabel("Query Parameters"));
		headerPanel.add(buttonsPanel);
		subPanel.add(headerPanel, BorderLayout.NORTH);
		subPanel.add(paramsScrollPane, BorderLayout.CENTER);
		panel.add(subPanel);

		// Second
		subPanel = new JPanel(new BorderLayout(5, 5));
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		insertButton = new JButton(AppIcons.getIcon("insertRow"));
		insertButton.setToolTipText("Append a header");
		deleteButton = new JButton(AppIcons.getIcon("deleteRow"));
		deleteButton.setToolTipText("Delete selected header");

		buttonsPanel.add(insertButton);
		buttonsPanel.add(deleteButton);

		insertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RequestParamObject o = new RequestParamObject("", "", "");
				headerModel.add(o);

				headerTable.invalidate();
				rootPanel.revalidate();
				rootPanel.repaint();
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = headerTable.getSelectedRow();
				if(index >= 0){
					getHeaders().remove(index);

					rootPanel.revalidate();
					rootPanel.repaint();
				}
			}
		});
		subPanel.add(buttonsPanel, BorderLayout.EAST);
		headerPanel = new JPanel(new GridLayout(1, 2));
		headerPanel.add(new JLabel("Headers"));
		headerPanel.add(buttonsPanel);
		subPanel.add(headerPanel, BorderLayout.NORTH);
		subPanel.add(headerScrollPane, BorderLayout.CENTER);
		panel.add(subPanel);

		// Third
		subPanel = new JPanel(new BorderLayout(5, 5));
		subPanel.add(new JLabel("Request Body"), BorderLayout.NORTH);
		subPanel.add(requestBodyScrollPane, BorderLayout.CENTER);
		panel.add(subPanel);

		this.add(panel, BorderLayout.CENTER);
	}
}
