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
package org.onesun.commons.text.classification.uclassify;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.scribe.model.Request;
import org.scribe.model.Response;
import org.scribe.model.Verb;

public class UClassifyClient {
	private final static Logger logger = Logger.getLogger(UClassifyClient.class);
	
	private static String uClassifyReadAccessKey = null;
	
	public static void setReadAccessKey(String uClassifyReadAccessKey){
		UClassifyClient.uClassifyReadAccessKey = uClassifyReadAccessKey;
	}
	
	private String text = null;
	private Classifier service = null;
	private UClassifyClassifier classifier = null;

	public UClassifyClient(String text, Classifier service, UClassifyClassifier classifier) {
		super();
		
		this.text = text;
		this.service = service;
		
		this.classifier = classifier;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private String buildServiceURL(){
		try {
			String encodedText = URLEncoder.encode(text, ("UTF-8"));
			
			return ( 
				service.getUrl() 
				+ "/" 
				+ service.getClassifier() 
				+ "/ClassifyText?readkey=" 
				+ uClassifyReadAccessKey 
				+ "&text=" 
				+ encodedText 
				+ "&output=" 
				+ "XML"
			);
			
		}catch (UnsupportedEncodingException e){
			logger.info("Charset UTF-8 unsupported");
		}
		finally {
		}
		
		return null;
	}
	
	public void execute() throws Exception {
		if(uClassifyReadAccessKey == null || uClassifyReadAccessKey.length() <= 0){
			throw new Exception("uClassify API Read Key undefined");
		}
		
		String url = buildServiceURL();

		Request request = new Request(Verb.POST, url);
		
		Response response = request.send();
		
		String responseBody = response.getBody();
		
		ResponseHandler responseHandler = new DefaultResponseHandler(responseBody);
		responseHandler.processResponse();
		
		if(classifier != null){
			classifier.classify(service, responseHandler.getResponseMap());
		}
	}
}
