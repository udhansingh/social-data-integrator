/*
   Copyright 2011 Udayakumar Dhansingh (Udy)
   				  Vikas Kumar Jha

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
package org.onesun.sdi.core.data.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONDataReader extends AbstractDataReader {
	private Object jsonObject;
	private Object processingObject;

	public JSONDataReader(String jsonString) throws JSONException {
		if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
			this.jsonObject = new JSONArray(jsonString);
		} else if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
			this.jsonObject = new JSONObject(jsonString);
		} else
			throw new JSONException("Invalid JSON data.");
	}

	private void addData(Map<String, String> data, String[] key, String value) {

		String path = key[0];
		for (int i = 1; i < key.length; i++) {
			path += "/" + key[i];
		}
		if(value.equals("null")){
			data.put(path, "");
		}
		else
			data.put(path, value);
	}

	@Override
	public void initialize() {
		String nodeName = metadata.getNodeName();
		processingObject = jsonObject;
		
		if (nodeName != null && nodeName.length() > 0) {
			String[] dataPath = nodeName.split("/");
			for (int i = 0; i < dataPath.length; i++) {
				if (processingObject instanceof JSONObject)
					try {
						processingObject = ((JSONObject) processingObject)
								.get(dataPath[i]);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
					try {
						throw new JSONException(
								"Data souce node cannot be inside an array ");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			// pathtrepae.setDataDepth(dataPath.length - 1);
		}
	}

	@Override
	public void load() {
		List<String> xPaths = new ArrayList<String>();
		for (String value : metadata.keySet()) {
			xPaths.add(value);
		}

		data.clear();
		processData();
	}

	public void processData() {
		if (processingObject instanceof JSONArray) {
			int length = ((JSONArray) processingObject).length();
			
			for (int i = 0; i < length; i++) {
				Map<String, String> jRow = Collections.synchronizedMap(new TreeMap<String, String>());
				
				for (String jPath : metadata.keySet()) {
					try {
						processData(
								((JSONArray) this.processingObject).get(i), jPath.split("/"), jRow, 0);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
					}
				}
				data.add(jRow);
			}

		} else if (processingObject instanceof JSONObject) {
			Map<String, String> jRow = Collections.synchronizedMap(new TreeMap<String, String>());
			
			for (String jPath : metadata.keySet()) {
				processData(this.processingObject, jPath.split("/"), jRow, 0);
			}
			data.add(jRow);
		}
	}

	private void processData(Object jsonObject, String[] jPath, Map<String, String> jRow, int depth) {

		Object jsonChild = null;
		if (depth > jPath.length - 1) {
			addData(jRow, jPath, "");
			return;
		}
		if (!(jsonObject instanceof JSONObject)) {
			addData(jRow, jPath, jsonObject.toString());
			return;
		}

		boolean dump = jPath[depth].endsWith("*");

		try {
			jsonChild = ((JSONObject) jsonObject).get(jPath[depth].replace("*",	""));
		} catch (JSONException e) {
			addData(jRow, jPath, "");
			return;
		}
		if(dump == true){
			addData(jRow, jPath, jsonChild.toString());
		}
		else if (jsonChild == null) {
			addData(jRow, jPath, "");
		} else if (jsonChild instanceof JSONObject) {
			processData(jsonChild, jPath, jRow, depth + 1);
		} else if (jsonChild instanceof JSONArray) {
			addData(jRow, jPath, ((JSONArray) jsonChild).toString());

		} else {
			addData(jRow, jPath, jsonChild.toString());
		}

	}
}
