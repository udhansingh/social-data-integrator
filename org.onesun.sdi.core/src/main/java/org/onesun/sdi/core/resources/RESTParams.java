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
package org.onesun.sdi.core.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class RESTParams {
	private String input = null;
	private Map<String, String> params = new HashMap<String, String>();
	
	public RESTParams(String url){
		this.input = url;
		
		init();
	}

	public String getParam(String key){
		return params.get(key);
	}
	
	private void init(){
		try {
			parse();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private void parse() throws UnsupportedEncodingException{
		if(input == null) return;
		
	    int index = input.indexOf("?", 0);
	    
	    String paramsText = input;
	    try {
	    	if (index > -1) {
	    		paramsText = input.substring(index + 1);
	    	}

	    	String paramsArray[] = paramsText.split("&");
	    	
	    	for (String param : paramsArray) {
	    		String temp[] = param.split("=");
	    		params.put(temp[0], URLDecoder.decode(temp[1], "UTF-8"));
	    	}
	    }catch(ArrayIndexOutOfBoundsException e){
	    }
	}
}
