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
package org.onesun.commons.xml;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

public class XMLValidator {
	private static Logger logger = Logger.getLogger(XMLValidator.class);
	
	public boolean validate(File dataFile, File schemaFile) {
		boolean status = false;

		try {
			// Only one schema to validate it against
			XmlObject[] schemas = { XmlObject.Factory.parse(schemaFile,
					new XmlOptions().setLoadLineNumbers()
							.setLoadMessageDigest()) };

			SchemaTypeLoader loader = XmlBeans.compileXsd(schemas, null,
					new XmlOptions().setErrorListener(null)
							.setCompileDownloadUrls().setCompileNoPvrRule());

			XmlObject object = loader
					.parse(dataFile,
							null,
							new XmlOptions()
									.setLoadLineNumbers(XmlOptions.LOAD_LINE_NUMBERS_END_ELEMENT));

			status = object.validate();

			logger.info("Validation Status: " + status);
		} catch (XmlException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return status;
	}
}
