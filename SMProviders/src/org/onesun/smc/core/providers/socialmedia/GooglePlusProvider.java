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

public class GooglePlusProvider extends AbstractSocialMediaProvider {
	@Override
	public void init() {
		// REFERENCE: http://code.google.com/apis/gdata/faq.html#AuthScopes
		String[] scopeArray = {
			"https://www.googleapis.com/auth/plus.me",
//			"http://gdata.youtube.com",
//			"http://picasaweb.google.com/data/",
//			"https://mail.google.com/",
//			"http://www.google.com/m8/feeds/",
//			"https://www.google.com/m8/feeds/"
		};
		
		List<String> scopeList = new ArrayList<String>();
		for(String scope : scopeArray){
			scopeList.add(scope);
		}

		// REFERENCE: depends on what scope you need
		RESTResource[] resourceArray = {
			// REFEREHCE : Google+ : https://developers.google.com/+/
			// People
			new RESTResource("Plus-People", 				"https://www.googleapis.com/plus/v1/people/$USER$"),
			new RESTResource("Plus-People-Query", 			"https://www.googleapis.com/plus/v1/people"),
			new RESTResource("Plus-People-Activities",		"https://www.googleapis.com/plus/v1/people/$USER$/activities/$SCOPE$/"),
			
			// Activities
			new RESTResource("Plus-Activities", 			"https://www.googleapis.com/plus/v1/activities/$ACTIVITY_ID$"),
			new RESTResource("Plus-Activities-People",		"https://www.googleapis.com/plus/v1/people/$USER$/activities/$SCOPE$/"),
			new RESTResource("Plus-Activities-Query",		"https://www.googleapis.com/plus/v1/activities/"),
			
			// Comments
			new RESTResource("Plus-Comments",				"https://www.googleapis.com/plus/v1/comments/$COMMENT_ID$"),
			new RESTResource("Plus-Comments-Activities",	"https://www.googleapis.com/plus/v1/activities/$ACTIVITY_ID$/comments"),
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
		return "Google Plus";
	}
}
