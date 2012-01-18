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
package org.onesun.commons.text.classification.opencalais;

import java.io.IOException;

import org.onesun.commons.text.classification.TextClassificaionHelper;
import org.scribe.model.Request;
import org.scribe.model.Response;
import org.scribe.model.Verb;

public class OpenCalaisClient {

	private static final String CALAIS_URL = "http://api.opencalais.com/tag/rs/enrich";
	private static String licenseKey = null;
	
	public static void setLicenseKey(String licenseKey){
		OpenCalaisClient.licenseKey = licenseKey;
	}

	public OpenCalaisClient() {
	}
	
	public String execute(String text) throws IOException {
		text = TextClassificaionHelper.cleanUpAll(text);

		Request request = new Request(Verb.POST, CALAIS_URL);

		request.addHeader("x-calais-licenseID", licenseKey);
		request.addHeader("Content-Type", "text/raw; charset=UTF-8");
		request.addHeader("Accept", "xml/rdf");
		request.addHeader("enableMetadataType", "GenericRelations,SocialTags");
		request.addHeader("outputFormat", "XML/RDF");

		request.addPayload(text);

		Response response = request.send();

		if (response != null) {
			return response.getBody();
		}

		return null;
	}
}