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

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.util.prefs.Preferences;

import javax.swing.JFrame;


public class MainWindow extends JFrame {

	private static final long serialVersionUID = -2113891520344941197L;

	/**
	 * @throws HeadlessException
	 */
	public MainWindow() {
		super();
	}

	/**
	 * Constructs a MainWindow.
	 * 
	 * @param title
	 *            Title of the window.
	 * @param iconImage
	 *            Icon to set on the window.
	 */
	public MainWindow(final String title, final Image iconImage) {
		super(title);
		setIconImage(iconImage);

		Preferences prefs = Preferences.userNodeForPackage(MainWindowAdapter.class);
		Dimension windowSize = new Dimension(prefs.getInt(WindowElements.WINDOW_SIZE_X.name(), 1100), prefs.getInt(WindowElements.WINDOW_SIZE_Y.name(), 600));
		setSize(windowSize);
		Point center = new Point(MainWindowUtils.getScreenCenter().x - (windowSize.width / 2), MainWindowUtils.getScreenCenter().y - (windowSize.height / 2));
		Point windowLocation = new Point(prefs.getInt(WindowElements.WINDOW_LOCATION_X.name(), center.x), prefs.getInt(WindowElements.WINDOW_LOCATION_Y.name(), center.y));
		setLocation(windowLocation);
		addWindowListener(new MainWindowAdapter());

		this.repaint();
	}

	public void quit() {
		MainWindowUtils.shutdown(this);
	}

	public void about() {
		MainWindowUtils.about(this);
	}

	public void preferences() {
		MainWindowUtils.preferences(this);
	}
}
