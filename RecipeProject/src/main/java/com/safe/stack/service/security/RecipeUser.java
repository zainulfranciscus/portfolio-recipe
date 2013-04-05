package com.safe.stack.service.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * A User of this website has a user has an alias, that 
 * might be different than the user's user name. To support alias, a custom type of a User class has to be created,
 * and this is the purpose of this class.
 * 
 * @author Zainul Franciscus
 *
 */
public class RecipeUser extends User {

    /**
     * An alias of a user.
     */
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
