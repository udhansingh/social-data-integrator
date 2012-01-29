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
package org.onesun.smc.core.data.reader;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.onesun.smc.core.exceptions.InvalidDataException;
import org.xml.sax.InputSource;

public class XMLDataReader extends AbstractDataReader {
	private static Logger logger = Logger.getLogger(XMLDataReader.class);
	
	private Element rootnode = null;
	private List<Element> nodeList = null;
	private boolean isOutput = false;
	private boolean isBlob = true;
	private boolean putWrapper = true;
	boolean multivalued = false;

	public XMLDataReader(File file) {
		try {
			rootnode = new SAXBuilder().build(file).getRootElement();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public XMLDataReader(String xmlString) {
		try {
			if(xmlString != null){
				rootnode = new SAXBuilder().build(new InputSource(new StringReader(xmlString))).getRootElement();
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addToMap(final Map<String, String> rowMap, final String key,
			final String value) {
		if (!rowMap.containsKey(key))
			rowMap.put(key, value);
	}

	private String getBlob(Element node) {
		String nodeName = node.getName();
		Element parent = node.getParentElement();
		List<?> nodes = parent.getChildren(nodeName);
		String text = "";
		if (isBlob) {
			if (/*nodes.size() > 1 &&*/ putWrapper) {
				text = "<" + parent.getName() + ">"
						+ new XMLOutputter().outputString(nodes) + "</"
						+ parent.getName() + ">";
			} else {
				text = new XMLOutputter().outputString(nodes);
			}

		} else
			text = node.getValue();
		return text;
	}
	
	private Element getNSChild(Element node, String nsName){
		int index = nsName.indexOf(":");
		if(index > -1){
			return node.getChild(nsName.substring(index+1), node.getNamespace(nsName.substring(0, index)));
		}
		else{
			return node.getChild(nsName);
		}
			
	}

	@Override
	public void initialize() {
		String nodeName = metadata.getNodeName();
		Element node = rootnode;

		if (nodeName != null && nodeName.length() > 0) {
			String[] dataPath = nodeName.split("/");
			if(dataPath.length > 1){
				for (int i = 1; i < dataPath.length-1; i++) {
					node = getNSChild(node,dataPath[i]);
				}
				nodeList = new ArrayList<Element>();
				for(Object object : node.getChildren(dataPath[dataPath.length - 1], node.getNamespace())){
					nodeList.add((Element)object);
				}
			}
			else{
				nodeList = new ArrayList<Element>();
				if(isMultiValued(node)){
					multivalued = true;
					nodeList = new ArrayList<Element>();
					for(Object object : node.getChildren()){
						nodeList.add((Element)object);
					}
				}
				else	
					nodeList.add(node);
			}
		}
		else{
			nodeList = new ArrayList<Element>();
			if(isMultiValued(node)){
				multivalued = true;
				nodeList = new ArrayList<Element>();
				for(Object object : node.getChildren()){
					nodeList.add((Element)object);
				}
			}
			else	
				nodeList.add(node);
		}
	}

	private boolean isMultiValued(Element node) {
		if(node == null) return false;
		
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
	public void load() {
		List<String> xPaths = new ArrayList<String>();
		for (String value : metadata.keySet()) {
			xPaths.add(value);
		}

		data.clear();

		try {
			data = processData(xPaths);
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
	}

	public List<Map<String, String>> processData(List<String> xPaths)
			throws InvalidDataException {

		if (xPaths == null)
			return null;

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		for (Element node : nodeList) {
			Map<String, String> rowMap = Collections.synchronizedMap(new TreeMap<String, String>());
			for (String path : xPaths) {
				int start;
				/*Handle multivalued parent attributes*/
				if(multivalued){
					String[] dataPath = path.split("/"); 
					if(dataPath.length == 2 && (start = path.indexOf("@")) > -1){
						Element parent = nodeList.get(0).getParentElement();
						Attribute attribute = parent.getAttribute(
								path.substring(start + 1),
								parent.getNamespace());
						if (attribute != null) {
							addToMap(rowMap, path, attribute.getValue());
						}
					}
				}
				/*Normal case handling */
				if (isOutput)
					logger.info(path);
				start = path.indexOf("/");
				if (start > -1) {
					if (path.substring(start + 1).charAt(0) == '@') {
						Attribute attribute = node.getAttribute(
								path.substring(start + 2),
								node.getNamespace());
						if (attribute != null) {
							addToMap(rowMap, path, attribute.getValue());
							if (isOutput)
								logger.info("\t " + attribute.getValue());
						}
					}
					else{
						if(multivalued)
							processData(path.substring(start+1), node, 1, rowMap, path);
						else
							processData(path, node, 1,	rowMap, path);
					}


				}
				else {
					String text = node.getTextNormalize();
					if (text.length() > 0)
						addToMap(rowMap, path, text);
					else
						addToMap(rowMap, path, "");
				}

			}
			data.add(rowMap);
		}
		return data;
	}

	private void processData(String path, Element node, int depth,
			Map<String, String> rowMap, String fullpath) {
		int start = path.indexOf("/");
		if (start > -1) {
			path = path.substring(start + 1);
			int end = path.indexOf("/");
			String pathchildname;
			if (end > -1)
				pathchildname = path.substring(0, end);
			else
				pathchildname = path;
			if (pathchildname.charAt(0) == '@') {
				Attribute attribute = node.getAttribute(path.substring(1));
				if (attribute != null) {
					addToMap(rowMap, fullpath, attribute.getValue());
					if (isOutput)
						logger.info("\t " + attribute.getValue());
				}
			} else {
				Element child = getNSChild(node, pathchildname.replace("*", ""));
				if (child == null) {
					addToMap(rowMap, fullpath, "");
					if (isOutput)
						logger.info("\t [Missing]");
				} else
					processData(path, (Element) child, depth + 1, rowMap, fullpath);
			}

		} else if (path.replace("*", "").equals(node.getName())||
				path.replace("*", "").equals(node.getNamespacePrefix() + ":" + node.getName())) {
			String text = "";
			if (path.contains("*") && depth >= 2) {
				text = getBlob(node);
				addToMap(rowMap, fullpath, text);
			} else {
				text = node.getTextNormalize();
				addToMap(rowMap, fullpath, text);
			}
			if (isOutput)
				logger.info("\t [values:] " + text);
		} else {
			addToMap(rowMap, fullpath, "");
			if (isOutput)
				logger.info("\t [blank]");
		}

	}

	/**
	 * Select the processing mode for * marked nodes.<br />
	 * <b>Warning:</b> '*' mark is checked only for the last node (leaf node) in
	 * path.
	 * 
	 * @param flag
	 *            : '*' marked nodes will be returned as xml if {@code true}
	 *            (default) <br />
	 *            else text contained by '*' marked node and its children will
	 *            be returned.
	 */

	public void setBlob(boolean flag) {
		isBlob = flag;
	}

	/**
	 * Only for debugging <b>^_^</b>
	 * 
	 * @param enable
	 */

	public void setOutput(boolean enable) {
		isOutput = enable;
	}
}