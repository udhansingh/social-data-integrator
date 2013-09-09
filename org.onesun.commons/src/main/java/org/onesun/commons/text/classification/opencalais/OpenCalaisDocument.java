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
package org.onesun.commons.text.classification.opencalais;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OpenCalaisDocument {
	private final static String expression = "//RDF/Description/type[starts-with(@resource, 'http://s.opencalais.com/1/type/em/e/') or @resource='http://s.opencalais.com/1/type/cat/DocCat']";
	private final static String rdfNS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	private final static String cNS = "http://s.opencalais.com/1/pred/";

	public String trim(String rdf){
		Document d = null;
		try {
			NodeList nodes = doQuery(setRDFtoDOM(rdf));
			d = buildReducedRDF(nodes);
			return writeDOMtoString(d);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Document setRDFtoDOM(String rdfText) throws ParserConfigurationException, SAXException, IOException{

		DocumentBuilder parser;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		parser = factory.newDocumentBuilder();
		InputStream is = new ByteArrayInputStream(rdfText.getBytes("UTF-8"));

		return parser.parse(is);
	}  

	public NodeList doQuery(Document rdfDoc) throws XPathExpressionException{
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(expression);
		NodeList nodes = (NodeList) expr.evaluate(rdfDoc,XPathConstants.NODESET);
		return nodes;
	} 

	public Document buildReducedRDF(NodeList nodes) throws ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder loader = factory.newDocumentBuilder();
		Document reducedRDF = loader.newDocument();
		Element rootNode = reducedRDF.createElement("rdf:RDF");
		rootNode.setAttribute("xmlns:rdf", rdfNS);
		rootNode.setAttribute("xmlns:c", cNS);

		int count = nodes.getLength();
		for (int i = 0; i < count; i++){
			Element type = (Element) nodes.item(i);
			Element description = (Element)type.getParentNode();
			Element descriptionCopy = (Element)reducedRDF.importNode(description, true);
			rootNode.appendChild(descriptionCopy);
		}

		reducedRDF.appendChild(rootNode);

		return reducedRDF;
	}

	private String writeDOMtoString(Document document) throws TransformerException{
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		DOMSource source = new DOMSource(document);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);

		transformer.transform(source, result);
		return writer.getBuffer().toString();
	}
}