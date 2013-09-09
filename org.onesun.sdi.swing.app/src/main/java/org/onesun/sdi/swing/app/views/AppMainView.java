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
package org.onesun.sdi.swing.app.views;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.onesun.sdi.core.api.Connection;
import org.onesun.sdi.core.model.Tasklet;
import org.onesun.sdi.swing.app.AppCommons;
import org.onesun.sdi.swing.app.AppCommonsUI;
import org.onesun.sdi.swing.app.api.DataAccessView;
import org.onesun.sdi.swing.app.api.DataAccessViewsFactory;

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
						Connection cp = bobj.getConnectionProperties();
						
						if(cp != null){
							String category = cp.getCategory();
							
							if(category != null){
								DataAccessView dataAccessView = DataAccessViewsFactory.getViewByCategory(category);
								
								if(dataAccessView != null){
									JPanel panel = dataAccessView.getViewPanel();
									
									pane.setComponentAt(index, panel);
									displayed = true;
								}
							}
						}
					}

					if(displayed == false){
						pane.setComponentAt(index, DataAccessViewsFactory.DEFAULT_DATA_ACCESS_VIEW);
					}
				}

				rootPanel.invalidate();
				rootPanel.repaint();
				rootPanel.revalidate();
			}
		});

		if(AppCommons.ALL_FEATURES_ENABLED || AppCommons.isFeatureEnabled(AppCommons.FEATURE_CONNECTIVITY)){
			AppCommonsUI.CONNECTION_MANAGER_TREE_VIEW = new ConnectionsView();
			tab.add("Connectivity", AppCommonsUI.CONNECTION_MANAGER_TREE_VIEW);
		}
		
		if(AppCommons.ALL_FEATURES_ENABLED || AppCommons.isFeatureEnabled(AppCommons.FEATURE_DATA_ACCESS)){
			tab.add("Data Access", null);
		}

		if(AppCommons.ALL_FEATURES_ENABLED || AppCommons.isFeatureEnabled(AppCommons.FEATURE_METADATA_DISCOVERY)){
			tab.add("Discoverer", new DiscovererView());
		}

		if(AppCommons.ALL_FEATURES_ENABLED || AppCommons.isFeatureEnabled(AppCommons.FEATURE_TASKLETS)){
			tab.add("Tasklets", new TaskletsView());
		}

		if(AppCommons.ALL_FEATURES_ENABLED || AppCommons.isFeatureEnabled(AppCommons.FEATURE_WORKFLOW)){
			tab.add("Workflow", new WorkFlowView());
		}
		
		if(AppCommons.ALL_FEATURES_ENABLED || AppCommons.isFeatureEnabled(AppCommons.FEATURE_SCHEDULING)){
			tab.add("Scheduler", new SchedulerView());
		}

		this.setLayout(new BorderLayout(5, 5));

		this.add(new AppToolBar(), BorderLayout.EAST);
		this.add(tab, BorderLayout.CENTER);
	}
}