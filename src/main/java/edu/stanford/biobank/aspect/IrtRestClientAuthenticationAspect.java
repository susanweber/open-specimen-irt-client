package edu.stanford.biobank.aspect;

import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.stanford.biobank.cache.IrtRestClientTokens;
import edu.stanford.biobank.data.restclient.irt.MrnValidationRestClient;
import edu.stanford.biobank.service.IrtTokenRefreshService;

@Aspect
@Component
public class IrtRestClientAuthenticationAspect {
	
	private static final Logger logger = Logger.getLogger(IrtRestClientAuthenticationAspect.class);
	
	@Autowired
	IrtRestClientTokens irtRestClientTokens;
	
	@Autowired
	IrtTokenRefreshService irtTokenRefreshService;
	
	
	@Around("execution(* edu.stanford.biobank.service.impl.MrnValidationServiceImpl..request*(..))")
	public Object authenticate (ProceedingJoinPoint pjp) throws Throwable {
		logger.info("START irtRestClientAuthenticate.authenticate");
		
		// get accessToken cached in memory
		String accessToken = irtRestClientTokens.accessToken;
		
		// Get args, fill in access token
		Map<String, Object> result = null;
		Object[] args = pjp.getArgs();
	    args[0] = accessToken;
	   
		try {
			result = (Map<String, Object>) pjp.proceed(args);
			String responseCode = (String) result.get(MrnValidationRestClient.RESPONSE_CODE);
			String exceptionMessage = (String) result.get("exceptionMessage");
			if ((responseCode.equals("200") && exceptionMessage !=null && exceptionMessage.contains("Authorization"))
				|| !responseCode.equals("200")) {
					args = pjp.getArgs();
					args[0] = irtTokenRefreshService.refreshAccessToken();
					result = (Map<String, Object>) pjp.proceed(args);
			}
		} catch (Throwable e) {
			logger.error("ERROR in IrtRestClientAuthenticationAspect.authenticate " + e.getMessage());
			throw e;
		}
		logger.info("END irtRestClientAuthenticate.authenticate");
		return result;
	}

}
