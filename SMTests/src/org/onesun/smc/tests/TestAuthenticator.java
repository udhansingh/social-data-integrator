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
package org.onesun.smc.tests;

import org.onesun.smc.api.ProviderFactory;
import org.onesun.smc.api.SocialMediaProvider;
import org.onesun.smc.core.connection.properties.SocialMediaConnectionProperties;
import org.onesun.smc.core.services.auth.Authenticator;
import org.scribe.model.Token;

public class TestAuthenticator {
	public static void main(String[] args){
		final String APP_NAME = "tg";
		
		// Obtain AccessKey
		if(args.length >= 3){
			String providerName = args[0].trim();
			String key = args[1].trim();
			String secret = args[2].trim();

			String scope = null;

			if(args.length == 4) {
				scope = args[3].trim();
			}
			int timeout = 1000 * 12;
			
			if(args.length == 5){
				timeout = Integer.parseInt(args[4].trim());
			}

			System.out.println("app = " + providerName + "\n" +
					"key = " + key + "\n" +
					"secret = " + secret + "\n" +
					"timeout = " + timeout);

			SocialMediaConnectionProperties cp = new SocialMediaConnectionProperties();
			cp.setApiKey(key);
			cp.setApiSecret(secret);
			cp.setIdentity(providerName);
			cp.toScopeList(scope);
			
			SocialMediaProvider provider = (SocialMediaProvider)ProviderFactory.getProvider(providerName.toLowerCase());
			
			if(provider == null) return;
			
			Authenticator authenticator = new Authenticator(provider, cp, timeout);
			authenticator.authorize();

			Token accessToken = authenticator.getAccessToken();
			if(accessToken != null){
				System.out.println("Access Token: " + accessToken.getToken());
				System.out.println("Access Secret: " + accessToken.getSecret());
			}
		}
		// Show Usage
		else {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Incorrect usage of this application!!!\n"); 
			buffer.append("USAGE: " + APP_NAME + " <provider> <key> <secret> [scope] [timeout]\n"); 
			buffer.append("EXAMPLE: " + APP_NAME + " twitter YOUR_TWITTER_API_KEY YOUR_TWITTER_API_SECRET\n");
			buffer.append("\t " + APP_NAME + " facebook YOUR_FACEBOOK_API_KEY YOUR_FACEBOOK_API_SECRET read_stream,user_status,friends_status,offline_access\n");
			
			buffer.append("\n");

			buffer.append("Supported applications\n"); 
			for(String key : ProviderFactory.getNames()){
				buffer.append("* " + key + "\n");
			}

			System.out.println(buffer.toString());
			System.exit(1);
		}
	}
}
