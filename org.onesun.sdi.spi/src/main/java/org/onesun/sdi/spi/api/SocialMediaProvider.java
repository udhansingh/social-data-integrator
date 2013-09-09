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
package org.onesun.sdi.spi.api;

import java.util.List;

import org.onesun.sdi.core.api.ServiceProvider;
import org.onesun.sdi.core.model.OAuthVersion;
import org.onesun.sdi.core.resources.RESTResource;
import org.scribe.builder.api.Api;

// TODO: Refer to https://apigee.com/console/ for exploring API
public interface SocialMediaProvider extends ServiceProvider {
	String getDeveloperUrl();
	Class<? extends Api> getApiClass();
	List<String> getApiScopeList();
	List<RESTResource> getResources();
	List<RESTResource> getResourcesByScope(String scope);
	OAuthVersion getOAuthVersion();
}