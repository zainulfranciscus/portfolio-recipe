package com.safe.stack.domain;

import java.io.Serializable;

/**
 * A POJO intended to encapsulates information about a recipe that will be displayed for the home page.
 * 
 * @author Zainul Franciscus
 *
 */
public class RecipeSummary extends Recipe implements Serializable {

	/**
	 * An id of a recipe
	 */
	private Long id;

	/**
	 * A name of a recipe
	 */
	private String name;

	/**
	 * Author of a recipe
	 */
	private String author;

	/**
	 * The type of diet of a recipe. e.g.: Vegan, Vegetarian, etc
	 */
	private String diet;


	/**
	 * A URL to the author of this recipe.
	 */
	private String authorLink;

	/**
	 * The file name of a picture for this recipe
	 */
	private String picture;

	/**
	 * Indicate whether a recipe has been liked by a user. 0 means that a user
	 * has not liked this recipe; 1 otherwise.
	 */
	private Long likedByUser = new Long(0);
	
	/**
	 * The number of users who have liked a recipe 
	 */
	private Long numberOfLikes;


	/**
	 * Contructor for this object
	 * 
	 * @param id
	 * @param name
	 * @param author
	 * @param diet
	 * @param numOfLikes
	 * @param authorLink
	 * @param picture
	 */
	public RecipeSummary(Long id, String name, String author, String diet, Long numOfLikes, String authorLink, String picture) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.diet = diet;
		this.numberOfLikes = numOfLikes;
		this.authorLink = authorLink;
		this.picture = picture;
	}

	/**
	 * Contructor for this object.
	 * @param id
	 * @param name
	 * @param author
	 * @param diet
	 * @param numOfLikes
	 * @param authorLink
	 * @param picture
	 * @param likedByUser
	 */
	public RecipeSummary(Long id, String name, String author, String diet, Long numOfLikes, String authorLink, String picture, Long likedByUser) {
		super();
		this.id = id;
		this.name = name;
		this.author = author;
		this.diet = diet;
		this.numberOfLikes = numOfLikes;
		this.authorLink = authorLink;
		this.picture = picture;
		this.likedByUser = likedByUser;
	}

	/**
	 * @return true if this recipe has been liked by this user.
	 */
	public boolean isLikedByThisUser() {
		return likedByUser.intValue() > 0;
	}

	/**
	 * @return the likedByAUser
	 */
	public Long getLikedByAUser() {
		return likedByUser;
	}

	/**
	 * @param likedByAUser
	 *            the likedByAUser to set
	 */
	public void setLikedByAUser(Long likedByUser) {
		this.likedByUser = likedByUser;
	}

	/**
	 * @return the picture
	 */
	public String getPicture() {
		return picture;
	}

	/**
	 * @param picture
	 *            the picture to set
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the diet
	 */
	public String getDiet() {
		return diet;
	}

	/**
	 * @param diet
	 *            the diet to set
	 */
	public void setDiet(String diet) {
		this.diet = diet;
	}

	/**
	 * @return the numOfLikes
	 */
	public Long getNumberOfLikes() {
		return numberOfLikes;
	}

	/**
	 * @param numOfLikes
	 *            the numOfLikes to set
	 */
	public void setNumberOfLikes(Long numOfLikes) {
		this.numberOfLikes = numOfLikes;
	}

	/**
	 * @return the authorLink
	 */
	public String getAuthorLink() {
		return authorLink;
	}

	/**
	 * @param authorLink
	 *            the authorLink to set
	 */
	public void setAuthorLink(String authorLink) {
		this.authorLink = authorLink;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
