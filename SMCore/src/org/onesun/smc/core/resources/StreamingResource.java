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

public class StreamingResource extends AbstractResource {
	private String url = null;
	private String parameters = null;
	private Map<String, String> headers = null;
	private String payload = null;
	
	public StreamingResource(String resourceName, Verb verb, String url) {
		this(verb, url);

		this.resourceName = resourceName;
	}

	public StreamingResource(Verb verb, String url) {
		this.verb = verb;
		this.url = url;
	}

	public StreamingResource(String url) {
		this(Verb.GET, url);
	}

	public StreamingResource() {
		super();
	}

	public Verb getVerb() {
		return verb;
	}

	public void setVerb(Verb verb) {
		this.verb = verb;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String toString(){
		return url;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
}
