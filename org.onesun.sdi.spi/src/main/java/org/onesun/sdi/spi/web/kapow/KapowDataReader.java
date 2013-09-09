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
package org.onesun.sdi.spi.web.kapow;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.onesun.sdi.core.data.reader.AbstractDataReader;

import com.kapowtech.robosuite.api.java.repository.construct.Attribute;
import com.kapowtech.robosuite.api.java.repository.construct.Type;
import com.kapowtech.robosuite.api.java.rql.construct.RQLObject;

public class KapowDataReader extends AbstractDataReader {
	private KapowObject object = null;
	private List<RQLObject> rqlObjects = null;

	public KapowDataReader(KapowObject object, List<RQLObject> rqlObjects){
		this.object = object;
		this.rqlObjects = rqlObjects;
	}
	
	@Override
	public void load() {
		if(object != null && rqlObjects != null){
			Type[] types = object.getReturnedTypes();

			for(Type type : types){
				for(RQLObject rqlObject : rqlObjects){
					Attribute[] attributes = type.getAttributes();
					Map<String, String> m = new TreeMap<String, String>();

					for(Attribute attribute : attributes){
						String attributeName = attribute.getName();
						String attributeValue = "";

						Object vo = rqlObject.get(attributeName);
						if(vo instanceof String){
							attributeValue = (String)vo;
						}

						// Use XPATH as column identifier
						m.put(type.getTypeName() + "/" + attribute.getName(), attributeValue);
					}

					data.add(m);
				}
			}
		}
	}

	@Override
	public void initialize() {
	}
}
