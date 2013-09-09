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
package org.onesun.sdi.sil.textanalysis;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.onesun.commons.text.classification.opencalais.OpenCalaisClassifier;
import org.onesun.commons.text.classification.opencalais.OpenCalaisClient;
import org.onesun.commons.text.classification.opencalais.OpenCalaisDocument;

public class OpenCalaisEntityExtractor extends AbstractTextAnalysisService {
	@Override
	public String getIdentity() {
		return "OpenCalais Entity Extractor";
	}
	
	@Override
	protected void process(Map<String, String> datum, String text) {
		// TODO: Throttle this request
		// OpenCalaiss does 4 request per second.
		try {
			OpenCalaisClient client = new OpenCalaisClient();
			
			String rdfText = client.execute(text);

			OpenCalaisDocument ocd = new OpenCalaisDocument();
			rdfText = ocd.trim(rdfText); 

			if (! rdfText.contains("Calais Backend-Server is Busy. Please try again later.")){
				OpenCalaisClassifier occ = new OpenCalaisClassifier();
				occ.classifyDocument(rdfText);

				Map<String, List<String>> entities = occ.getEntities();

				StringBuffer buffer = new StringBuffer();
				for(String entityType : entities.keySet()){
					buffer.append(String.format("%s:%s\n", entityType, entities.get(entityType)));
				}

				datum.put("opencalais_entities", buffer.toString());
			}

			// http://www.opencalais.com/documentation/calais-web-service-api/usage-quotas
			// 4 request per second is allowed by OpenCalais
			
			// 1000 milliseconds = 1 second
			Thread.sleep(1000/4);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}  catch (Exception e) {
			e.printStackTrace();
		}
		finally{
		}
	}
}
