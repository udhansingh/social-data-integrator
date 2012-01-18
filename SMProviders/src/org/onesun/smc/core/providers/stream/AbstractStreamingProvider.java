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
package org.onesun.smc.core.providers.stream;


import java.util.ArrayList;
import java.util.List;

import org.onesun.smc.api.Connector;
import org.onesun.smc.api.StreamingProvider;
import org.onesun.smc.core.model.Authentication;
import org.onesun.smc.core.resources.StreamingResource;

public abstract class AbstractStreamingProvider implements StreamingProvider {
	protected String serviceProviderName = null;
	protected Connector connection = null;
	
	protected List<StreamingResource> resources = new ArrayList<StreamingResource>();
	
	@Override
	public abstract String getIdentity();
	
	@Override
	public abstract String getCategory();
	
	@Override
	public void setConnection(Connector connection){
		this.connection = connection;
	}
	
	@Override
	final public List<StreamingResource> getResources(){
		return resources;
	}
	
	@Override
	public abstract Authentication getAuthentication();
	
	public AbstractStreamingProvider(){
		super();
		
		init();
	}
	
	public abstract void init();
	
	@Override
	public final boolean isResponseRequired() {
		return true;
	}
	
	@Override
	public boolean save(String pathToExports) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean load(String pathToExports) {
		// TODO Auto-generated method stub
		return false;
	}
}
