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

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.onesun.commons.webbrowser.WebBrowser;
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

public class OAuthenticator {
	private static Logger logger = Logger.getLogger(OAuthenticator.class);

	private volatile Notifier	notifier			= new Notifier(); 

	private static int 			TIMEOUT 			= 1000 * 15;
	private static final Token 	EMPTY_TOKEN 		= null;
	private static String 		PROTOCOL 			= "http";
	private static String 		HOST 				= "simba.localdomain.com";
	private static int 			PORT 				= 7080;
	private static String 		CALLBACK_CONTEXT 	= "/OAuthApp/jrs/callback";
	private static String 		BASE_URI 			= PROTOCOL + "://" + HOST + ":" + PORT;
	private static String 		CALLBACK_URL 		= BASE_URI + CALLBACK_CONTEXT;

	private static WebBrowser	webBrowser			= null;
	
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

	public static String initialize(String protocol, String host, int port, String context, int timeout){
		PROTOCOL = protocol;
		HOST = host;
		PORT = port;
		CALLBACK_CONTEXT = context;


		BASE_URI = PROTOCOL + "://" + HOST + ":" + PORT;
		CALLBACK_URL = BASE_URI + CALLBACK_CONTEXT;
		
		return CALLBACK_URL;
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
		OAuthenticator.TIMEOUT = timeout;
	}

	public OAuthenticator(SocialMediaProvider provider, SocialMediaConnectionProperties connection, final int timeout){
		this(provider, connection);

		OAuthenticator.TIMEOUT = timeout;
	}

	public OAuthenticator(SocialMediaProvider provider, SocialMediaConnectionProperties connection){
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
			scope = connection.toScopeCSV();
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
					if(webBrowser != null){
						webBrowser.browse(authorizationUrl);
					}
				} catch (IOException e) {
					logger.error("IOException while lanching browser " + provider +": " + e.getMessage());
				} catch (URISyntaxException e) {
					logger.error("URISyntaxException while lanching browser " + provider +": " + e.getMessage());
				} 
			}
		}
	}

	public void authorize(){
		try{
			start();
			logger.info("HttpServer started - Requesting token generation");
			requestAuthorization();
			logger.info("Blocking for application to complete Token generation");
			block();

			// Validate
			accessToken = getAccessToken();
			service = getService();
		}
		catch(OAuthException oae){
			throw oae;
		}
		finally {
			logger.info("HttpServer stopped");
			stop();
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

	public boolean getAccessKeys(String url){
		String oauth_verifier = null;

		RESTParams urlParams = new RESTParams(url);

		if(provider.getOAuthVersion() == OAuthVersion.VERSION_2){
			oauth_verifier = urlParams.getParam("code");
		}
		else {
			oauth_verifier = urlParams.getParam("oauth_verifier");
		}
		
		Verifier verifier = null;
		try {
			verifier= new Verifier(oauth_verifier);
		}
		catch(IllegalArgumentException e){
			logger.info("Exception while extracting verifier: " + e.getMessage());
		}

		if(service != null && verifier != null){
			accessToken = service.getAccessToken(requestToken, verifier);
			logger.info("Access Token : " + accessToken.getToken() + " Access Secret: " + accessToken.getSecret());
			//notifier.doNotify();
			
			return true;
		}
		
		return false;
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
				logger.info("Callback Handers processing request: " + exchange.getRequestURI());
				// getAccessKeys(exchange.getRequestURI().getQuery());

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

	public static WebBrowser getWebBrowser() {
		return webBrowser;
	}

	public static void setWebBrowser(WebBrowser webBrowser) {
		OAuthenticator.webBrowser = webBrowser;
	}
}
