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
package org.onesun.commons.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.onesun.commons.StreamUtils;

// A Java wrapper for XML.ObjTree
// Thanks to - http://openjsan.org/src/k/ka/kawasaki/XML.ObjTree-0.24/README
public class XMLObjTree {
    public static ScriptEngine getJsengine() {
		return jsEngine;
	}

	private static ScriptEngine jsEngine;
    
    static {
        try {
        	jsEngine = new ScriptEngineManager().getEngineByExtension("js");

        	InputStream is = XMLObjTree.class.getResourceAsStream("ObjTree.js");
        	InputStreamReader reader = new InputStreamReader(is);
			
        	jsEngine.eval(reader);
			
			is.close();
			reader.close();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public String jsonToXml(final File jsonFile) throws FileNotFoundException, IOException {
    	return (jsonToXml(StreamUtils.streamToString(jsonFile)));
    }
    
    public String jsonToXml(final InputStream is){
    	String input = StreamUtils.streamToString(is);
    	
    	return (jsonToXml(input));
    }
    
    public String jsonToXml(final String jsonText) {
        try{
            return (String) jsEngine.eval("(new XML.ObjTree()).writeXML(" + jsonText + ");");
        }
        catch(ScriptException ex) {
            throw new RuntimeException(ex);
        }
    }
}
