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
package org.onesun.smc.core.providers.filesystem;


import java.io.File;
import java.util.List;

import org.onesun.smc.api.FileSystemProvider;

public class FileProvider implements FileSystemProvider {
	protected List<File> resources = null;
	
	@Override
	final public List<File> getResources(){
		return resources;
	}
	
	@Override
	final public String getAuthentication() {
		return "FILE_SYSTEM";
	}
	
	public FileProvider(){
		super();
		
		init();
	}
	
	@Override
	public void init(){
	}

	@Override
	public final boolean isResponseRequired() {
		return true;
	}

	@Override
	public String getIdentity() {
		return "File System";
	}

	@Override
	public String getCategory() {
		return "FILE_SYSTEM";
	}

	@Override
	public boolean save(String pathToExports) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean load(String pathToExports) {
		// TODO Auto-generated method stub
		return false;
	}
}
