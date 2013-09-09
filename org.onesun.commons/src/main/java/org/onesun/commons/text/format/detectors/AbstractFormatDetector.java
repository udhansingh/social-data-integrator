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
package org.onesun.commons.text.format.detectors;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public abstract class AbstractFormatDetector implements FormatDetector{
	protected TextFormat format = TextFormat.UNKNOWN;
	
	@Override
	final public TextFormat getFormat() {
		return format;
	}

	@Override
	final public void analyze(String input) throws Exception{
		process(input);
	}
	
	@Override
	final public void analyze(File file) throws Exception {
		process(file);
	}
	
	protected void process(File file) throws Exception{
		try {
			InputStream is = new FileInputStream(file);
			process(is);
			is.close();
		} finally {
		}
	}
	
	protected void process(String input) throws Exception{
		InputStream is;
		try {
			is = new ByteArrayInputStream(input.getBytes("UTF-8"));
			process(is);
			is.close();
		} finally {
		}
	}
	
	protected abstract void process(InputStream is) throws Exception;
}
