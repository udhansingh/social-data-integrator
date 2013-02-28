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
package org.onesun.smc.core.providers.aggregators;

import java.util.ArrayList;
import java.util.List;

import org.onesun.smc.api.ConnectionProperties;
import org.onesun.smc.api.StreamingProvider;
import org.onesun.smc.core.resources.StreamingResource;

public class GnipStreamingProvider implements StreamingProvider {
	protected String serviceProviderName = null;
	protected ConnectionProperties connection = null;
	
	protected List<StreamingResource> resources = new ArrayList<StreamingResource>();
	
	@Override
	public void init() {
		serviceProviderName = getIdentity().toLowerCase();
	}
	
	@Override
	public final String getIdentity() {
		return "Gnip";
	}

	@Override
	public String getAuthentication() {
		return "GNIP";
	}

	@Override
	public String getCategory() {
		return "GNIP";
	}

	@Override
	public boolean isResponseRequired() {
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

	@Override
	public void setConnection(ConnectionProperties connection){
		this.connection = connection;
	}
	
	@Override
	final public List<StreamingResource> getResources(){
		return resources;
	}
}
