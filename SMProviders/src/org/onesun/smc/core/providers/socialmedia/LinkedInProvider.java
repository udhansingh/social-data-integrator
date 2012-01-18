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
import org.scribe.model.Verb;

public class LinkedInProvider extends AbstractSocialMediaProvider {
	@Override
	public void init() {
		// REFERENCE: 	https://developer.linkedin.com/apis
		//				http://developer.linkedinlabs.com/rest-console/
		RESTResource[] resourceArray = {
			// Profile
			new RESTResource("People", 											"http://api.linkedin.com/v1/people/~"), 
			new RESTResource("People-ID", 										"http://api.linkedin.com/v1/people/id=$ID$"),
			new RESTResource("People-URL",										"http://api.linkedin.com/v1/people/url=$URL$"),
			//Bulk Profile URL
			new RESTResource("People-Bulk",										"http://api.linkedin.com/v1/people::($VALUES$)"),
			// People Search 		
			new RESTResource("People-Search",									"http://api.linkedin.com/v1/people-search?$SEARCH_TERM$"),
			// Connections
			new RESTResource("Connections",										"http://api.linkedin.com/v1/people/~/connections"),
			new RESTResource("Connections-ID",									"http://api.linkedin.com/v1/people/id=$ID$/connections"),
			new RESTResource("Connections-URL",									"http://api.linkedin.com/v1/people/url=$URL$/connections"),
			// Mailbox
			new RESTResource("Mailbox",							Verb.POST, 		"http://api.linkedin.com/v1/people/~/mailbox"),
			// Network updates
			new RESTResource("Network-Updates",									"http://api.linkedin.com/v1/people/~/network/updates"),
			new RESTResource("Person-Activities",				Verb.POST, 		"http://api.linkedin.com/v1/people/~/person-activities"),
			// Comments, Likes and Shares
			new RESTResource("Network-Updates-Comments",						"http://api.linkedin.com/v1/people/~/network/updates/key=$KEY$/update-comments"),
			new RESTResource("Network-Updates-Likes",							"http://api.linkedin.com/v1/people/~/network/updates/key=$KEY$/likes"),
			new RESTResource("Network-Updates-Update-Comment",	Verb.POST, 		"http://api.linkedin.com/v1/people/~/network/updates/key=$KEY$/update-comments"),
			new RESTResource("Network-Updates-Liked",			Verb.PUT, 		"http://api.linkedin.com/v1/people/~/network/updates/key=$KEY$/is-liked"),
			new RESTResource("Shares",							Verb.POST, 		"http://api.linkedin.com/v1/people/~/shares"),
			new RESTResource("Current-Share",									"http://api.linkedin.com/v1/people/~:(current-share)"),
			new RESTResource("Network-Share",									"http://api.linkedin.com/v1/people/~/network"),
			// Company
			new RESTResource("Companies-Email-Domain",							"http://api.linkedin.com/v1/companies"),
			new RESTResource("Companies-Name",									"http://api.linkedin.com/v1/companies/$NAME$"),
			new RESTResource("Companies",										"http://api.linkedin.com/v1/companies::($VALUES$)"),
			new RESTResource("Companies-Search",								"http://api.linkedin.com/v1/company-search"),
			new RESTResource("Companies-Following",								"http://api.linkedin.com/v1/people/~/following/companies"),
			new RESTResource("Companies-To-Follow",								"http://api.linkedin.com/v1/people/~/suggestions/to-follow/companies"),
			new RESTResource("Companies-Following-Post",		Verb.POST, 		"http://api.linkedin.com/v1/people/~/following/companies"),
			// Groups
			new RESTResource("Groups-Memberships-Create",		Verb.PUT, 		"http://api.linkedin.com/v1/people/~/group-memberships/$VALUE$"),
			new RESTResource("Groups-Memberships-Delete",		Verb.DELETE, 	"http://api.linkedin.com/v1/people/~/group-memberships/$VALUE$"),
			new RESTResource("Groups-Memberships", 								"http://api.linkedin.com/v1/people/~/group-memberships"),
			new RESTResource("Groups-Suggestions",								"http://api.linkedin.com/v1/people/~/suggestions/groups"),
			new RESTResource("Groups-Search",									"http://api.linkedin.com/v1/people/~/group-memberships/$VALUE$:(show-group-logo-in-profile,contact-email,email-digest-frequency,email-announcements-from-managers,allow-messages-from-members,email-for-every-new-post)"),
			new RESTResource("Groups-Posts-Fields",								"http://api.linkedin.com/v1/groups/$VALUE$/posts:(creation-timestamp,title,summary,creator:(first-name,last-name,picture-url,headline),likes,attachment:(image-url,content-domain,content-url,title,summary),relation-to-viewer)"),
			new RESTResource("Groups-Posts",					Verb.POST, 		"http://api.linkedin.com/v1/groups/$VALUE$/posts"),
			new RESTResource("Groups-Relation-To-Viewer",		Verb.PUT, 		"http://api.linkedin.com/v1/posts/g-$SOURCE$-S-$TARGET$/relation-to-viewer/is-liked"),
			new RESTResource("Groups-Comments",					Verb.POST, 		"http://api.linkedin.com/v1/posts/g-$SOURCE$-S-$TARGET$/comments"),
			// Jobs
			new RESTResource("Jobs-Search",										"http://api.linkedin.com/v1/job-search"),
			new RESTResource("Jobs-Suggestions",								"http://api.linkedin.com/v1/people/~/suggestions/job-suggestions"),
			new RESTResource("Jobs",											"http://api.linkedin.com/v1/jobs/$JOB$"),
			//api base url
			new RESTResource("Generic",											"http://api.linkedin.com/v1/$QUERY$"),
		};
			
		List<RESTResource> requestList = new ArrayList<RESTResource>();
		for(RESTResource resource : resourceArray){
			requestList.add(resource);
		}
		
		this.apiProvider = getIdentity();
		this.developerUrl = "https://www.linkedin.com/secure/developer";
		this.apiClass = org.scribe.builder.api.LinkedInApi.class;
		this.apiScopeList = null;
		this.resources = requestList;
	}
	
	@Override
	public final String getIdentity() {
		return "LinkedIn";
	}
}
