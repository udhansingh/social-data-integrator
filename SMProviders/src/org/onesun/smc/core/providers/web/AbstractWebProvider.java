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
package org.onesun.smc.core.providers.web;


import java.util.ArrayList;
import java.util.List;

import org.onesun.smc.api.WebProvider;
import org.onesun.smc.core.connectors.WebConnector;
import org.onesun.smc.core.model.Authentication;
import org.onesun.smc.core.resources.WebResource;

public abstract class AbstractWebProvider implements WebProvider {
	protected List<WebResource> resources = new ArrayList<WebResource>();
	protected WebConnector connection = null;
	
	@Override
	public abstract String getIdentity();
	
	@Override
	public void setConnection(WebConnector connection){
		this.connection = connection;
	}
	
	@Override
	final public List<WebResource> getResources(){
		return resources;
	}
	
	@Override
	public Authentication getAuthentication() {
		return Authentication.WEB_SERVER;
	}
	
	public AbstractWebProvider(){
		super();
		
		init();
	}
	
	public abstract void init();
	
	@Override
	public final boolean isResponseRequired() {
		return false;
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
