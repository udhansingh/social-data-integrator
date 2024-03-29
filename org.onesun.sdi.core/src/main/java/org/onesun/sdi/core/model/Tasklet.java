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
package org.onesun.sdi.core.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.onesun.commons.xml.XMLUtils;
import org.onesun.sdi.core.api.Connection;
import org.onesun.sdi.core.api.ConnectionsFactory;
import org.onesun.sdi.core.api.Exporter;
import org.onesun.sdi.core.api.Resource;
import org.onesun.sdi.core.api.ResourceFactory;
import org.onesun.sdi.core.metadata.FilterMetadata;
import org.onesun.sdi.core.metadata.Metadata;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class Tasklet implements Exporter, Cloneable {
	private String name;
	private String identity;
	private Connection connectionProperties;
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

	public Connection getConnectionProperties() {
		return connectionProperties;
	}
	public void setConnectionProperties(Connection connectionProperties) {
		this.connectionProperties = connectionProperties;
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
		connectionProperties = null;
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
		if(connectionProperties != null){
			parent = connectionProperties.toElement(document);
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

	public static Tasklet toTasklet(Document document) {
		Tasklet tasklet = new Tasklet();
		
		Element root = document.getDocumentElement();
		tasklet.setIdentity(root.getAttribute("identity"));
		tasklet.setName(root.getAttribute("name"));

		Element element = null;
		
		// process connection
		element = XMLUtils.getElement(root, "connection");
		tasklet.setConnectionProperties(toConnection(element));

		// process resource
		element = XMLUtils.getElement(root, "resource");
		tasklet.setResource(toResource(element));

		// process metadata
		element = XMLUtils.getElement(root, "metadata");
		tasklet.setMetadata(toMetadata(element));
		
		// process filter
		element = XMLUtils.getElement(root, "filter");
		tasklet.setFilterMetadata(toFilterMetadata(element));
		
		return tasklet;
	}

	private static FilterMetadata toFilterMetadata(Element element) {
		return FilterMetadata.toFilterMetadata(element);
	}

	private static Metadata toMetadata(Element element) {
		return Metadata.toMetadata(element);
	}

	private static Resource toResource(Element element) {
		try {
			String type = XMLUtils.getValue(element, "type");
			return ResourceFactory.toResource(type, element);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private static Connection toConnection(Element element) {
		try {
			String authentication = XMLUtils.getValue(element, "authentication");
			return ConnectionsFactory.toConnection(authentication, element);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
