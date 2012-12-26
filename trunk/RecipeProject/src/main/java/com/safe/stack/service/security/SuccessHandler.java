package com.safe.stack.service.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.web.authentication.
     * SavedRequestAwareAuthenticationSuccessHandler
     * #onAuthenticationSuccess(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse,
     * org.springframework.security.core.Authentication)
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
	    Authentication authentication) throws ServletException, IOException {

	String url = (String) request.getSession().getAttribute("url_prior_login");

	getRedirectStrategy().sendRedirect(request, response, url);
    }

}
