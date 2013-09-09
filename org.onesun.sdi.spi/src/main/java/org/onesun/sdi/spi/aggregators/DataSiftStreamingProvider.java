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
package org.onesun.sdi.spi.aggregators;

import java.util.ArrayList;
import java.util.List;

import org.onesun.sdi.core.api.Connection;
import org.onesun.sdi.core.resources.StreamingResource;
import org.onesun.sdi.spi.api.StreamingProvider;

public class DataSiftStreamingProvider implements StreamingProvider {
	protected String serviceProviderName = null;
	protected Connection connection = null;
	
	protected List<StreamingResource> resources = new ArrayList<StreamingResource>();
	
	@Override
	public void init() {
		serviceProviderName = getIdentity().toLowerCase();
	}
	
	@Override
	public final String getIdentity() {
		return "DataSift";
	}

	@Override
	public String getAuthentication() {
		return "DATASIFT";
	}

	@Override
	public String getCategory() {
		return "DATASIFT";
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
	public void setConnection(Connection connection){
		this.connection = connection;
	}
	
	@Override
	final public List<StreamingResource> getResources(){
		return resources;
	}
}
