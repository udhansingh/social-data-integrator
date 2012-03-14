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

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.onesun.smc.api.ConnectionProperties;
import org.onesun.smc.app.AppCommons;
import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.app.views.data.DataAccessViews;
import org.onesun.smc.core.model.Tasklet;

public class AppMainView extends JPanel{
	/**
	 * 
	 */
	private JPanel rootPanel = this;
	private static final long serialVersionUID = -3894385454350008478L;
	private JTabbedPane tab = new JTabbedPane();

	public AppMainView(){
		tab.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				final JTabbedPane pane = (JTabbedPane)e.getSource();
				final int index = pane.getSelectedIndex();

				String title = null;
				if(index >= 0){
					title = pane.getTitleAt(index);
				}

				// Update Data Access View when tab changes
				if(title != null && title.compareTo("Data Access") == 0){
					Tasklet bobj = AppCommons.TASKLET;
					boolean displayed = false;
					
					if(bobj != null){
						ConnectionProperties cp = bobj.getConnectionProperties();
						if(cp != null){
							String category = cp.getCategory();

							if(category.compareTo("GENERAL") == 0){
								pane.setComponentAt(index, DataAccessViews.RESTFUL_DATA_ACCESS_INPUT_VIEW);
								displayed = true;
							}
							else if(category.compareTo("FILE_SYSTEM") == 0){
								pane.setComponentAt(index, DataAccessViews.FILE_DATA_ACCESS_VIEW);
								displayed = true;
							}
							else if(category.compareTo("SOCIAL_MEDIA") == 0){
								pane.setComponentAt(index, DataAccessViews.SOCIAL_DATA_ACCESS_LIST_VIEW);
								displayed = true;
							}
							else if(category.compareTo("TWITTER_STREAMING") == 0){
								pane.setComponentAt(index, DataAccessViews.TWITTER_STREAMING_DATA_ACCESS_VIEW);
								displayed = true;
							} 
							else if(category.compareTo("KAPOW") == 0){
								pane.setComponentAt(index, DataAccessViews.KAPOW_DATA_ACCESS_VIEW);
								displayed = true;
							}
//							else if(category.compareTo("CONNOTATE") == 0){
//								pane.setComponentAt(index, DataAccessViews.WEB_DATA_ACCESS_VIEW);
//							}
						}
					}

					if(displayed == false){
						pane.setComponentAt(index, DataAccessViews.DEFAULT_DATA_ACCESS_VIEW);
					}
				}

				rootPanel.invalidate();
				rootPanel.repaint();
				rootPanel.revalidate();
			}
		});

		AppCommonsUI.CONNECTION_MANAGER_TREE_VIEW = new ConnectionsView();
		tab.add("Connectivity", AppCommonsUI.CONNECTION_MANAGER_TREE_VIEW);
		
		tab.add("Data Access", null);

		tab.add("Discoverer", new DiscovererView());

		tab.add("Tasklets", new TaskletsView());

		tab.add("Workflow", new WorkFlowView());
		
		tab.add("Scheduler", new SchedulerView());

		this.setLayout(new BorderLayout(5, 5));

		this.add(new AppToolBar(), BorderLayout.EAST);
		this.add(tab, BorderLayout.CENTER);
	}
}