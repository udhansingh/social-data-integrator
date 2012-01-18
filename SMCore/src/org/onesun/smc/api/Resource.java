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
package org.onesun.smc.api;

import org.onesun.commons.text.format.detectors.TextFormat;
import org.scribe.model.Verb;

public interface Resource {
	void checkFormat();

	TextFormat getTextFormat();

	String getFormattedText();

	void setObject(Object object);
	Object getObject();

	String getUrl();
	
	String getResourceName();
	void setResourceName(String resourceName);

	Verb getVerb();
}
