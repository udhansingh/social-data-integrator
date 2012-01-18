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
package org.onesun.commons.text.format.detectors;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLFormatDetector extends AbstractFormatDetector {
	@Override
	protected void process(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		format = TextFormat.UNKNOWN;

		if(is != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				documentBuilderFactory.setNamespaceAware(true);	// NOTE: never forget this

				DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();

				Document document = builder.parse(is);
				
				if(document != null){
					System.out.println("Elements size # " + document.getChildNodes().getLength());
					format = TextFormat.XML;
				}
			} finally {
			}
		}
	}
}
