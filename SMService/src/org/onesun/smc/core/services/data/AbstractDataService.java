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
package org.onesun.smc.core.services.data;

import java.util.List;
import java.util.Map;

import org.onesun.smc.api.DataService;
import org.onesun.smc.core.metadata.Metadata;

public abstract class AbstractDataService implements DataService {
	protected List<Map<String, String>> data = null;
	protected Metadata metadata = null;
	
	@Override
	public final void setData(List<Map<String, String>> data){
		this.data = data;
	}
	
	@Override
	public final void setMetadata(Metadata metadata){
		this.metadata = metadata;
	}
	
	protected abstract void process(Map<String, String> datum, String text);
	
	@Override
	public abstract void execute();
}
