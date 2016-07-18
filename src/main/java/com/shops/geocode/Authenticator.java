package com.shops.geocode;

import java.io.IOException;
import java.net.Proxy;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Responds to authentication challenges from the remote web or proxy server.
 * 
 * @author ranjan
 *
 */
public class Authenticator implements com.squareup.okhttp.Authenticator {

	private String proxyUser;

	private String proxyPassword;

	private String hostUser;

	private String hostPassword;

	public Authenticator(String user, String password) {
		this.proxyUser = user;
		this.proxyPassword = password;
	}

	/**
	 * Returns a request that includes a credential to satisfy an authentication
	 * challenge in {@code response}. Returns null if the challenge cannot be
	 * satisfied. This method is called in response to an HTTP 401 unauthorized
	 * status code sent by the origin server.
	 */
	@Override
	public Request authenticate(Proxy proxy, Response response) throws IOException {
		String credential = Credentials.basic("username", "password");
		return response.request().newBuilder().header("Proxy-Authorization", credential).build();
	}

	/**
	 * Returns a request that includes a credential to satisfy an authentication
	 * challenge made by {@code response}. Returns null if the challenge cannot
	 * be satisfied. This method is called in response to an HTTP 407
	 * unauthorized status code sent by the proxy server.
	 *
	 */
	@Override
	public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
		String credential = Credentials.basic(this.proxyUser, this.proxyPassword);
		return response.request().newBuilder().header("Proxy-Authorization", credential).build();
	}

}
