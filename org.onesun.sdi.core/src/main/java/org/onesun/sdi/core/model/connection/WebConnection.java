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

import javax.xml.parsers.ParserConfigurationException;

import org.onesun.commons.xml.XMLUtils;
import org.onesun.sdi.core.api.Connection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class WebConnection extends AbstractConnection {
	public WebConnection(String identity, String category, String authentication) {
		super(identity, category, authentication);
	}

	protected String url = null;
	protected String username = null;
	protected String password = null;
	
	public String toString(){
		return "[" + name + "," + url + "," + username + "," + password + "]";
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
	
	@Override
	public Element toElement(Document document) throws ParserConfigurationException {
		Element parent = super.toElement(document);
		
		Element child = document.createElement("url");
		child.setTextContent((url != null) ? url : "");
		parent.appendChild(child);
		
		child = document.createElement("username");
		child.setTextContent((username != null) ? username : "");
		parent.appendChild(child);
		
		child = document.createElement("password");
		child.setTextContent((password != null) ? password : "");
		parent.appendChild(child);
		
		return parent;
	}
	
	@Override
	public Connection toConnection(Element element) throws ParserConfigurationException {
		super.toConnection(element);
		
		String value = null;
		
		value = XMLUtils.getValue(element, "url");
		setUrl((value != null) ? value : "");
		
		value = XMLUtils.getValue(element, "username");
		setUsername((value != null) ?  value : "");

		value = XMLUtils.getValue(element, "password");
		setPassword((value != null) ? value : "");
		
		return this;
	}
}
