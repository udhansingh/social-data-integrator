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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.api.DataTypeFactory;
import org.onesun.smc.core.model.MetaObject;
import org.onesun.smc.core.tools.XMLImporter;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class MasterMetadataMerger {
	private static Logger logger = Logger.getLogger(MasterMetadataMerger.class);
	
	private String providerName;
	private String resourceName;
	private String fileName;
	
	public MasterMetadataMerger(String providerName, String folderName, String resourceName, String path){
		this.providerName = providerName;
		this.resourceName = resourceName;
		
		String dirToCreate = path + "/" + providerName + "/";
		if(folderName != null){
			dirToCreate += folderName + "/";
		}
		
		this.fileName = dirToCreate + resourceName + ".properties";
		
		File dir = new File(dirToCreate);
		if(!dir.exists()){
			dir.mkdirs();
		}
	}
	
	public void merge(Metadata metadata){
		final String commentText = providerName + " --> " + resourceName;
		
		Document document = metadata.toDocument();
		Comment comment = document.createComment(commentText);
		document.appendChild(comment);
		
		// Create to master metadata
		File file = new File(fileName);
		if(file.exists()){
			// Merge and write master metadata
			try{
				MetadataImporter mi = new MetadataImporter();
				mi.load(fileName);
				
				List<MetaObject> items = mi.getItems();
				
				// merge master into cache
				for(MetaObject item : items){
					String key = item.getPath();
					
					if(! metadata.containsKey(key) ){
						metadata.put(key, item);
					}
					else {
						// Assume in-memory version is latest;
					}
				}
				
			}finally {
			}
		}
		
		// compact cached model
		metadata.compact();
		
		try {
			// Write master metadata
			OutputFormat format = new OutputFormat(document);
			format.setIndenting(true);

			XMLSerializer serializer = new XMLSerializer(new FileOutputStream(file), format);
			logger.info("Serializing completed for: " + file.getAbsolutePath());
			serializer.serialize(document);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		finally{
		}
	}
	
	private static class MetadataImporter extends XMLImporter {
		public MetadataImporter() {
			super();
		}

		private List<MetaObject> items = new ArrayList<MetaObject>();

		public List<MetaObject> getItems(){
			return items;
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
							MetaObject item = new MetaObject();
							item.setName(XMLUtils.getValue(element, "name"));
							item.setPath(XMLUtils.getValue(element, "path"));
							item.setType(
								DataTypeFactory.getDataType(
										XMLUtils.getValue(element, "type")
								)
							);

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
	}
}
