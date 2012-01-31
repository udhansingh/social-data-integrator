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

import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.api.ConnectionProperties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractConnectionProperties implements ConnectionProperties {
	protected String name = null;
	protected String identity = null;
	protected String category = null;
	protected String authentication = null;

	public AbstractConnectionProperties(String identity, String category, String authentication){
		this.identity = identity;
		this.category = category;
		this.authentication = authentication;
	}
	
	public String getCategory() {
		return category;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthentication() {
		return authentication;
	}
	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}
	
	@Override 
	public abstract void read(Properties properties);
	
	@Override
	public Element toElement(Document document) throws ParserConfigurationException{
		if(document == null){
			document = XMLUtils.newDocument();
		}

		Element parent = document.createElement("connection");
		Element child = document.createElement("identity");
		child.setTextContent((identity != null) ? identity : "");
		parent.appendChild(child);

		child = document.createElement("category");
		child.setTextContent((category != null) ? category : "");
		parent.appendChild(child);

		child = document.createElement("name");
		child.setTextContent((name != null) ? name : "");
		parent.appendChild(child);

		child = document.createElement("authentication");
		child.setTextContent((authentication != null) ? authentication : "");
		
		parent.appendChild(child);

		return parent;
	}
}
