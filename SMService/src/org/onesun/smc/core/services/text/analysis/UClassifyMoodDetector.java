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
package org.onesun.smc.core.services.text.analysis;

import java.util.Map;

import org.onesun.commons.text.classification.uclassify.Classifier;
import org.onesun.commons.text.classification.uclassify.UClassifyClassifier;
import org.onesun.commons.text.classification.uclassify.UClassifyClient;
import org.onesun.smc.core.services.data.AbstractDataService;

public class UClassifyMoodDetector extends AbstractDataService {
	@Override
	protected void process(Map<String, String> datum, String text) {
		UClassifyClient client = new UClassifyClient(text, Classifier.MOOD, 
				new MoodProcessor(datum, columnName));

		// TODO: Throttle this request
		try {

			client.execute();
			// Assume 4 request per second
			Thread.sleep(1000/4);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected class MoodProcessor implements UClassifyClassifier {
		private Map<String, String> data;
		private String columnName;
		
		public MoodProcessor(Map<String, String> data, String columnName){
			this.data = data;
			this.columnName = columnName;
		}

		@Override
		public void classify(Classifier serviceType, Map<String, Double> results) {
			data.put(columnName, results.toString());
		}
	}
}
