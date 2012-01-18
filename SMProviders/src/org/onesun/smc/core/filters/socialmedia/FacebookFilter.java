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

public class FacebookFilter extends AbstractFilter {
	public FacebookFilter(){
		super();
	}
	
	@Override
	public final String getIdentity() {
		return "Facebook";
	}
	
	@Override
	public void init() {
		final RequestParamObject limitObject = new RequestParamObject("limit", "max_result", "500");
		final RequestParamObject queryObject = new RequestParamObject("q", "search_term", "");
		final RequestParamObject userObject = new RequestParamObject("$USER_NAME$", "user_name", "me");
		
		FilterMetadata fm = null;
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(queryObject.getInternalName(), queryObject);
		this.put("Search-Post", fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(queryObject.getInternalName(), queryObject);
		this.put("Search-User",		fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(queryObject.getInternalName(), queryObject);
		this.put("Search-Page",		fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(queryObject.getInternalName(), queryObject);
		this.put("Search-Event",	fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(queryObject.getInternalName(), queryObject);
		this.put("Search-Group",	fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(queryObject.getInternalName(), queryObject);
		this.put("Search-Place",	fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(queryObject.getInternalName(), queryObject);
		this.put("Search-Checkin",	fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Self", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Accounts", 				fm);

		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Activities", 				fm);

		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("AD-Accounts", 			fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Albums", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("App-Requests", 			fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Books", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("CheckIns", 				fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Events", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Family", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Feed", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Friend-Lists", 			fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Friend-Requests", 		fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Friends", 				fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Games",					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Groups", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Home", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Inbox", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Interests", 				fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Likes", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Links", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Movies",					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Music", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Mutual-Friends", 			fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Notes", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Notifications", 			fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Outbox", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Payments", 				fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Permissions", 			fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Photos", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Picture", 				fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Posts", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Scores", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Statuses", 				fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Tagged", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Television", 				fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Updates", 				fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Videos", 					fm);
		
		fm = new FilterMetadata();
		fm.putParamObject(limitObject.getInternalName(), limitObject);
		fm.putParamObject(userObject.getInternalName(), userObject);
		this.put("Uploaded", 				fm);
	}
}
