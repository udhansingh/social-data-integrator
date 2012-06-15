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

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.app.views.connection.properties.ConnotateConnectionPropertiesView;
import org.onesun.smc.app.views.connection.properties.DataSiftConnectionPropertiesView;
import org.onesun.smc.app.views.connection.properties.FacebookStreamingConnectionPropertiesView;
import org.onesun.smc.app.views.connection.properties.FileConnectionPropertiesView;
import org.onesun.smc.app.views.connection.properties.GnipConnectionPropertiesView;
import org.onesun.smc.app.views.connection.properties.KapowConnectionPropertiesView;
import org.onesun.smc.app.views.connection.properties.RESTConnectionPropertiesView;
import org.onesun.smc.app.views.connection.properties.SocialMediaConnectionPropertiesView;
import org.onesun.smc.app.views.connection.properties.TwitterStreamingConnectionPropertiesView;
import org.onesun.smc.core.tools.XMLImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class ConnectionPropertiesViewsFactory {
	private static Logger logger = Logger.getLogger(ConnectionPropertiesViewsFactory.class);

	public static final JPanel DEFAULT_CONNECTION_VIEW = new JPanel(new BorderLayout(5, 5));

	private static Map<String, ConnectionPropertiesView> viewsByCategory = new TreeMap<String, ConnectionPropertiesView>();
	private static Map<String, ConnectionPropertiesView> viewsByName = new TreeMap<String, ConnectionPropertiesView>();

	static {
		final String text = "<html><body>" +
				"<h1>What's next?</h1><br>" +
				"<h2>You may create connections that don't exist</h2> by choosing a provider to whom you want to connect<br><br>" +
				"<h2>You may choose a connection</h2> from instances already created<br>" +
				"</body></html>";
		JLabel label = new JLabel(text);
		// label.setFont(new Font("Courier New", Font.PLAIN, 32));
		DEFAULT_CONNECTION_VIEW.add(label, BorderLayout.CENTER);
	}

	private ConnectionPropertiesViewsFactory(){}

	private static List<Item> init() {
		List<Item> items = new ArrayList<Item>();
		Item item = null;
		
		item = new Item();
		item.setName("Social Media");
		item.setCategory("SOCIAL_MEDIA");
		item.setClazz(SocialMediaConnectionPropertiesView.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setName("General");
		item.setCategory("GENERAL");
		item.setClazz(RESTConnectionPropertiesView.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setName("Kapow");
		item.setCategory("KAPOW");
		item.setClazz(KapowConnectionPropertiesView.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setName("Connotate");
		item.setCategory("CONNOTATE");
		item.setClazz(ConnotateConnectionPropertiesView.class.getCanonicalName());
		items.add(item);
				
		item = new Item();
		item.setName("File System");
		item.setCategory("FILE_SYSTEM");
		item.setClazz(FileConnectionPropertiesView.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setName("Twitter Streaming");
		item.setCategory("TWITTER_STREAMING");
		item.setClazz(TwitterStreamingConnectionPropertiesView.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setName("DataSift");
		item.setCategory("DATASIFT");
		item.setClazz(DataSiftConnectionPropertiesView.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setName("Gnip");
		item.setCategory("GNIP");
		item.setClazz(GnipConnectionPropertiesView.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setName("Facebook Streaming");
		item.setCategory("FACEBOOK_STREAMING");
		item.setClazz(FacebookStreamingConnectionPropertiesView.class.getCanonicalName());
		items.add(item);
		
		return items;
	}

	private static class Item {
		private String category;
		private String clazz;
		private String name;

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

		public String getClazz() {
			return clazz;
		}
		public void setClazz(String clazz) {
			this.clazz = clazz;
		}
	}

	private static ItemImporterExporter importerExporter = new ItemImporterExporter();
	public static void load(String pathToServicesFile){
		boolean status = importerExporter.load(pathToServicesFile);
		List<Item> items = null;
		
		if(status == true){
			items = importerExporter.getItems();

			logger.info("Found " + items.size() + " connector view entries");
		}
		else {
			items = init();
			importerExporter.setItems(items);
			importerExporter.save(pathToServicesFile);

			logger.info("Initilized and exported " + items.size() + " connector class entries");
		}

		if(items != null){
			for(Item item : items){
				logger.info("Instantiating connector view ... " + item.getClazz());
				
				try {
					Class<?> instance = Class.forName(item.getClazz());
					if(instance != null){
						ConnectionPropertiesView view = (ConnectionPropertiesView) instance.newInstance();
						viewsByCategory.put(item.getCategory(), view);
						viewsByName.put(item.getName(), view);
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

	public static ConnectionPropertiesView newConnectorViewByName(String name){
		List<Item> items = importerExporter.getItems();
		
		if(items == null) return null;

		for(Item item : items){
			if(item.getName().compareTo(name) == 0){
				try {
					Class<?> instance = Class.forName(item.getClazz());
					if(instance != null){
						ConnectionPropertiesView view = (ConnectionPropertiesView) instance.newInstance();
						return view;
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

		return null;
	}

	public static ConnectionPropertiesView newConnectorViewByCategory(String category){
		List<Item> items = importerExporter.getItems();
		
		if(items == null) return null;

		for(Item item : items){
			if(item.getCategory().compareTo(category) == 0){
				try {
					Class<?> instance = Class.forName(item.getClazz());
					if(instance != null){
						ConnectionPropertiesView view = (ConnectionPropertiesView) instance.newInstance();
						return view;
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

		return null;
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
							item.setName(XMLUtils.getValue(element, "name"));
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

	public static ConnectionPropertiesView getViewByCategory(String category) {
		return viewsByCategory.get(category);
	}

	public static ConnectionPropertiesView getViewByName(String name) {
		return viewsByName.get(name);
	}
}
