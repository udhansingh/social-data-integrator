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
package org.onesun.sdi.spi.filters.socialmedia;

import org.onesun.sdi.core.metadata.FilterMetadata;
import org.onesun.sdi.core.model.Parameter;
import org.onesun.sdi.spi.filters.AbstractFilter;

public class LinkedInFilter extends AbstractFilter {
	@Override
	public final String getIdentity() {
		return "LinkedIn";
	}
	
	public LinkedInFilter() {
		super();
	}

	@Override
	public void init() {
		FilterMetadata fm = null;
		Parameter o = null;
		
		fm = new FilterMetadata();
		this.put("People", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VALUES$", "param_values", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("People-Generic", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$ID$", "param_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("People-ID", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$URL$", "param_url", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("People-URL", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VALUES$", "param_values", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("People-Bulk", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$SEARCH_TERM$", "param_search_term", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("People-Search", fm);
		
		fm = new FilterMetadata();
		this.put("Connections", fm);

		fm = new FilterMetadata();
		o = new Parameter("$ID$", "param_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Connections-ID", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$URL$", "param_url", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Connections-URL", fm);
		
		fm = new FilterMetadata();
		this.put("Mailbox", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("count", "param_count", "500");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Network-Updates", fm);
		
		fm = new FilterMetadata();
		this.put("Person-Activities", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$KEY$", "param_key", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Network-Updates-Comments", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$KEY$", "param_key", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Network-Updates-Likes", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$KEY$", "param_key", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Network-Updates-Update-Comment", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$KEY$", "param_key", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Network-Updates-Liked", fm);
		
		fm = new FilterMetadata();
		this.put("Shares", fm);
		
		fm = new FilterMetadata();
		this.put("Current-Share", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("type", "param_type", "SHAR");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Network-Share", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("email-domain", "param_email_domain", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Companies-Email-Domain", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$NAME$", "param_name", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Companies-Name", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VALUES$", "param_values", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Companies", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("keywords", "param_keywords", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Companies-Search", fm);
		
		fm = new FilterMetadata();
		this.put("Companies-Following", fm);
		
		fm = new FilterMetadata();
		this.put("Companies-To-Follow", fm);
		
		fm = new FilterMetadata();
		this.put("Companies-Following-Post", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VALUE$", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Groups-Memberships-Create", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VALUE$", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Groups-Memberships-Delete", fm);
		
		fm = new FilterMetadata();
		this.put("Groups-Memberships", fm);
		
		fm = new FilterMetadata();
		this.put("Groups-Suggestions", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VALUE$", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Groups-Search", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VALUE$", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Groups-Posts-Fields", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$VALUE$", "param_value", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Groups-Posts", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$SOURCE$", "param_source", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$TARGET$", "param_target", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Groups-Relation-To-Viewer", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$SOURCE$", "param_source", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$TARGET$", "param_target", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Groups-Comments", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("distance", "param_distance", "10");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Jobs-Search", fm);
		
		fm = new FilterMetadata();
		this.put("Jobs-Suggestions", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$JOB$", "param_job", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Jobs", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$QUERY$", "param_query", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Generic", fm);
	}
}
