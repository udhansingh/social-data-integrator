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

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.commons.text.format.detectors.TextFormatDetector;
import org.onesun.smc.api.Resource;
import org.scribe.model.Verb;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class AbstractResource implements Resource, Cloneable {
	protected String resourceName = null;
	protected Object object;
	protected TextFormat textFormat = TextFormat.UNKNOWN;
	protected Verb verb = Verb.GET;

	@Override 
	public Verb getVerb(){
		return verb;
	}
	
	@Override
	public String getResourceName(){
		return resourceName;
	}
	@Override
	public void setResourceName(String resourceName){
		this.resourceName = resourceName;
	}
	
	@Override
	public abstract String getUrl();
	
	@Override
	public void setObject(Object object){
		this.object = object;
	}
	
	@Override
	public Object getObject(){
		return object;
	}
	
	@Override
	public void checkFormat() {
		TextFormatDetector textFormatDetector = new TextFormatDetector();
		textFormatDetector.checkFormat(object);
		textFormat = textFormatDetector.getFormat();
	}

	@Override
	public TextFormat getTextFormat(){
		return textFormat;
	}

	@Override
	public String getFormattedText(){
		String responseText = null;

		if(object == null) return responseText;

		switch(textFormat){
		case XML:
			if(object instanceof String){
				responseText = (String)object;
			}
			break;

		case JSON: 
		{
			if(object instanceof String){
				String text = (String)object;

				int failCount = 0;
				boolean parsingFailed = false;
				try {
					JSONObject jsonObject = new JSONObject(text);
					responseText = jsonObject.toString(4);
				} catch (JSONException e) {
					parsingFailed = true;
					failCount++;
				}

				// JSONObject failed, try JSONArray now!
				if(parsingFailed == true){
					parsingFailed = false;

					try {
						JSONArray jsonArray = new JSONArray(text);
						responseText = jsonArray.toString(4);
					} catch (JSONException e) {
						parsingFailed = true;
						failCount++;
					}
				}

				// If nothing works
				if(parsingFailed == true && failCount > 1){
					responseText = text;
				}
			}
		}
		break;
		}

		return responseText;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch(CloneNotSupportedException e) {
			return this;
		}
	}
	
	public void setTextFormat(TextFormat textFormat) {
		this.textFormat = textFormat;
	}
	
	@Override
	public Element toElement(Document document) throws ParserConfigurationException{
		Element parent = document.createElement("resource");
		
		Element child =  document.createElement("verb");
		child.setTextContent((verb != null) ? verb.name() : "");
		parent.appendChild(child);

		child = document.createElement("resourceName");
		child.setTextContent((resourceName != null) ? resourceName : "");
		parent.appendChild(child);
		
		child =  document.createElement("type");
		child.setTextContent(getType());
		parent.appendChild(child);

		return parent;
	}
}
