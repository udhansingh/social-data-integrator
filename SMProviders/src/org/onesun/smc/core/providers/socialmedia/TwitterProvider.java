/*
   Copyright 2011 Udayakumar Dhansingh (Udy)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       https://www.apache.org/licenses/LICENSE-2.0

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
import org.scribe.model.Verb;

public class TwitterProvider extends AbstractSocialMediaProvider {
	@Override
	public void init() {
		// REFERENCE: 	https://dev.twitter.com/docs/api
		// 				https://dev.twitter.com/console
		
		
		RESTResource[] resourceArray = {
			// Unauthenticated
			new RESTResource("Search", 													"https://api.twitter.com/1.1/search/tweets.json"),
			
			// Authenticated
			// Account
			new RESTResource("Account-Verify-Credentials", 								"https://api.twitter.com/1.1/account/verify_credentials.json"),
			new RESTResource("Account-Rate-Limit-Status",								"https://api.twitter.com/1.1/account/rate_limit_status.json"),
			new RESTResource("Account-Update-Profile-Colors", 			Verb.POST, 		"https://api.twitter.com/1.1/account/update_profile_colors.json"),
			new RESTResource("Account-Update-Profile_Image", 			Verb.POST, 		"https://api.twitter.com/1.1/account/update_profile_image.json"),
			new RESTResource("Account-Update-Profile-Background-Image", Verb.POST, 		"https://api.twitter.com/1.1/account/update_profile_background_image.json"),
			new RESTResource("Account-Update-Profile", 					Verb.POST, 		"https://api.twitter.com/1.1/account/update_profile.json"),
			// Blocks
			new RESTResource("Blocks-Create", 							Verb.POST, 		"https://api.twitter.com/1.1/blocks/create.json"),
			new RESTResource("Blocks-Destroy",							Verb.DELETE, 	"https://api.twitter.com/1.1/blocks/destroy.json"),
			new RESTResource("Blocks-Exists", 											"https://api.twitter.com/1.1/blocks/exists.json"),
			new RESTResource("Blocks-Blocking", 										"https://api.twitter.com/1.1/blocks/blocking.json"),
			new RESTResource("Blocks-Blocking-IDs", 									"https://api.twitter.com/1.1/blocks/blocking/ids.json"),
			// Direct Message
			new RESTResource("Direct-Messages", 										"https://api.twitter.com/1.1/direct_messages.json"),
			new RESTResource("Direct-Messages-Sent", 									"https://api.twitter.com/1.1/direct_messages/sent.json"),
			new RESTResource("Direct-Messages-New",						Verb.POST, 		"https://api.twitter.com/1.1/direct_messages/new.json"),
			new RESTResource("Direct-Messages-Destroy", 				Verb.DELETE, 	"https://api.twitter.com/1.1/direct_messages/destroy/$VALUE$.json"),
			// Favorite
			new RESTResource("Favorites", 												"https://api.twitter.com/1.1/favorites/$VALUE$.json"),
			new RESTResource("Favorites-Create", 						Verb.POST, 		"https://api.twitter.com/1.1/favorites/create/$VALUE$.json"),
			new RESTResource("Favorites-Destory", 						Verb.DELETE, 	"https://api.twitter.com/1.1/favorites/destroy/$VALUE$.json"),
			// Friends and Followers
			new RESTResource("Friends", 												"https://api.twitter.com/1.1/friends/ids.json"),
			new RESTResource("Followers", 												"https://api.twitter.com/1.1/followers/ids.json"),
			// Friendship
			new RESTResource("Friendships-Create", 						Verb.POST, 		"https://api.twitter.com/1.1/friendships/create.json"),
			new RESTResource("Friendships-Destroy", 					Verb.DELETE, 	"https://api.twitter.com/1.1/friendships/destroy.json"),
			new RESTResource("Friendships-Exists", 										"https://api.twitter.com/1.1/friendships/exists.json"),
			new RESTResource("Friendships-Show", 										"https://api.twitter.com/1.1/friendships/show.json"),
			new RESTResource("Friendships-Incoming", 									"https://api.twitter.com/1.1/friendships/incoming.json"),
			new RESTResource("Friendships-Outgoing", 									"https://api.twitter.com/1.1/friendships/outgoing.json"),
			// Geo
			new RESTResource("Geo-Search", 												"https://api.twitter.com/1.1/geo/search.json"),
			new RESTResource("Geo-Similar-Places", 										"https://api.twitter.com/1.1/geo/similar_places.json"),
			new RESTResource("Geo-Reverse-Geo-Code", 									"https://api.twitter.com/1.1/geo/reverse_geocode.json"),
			new RESTResource("Geo-Id", 													"https://api.twitter.com/1.1/geo/id/$ID$.json"),
			new RESTResource("Geo-Place", 												"https://api.twitter.com/1.1/geo/place.json"),
			// Help
			new RESTResource("Help", 													"https://api.twitter.com/1.1/help/test.json"),
			// TOS
			new RESTResource("TOS", 													"https://api.twitter.com/1.1/legal/tos.json"),
			new RESTResource("TOS-Privacy", 											"https://api.twitter.com/1.1/legal/privacy.json"),
			// Lists
			new RESTResource("Lists", 													"https://api.twitter.com/1.1/%s/lists.json"),
			new RESTResource("Lists-Memberships", 										"https://api.twitter.com/1.1/lists/memberships.json"),
			new RESTResource("Lists-Subscriptions", 									"https://api.twitter.com/1.1/lists/subscriptions.json"),
			new RESTResource("Lists-Show-Slug", 										"https://api.twitter.com/1.1/lists/show.json"),
			new RESTResource("Lists-statuses-ID", 										"https://api.twitter.com/1.1/lists/statuses"),
			new RESTResource("Lists-Create",							Verb.POST, 		"https://api.twitter.com/1.1/lists/create.json"),
			new RESTResource("Lists-Update", 							Verb.POST, 		"https://api.twitter.com/1.1/lists/update.json"),
			new RESTResource("Lists-Destroy", 							Verb.POST, 		"https://api.twitter.com/1.1/lists/destroy.json"),
			// Members
			new RESTResource("Members-List-Destroy-Slug", 								"https://api.twitter.com/1.1/lists/destroy.json"),
			new RESTResource("Members-List-Show", 										"https://api.twitter.com/1.1/lists/members/show.json"),
			new RESTResource("Members-List-Create", 					Verb.POST, 		"https://api.twitter.com/1.1/lists/members/create.json"),
			new RESTResource("Members-List-Create-Slug", 				Verb.POST, 		"https://api.twitter.com/1.1/lists/members/create_all.json"),
			new RESTResource("Members-List-Destroy", 					Verb.POST, 		"https://api.twitter.com/1.1/lists/members/destroy.json"),
			// Subscribers
			new RESTResource("Subscribers", 											"https://api.twitter.com/1.1/lists/subscribers.json"),
			new RESTResource("Subscribers-Show", 										"https://api.twitter.com/1.1/lists/subscribers/show.json"),
			new RESTResource("Subscribers-Create", 						Verb.POST, 		"https://api.twitter.com/1.1/lists/subscribers/create.json"),
			new RESTResource("Subscribers-Destroy", 					Verb.POST, 		"https://api.twitter.com/1.1/lists/subscribers/destroy.json"),
			// Local Trends
			new RESTResource("Trends-Available", 										"https://api.twitter.com/1.1/trends/available.json"),
			new RESTResource("Trends-Local", 											"https://api.twitter.com/1.1/trends/$WOEID$.json"),
			// Notification
			new RESTResource("Notifications-Follow", 					Verb.POST, 		"https://api.twitter.com/1.1/notifications/follow.json"),
			new RESTResource("Notifications-Leave", 					Verb.POST, 		"https://api.twitter.com/1.1/notifications/leave.json"),
			// Saved Searches
			new RESTResource("Saved-Searches", 											"https://api.twitter.com/1.1/saved_searches.json"),
			new RESTResource("Saved-Searches-Show", 									"https://api.twitter.com/1.1/saved_searches/show/$VALUE$.json"),
			new RESTResource("Saved-Searches-Create", 					Verb.POST, 		"https://api.twitter.com/1.1/saved_searches/create.json"),
			new RESTResource("Saved-Searches-Destroy", 					Verb.DELETE, 	"https://api.twitter.com/1.1/saved_searches/destroy/$VALUE$.json"),
			// Report Spam
			new RESTResource("Report-Spam", 							Verb.POST, 		"https://api.twitter.com/1.1/report_spam.json"),
			// Timeline
			
			// public_timeline : cached for 60 seconds - max 20 rows
			new RESTResource("Statuses-Public-Timeline", 								"https://api.twitter.com/1.1/statuses/public_timeline.json"),
			// home_timeline : count parameter to get max rows - max 200 rows
			new RESTResource("Statuses-Home-Timeline", 									"https://api.twitter.com/1.1/statuses/home_timeline.json"),
			// user_timeline : count parameter to get max rows - max 200 rows
			new RESTResource("Statuses-User-Timeline", 									"https://api.twitter.com/1.1/statuses/user_timeline.json"),
			new RESTResource("Statuses-Mentions", 										"https://api.twitter.com/1.1/statuses/mentions.json"),
			new RESTResource("Statuses-Retweeted-By-Me", 								"https://api.twitter.com/1.1/statuses/retweeted_by_me.json"),
			new RESTResource("Statuses-Retweeted-To-Me", 								"https://api.twitter.com/1.1/statuses/retweeted_to_me.json"),
			new RESTResource("Statuses-Retweets-Of-Me", 								"https://api.twitter.com/1.1/statuses/retweets_of_me.json"),
			// Trends
			new RESTResource("Trends", 													"https://api.twitter.com/1.1/trends.json"),
			new RESTResource("Trends-Current", 											"https://api.twitter.com/1.1/trends/current.json"),
			new RESTResource("Trends-Daily", 											"https://api.twitter.com/1.1/trends/daily.json"),
			new RESTResource("Trends-Weekly", 											"https://api.twitter.com/1.1/trends/weekly.json"),
			// Tweets
			new RESTResource("Statuses-Retweeted-By", 									"https://api.twitter.com/1.1/statuses/$SCREEN_NAME$/retweeted_by.json"),
			new RESTResource("Statuses-Retweeted-By-ID", 								"https://api.twitter.com/1.1/statuses/$SCREEN_NAME$/retweeted_by/ids.json"),
			new RESTResource("Statuses-Show", 											"https://api.twitter.com/1.1/statuses/show/$SCREEN_NAME$.json"),
			new RESTResource("Statuses-Update", 						Verb.POST, 		"https://api.twitter.com/1.1/statuses/update.json"),
			new RESTResource("Statuses-Destroy", 						Verb.POST, 		"https://api.twitter.com/1.1/statuses/destroy/$SCREEN_NAME$.json"),
			new RESTResource("Statuses-Retweet", 						Verb.POST, 		"https://api.twitter.com/1.1/statuses/retweet/$SCREEN_NAME$.json"),
			new RESTResource("Statuses-Retweets", 										"https://api.twitter.com/1.1/statuses/retweets/$SCREEN_NAME$.json"),
			// User
			new RESTResource("Users-Show", 												"https://api.twitter.com/1.1/users/show.json"),
			new RESTResource("Users-Lookup", 											"https://api.twitter.com/1.1/users/lookup.json"),
			new RESTResource("Users-Search", 											"https://api.twitter.com/1.1/users/search.json"),
			new RESTResource("Users-Suggestions", 										"https://api.twitter.com/1.1/users/suggestions.json"),
			new RESTResource("Users-Suggestions-User", 									"https://api.twitter.com/1.1/users/suggestions/$SCREEN_NAME$.json"),
			new RESTResource("Users-Profile-Image", 									"https://api.twitter.com/1.1/users/profile_image/$SCREEN_NAME$.json")
		};

		List<RESTResource> requestList = new ArrayList<RESTResource>();
		for(RESTResource resource : resourceArray){
			requestList.add(resource);
		}
		
		this.apiProvider = getIdentity();
		this.developerUrl = "https://dev.twitter.com/apps";
		this.apiClass = org.scribe.builder.api.TwitterApi.class;
		this.apiScopeList = null;
		this.resources = requestList;
	}
	
	@Override
	public final String getIdentity() {
		return "Twitter";
	}
}
