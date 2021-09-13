package edu.stanford.biobank.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class WebAuthFilter implements Filter {
	private static final Logger logger = Logger.getLogger(WebAuthFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			logger.info("START doFilter");
	        // Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
			PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(((HttpServletRequest)request).getRemoteUser(), "password");
	      //  token.setDetails(new WebAuthenticationDetails(request));
	        //Authentication authentication = this.authenticationProvider.authenticate(token);
	       
	       SecurityContextHolder.getContext().setAuthentication(token);
	            
	     } catch (Exception e) {
	        SecurityContextHolder.getContext().setAuthentication(null);
	    }
		
		chain.doFilter(request, response);
		return;
		
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
