package edu.stanford.biobank.data.restclient.irt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.biobank.data.model.PatientId;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MrnValidationRestClient {
	private static final Logger logger = Logger.getLogger(MrnValidationRestClient.class);
	public static String MRN_VAL_URL = "https://starr.med.stanford.edu/identifiers/api/v1/mrn/biobank";
	public static String REFRESH_URL = "https://starr.med.stanford.edu/token/api/v1/refresh";
	public static String MRN_DEID_URL = "https://starr.med.stanford.edu/identifiers/api/v1/uid/biobank";
	
	public static final String RESPONSE_CODE = "responseCode";
	
	public static Map<String, Object> getPatients (String accessToken, List<String> mrns) throws ClientProtocolException, IOException {
		logger.info("START MrnValidationRestClient.getPatients");
		logger.info("accessToken: "  + accessToken);
		Map<String, Object> result = new HashMap<String,Object>();
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(MRN_VAL_URL);
		
		post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		
		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode mrnArrayNode = objectMapper.valueToTree(mrns);
	  
		ObjectNode requestBody = objectMapper.createObjectNode();
		requestBody.putArray("mrns").addAll(mrnArrayNode);
		
		StringEntity requestEntity =new StringEntity(requestBody.toString(),"UTF-8");
		post.setEntity(requestEntity);
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
        HttpResponse response = httpClient.execute(post);
        int responseCode = response.getStatusLine().getStatusCode();
        result.put(RESPONSE_CODE, String.valueOf(responseCode));
       
        HttpEntity responseEntity = response.getEntity();
        result.putAll( (Map<String, Object>) objectMapper.readValue(responseEntity.getContent(), Object.class) );
        
        logger.info("FINISH MrnValidationRestClient.getPatients");
        return result;	 
	}

	public static Map<String, Object> getDeidMrns(String accessToken, List<PatientId> mrns) throws IOException {
		logger.info("START MrnValidationRestClient.getDeidMrns");
		logger.info("accessToken: " + accessToken);
		Map<String, Object> result = new HashMap<String, Object>();

		HttpPost post = new HttpPost(MRN_DEID_URL);

		post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

		ObjectMapper objectMapper = new ObjectMapper();
		ArrayNode mrnArrayNode = objectMapper.valueToTree(mrns);

		ObjectNode requestBody = objectMapper.createObjectNode();
		requestBody.putArray("identifiers").addAll(mrnArrayNode);

		StringEntity requestEntity = new StringEntity(requestBody.toString(), "UTF-8");
		post.setEntity(requestEntity);

		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpResponse response = httpClient.execute(post);
		int responseCode = response.getStatusLine().getStatusCode();
		result.put(RESPONSE_CODE, String.valueOf(responseCode));

		HttpEntity responseEntity = response.getEntity();
		result.putAll( (Map<String, Object>) objectMapper.readValue(responseEntity.getContent(), Object.class) );

		logger.info("FINISH MrnValidationRestClient.getDeidMrns");
		return result;
	}
	
	public static final String REFRESH_TOKEN_KEY = "refreshToken";
	public static final String ACCESS_TOKEN_KEY = "accessToken";
	
	public static Map<String,String> getNewAuthTokens(String refreshToken) throws ClientProtocolException, IOException {
		Map<String, String> result = new HashMap<String, String>();
		
		// Get new tokens
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(REFRESH_URL);
		
		ObjectMapper objectMapper = new ObjectMapper();
	  
		ObjectNode requestBody = objectMapper.createObjectNode();
		requestBody.put("refreshToken", refreshToken);
		
		StringEntity requestEntity =new StringEntity(requestBody.toString(),"UTF-8");
		post.setEntity(requestEntity);
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
        HttpResponse response = httpClient.execute(post);
        int responseCode = response.getStatusLine().getStatusCode();
        if (responseCode != 401) {
        
        	HttpEntity responseEntity = response.getEntity();
        	Map<String, Object> responseBody = (Map<String, Object>) objectMapper.readValue(responseEntity.getContent(), Object.class);
        	logger.info(responseBody.toString());
        	
        	String accessToken = (String) responseBody.get("accessToken");
        	logger.info(ACCESS_TOKEN_KEY + accessToken);
        	result.put(ACCESS_TOKEN_KEY, accessToken);
        	
        	refreshToken = (String) responseBody.get("refreshToken");
        	logger.info(REFRESH_TOKEN_KEY + refreshToken);
        	result.put(REFRESH_TOKEN_KEY, refreshToken);
        	
        	return result;
       
        } else {
        	String message = "Invalid Refresh Token: " + refreshToken;
        	logger.error(message);
        	throw new RuntimeException(message);
        }
        
	}
	
	public static void main (String args[]) throws ClientProtocolException, IOException {
//		List<String> mrns = new ArrayList<String>();
//		mrns.add("21423074");
//		mrns.add("46027462");
//		mrns.add("test");
//		Map<String, Object> result = MrnValidationRestClient.getPatients(mrns, "17bzwu9re19om1txhh3zhfasm9j5nv2panb0c0p7a55m12tdqm14emtduo8m0zc1");
//		System.out.println(result.toString());
		
		//String authToken = MrnValidationRestClient.getAuthToken();
		
	}
}
