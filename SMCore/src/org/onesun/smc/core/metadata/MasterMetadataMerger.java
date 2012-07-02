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

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.core.model.MetaObject;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
		
		Document document = null;
		try {
			document = XMLUtils.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		finally {
		}
		
		if(document == null) return;
		
		Comment comment = document.createComment(commentText);
		document.appendChild(comment);

		// Create to master metadata
		File file = new File(fileName);
		if(file.exists()){
			// Merge and write master metadata
			try{
				MetadataImporter mi = new MetadataImporter();
				mi.load(fileName);
				
				Metadata md = mi.getMetadata();
				
				// merge master into cache
				for(MetaObject item : md.values()){
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
			Element element = metadata.toElement(document);
			document.appendChild(element);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		try {
			// Write master metadata
			OutputFormat format = new OutputFormat(document);
			format.setIndenting(true);

			FileOutputStream fos = new FileOutputStream(file);
			XMLSerializer serializer = new XMLSerializer(fos, format);
			logger.info("Serializing completed for: " + file.getAbsolutePath());
			serializer.serialize(document);
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		finally{
		}
	}
}
