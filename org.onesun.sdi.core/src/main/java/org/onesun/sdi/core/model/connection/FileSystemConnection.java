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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FileSystemConnection extends AbstractConnection {
	private static Logger logger = Logger.getLogger(FileSystemConnection.class);
	
	private String path = "/";
	private List<String> filter = new ArrayList<String>();
	
	public FileSystemConnection(){
		super("File System", "FILE_SYSTEM", "FILE_SYSTEM");
		
		filter.add("ATOM");
		filter.add("JSON");
		filter.add("RSS");
		filter.add("XML");
	}
	
	public String toString(){
		return "[" + name + "," + path + "," + filter + "]";
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		if(filter != null){
			String[] values = filter.split(",");
			this.filter.clear();
			
			for(String value : values){
				this.filter.add(value.trim());
			}
		}
	}

	@Override
	public void read(Properties properties) {
		setName(properties.getProperty("connectionName"));
		setIdentity(properties.getProperty("identity"));
		setPath(properties.getProperty("path"));
		setFilter(properties.getProperty("filter"));
	}
	
	@Override
	public void save(String path) throws FileNotFoundException, IOException {
		String connectionFileName = path + getName() + FILE_EXTENSION;
		
		File file = new File(connectionFileName);
		
		FileOutputStream fos = new FileOutputStream(file);
		
		Properties properties = new Properties();
		properties.put("authentication", getAuthentication().trim());
		properties.put("connectionName", getName().trim());
		
		String providerName = getIdentity();
		providerName = (providerName == null || (providerName != null && providerName.isEmpty())) ? "File System" : providerName;
		
		properties.put("identity", providerName.trim());
		
		properties.put("path", getPath().trim());
		properties.put("filter", getFilter().toString().replace("[", "").replace("]", ""));
		
		properties.store(fos, "File Connection Properties");
		
		fos.close();
		
		logger.info("File Saved: " + connectionFileName);
	}
	
	@Override
	public Element toElement(Document document) throws ParserConfigurationException {
		Element parent = super.toElement(document);
		
		Element child = document.createElement("filter");
		child.setTextContent((filter != null) ? filter.toString().replace("[", "").replace("]", "") : "");
		parent.appendChild(child);
		
		child = document.createElement("path");
		child.setTextContent((path != null) ? path : "");
		parent.appendChild(child);
		
		return parent;
	}
}
