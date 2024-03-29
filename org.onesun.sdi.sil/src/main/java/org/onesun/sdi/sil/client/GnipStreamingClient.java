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

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.GzipDecompressingEntity;
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
import org.apache.http.protocol.HttpContext;
import org.onesun.sdi.core.api.Connection;
import org.onesun.sdi.core.model.connection.GnipConnection;
import org.onesun.sdi.sil.handler.ConnectionHandler;
import org.onesun.sdi.sil.handler.DataHandler;

import java.net.URI;
import java.net.URISyntaxException;

public class GnipStreamingClient {
	private static final String ENCODING								= "UTF-8";

	private Object requestObject = null;
	private DefaultHttpClient client = null;

	private GnipConnection connectionProperties = null;
	private volatile boolean running = true;
	private StreamProcessor processor = null;
	
	private DataHandler dataHandler = null;
	private ConnectionHandler connectionHandler = null;
	
	public GnipStreamingClient(DataHandler datahandler, ConnectionHandler connectionHandler){
		this.dataHandler = datahandler;
		this.connectionHandler = connectionHandler;
	}

	public Connection getConnectionProperties() {
		return connectionProperties;
	}

	public void setConnectionProperties(GnipConnection connectionProperties) {
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
			HttpProtocolParams.setHttpElementCharset(params, ENCODING);

			final String url = connectionProperties.getUrl();
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

				if(connectionProperties.isCompressionEnabled() == true){
					client.addRequestInterceptor(new HttpRequestInterceptor() {
						public void process(
								final HttpRequest request,
								final HttpContext context) throws HttpException, IOException {
							if (!request.containsHeader("Accept-Encoding")) {
								request.addHeader("Accept-Encoding", "gzip");
							}
						}

					});

					client.addResponseInterceptor(new HttpResponseInterceptor() {
						public void process(
								final HttpResponse response,
								final HttpContext context) throws HttpException, IOException {
							HttpEntity entity = response.getEntity();

							if (entity != null) {
								Header ceheader = entity.getContentEncoding();
								if (ceheader != null) {
									HeaderElement[] codecs = ceheader.getElements();
									for (int i = 0; i < codecs.length; i++) {
										if (codecs[i].getName().equalsIgnoreCase("gzip")) {
											response.setEntity(
													new GzipDecompressingEntity(response.getEntity()));
											return;
										}
									}
								}
							}
						}

					});
				}
	            
				client.getCredentialsProvider().setCredentials(new AuthScope(uri.getHost(), port),
						new UsernamePasswordCredentials(connectionProperties.getUsername(), connectionProperties.getPassword()));

				String verb = "get";
				if(verb.compareToIgnoreCase("post") == 0){
					requestObject = new HttpPost(url);
				}
				else if(verb.compareToIgnoreCase("get") == 0){
					requestObject = new HttpGet(url);
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
						resp = client.execute(get);
					}
					else if(requestObject instanceof HttpPost) {
						HttpPost post = (HttpPost)requestObject;
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

		private BufferedReader reader = null;
		public void processStream(InputStream stream){
			if(reader == null && stream != null){
				try {
					InputStreamReader isr = new InputStreamReader(stream, ENCODING);
					reader = new BufferedReader(isr);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}				

			while(running){
				try {
					if(reader != null && dataHandler != null){
						String text = reader.readLine();
						
						if(text != null && text.length() > 0){
							dataHandler.flush(text);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
