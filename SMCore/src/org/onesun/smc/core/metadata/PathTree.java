package org.onesun.smc.core.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.onesun.smc.core.exceptions.InvalidDataException;

public class PathTree {
	private Node rootnode = new Node();
	public  boolean stopAtMultivalued = true;
	public boolean leafNodesOnly = true;
	private int depth = Integer.MAX_VALUE;
	private int dataDepth = 0;
	private int processingDepth = 0;
	public List<String> xpaths = new ArrayList<String>();
	public int TEST_DEPTH = 1;

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