package com.safe.stack.service.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

/**
 * This class is used as part of the Authentication process of this project.
 * The method defined in this method was created to load a user based on the email
 * address of the user.
 * 
 * @author Zainul Franciscus
 *
 */
public class UserService extends JdbcDaoImpl {

	/**
	 * Find a user based on an email address
	 */
	private String usersByUsernameQuery = "select email,password,enabled,username from account where email = ?";

	/**
	 * a query used to determine user authorities based on the provided email address.
	 */
	private static final String DEF_AUTHORITIES_BY_USERNAME_QUERY = "select email, authority from account where email=?  ";

	public UserService() {
		super();
		setAuthoritiesByUsernameQuery(DEF_AUTHORITIES_BY_USERNAME_QUERY);
	}

	/*
	 * Obtain a list of user details based on the provided user name.
	 * 
	 * @see org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl#
	 * loadUsersByUsername(java.lang.String)
	 */
	@Override
	protected List<UserDetails> loadUsersByUsername(String username) {

		return getJdbcTemplate().query(usersByUsernameQuery, new String[] { username }, new RowMapper<UserDetails>() {
			public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				String username = rs.getString(1);
				String password = rs.getString(2);
				boolean enabled = rs.getBoolean(3);
				String alias = rs.getString(4);

				RecipeUser recipeUser = new RecipeUser(username, password, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
				recipeUser.setUserAlias(alias);

				return recipeUser;
			}

		});
	}

	/*
	 * create a user detail based on the provided user name, and authorities.
	 * 
	 * @see org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl#
	 * createUserDetails(java.lang.String,
	 * org.springframework.security.core.userdetails.UserDetails,
	 * java.util.List)
	 */
	@Override
	protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {

		RecipeUser recipeUserFromQuery = (RecipeUser) userFromUserQuery;

		UserDetails userDetails = super.createUserDetails(username, userFromUserQuery, combinedAuthorities);

		RecipeUser recipeUser = new RecipeUser(userDetails.getUsername(), userDetails.getPassword(), userDetails.isEnabled(), userDetails.isAccountNonExpired(), userDetails.isCredentialsNonExpired(),
				userDetails.isAccountNonLocked(), userDetails.getAuthorities());

		recipeUser.setUserAlias(recipeUserFromQuery.getUserAlias());

		return recipeUser;

	}
}
