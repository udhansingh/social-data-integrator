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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.math.NumberUtils;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.api.DataTypeFactory;
import org.onesun.smc.core.model.MetaObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Metadata implements Cloneable {
	private boolean discovered = true;
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch(CloneNotSupportedException e) {
			return this;
		}
	}
	
	public boolean isDiscovered() {
		return discovered;
	}

	public void setDiscovered(boolean discovered) {
		this.discovered = discovered;
	}

	// key=Path, value=Name
	private String name = null;
	private Map<String, MetaObject> map = Collections.synchronizedMap(new TreeMap<String, MetaObject>());
	private String nodeName = null;
	private String url = null;
	private String verb = null;

	public void compact() {
		if(map.size() == 0){
			return;
		}
		
		Set<String> compactKeySet = new HashSet<String>();
		Set<String> removalKeySet = new HashSet<String>();
		// Pass 1 : process each key and find out if there is an unwanted object; any node name that begins with a number need not be persisted.
		for(String key : map.keySet()){
			String pk = key.replace("*", "").replace("@", ""); 		// remove special characters (used by processing logic)
			String[] tokens = pk.split("/");						// Split by Path character
			String token = (tokens != null && tokens.length > 0) ? tokens[tokens.length - 1] : null;
			
			if(token != null){
				boolean status = NumberUtils.isNumber(token);
				if(status == true){
					if(tokens.length > 1){
						compactKeySet.add(tokens[0]);
						removalKeySet.add(key);
					}
					else if(tokens.length == 0){
						removalKeySet.add(key);
					}
				}
			}
		}
		
		// Pass 2 : Process compactKeySet and removalKeySet
		if(compactKeySet.size() > 0){
			for(String key : compactKeySet){
				map.put(key + "*", map.get(key));
				map.remove(key);
			}
		}
		if(removalKeySet.size() > 0){
			for(String key : removalKeySet){
				map.remove(key);
			}
		}
	}
		
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	public MetaObject get(String key) {
		return map.get(key);
	}
	
	public String getNodeName() {
		return nodeName;
	}

	public String getVerb() {
		return verb;
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	private String normalized(String value){
		if(value != null){
			return value.replace("/", "_").replace("-", "_").replace("@", "").replace("*", "").replace(":", "");
		}
		else {
			return null;
		}
	}

	public void put(String key, MetaObject value) {
		value.setName(normalized(value.getPath()));
		map.put(key, value);
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}
	
	public int size() {
		return map.size();
	}
	
	public Element toElement(Document document) throws ParserConfigurationException {
		Element parent = document.createElement("metadata");
		Element child = document.createElement("verb");
		child.setTextContent((verb != null) ? verb : "");
		parent.appendChild(child);

		child = document.createElement("name");
		child.setTextContent((name != null) ? name : "");
		parent.appendChild(child);

		child = document.createElement("url");
		child.setTextContent((url != null) ? url : "");
		parent.appendChild(child);

		child = document.createElement("discovered");
		child.setTextContent(Boolean.toString(discovered));
		parent.appendChild(child);

		child = document.createElement("nodeName");
		child.setTextContent((nodeName != null) ? nodeName : "");
		parent.appendChild(child);

		child = document.createElement("items");
		for(String key : map.keySet()){
			MetaObject mo = map.get(key);

			Element grandChild = document.createElement("item");

			Element element = document.createElement("path");
			element.setTextContent(mo.getPath());
			grandChild.appendChild(element);

			element = document.createElement("name");
			element.setTextContent(mo.getName());
			grandChild.appendChild(element);

			element = document.createElement("type");
			element.setTextContent(mo.getType().getName());
			grandChild.appendChild(element);

			element = document.createElement("size");
			element.setTextContent(Double.toString(mo.getSize()));
			grandChild.appendChild(element);

			Boolean flag = mo.isIgnore();
			if(flag == true){
				element = document.createElement("ignore");
				element.setTextContent(Boolean.toString(flag));
				grandChild.appendChild(element);
			}

			child.appendChild(grandChild);
		}
		parent.appendChild(child);

		return parent;
	}

	public Collection<MetaObject> values() {
		return map.values();
	}
	
	public static Metadata toMetadata(Element element){
		Metadata metadata = new Metadata();
		
		metadata.setVerb(XMLUtils.getValue(element, "verb"));
		metadata.setUrl(XMLUtils.getValue(element, "url"));
		
		String value = XMLUtils.getValue(element, "discovered");
		metadata.setDiscovered(Boolean.parseBoolean((value == null)? "false" : value));

		metadata.setName(XMLUtils.getValue(element, "name"));
		metadata.setNodeName(XMLUtils.getValue(element, "nodeName"));

		NodeList nodes = element.getElementsByTagName("item");
		if(nodes != null && nodes.getLength() > 0){
			for(int index = 0; index < nodes.getLength(); index++){
				Element child = (Element)nodes.item(index);

				if(child == null) continue;
				
				try{
					MetaObject item = new MetaObject();
					
					item.setName(XMLUtils.getValue(child, "name"));
					item.setPath(XMLUtils.getValue(child, "path"));
					item.setType(
						DataTypeFactory.getDataType(
							XMLUtils.getValue(child, "type")
						)
					);
					value = XMLUtils.getValue(child, "size");
					item.setSize((value == null) ? -1 : Integer.parseInt(value));
					
					value = XMLUtils.getValue(child, "ignore");
					item.setIgnore((value == null) ? false : Boolean.parseBoolean(value));

					// Add to the meta-data only if it must be displayed and is relevant to the use case;
					if((item.getSize() != -1) && (item.getPath().startsWith("social.media.internal.metadata.mapping.") == false)){
						metadata.put(item.getPath(), item);
					}
				}catch(Exception e){
					System.out.println("Exception while extracting values from xml element : " + e.getMessage());
				}
			}
		}
		
		return metadata;
	}
}
