/*
	Copyright 2011 Udayakumar Dhansingh (Udy)
	   			   Vikas Kumar Jha

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

package org.onesun.smc.core.metadata;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.onesun.smc.core.exceptions.InvalidDataException;
import org.onesun.smc.core.model.MetaObject;
import org.xml.sax.InputSource;

public class XMLMetadataReader extends AbstractMetadataReader {
	public static final String NAME_SEPERATOR = "_";
	public static final String PATH_SEPERATOR = "/";

	private Metadata metadata = new Metadata();

	public Metadata getMetadata() {
		return metadata;
	}
	
	private Document document = null;

	public XMLMetadataReader(File file) {
		try {
			document = new SAXBuilder().build(file);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public XMLMetadataReader(String xmlString) {
		try {
			if(xmlString != null){
				document = new SAXBuilder().build(new InputSource(new StringReader(xmlString)));
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PathTree pathtree = new PathTree();

	
	public void setStopAtMultivalued(boolean enable) {
		pathtree.stopAtMultivalued = enable;
	}

	List<String> PathList = new ArrayList<String>();

	private boolean isMultiValued(Element node) {
		List<?> children = node.getChildren();
		List<String> childrennames = new ArrayList<String>();
		for (Object child : children) {
			Element childElement = (Element) child;
			if (!childrennames.contains(childElement.getName()))
				childrennames.add(childElement.getName());
		}
		if (childrennames.size() == 1 && children.size() != 1)
			return true;
		else
			return false;
	}

	@Override
	public void initialize() throws Exception {
		if(document == null) return;
		
		Element rootElement = document.getRootElement();
		List<Element> elements = null;
		
		if (beginPath != null && beginPath.length() > 1) {
			String[] dataPath = beginPath.split("/");
			if (dataPath.length > 1) {
				for (int i = 1; i < dataPath.length - 1; i++) {
					rootElement = rootElement.getChild(dataPath[i]);
					if (rootElement == null)
						throw new InvalidDataException(
								"Path is invalid for xml file.");
				}
				pathtree.setDataDepth(dataPath.length - 1);
				elements = new ArrayList<Element>();
				for(Object object : rootElement.getChildren(dataPath[dataPath.length - 1], rootElement.getNamespace())){
					elements.add((Element)object);
				}
				pathtree.TEST_DEPTH = 1;
			}
			else {
				elements = new ArrayList<Element>();
				elements.add(rootElement);
				if(isMultiValued(rootElement))
					pathtree.TEST_DEPTH = 2;
			}
		}
		else {
			elements = new ArrayList<Element>();
			elements.add(rootElement);
			if(isMultiValued(rootElement))
				pathtree.TEST_DEPTH = 2;
		}
			
			
		for(Element element : elements){
			initialize(element, null);
		}
	}
	
	private String getNSName(Element node){
		String ns = node.getNamespacePrefix();
		if (ns != null && ns.length() > 0){
			return ns + ":"+ node.getName();
		}
		else
			return node.getName();
	}
	
	
	private void initialize(Element node, String path) {
		if (node == null)
			return;
		boolean multivalued = false;
		
		List<?> children = node.getChildren();
		Element parent = node.getParentElement();
		if (parent != null && (parent.getChildren(node.getName(), node.getNamespace())
						.size() > 1))
			multivalued = true;
		if (path == null)
			path = getNSName(node);
		else
			path = path + "/" + getNSName(node);

		pathtree.addpath(path, multivalued);

		List<?> Attributes = node.getAttributes();
		for (Object attribute : Attributes) {
			String AttrPath = path + "/@" + ((Attribute) attribute).getName();
			pathtree.addpath(AttrPath, false);
		}

		for (Object child : children) {
			initialize((Element) child, path);
		}
	}

	@Override
	public void generateMetadata() throws Exception {
		pathtree.generate();
		for (String path : pathtree.xpaths) {
			MetaObject mo = new MetaObject();
			mo.setPath(path);
			metadata.put(path, mo);
		}

	}

	@Override
	public void setLeafNodesOnly(boolean leafNodesOnly) {
		this.leafNodesOnly = leafNodesOnly;
		pathtree.leafNodesOnly = this.leafNodesOnly;
	}
}