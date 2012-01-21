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
package org.onesun.smc.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.onesun.commons.swing.MainWindow;
import org.onesun.smc.app.views.AppMainView;


public class App {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File(AppCommons.PATH_TO_WORK);
		if(file.exists() == false){
			file.mkdirs();
		}
		
		try {
			UIManager.setLookAndFeel(
				// UIManager.getSystemLookAndFeelClassName()
				UIManager.getCrossPlatformLookAndFeelClassName()
			);
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (UnsupportedLookAndFeelException e) {
		}
		
		MainWindow mainWindow = AppCommonsUI.MAIN_WINDOW;
		ImageIcon applicationIcon = AppIcons.getIcon("application");
		mainWindow.setIconImage(applicationIcon.getImage());
		
		// mw.setResizable(false);
		mainWindow.add(new AppMainView(), BorderLayout.CENTER);
		
		Dimension d = new Dimension(1000, 735);
		mainWindow.setPreferredSize(d);
		mainWindow.setSize(d);
		
		mainWindow.setVisible(true);
	}
}
