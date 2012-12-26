package com.safe.stack.service.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.access.ExceptionTranslationFilter;

public class SecurityFilter extends ExceptionTranslationFilter {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.web.access.ExceptionTranslationFilter#doFilter
     * (javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	    throws IOException, ServletException {

	HttpServletRequest httpReq = (HttpServletRequest) req;

	String referrer = httpReq.getHeader("Referer");
	
	System.out.println(referrer);

	super.doFilter(req, res, chain);
    }

}
