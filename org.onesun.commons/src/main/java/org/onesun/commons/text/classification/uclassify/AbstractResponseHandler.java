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

import java.util.Map;


public abstract class AbstractResponseHandler implements ResponseHandler {
	public AbstractResponseHandler(String responseText){
		this.responseText = responseText;
	}
	
	protected String responseText = null;
	protected Map<String, Double> responseMap = null;
	
	public String getResponseText() {
		return responseText;
	}

	public void setResultText(String resultText) {
		this.responseText = resultText;
	}

	@Override
	public Map<String, Double> getResponseMap() {
		return responseMap;
	}
}
