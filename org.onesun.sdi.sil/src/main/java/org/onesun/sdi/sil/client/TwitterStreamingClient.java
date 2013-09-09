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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.log4j.Logger;
import org.onesun.sdi.core.api.Connection;
import org.onesun.sdi.core.model.connection.TwitterStreamingConnection;
import org.onesun.sdi.core.resources.StreamingResource;
import org.onesun.sdi.sil.handler.ConnectionHandler;
import org.onesun.sdi.sil.handler.DataHandler;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

public class TwitterStreamingClient {
	private static Logger logger = Logger.getLogger(TwitterStreamingClient.class);
	private static final String ENCODING								= "UTF-8";

	private Object requestObject = null;
	private DefaultHttpClient client = null;

	private StreamingResource resource = null;
	
	private String connectionType = null;
	
	private Connection connectionProperties = null;
	
	private volatile boolean running = true;
	private StreamProcessor processor = null;
	
	private DataHandler dataHandler = null;
	private ConnectionHandler connectionHandler = null;
	
	public TwitterStreamingClient(String connectionType, DataHandler datahandler, ConnectionHandler connectionHandler){
		this.connectionType = connectionType;
		this.dataHandler = datahandler;
		this.connectionHandler = connectionHandler;
	}

	public StreamingResource getResource() {
		return resource;
	}

	public void setResource(StreamingResource resource) {
		this.resource = resource;
	}

	public Connection getConnectionProperties() {
		return connectionProperties;
	}

	public void setConnectionProperties(TwitterStreamingConnection connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

	public void start() {
		processor = new StreamProcessor();
		processor.start();
	}

	public void stop(){
		processor.terminate();
	}

	private class StreamProcessor extends Thread{
		private HttpParams params = new BasicHttpParams();
		private InputStream stream = null;

		public StreamProcessor(){
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, ENCODING);

			String parameters = resource.getParameters();
			String url = resource.getUrl();
			if(parameters != null && parameters.trim().length() > 0){
				if(url.contains("?")){
					parameters = parameters.replace("?", "&");
				}
				
				url += parameters;
			}

			OAuthRequest request = null;
			String usableUrl = new String(url);
			try {
				URI uri = new URI(url);

				int port = uri.getPort();
				SchemeSocketFactory socketFactory = null;


				if(uri.getScheme().compareToIgnoreCase("http") == 0){
					port = 80;
					socketFactory = PlainSocketFactory.getSocketFactory();
				}
				else if(uri.getScheme().compareToIgnoreCase("https") == 0){
					port = 443;
					socketFactory = SSLSocketFactory.getSocketFactory();
				}

				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme(uri.getScheme(), port, socketFactory));

				ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(registry);
				client = new DefaultHttpClient(manager, params);

				if(connectionType != null && connectionType.compareToIgnoreCase("BASIC") == 0){
					TwitterStreamingConnection cp =  (TwitterStreamingConnection)connectionProperties;
					
					client.getCredentialsProvider().setCredentials(new AuthScope(uri.getHost(), port),
						new UsernamePasswordCredentials(cp.getUsername(), cp.getPassword()));
				} else if(connectionType != null && connectionType.compareToIgnoreCase("OAUTH") == 0){
					// SocialMediaConnectionProperties cp = (SocialMediaConnectionProperties)connectionProperties;
					
					request = new OAuthRequest(resource.getVerb(), url);
					
					OAuthService service = new ServiceBuilder()
                    .provider(TwitterApi.class)
                    //.apiKey(cp.getApiKey())
                    //.apiSecret(cp.getApiSecret())
                    .apiKey("O0DVGQLZ8LoIlW44mt5A")
                    .apiSecret("Zr0zShDB6ukUnflchWkDfTedRrpH6Btv16dYKyJMMzI")
                    .build();
					
					//Token accessToken = new Token(cp.getAccessToken(), cp.getAccessSecret());
					Token accessToken = new Token("29169735-aLyWSq9tTXkSBjnaTgq2XXMlsHMMPzf9OPj3LCdvz", "RVAger87Ihd8VTuZ2thOYKYcqIoxMj9nCrt4X5KFF4c");
					service.signRequest(accessToken, request);

//					Map<String, String> oauthParameters = request.getOauthParameters();
//					for(String key : oauthParameters.keySet()){
//						if(usableUrl.contains("?")){
//							usableUrl += "&" + key + "=" + oauthParameters.get(key);
//						}
//						else {
//							usableUrl += "?" + key + "=" + oauthParameters.get(key);
//						}
//					}
				}
				
				logger.info("URL to connect: " + usableUrl);

				if(resource.getVerb().name().compareToIgnoreCase("post") == 0){
					HttpPost post = new HttpPost(usableUrl);
					
					if(request != null){
						// Add headers
						Map<String, String> headers = request.getHeaders();
						for(String key : headers.keySet()){
							post.addHeader(key, headers.get(key));
						}
					}
					
					
					requestObject = post; // + URLEncoder.encode("?" + paramsText, "UTF-8"));
				}
				else if(resource.getVerb().name().compareToIgnoreCase("get") == 0){
					HttpGet get = new HttpGet(usableUrl);
					
					if(request != null){
						// Add headers
						Map<String, String> headers = request.getHeaders();
						for(String key : headers.keySet()){
							get.addHeader(key, headers.get(key));
						}
					}
					
					requestObject = get; // + "?" + URLEncoder.encode("?" + paramsText, "UTF-8"));
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}

		public void terminate(){
			running = false;
			client.getConnectionManager().shutdown();
		}

		@Override 
		public void run(){
			running = true;

			try { 
				HttpResponse resp = null;
				
				// TODO Yet to find how to send request content (a.k.a body)
				if(requestObject != null && client != null){
					if(requestObject instanceof HttpGet) {
						HttpGet get = (HttpGet)requestObject;
						Map<String, String> headers = resource.getHeaders();
						if(headers != null){
							for(String key : headers.keySet()){
								get.addHeader(key, headers.get(key));
							}
						}
						
						
						resp = client.execute(get);
					}
					else if(requestObject instanceof HttpPost) {
						HttpPost post = (HttpPost)requestObject;
						Map<String, String> headers = resource.getHeaders();
						if(headers != null){
							for(String key : headers.keySet()){
								post.addHeader(key, headers.get(key));
							}
						}
						
						resp = client.execute(post);
					}
				}

				if(resp != null){
					int statusCode = resp.getStatusLine().getStatusCode();

					connectionHandler.setStatusCode(statusCode);
					
					if (statusCode == 200) {
						stream = resp.getEntity().getContent();

						processStream(stream);
					}
					else {
						dataHandler.flush(resp.getStatusLine().getReasonPhrase());
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}

		public void processStream(InputStream stream){
			if(stream != null){
				BufferedReader reader = null;

				try {
					InputStreamReader isr = new InputStreamReader(stream, ENCODING);
					reader = new BufferedReader(isr);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				while(running){
					try {
						String text = reader.readLine();
						dataHandler.flush(text);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
