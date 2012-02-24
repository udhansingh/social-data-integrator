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
package org.onesun.smc.core.providers.socialmedia;

import java.util.ArrayList;
import java.util.List;

import org.onesun.smc.core.resources.RESTResource;

public class YahooProvider extends AbstractSocialMediaProvider {
	@Override
	public void init() {
		// REFERENCE: 	http://developer.yahoo.com/everything.html
		String[] scopeArray = {};
		
		RESTResource[] resourceArray = {
			new RESTResource("Get-Latest-Email", 	true,	"http://query.yahooapis.com/v1/yql?q=select%20*%20from%20ymail.messages%20where%20numMid%3D'100'")
		};

		List<String> scopeList = new ArrayList<String>();
		for(String scope : scopeArray){
			scopeList.add(scope);
		}
		
		List<RESTResource> requestList = new ArrayList<RESTResource>();
		for(RESTResource resource : resourceArray){
			requestList.add(resource);
		}
		
		this.apiProvider = getIdentity();
		this.developerUrl = "https://developer.apps.yahoo.com/projects";
		this.apiClass = org.scribe.builder.api.YahooApi.class;
		this.apiScopeList = null;
		this.resources = requestList;
	}
	
	@Override
	public final String getIdentity() {
		return "Yahoo";
	}
}
