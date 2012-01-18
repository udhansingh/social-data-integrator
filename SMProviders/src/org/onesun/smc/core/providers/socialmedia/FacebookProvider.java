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

public class FacebookProvider extends AbstractSocialMediaProvider {
	@Override
	public void init() {
		// REFERENCE: 	https://developers.facebook.com/docs/reference/api/permissions
		//				https://developers.facebook.com/tools/explorer?method=GET&path=1175900313
		String[] scopeArray = {
			// User Data Permissions
			"user_about_me", "user_activities", "user_birthday", "user_checkins", "user_education_history",
			"user_events", "user_games_activity", "user_groups", "user_hometown", "user_interests",
			"user_likes", "user_location", "user_notes", "user_online_presence", "user_photo_video_tags",
			"user_photos", "user_relationship_details", "user_relationships", "user_religion_politics", "user_status",
			"user_subscriptions", "user_videos", "user_website", "user_work_history",
			// Friends Data Permissions
			"friends_about_me", "friends_activities", "friends_birthday", "friends_checkins", "friends_education_history",
			"friends_events", "friends_games_activity", "friends_groups", "friends_hometown", "friends_interests",
			"friends_likes", "friends_location", "friends_notes", "friends_online_presence", "friends_photo_video_tags",
			"friends_photos", "friends_relationship_details", "friends_relationships", "friends_religion_politics", "friends_status",
			"friends_subscriptions", "friends_videos", "friends_website", "friends_work_history",
			// Extended Permissions
			"ads_management", "create_event", "create_note", "email", "export_stream",
			"manage_friendlists", "manage_notifications", "manage_pages", "offline_access", "photo_upload",
			"publish_actions", "publish_checkins", "publish_stream", "read_friendlists", "read_insights",
			"read_mailbox", "read_requests", "read_stream", "rsvp_event", "share_item",
			"sms", "status_update", "video_upload", "xmpp_login"
		};
		
		List<String> scopeList = new ArrayList<String>();
		for(String scope : scopeArray){
			scopeList.add(scope);
		}
		
		// REFERENCE: https://developers.facebook.com/docs/reference/api/
		RESTResource[] resourceArray = {
			// Unauthenticated
			new RESTResource("Search-Post",		false,	"https://graph.facebook.com/search?type=POST"),
			new RESTResource("Search-User",		false,	"https://graph.facebook.com/search?type=USER"),
			new RESTResource("Search-Page",		false,	"https://graph.facebook.com/search?type=PAGE"),
			new RESTResource("Search-Event",	false,	"https://graph.facebook.com/search?type=EVENT"),
			new RESTResource("Search-Group",	false,	"https://graph.facebook.com/search?type=GROUP"),
			new RESTResource("Search-Place",	false,	"https://graph.facebook.com/search?type=PLACE"),
			new RESTResource("Search-Checkin",	false,	"https://graph.facebook.com/search?type=CHECKIN"),
			
			
			// Authenticated
			new RESTResource("Self", 					"https://graph.facebook.com/$USER_NAME$"),
			new RESTResource("Accounts", 				"https://graph.facebook.com/$USER_NAME$/accounts"),
			new RESTResource("Activities", 				"https://graph.facebook.com/$USER_NAME$/activities"),
			new RESTResource("AD-Accounts", 			"https://graph.facebook.com/$USER_NAME$/adaccounts"),
			new RESTResource("Albums", 					"https://graph.facebook.com/$USER_NAME$/albums"),
			new RESTResource("App-Requests", 			"https://graph.facebook.com/$USER_NAME$/apprequests"),
			new RESTResource("Books", 					"https://graph.facebook.com/$USER_NAME$/books"),
			new RESTResource("CheckIns", 				"https://graph.facebook.com/$USER_NAME$/checkins"),
			new RESTResource("Events", 					"https://graph.facebook.com/$USER_NAME$/events"),
			new RESTResource("Family", 					"https://graph.facebook.com/$USER_NAME$/family"),
			new RESTResource("Feed", 					"https://graph.facebook.com/$USER_NAME$/feed"),
			new RESTResource("Friend-Lists", 			"https://graph.facebook.com/$USER_NAME$/friendlists"),
			new RESTResource("Friend-Requests", 		"https://graph.facebook.com/$USER_NAME$/friendrequests"),
			new RESTResource("Friends", 				"https://graph.facebook.com/$USER_NAME$/friends"),
			new RESTResource("Games",					"https://graph.facebook.com/$USER_NAME$/games"),
			new RESTResource("Groups", 					"https://graph.facebook.com/$USER_NAME$/groups"),
			new RESTResource("Home", 					"https://graph.facebook.com/$USER_NAME$/home"),
			new RESTResource("Inbox", 					"https://graph.facebook.com/$USER_NAME$/inbox"),
			new RESTResource("Interests", 				"https://graph.facebook.com/$USER_NAME$/interests"),
			new RESTResource("Likes", 					"https://graph.facebook.com/$USER_NAME$/likes"),
			new RESTResource("Links", 					"https://graph.facebook.com/$USER_NAME$/links"),
			new RESTResource("Movies",					"https://graph.facebook.com/$USER_NAME$/movies"),
			new RESTResource("Music", 					"https://graph.facebook.com/$USER_NAME$/music"),
			new RESTResource("Mutual-Friends", 			"https://graph.facebook.com/$USER_NAME$/mutualfriends"),
			new RESTResource("Notes", 					"https://graph.facebook.com/$USER_NAME$/notes"),
			new RESTResource("Notifications", 			"https://graph.facebook.com/$USER_NAME$/notifications"),
			new RESTResource("Outbox", 					"https://graph.facebook.com/$USER_NAME$/outbox"),
			new RESTResource("Payments", 				"https://graph.facebook.com/$USER_NAME$/payments"),
			new RESTResource("Permissions", 			"https://graph.facebook.com/$USER_NAME$/permissions"),
			new RESTResource("Photos", 					"https://graph.facebook.com/$USER_NAME$/photos"),
			new RESTResource("Picture", 				"https://graph.facebook.com/$USER_NAME$/picture"),
			new RESTResource("Posts", 					"https://graph.facebook.com/$USER_NAME$/posts"),
			new RESTResource("Scores", 					"https://graph.facebook.com/$USER_NAME$/scores"),
			new RESTResource("Statuses", 				"https://graph.facebook.com/$USER_NAME$/statuses"),
			new RESTResource("Tagged", 					"https://graph.facebook.com/$USER_NAME$/tagged"),
			new RESTResource("Television", 				"https://graph.facebook.com/$USER_NAME$/television"),
			new RESTResource("Updates", 				"https://graph.facebook.com/$USER_NAME$/updates"),
			new RESTResource("Videos", 					"https://graph.facebook.com/$USER_NAME$/videos"),
			new RESTResource("Uploaded", 				"https://graph.facebook.com/$USER_NAME$/videos/uploaded")
		};
		
		List<RESTResource> requestList = new ArrayList<RESTResource>();
		for(RESTResource resource : resourceArray){
			requestList.add(resource);
		}
		
		this.apiProvider = getIdentity();
		this.developerUrl = "https://developers.facebook.com/apps";
		this.apiClass = org.scribe.builder.api.FacebookApi.class;
		this.apiScopeList = scopeList;
		this.resources = requestList;
		this.oauthVersion = OAuthVersion.VERSION_2;
	}
	
	@Override
	public final String getIdentity() {
		return "Facebook";
	}
}
