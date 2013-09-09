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

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class HashUtils {
	public enum Context {FILE_SYSTEM};
	
	private static Logger logger = Logger.getLogger(HashUtils.class);
	
	private static MessageDigest digest = null;
	
	public static String makeHash(String text){

		if(digest == null){
			try {
				digest = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e) {
				logger.error("SecurityUtils: No Such Algorithim Exception");
			}
		}
		
		digest.reset();
		byte[] hashBytes = digest.digest(text.getBytes());
		
		Base64 base64 = new Base64();
		String hashString = new String(base64.encode(hashBytes), Charset.forName("US-ASCII"));
		
		return StringUtils.trim(hashString);
	}
	
	public static String makeHash(String text, Context context){
		String hash = makeHash(text);
		
		switch(context){
		case FILE_SYSTEM:
			return hash.replace("/", "_");
			
		default:
			return hash;
		}
	}
}