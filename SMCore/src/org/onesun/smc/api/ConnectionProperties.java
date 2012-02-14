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
package org.onesun.smc.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface ConnectionProperties {
	final static String FILE_EXTENSION = ".cnx";
	
	String getCategory();
	
	String getIdentity();
	
	void setIdentity(String identity);
	
	String getName();
	
	void setName(String connectionName);
	
	String getAuthentication();
	
	void setAuthentication(String authentication);
	
	void read(Properties properties);
	
	void save(String path) throws FileNotFoundException, IOException;
	
	Element toElement(Document document) throws ParserConfigurationException;
	
	ConnectionProperties fromElement(Element element) throws ParserConfigurationException;
}
