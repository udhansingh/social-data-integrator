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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class XMLUtils {	public static String getAttributeValue(Element element, String tag){
		if(element != null) {			NamedNodeMap attributes = element.getAttributes();
			for(int index = 0; index < attributes.getLength(); index++){				Node an = attributes.item(index);
				if(an.getNodeName().compareTo(tag) == 0){					return an.getNodeValue();				}			}		}
		return null;	}
	public static String getValue(Element element, String tag){		String textValue = null;
		NodeList nodes = element.getElementsByTagName(tag);		if(nodes != null && nodes.getLength() > 0){			Element childElement = (Element)nodes.item(0);			Node node = childElement.getFirstChild();
			if(node != null) {				textValue = node.getNodeValue();			}		}		return textValue;	}
	public static List<String> getMultiValue(Element element, String tag){		List<String> values = null;
		NodeList nodes = element.getElementsByTagName(tag);		if(nodes != null && nodes.getLength() > 0){			values = new ArrayList<String>();
			for(int index = 0; index < nodes.getLength(); index++){				Element childElement = (Element)nodes.item(index);				Node node = childElement.getFirstChild();				if(node != null) {					values.add(node.getNodeValue());				}			}		}		return values;	}
	public static Element getElement(Element element, String tag){		NodeList nodes = element.getElementsByTagName(tag);		if(nodes != null && nodes.getLength() > 0){			Element childElement = (Element)nodes.item(0);			String nodeName = null; 			if(childElement != null) {				nodeName = childElement.getNodeName();			}			if(nodeName != null && nodeName.compareTo(tag) == 0){				return childElement;			}		}		return null;	}		public static List<Element> getElements(Element element, String tag){		List<Element> list = null;				NodeList nodes = element.getElementsByTagName(tag);				if(nodes != null && nodes.getLength() > 0){			list = new ArrayList<Element>();						for(int index = 0; index < nodes.getLength(); index++){				Element childElement = (Element)nodes.item(index);								String nodeName = null; 				if(childElement != null) {					nodeName = childElement.getNodeName();				}								if(nodeName != null && nodeName.compareTo(tag) == 0){					list.add(childElement);				}			}		}		return list;	}
	public static boolean exists(Element element, String tag) {		NodeList nodes = element.getElementsByTagName(tag);				if(nodes != null && nodes.getLength() > 0){			return true;		}		return false;	}
	
	public static Document toDocument(String input) throws SAXException, IOException, ParserConfigurationException{
		InputStream is = new ByteArrayInputStream(input.getBytes("UTF-8"));
		Document document = toDocument(is);		return document;
	}

	
	public static Document toDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);	// NOTE: never forget this
		
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(is);
		is.close();		return document;
	}

	public static Document newDocument() throws ParserConfigurationException{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);	// NOTE: never forget this

		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		
		return document;
	}}