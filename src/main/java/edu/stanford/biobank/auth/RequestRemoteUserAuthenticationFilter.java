package edu.stanford.biobank.auth;

import javax.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;


import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

public class RequestRemoteUserAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
	private String principalEnvironmentVariable = "REMOTE_USER";
	private String credentialsEnvironmentVariable;
	private boolean exceptionIfVariableMissing = true;

	private static final Logger logger = Logger.getLogger(RequestRemoteUserAuthenticationFilter.class);
	
	/**
	 * Read and returns the variable named by {@code principalEnvironmentVariable} from
	 * the request.
	 *
	 * @throws PreAuthenticatedCredentialsNotFoundException if the environment variable is
	 * missing and {@code exceptionIfVariableMissing} is set to {@code true}.
	 */
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		logger.info("START getPreAuthenticatedPrincipal");
		//String principal = (String) request.getAttribute(principalEnvironmentVariable);
		String principal = request.getRemoteUser();
		
		if (principal == null && exceptionIfVariableMissing) {
			throw new PreAuthenticatedCredentialsNotFoundException(
					principalEnvironmentVariable + " variable not found in request.");
		} else {
			try {
				logger.info("adding Authentication to Servlet Context");
		        // Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
				PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(((HttpServletRequest)request).getRemoteUser(), "password");
		       
		        SecurityContextHolder.getContext().setAuthentication(token);
		    } catch (Exception e) {
		        SecurityContextHolder.getContext().setAuthentication(null);
		    }
		}
		
		return principal;
	}

	/**
	 * Credentials aren't usually applicable, but if a
	 * {@code credentialsEnvironmentVariable} is set, this will be read and used as the
	 * credentials value. Otherwise a dummy value will be used.
	 */
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		logger.info("START getPreAuthenticatedCredentials");
		if (credentialsEnvironmentVariable != null) {
			return request.getAttribute(credentialsEnvironmentVariable);
		}

		return "N/A";
	}

	public void setPrincipalEnvironmentVariable(String principalEnvironmentVariable) {
		logger.info("START setPrincipalEnvironmentVariable " + principalEnvironmentVariable);
		Assert.hasText(principalEnvironmentVariable,
				"principalEnvironmentVariable must not be empty or null");
		this.principalEnvironmentVariable = principalEnvironmentVariable;
	}

	public void setCredentialsEnvironmentVariable(String credentialsEnvironmentVariable) {
		logger.info("START setCredentialsEnvironmentVariable");
		Assert.hasText(credentialsEnvironmentVariable,
				"credentialsEnvironmentVariable must not be empty or null");
		this.credentialsEnvironmentVariable = credentialsEnvironmentVariable;
	}

	/**
	 * Defines whether an exception should be raised if the principal variable is missing.
	 * Defaults to {@code true}.
	 *
	 * @param exceptionIfVariableMissing set to {@code false} to override the default
	 * behaviour and allow the request to proceed if no variable is found.
	 */
	public void setExceptionIfVariableMissing(boolean exceptionIfVariableMissing) {
		logger.info("START setExceptionIfVariableMissing " + exceptionIfVariableMissing);
		this.exceptionIfVariableMissing = exceptionIfVariableMissing;
	}
}
