package org.scribe.builder.api;

import org.scribe.model.*;
import org.scribe.services.*;
import org.scribe.utils.OAuthEncoder;

public class YammerApi extends DefaultApi20
{
  private static final String AUTHORIZATION_URL = "https://www.yammer.com/dialog/oauth?client_id=%s&redirect_uri=%s"; 
		  // "https://www.yammer.com/oauth/authorize?oauth_token=%s";

  @Override
  public String getAccessTokenEndpoint()
  {
    return "https://www.yammer.com/oauth2/access_token";
  }

  @Override
  public String getAuthorizationUrl(OAuthConfig config)
  {
    // return String.format(AUTHORIZATION_URL, requestToken.getToken());
	  return String.format(AUTHORIZATION_URL, config.getApiKey(), OAuthEncoder.encode(config.getApiKey()), OAuthEncoder.encode(config.getCallback()));
  }
}
