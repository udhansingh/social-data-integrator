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
package org.onesun.smc.core.tools;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.onesun.commons.xml.XMLUtils;
import org.w3c.dom.Document;

public abstract class XMLImporter extends AbstractImporter {
	private static Logger logger = Logger.getLogger(XMLImporter.class);

	@Override
	final public void init() {
		try {
			InputStream is = new FileInputStream(resource);
			if(is != null) {
				Document document = XMLUtils.toDocument(is);
				
				if(document != null) { 
					parse(document);
				}
			}

		} catch (Exception e) {
			logger.error("Exception while processing XML document " + resource.getName() + "\t" + e.getMessage());
		}
	}
}