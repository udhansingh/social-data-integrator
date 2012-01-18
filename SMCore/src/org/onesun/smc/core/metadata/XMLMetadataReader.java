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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.onesun.smc.core.exceptions.InvalidDataException;
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
			path = node.getName();
		else
			path = path + "/" + node.getName();

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
			metadata.put(path, path);
		}

	}

	@Override
	public void setLeafNodesOnly(boolean leafNodesOnly) {
		this.leafNodesOnly = leafNodesOnly;
		pathtree.leafNodesOnly = this.leafNodesOnly;
	}

	private class PathTree {
		private Node rootnode = new Node();
		private boolean stopAtMultivalued = true;
		private boolean leafNodesOnly = true;
		private int depth = Integer.MAX_VALUE;
		private int dataDepth = 0;
		private int processingDepth = 0;
		private List<String> xpaths = new ArrayList<String>();
		private int TEST_DEPTH = 1;

		@SuppressWarnings("unused")
		private class Node {
			private String name = null;
			private Node parent = null;
			private boolean multivalued = false;

			private Map<String, Node> children = new HashMap<String, Node>();

			public void add(String path, boolean multivalued) {
				int loc = path.indexOf("/");
				String childname;
				if (loc > -1) {
					childname = path.substring(0, loc);
					path = path.substring(loc + 1);
					/* Not used normally. Only for safety from jumps in xpaths */
					if (!children.containsKey(childname)) {
						Node node = new Node();
						node.setParent(this);
						node.setName(childname);
						if (!node.multivalued)
							node.multivalued = multivalued;
						children.put(childname, node);
					}

					children.get(childname).add(path, multivalued);
				} else {
					childname = path;
					if (!children.containsKey(path)) {
						Node node = new Node();
						node.setParent(this);
						node.setName(childname);
						if (!node.multivalued)
							node.multivalued = multivalued;
						children.put(childname, node);
					} else {
						Node node = children.get(childname);
						if (!node.multivalued)
							node.multivalued = multivalued;

					}
				}
			}

			public void setName(String name) {
				this.name = name;
			}

			public void setParent(Node parent) {
				this.parent = parent;
			}
		}

		public PathTree() {
		}

		public void addpath(String path, boolean multivalued) {
			rootnode.add(path, multivalued);
		}

		public void generate(Node node, String path, int depth, List<String> xpaths) {
			Set<String> nodeset = node.children.keySet();

			if (depth > processingDepth) {
				if (nodeset.size() > 0 && node.multivalued == false)
					xpaths.add(path + "*");
				else
					xpaths.add(path);
				return;
			}

			if (path != null) {
				if ((leafNodesOnly && (node.children.size() == 0 || (node.multivalued && depth >= TEST_DEPTH + 1)))
						|| (!leafNodesOnly))
					xpaths.add(path);
			}

			if ((stopAtMultivalued && node.multivalued && depth >= TEST_DEPTH + 1))
				return;

			for (String nodename : nodeset) {
				String marker = "";
				Node child = node.children.get(nodename);
				if (child.multivalued && depth >= TEST_DEPTH)
					marker = "*";
				if (path == null)
					generate(child, nodename + marker, depth + 1, xpaths);
				else
					generate(child, path + "/" + nodename + marker, depth + 1,
							xpaths);

			}
		}

		/**
		 * 
		 * @return {@code List<String>} containing xPaths to any depth upto
		 *         multivalued nodes.
		 * @throws InvalidDataException
		 */
		public List<String> generate() throws InvalidDataException {
			processingDepth = Integer.MAX_VALUE;
			// TODO Auto-generated method stub
			if (depth < 0) {
				throw new InvalidDataException("How depth can ne negative!");
			}
			if (depth == 0) {
				throw new InvalidDataException(
						"Why do you need this algo for 0 depth! Return the whole xml data.");
			}
			processingDepth = depth - dataDepth;
			if (processingDepth < 1) {
				throw new InvalidDataException(
						String.format(
								"Invalid depth for current xPath generation settings: %d.",
								depth));
			}
			generate(rootnode, null, 0, xpaths);
			return xpaths;
		}

		public void setDataDepth(int depth) {
			this.dataDepth = depth;
		}

	}

}