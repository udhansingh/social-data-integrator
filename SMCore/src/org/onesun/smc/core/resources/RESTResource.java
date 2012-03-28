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

import javax.xml.parsers.ParserConfigurationException;

import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.smc.api.Resource;
import org.scribe.model.Verb;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


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
	
	@Override
	public Element toElement(Document document) throws ParserConfigurationException{
		Element parent = super.toElement(document);
		
		Element child =  document.createElement("accessTokenRequired");
		child.setTextContent(Boolean.toString(accessTokenRequired));
		parent.appendChild(child);
		
		child =  document.createElement("url");
		child.setTextContent((url != null) ? url : "");
		parent.appendChild(child);

		child =  document.createElement("parameters");
		child.setTextContent((parameters != null) ? parameters : "");
		parent.appendChild(child);
		
		child =  document.createElement("payload");
		child.setTextContent((payload != null) ? payload : "");
		parent.appendChild(child);
		
		child =  document.createElement("textFormat");
		child.setTextContent((textFormat != null) ? textFormat.name() : "UNKNOWN");
		parent.appendChild(child);
		
		if(headers != null && headers.size() > 0){
			child = document.createElement("headers");
			
			for(String key : headers.keySet()){
				Element header =  document.createElement("header");

				Element element = document.createElement("name");
				element.setTextContent(key);
				header.appendChild(element);

				element = document.createElement("value");
				String value = headers.get(key);
				element.setTextContent((value != null) ? value : "");
				header.appendChild(element);
				
				child.appendChild(header);
			}
			
			parent.appendChild(child);
		}
		
		return parent;
	}
	
	@Override
	public String getType(){
		return "REST";
	}
	
	@Override
	public Resource toResource(Element element) throws ParserConfigurationException {
		String text = null;
		
		this.url = XMLUtils.getValue(element, "url");
		this.resourceName = XMLUtils.getValue(element, "resourceName");
		this.parameters = XMLUtils.getValue(element, "parameters");
		this.payload = XMLUtils.getValue(element, "payload");

		text = XMLUtils.getValue(element, "accessTokenRequired");
		if(text != null && text.trim().length() > 0){
			this.accessTokenRequired = Boolean.valueOf(text);
		}
		
		text = XMLUtils.getValue(element, "verb");
		if(text != null && text.trim().length() > 0){
			this.verb = Verb.valueOf(text);
		}
		
		text = XMLUtils.getValue(element, "textFormat");
		if(text != null && text.trim().length() > 0){
			this.textFormat = TextFormat.valueOf(text);
		}
		
		return this;
	}

	public TextFormat getTextFormat() {
		return textFormat;
	}

	public void setTextFormat(TextFormat textFormat) {
		this.textFormat = textFormat;
	}
}
