package edu.stanford.biobank.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public interface IrtTokenRefreshService {
	public String refreshAccessToken() throws ClientProtocolException, IOException;
	
}
