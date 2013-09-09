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
package org.onesun.sdi.core.tools;

import java.io.File;

import org.onesun.sdi.core.api.Importer;

public abstract class AbstractImporter implements Importer {
	public abstract void init();

	public abstract void parse(Object object);
	
	public abstract void process();
	
	@Override
	public abstract boolean load(String pathToImports);
	
	protected File resource = null;
	
	public File getResource() {
		return resource;
	}
	
	public void setResource(File resource) {
		this.resource = resource;
	}
}