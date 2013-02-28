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
package org.onesun.smc.core.metadata;

import java.util.ArrayList;
import java.util.List;

import org.onesun.smc.api.MetadataReader;

public abstract class AbstractMetadataReader implements MetadataReader {
	protected int 			depth 				= 0;
	protected List<String>	paths 				= new ArrayList<String>();
	protected boolean		stopAtMultiValued 	= true;
	protected boolean		leafNodesOnly 		= false;
	protected String		beginPath 			= null;
	
	@Override
	public void setStopAtMultivalued(boolean flag) {
		this.stopAtMultiValued = flag;
	}
	
	@Override
	final public void beginProcessingAt(String beginPath){
		this.beginPath = beginPath;
	}
	
	final public int getDepth() {
		return depth;
	}
	
	@Override
	final public void setDepth(int depth) {
		this.depth = depth;
	}
	
	final public void setPaths(List<String> paths) {
		this.paths = paths;
	}

	final public boolean isStopAtMultiValued() {
		return stopAtMultiValued;
	}
	
	final public void setStopAtMultiValued(boolean stopAtMultiValued) {
		this.stopAtMultiValued = stopAtMultiValued;
	}
	
	final public boolean isLeafNodesOnly() {
		return leafNodesOnly;
	}
	
	@Override
	public void setLeafNodesOnly(boolean leafNodesOnly) {
		this.leafNodesOnly = leafNodesOnly;
	}
	
	@Override
	public void initialize ()  throws Exception{
		
	}
}
