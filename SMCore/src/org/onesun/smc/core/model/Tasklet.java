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
package org.onesun.smc.core.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.api.ConnectionProperties;
import org.onesun.smc.api.Exporter;
import org.onesun.smc.api.Resource;
import org.onesun.smc.core.metadata.FilterMetadata;
import org.onesun.smc.core.metadata.Metadata;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class Tasklet implements Exporter, Cloneable {
	private String name;
	private String identity;
	private ConnectionProperties connection;
	private Resource resource;
	private Metadata metadata;
	private FilterMetadata filterMetadata;
	private Schedule schedule;

	public String toString(){
		return name;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch(CloneNotSupportedException e) {
			return this;
		}
	}
	
	public String getIdentity() {
		return identity;
	}
	
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public ConnectionProperties getConnection() {
		return connection;
	}
	public void setConnection(ConnectionProperties connection) {
		this.connection = connection;
	}

	public String toXML(){
		try {
			Document document = toDocument();
			OutputFormat format = new OutputFormat(document);
			format.setIndenting(true);

			Writer writer = new StringWriter();
			XMLSerializer serializer = new XMLSerializer(writer, format);
			serializer.serialize(document);
			
			String text = writer.toString();
			writer.close();
			
			return text;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		}
		
		return null;
	}

	public void reset() {
		name = null;
		connection = null;
		resource = null;
		metadata = null;
		filterMetadata = null;
		schedule = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public FilterMetadata getFilterMetadata() {
		return filterMetadata;
	}

	public void setFilterMetadata(FilterMetadata filterMetadata) {
		this.filterMetadata = filterMetadata;
	}

	private Document toDocument() throws ParserConfigurationException{
		Document document = XMLUtils.newDocument();
		
		Element root = document.createElement("root");
		if(name != null){
			root.setAttribute("name", name);
		}
		if(identity == null){
			identity = UUID.randomUUID().toString();
		}
		root.setAttribute("identity", identity);

		Element parent = null;

		// connection
		if(connection != null){
			parent = connection.toElement(document);
			root.appendChild(parent);
		}

		// resource
		if(resource != null){
			parent = resource.toElement(document);
			root.appendChild(parent);
		}

		// metadata
		if(metadata != null){
			parent = metadata.toElement(document);
			root.appendChild(parent);
		}

		// filter metadata
		if(filterMetadata != null){
			parent = filterMetadata.toElement(document);
			root.appendChild(parent);
		}

		// return filled up document
		document.appendChild(root);

		return document;
	}
	
	@Override
	public boolean save(String pathToExports) {
		try {
			Document document = toDocument();
			OutputFormat format = new OutputFormat(document);
			format.setIndenting(true);

			File file = new File(pathToExports);
			XMLSerializer serializer = new XMLSerializer(new FileOutputStream(file), format);
			serializer.serialize(document);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{

		}
		return false;
	}
}
