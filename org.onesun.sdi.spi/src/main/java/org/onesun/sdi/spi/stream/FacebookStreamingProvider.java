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
package org.onesun.sdi.spi.stream;

import java.util.ArrayList;
import java.util.List;

import org.onesun.sdi.core.resources.StreamingResource;
import org.scribe.model.Verb;

public class FacebookStreamingProvider extends AbstractStreamingProvider {
	@Override
	public void init() {
		serviceProviderName = getIdentity().toLowerCase();
		
		StreamingResource[] resourceArray = {
			new StreamingResource(Verb.POST, "http://localhost:8080/facebook_stream")
		};

		List<StreamingResource> resourseList = new ArrayList<StreamingResource>();
		for(StreamingResource resource : resourceArray){
			resourseList.add(resource);
		}
		
		this.resources = resourseList;
	}
	
	@Override
	public final String getIdentity() {
		return "Facebook Streaming";
	}
	
	@Override
	public String getAuthentication() {
		return "FACEBOOK_STREAMING";
	}
	
	@Override
	public String getCategory() {
		return "FACEBOOK_STREAMING";
	}
}
