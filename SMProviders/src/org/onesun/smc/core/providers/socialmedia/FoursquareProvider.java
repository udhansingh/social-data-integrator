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

import org.onesun.smc.core.model.OAuthVersion;
import org.onesun.smc.core.resources.RESTResource;
import org.scribe.model.Verb;

public class FoursquareProvider extends AbstractSocialMediaProvider {
	@Override
	public void init() {
		RESTResource[] resourceArray = {
			// REFEREHCE : https://developer.foursquare.com/docs/
				
			// Checkin
			new RESTResource("CheckIns", 								"https://api.foursquare.com/v2/checkins/$CHECKIN_ID$"),
			new RESTResource("CheckIns-Add", 				Verb.POST,	"https://api.foursquare.com/v2/checkins/add"),
			new RESTResource("CheckIns-Recent", 						"https://api.foursquare.com/v2/checkins/recent"),
			new RESTResource("CheckIns-Add-Comment", 		Verb.POST, 	"https://api.foursquare.com/v2/checkins/$CHECKIN_ID$/addcomment"),
			new RESTResource("CheckIns-Delete-Comment", 	Verb.POST, 	"https://api.foursquare.com/v2/checkins/$CHECKIN_ID$/deletecomment"),
			
			// Photo
			new RESTResource("Photos", 									"https://api.foursquare.com/v2/photos/$PHOTO_ID$"),
			new RESTResource("Photos-Add", 					Verb.POST, 	"https://api.foursquare.com/v2/photos/add"),
			
			// Multi
			new RESTResource("Multi", 									"https://api.foursquare.com/v2/multi"),
			
			// Settings
			new RESTResource("Settings", 								"https://api.foursquare.com/v2/settings/$SETTINGS_ID$"),
			new RESTResource("Settings-All", 							"https://api.foursquare.com/v2/settings/all"),
			new RESTResource("Settings-Set-Value", 			Verb.POST, 	"https://api.foursquare.com/v2/settings/$SETTINGS_ID$/set"),
			
			// Special
			new RESTResource("Specials", 								"https://api.foursquare.com/v2/specials/$SPECIALS_ID$"),
			new RESTResource("Specials-Search", 						"https://api.foursquare.com/v2/specials/search?ll=$LATITUDE$,$LONGTITUDE$"),
			
			// Tip
			new RESTResource("Tips", 									"https://api.foursquare.com/v2/tips/$TIPS_ID$"),
			new RESTResource("Tips-Add-Venue-Id", 			Verb.POST, 	"https://api.foursquare.com/v2/tips/add"),
			new RESTResource("Tips-Search-Longtitude", 					"https://api.foursquare.com/v2/tips/search?ll=$LATITUDE$,$LONGTITUDE$"),
			new RESTResource("Tips-Search-Longtitude-Post", Verb.POST, 	"https://api.foursquare.com/v2/tips/search?ll=$LATITUDE$,$LONGTITUDE$"),
			new RESTResource("Tips-MarkDone", 				Verb.POST, 	"https://api.foursquare.com/v2/tips/$TIPS_ID$/markdone"),
			new RESTResource("Tips-Unmark", 				Verb.POST, 	"https://api.foursquare.com/v2/tips/$TIPS_ID$/unmark"),
			
			// User
			new RESTResource("Users", 									"https://api.foursquare.com/v2/users/$USERS_ID$"),
			new RESTResource("Users-Search-Email",						"https://api.foursquare.com/v2/users/search"),
			new RESTResource("Users-Search-Email-Post",		Verb.POST, 	"https://api.foursquare.com/v2/users/search"),
			new RESTResource("Users-Requests",							"https://api.foursquare.com/v2/users/requests"),
			new RESTResource("Users-Badges",							"https://api.foursquare.com/v2/users/$USERS_ID$/badges"),
			new RESTResource("Users-CheckIns",							"https://api.foursquare.com/v2/users/$USERS_ID$/checkins"),
			new RESTResource("Users-Friends",							"https://api.foursquare.com/v2/users/$USERS_ID$/friends"),
			new RESTResource("Users-Tips",								"https://api.foursquare.com/v2/users/$USERS_ID$/tips"),
			new RESTResource("Users-TODO",								"https://api.foursquare.com/v2/users/$USERS_ID$/todos?ll=$LATITUDE$,$LONGTITUDE$"),
			new RESTResource("Users-Venue-History",						"https://api.foursquare.com/v2/users/$USERS_ID$/venuehistory"),
			new RESTResource("Users-Request",				Verb.POST, 	"https://api.foursquare.com/v2/users/$USERS_ID$/request"),
			new RESTResource("Users-Unfriend",				Verb.POST, 	"https://api.foursquare.com/v2/users/$USERS_ID$/unfriend"),
			new RESTResource("Users-Approve",				Verb.POST, 	"https://api.foursquare.com/v2/users/$USERS_ID$/approve"),
			new RESTResource("Users-Deny",					Verb.POST, 	"https://api.foursquare.com/v2/users/$USERS_ID$/deny"),
			new RESTResource("Users-Set-Pings",				Verb.POST, 	"https://api.foursquare.com/v2/users/$USERS_ID$/setpings"),
			
			// Venue
			new RESTResource("Venues", 									"https://api.foursquare.com/v2/venues/$VENUES_ID$"),
			new RESTResource("Venues-Add", 					Verb.POST, 	"https://api.foursquare.com/v2/venues/add?ll=$LATITUDE$,$LONGTITUDE$"),
			new RESTResource("Venues-Categoreis", 						"https://api.foursquare.com/v2/venues/categories"),
			new RESTResource("Venues-Search", 							"https://api.foursquare.com/v2/venues/search?ll=$LATITUDE$,$LONGTITUDE$"),
			new RESTResource("Venues-Here-Now", 						"https://api.foursquare.com/v2/venues/$VENUES_ID$/herenow"),
			new RESTResource("Venues-Tips", 							"https://api.foursquare.com/v2/venues/$VENUES_ID$/tips"),
			new RESTResource("Venues-Photos-Group", 					"https://api.foursquare.com/v2/venues/$VENUES_ID$/photos"),
			new RESTResource("Venues-Mark-TODO", 			Verb.POST, 	"https://api.foursquare.com/v2/venues/$VENUES_ID$/marktodo"),
			new RESTResource("Venues-Flag-Problem", 		Verb.POST, 	"https://api.foursquare.com/v2/venues/$VENUES_ID$/flag"),
			new RESTResource("Venues-Propose-Edit", 		Verb.POST, 	"https://api.foursquare.com/v2/venues/$VENUES_ID$/proposeedit?ll=$LATITUDE$,$LONGTITUDE$")
		};

		List<RESTResource> requestList = new ArrayList<RESTResource>();
		for(RESTResource resource : resourceArray){
			requestList.add(resource);
		}
		
		// REFERENCE: https://developer.foursquare.com/docs/explore.html
		this.apiProvider = getIdentity();
		this.developerUrl = "https://foursquare.com/oauth/";
		this.apiClass = org.scribe.builder.api.Foursquare2Api.class;
		this.apiScopeList = null;
		this.resources = requestList;
		this.oauthVersion = OAuthVersion.VERSION_2;
	}
	
	@Override
	public final String getIdentity() {
		return "Foursquare";
	}
}
