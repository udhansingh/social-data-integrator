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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class Log4jUtils {
	public static void initLog(){
		Properties properties = new Properties();
		
		properties.setProperty("log4j.rootLogger", "INFO, CONSOLE");
		properties.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
		properties.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
		properties.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{ABSOLUTE} %5p %c{1}:%L - %m%n");
		
		PropertyConfigurator.configure(properties);
	}
	
	public static void initLog(File log4jFile) throws FileNotFoundException, IOException {
		InputStream inStream = null;
		String message = null;
		
		try {
			Properties properties = new Properties();
			inStream = new FileInputStream(log4jFile);
			properties.load(inStream);
			PropertyConfigurator.configure(properties);
		} catch (FileNotFoundException e) {
			message = "log4j properties file '" + log4jFile.getAbsolutePath() + "' cannot be found!";
			throw new FileNotFoundException(message);
		} catch (IOException e) {
			message = "Problem encountered while reading log4j properties file '" + log4jFile.getAbsolutePath();
			throw new IOException(message);
		} finally {
			if(inStream != null){
				inStream.close();
			}
		}
	}
}
