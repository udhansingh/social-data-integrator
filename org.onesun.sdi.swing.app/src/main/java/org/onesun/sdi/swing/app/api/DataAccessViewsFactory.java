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
package org.onesun.sdi.swing.app.api;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import org.onesun.sdi.core.api.Exporter;
import org.onesun.sdi.core.tools.XMLImporter;
import org.onesun.sdi.swing.app.AppMessages;
import org.onesun.sdi.swing.app.views.dataaccess.DataSiftDataAccessView;
import org.onesun.sdi.swing.app.views.dataaccess.FileDataAccessView;
import org.onesun.sdi.swing.app.views.dataaccess.GnipDataAccessView;
import org.onesun.sdi.swing.app.views.dataaccess.KapowDataAccessView;
import org.onesun.sdi.swing.app.views.dataaccess.RESTDataAccessView;
import org.onesun.sdi.swing.app.views.dataaccess.SocialMediaDataAccessView;
import org.onesun.sdi.swing.app.views.dataaccess.TwitterStreamingDataAccessView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class DataAccessViewsFactory {
	private static Logger logger = Logger.getLogger(DataAccessViewsFactory.class);

	public static final JPanel DEFAULT_DATA_ACCESS_VIEW = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

	private static Map<String, DataAccessView> viewsByCategory = new TreeMap<String, DataAccessView>();
	private static Map<String, DataAccessView> viewsByName = new TreeMap<String, DataAccessView>();

	static {
		final String text = "<html><body>" +
				"<h1>" + AppMessages.INFORMATION_CHOOSE_A_CONNECTION_OR_UNSUPPORTED_FEATURE + "</h1><br>" +
				"</body></html>";
		JLabel label = new JLabel(text);
		
		DEFAULT_DATA_ACCESS_VIEW.add(label, BorderLayout.CENTER);
	}

	private DataAccessViewsFactory(){}

	private static List<Item> init() {
		List<Item> items = new ArrayList<Item>();
		Item item = null;
		
		item = new Item();
		item.setName("Social Media");
		item.setCategory("SOCIAL_MEDIA");
		item.setClazz(SocialMediaDataAccessView.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setName("General");
		item.setCategory("GENERAL");
		item.setClazz(RESTDataAccessView.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setName("Kapow");
		item.setCategory("KAPOW");
		item.setClazz(KapowDataAccessView.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setName("File System");
		item.setCategory("FILE_SYSTEM");
		item.setClazz(FileDataAccessView.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setName("Twitter Streaming");
		item.setCategory("TWITTER_STREAMING");
		item.setClazz(TwitterStreamingDataAccessView.class.getCanonicalName());
		items.add(item);
		
		item = new Item();
		item.setName("DataSift");
		item.setCategory("DATASIFT");
		item.setClazz(DataSiftDataAccessView.class.getCanonicalName());
		items.add(item);

		item = new Item();
		item.setName("Gnip");
		item.setCategory("GNIP");
		item.setClazz(GnipDataAccessView.class.getCanonicalName());
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
				logger.info("Instantiating data access view ... " + item.getClazz());
				
				try {
					Class<?> instance = Class.forName(item.getClazz());
					if(instance != null){
						DataAccessView view = (DataAccessView) instance.newInstance();
						view.initialize();
						
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

	public static DataAccessView newDataAccessViewByName(String name){
		List<Item> items = importerExporter.getItems();
		
		if(items == null) return null;

		for(Item item : items){
			if(item.getName().compareTo(name) == 0){
				try {
					Class<?> instance = Class.forName(item.getClazz());
					if(instance != null){
						DataAccessView view = (DataAccessView) instance.newInstance();
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

	public static DataAccessView newDataAccessViewByCategory(String category){
		List<Item> items = importerExporter.getItems();
		
		if(items == null) return null;

		for(Item item : items){
			if(item.getCategory().compareTo(category) == 0){
				try {
					Class<?> instance = Class.forName(item.getClazz());
					if(instance != null){
						DataAccessView view = (DataAccessView) instance.newInstance();
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

	public static DataAccessView getViewByCategory(String category) {
		return viewsByCategory.get(category);
	}

	public static DataAccessView getViewByName(String name) {
		return viewsByName.get(name);
	}
}
