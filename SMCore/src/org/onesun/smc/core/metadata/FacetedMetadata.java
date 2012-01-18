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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.onesun.commons.HashUtils;
import org.onesun.smc.api.MetadataReader;


public class FacetedMetadata {
	private static Logger logger = Logger.getLogger(FacetedMetadata.class);
	
	private Object object;
	private Map<String, Metadata> map = new HashMap<String, Metadata>();
	
	public Set<String> getFacets(){
		return map.keySet();
	}
	
	public Metadata getMetadata(String facet){
		return map.get(facet);
	}
	
	public static void main(String[] args){
		FacetedMetadata fm = new FacetedMetadata();
		
		String path = "/home/innovator/Desktop/FB_Streaming.txt";
		
		fm.setObject(new File(path));
		
		fm.analyze();
	}
	
	private void makeMetadata(String text){
		try{
			JSONObject jsonObject = new JSONObject(text);
			
			MetadataReader metadataReader = new JSONMetadataReader(jsonObject.toString());
			metadataReader.initialize();
			metadataReader.generateMetadata();
			Metadata metadata = metadataReader.getMetadata();
			
			String valuesString = metadata.values().toString().replace("[", "").replace("]", "");
			String hash = HashUtils.makeHash(valuesString, HashUtils.Context.FILE_SYSTEM);
			
			if(map.containsKey(hash) == false) {
				map.put(hash, metadata);
			}
			else {
				Metadata masterMetadata = map.get(hash);
				
				// Merge to prepare the master
				if(masterMetadata != null){
					for(String k : metadata.keySet()){
						masterMetadata.put(k, metadata.get(k));
					}
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
		}
	}

	public void analyze() {
		File file = null;
		String string = null;
		
		if(object instanceof File){
			file = (File)object;
		}
		else if(object instanceof String){
			string = (String)object;
		}
		
		BufferedReader reader = null;
		if(file != null){
			try {
				InputStream in = new FileInputStream(file);
				reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		else if(string != null){
			try {
				reader = new BufferedReader(new StringReader(string));
			} finally {
			}
		}
		
		try {
			if(reader != null){
				String text = null;
				
				while((text = reader.readLine()) != null){
					makeMetadata(text);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String k : map.keySet()){
			logger.debug("Metadata for " + k);
			logger.debug("------------------------------------------------------");
			Metadata m = map.get(k);
			for(String mk : m.keySet()){
				logger.debug(mk + "=" + m.get(mk));
			}
			logger.debug("------------------------------------------------------");
		}
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
