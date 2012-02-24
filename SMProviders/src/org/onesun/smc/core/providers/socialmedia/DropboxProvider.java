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

public class DropboxProvider extends AbstractSocialMediaProvider {
	@Override
	public void init() {
		// REFERENCE: 	https://www.dropbox.com/developers/reference/api
		
		RESTResource[] resourceArray = {
			new RESTResource("Account-Info", 							false,			"https://api.dropbox.com/1/account/info"),
			new RESTResource("Get-Sandbox-Files", 						false,			"https://api-content.dropbox.com/1/files/sandbox/"),
			new RESTResource("Get-Dropbox-Files", 						false,			"https://api-content.dropbox.com/1/files/dropbox/")
		};

		List<RESTResource> requestList = new ArrayList<RESTResource>();
		for(RESTResource resource : resourceArray){
			requestList.add(resource);
		}
		
		this.apiProvider = getIdentity();
		this.developerUrl = "https://www.dropbox.com/developers/apps";
		this.apiClass = org.scribe.builder.api.DropBoxApi.class;
		this.apiScopeList = null;
		this.resources = requestList;
	}
	
	@Override
	public final String getIdentity() {
		return "Dropbox";
	}
}
