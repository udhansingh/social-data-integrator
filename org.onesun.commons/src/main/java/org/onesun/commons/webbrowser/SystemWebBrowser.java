package org.onesun.commons.webbrowser;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SystemWebBrowser implements WebBrowser {
	@Override
	public void browse(String url) throws IOException, URISyntaxException {
		Desktop.getDesktop().browse(new URI(url));
	}

	@Override
	public void onLocationChanged(String url) {
		// TODO: Nothing
	}
}
