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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.core.services.data.AbstractDBService;
import org.onesun.smc.core.services.text.analysis.OpenCalaisEntityExtractor;
import org.onesun.smc.core.services.text.analysis.UClassifyMoodDetector;
import org.onesun.smc.core.services.text.analysis.UClassifySentimentAnalyzer;
import org.onesun.smc.core.tools.XMLImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class DataServicesFactory {
	private static Logger logger = Logger.getLogger(DataServicesFactory.class);

	private DataServicesFactory() {}

	private static Map<String, DataService> services = new TreeMap<String, DataService>();

	private static List<Item> init() {
		List<Item> items = new ArrayList<Item>();
		Item item = null;

		item = new Item();
		item.setIdentity("uclassify_mood");
		item.setName("UClassify Mood Detector");
		item.setClazz(UClassifyMoodDetector.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setIdentity("uclassify_sentiment");
		item.setName("UClassify Sentiment Analyzer");
		item.setClazz(UClassifySentimentAnalyzer.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setIdentity("opencalais_entities");
		item.setName("OpenCalais Entity Extractor");
		item.setClazz(OpenCalaisEntityExtractor.class.getCanonicalName());
		items.add(item);
		
//		item = new Item();
//		item.setIdentity("null");
//		item.setName("DB Writer Service");
//		item.setClazz(HSQLDBWriter.class.getCanonicalName());
//		items.add(item);

		return items;
	}

	public static DataService getDataService(String name){
		return services.get(name);
	}

	public static String[] getServiceNames() {
		String[] values = new String[services.size()];

		int index = 0;
		for(String k : services.keySet()){
			values[index] = k;
			index++;
		}

		return values;
	}

	public static void load(String pathToServicesFile){
		ItemImporterExporter importerExporter = new ItemImporterExporter();
		boolean status = importerExporter.load(pathToServicesFile);
		List<Item> items = null;

		if(status == true){
			items = importerExporter.getItems();

			logger.info("Found " + items.size() + " service entries");
		}
		else {
			items = init();
			importerExporter.setItems(items);
			importerExporter.save(pathToServicesFile);

			logger.info("Initilized and exported " + items.size() + " data services class entries");
		}

		if(items != null){
			for(Item item : items){
				logger.info("Instantiating data service ... " + item.getClazz());
				
				Class<?> instance = null;
				try {
					instance = Class.forName(item.getClazz());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				try {
					if(instance != null){
						TextAnalysisService ds = (TextAnalysisService)instance.newInstance();
						ds.setColumnName(item.getIdentity());
						services.put(item.getName(), ds);
					}
				} catch(ClassCastException e){
					if(instance != null){
						try {
							DataService ds = (DataService)instance.newInstance();
							services.put(item.getName(), ds);
						} catch (InstantiationException ex) {
							ex.printStackTrace();
						} catch (IllegalAccessException ex) {
							ex.printStackTrace();
						}
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class Item {
		private String name;
		private String clazz;
		private String identity;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getClazz() {
			return clazz;
		}
		public void setClazz(String clazz) {
			this.clazz = clazz;
		}
		public String getIdentity() {
			return identity;
		}
		public void setIdentity(String identity) {
			this.identity = identity;
		}
	}

	private static class ItemImporterExporter extends XMLImporter implements Exporter {
		public ItemImporterExporter() {
			super();
		}

		private List<Item> items = new ArrayList<Item>();

		public List<Item> getItems(){
			return items;
		}

		public void setItems(List<Item> items) {
			this.items = items;			
		}

		@Override
		public boolean load(String pathToImports){
			File file = new File(pathToImports);
			setResource(file);

			init();
			process();

			if(items != null && items.size() > 0){
				return true;
			}

			return false;
		}

		@Override
		public void process() {
		}

		@Override
		public void parse(Object object) {
			if(object != null && object instanceof Document){
				Document document = (Document)object;

				Element root = document.getDocumentElement();

				NodeList nodes = root.getElementsByTagName("item");
				if(nodes != null && nodes.getLength() > 0){
					for(int index = 0; index < nodes.getLength(); index++){
						Element element = (Element)nodes.item(index);

						try{
							Item item = new Item();
							item.setIdentity(XMLUtils.getValue(element, "identity"));
							item.setName(XMLUtils.getValue(element, "name"));
							item.setClazz(XMLUtils.getValue(element, "class"));

							items.add(item);
						}catch(Exception e){
							logger.error("Exception while extracting subscriptions : " + e.getMessage());
						}
					}
				}
			}
		}

		@Override
		public boolean save(String pathToExports) {
			try {
				DocumentBuilderFactory documentBuilderFactory = 
						DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = 
						documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();

				Element root = document.createElement("root");
				OutputFormat format = null;

				for(Item item : items){
					Element attribute = null;
					Element itemElement = document.createElement("item");

					attribute = document.createElement("name");
					attribute.setTextContent(item.getName());
					itemElement.appendChild(attribute);

					attribute = document.createElement("identity");
					attribute.setTextContent(item.getIdentity());
					itemElement.appendChild(attribute);

					attribute = document.createElement("class");
					attribute.setTextContent(item.getClazz());
					itemElement.appendChild(attribute);

					root.appendChild(itemElement);
				}

				document.appendChild(root);

				format = new OutputFormat(document);
				format.setIndenting(true);

				File file = new File(pathToExports);
				XMLSerializer serializer = new XMLSerializer(new FileOutputStream(file), format);
				logger.info("Serializing completed for: " + file.getAbsolutePath());
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
}

