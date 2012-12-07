package com.safe.stack.service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class RecipeUser extends User {

    private String userAlias;
    
   

    public RecipeUser(String username, String password, boolean enabled, boolean accountNonExpired,
	    boolean credentialsNonExpired, boolean accountNonLocked,
	    Collection<? extends GrantedAuthority> authorities) {
	super(username, password, enabled, accountNonExpired, credentialsNonExpired,
		accountNonLocked, authorities);
    }

    public RecipeUser(String username, String password,
	    Collection<? extends GrantedAuthority> authorities) {
	super(username, password, authorities);

    }

    /**
     * @return the userAlias
     */
    public String getUserAlias() {
	return userAlias;
    }

    /**
     * @param userAlias
     *            the userAlias to set
     */
    public void setUserAlias(String userAlias) {
	this.userAlias = userAlias;
    }

}
