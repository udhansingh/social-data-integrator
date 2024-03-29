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
package org.onesun.sdi.core.api;

import java.util.List;
import java.util.Map;

import org.onesun.sdi.core.metadata.Metadata;

public interface DataReader {
	void load();
	void setMetadata(Metadata metadata);
	Metadata getMetadata();
	List<Map<String, String>> getData();
	void initialize();
	void setData(List<Map<String, String>> data);
}