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
package org.onesun.smc.core.providers.web.connotate;

import org.onesun.smc.core.providers.web.AbstractWebProvider;
import org.onesun.smc.core.resources.WebResource;


public class ConnotateProvider extends AbstractWebProvider {
	@Override
	public void init() {
	}
	
	@Override
	public void refreshMetadata(){
/*		ConnotateClient client = new ConnotateClient(c.getUrl());
		boolean status = client.login(c.getUsername(), c.getPassword());

		if(status == true){
			ArrayOfSubscription subscriptions = client.getAllWebServicesSubscriptions();
			
			resources.clear();
			
			for(Subscription subscription : subscriptions.getSubscription()){
				System.out.println("Subscription NAME: " + subscription.getName() + " ID: " + subscription.getSubscriptionID());
				resources.add(new WEBRequest(subscription.getName(), subscription));
			}
		}
		
		// DO NOT FORGET TO LOGOUT
		client.logout();*/
	}
	
	@Override
	public String execute(WebResource resource){
		String responseBody = null;
/*		
		Subscription subscription = (Subscription)resource.getObject();
		
		System.out.println("Executing request for: " + subscription.getName() + " ID: " + subscription.getSubscriptionID());
		ConnotateClient client = new ConnotateClient(c.getUrl());
		boolean status = client.login(c.getUsername(), c.getPassword());

		if(status == true){
			CTWebService ctwService = client.newCTWebService(subscription.getSubscriptionID());

			if(ctwService != null){
				CTWebServiceSoap ctwServiceSoap = ctwService.getCTWebServiceSoap();

				if(ctwServiceSoap != null){
					try{
						responseBody = ctwServiceSoap.run();
					} catch(WebServiceException ex){
						ex.printStackTrace();
					}
				}
			}
		}

		// DO NOT FORGET TO LOGOUT
		client.logout();
	*/	
		return responseBody;
	}

	@Override
	public void validate() {
	}
	
	@Override
	public String getIdentity() {
		return "Connotate";
	}
	
	@Override
	public String getCategory() {
		return "CONNOTATE";
	}
}
