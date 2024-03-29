package org.onesun.sdi.core.api;

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
import org.onesun.sdi.core.model.DataType;
import org.onesun.sdi.core.tools.XMLImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class DataTypeFactory {
	private static Logger logger = Logger.getLogger(DataTypeFactory.class);
	
	private static Map<String, DataType> dataTypes = new TreeMap<String, DataType>();
	
	public static List<DataType> init() {
		List<DataType> dataTypeList = new ArrayList<DataType>();
		DataType dataType = null;
		Class<?> clazz = null;
		
		dataType = new DataType();
		clazz = java.lang.String.class;
		dataType.setName(clazz.getSimpleName());
		dataType.setClazz(clazz);
		dataType.setSize(4096);
		dataTypeList.add(dataType);
		
		dataType = new DataType();
		clazz = java.lang.Integer.class;
		dataType.setName(clazz.getSimpleName());
		dataType.setClazz(clazz);
		dataType.setSize(16);
		dataTypeList.add(dataType);

		dataType = new DataType();
		clazz = java.lang.Double.class;
		dataType.setName(clazz.getSimpleName());
		dataType.setClazz(clazz);
		dataType.setSize(64);
		dataTypeList.add(dataType);
		
		dataType = new DataType();
		clazz = java.lang.Long.class;
		dataType.setName(clazz.getSimpleName());
		dataType.setClazz(clazz);
		dataType.setSize(32);
		dataTypeList.add(dataType);
		
		dataType = new DataType();
		clazz = java.lang.Boolean.class;
		dataType.setName(clazz.getSimpleName());
		dataType.setClazz(clazz);
		dataType.setSize(8);
		dataTypeList.add(dataType);

		dataType = new DataType();
		clazz = java.util.Date.class;
		dataType.setName(clazz.getSimpleName());
		dataType.setClazz(clazz);
		dataType.setSize(32);
		dataTypeList.add(dataType);
		
		dataType = new DataType();
		clazz = java.net.URL.class;
		dataType.setName(clazz.getSimpleName());
		dataType.setClazz(clazz);
		dataType.setSize(2048);
		dataTypeList.add(dataType);
		
		for(DataType dtItem : dataTypeList){
			dataTypes.put(dtItem.getName(), dtItem);
		}
		
		return dataTypeList;
	}
	
	public static void load(String pathToServicesFile){
		DataTypeImporterExporter importerExporter = new DataTypeImporterExporter();
		List<DataType> items = null;

		boolean status = importerExporter.load(pathToServicesFile);
		if(status == true){
			items = importerExporter.getItems();
			logger.info("Found " + items.size() + " provider entries");
		}else {
			items = init();
			importerExporter.setItems(items);
			importerExporter.save(pathToServicesFile);

			logger.info("Exported " + items.size() + " datatypes");
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
	
	public static List<DataType> getDataTypesList(){
		List<DataType> list = new ArrayList<DataType>();
		
		for(DataType t : dataTypes.values()){
			list.add(t);
		}
		
		return list;
	}
	
	public static DataType[] getDataTypesArray(){
		DataType[] list = new DataType[dataTypes.size()];
		
		int index = 0;
		for(DataType t : dataTypes.values()){
			list[index++] = t;
		}
		
		return list;
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
							item.setName(XMLUtils.getValue(element, "name"));
							
							String className = XMLUtils.getValue(element, "class");
							Class<?> clazz = Class.forName(className);
							item.setClazz(clazz);
							
							String sizeValue = XMLUtils.getValue(element, "size");
							int size = -1;
							try {
								size = Integer.parseInt(sizeValue);
							} catch(NumberFormatException e){
							}
							item.setSize(size);

							logger.info("Loading datatype ... " + item.getClazz());

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
					
					attribute = document.createElement("size");
					String size = "-1";
					
					try {
						size = Double.toString(dataType.getSize());
					} catch(NumberFormatException e){
					}
					
					attribute.setTextContent(size);
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

				return true;
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

	public static DataType getDataType(String name) {
		DataType t = dataTypes.get(name);
		
		return (DataType)t.clone();
	}
	
	public static Class<?> getDataTypeClass(String name) {
		return dataTypes.get(name).getClazz();
	}

	public static double getDataTypeSize(String name) {
		return dataTypes.get(name).getSize();
	}
}