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

public class GoogleMailProvider extends AbstractSocialMediaProvider {
	@Override
	public void init() {
		// REFERENCE: http://code.google.com/apis/gdata/faq.html#AuthScopes
		String[] scopeArray = {
			"https://mail.google.com/",
		};
		
		List<String> scopeList = new ArrayList<String>();
		for(String scope : scopeArray){
			scopeList.add(scope);
		}

		// REFERENCE: depends on what scope you need
		RESTResource[] resourceArray = {
			// ---------------------------------------------------------------------------
			// Google Mail
			new RESTResource("Mail-IMAP", 					"https://mail.google.com/mail/b/$EMAIL$/imap/"),
			new RESTResource("Mail-SMTP", 					"https://mail.google.com/mail/b/$EMAIL$/smtp/")
		};

		// http://code.google.com/apis/gdata/faq.html#AuthScopes
		List<RESTResource> requestList = new ArrayList<RESTResource>();
		for(RESTResource resource : resourceArray){
			requestList.add(resource);
		}
		
		// REFERENCE: "https://code.google.com/apis/console/
		this.apiProvider = getIdentity();
		this.developerUrl = "http://code.google.com/more/";
		this.apiClass = org.scribe.builder.api.GoogleApi.class;
		this.apiScopeList = scopeList;
		this.resources = requestList;
	}
	
	@Override
	public final String getIdentity() {
		return "Google Mail";
	}
}
