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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class CollectionUtils {
	public static boolean isAllNull(Collection<String> values) {
		boolean status = true;
		
		for(String value : values){
			if(value != null){
				status = false;
				break;
			}
		}
		
		return status;
	}
	
	// Use this methods to avoid Concurrent Modification Exception on a Map
	public static synchronized boolean remove(Map<?, ?> map, String key){
		Iterator<?> it = map.keySet().iterator();
		
		while(it.hasNext()){
			Object keyObject = it.next();
			
			if(keyObject instanceof String){
				String k = (String)keyObject;
				
				// Remove the first found and return
				if(k.compareTo(key) == 0){
					it.remove();
					return true;
				}
			}
		}
		
		return false;
	}
}
