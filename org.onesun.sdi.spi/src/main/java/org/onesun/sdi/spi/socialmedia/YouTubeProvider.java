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
package org.onesun.sdi.spi.socialmedia;


import java.util.ArrayList;
import java.util.List;

import org.onesun.sdi.core.resources.RESTResource;

public class YouTubeProvider extends AbstractSocialMediaProvider {
	@Override
	public void init() {
		// REFERENCE: http://code.google.com/apis/gdata/faq.html#AuthScopes
		String[] scopeArray = {
			"http://gdata.youtube.com",
		};
		
		List<String> scopeList = new ArrayList<String>();
		for(String scope : scopeArray){
			scopeList.add(scope);
		}

		// REFERENCE: depends on what scope you need
		RESTResource[] resourceArray = {
			// ---------------------------------------------------------------------------
			// Youtube
			new RESTResource("Most-Viewed",			"http://gdata.youtube.com/feeds/api/standardfeeds/most_viewed"),
			new RESTResource("Top-Rated", 			"http://gdata.youtube.com/feeds/api/standardfeeds/top_rated"),
			new RESTResource("Recently-Featured", 	"http://gdata.youtube.com/feeds/api/standardfeeds/recently_featured"),
			new RESTResource("Watch-On-Mobile", 	"http://gdata.youtube.com/feeds/api/standardfeeds/watch_on_mobile"),
			new RESTResource("Most-Discussed", 		"http://gdata.youtube.com/feeds/api/standardfeeds/most_discussed"),
			new RESTResource("Top-Favorites", 		"http://gdata.youtube.com/feeds/api/standardfeeds/top_favorites"),
			new RESTResource("Most-Responded", 		"http://gdata.youtube.com/feeds/api/standardfeeds/most_responded"),
			new RESTResource("Most-Recent", 		"http://gdata.youtube.com/feeds/api/standardfeeds/most_recent"),
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
		return "YouTube";
	}
}
