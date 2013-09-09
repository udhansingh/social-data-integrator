package org.scribe.model;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;


/**
 * The representation of an OAuth HttpRequest.
 * 
 * Adds BasicAuth-related functionality to the {@link Request}  
 * 
 * @author Udayakumar Dhansingh
 */
public class BasicRequest extends Request
{
	/**
	 * Default constructor.
	 * 
	 * @param verb Http verb/method
	 * @param url resource URL
	 * @param usernamePasswordCredentials 
	 */

	private UsernamePasswordCredentials usernamePasswordCredentials;

	public BasicRequest(Verb verb, String url, UsernamePasswordCredentials usernamePasswordCredentials)
	{
		super(verb, url);
		this.usernamePasswordCredentials = usernamePasswordCredentials;
	}

	@Override
	Response doSend(RequestTuner tuner) throws IOException
	{
		if(usernamePasswordCredentials != null){
			final Charset charset = Charset.forName("UTF-8");

			String authString = usernamePasswordCredentials.getUsername() + ":" + usernamePasswordCredentials.getPassword();
			byte[] encodedBytes = Base64.encodeBase64(authString.getBytes(charset));
			String encodedString = new String(encodedBytes, charset);
			connection.setRequestProperty("Authorization", "Basic " + encodedString);
		}
		
		return super.doSend(tuner);
	}

	@Override
	public String toString()
	{
		return String.format("@BasicRequest(%s, %s)", getVerb(), getUrl());
	}
}
