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

public class TwitterStreamingProvider extends AbstractStreamingProvider {
	@Override
	public void init() {
		serviceProviderName = getIdentity().toLowerCase();
		
		List<StreamingResource> resourseList = new ArrayList<StreamingResource>();

		StreamingResource resource = null;
		
		resource = new StreamingResource(Verb.POST, "https://stream.twitter.com/1/statuses/filter.json");
		resource.setResourceName("Filter");
		resourseList.add(resource);

		resource = new StreamingResource(Verb.GET, "https://stream.twitter.com/1/statuses/firehose.json");
		resource.setResourceName("FireHose");
		resourseList.add(resource);

//		resource = new StreamingResource(Verb.POST, "https://stream.twitter.com/1/statuses/links.json");
//		resource.setResourceName("Links");
//		resourseList.add(resource);
//		
//		resource = new StreamingResource(Verb.POST, "https://stream.twitter.com/1/statuses/retweet.json");
//		resource.setResourceName("Retweet");
//		resourseList.add(resource);
		
		resource = new StreamingResource(Verb.GET, "https://stream.twitter.com/1/statuses/sample.json");
		resource.setResourceName("Sample");
		resourseList.add(resource);
		
		this.resources = resourseList;
	}
	
	@Override
	public final String getIdentity() {
		return "Twitter Streaming";
	}

	@Override
	public String getAuthentication() {
		return "TWITTER_STREAMING";
	}

	@Override
	public String getCategory() {
		return "TWITTER_STREAMING";
	}
}
