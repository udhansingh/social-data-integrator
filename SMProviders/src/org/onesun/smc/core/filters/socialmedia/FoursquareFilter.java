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
import org.onesun.smc.core.model.Parameter;

public class FoursquareFilter extends AbstractFilter {

	public FoursquareFilter() {
		super();
	}
	
	@Override
	public final String getIdentity() {
		return "Foursquare";
	}

	@Override
	public void init() {
		FilterMetadata fm = null;

		// Checkin
		fm = new FilterMetadata();
		Parameter o = new Parameter("$CHECKIN_ID$", "param_checkin_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("CheckIns", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("broadcast", "param_broadcast", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("venueId", "param_venue_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("CheckIns-Add", fm);
		
		fm = new FilterMetadata();
		this.put("CheckIns-Recent", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$CHECKIN_ID$", "param_checkin_id", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("text", "param_text", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("CheckIns-Add-Comment", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$CHECKIN_ID$", "param_checkin_id", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("commentId", "param_comment_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("CheckIns-Delete-Comment", fm);
		
		// Photo
		fm = new FilterMetadata();
		o = new Parameter("$PHOTO_ID$", "param_photo_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Photos", fm);
		
		fm = new FilterMetadata();
		this.put("Photos-Add", fm);
		
		// Multi
		fm = new FilterMetadata();
		o = new Parameter("requests", "param_multi_request_csv", "request_1,request_2,...,request_N");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Multi", fm);
		
		// Settings
		fm = new FilterMetadata();
		o = new Parameter("$SETTINGS_ID$", "param_settings_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Settings", fm);
		
		fm = new FilterMetadata();
		this.put("Settings-All", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$SETTINGS_ID$", "param_settings_id", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("value", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Settings-Set-Value", fm);
		
		// Special
		fm = new FilterMetadata();
		o = new Parameter("$SPECIALS_ID$", "param_specials_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Specials", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$LATITUDE$", "param_latitude", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$LONGTITUDE$", "param_longtitude", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Specials-Search", fm);
		
		// Tip
		fm = new FilterMetadata();
		o = new Parameter("$TIPS_ID$", "param_tips_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Tips", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("venueId", "param_venue_id", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("text", "param_text", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Tips-Add-Venue-Id", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$LATITUDE$", "param_latitude", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$LONGTITUDE$", "param_longtitude", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Tips-Search-Longtitude", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$LATITUDE$", "param_latitude", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$LONGTITUDE$", "param_longtitude", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Tips-Search-Longtitude-Post", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$TIPS_ID$", "param_tips_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Tips-MarkDone", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$TIPS_ID$", "param_tips_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Tips-Unmark", fm);
		
		// User
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "param_users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("email", "param_users_email", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Search-Email", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("email", "param_users_email", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Search-Email-Post", fm);
		
		fm = new FilterMetadata();
		this.put("Users-Requests", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "param_users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Badges", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "param_users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-CheckIns", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Friends", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "param_users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Tips", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "param_users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$LATITUDE$", "param_latitude", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$LONGTITUDE$", "param_longtitude", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-TODO", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "param_users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Venue-History", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "param_users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Request", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "param_users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Unfriend", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "param_users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Approve", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "param_users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Deny", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USERS_ID$", "param_users_id", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("value", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Users-Set-Pings", fm);
		
		// Venue
		fm = new FilterMetadata();
		o = new Parameter("$VENUES_ID$", "param_venues_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Venues", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("name", "param_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$LATITUDE$", "param_latitude", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$LONGTITUDE$", "param_longtitude", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Venues-Add", fm);
		
		fm = new FilterMetadata();
		this.put("Venues-Categoreis", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$LATITUDE$", "param_latitude", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$LONGTITUDE$", "param_longtitude", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Venues-Search", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VENUES_ID$", "param_venues_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Venues-Here-Now", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VENUES_ID$", "param_venues_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Venues-Tips", fm);

		fm = new FilterMetadata();
		o = new Parameter("$VENUES_ID$", "param_venues_id", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("group", "param_group", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Venues-Photos-Group", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VENUES_ID$", "param_venues_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Venues-Mark-TODO", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VENUES_ID$", "param_venues_id", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("problem", "param_problem", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Venues-Flag-Problem", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VENUES_ID$", "param_venues_id", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("name", "param_full_name", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("address", "param_address", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("city", "param_city", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("state", "param_sate", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$LATITUDE$", "param_latitude", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$LONGTITUDE$", "param_longtitude", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Venues-Propose-Edit", fm);
	}
}
