package org.onesun.commons.webbrowser;

import java.io.IOException;
import java.net.URISyntaxException;

public interface WebBrowser {
	void browse(String url) throws IOException, URISyntaxException;
	void onLocationChanged(String url);
}
