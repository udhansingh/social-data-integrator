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
package org.onesun.smc.core.listeners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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
import org.onesun.smc.api.ConnectionProperties;
import org.onesun.smc.core.connection.properties.TwitterStreamingConnectionProperties;
import org.onesun.smc.core.resources.StreamingResource;
import org.onesun.smc.core.services.handler.ConnectionHandler;
import org.onesun.smc.core.services.handler.DataHandler;

import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;

public class TwitterStreamingListener {
	private static final String ENCODING								= "UTF-8";

	private Object requestObject = null;
	private DefaultHttpClient client = null;

	private StreamingResource resource = null;
	private TwitterStreamingConnectionProperties connectionProperties = null;
	private volatile boolean running = true;
	private StreamProcessor processor = null;
	
	private DataHandler dataHandler = null;
	private ConnectionHandler connectionHandler = null;
	
	public TwitterStreamingListener(DataHandler datahandler, ConnectionHandler connectionHandler){
		this.dataHandler = datahandler;
		this.connectionHandler = connectionHandler;
	}

	public StreamingResource getResource() {
		return resource;
	}

	public void setResource(StreamingResource resource) {
		this.resource = resource;
	}

	public ConnectionProperties getConnectionProperties() {
		return connectionProperties;
	}

	public void setConnectionProperties(TwitterStreamingConnectionProperties connectionProperties) {
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

			try {
				URI uri = new URI(url);

				int port = uri.getPort();
				SchemeSocketFactory socketFactory = null;


				if(port == -1){
					if(uri.getScheme().compareToIgnoreCase("http") == 0){
						port = 80;
						socketFactory = PlainSocketFactory.getSocketFactory();
					}
					else if(uri.getScheme().compareToIgnoreCase("https") == 0){
						port = 443;
						socketFactory = SSLSocketFactory.getSocketFactory();
					}
				}

				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme(uri.getScheme(), port, socketFactory));

				ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(registry);
				client = new DefaultHttpClient(manager, params);

				client.getCredentialsProvider().setCredentials(new AuthScope(uri.getHost(), port),
						new UsernamePasswordCredentials(connectionProperties.getUsername(), connectionProperties.getPassword()));

				if(resource.getVerb().name().compareToIgnoreCase("post") == 0){
					requestObject = new HttpPost(url);// + URLEncoder.encode("?" + paramsText, "UTF-8"));
				}
				else if(resource.getVerb().name().compareToIgnoreCase("get") == 0){
					requestObject = new HttpGet(url);// + "?" + URLEncoder.encode("?" + paramsText, "UTF-8"));
				}
			} catch (MalformedURIException e) {
				e.printStackTrace();
			} 
			/*catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}*/

		}

		public void terminate(){
			running = false;
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
