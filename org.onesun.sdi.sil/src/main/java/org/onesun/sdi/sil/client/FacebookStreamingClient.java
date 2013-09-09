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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.onesun.commons.StreamUtils;
import org.onesun.sdi.core.resources.RESTParams;
import org.scribe.model.Verb;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class FacebookStreamingClient {
	private static Logger logger = Logger.getLogger(FacebookStreamingClient.class);
	
	private static int timeout = 1000 * 12;
	
	private static final String CALLBACK_CONTEXT = "/facebook/realtime_callback";
//	private static final String SUBSCRIBE_CONTEXT = "/facebook/subscribe";
	
	// TODO: set this verify token
	private static final String verifiyToken = UUID.randomUUID().toString();

	private static final String PROTOCOL = "http";
	private static final String HOST = "10.65.112.195"; // "oauth.buzzer.com";
	private static final int PORT = 8085;
	private static final String BASE_URI = PROTOCOL + "://" + HOST + ":" + PORT ;
	
	private static final String CALLBACK_URI = BASE_URI + CALLBACK_CONTEXT;
	private String accessToken = "148721421823728|902156506bd070829d58fcb7.4-1175900313|8p8VDj8CXXbp6Nvh4qEni4asXu4";
	
	private HttpServer server = null;

	public static int getTimeout() {
		return timeout;
	}
	
	public static void setTimeout(int timeout) {
		FacebookStreamingClient.timeout = timeout;
	}

	public void start(){
		// create an instance of the lightweight HTTP server on port
		try {
			server = HttpServer.create(new InetSocketAddress(PORT), 0);
		} catch (IOException e) {
			logger.error("IOException while launching HttpServer " + e.getMessage());
		}

//		server.createContext(SUBSCRIBE_CONTEXT, new HttpHandler() {
//
//			@Override
//			public void handle(HttpExchange arg0) throws IOException {
//				
//			}
//		});

		// assign a handler to callback context
		logger.info("Callback URL: " + CALLBACK_URI);
		server.createContext(CALLBACK_CONTEXT, new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {
				final String requestMethod = exchange.getRequestMethod();

				final String query = exchange.getRequestURI().getQuery();

				RESTParams urlParams = new RESTParams(query);

				OutputStream os = exchange.getResponseBody();
				Headers responseHeaders = exchange.getResponseHeaders();

				// List subscriptions
				if(requestMethod.compareToIgnoreCase(Verb.GET.name()) == 0){
					logger.info("GET Request Invoked");

					String hubMode = urlParams.getParam("hub.mode");
					String hubVerifyToken= urlParams.getParam("hub.verify_token");

					if((hubMode.compareTo("subscribe")) == 0 && (hubVerifyToken.compareTo(verifiyToken) == 0)) {
						exchange.sendResponseHeaders(200, 0);
						responseHeaders.add("ContentType", "text/plan");

						String hubChallenge = urlParams.getParam("hub.challenge");

						if(hubChallenge != null) {
							os.write(hubChallenge.getBytes());
						}
					}
				}
				// Add/Modify subscription
				else if(requestMethod.compareToIgnoreCase(Verb.POST.name()) == 0){
					logger.info("POST Request Invoked");

					// TODO: Read the stream
					InputStream is= exchange.getRequestBody();
					try {
						logger.info("Response Received: " + StreamUtils.streamToString(is));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					is.close();
				}
				// Delete subscription
				else if(requestMethod.compareToIgnoreCase(Verb.DELETE.name()) == 0){
					logger.info("DELETE Request Invoked");
				}
				// notify the app the user has authorized
				//					this.notifyAll();

				// TODO: Examine the pitfalls of closing this
				os.close();
			}
		});

		// creates a default executor
		server.setExecutor(null);
		// start the server
		logger.info("Embeded Facebook Realtime callback services started!!");
		server.start();
	}

	public void stop(){
		// user has authorized - stop the lightweight HTTP server
		if(server != null){
			server.stop(0);
		}
	}
	
	public static void main(String[] args){
		FacebookStreamingClient frc = new FacebookStreamingClient();
		frc.start();
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
