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
package org.onesun.smc.core.data.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.onesun.smc.api.DataReader;
import org.onesun.smc.core.metadata.Metadata;

public abstract class AbstractDataReader implements DataReader {
	protected Metadata metadata =  new Metadata();
	protected List<Map<String, String>> data = Collections.synchronizedList(new ArrayList<Map<String, String>>());
	
	@Override
	public final Metadata getMetadata() {
		return metadata;
	}
	
	@Override
	public final void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	@Override
	public final List<Map<String, String>> getData() {
		return data;
	}
	
	@Override
	public final void setData(List<Map<String, String>> data){
		this.data = data;
	}

	@Override
	public abstract void initialize();
}
