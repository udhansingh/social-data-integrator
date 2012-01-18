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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.onesun.commons.StreamUtils;
import org.onesun.commons.text.format.detectors.TextFormat;

public class SchemaGenerator {
	public SchemaGenerator(Object document, TextFormat documentFormat) {
		this.document = document;
		this.documentFormat = documentFormat;
	}

	public enum StartAt {ROOT, FIRST_CHILD};
	
	private SchemaDocument schemaDocument = null;
	
	public SchemaDocument getSchemaDocument(){
		return schemaDocument;
	}
	
	private TextFormat documentFormat = TextFormat.UNKNOWN;
	private Object document = null;
	
	private XSDDesign schemaDesign = XSDDesign.VENETIAN_BLIND;
	
	public void displaySchema(){
		// Listing fields
		System.out.println("---------------------------------------------------------");
		System.out.println("Schema");
		System.out.println("---------------------------------------------------------");
		
		System.out.println(getSchemaPrettyPrint());
		
		System.out.println("---------------------------------------------------------");
	}
	
	public void generateSchema() throws Exception {
		if(document == null) return;

		if(documentFormat == TextFormat.JSON) {
			json2Xml();
		}
		
		try {
			XSDGenerator xmlBeans = new XSDGenerator();
			
			if(document instanceof String){
				schemaDocument = xmlBeans.execute((String) document, schemaDesign);
			}
			else if(document instanceof File){
				schemaDocument = xmlBeans.execute((File) document, schemaDesign);
			}
			else if(document instanceof InputStream){
				schemaDocument = xmlBeans.execute((InputStream) document, schemaDesign);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public TextFormat getDataFormat() {
		return documentFormat;
	}

	public void setDocumentFormat(TextFormat documentFormat) {
		this.documentFormat = documentFormat;
	}
	
	public void setDocument(Object input) {
		this.document = input;
	}

	private void json2Xml() {
		if(documentFormat == TextFormat.JSON){
			try {
				// TODO: make jsonToXml handle any input object
				String xmlText = null;
				XMLObjTree xmlObjTree = new XMLObjTree();
				
				if(document instanceof String) {
					xmlText = xmlObjTree.jsonToXml((String) document);
				}
				else if(document instanceof File) {
					xmlText = xmlObjTree.jsonToXml((File) document);
				}
				if(document instanceof InputStream) {
					xmlText = xmlObjTree.jsonToXml((InputStream) document);
				}

				document = xmlText;
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} 
			finally {
			}
		}
	}

	public String getSchemaPrettyPrint(){
		try {
			
			StringWriter writer = new StringWriter();
			schemaDocument.save(writer, new XmlOptions().setSavePrettyPrint());
			writer.close();
			
			return writer.toString();
		} catch (IOException e) {
		}
    	
    	return null;
	}
	
	public void saveSchema(File outFile) throws IOException {
		schemaDocument.save(outFile, new XmlOptions().setSavePrettyPrint());
	}

	public void setSchemaDesign(XSDDesign schemaDesign) {
		this.schemaDesign  = schemaDesign;
	}

	public void saveDocument(File xmlFile) throws IOException {
		if(document instanceof String){
			StreamUtils.write((String)document, xmlFile); 
		}
	}
}