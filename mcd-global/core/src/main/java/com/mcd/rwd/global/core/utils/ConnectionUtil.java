package com.mcd.rwd.global.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;

/**
 * Created by deepti_b on 3/7/2016.
 */
public final class ConnectionUtil {
	/**
	 * default logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionUtil.class);

	/**
	 * get method key
	 */
	private static final String GET_METHOD = "GET";

	/**
	 * key for utf encoding
	 */
	private static final String ENCODING_UTF = "UTF-8";

	/**
	 * connection timeout
	 */
	private static final int CONNECTION_READ_TIMEOUT = 60 * 1000 * 5;

	/**
	 * Make a request to the given URL.
	 *
	 * @param desiredUrl
	 * @return
	 */
	public synchronized String sendGet(final String desiredUrl) {
		return sendGet(desiredUrl, null);
	}

	/**
	 * Makes a request to the URL along with the headers that are passed.
	 *
	 * @param desiredUrl
	 * @param header
	 * @return
	 */
	public synchronized String sendGet(final String desiredUrl, Map<String, String> header) {
		BufferedReader reader = null;
		StringBuilder stringBuilder;

		try {
			HttpURLConnection connection = getConnection(desiredUrl, header);

			if (connection != null && connection.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
				// read the output from the server
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), ENCODING_UTF));
				stringBuilder = new StringBuilder();

				String line = null;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line).append("\n");
				}
				return stringBuilder.toString();
			} else {
				return null; // failure
			}

		} catch (Exception e) {
			LOGGER.error("Exception in ConnectionUtil class while making connection", e);
		} finally {
			// close the reader; this can throw an exception too, so wrap it in another try/catch block.
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ioe) {
					LOGGER.error("Exception while closing BufferedReader", ioe);
				}
			}
		}
		return null;
	}

	/**
	 * Makes a request
	 *
	 * @param desiredUrl
	 * @return
	 */
	public synchronized InputStream makeRequest(final String desiredUrl) {

		try {
			HttpURLConnection connection = getConnection(desiredUrl, null);

			if (connection != null && connection.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
				return connection.getInputStream();
			}
		} catch (Exception e) {
			LOGGER.error("Exception in ConnectionUtil class while making connection or reading data", e);
		}
		return null;
	}

	/**
	 * Creates the URL and opens the connection.
	 * @param desiredUrl
	 * @param header
	 * @return
	 */
	private HttpURLConnection getConnection(final String desiredUrl, final Map<String, String> header) {

		URL url = null;

		try {
			// create the HttpURLConnection
			url = new URL(desiredUrl);

			// Add HCL proxy here
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// just want to do an HTTP GET here
			connection.setRequestMethod(GET_METHOD);

			// give it 15 seconds to respond
			connection.setReadTimeout(CONNECTION_READ_TIMEOUT);

			// append header
			if (header != null) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					connection.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}

			return connection;

		} catch (MalformedURLException mfe) {
			LOGGER.error("Malformed URL Exception while making request to {}", url, mfe);
		} catch (ProtocolException e) {
			LOGGER.error("Protocol Exception while making connection to {}", url, e);
		} catch (IOException e) {
			LOGGER.error("IO Exception while making connection to {}", url, e);
		}
		return null;
	}
}
