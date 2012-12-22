package com.safe.stack.service.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

public class UserService extends JdbcDaoImpl {

    private String usersByUsernameQuery = "select email,password,enabled,username from account where email = ?";

    private static final String DEF_AUTHORITIES_BY_USERNAME_QUERY = "select email, authority from account where email=?  ";

    public UserService() {
	super();
	setAuthoritiesByUsernameQuery(DEF_AUTHORITIES_BY_USERNAME_QUERY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl#
     * loadUsersByUsername(java.lang.String)
     */
    @Override
    protected List<UserDetails> loadUsersByUsername(String username) {

	return getJdbcTemplate().query(usersByUsernameQuery, new String[] { username },
		new RowMapper<UserDetails>() {
		    public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			String username = rs.getString(1);
			String password = rs.getString(2);
			boolean enabled = rs.getBoolean(3);
			String alias = rs.getString(4);

			RecipeUser recipeUser = new RecipeUser(username, password, enabled, true,
				true, true, AuthorityUtils.NO_AUTHORITIES);
			recipeUser.setUserAlias(alias);

			return recipeUser;
		    }

		});
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl#
     * createUserDetails(java.lang.String,
     * org.springframework.security.core.userdetails.UserDetails,
     * java.util.List)
     */
    @Override
    protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery,
	    List<GrantedAuthority> combinedAuthorities) {

	RecipeUser recipeUserFromQuery = (RecipeUser) userFromUserQuery;

	UserDetails userDetails = super.createUserDetails(username, userFromUserQuery,
		combinedAuthorities);

	RecipeUser recipeUser = new RecipeUser(userDetails.getUsername(),
		userDetails.getPassword(), userDetails.isEnabled(),
		userDetails.isAccountNonExpired(), userDetails.isCredentialsNonExpired(),
		userDetails.isAccountNonLocked(), userDetails.getAuthorities());
	
	recipeUser.setUserAlias(recipeUserFromQuery.getUserAlias());

	return recipeUser;

    }
}
