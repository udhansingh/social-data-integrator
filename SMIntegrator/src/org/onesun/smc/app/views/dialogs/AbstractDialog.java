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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.onesun.smc.app.handlers.UserAction;
import org.onesun.smc.app.handlers.UserActionListener;


public abstract class AbstractDialog extends JDialog {
	private static final long serialVersionUID = 8615404465470079746L;
	
	protected JPanel contentContainer = new JPanel(new BorderLayout(5, 5));
	protected JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
	protected Frame parent = null;
	
	protected boolean frameSizeAdjusted = false;
	protected JButton okButton = new JButton("Ok");

	public AbstractDialog(Frame parent, String title, boolean addOkButton, UserActionListener listner) {
		this(parent, title, addOkButton);
		okButton.addActionListener(listner);
	}
	
	public AbstractDialog(Frame parent, String title, boolean addOkButton) {
		this(parent, title);
		
		if(addOkButton == true){
			buttonContainer.add(okButton);
			
			UserActionListener listner = new UserActionListener(okButton, new UserAction() {
				@Override
				public void execute(ActionEvent event) {
					setVisible(false);
				}
			});
			okButton.addActionListener(listner);
		}
	}
	
	public AbstractDialog(Frame parent, String title) {
		super(parent, true);
		
		this.parent = parent;
		this.setLayout(new BorderLayout());
		this.setTitle(title);
		
		this.add(contentContainer, BorderLayout.CENTER);
		this.add(buttonContainer, BorderLayout.SOUTH);
	}

	public final void setVisible(boolean b) {
		if (b) {
			final Dimension parentSize = parent.getSize();
			final Point location = parent.getLocation();
			
			final int x = (parentSize.width - this.getWidth()) / 2;
			final int y = (parentSize.height - this.getHeight()) / 2;
			
			this.setLocation(location.x + x, location.y + y);
		}
		
		super.setVisible(b);
		
		super.revalidate();
		super.repaint();
	}

	public final void addNotify() {
		Dimension size = getSize();
		super.addNotify();

		if (frameSizeAdjusted) {
			return;
		}
		
		frameSizeAdjusted = true;

		Insets insets = getInsets();
		setSize(insets.left + insets.right + size.width, insets.top	+ insets.bottom + size.height);
	}
}