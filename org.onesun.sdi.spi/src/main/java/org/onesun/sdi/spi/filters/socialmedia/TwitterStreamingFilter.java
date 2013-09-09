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

public class TwitterStreamingFilter extends AbstractFilter {
	@Override
	public final String getIdentity() {
		return "Twitter Streaming";
	}
	
	public TwitterStreamingFilter() {
		super();
	}

	@Override
	public void init() {
		FilterMetadata fm = null;
		Parameter o = null;
		
		fm = new FilterMetadata();
		o = new Parameter("count", "param_count", "0");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("delimited", "param_delimited", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("follow", "param_follow", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("locations", "param_locations", "");
		fm.putParamObject(o.getInternalName(), o);
		o = new Parameter("track", "param_track", "<TOPIC>");
		fm.putParamObject(o.getInternalName(), o);
		this.put("statuses/filter", fm);
	}
	
	@Override
	public FilterMetadata get(String key) {
		for(String k : filters.keySet()){
			if(k.contains(key.toLowerCase())){
				return filters.get(k);
			}
		}
		
		return null;
	}
}
