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

import javax.xml.parsers.ParserConfigurationException;

import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.commons.xml.XMLUtils;
import org.onesun.sdi.core.api.Resource;
import org.scribe.model.Verb;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FileResource extends AbstractResource {
	private String fileName = null;

	public FileResource(String fileName) {
		this.fileName = fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getUrl() {
		return fileName;
	}
	
	@Override
	public Element toElement(Document document) throws ParserConfigurationException{
		Element parent = super.toElement(document);
		
		Element child =  document.createElement("fileName");
		child.setTextContent((fileName != null) ? fileName : "");
		parent.appendChild(child);

		return parent;
	}
	
	@Override
	public String getType(){
		return "FILE";
	}
	
	
	@Override
	public Resource toResource(Element element) throws ParserConfigurationException {
		String text = null;
		
		this.fileName = XMLUtils.getValue(element, "fileName");
		this.resourceName = XMLUtils.getValue(element, "resourceName");

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
}