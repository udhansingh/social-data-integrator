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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

import org.apache.log4j.Logger;

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
	
	public void merge(Metadata m){
		final String comment = providerName + " --> " + resourceName;
		
		Properties p = m.toProperties();
		// Create to master metadata
		File file = new File(fileName);
		if(! file.exists() ){
			try {
				// Write master metadata
				Writer writer = new FileWriter(file);
				p.store(writer, comment);
				
				// Close writer
				writer.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			finally{
			}
		}
		else {
			// Merge and write master metadata
			try{
				Reader reader = new FileReader(file);
				Properties pt = new Properties();
				pt.load(reader);
				
				// merge master into cache
				for(Object o : p.keySet()){
					String ostr = (String)o;
					String value = (String)p.get(ostr);
					
					if(! pt.containsKey(ostr) ){
						pt.put(ostr, value);
					}
					else {
						String oldValue = pt.getProperty(ostr);
						
						if(oldValue.compareTo(value) != 0){
							pt.put(ostr, value);
						}
					}
				}
				
				// Update cached Model
				for(Object o : pt.keySet()){
					String ostr = (String)o;
					
					if(! ostr.contains("social.media.internal.")){
						m.put(ostr, ostr);
					}
				}
				
				// compact cached model
				m.compact();
				// make properties from compacted metadata
				pt = m.toProperties();
				
				// Close Reader
				reader.close();
				
				// don't write if they are same
				logger.info("Master metadata changed for " + fileName);

				// Write master metadata
				Writer writer = new FileWriter(file);
				pt.store(writer, comment);

				// Close writer
				writer.close();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
			}
		}
	}
}
