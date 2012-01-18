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
package org.onesun.smc.core.filters.socialmedia;

import org.onesun.smc.core.filters.AbstractFilter;
import org.onesun.smc.core.metadata.FilterMetadata;
import org.onesun.smc.core.model.RequestParamObject;

public class TwitterFilter extends AbstractFilter {
	@Override
	public final String getIdentity() {
		return "Twitter";
	}
	
	public TwitterFilter() {
		super();
	}

	@Override
	public void init() {
		FilterMetadata fm = null;
		RequestParamObject o = null;
		
		fm = new FilterMetadata();
		o = new RequestParamObject("q", "param_query", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Search", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("tile", "param_title", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Account-Update-Profile-Background-Image", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("name", "param_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("url", "param_url", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("location", "param_location", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("description", "param_description", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Account-Update-Profile", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Blocks-Create", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Blocks-Destroy", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Blocks-Exists", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("text", "param_text", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Direct-Messages-New", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$VALUE$", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Direct-Messages-Destroy", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$VALUE$", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Favorites", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$VALUE$", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Favorites-Create", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$VALUE$", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Favorites-Destory", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Friends", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Followers", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Friendships-Create", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Friendships-Destroy", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("user_a", "param_user_a", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("user_b", "param_user_b", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Friendships-Exists", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("target_screen_name", "param_target_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("source_screen_name", "param_source_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Friendships-Show", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("cursor", "param_cursor", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Friendships-Incoming", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("cursor", "param_cursor", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Friendships-Outgoing", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("lat", "param_lat", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("long", "param_long", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("query", "param_query", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Geo-Search", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("name", "param_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Geo-Similar-Places", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("lat", "param_lat", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("long", "param_long", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("GGeo-Reverse-Geo-Code", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$ID$", "param_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Geo-Id", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("name", "param_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Geo-Place", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Lists", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("owner_screen_name", "param_owner_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("slug", "param_slug", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Lists-Show-Slug", fm);

		fm = new FilterMetadata();
		o = new RequestParamObject("list_id", "param_list_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Lists-statuses-ID", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("name", "param_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Lists-Create", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("owner_screen_name", "param_owner_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("slug", "param_slug", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Lists-Update", fm);
	
		fm = new FilterMetadata();
		o = new RequestParamObject("owner_screen_name", "param_owner_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("slug", "param_slug", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Lists-Destroy", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("owner_screen_name", "param_owner_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("slug", "param_slug", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Members-List-Destroy-Slug", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Members-List-Show", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Members-List-Create", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("owner_screen_name", "param_owner_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("slug", "param_slug", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Members-List-Create-Slug", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Members-List-Destroy", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("owner_screen_name", "param_owner_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("slug", "param_slug", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Subscribers", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("owner_screen_name", "param_owner_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("slug", "param_slug", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Subscribers-Show", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("owner_screen_name", "param_owner_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("slug", "param_slug", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Subscribers-Create", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("owner_screen_name", "param_owner_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("slug", "param_slug", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Subscribers-Destroy", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$WOEID$", "param_woe_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Trends-Local", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Notifications-Follow", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Notifications-Leave", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$VALUE$", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Saved-Searches-Show", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("query", "param_query", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Saved-Searches-Create", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$VALUE$", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Saved-Searches-Destroy", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Report-Spam", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("count", "param_count", "200");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Statuses-Home-Timeline", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("count", "param_count", "200");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Statuses-User-Timeline", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$SCREEN_NAME$", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Statuses-Retweeted-By", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$SCREEN_NAME$", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Statuses-Retweeted-By-ID", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$SCREEN_NAME$", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Statuses-Show", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("status", "param_status", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Statuses-Update", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$SCREEN_NAME$", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Statuses-Destroy", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$SCREEN_NAME$", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Statuses-Retweet", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$SCREEN_NAME$", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Statuses-Retweets", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Show", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("screen_name", "param_screen_name", "<SCREEN_NAME>,sonoa");
		fm.putParamObject(o.getInternalName(), o);
		o = new RequestParamObject("user_id", "param_user_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Lookup", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("q", "param_query", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Search", fm);
	
		fm = new FilterMetadata();
		o = new RequestParamObject("$SCREEN_NAME$", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Suggestions-User", fm);
		
		fm = new FilterMetadata();
		o = new RequestParamObject("$SCREEN_NAME$", "param_screen_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Profile-Image", fm);
	}
}
