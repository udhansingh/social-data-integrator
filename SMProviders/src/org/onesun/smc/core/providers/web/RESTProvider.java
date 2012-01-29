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
package org.onesun.smc.core.providers.web;


import org.onesun.smc.api.ServiceProvider;

public class RESTProvider implements ServiceProvider {

	@Override
	public String getCategory() {
		return "GENERAL";
	}

	@Override
	public String getIdentity() {
		return "General";
	}

	@Override
	public boolean isResponseRequired() {
		return true;
	}

	@Override
	public String getAuthentication() {
		return "REST";
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean save(String pathToExports) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean load(String pathToExports) {
		// TODO Auto-generated method stub
		return false;
	}
}
