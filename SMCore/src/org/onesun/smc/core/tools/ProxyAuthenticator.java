package org.onesun.smc.core.tools;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyAuthenticator extends Authenticator {
	private String password = null;
	private String username = null;

	public ProxyAuthenticator(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username == null ? "" : username, password == null ? new char[]{} : password.toCharArray());
	}
}