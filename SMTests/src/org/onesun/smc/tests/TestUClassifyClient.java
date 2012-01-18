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

import java.util.Map;

import org.onesun.commons.text.classification.uclassify.Classifier;
import org.onesun.commons.text.classification.uclassify.UClassifyClassifier;
import org.onesun.commons.text.classification.uclassify.UClassifyClient;

public class TestUClassifyClient {
	public static void main(String[] args) {
		for(Classifier service : Classifier.values()){
			String text = 
				"A new survey has been launched in the United Kingdom to unearth the true nature of cyber stalking in the country." 
				+ "\n"
				+ "The Network for Surviving Stalking has issued an \"Electronic Communication Harassment Observation\" or ECHO questionnaire in collaboration with the scientists at the University of Bedfordshire."
				+ "\n"
				+ "The survey has been commissioned to classify those who have been stalked on web and how according to a number of criteria." 
				+ "\n"
				+ "The questionnaire will ask respondents if they were harassed or threatened on a social networking site such as Facebook, Twitter and LinkedIn, email service or Instant Messaging."
				+ "\n"
				+ "\"At the moment there are very few widely agreed guidelines or rules about how to behave online - we hope Echo will define behaviours that are generally experienced as anti-social or likely to cause distress in online communication.\" said Dr. Emma Short, head of the project ECHO."
				+ "\n"
				+ "The survey has been launched after Crown Prosecution Service (CPS) of the UK revealed a set of new guidelines for law enforcers tough on stalkers on web."
				+ "\n"
				+ "Read more: http://www.itproportal.com/security/news/article/2010/9/25/study-reveal-nature-cyberstalking-uk/#ixzz10YckSmCr";
			
			
			// *******************************************************************
			// DO NOT FORGET TO SET YOUR OWN KEY HERE BEFORE RUNNING APP
			// You can get a key from: http://www.uclassify.com/Register.aspx
			// *******************************************************************
			UClassifyClient.setReadAccessKey("4YYLXtnmPyE4TfOSJQFz14CBi0w");
			// *******************************************************************
			
			UClassifyClient uClassifyService = new UClassifyClient(text, service, new UClassifyClassifier() {
				
				@Override
				public void classify(Classifier serviceType, Map<String, Double> results) {
					System.out.println(
							"---------------------------------------------------------------------\n" 
							+
							serviceType.getUrl() + " <<<>>> " + serviceType.getClassifier() + "\n" +
							"---------------------------------------------------------------------\n"
						);
					
					for(String key : results.keySet()){
						Double result = results.get(key);
						
						// interested in match >= 25%
						if(result >= 25) System.out.format("%1$-50s %2$10.2f\n", key, result);
					}
				}
			});
			
			try{
				uClassifyService.execute();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
}
