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
package org.onesun.commons.text.classification;

import org.apache.commons.lang3.StringEscapeUtils;


public class TextClassificaionHelper {
	private static String removeHtmlTags(String text){
		text = StringEscapeUtils.unescapeHtml4(text);
		text = text.replaceAll("\\<.*?\\>", "");
		return text;
	}
	
	public static String cleanUpAll(String text){
		text = removeHtmlTags(text);
		return text;
	}
}
