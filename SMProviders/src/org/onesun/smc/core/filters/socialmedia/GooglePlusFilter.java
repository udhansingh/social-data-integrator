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

public class GooglePlusFilter extends AbstractFilter {

	public GooglePlusFilter() {
		super();
	}

	@Override
	public final String getIdentity() {
		return "Google Plus";
	}
	
	@Override
	public void init() {
		FilterMetadata fm = null;
		Parameter o = null;
		
		fm = new FilterMetadata();
		o = new Parameter("$USER$", "param_user", "self");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Plus-People", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("maxResults", "param_max_results", "20");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("query", "param_query", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Plus-People-Query", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USER$", "param_user", "self");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$SCOPE$", "param_scope", "public");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("maxResults", "param_max_results", "100");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Plus-People-Activities", fm);
		
		// Activities
		fm = new FilterMetadata();
		o = new Parameter("$ACTIVITY_ID$", "param_activity_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Plus-Activities", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$USER$", "param_user", "self");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("$SCOPE$", "param_scope", "public");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("maxResults", "param_max_results", "100");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Plus-Activities-People", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("maxResults", "param_max_results", "100");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("query", "param_query", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Plus-Activities-Query", fm);
		
		// Comments
		fm = new FilterMetadata();
		o = new Parameter("$COMMENT_ID$", "param_comment_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Plus-Comments", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$ACTIVITY_ID$", "param_activity_id", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Plus-Comments-Activities", fm);
		
		
		// ---------------------------------------------------------------------------
		// Google Mail
		fm = new FilterMetadata();
		o = new Parameter("$EMAIL$", "param_email", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Mail-IMAP", fm);
		
		fm = new FilterMetadata();
		o = new Parameter("$EMAIL$", "param_email", "");
		fm.putParamObject(o.getInternalName(), o);
		this.put("Mail-SMTP", fm);
	}
}
