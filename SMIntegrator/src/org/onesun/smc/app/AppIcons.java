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

import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;

import org.onesun.commons.swing.IconUtils;

public class AppIcons {
	public static Map<String, ImageIcon> icons = new TreeMap<String, ImageIcon>();
	
	static {
		icons.put("add", 				IconUtils.createImageIcon("/icons/addIcon.png"));
		icons.put("delete", 			IconUtils.createImageIcon("/icons/deleteIcon.png"));
		icons.put("application",		IconUtils.createImageIcon("/icons/applicationIcon.png"));
		icons.put("settings", 			IconUtils.createImageIcon("/icons/settingsIcon.png"));
		icons.put("connection",	 		IconUtils.createImageIcon("/icons/connectionIcon.png"));
		
		icons.put("about", 				IconUtils.createImageIcon("/icons/aboutIcon.png"));
		icons.put("clear", 				IconUtils.createImageIcon("/icons/clearIcon.png"));
		icons.put("select", 			IconUtils.createImageIcon("/icons/selectIcon.png"));
		icons.put("refresh", 			IconUtils.createImageIcon("/icons/refreshIcon.png"));
		icons.put("help", 				IconUtils.createImageIcon("/icons/helpIcon.png"));
		icons.put("copy", 				IconUtils.createImageIcon("/icons/copyIcon.png"));
		icons.put("save", 				IconUtils.createImageIcon("/icons/saveIcon.png"));
		icons.put("authorize", 			IconUtils.createImageIcon("/icons/authorizeIcon.png"));
		icons.put("question", 			IconUtils.createImageIcon("/icons/questionIcon.png"));
		icons.put("console", 			IconUtils.createImageIcon("/icons/consoleIcon.png"));
		icons.put("insertRow", 			IconUtils.createImageIcon("/icons/insertRowIcon.png"));
		icons.put("deleteRow", 			IconUtils.createImageIcon("/icons/deleteRowIcon.png"));
		
		icons.put("network", 			IconUtils.createImageIcon("/icons/networkIcon.png"));
		icons.put("general", 			IconUtils.createImageIcon("/icons/webIcon.png"));
		icons.put("file system", 		IconUtils.createImageIcon("/icons/filesystemIcon.png"));
		icons.put("web", 				IconUtils.createImageIcon("/icons/webIcon.png"));

		icons.put("facebook", 			IconUtils.createImageIcon("/icons/facebookIcon.png"));
		icons.put("google", 			IconUtils.createImageIcon("/icons/googleIcon.png"));
		icons.put("twitter", 			IconUtils.createImageIcon("/icons/twitterIcon.png"));
		icons.put("foursquare", 		IconUtils.createImageIcon("/icons/foursquareIcon.png"));
		icons.put("linkedin", 			IconUtils.createImageIcon("/icons/linkedinIcon.png"));
		icons.put("youtube", 			IconUtils.createImageIcon("/icons/youtubeIcon.png"));
		icons.put("dropbox", 			IconUtils.createImageIcon("/icons/dropboxIcon.png"));
		icons.put("yahoo", 				IconUtils.createImageIcon("/icons/yahooIcon.png"));
		icons.put("yammer", 			IconUtils.createImageIcon("/icons/yammerIcon.png"));
		icons.put("datasift", 			IconUtils.createImageIcon("/icons/datasiftIcon.png"));
		icons.put("gnip", 				IconUtils.createImageIcon("/icons/gnipIcon.png"));
		
		icons.put("twitter streaming", 	IconUtils.createImageIcon("/icons/streamIcon.png"));
		icons.put("facebook streaming", IconUtils.createImageIcon("/icons/streamIcon.png"));
		icons.put("google plus",		IconUtils.createImageIcon("/icons/googlePlusIcon.png"));
		icons.put("google mail",		IconUtils.createImageIcon("/icons/googleMailIcon.png"));
		
		icons.put("kapow",				IconUtils.createImageIcon("/icons/kapowIcon.png"));
		icons.put("connotate",			IconUtils.createImageIcon("/icons/webIcon.png"));
	}
	
	public static ImageIcon getIcon(String name){
		return icons.get(name);
	}
	
	private AppIcons(){
	}
}
