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
package org.onesun.smc.core.connection.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.api.ConnectionProperties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.xml.internal.ws.util.StringUtils;

public class SocialMediaConnectionProperties extends AbstractConnectionProperties {
	private static Logger logger = Logger.getLogger(SocialMediaConnectionProperties.class);
	
	private String apiKey = null;
	private String apiSecret = null;
	private List<String> apiScopeList = null;
	private String accessToken = null;
	private String accessSecret = null;
	
	public SocialMediaConnectionProperties(){
		super("Social Media", "SOCIAL_MEDIA", "OAUTH");
	}
	
	public String toScopeCSV() {
		if(apiScopeList != null && apiScopeList.size() > 0){
			String scope = apiScopeList.toString();
		
			scope = scope.replace("[", "");
			scope = scope.replace("]", "");

			return scope;
		}

		return null;
	}
	
	public String toString(){
		String scope = toScopeCSV();
		
		return name + "\t" + identity + "\t" + apiKey + "\t" + apiSecret + "\t" + ((scope != null) ? scope : "null")  + "\t" + accessToken + "\t" + accessSecret;
	}

	public void toScopeList(String csvText) {
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
		setName(properties.getProperty("connectionName"));
		setIdentity(properties.getProperty("identity"));
		setApiKey(properties.getProperty("apiKey"));
		setApiSecret(properties.getProperty("apiSecret"));
		setAccessToken(properties.getProperty("accessToken"));
		setAccessSecret(properties.getProperty("accessSecret"));
		toScopeList(properties.getProperty("apiScope"));		
	}
	
	@Override
	public void save(String path) throws FileNotFoundException, IOException {
		String connectionFileName = path + getName() + FILE_EXTENSION;
	
		File file = new File(connectionFileName);
		
		FileOutputStream fos = new FileOutputStream(file);
		
		Properties properties = new Properties();
		properties.put("authentication", getAuthentication().trim());
		properties.put("connectionName", getName().trim());
		properties.put("identity", getIdentity().trim());
		properties.put("apiKey", getApiKey().trim());
		properties.put("apiSecret", getApiSecret().trim());
		
		String scope = toScopeCSV();
		if(scope == null) {
			scope = "";
		}
		properties.put("apiScope", scope);
		
		String accessToken = getAccessToken();
		if(accessToken == null){
			accessToken = "";
		}
		properties.put("accessToken", accessToken.trim());
		
		String accessSecret = getAccessSecret();
		if(accessSecret == null){
			accessSecret = "";
		}
		properties.put("accessSecret", accessSecret.trim());
		
		properties.store(fos, StringUtils.capitalize(getIdentity()) + " Connection Properties");
		
		fos.close();
		
		logger.info("File Saved: " + connectionFileName);
	}
	
	@Override
	public Element toElement(Document document) throws ParserConfigurationException {
		Element parent = super.toElement(document);
		
		Element child = document.createElement("apiKey");
		child.setTextContent((apiKey != null) ? apiKey : "");
		parent.appendChild(child);
		
		child = document.createElement("apiSecret");
		child.setTextContent((apiSecret != null) ? apiSecret : "");
		parent.appendChild(child);
		
		child = document.createElement("accessToken");
		child.setTextContent((accessToken != null) ? accessToken : "");
		parent.appendChild(child);
		
		child = document.createElement("accessSecret");
		child.setTextContent((accessSecret != null) ? accessSecret : "");
		parent.appendChild(child);

		child = document.createElement("apiScope");
		child.setTextContent((apiScopeList != null) ? toScopeCSV() : "");
		parent.appendChild(child);
		
		return parent;
	}
	
	@Override
	public ConnectionProperties toConnectionProperties(Element element) throws ParserConfigurationException {
		super.toConnectionProperties(element);
		
		String value = null;

		value = XMLUtils.getValue(element, "apiKey");
		setApiKey((value != null) ? value : "");

		value = XMLUtils.getValue(element, "apiSecret");
		setApiSecret((value != null) ? value : "");

		value = XMLUtils.getValue(element, "accessToken");
		setAccessToken((value != null) ? value : "");

		value = XMLUtils.getValue(element, "accessSecret");
		setAccessSecret((value != null) ? value : "");

		value = XMLUtils.getValue(element,"apiScope");
		toScopeList((value != null) ? value : "");
		
		return this;
	}
}
