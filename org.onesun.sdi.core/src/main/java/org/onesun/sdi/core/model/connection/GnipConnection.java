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
package org.onesun.sdi.core.model.connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.sdi.core.api.Connection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GnipConnection extends AbstractConnection {
	private static Logger logger = Logger.getLogger(GnipConnection.class);

	private String username = null;
	private String password = null;
	private String url = null;
	private Boolean compressionEnabled = false;
	
	public GnipConnection(){
		super("Gnip", "GNIP", "GNIP");
	}

	@Override
	public void read(Properties properties) {
		setName(properties.getProperty("connectionName"));
		setIdentity(properties.getProperty("identity"));
		
		setUsername(properties.getProperty("username"));
		setPassword(properties.getProperty("password"));
		setUrl(properties.getProperty("url"));
		
		String ceStatus = properties.getProperty("compressionEnabled");
		if(ceStatus.compareToIgnoreCase("true") == 0 || ceStatus.compareToIgnoreCase("yes") == 0){
			setCompressionEnabled(true);
		} else {
			setCompressionEnabled(false);
		}
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
		
		properties.put("username", getUsername().trim());
		properties.put("password", getPassword().trim());
		properties.put("url", getUrl().trim());
		properties.put("compressionEnabled", Boolean.toString(compressionEnabled));
		
		properties.store(fos, "DataSift Connection Properties");
		
		fos.close();
		
		logger.info("File Saved: " + connectionFileName);
	}
	
	@Override
	public Element toElement(Document document) throws ParserConfigurationException {
		Element parent = super.toElement(document);
		
		Element child = document.createElement("username");
		child.setTextContent(getUsername());
		parent.appendChild(child);
		
		child = document.createElement("password");
		child.setTextContent(getPassword());
		parent.appendChild(child);
		
		child = document.createElement("url");
		child.setTextContent(getUrl());
		parent.appendChild(child);

		child = document.createElement("compressionEnabled");
		child.setTextContent(Boolean.toString(isCompressionEnabled()));
		parent.appendChild(child);
		
		return parent;
	}
	
	@Override
	public Connection toConnection(Element element) throws ParserConfigurationException {
		super.toConnection(element);
		
		setUsername(XMLUtils.getValue(element, "username"));
		setPassword(XMLUtils.getValue(element, "password"));
		setUrl(XMLUtils.getValue(element, "url"));
		
		String ceStatus = XMLUtils.getValue(element, "compressionEnabled");
		
		if(ceStatus.compareToIgnoreCase("true") == 0 || ceStatus.compareToIgnoreCase("yes") == 0){
			setCompressionEnabled(true);
		} else {
			setCompressionEnabled(false);
		}
		
		return this;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isCompressionEnabled() {
		return compressionEnabled;
	}

	public void setCompressionEnabled(boolean compressionEnabled) {
		this.compressionEnabled = compressionEnabled;
	}
}
