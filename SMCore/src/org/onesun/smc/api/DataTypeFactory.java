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
import org.onesun.smc.core.model.DataType;
import org.onesun.smc.core.tools.XMLImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class DataTypeFactory {
	private static Logger logger = Logger.getLogger(DataTypeFactory.class);
	
	private static Map<String, DataType> dataTypes = new TreeMap<String, DataType>();
	
	private static List<DataType> initConnectors() {
		List<DataType> items = new ArrayList<DataType>();
		DataType item = null;

		item = new DataType();
		item.setName("String");
		item.setClazz(java.lang.String.class);
		items.add(item);
		
		item = new DataType();
		item.setName("Integer");
		item.setClazz(java.lang.Integer.class);
		items.add(item);

		item = new DataType();
		item.setName("Double");
		item.setClazz(java.lang.Double.class);
		items.add(item);
		
		item = new DataType();
		item.setName("Boolean");
		item.setClazz(java.lang.Boolean.class);
		items.add(item);
		
		item = new DataType();
		item.setName("Long");
		item.setClazz(java.lang.Long.class);
		items.add(item);

		item = new DataType();
		item.setName("Date");
		item.setClazz(java.util.Date.class);
		items.add(item);
		
		return items;
	}
	
	public static void load(String pathToServicesFile){
		DataTypeImporterExporter importerExporter = new DataTypeImporterExporter();
		List<DataType> items = null;

		boolean status = importerExporter.load(pathToServicesFile);
		if(status == true){
			items = importerExporter.getItems();
			logger.info("Found " + items.size() + " provider entries");
		}else {
			items = initConnectors();
			importerExporter.setItems(items);
			importerExporter.save(pathToServicesFile);

			logger.info("Initilized and exported " + items.size() + " provider class entries");
		}

		if(items != null){
			for(DataType item : items){
				dataTypes.put(item.getName(), item);
			}
		}
	}
	
	public static Map<String, DataType> getDataTypes(){
		return dataTypes;
	}
	
	private static class DataTypeImporterExporter extends XMLImporter implements Exporter {
		public DataTypeImporterExporter() {
			super();
		}

		private List<DataType> items = new ArrayList<DataType>();

		public List<DataType> getItems(){
			return items;
		}
		
		public void setItems(List<DataType> items) {
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

							DataType item = new DataType();
							item.setName(XMLUtils.getValue(element, "identity"));
							
							String className = XMLUtils.getValue(element, "class");
							Class<?> clazz = Class.forName(className);
							item.setClazz(clazz);

							logger.info("Instantiating Service Provider ... " + item.getClazz());

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

				for(DataType dataType : items){
					Element attribute = null;
					Element itemElement = document.createElement("item");

					attribute = document.createElement("name");
					attribute.setTextContent(dataType.getName());
					itemElement.appendChild(attribute);

					attribute = document.createElement("class");
					attribute.setTextContent(dataType.getClazz().getCanonicalName());
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
