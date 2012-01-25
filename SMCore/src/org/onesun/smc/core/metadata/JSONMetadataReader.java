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
package org.onesun.smc.core.metadata;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.onesun.smc.core.model.MetaObject;

public class JSONMetadataReader extends AbstractMetadataReader {
	protected String jsonData = null;
	protected String processingData = null;

	public JSONMetadataReader(String data) {
		this.jsonData = data;
	}

	@Override
	public void initialize() throws Exception {
		if (beginPath != null && beginPath.length() > 0) {
			String[] dataPath = beginPath.split("/");
			Object jsonObject = new JSONObject(jsonData);
			for (int i = 0; i < dataPath.length; i++) {
				if (jsonObject instanceof JSONObject)
					try {
						jsonObject = ((JSONObject) jsonObject).get(dataPath[i]);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
					try {
						throw new JSONException(
								"Data souce inside an array not supported.");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			processingData = jsonObject.toString();
		} else
			processingData = jsonData;
	}

	private void generateJPaths(JSONArray jsonArray, String Path, int depth) {
		for (int i = 0; i < jsonArray.length(); i++) {
			Object jsonChild = null;
			try {
				jsonChild = jsonArray.get(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (jsonChild == null) {
				return;
			} else if (jsonChild instanceof JSONObject) {
				generateJPaths((JSONObject) jsonChild, Path, depth + 1);
			} else {
				if (!paths.contains("@values"))
					paths.add("@values");
			}
		}
	}

	private void generateJPaths(JSONObject jsonObject, String Path, int depth) {
		Iterator<?> itr = jsonObject.keys();
		try {
			while (itr.hasNext()) {
				Object jsonChild;
				String key = (String) itr.next();
				jsonChild = jsonObject.get(key);
				if (jsonChild instanceof JSONObject) {
					String jPath = Path + key;

					if (!leafNodesOnly && !paths.contains(jPath))
						paths.add(jPath);

					generateJPaths((JSONObject) jsonChild, jPath + "/",
							depth + 1);
				} else if (jsonChild instanceof JSONArray) {
					String jPath = Path + key + "*";
					if (!paths.contains(jPath))
						paths.add(jPath);
					/*
					 * Functionality not supported by data reader.
					 * 
					 * if(!stopAtMultiValued){ generateJPaths((JSONArray)
					 * jsonChild, jPath+"/", depth + 1); }
					 */

				} else {
					String jPath = Path + key;
					if (!paths.contains(jPath))
						paths.add(jPath);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Metadata getMetadata() {
		Metadata metadata = new Metadata();

		for (String path : paths) {
			MetaObject mo = new MetaObject();
			mo.setName(path);
			mo.setPath(path);
			
			metadata.put(path, mo);
		}

		return metadata;
	}

	@Override
	public void generateMetadata() throws JSONException {
		if (processingData.startsWith("[") && processingData.endsWith("]")) {
			JSONArray jsonArray = new JSONArray(processingData);
			generateJPaths(jsonArray, "", 0);
		} else if (processingData.startsWith("{")
				&& processingData.endsWith("}")) {
			JSONObject jsonObject = new JSONObject(processingData);
			generateJPaths(jsonObject, "", 0);
		} else
			throw new JSONException("Invalid JSON data.");

	}
}
