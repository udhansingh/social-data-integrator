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

import org.apache.log4j.Logger;
import org.onesun.smc.core.model.Authentication;

public class RESTConnector extends AbstractConnector {
	private static Logger logger = Logger.getLogger(RESTConnector.class);
			
	protected String username = null;
	protected String password = null;
	
	public RESTConnector(){
		super("GENERAL", Authentication.REST);
	}
	
	protected RESTConnector(String category, Authentication authentication) {
		super(category, authentication);
	}

	public String toString(){
		return "[" + connectionName + "," + username + "," + password + "]";
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

	@Override
	public void read(Properties properties) {
		setAuthentication(Authentication.REST);
		setConnectionName(properties.getProperty("connectionName"));
		setIdentity(properties.getProperty("identity"));
		setUsername(properties.getProperty("username"));
		setPassword(properties.getProperty("password"));
	}
	
	@Override
	public void save(String path) throws FileNotFoundException, IOException {
		String connectionFileName = path + getName() + FILE_EXTENSION;
		
		File file = new File(connectionFileName);
		
		FileOutputStream fos = new FileOutputStream(file);
		
		Properties properties = new Properties();
		properties.put("authentication", getAuthentication().name());
		properties.put("connectionName", getName());
		
		String providerName = getIdentity();
		providerName = (providerName == null) ? "General" : providerName;
		
		properties.put("identity", providerName);
		
		properties.put("username", getUsername());
		properties.put("password", getPassword());
		
		properties.store(fos, "BasicHttp Connection Properties");
		
		fos.close();
		
		logger.info("File Saved: " + connectionFileName);
	}
}
