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

import java.io.File;

import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.commons.text.format.detectors.TextFormatDetector;

public class TestTextFormatDetector {
	public static void main(String[] args){
		final String folderName = "/Shared/test-data/";
		final String fileName = "test-data";
		
		final String[] fileExtensions = {".html", ".xml", ".json", ".rss", ".atom"};
		final TextFormatDetector dfa = new TextFormatDetector();
		
		for(String fileExtension : fileExtensions){
			final String filePath = folderName + fileName + fileExtension;

			try {
				System.out.println("Processing File: " + filePath);
				File file = new File(filePath);

				TextFormat format = dfa.checkFormat(file);

				System.out.println("Data Format discovered : " + format);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
