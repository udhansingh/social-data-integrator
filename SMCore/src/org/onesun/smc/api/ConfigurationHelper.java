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
package org.onesun.smc.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationHelper {
	public ConfigurationHelper(){
	}

	private Properties properties = new Properties();

	public Properties getProperties(){
		return properties;
	}
	
	public void setProperties(Properties properties){
		this.properties = properties;
	}
	
	public void saveConfiguration(String configFile){
		saveConfiguration(new File(configFile));
	}
	public void saveConfiguration(File configFile){
		try {
			FileOutputStream fos = new FileOutputStream(configFile);
			properties.store(fos, "Default Configuration Values");
			fos.close();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void loadConfiguration(String configFile){
		loadConfiguration(new File(configFile));
	}
	
	public void loadConfiguration(File configFile){
		if(configFile.exists() == true){
			try {
				FileInputStream fis = new FileInputStream(configFile);
				properties.load(fis);
				fis.close();
			} catch(FileNotFoundException e){
				e.printStackTrace();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
		else {
			saveConfiguration(configFile);
		}
	}
}
