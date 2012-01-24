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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.onesun.smc.app.AppCommonsUI;
import org.onesun.smc.app.AppIcons;
import org.onesun.smc.app.views.dialogs.AboutDialog;
import org.onesun.smc.app.views.dialogs.HelpDialog;
import org.onesun.smc.app.views.dialogs.PreferencesDialog;

public class AppToolBar extends JToolBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8483370091383807403L;
	private static final PreferencesDialog preferencesDialog = new PreferencesDialog(AppCommonsUI.MAIN_WINDOW);
	private static final AboutDialog aboutDialog = new AboutDialog(AppCommonsUI.MAIN_WINDOW);
	private static final HelpDialog helpDialog = new HelpDialog(AppCommonsUI.MAIN_WINDOW);
	
	public AppToolBar(){
		super(JToolBar.VERTICAL);
		
		this.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
		
		createControls();
	}
	
	private void createControls(){
		ImageIcon icon = null;
		JButton button = null;
		
		icon = AppIcons.getIcon("settings");
		button = new JButton(icon); 
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				preferencesDialog.setVisible(true);
			}
		});
		
		this.add(button);
		
		// -------------
		icon = AppIcons.getIcon("about");
		button = new JButton(icon); 
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutDialog.setVisible(true);
			}
		});
		
		this.add(button);
		
		// -------------
		icon = AppIcons.getIcon("question");
		button = new JButton(icon); 
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpDialog.setVisible(true);
			}
		});
		
		this.add(button);
	}
}
