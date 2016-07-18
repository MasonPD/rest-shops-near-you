package com.shops.geocode;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.ApiResponse;
import com.google.maps.internal.OkHttpPendingResult;
import com.google.maps.internal.RateLimitExecutorService;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * A strategy for handling URL requests using OkHttp with Authentication.
 *
 * @see com.google.maps.GeoApiContext.RequestHandler
 */
public class AuthenticatedOkHttpRequestHandler implements GeoApiContext.RequestHandler {

	/**
	 * logger
	 */
	private static final Logger LOG = Logger.getLogger(AuthenticatedOkHttpRequestHandler.class.getName());

	/**
	 * the http client
	 */
	private final OkHttpClient client = new OkHttpClient();

	/**
	 * the rate limiting service
	 */
	private final RateLimitExecutorService rateLimitExecutorService;

	public AuthenticatedOkHttpRequestHandler() {
		rateLimitExecutorService = new RateLimitExecutorService();
		client.setDispatcher(new Dispatcher(rateLimitExecutorService));
	}

	@Override
	public <T, R extends ApiResponse<T>> PendingResult<T> handle(String hostName, String url, String userAgent,
			Class<R> clazz, FieldNamingPolicy fieldNamingPolicy, long errorTimeout) {
		Request req = new Request.Builder().get().header("User-Agent", userAgent).url(hostName + url).build();

		LOG.log(Level.INFO, "Request: {0}", hostName + url);

		return new OkHttpPendingResult<T, R>(req, client, clazz, fieldNamingPolicy, errorTimeout);
	}

	@Override
	public void setConnectTimeout(long timeout, TimeUnit unit) {
		client.setConnectTimeout(timeout, unit);
	}

	@Override
	public void setReadTimeout(long timeout, TimeUnit unit) {
		client.setReadTimeout(timeout, unit);
	}

	@Override
	public void setWriteTimeout(long timeout, TimeUnit unit) {
		client.setWriteTimeout(timeout, unit);
	}

	@Override
	public void setQueriesPerSecond(int maxQps) {
		rateLimitExecutorService.setQueriesPerSecond(maxQps);
	}

	@Override
	public void setQueriesPerSecond(int maxQps, int minimumInterval) {
		rateLimitExecutorService.setQueriesPerSecond(maxQps, minimumInterval);
	}

	@Override
	public void setProxy(Proxy proxy) {
		client.setProxy(proxy);
	}

	public void setAuthenticator(Authenticator authenticator) {
		client.setAuthenticator(authenticator);
	}

}
