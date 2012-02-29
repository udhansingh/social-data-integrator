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
import java.util.List;
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
	
	private void makeMetadata(String text){
		try{
			logger.info("Processing JSON: " + text);
			JSONObject jsonObject = new JSONObject(text);
			
			MetadataReader metadataReader = new JSONMetadataReader(jsonObject.toString());
			metadataReader.initialize();
			metadataReader.generateMetadata();
			Metadata metadata = metadataReader.getMetadata();
			
			String valuesString = metadata.values().toString().replace("[", "").replace("]", "");
			String hash = HashUtils.makeHash(valuesString, HashUtils.Context.FILE_SYSTEM);
			
			String matchingMetadataName = getMatchingMetadata(metadata);
			
			/*if(map.containsKey(hash) == false) {
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
			}*/
			if(matchingMetadataName == null) {
				map.put(hash, metadata);
			}
			else {
				Metadata masterMetadata = map.get(matchingMetadataName);
				
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

	@SuppressWarnings("unchecked")
	public void analyze() {
		String usagePolicy = "USE_READER";

		BufferedReader reader = null;
		List<String> list = null;
		
		if(object instanceof File){
			File file = (File)object;
			
			try {
				InputStream in = new FileInputStream(file);
				reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		else if(object instanceof String){
			String string = (String)object;
			
			try {
				reader = new BufferedReader(new StringReader(string));
			} finally {
			}
		}
		else if(object instanceof List){
			list = (List<String>)object;
			usagePolicy = "USE_LIST";
		}
		
		try {
			if(usagePolicy.compareTo("USE_READER") == 0){
				logger.info("Using Reader interfaces");

				if(reader != null){
					String text = null;

					while((text = reader.readLine()) != null){
						makeMetadata(text);
					}
				}
			}
			else if(usagePolicy.compareTo("USE_LIST") == 0){
				logger.info("Using List interfaces");
				
				if(list != null){
					for(String text : list){
						makeMetadata(text);
					}
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
	
	private float toleranceRatio = 0.1f;
	
	private String getMatchingMetadata(Metadata discoveredMetadata){
		int tolerance = (int) (discoveredMetadata.size()*toleranceRatio);
			for(String metadataName : map.keySet()){
				Metadata metadata = map.get(metadataName);
				if(Math.abs(discoveredMetadata.size()-metadata.size()) > tolerance){
					continue;
				}
				int diff = 0;
				for(String field : discoveredMetadata.keySet()){
					if(!metadata.containsKey(field))
						diff++;
					if(diff > tolerance)
						break;
				}
				if(diff <= tolerance)
					return metadataName;
			}
		return null;
	}
}
