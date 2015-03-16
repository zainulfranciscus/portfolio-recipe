package com.safe.stack.service.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * A user could sign in from more than 1 page in this application. Once they have signed in, 
 * users must be redirected to a page where the click the sign in button. 
 * To do this, the application must obtain the URL where users click the sign-in button, and use
 * that URL to redirect user to the URL where users click the sign-in button. This is the 
 * purpose of this class.
 * 
 * @author Zainul Franciscus
 *
 */
public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	/*
	 * Redirect user to a URL once they sign in.
	 * 
	 * @see org.springframework.security.web.authentication.
	 * SavedRequestAwareAuthenticationSuccessHandler
	 * #onAuthenticationSuccess(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

		String url = (String) request.getSession().getAttribute("url_prior_login");

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal != null && principal instanceof RecipeUser) {
			request.getSession().setAttribute("RecipeUser", (RecipeUser) principal);
		}

		getRedirectStrategy().sendRedirect(request, response, url);
	}

}
