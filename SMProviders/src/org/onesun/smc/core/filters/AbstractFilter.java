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
package org.onesun.smc.core.filters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.onesun.smc.api.Filter;
import org.onesun.smc.core.metadata.FilterMetadata;
import org.onesun.smc.core.model.RequestParamObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public abstract class AbstractFilter implements Filter {
	private static Logger logger = Logger.getLogger(AbstractFilter.class);

	protected Map<String, FilterMetadata> filters = new TreeMap<String, FilterMetadata>();

	@Override
	public abstract String getIdentity();

	public boolean containsKey(String key) {
		return filters.containsKey(key);
	}

	public FilterMetadata get(String key) {
		return filters.get(key);
	}

	@Override
	public abstract void init();

	public Set<String> keySet() {
		return filters.keySet();
	}

	public void put(String key, FilterMetadata value) {
		filters.put(key, value);
	}

	public int size() {
		return filters.size();
	}

	public Collection<FilterMetadata> values() {
		return filters.values();
	}


	@Override
	public boolean load(String pathToImports) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void save(String pathToExports) {
		try {
			String dirPath = pathToExports + getIdentity() + "/";
			File dir = new File(dirPath);
			if(dir.exists() == false){
				logger.info("Export: " + dir.getAbsolutePath());
				dir.mkdirs();
			}

			DocumentBuilderFactory documentBuilderFactory = 
					DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = 
					documentBuilderFactory.newDocumentBuilder();
			Document document = null;
			Element root = null;

			OutputFormat format = null;
			String path = null;
			File file = null;

			// filter must be loaded ... Provider must be initialized (lazy loading)
			if(filters.size() == 0){
				init();
			}

			// Write
			path = dirPath + "filters.xml";
			file = new File(path);
			if(file.exists() == false){
				document = documentBuilder.newDocument();
				root = document.createElement("root");
				root.setAttribute("identity", getIdentity());

				for(String key : filters.keySet()){
					FilterMetadata fm = filters.get(key);
					if(fm != null){
						Element filterNode = document.createElement("filter");
						filterNode.setAttribute("identity", key);
						String text = null;

						// Pay load
						RequestParamObject object = fm.getPayload();
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

							filterNode.appendChild(node);
						}

						// Params
						Element parent = document.createElement("params");
						for(RequestParamObject r : fm.paramValues()){
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
						filterNode.appendChild(parent);
						
						
						// Headers
						parent = document.createElement("headers");
						for(RequestParamObject r : fm.paramValues()){
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
						filterNode.appendChild(parent);
						
						root.appendChild(filterNode);
					}
				}

				document.appendChild(root);

				format = new OutputFormat(document);
				format.setIndenting(true);

				XMLSerializer serializer = new XMLSerializer(new FileOutputStream(file), format);
				logger.info("Serializing completed for: " + file.getAbsolutePath());
				serializer.serialize(document);
			}
		} catch (ParserConfigurationException e) {
			logger.error("ParserConfigurationException while creating new document instance: " + e.getMessage());
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException while creating new document instance: " + e.getMessage());
		} catch (IOException e) {
			logger.error("IOException while creating new document instance: " + e.getMessage());
		}finally {
		}
	}
}
