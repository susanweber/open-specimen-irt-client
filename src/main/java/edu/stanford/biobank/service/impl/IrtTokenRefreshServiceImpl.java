package edu.stanford.biobank.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.stanford.biobank.cache.IrtRestClientTokens;
import edu.stanford.biobank.data.dao.IrtPropertyDao;
import edu.stanford.biobank.data.model.IrtProperty;
import edu.stanford.biobank.data.restclient.irt.MrnValidationRestClient;
import edu.stanford.biobank.service.IrtTokenRefreshService;

@Service
public class IrtTokenRefreshServiceImpl implements IrtTokenRefreshService {
	
	public static final String REFRESH_TOKEN_KEY = "refreshToken";
	
	@Autowired
	IrtRestClientTokens irtRestClientTokens;
	
	@Autowired
	IrtPropertyDao irtPropertyDao;
	
	@Override
	@Transactional
	public String refreshAccessToken() throws ClientProtocolException, IOException {
		String accessToken = null;
		
		synchronized (irtRestClientTokens) {
			
			// Get refresh token from DB
			String refreshToken = null;
			List<IrtProperty> props = irtPropertyDao.listIrtProperties();
			for (IrtProperty prop: props) {
				if (prop.getName().equals(REFRESH_TOKEN_KEY)) {
					refreshToken = prop.getValue();
				}
			}
			
			// Get new tokens from IRT
			Map <String, String> newTokens = MrnValidationRestClient.getNewAuthTokens(refreshToken);
			accessToken = newTokens.get(MrnValidationRestClient.ACCESS_TOKEN_KEY);
			refreshToken = newTokens.get(MrnValidationRestClient.REFRESH_TOKEN_KEY);
			
			// Save new refresh token in DB
			for (IrtProperty prop: props) {
				if (prop.getName().equals(REFRESH_TOKEN_KEY)) {
					prop.setValue(refreshToken);
				} 
				irtPropertyDao.update(prop);
			}
			
			// Save new access token in memory
			irtRestClientTokens.accessToken = accessToken;
		}
		return accessToken;
	}

}
