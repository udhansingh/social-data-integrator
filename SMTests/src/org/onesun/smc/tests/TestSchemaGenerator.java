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
import java.io.FileNotFoundException;
import java.io.IOException;

import org.onesun.commons.StreamUtils;
import org.onesun.commons.text.format.detectors.TextFormat;
import org.onesun.commons.xml.SchemaGenerator;

public class TestSchemaGenerator {
	public static void main(String[] args){
		final String filePath = "/Shared/test-data/test-data.json";
		final File file = new File(filePath);
		
		try {
			final String document = StreamUtils.streamToString(file);
			SchemaGenerator sg = new SchemaGenerator(document, TextFormat.JSON);
			
			sg.generateSchema();

			sg.displaySchema();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
