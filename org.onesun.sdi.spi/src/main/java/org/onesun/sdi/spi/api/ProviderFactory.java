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
package org.onesun.sdi.spi.api;

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
import org.onesun.sdi.core.api.Exporter;
import org.onesun.sdi.core.api.ServiceProvider;
import org.onesun.sdi.core.tools.XMLImporter;
import org.onesun.sdi.spi.filesystem.FileProvider;
import org.onesun.sdi.spi.socialmedia.DropboxProvider;
import org.onesun.sdi.spi.socialmedia.FacebookProvider;
import org.onesun.sdi.spi.socialmedia.FoursquareProvider;
import org.onesun.sdi.spi.socialmedia.GoogleMailProvider;
import org.onesun.sdi.spi.socialmedia.LinkedInProvider;
import org.onesun.sdi.spi.socialmedia.TwitterProvider;
import org.onesun.sdi.spi.socialmedia.YahooProvider;
import org.onesun.sdi.spi.socialmedia.YouTubeProvider;
import org.onesun.sdi.spi.stream.FacebookStreamingProvider;
import org.onesun.sdi.spi.stream.TwitterStreamingProvider;
import org.onesun.sdi.spi.web.RESTProvider;
import org.onesun.sdi.spi.web.connotate.ConnotateProvider;
import org.onesun.sdi.spi.web.kapow.KapowProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ProviderFactory {
	private static Logger logger = Logger.getLogger(ProviderFactory.class);

	private static Map<String, ServiceProvider>	providers				= new TreeMap<String, ServiceProvider>();

	private static List<Item> init() {
		List<Item> items = new ArrayList<Item>();
		Item item = null;

		item = new Item();
		item.setIdentity("General");
		item.setClazz(RESTProvider.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setIdentity("File System");
		item.setClazz(FileProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("Facebook");
		item.setClazz(FacebookProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("Foursquare");
		item.setClazz(FoursquareProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("Google Plus");
		item.setClazz(FileProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("Google Mail");
		item.setClazz(GoogleMailProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("YouTube");
		item.setClazz(YouTubeProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("LinkedIn");
		item.setClazz(LinkedInProvider.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setIdentity("Facebook Streaming");
		item.setClazz(FacebookStreamingProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("Twitter");
		item.setClazz(TwitterProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("Twitter Streaming");
		item.setClazz(TwitterStreamingProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("Kapow");
		item.setClazz(KapowProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("Connotate");
		item.setClazz(ConnotateProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("Dropbox");
		item.setClazz(DropboxProvider.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setIdentity("Yahoo");
		item.setClazz(YahooProvider.class.getCanonicalName());
		items.add(item);
		
		return items;
	}

	public static ServiceProvider getProvider(String name){
		for(ServiceProvider p : providers.values()){
			if(p.getIdentity().compareToIgnoreCase(name) == 0){
				return p;
			}
		}

		return null;
	}

	public static List<String> getNames() {
		List<String> providerNames = new ArrayList<String>();
		// providerNames.add(nullProvider.getIdentity());

		for(String key : providers.keySet()){
			providerNames.add(key);
		}

		return providerNames;
	}

	public static ServiceProvider getProvider(String name, String category) {
		for(ServiceProvider p : providers.values()){
			if(p.getIdentity().compareToIgnoreCase(name) == 0 && p.getCategory().compareToIgnoreCase(category) == 0){
				return p;
			}
		}

		return null;
	}

	public static List<ServiceProvider> getProviderNamesByCategory(String category) {
		List<ServiceProvider> list = new ArrayList<ServiceProvider>();

		for(ServiceProvider p : providers.values()){
			if(p.getCategory().compareToIgnoreCase(category) == 0){
				list.add(p);
			}
		}

		return list;
	}

	public static void load(String pathToServicesFile, String pathToExports){
		ItemImporterExporter importerExporter = new ItemImporterExporter();
		List<Item> items = null;

		boolean status = importerExporter.load(pathToServicesFile);
		if(status == true){
			items = importerExporter.getItems();
			logger.info("Found " + items.size() + " provider entries");
		}else {
			items = init();
			importerExporter.setItems(items);
			importerExporter.save(pathToServicesFile);

			logger.info("Initilized and exported " + items.size() + " provider class entries");
		}

		if(items != null){
			for(Item item : items){
				logger.info("Instantiating Service Provider ... " + item.getClazz());
				
				try {
					Class<?> instance = Class.forName(item.getClazz());

					if(instance != null){
						ServiceProvider sp = (ServiceProvider)instance.newInstance();
						providers.put(item.getIdentity(), sp);

						// Try loading from existing files
						boolean flag = sp.load(pathToExports);

						// If files don't exist, bootstrap using coded resources
						if(flag == false){
							sp.init();
							sp.save(pathToExports);
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static class Item {
		private String clazz;
		private String identity;

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
		public boolean load(String pathToImports) {
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
