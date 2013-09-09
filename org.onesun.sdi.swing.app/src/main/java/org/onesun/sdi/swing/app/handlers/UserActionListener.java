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
package org.onesun.sdi.swing.app.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserActionListener implements ActionListener {
	private Object object = null;
	private UserAction action = null;
	
	public UserActionListener(Object object, UserAction action){
		this.object = object;
		this.action = action;
	}
	
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		if (object == this.object)
			action.execute(event);
	}
}