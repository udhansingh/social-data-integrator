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
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.api.ConnectionProperties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DataSiftConnectionProperties extends AbstractConnectionProperties {
	private static Logger logger = Logger.getLogger(DataSiftConnectionProperties.class);

	private String userId = null;
	private String apiKey = null;
	private String streamHash = null;
	private String streamConsumerType = null;
	
	
	public DataSiftConnectionProperties(){
		super("DataSift", "DATASIFT", "DATASIFT");
	}

	@Override
	public void read(Properties properties) {
		setName(properties.getProperty("connectionName"));
		setIdentity(properties.getProperty("identity"));
		
		setUserId(properties.getProperty("userId"));
		setApiKey(properties.getProperty("apiKey"));
		setStreamHash(properties.getProperty("streamHash"));
		setStreamConsumerType(properties.getProperty("streamConsumerType"));
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
		
		properties.put("userId", getUserId().trim());
		properties.put("apiKey", getApiKey().trim());
		properties.put("streamHash", getStreamHash().trim());
		properties.put("streamConsumerType", getStreamConsumerType());
		
		properties.store(fos, "DataSift Connection Properties");
		
		fos.close();
		
		logger.info("File Saved: " + connectionFileName);
	}
	
	@Override
	public Element toElement(Document document) throws ParserConfigurationException {
		Element parent = super.toElement(document);
		
		Element child = document.createElement("usreId");
		child.setTextContent(getUserId());
		parent.appendChild(child);
		
		child = document.createElement("apiKey");
		child.setTextContent(getApiKey());
		parent.appendChild(child);
		
		child = document.createElement("streamHash");
		child.setTextContent(getStreamHash());
		parent.appendChild(child);
		
		child = document.createElement("streamConsumerType");
		child.setTextContent(getStreamConsumerType());
		parent.appendChild(child);

		return parent;
	}
	
	@Override
	public ConnectionProperties toConnectionProperties(Element element) throws ParserConfigurationException {
		super.toConnectionProperties(element);
		
		setUserId(XMLUtils.getValue(element, "userId"));
		setApiKey(XMLUtils.getValue(element, "apiKey"));
		setStreamHash(XMLUtils.getValue(element, "streamHash"));
		setStreamConsumerType(XMLUtils.getValue(element, "streamConsumerType"));
		
		
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getStreamHash() {
		return streamHash;
	}

	public void setStreamHash(String streamHash) {
		this.streamHash = streamHash;
	}

	public String getStreamConsumerType() {
		return streamConsumerType;
	}

	public void setStreamConsumerType(String streamConsumerType) {
		this.streamConsumerType = streamConsumerType;
	}
}
