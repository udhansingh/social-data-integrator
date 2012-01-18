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
package org.onesun.smc.app.views.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JScrollPane;

import org.onesun.smc.app.AppCommonsUI;

public class ConsoleDialog extends AbstractDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8615404465470079746L;
	private JScrollPane scrollPane = new JScrollPane(AppCommonsUI.MODEL_TEXTAREA);
	
	public ConsoleDialog(Frame parent) {
		super(parent, "Console", true);
		
		setSize(800, 700);
		setResizable(true);
		setVisible(false);
		
		
		contentContainer.add(scrollPane, BorderLayout.CENTER);
	}
}