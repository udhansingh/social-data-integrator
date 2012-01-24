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
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.onesun.smc.core.model.Authentication;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class KapowConnector extends WebConnector {
	private static Logger logger = Logger.getLogger(KapowConnector.class);
	
	protected int rqlPort = -1;
	
	public void setRqlPort(int rqlPort) {
		this.rqlPort = rqlPort;
	}
	
	public int getRqlPort() {
		return rqlPort;
	}
	
	public KapowConnector(){
		super(Authentication.KAPOW.name(), Authentication.KAPOW);
	}

	@Override
	public void read(Properties properties) {
		setAuthentication(Authentication.KAPOW);
		setName(properties.getProperty("connectionName"));
		setIdentity(properties.getProperty("identity"));
		setUrl(properties.getProperty("url"));
		String port = properties.getProperty("rqlPort");
		setRqlPort(Integer.parseInt(port));
		setUsername(properties.getProperty("username"));
		setPassword(properties.getProperty("password"));
	}
	
	@Override
	public void save(String path) throws FileNotFoundException, IOException {
		String connectionFileName = path + getName() + FILE_EXTENSION;
		
		File file = new File(connectionFileName);
		
		FileOutputStream fos = new FileOutputStream(file);
		
		Properties properties = new Properties();
		properties.put("authentication", getAuthentication().name().trim());
		properties.put("connectionName", getName().trim());
		properties.put("identity", getIdentity().trim());
		
		properties.put("url", getUrl().trim());
		properties.put("rqlPort", Integer.toString(getRqlPort()));
		properties.put("username", getUsername().trim());
		properties.put("password", getPassword());
		
		properties.store(fos, "Kapow Connection Properties");
		
		fos.close();
		
		logger.info("File Saved: " + connectionFileName);
	}
	
	@Override
	public Element toElement(Document document) throws ParserConfigurationException {
		Element parent = super.toElement(document);
		
		Element child = document.createElement("rqlPort");
		child.setTextContent(Integer.toString(getRqlPort()));
		parent.appendChild(child);

		return parent;
	}
}
