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
package org.onesun.smc.core.services.auth;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.onesun.smc.api.SocialMediaProvider;
import org.onesun.smc.core.connection.properties.SocialMediaConnectionProperties;
import org.onesun.smc.core.model.OAuthVersion;
import org.onesun.smc.core.resources.RESTParams;
import org.scribe.builder.ServiceBuilder;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Authenticator {
	private static Logger logger = Logger.getLogger(Authenticator.class);

	private volatile Notifier	notifier			= new Notifier(); 

	private static int 			TIMEOUT 			= 1000 * 15;
	private static final Token 	EMPTY_TOKEN 		= null;
	private static String 		PROTOCOL 			= "http";
	private static String 		HOST 				= "simba.localdomain.com";
	private static int 			PORT 				= 7080;
	private static String 		CALLBACK_CONTEXT 	= "/OAuthApp/jrs/callback";
	private static String 		BASE_URI 			= PROTOCOL + "://" + HOST + ":" + PORT;
	private static String 		CALLBACK_URL 		= BASE_URI + CALLBACK_CONTEXT;

	class NotifierObject {}

	class Notifier {
		private NotifierObject object = new NotifierObject();
		private boolean wasSignalled = false;

		public void doWait(){
			synchronized(object){
				if(!wasSignalled){
					try{
						object.wait(TIMEOUT);
					} catch(InterruptedException e){
					}
				}

				//clear signal and continue running.
				wasSignalled = false;
			}
		}

		public void doNotify(){
			synchronized(object){
				wasSignalled = true;
				object.notify();
			}
		}
	}

	public static void initialize(String protocol, String host, int port, String context, int timeout){
		PROTOCOL = protocol;
		HOST = host;
		PORT = port;
		CALLBACK_CONTEXT = context;


		BASE_URI = PROTOCOL + "://" + HOST + ":" + PORT;
		CALLBACK_URL = BASE_URI + CALLBACK_CONTEXT;
	}

	public static String getCallbackurl() {
		return CALLBACK_URL;
	}

	private SocialMediaProvider provider = null;

	private volatile OAuthService service = null;
	private volatile SocialMediaConnectionProperties connection = null;

	private volatile Token requestToken = null;
	private volatile Token accessToken = null;

	private HttpServer server = null;

	public int getTimeout() {
		return TIMEOUT;
	}

	public void setTimeout(int timeout) {
		Authenticator.TIMEOUT = timeout;
	}

	public Authenticator(SocialMediaProvider provider, SocialMediaConnectionProperties connection, final int timeout){
		this(provider, connection);

		Authenticator.TIMEOUT = timeout;
	}

	public Authenticator(SocialMediaProvider provider, SocialMediaConnectionProperties connection){
		this.provider = provider;
		this.connection = connection;
	}

	private void block(){
		try {
			logger.info("Waiting for response from '" + provider + "'");
			notifier.doWait();
		} 
		catch (IllegalMonitorStateException e){
			logger.error("IllegalMonitorStateException waiting for a response from " + provider +": " + e.getMessage());
		}
	}

	private void requestAuthorization(){
		String scope = null;
		if(connection != null){
			scope = connection.scopeCSV();
		}

		if(provider.getApiClass() != null){
			String authorizationUrl = null;

			if(provider.getOAuthVersion() == OAuthVersion.VERSION_2) {
				if(provider.getApiScopeList() != null){
					service = new ServiceBuilder()
					.provider(provider.getApiClass())
					.apiKey(connection.getApiKey())
					.apiSecret(connection.getApiSecret())
					.callback(CALLBACK_URL)
					.scope(scope)
					.build();
				} else {
					service = new ServiceBuilder()
					.provider(provider.getApiClass())
					.apiKey(connection.getApiKey())
					.apiSecret(connection.getApiSecret())
					.callback(CALLBACK_URL)
					.build();
				}

				authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
			}
			else {
				if(scope != null){
					service = new ServiceBuilder()
					.provider(provider.getApiClass())
					.apiKey(connection.getApiKey())
					.apiSecret(connection.getApiSecret())
					.callback(CALLBACK_URL)
					.scope(scope)
					.build();
				}
				else {
					service = new ServiceBuilder()
					.provider(provider.getApiClass())
					.apiKey(connection.getApiKey())
					.apiSecret(connection.getApiSecret())
					.callback(CALLBACK_URL)
					.build();
				}

				requestToken = service.getRequestToken();
				authorizationUrl = service.getAuthorizationUrl(requestToken);
			}


			if(authorizationUrl != null){
				try {
					// open the authorization URI in the browser
					Desktop.getDesktop().browse(new URI(authorizationUrl));
				} catch (IOException e) {
					logger.error("IOException while lanching browser " + provider +": " + e.getMessage());
				} catch (URISyntaxException e) {
					logger.error("URISyntaxException while lanching browser " + provider +": " + e.getMessage());
				} 
			}
		}
	}

	public void authorize(){
		Authenticator tokenGenerator = null;

		try{
			tokenGenerator = new Authenticator(provider, connection, TIMEOUT);

			tokenGenerator.start();
			logger.info("HttpServer started - Requesting token generation");
			tokenGenerator.requestAuthorization();
			logger.info("Blocking for application to complete Token generation");
			tokenGenerator.block();

			// Validate
			accessToken = tokenGenerator.getAccessToken();
			service = tokenGenerator.getService();
		}
		catch(OAuthException oae){
			throw oae;
		}
		finally {
			logger.info("HttpServer stopped");
			tokenGenerator.stop();
		}
	}

	public Token getAccessToken() {
		return accessToken;
	}

	public OAuthService getService() {
		return service;
	}

	public void setAccessToken(Token accessToken) {
		this.accessToken = accessToken;
	}


	public void setService(OAuthService service) {
		this.service = service;
	}

	private void start(){
		// create an instance of the lightweight HTTP server on port
		try {
			server = HttpServer.create(new InetSocketAddress(PORT), 5);
		} catch (IOException e) {
			logger.error("IOException while launching HttpServer " + e.getMessage());
		}

		// assign a handler to callback context
		server.createContext(CALLBACK_CONTEXT, new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {
				String oauth_verifier = null;

				RESTParams urlParams = new RESTParams(exchange.getRequestURI().getQuery());

				if(provider.getOAuthVersion() == OAuthVersion.VERSION_2){
					oauth_verifier = urlParams.getParam("code");
				}
				else {
					oauth_verifier = urlParams.getParam("oauth_verifier");
				}

				Verifier verifier = new Verifier(oauth_verifier);

				if(service != null){
					accessToken = service.getAccessToken(requestToken, verifier);
					logger.info("Access Token : " + accessToken.getToken() + " Access Secret: " + accessToken.getSecret());

					exchange.sendResponseHeaders(200, 0);
					OutputStream os = exchange.getResponseBody();

					StringBuffer buffer = new StringBuffer();
					buffer.append("<html><head></head><body>");
					buffer.append("<h1>Authorization successful</h1>");
					buffer.append("<p>You have successfully authorized your application to access data on '" + provider.getIdentity() + "'</p>");
					buffer.append("<p>You may close your browser window now</p>");
					buffer.append("</body></html>");

					logger.info("HTML: " + buffer.toString());

					os.write(buffer.toString().getBytes());

					// close the streams
					os.close();
				}

				// notify the app the user has authorized
				notifier.doNotify();
				logger.info("Wakeup!!");
			}
		});

		// creates a default executor
		server.setExecutor(null);
		// start the server
		server.start();
		logger.info("Embeded OAuth callback services started!!");
	}

	private void stop(){
		// user has authorized - stop the lightweight HTTP server
		if(server != null){
			server.stop(0);
			logger.info("Embeded OAuth callback services stopped!!");
		}
	}

	public static void setCallbackUrl(String callbackUrl) {
		CALLBACK_URL = callbackUrl;
	}
}
