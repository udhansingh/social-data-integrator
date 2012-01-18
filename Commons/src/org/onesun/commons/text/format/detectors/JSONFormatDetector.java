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
package org.onesun.commons.text.format.detectors;

import java.io.IOException;
import java.io.InputStream;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class JSONFormatDetector extends AbstractFormatDetector {
	private JsonFactory jsonFactory = new JsonFactory();
	
	@Override
	protected void process(InputStream is) throws JsonParseException, IOException {
		format = TextFormat.UNKNOWN;
		
		if(is != null){
			// Use Jackson parser to validate the document
			try {
				JsonParser parser = jsonFactory.createJsonParser(is);
				
				JsonToken token = parser.nextToken();
				
				if(token != null && (token == JsonToken.START_ARRAY || token == JsonToken.START_OBJECT)){
					format = TextFormat.JSON;
				}
			} finally {
			}
		}
	}
}
