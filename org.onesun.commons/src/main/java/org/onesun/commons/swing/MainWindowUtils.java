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
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;


public final class MainWindowUtils {
	private static Logger logger = Logger.getLogger(MainWindowUtils.class);
	
    private MainWindowUtils() {

    }

    public static void shutdown(final Window window) {
        new Thread(new Runnable() {
            public void run() {
                Preferences prefs = Preferences.userNodeForPackage(MainWindowAdapter.class);
                prefs.putInt(WindowElements.WINDOW_LOCATION_X.name(), window.getLocation().x);
                prefs.putInt(WindowElements.WINDOW_LOCATION_Y.name(), window.getLocation().y);

                prefs.putInt(WindowElements.WINDOW_SIZE_X.name(), window.getSize().width);
                prefs.putInt(WindowElements.WINDOW_SIZE_Y.name(), window.getSize().height);

                System.exit(0);
            }
        }).start();
    }

    public static boolean isMacOSX() {
        String osName = System.getProperty("os.name");
        return osName.equals("Mac OS X");
    }

    /**
     * Gets the center of the screen.
     * 
     * @return a Point.
     */
    public static Point getScreenCenter() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point(size.width / 2, size.height / 2);
    }

    /**
     * Tell system to use native look and feel, as in previous releases. Metal
     * (Java) LAF is the default otherwise.
     */
    public static void setNativeLookAndFeel() {
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        	logger.error("Error setting native LAF: " + e);
        }
    }

    public static void setVisualStudio2005LookAndFeel() {
        try {
            UIManager.setLookAndFeel("org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel");
        } catch (Exception e) {
        	logger.error("Error setting native LAF: " + e);
        }
    }

    /**
     * Selects the java metal laf.
     */
    public static void setJavaLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
        	logger.error("Error setting Java LAF: " + e);
        }
    }

    /**
     * Motif laf. Don't use. very ugly.
     */
    public static void setMotifLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (Exception e) {
        	logger.error("Error setting Motif LAF: " + e);
        }
    }

    public static void about(MainWindow mainWindow) {
        JOptionPane.showMessageDialog(mainWindow, "About:");
    }

    public static void preferences(MainWindow mainWindow) {
        JOptionPane.showMessageDialog(mainWindow, "Preferences");
    }
}
