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
package org.onesun.commons.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;

public class JRadioButtonUtils {
	public static Container createRadioButtonGrouping(Alignment alignment, String elements[]) {
		return createRadioButtonGrouping(alignment, elements, null, null, null, null);
	}

	public static Container createRadioButtonGrouping(Alignment alignment, String elements[],
			String title) {
		return createRadioButtonGrouping(alignment, elements, title, null, null, null);
	}

	public static Container createRadioButtonGrouping(Alignment alignment, String elements[],
			String title, ActionListener actionListener) {
		return createRadioButtonGrouping(alignment, elements, title, actionListener, null,
				null);
	}

	public static Container createRadioButtonGrouping(Alignment alignment, String elements[],
			String title, ActionListener actionListener,
			ItemListener itemListener) {
		return createRadioButtonGrouping(alignment, elements, title, actionListener,
				itemListener, null);
	}

	public static Container createRadioButtonGrouping(Alignment alignment, String elements[],
			String title, ActionListener actionListener,
			ItemListener itemListener, ChangeListener changeListener) {
		return createRadioButtonGrouping(alignment, elements,
				title, actionListener,
				itemListener, changeListener, null);
	}
	
	public static Container createRadioButtonGrouping(Alignment alignment, String elements[],
			String title, ActionListener actionListener,
			ItemListener itemListener, ChangeListener changeListener, String nameEnabled) {
		
		// Default Vertical
		GridLayout layout = new GridLayout(0, 1);
		
		if(alignment == Alignment.HORIZONTAL){
			layout = new GridLayout(1, 0);
		}
		
		JPanel panel = new JPanel(layout);
		// If title set, create titled border
		if (title != null) {
			Border border = BorderFactory.createTitledBorder(title);
			panel.setBorder(border);
		}
		// Create group
		ButtonGroup group = new ButtonGroup();
		JRadioButton aRadioButton;
		// For each String passed in:
		// Create button, add to panel, and add to group
		for (int i = 0, n = elements.length; i < n; i++) {
			aRadioButton = new JRadioButton(elements[i]);
			
			if(nameEnabled != null && elements[i].compareTo(nameEnabled) == 0){
				aRadioButton.setSelected(true);
			}
			
			panel.add(aRadioButton);
			group.add(aRadioButton);
			if (actionListener != null) {
				aRadioButton.addActionListener(actionListener);
			}
			if (itemListener != null) {
				aRadioButton.addItemListener(itemListener);
			}
			if (changeListener != null) {
				aRadioButton.addChangeListener(changeListener);
			}
		}
		return panel;
	}

	public static Container createRadioButtonGrouping(Alignment alignment, String elements[],
			String title, ItemListener itemListener) {
		return createRadioButtonGrouping(alignment, elements, title, null, itemListener,
				null);
	}

	public static Enumeration<?> getSelectedElements(Container container) {
		Vector<String> selections = new Vector<String>();
		Component components[] = container.getComponents();
		for (int i = 0, n = components.length; i < n; i++) {
			if (components[i] instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) components[i];
				if (button.isSelected()) {
					selections.addElement(button.getText());
				}
			}
		}
		return selections.elements();
	}

	private JRadioButtonUtils() {
	}
}