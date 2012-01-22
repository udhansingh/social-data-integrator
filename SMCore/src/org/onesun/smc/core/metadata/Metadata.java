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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.math.NumberUtils;
import org.onesun.smc.core.model.MetaObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	
	public Document toDocument() {
		try {
			DocumentBuilderFactory documentBuilderFactory = 
					DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = 
					documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			
			Element root = document.createElement("root");

			Element parent = null;
			Element child = null;
			MetaObject mo = null;
			
			// node name
			mo = new MetaObject();
			mo.setName(nodeName);
			mo.setPath("social.media.internal.node.name");

			parent = document.createElement("item");
			
			child = document.createElement("path");
			child.setTextContent(mo.getPath());
			parent.appendChild(child);
			
			child = document.createElement("name");
			child.setTextContent(mo.getName());
			parent.appendChild(child);
			
			child = document.createElement("type");
			child.setTextContent(mo.getType().getName());
			parent.appendChild(child);
			
			root.appendChild(parent);
			
			// url
			mo = new MetaObject();
			mo.setName(url);
			mo.setPath("social.media.internal.url");

			parent = document.createElement("item");
			
			child = document.createElement("path");
			child.setTextContent(mo.getPath());
			parent.appendChild(child);
			
			child = document.createElement("name");
			child.setTextContent(mo.getName());
			parent.appendChild(child);

			child = document.createElement("type");
			child.setTextContent(mo.getType().getName());
			parent.appendChild(child);
			
			root.appendChild(parent);
			
			
			// verb
			mo = new MetaObject();
			mo.setName(verb);
			mo.setPath("social.media.internal.verb");

			parent = document.createElement("item");
			
			child = document.createElement("path");
			child.setTextContent(mo.getPath());
			parent.appendChild(child);
			
			child = document.createElement("name");
			child.setTextContent(mo.getName());
			parent.appendChild(child);
			
			child = document.createElement("type");
			child.setTextContent(mo.getType().getName());
			parent.appendChild(child);
			
			root.appendChild(parent);
			
			// rest of the items
			for(String k : map.keySet()){
				mo = map.get(k);
				
				parent = document.createElement("item");
				
				child = document.createElement("path");
				child.setTextContent(mo.getPath());
				parent.appendChild(child);
				
				child = document.createElement("name");
				child.setTextContent(mo.getName());
				parent.appendChild(child);

				child = document.createElement("type");
				child.setTextContent(mo.getType().getName());
				parent.appendChild(child);
				
				child = document.createElement("size");
				child.setTextContent(Integer.toString(mo.getSize()));
				parent.appendChild(child);
				
				Boolean flag = mo.isIgnore();
				if(flag == true){
					child = document.createElement("ignore");
					child.setTextContent(Boolean.toString(flag));
					parent.appendChild(child);
				}
				
				root.appendChild(parent);
			}
				
			root.appendChild(parent);
			document.appendChild(root);
			
			return document;
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}finally{
		}
		
		return null;
	}

	public Collection<MetaObject> values() {
		return map.values();
	}
}
