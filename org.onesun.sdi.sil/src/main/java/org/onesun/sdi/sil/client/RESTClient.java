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
package org.onesun.sdi.sil.client;

import java.util.Map;

import org.apache.log4j.Logger;
import org.onesun.sdi.core.api.Connection;
import org.onesun.sdi.core.api.ServiceProvider;
import org.onesun.sdi.core.model.connection.RESTConnection;
import org.onesun.sdi.core.model.connection.OAuthConnection;
import org.onesun.sdi.core.resources.RESTResource;
import org.onesun.sdi.spi.api.SocialMediaProvider;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.BasicRequest;
import org.scribe.model.Request;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.UsernamePasswordCredentials;
import org.scribe.oauth.OAuthService;

public class RESTClient {
	private static Logger logger = Logger.getLogger(RESTClient.class);

	private RESTResource resource = null;
	private String authentication = "NONE";

	private Connection connection = null;
	private OAuthService service = null;
	private Token accessToken = null;

	private Integer responseCode = -1;
	private String responseBody = null;
	private ServiceProvider serviceProvider = null;

	public RESTClient(ServiceProvider serviceProvider, RESTResource resource, String authentication){
		// Generic
		this.serviceProvider = serviceProvider;
		this.resource = resource;
		this.authentication = authentication;
	}

	public void execute(){
		// Substitute URL parameters if required
		String parameters = resource.getParameters();
		String url = resource.getUrl();

		if(parameters != null && parameters.trim().length() > 0){
			if(url.contains("?")){
				parameters = parameters.replace("?", "&");
			}

			url += parameters;
		}

		Request request = null;

		// Sign with access token if required
		if((authentication.compareTo("BASIC") == 0)){
			if(connection != null){
				RESTConnection restConnection = (RESTConnection)connection;
				UsernamePasswordCredentials upc = new UsernamePasswordCredentials(restConnection.getUsername(), restConnection.getPassword());
				request = new BasicRequest(resource.getVerb(), url, upc);
			}
		}
		else if(authentication.compareTo("OAUTH") == 0  && resource.isAccessTokenRequired() == true){
			request = new OAuthRequest(resource.getVerb(), url);

			// Assume that authorizer will not be null as it must authenticated
			// A new request was made

			// Strings loaded from file
			if(connection != null){
				OAuthConnection oauthConnection = (OAuthConnection)connection;

				accessToken = new Token(oauthConnection.getAccessToken(), oauthConnection.getAccessSecret());

				if(serviceProvider.getAuthentication().compareTo("OAUTH") == 0){
					SocialMediaProvider oauthServiceProvider = (SocialMediaProvider)serviceProvider;

					String scope = oauthConnection.toScopeCSV();
					if(scope != null){
						service = new ServiceBuilder()
						.provider(oauthServiceProvider.getApiClass())
						.apiKey(oauthConnection.getApiKey())
						.apiSecret(oauthConnection.getApiSecret())
						.scope(scope)
						.build();
					}
					else {
						service = new ServiceBuilder()
						.provider(oauthServiceProvider.getApiClass())
						.apiKey(oauthConnection.getApiKey())
						.apiSecret(oauthConnection.getApiSecret())
						.build();
					}
				}
			}

			if(service != null){
				service.signRequest(accessToken, (OAuthRequest)request);
			}
		}
		else {
			request = new Request(resource.getVerb(), url);
		}

		try{
			// Process Request Headers
			Map<String, String> headers = resource.getHeaders();
			if(headers != null){
				for(String key : headers.keySet()){
					request.addHeader(key, headers.get(key));
				}
			}

			// Process Request Payload
			String payload = resource.getPayload();
			if(payload != null && payload.trim().length() > 0){
				request.addPayload(payload);
			}

			logger.info("Executing Request: " + url);

			// make a request
			Response response = null;
			
			if(authentication.compareTo("BASIC") == 0) {
				response = ((BasicRequest)request).send();
			} else if(authentication.compareTo("OAUTH") == 0) {
				try {
					response = ((OAuthRequest)request).send();
				} catch (ClassCastException e){
					response = ((Request)request).send();
				}
			} else {
				response = request.send();
			}

			if(response != null) {
				// record response code
				responseCode = response.getCode();

				// record response body
				responseBody = (response != null) ? response.getBody() : null;

				if(responseBody != null){
					logger.info("Response\n" + responseBody + "\n");
				}
			}
		}catch(Exception e){
			logger.error("Exception occured during HTTP Send: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setOAuthService(OAuthService oauthService) {
		this.service = oauthService;
	}

	public void setAccessToken(Token accessToken) {
		this.accessToken = accessToken;
	}
}