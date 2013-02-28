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

import java.io.File;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.onesun.commons.text.classification.opencalais.OpenCalaisClient;
import org.onesun.commons.text.classification.uclassify.UClassifyClient;
import org.onesun.commons.webbrowser.EmbeddedWebBrowser;
import org.onesun.commons.webbrowser.LocationChangeHandler;
import org.onesun.commons.webbrowser.SystemWebBrowser;
import org.onesun.commons.webbrowser.WebBrowser;
import org.onesun.smc.api.ConfigurationHelper;
import org.onesun.smc.api.ConnectionPropertiesFactory;
import org.onesun.smc.api.ConnectionPropertiesViewsFactory;
import org.onesun.smc.api.DataAccessViewsFactory;
import org.onesun.smc.api.DataServicesFactory;
import org.onesun.smc.api.DataTypeFactory;
import org.onesun.smc.api.FilterFactory;
import org.onesun.smc.api.ProviderFactory;
import org.onesun.smc.api.ProxyConfiguration;
import org.onesun.smc.api.TaskletsFactory;
import org.onesun.smc.core.model.Tasklet;
import org.onesun.smc.core.services.auth.OAuthenticator;
import org.onesun.smc.core.tools.ProxyAuthenticator;

public class AppCommons {
	private static Logger 								logger 								= Logger.getLogger(AppCommons.class);
	
	public static final String 							FEATURE_CONNECTIVITY 				= "Connectivity";
	public static final String 							FEATURE_DATA_SERVICES 				= "DataServices";
	public static final String 							FEATURE_DATA_ACCESS 				= "DataAccess";
	public static final String 							FEATURE_METADATA_DISCOVERY 			= "MetadataDiscovery";	
	public static final String 							FEATURE_TASKLETS 					= "Tasklets";
	public static final String 							FEATURE_WORKFLOW 					= "Workflow";
	public static final String 							FEATURE_SCHEDULING 					= "Scheduling";
	
	public static boolean 								ALL_FEATURES_ENABLED				= false;

	public static Tasklet 								TASKLET								= new Tasklet();
	public static String 								APPLICATION_TITLE					= "Social Data Integrator";
	public static String 								AUTHENTICATION						= "NONE";
	public static int 									HTTP_CONNECTION_TIMEOUT				= 1000 * 25;
	public static OAuthenticator 						AUTHENTICATOR						= null;
	public static Object 								RESPONSE_OBJECT						= null;
	public static String								PATH_TO_APP_HOME					= null;
	public static String 								PATH_TO_WORK						= null;
	public static String								PATH_TO_APP_CONFIG					= null;
	public static String								PATH_TO_CORE						= null;
	public static String								PATH_TO_DATA_ACCESS					= null;
	public static String								PATH_TO_TASKLETS					= null;
	public static String								PATH_TO_MASTER_METADATA				= null;
	public static String								CONFIG_FILE_PATH					= null;
	public static String								PATH_TO_CONNECTIONS					= null;
	public static String 								OAUTH_CALLBACK_SERVER_PROTOCOL		= "http";
	public static String 								OAUTH_CALLBACK_SERVER_NAME			= null;
	public static String 								OAUTH_CALLBACK_SERVER_CONTEXT		= "/OAuthApp/jrs/callback";
	public static int	 								OAUTH_CALLBACK_SERVER_PORT			= 7080;
	public static String 								UCLASSIFY_READ_ACCESS_KEY			= null;
	public static String 								OPENCALAIS_LICENSE_KEY				= null;
	public static WebBrowser							WEB_BROWSER							= null;
	public static String								CALLBACK_URL						= null;
	public static ProxyConfiguration					PROXY_CONFIGURATION					= new ProxyConfiguration();
	public static ConfigurationHelper					CONFIGURATION_HELPER				= new ConfigurationHelper();
	private static Map<String, Boolean> 				enabledFeaturesMap					= new HashMap<String, Boolean>();
	
	public static void init() {
		// Free features
		enabledFeaturesMap.put(FEATURE_CONNECTIVITY, true);
		
		try {
			InetAddress addr = InetAddress.getLocalHost();
			OAUTH_CALLBACK_SERVER_NAME = addr.getCanonicalHostName();
			logger.info("Canonical Hostname: " + OAUTH_CALLBACK_SERVER_NAME);
		} catch (UnknownHostException e) {
		}
		
		final String APP_HOME = "SDI_HOME";
		PATH_TO_APP_HOME = System.getenv(APP_HOME);
		
		if(PATH_TO_APP_HOME == null){
			String os = System.getProperty("os.name");
			os = os.trim();
			
			logger.info("OS: " + os);
			
			String homeDirectory = null;
			if(os.toLowerCase().startsWith("windows")){
				// Windows
				homeDirectory = System.getProperty("USERPROFILE");
			} else {
				// *NIX
				homeDirectory = System.getProperty("HOME");
			}
			
			if(homeDirectory != null && homeDirectory.length() > 0){
				PATH_TO_APP_HOME = homeDirectory + File.separator + "apps" + File.separator + "sdi";
			} else {
				PATH_TO_APP_HOME = File.separator + "apps" + File.separator + "sdi";
			}
		}
		
		// Pad File.separator iff, it is not existing already
		if(PATH_TO_APP_HOME.endsWith(File.separator) == false){
			PATH_TO_APP_HOME += File.separator;
		}
		
		logger.info("HOME=" + PATH_TO_APP_HOME);
		
		// Initialize
		PATH_TO_WORK				= PATH_TO_APP_HOME		+ "work" + File.separator;
		PATH_TO_APP_CONFIG			= PATH_TO_APP_HOME		+ "etc" + File.separator + "conf" + File.separator;
		PATH_TO_MASTER_METADATA		= PATH_TO_APP_HOME		+ "etc" + File.separator + "master-metadata" + File.separator;
		PATH_TO_DATA_ACCESS			= PATH_TO_APP_CONFIG	+ "data-access" + File.separator;
		PATH_TO_CORE				= PATH_TO_APP_CONFIG	+ "core" + File.separator;
		PATH_TO_CONNECTIONS			= PATH_TO_APP_CONFIG	+ "connections" + File.separator;
		PATH_TO_TASKLETS			= PATH_TO_APP_CONFIG	+ "tasklets" + File.separator;
		CONFIG_FILE_PATH			= PATH_TO_APP_CONFIG	+ "core.properties";
		
		File dir = null;
		
		if(ALL_FEATURES_ENABLED || isFeatureEnabled(FEATURE_CONNECTIVITY)){
			dir = new File(PATH_TO_APP_CONFIG);
			if(dir.exists() == false){
				dir.mkdirs();
			}
			dir = new File(PATH_TO_CONNECTIONS);
			if(dir.exists() == false){
				dir.mkdirs();
			}
			dir = new File(AppCommons.PATH_TO_CORE);
			if(dir.exists() == false){
				dir.mkdirs();
			}

			dir = new File(AppCommons.PATH_TO_DATA_ACCESS);
			if(dir.exists() == false){
				dir.mkdirs();
			}
		}
		
		if(ALL_FEATURES_ENABLED || isFeatureEnabled(FEATURE_METADATA_DISCOVERY)){
			dir = new File(PATH_TO_MASTER_METADATA);
			if(dir.exists() == false){
				dir.mkdirs();
			}
		}
		
		if(ALL_FEATURES_ENABLED || isFeatureEnabled(FEATURE_TASKLETS)){
			dir = new File(PATH_TO_TASKLETS);
			if(dir.exists() == false){
				dir.mkdirs();
			}
		}
		
		// Setup configuration items
		CONFIGURATION_HELPER.loadConfiguration(CONFIG_FILE_PATH);
		Properties properties = CONFIGURATION_HELPER.getProperties();
		
		if(properties != null && properties.size() > 0){
			String propertyValue = null;
			
			// Generic Settings
			propertyValue = (String)properties.get("http.timeout");
			HTTP_CONNECTION_TIMEOUT = (propertyValue != null && propertyValue.trim().length() > 0) ? Integer.parseInt(propertyValue) : HTTP_CONNECTION_TIMEOUT;
			
			propertyValue = (String)properties.get("oauth.callback.server.name");
			OAUTH_CALLBACK_SERVER_NAME = (propertyValue != null && propertyValue.trim().length() > 0) ? propertyValue : OAUTH_CALLBACK_SERVER_NAME;

			propertyValue = (String)properties.get("oauth.callback.server.protocol");
			OAUTH_CALLBACK_SERVER_PROTOCOL = (propertyValue != null && propertyValue.trim().length() > 0) ? propertyValue : OAUTH_CALLBACK_SERVER_PROTOCOL;
			
			propertyValue = (String)properties.get("oauth.callback.server.port");
			OAUTH_CALLBACK_SERVER_PORT = (propertyValue != null && propertyValue.trim().length() > 0) ? Integer.parseInt(propertyValue) : OAUTH_CALLBACK_SERVER_PORT;
			
			propertyValue = (String)properties.get("oauth.callback.server.context");
			OAUTH_CALLBACK_SERVER_CONTEXT = (propertyValue != null && propertyValue.trim().length() > 0) ? propertyValue : OAUTH_CALLBACK_SERVER_CONTEXT;

			// Proxy Settings
			
			propertyValue = (String)properties.get("http.proxy.enabled");
			PROXY_CONFIGURATION.setEnabled((propertyValue != null && propertyValue.trim().length() > 0) ? Boolean.parseBoolean(propertyValue) : false);
			
			propertyValue = (String)properties.get("http.proxy.host");
			PROXY_CONFIGURATION.setHostname((propertyValue != null && propertyValue.trim().length() > 0) ? propertyValue : "");

			propertyValue = (String)properties.get("http.proxy.port");
			PROXY_CONFIGURATION.setPort((propertyValue != null && propertyValue.trim().length() > 0) ? Integer.parseInt(propertyValue) : -1);
			
			propertyValue = (String)properties.get("http.proxy.username");
			PROXY_CONFIGURATION.setUsername((propertyValue != null && propertyValue.trim().length() > 0) ? propertyValue : "");
			
			propertyValue = (String)properties.get("http.proxy.password");
			PROXY_CONFIGURATION.setPassword((propertyValue != null && propertyValue.trim().length() > 0) ? propertyValue : "");

			// License Settings
			propertyValue = (String)properties.get("text.analysis.license.uclassify");
			UCLASSIFY_READ_ACCESS_KEY = (propertyValue != null && propertyValue.trim().length() > 0) ? propertyValue : UCLASSIFY_READ_ACCESS_KEY;
			
			propertyValue = (String)properties.get("text.analysis.license.opencalais");
			OPENCALAIS_LICENSE_KEY = (propertyValue != null && propertyValue.trim().length() > 0) ? propertyValue : OPENCALAIS_LICENSE_KEY;
		}
		else {
			saveConfiguration();
		}

		// Initialize which browser to use
		OAuthenticator.setWebBrowser(new SystemWebBrowser());
		
/*		
		OAuthenticator.setWebBrowser(new EmbeddedWebBrowser("Authorize Application", new LocationChangeHandler() {
			@Override
			public boolean execute(String url) {
					return AUTHENTICATOR.getAccessKeys(url);
			}
		}));
*/		
		// Setup Configuration
		setup();
		
		// Load Data
		DataTypeFactory.load(PATH_TO_CORE + "core-data-types.xml");
		if(ALL_FEATURES_ENABLED || isFeatureEnabled(FEATURE_CONNECTIVITY)){
			ConnectionPropertiesFactory.load(PATH_TO_CORE + "connection-properties.xml");
			ConnectionPropertiesFactory.loadConnectionProperties(PATH_TO_CONNECTIONS);
			ConnectionPropertiesViewsFactory.load(PATH_TO_CORE + "connection-properties-views.xml");

			ProviderFactory.load(PATH_TO_CORE + "data-providers.xml", PATH_TO_DATA_ACCESS);
			FilterFactory.load(PATH_TO_CORE + "data-filters.xml", PATH_TO_DATA_ACCESS);
		}
		
		if(ALL_FEATURES_ENABLED || isFeatureEnabled(FEATURE_DATA_SERVICES)){
			DataServicesFactory.load(PATH_TO_CORE + "data-services.xml");
		}

		if(ALL_FEATURES_ENABLED || isFeatureEnabled(FEATURE_DATA_ACCESS)){
			DataAccessViewsFactory.load(PATH_TO_CORE + "data-access-views.xml");
		}

		if(ALL_FEATURES_ENABLED || isFeatureEnabled(FEATURE_TASKLETS)){
			TaskletsFactory.load(PATH_TO_TASKLETS);
			
			for(Tasklet tasklet : TaskletsFactory.getTasklets()){
				AppCommonsUI.TASKLETS_MODEL.addElement(tasklet);
			}
		}
	}
	
	public static void saveConfiguration(){
		Properties properties = new Properties();
		
		properties.put("http.timeout",						Integer.toString(HTTP_CONNECTION_TIMEOUT));
		properties.put("oauth.callback.server.name",		OAUTH_CALLBACK_SERVER_NAME);
		properties.put("oauth.callback.server.protocol",	OAUTH_CALLBACK_SERVER_PROTOCOL);
		properties.put("oauth.callback.server.port",		Integer.toString(OAUTH_CALLBACK_SERVER_PORT));
		properties.put("oauth.callback.server.context",		OAUTH_CALLBACK_SERVER_CONTEXT);
		
		properties.put("http.proxy.enabled",				Boolean.toString(PROXY_CONFIGURATION.isEnabled()));
		properties.put("http.proxy.host",					PROXY_CONFIGURATION.getHostname());
		properties.put("http.proxy.port",					Integer.toString(PROXY_CONFIGURATION.getPort()));
		properties.put("http.proxy.username",				PROXY_CONFIGURATION.getUsername());
		properties.put("http.proxy.password",				PROXY_CONFIGURATION.getPassword());
		
		if(ALL_FEATURES_ENABLED == true || isFeatureEnabled(FEATURE_DATA_SERVICES) ){
			properties.put("text.analysis.license.uclassify",	(UCLASSIFY_READ_ACCESS_KEY == null) ? "" : UCLASSIFY_READ_ACCESS_KEY);
			properties.put("text.analysis.license.opencalais",	(OPENCALAIS_LICENSE_KEY == null) ? "" : OPENCALAIS_LICENSE_KEY);
		}
		
		CONFIGURATION_HELPER.setProperties(properties);
		CONFIGURATION_HELPER.saveConfiguration(CONFIG_FILE_PATH);
	}
	
	public static void setup() {
		if(PROXY_CONFIGURATION.isEnabled() == true){
			ProxyAuthenticator pa = new ProxyAuthenticator(PROXY_CONFIGURATION.getUsername(), PROXY_CONFIGURATION.getPassword());
			Authenticator.setDefault(pa);
			
			// HTTP Proxy
			System.setProperty("http.proxySet", "true");
			System.getProperties().put("http.proxyHost", PROXY_CONFIGURATION.getHostname());
			System.getProperties().put("http.proxyPort", Integer.toString(PROXY_CONFIGURATION.getPort()));
			// HTTPS Proxy
			System.setProperty("https.proxySet", "true");
			System.getProperties().put("https.proxyHost", PROXY_CONFIGURATION.getHostname());
			System.getProperties().put("https.proxyPort", Integer.toString(PROXY_CONFIGURATION.getPort()));
		}
		
		CALLBACK_URL = OAuthenticator.initialize(
			OAUTH_CALLBACK_SERVER_PROTOCOL, 
			OAUTH_CALLBACK_SERVER_NAME, 
			OAUTH_CALLBACK_SERVER_PORT, 
			OAUTH_CALLBACK_SERVER_CONTEXT,
			HTTP_CONNECTION_TIMEOUT
		);

		if(ALL_FEATURES_ENABLED == true || isFeatureEnabled(FEATURE_DATA_SERVICES)){
			UClassifyClient.setReadAccessKey(UCLASSIFY_READ_ACCESS_KEY);
			OpenCalaisClient.setLicenseKey(OPENCALAIS_LICENSE_KEY);
		}
	}

	public static void setFeatureEnabled(String featureName, Boolean status) {
		enabledFeaturesMap.put(featureName, status);
	}
	
	public static Boolean isFeatureEnabled(String featureName) {
		Boolean flag = enabledFeaturesMap.get(featureName);
		if(flag == null || flag == false){
			return false;
		}

		return flag;
	}
}