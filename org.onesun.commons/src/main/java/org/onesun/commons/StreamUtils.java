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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class StreamUtils {
	public static String streamToString(InputStream is) {
		try
		{
			final char[] buffer = new char[4096];
			StringBuilder out = new StringBuilder();

			Reader in = new InputStreamReader(is, "UTF-8");

			int read;
			do
			{
				read = in.read(buffer, 0, buffer.length);
				if (read > 0)
				{
					out.append(buffer, 0, read);
				}
			} while (read >= 0);
		
			in.close();
			
			return out.toString();
		} catch (IOException ioe)
		{
			throw new IllegalStateException("Error while reading input stream", ioe);
		}
	}
	
	public static String streamToString(File file) throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(file);
		
		String string = streamToString(fis);
		fis.close();
		
		return string;
	}
	
	public static void write(final String data, final Writer writer) throws IOException{
		writer.write(data);
		
		writer.flush();
		
		writer.close();
	}
	
	public static void write(final String data, final File file) throws IOException{
		try{
			FileOutputStream fos = new FileOutputStream(file);
			
			Writer writer = new OutputStreamWriter(fos, "UTF-8");
			write(data, writer);

			fos.close();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
