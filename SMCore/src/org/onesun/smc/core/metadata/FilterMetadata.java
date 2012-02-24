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
package org.onesun.smc.core.metadata;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.core.model.Parameter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FilterMetadata {
	// Key: Internal Name
	// Value: Filter Object
	private Map<String, Parameter> headers = Collections.synchronizedMap(new TreeMap<String, Parameter>());
	private Map<String, Parameter> params = Collections.synchronizedMap(new TreeMap<String, Parameter>());
	private Parameter payload = null;
	
	public void setParams(Map<String, Parameter> params){
		this.params = params;
	}
	
	public void setHeaders(Map<String, Parameter> headers){
		this.headers = headers;
	}
	
	public boolean containsHeaderKey(String key) {
		return headers.containsKey(key);
	}

	public Parameter getHeaderObject(String key) {
		return headers.get(key);
	}
	

	public Set<String> headersKeySet() {
		return headers.keySet();
	}

	public void putHeaderObject(String key, Parameter value) {
		headers.put(key, value);
	}

	
	public Collection<Parameter> headerValues() {
		return headers.values();
	}
	
	public boolean containsParamKey(String key) {
		return params.containsKey(key);
	}

	public Parameter getParamObject(String key) {
		return params.get(key);
	}
	

	public Set<String> paramsKeySet() {
		return params.keySet();
	}

	public void putParamObject(String key, Parameter value) {
		params.put(key, value);
	}

	
	public Collection<Parameter> paramValues() {
		return params.values();
	}

	public Parameter getPayload() {
		return payload;
	}

	public void setPayload(Parameter payload) {
		this.payload = payload;
	}
	
	public Element toElement(Document document) throws ParserConfigurationException{
		Element filter = document.createElement("filter");
		
		String text = null;

		// Pay load
		Parameter object = getPayload();
		if(object != null){
			Element node = document.createElement("payload");

			Element e = document.createElement("externalName");
			text = object.getExternalName();
			text = (text == null) ? "" : text;
			e.setTextContent(text);
			node.appendChild(e);

			e = document.createElement("internalName");
			text = object.getInternalName();
			text = (text == null) ? "" : text;
			e.setTextContent(text);
			node.appendChild(e);

			e = document.createElement("defaultValue");
			text = object.getDefaultValue();
			text = (text == null) ? "" : text;
			e.setTextContent(text);
			node.appendChild(e);

			filter.appendChild(node);
		}

		// Params
		Element parent = document.createElement("params");
		for(Parameter r : paramValues()){
			Element node = document.createElement("param");

			Element e = document.createElement("externalName");
			text = r.getExternalName();
			text = (text == null) ? "" : text;
			e.setTextContent(text);
			node.appendChild(e);

			e = document.createElement("internalName");
			text = r.getInternalName();
			text = (text == null) ? "" : text;
			e.setTextContent(text);
			node.appendChild(e);

			e = document.createElement("defaultValue");
			text = r.getDefaultValue();
			text = (text == null) ? "" : text;
			e.setTextContent(text);
			node.appendChild(e);

			parent.appendChild(node);
		}
		filter.appendChild(parent);


		// Headers
		parent = document.createElement("headers");
		for(Parameter r : headerValues()){
			Element node = document.createElement("header");

			Element e = document.createElement("externalName");
			text = r.getExternalName();
			text = (text == null) ? "" : text;
			e.setTextContent(text);
			node.appendChild(e);

			e = document.createElement("internalName");
			text = r.getInternalName();
			text = (text == null) ? "" : text;
			e.setTextContent(text);
			node.appendChild(e);

			e = document.createElement("defaultValue");
			text = r.getDefaultValue();
			text = (text == null) ? "" : text;
			e.setTextContent(text);
			node.appendChild(e);

			parent.appendChild(node);
		}
		filter.appendChild(parent);

		return filter;
	}

	public static FilterMetadata toFilterMetadata(Element element) {
		FilterMetadata fm = new FilterMetadata();
		
		// Read params
		Element p = XMLUtils.getElement(element, "params");
		NodeList nodes = p.getElementsByTagName("param");
		if(nodes != null && nodes.getLength() > 0){
			for(int index = 0; index < nodes.getLength(); index++){
				Element child = (Element)nodes.item(index);

				try{
					String en = XMLUtils.getValue(child, "externalName");
					String in = XMLUtils.getValue(child, "internalName");
					String dv = XMLUtils.getValue(child, "defaultValue");
					
					if(en != null && in != null){
						Parameter pm = new Parameter(en, in, dv);
						fm.params.put(en, pm);
					}
				}catch(Exception e){
					System.out.println("Exception while extracting values from xml element : " + e.getMessage());
				}
			}
		}
		
		// Read headers
		Element h = XMLUtils.getElement(element, "headers");
		nodes = h.getElementsByTagName("header");
		if(nodes != null && nodes.getLength() > 0){
			for(int index = 0; index < nodes.getLength(); index++){
				Element child = (Element)nodes.item(index);

				try{
					String en = XMLUtils.getValue(child, "externalName");
					String in = XMLUtils.getValue(child, "internalName");
					String dv = XMLUtils.getValue(child, "defaultValue");
					
					if(en != null && in != null){
						Parameter pm = new Parameter(en, in, dv);
						fm.headers.put(en, pm);
					}
				}catch(Exception e){
					System.out.println("Exception while extracting values from xml element : " + e.getMessage());
				}
			}
		}
		
		// Read payload
		Element l = XMLUtils.getElement(element, "payload");
		String en = XMLUtils.getValue(l, "externalName");
		String in = XMLUtils.getValue(l, "internalName");
		String dv = XMLUtils.getValue(l, "defaultValue");
		
		if(en != null && in != null){
			fm.payload = new Parameter(en, in, dv);
		}
		
		return fm;
	}
}
