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
import java.io.InputStream;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.inst2xsd.Inst2Xsd;
import org.apache.xmlbeans.impl.inst2xsd.Inst2XsdOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;

public class XSDGenerator {
	public XSDGenerator(){
		this(true);
	}
	
	public XSDGenerator(boolean useEnumerations){
		this.useEnumerations = useEnumerations;
	}
	
	private boolean useEnumerations = false;

	public SchemaDocument execute(File inputFile) throws XmlException, IOException {
		return execute(inputFile, XSDDesign.VENETIAN_BLIND);
	}

	public SchemaDocument execute(File inputFile, XSDDesign design) throws XmlException, IOException {
		// Only 1 instance is required for now
		XmlObject[] xmlInstances = new XmlObject[1];
		xmlInstances[0] = XmlObject.Factory.parse(inputFile);

		return inst2xsd(xmlInstances, design);
	}

	public SchemaDocument execute(InputStream is, XSDDesign design) throws XmlException, IOException {
		// Only 1 instance is required for now
		XmlObject[] xmlInstances = new XmlObject[1];
		xmlInstances[0] = XmlObject.Factory.parse(is);

		return inst2xsd(xmlInstances, design);
	}

	public SchemaDocument execute(String input) throws XmlException, IOException {
		return execute(input, XSDDesign.VENETIAN_BLIND);
	}

	public SchemaDocument execute(String input, XSDDesign design) throws XmlException, IOException {
		// Only 1 instance is required for now
		XmlObject[] xmlInstances = new XmlObject[1];
		xmlInstances[0] = XmlObject.Factory.parse(input);

		return inst2xsd(xmlInstances, design);
	}

	private SchemaDocument inst2xsd(XmlObject[] xmlInstances, XSDDesign design) throws IOException {
		Inst2XsdOptions inst2XsdOptions = new Inst2XsdOptions();
		if (design == null || design == XSDDesign.VENETIAN_BLIND) {
			inst2XsdOptions.setDesign(Inst2XsdOptions.DESIGN_VENETIAN_BLIND);
		} else if (design == XSDDesign.RUSSIAN_DOLL) {
			inst2XsdOptions.setDesign(Inst2XsdOptions.DESIGN_RUSSIAN_DOLL);
		} else if (design == XSDDesign.SALAMI_SLICE) {
			inst2XsdOptions.setDesign(Inst2XsdOptions.DESIGN_SALAMI_SLICE);
		}
		
		if(useEnumerations == true){
			inst2XsdOptions.setUseEnumerations(Inst2XsdOptions.ENUMERATION_NEVER);
		}

		SchemaDocument[] schemaDocuments = Inst2Xsd.inst2xsd(xmlInstances,
				inst2XsdOptions);
		if (schemaDocuments != null && schemaDocuments.length > 0) {
			return schemaDocuments[0];
		}

		return null;
	}
}
