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
package org.onesun.smc.core.connectors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.onesun.smc.core.model.Authentication;

import com.sun.xml.internal.ws.util.StringUtils;

public class SocialMediaConnector extends AbstractConnector {
	private static Logger logger = Logger.getLogger(SocialMediaConnector.class);
	
	private String apiKey = null;
	private String apiSecret = null;
	private List<String> apiScopeList = null;
	private String accessToken = null;
	private String accessSecret = null;
	
	public SocialMediaConnector(){
		super("SOCIAL_MEDIA", Authentication.OAUTH);
	}
	
	public String scopeCSV() {
		if(apiScopeList != null && apiScopeList.size() > 0){
			String scope = apiScopeList.toString();
		
			scope = scope.replace("[", "");
			scope = scope.replace("]", "");
			scope = scope.replaceAll(" ", "");

			return scope;
		}

		return null;
	}
	
	public String toString(){
		String scope = scopeCSV();
		
		return connectionName + "\t" + identity + "\t" + apiKey + "\t" + apiSecret + "\t" + ((scope != null) ? scope : "null")  + "\t" + accessToken + "\t" + accessSecret;
	}

	public void scopeCSV(String csvText) {
		if((csvText == null) || ((csvText != null) && (csvText.length() <= 0))){
			apiScopeList = null;
		}
		else {

			String[] values = csvText.split(",");
			apiScopeList = new ArrayList<String>();

			for(String value : values){
				apiScopeList.add(value);
			}
		}
	}

	public String getName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public List<String> getApiScope() {
		return apiScopeList;
	}

	public void setApiScope(List<String> apiScope) {
		this.apiScopeList = apiScope;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessSecret() {
		return accessSecret;
	}

	public void setAccessSecret(String accessSecret) {
		this.accessSecret = accessSecret;
	}

	@Override
	public void read(Properties properties) {
		setAuthentication(Authentication.OAUTH);
		setConnectionName(properties.getProperty("connectionName"));
		setIdentity(properties.getProperty("identity"));
		setApiKey(properties.getProperty("apiKey"));
		setApiSecret(properties.getProperty("apiSecret"));
		setAccessToken(properties.getProperty("accessToken"));
		setAccessSecret(properties.getProperty("accessSecret"));
		scopeCSV(properties.getProperty("apiScope"));		
	}
	
	@Override
	public void save(String path) throws FileNotFoundException, IOException {
		String connectionFileName = path + getName() + FILE_EXTENSION;
	
		File file = new File(connectionFileName);
		
		FileOutputStream fos = new FileOutputStream(file);
		
		Properties properties = new Properties();
		properties.put("authentication", getAuthentication().name());
		properties.put("connectionName", getName());
		properties.put("identity", getIdentity());
		properties.put("apiKey", getApiKey());
		properties.put("apiSecret", getApiSecret());
		
		String scope = scopeCSV();
		if(scope == null) {
			scope = "";
		}
		properties.put("apiScope", scope);
		
		String accessToken = getAccessToken();
		if(accessToken == null){
			accessToken = "";
		}
		properties.put("accessToken", accessToken);
		
		String accessSecret = getAccessSecret();
		if(accessSecret == null){
			accessSecret = "";
		}
		properties.put("accessSecret", accessSecret);
		
		properties.store(fos, StringUtils.capitalize(getIdentity()) + " Connection Properties");
		
		fos.close();
		
		logger.info("File Saved: " + connectionFileName);
	}
}
