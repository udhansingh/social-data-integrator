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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.onesun.smc.core.model.RequestParamObject;

public class FilterMetadata {
	// Key: Internal Name
	// Value: Filter Object
	private Map<String, RequestParamObject> headers = Collections.synchronizedMap(new TreeMap<String, RequestParamObject>());
	private Map<String, RequestParamObject> params = Collections.synchronizedMap(new TreeMap<String, RequestParamObject>());
	private RequestParamObject payload = null;
	
	public void setParams(Map<String, RequestParamObject> params){
		this.params = params;
	}
	
	public void setHeaders(Map<String, RequestParamObject> headers){
		this.headers = headers;
	}
	
	public boolean containsHeaderKey(String key) {
		return headers.containsKey(key);
	}

	public RequestParamObject getHeaderObject(String key) {
		return headers.get(key);
	}
	

	public Set<String> headersKeySet() {
		return headers.keySet();
	}

	public void putHeaderObject(String key, RequestParamObject value) {
		headers.put(key, value);
	}

	
	public Collection<RequestParamObject> headerValues() {
		return headers.values();
	}
//	public Properties toProperties(){
//		Properties p = new Properties();
//		
//		for(String k : map.keySet()){
//			p.put(k, map.get(k));
//		}
//		
//		return p;
//	}

//	public String toString(){
//		return toProperties().toString();
//	}
	
	public boolean containsParamKey(String key) {
		return params.containsKey(key);
	}

	public RequestParamObject getParamObject(String key) {
		return params.get(key);
	}
	

	public Set<String> paramsKeySet() {
		return params.keySet();
	}

	public void putParamObject(String key, RequestParamObject value) {
		params.put(key, value);
	}

	
	public Collection<RequestParamObject> paramValues() {
		return params.values();
	}

	public RequestParamObject getPayload() {
		return payload;
	}

	public void setPayload(RequestParamObject payload) {
		this.payload = payload;
	}
}
