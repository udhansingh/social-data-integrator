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
package org.onesun.commons;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class JenaUtils {
	private JenaUtils(){}
	
	public static void main(String[] args){
		String pathname = "/smconsole/work/RFAILED/oc.rdf";
		try {
			String rdfText = StreamUtils.streamToString(new File(pathname));
			
			Model model = JenaUtils.convertToModel(rdfText);
			
			System.out.println(model);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Model convertToModel(String rdfText) {
		Model model = ModelFactory.createDefaultModel();
		
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(rdfText.getBytes("UTF-8"));
			model.read(in, null);
			in.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return model;
	}
}
