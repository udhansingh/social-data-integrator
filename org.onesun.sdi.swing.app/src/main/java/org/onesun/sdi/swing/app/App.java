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
package org.onesun.sdi.swing.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.onesun.commons.Log4jUtils;
import org.onesun.commons.swing.MainWindow;
import org.onesun.sdi.swing.app.views.AppMainView;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


public class App {
	private final static String VERSION = "Feb 28, 2013 20:00 PST";
	
	@Option(required=false, name="-f", aliases="--features", usage="Connectivity,DataAccess,MetadataDiscovery", metaVar="<list of features>")
	private String features = null;
	
	@Option(required=false, name="-b", aliases="--browser", usage="<embedded|system>", metaVar="<browser to use>")
	private String browser = null;
	
	@Option(required=false, name="-h", aliases="--home", usage="path to app home", metaVar="<home directory>")
	private String home = null;

	@Option(required=false, name="-v", aliases="--version", usage="show version")
	private boolean version = false;
	
	@Option(required=false, name="-l", aliases="--log4j", usage="log4j configuration", metaVar="<log4j properties file>")
	private File				log4jFile		= null;

	@Argument
	private List<String> arguments = null;
	
	private String[] commandLineArguments = null;
	
	public App(String[] commandLineArguments){
		this.commandLineArguments = commandLineArguments;
	}
	
	public void init(){
		CmdLineParser parser = new CmdLineParser(this);

		// Validate arguments
		try {
			parser.parseArgument(commandLineArguments);
		} catch(CmdLineException e) {
			System.out.println("Version: " + VERSION);
			
			System.err.println(e.getMessage());
			System.err.println("java " + this.getClass().getSimpleName() + " [options...] arguments ...");
			parser.printUsage(System.err);
			System.err.println();
			
			System.exit(-1);
		}

		if(log4jFile != null){
			try {
				Log4jUtils.initLog(log4jFile);
			} catch(FileNotFoundException e) {
			} catch(IOException e) {
			}
		}
		
		// display version
		if(version == true) {
			System.out.println("Version: " + VERSION);
			System.exit(0);
		}
		
		// set home directory
		if(home != null){
			File f = new File(home);
			if(f.exists() == true){
				AppCommons.PATH_TO_APP_HOME = f.getAbsolutePath();
			}
		}
		
		// set browser
		if(browser == null) {
			browser = "embedded";
		}
		
		// Validate browser option
		if(browser.compareToIgnoreCase("embedded") == 0 || browser.compareToIgnoreCase("system") == 0) {
			AppCommons.BROWSER = browser;
		}
		else {
			System.err.println("Browser option must be either 'embedded' or 'system'");
			System.exit(-1);
		}

		// Default features
		if(features == null){
			features = "Connectivity,DataAccess,MetadataDiscovery";
		}
		
		// Validate features
		if(features != null) {
			String[] requestedFeatures = features.split(",");

			if(requestedFeatures != null && requestedFeatures.length > 0) {
				if(requestedFeatures.length == 1 && requestedFeatures[0].compareToIgnoreCase("alpha") == 0) {
					AppCommons.ALL_FEATURES_ENABLED = true;
				}
				else {
					int invalidCount = 0;
					
					for(String feature : requestedFeatures){
						if(AppCommons.isValidFeature(feature)) {
							AppCommons.setFeatureEnabled(feature, true);
						}
						else {
							invalidCount++;
						}
					}
					
					if(invalidCount > 0){
						System.err.println("Features must set of values from [Connectivity,DataAccess,MetadataDiscovery]");
						System.exit(-1);
					}
				}
			}
		}
		
		// Process other arguments
		if(arguments != null && arguments.size() > 0){
			// TODO: For future use
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		App app = new App(args);
		app.init();
		
		// Must be invoked
		AppCommons.init();
		
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
