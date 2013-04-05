package com.safe.stack.domain;

import java.io.Serializable;
import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * An entity class intended to map field in ACCOUNT table to a Java Object
 * @author Zainul Franciscus
 *
 */
@Entity
@Table(name = "Account")
public class Account implements Serializable {

	/**
	 * An email of a user.
	 */
	private String email;
	/**
	 * Password of a user
	 */
	private String password;
	/**
	 * User Name of a user
	 */
	private String userName;
	/**
	 * Picture of a user
	 */
	private Blob picture;
	/**
	 * A description of a user
	 */
	private String bio;
	/**
	 * A location of a user
	 */
	private String location;
	/**
	 * A twitter user name of a user
	 */
	private String twitter;
	/**
	 * A number used for optimistic locking
	 */
	private int version;
	/**
	 * A role that assigned to this user
	 */
	private String authority;

	/**
	 * A set of recipes that a user has liked
	 */
	private Set<Recipe> likedRecipes = new HashSet<Recipe>();

	/**
	 * @return the likedRecipes
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "LikedRecipe", joinColumns = @JoinColumn(name = "email"), inverseJoinColumns = @JoinColumn(name = "recipeId"))
	public Set<Recipe> getLikedRecipes() {
		return likedRecipes;
	}

	/**
	 * @param likedRecipes
	 *            the likedRecipes to set
	 */
	public void setLikedRecipes(Set<Recipe> likedRecipes) {
		this.likedRecipes = likedRecipes;
	}

	/**
	 * @return the email
	 */
	@NotEmpty(message = "{validation.email.NotEmpty.message}")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "{validation.email.invalid.format}")
	@Column(name = "email")
	@Id
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	@NotEmpty(message = "{validation.password.NotEmpty.message}")
	@Pattern(regexp = "^.*(?=.{6,})(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d\\W]).*$", message = "{validation.password.invalid.format}")
	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userName
	 */
	@Column(name = "userName")
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the picture
	 */
	@Column(name = "picture")
	public Blob getPicture() {
		return picture;
	}

	/**
	 * @param picture
	 *            the picture to set
	 */
	public void setPicture(Blob picture) {
		this.picture = picture;
	}

	/**
	 * @return the bio
	 */
	@Column(name = "bio")
	public String getBio() {
		return bio;
	}

	/**
	 * @param bio
	 *            the bio to set
	 */
	public void setBio(String bio) {
		this.bio = bio;
	}

	/**
	 * @return the location
	 */
	@Column(name = "location")
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the twitter
	 */
	@Column(name = "twitter")
	public String getTwitter() {
		return twitter;
	}

	/**
	 * @param twitter
	 *            the twitter to set
	 */
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	/**
	 * @return the version
	 */
	@Version
	@Column(name = "version")
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the authority
	 */
	@Column(name = "authority")
	public String getAuthority() {
		return authority;
	}

	/**
	 * @param authority
	 *            the authority to set
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/**
	 * @param userName
	 *            of a user
	 * @param password
	 *            of a user
	 * @return a new Account that can be used to create a new user account
	 */
	public static Account getNewAccount(String userName, String password) {
		Account acc = new Account();
		acc.setEmail(userName);
		acc.setPassword(password);
		acc.setUserName("user" + userName.hashCode());
		acc.setAuthority("ROLE_USER");
		return acc;
	}

}
