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
package org.onesun.smc.core.resources;

import java.util.Map;

import org.scribe.model.Verb;


public class RESTResource extends AbstractResource implements Cloneable {
	private boolean accessTokenRequired = true;
	private Object additionalInformation = null;
	
	private String url = null;
	private String parameters = null;
	private Map<String, String> headers = null;
	private String payload = null;
	
	public RESTResource() {
		super();
	}

	public RESTResource(String resourceName, boolean accessTokenRequired, String url){
		this(resourceName, accessTokenRequired, Verb.GET, url);
	}

	public RESTResource(String resourceName, boolean accessTokenRequired, Verb verb, String url){
		this.resourceName = resourceName;
		this.verb = verb;
		this.url = url;
		this.accessTokenRequired = accessTokenRequired;
	}

	public RESTResource(String resourceName, boolean accessTokenRequired, Verb verb, String url, Map<String, Object> additionalInformation){
		this(resourceName, accessTokenRequired, verb, url);
		this.additionalInformation = additionalInformation;
	}
	
	public RESTResource(String resourceName, String url){
		this(resourceName, Verb.GET, url);
	}
	
	public RESTResource(String resourceName, String url, Object additionalInformation) {
		this(resourceName, true, Verb.GET, url);
		this.additionalInformation = additionalInformation;
	}

	public RESTResource(String resourceName, Verb verb, String url){
		this(resourceName, true, Verb.GET, url);
	}
	
	public Object getAdditionalInformation() {
		return additionalInformation;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getParameters() {
		return parameters;
	}

	public String getPayload() {
		return payload;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public boolean isAccessTokenRequired() {
		return accessTokenRequired;
	}

	public void setAccessTokenRequired(boolean accessTokenRequired) {
		this.accessTokenRequired = accessTokenRequired;
	}

	public void setAdditionalInformation(Object additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setVerb(Verb verb) {
		this.verb = verb;
	}
	
	public String toString(){
		return url;
	}
}
