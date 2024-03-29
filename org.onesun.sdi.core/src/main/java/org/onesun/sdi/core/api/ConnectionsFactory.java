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
package org.onesun.sdi.core.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.sdi.core.model.connection.ConnotateConnection;
import org.onesun.sdi.core.model.connection.DataSiftConnection;
import org.onesun.sdi.core.model.connection.FacebookStreamingConnection;
import org.onesun.sdi.core.model.connection.FileSystemConnection;
import org.onesun.sdi.core.model.connection.KapowConnection;
import org.onesun.sdi.core.model.connection.RESTConnection;
import org.onesun.sdi.core.model.connection.OAuthConnection;
import org.onesun.sdi.core.model.connection.TwitterStreamingConnection;
import org.onesun.sdi.core.tools.XMLImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ConnectionsFactory {
	private static Logger logger = Logger.getLogger(ConnectionsFactory.class);
	
	private ConnectionsFactory(){
	}
	
	private static Map<String, Connection> connections = new TreeMap<String, Connection>();
	private static Map<String, Item> connectionsByCategory = new TreeMap<String, Item>();
	
	public static Connection toConnection(File file) throws FileNotFoundException, IOException{
		final String fileName = file.getName();
		
		// Process .cnx files
		if(fileName.toLowerCase().endsWith(Connection.FILE_EXTENSION)){
			Properties properties = new Properties();

			Reader reader = new FileReader(file);
			properties.load(reader);
			reader.close();
			
			// Process Properties
			Connection connector = null;

			if(properties.containsKey("authentication") == true){
				final String authentication = (String)properties.get("authentication");
				
				if(connectionsByCategory.containsKey(authentication)){
					Item item = connectionsByCategory.get(authentication);
					
					try {
						Class<?> instance = Class.forName(item.getClazz());

						if(instance != null){
							connector = (Connection)instance.newInstance();
							connector.read(properties);
							
							logger.info("instantiating connection instance " + connector.getClass().getCanonicalName());
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
			
			return connector;
		}
		
		return null;
	}

	public static Connection toConnection(String authentication, 
			Element element) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException {
		// Process Properties
		Connection cp = null;
		
		if(connectionsByCategory.containsKey(authentication)){
			Item item = connectionsByCategory.get(authentication);

			Class<?> instance = Class.forName(item.getClazz());

			if(instance != null){
				cp = (Connection)instance.newInstance();
				cp.toConnection(element);

				logger.info("instantiating connection instance " + cp.getClass().getCanonicalName());
			}
		}
		
		return cp;
	}
	
	private static List<Item> init(){
		List<Item> items = new ArrayList<Item>();
		
		Item item = null;
		
		item = new Item();
		item.setName("General");
		item.setCategory("GENERAL");
		item.setAuthentication("REST");
		item.setClazz(RESTConnection.class.getCanonicalName());
		connectionsByCategory.put(item.getAuthentication(), item);
		items.add(item);
		
		item = new Item();
		item.setName("Connotate");
		item.setCategory("CONNOTATE");
		item.setAuthentication("CONNOTATE");
		item.setClazz(ConnotateConnection.class.getCanonicalName());
		connectionsByCategory.put(item.getAuthentication(), item);
		items.add(item);
		
		item = new Item();
		item.setName("Facebook Streaming");
		item.setCategory("FACEBOOK_STREAMING");
		item.setAuthentication("FACEBOOK_STREAMING");
		item.setClazz(FacebookStreamingConnection.class.getCanonicalName());
		connectionsByCategory.put(item.getAuthentication(), item);
		items.add(item);
		
		item = new Item();
		item.setName("File System");
		item.setCategory("FILE_SYSTEM");
		item.setAuthentication("FILE_SYSTEM");
		item.setClazz(FileSystemConnection.class.getCanonicalName());
		connectionsByCategory.put(item.getAuthentication(), item);
		items.add(item);
		
		item = new Item();
		item.setName("Kapow");
		item.setCategory("KAPOW");
		item.setAuthentication("KAPOW");
		item.setClazz(KapowConnection.class.getCanonicalName());
		connectionsByCategory.put(item.getAuthentication(), item);
		items.add(item);
		
		item = new Item();
		item.setName("Social Media");
		item.setCategory("SOCIAL_MEDIA");
		item.setAuthentication("OAUTH");
		item.setClazz(OAuthConnection.class.getCanonicalName());
		connectionsByCategory.put(item.getAuthentication(), item);
		items.add(item);
		
		item = new Item();
		item.setName("Twitter Streaming");
		item.setCategory("TWITTER_STREAMING");
		item.setAuthentication("TWITTER_STREAMING");
		item.setClazz(TwitterStreamingConnection.class.getCanonicalName());
		connectionsByCategory.put(item.getAuthentication(), item);
		items.add(item);

		item = new Item();
		item.setName("DataSift");
		item.setCategory("DATASIFT");
		item.setAuthentication("DATASIFT");
		item.setClazz(DataSiftConnection.class.getCanonicalName());
		connectionsByCategory.put(item.getAuthentication(), item);
		items.add(item);
		
		return items;
	}
	
	public static void load(String pathToServicesFile){
		ItemImporterExporter importerExporter = new ItemImporterExporter();
		boolean status = importerExporter.load(pathToServicesFile);
		
		if(status == true){
			List<Item> items = importerExporter.getItems();
			
			if(items != null){
				for(Item item : items){
					connectionsByCategory.put(item.getAuthentication(), item);
				}
			}

			logger.info("Found " + items.size() + " connector class entries, imported " + connectionsByCategory.size());
		}
		else {
			List<Item> items = init();
			importerExporter.setItems(items);
			importerExporter.save(pathToServicesFile);
			
			logger.info("Initilized " + items.size() + " connector class entries, exported " + connectionsByCategory.size());
		}
	}
	
	public static void loadConnection(String pathToConnections){
		File directory = new File(pathToConnections);
		
		if(directory.exists() == true){
			File[] files = directory.listFiles();
			
			for(File file : files){
				logger.info("Processing connection: " + file.getAbsolutePath());
				if(file.getName().endsWith(Connection.FILE_EXTENSION)){
					try {
						Connection connector = toConnection(file);
						connections.put(connector.getName(), connector);
					} catch(FileNotFoundException e){
						e.printStackTrace();
					} catch(IOException e){
						e.printStackTrace();
					} catch(NullPointerException e){
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void addConnection(Connection properties){
		connections.put(properties.getName(), properties);
	}
	
	public static void deleteConnection(final Connection properties, final String pathToConnection){
		connections.remove(properties.getName());
		
		File file = new File(pathToConnection + properties.getName() + ".cnx");
		boolean status = file.delete();
		
		logger.info("Delete file " + file.getAbsolutePath() + " status is " + status);
	}

	public static Map<String, Connection> getConnections() {
		return connections;
	}

	public static Connection getConnectionByName(String connectionName) {
		if(connectionName == null) return null;
		else return connections.get(connectionName);
	}
	
	private static class Item {
		private String name;
		private String category;
		private String authentication;
		private String clazz;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getAuthentication() {
			return authentication;
		}
		public void setAuthentication(String authentication) {
			this.authentication = authentication;
		}
		public String getClazz() {
			return clazz;
		}
		public void setClazz(String clazz) {
			this.clazz = clazz;
		}
		
		public String toString(){
			return getName() + ", " + getCategory() + ", " + getAuthentication() + ", " + getClazz();
		}
	}
	
	private static class ItemImporterExporter extends XMLImporter implements Exporter {
		public ItemImporterExporter() {
			super();
		}

		public void setItems(List<Item> items) {
			this.items = items;			
		}

		private List<Item> items = new ArrayList<Item>();

		public List<Item> getItems(){
			return items;
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
							item.setName(XMLUtils.getValue(element, "name"));
							item.setAuthentication(XMLUtils.getValue(element, "authentication"));
							item.setCategory(XMLUtils.getValue(element, "category"));
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
				
				attribute = document.createElement("category");
				attribute.setTextContent(item.getCategory());
				itemElement.appendChild(attribute);
				
				attribute = document.createElement("authentication");
				attribute.setTextContent(item.getAuthentication());
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
