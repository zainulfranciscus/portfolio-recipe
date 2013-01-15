package com.safe.stack.domain;

import java.io.Serializable;

public class RecipeSummary implements Serializable{
    
    private Long id;

    private String name;

    private String author;

    private String diet;

    private Long numOfLikes;

    private String authorLink;

    private String picture;
    
    private Long likedByUser = new Long(0);
    
    public RecipeSummary(Long id, String name, String author, String diet, Long numOfLikes,
	    String authorLink, String picture) {
	super();
	this.id = id;
	this.name = name;
	this.author = author;
	this.diet = diet;
	this.numOfLikes = numOfLikes;
	this.authorLink = authorLink;
	this.picture = picture;	
    }
    
    public RecipeSummary(Long id, String name, String author, String diet, Long numOfLikes,
	    String authorLink, String picture, Long likedByUser) {
	super();
	this.id = id;
	this.name = name;
	this.author = author;
	this.diet = diet;
	this.numOfLikes = numOfLikes;
	this.authorLink = authorLink;
	this.picture = picture;
	this.likedByUser = likedByUser;
    }

    
    public boolean isLikedByThisUser()
    {
	return likedByUser.intValue() > 0;
    }
    /**
     * @return the likedByAUser
     */
    public Long getLikedByAUser() {
        return likedByUser;
    }


    /**
     * @param likedByAUser the likedByAUser to set
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
     * @param picture the picture to set
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
    public Long getNumOfLikes() {
	return numOfLikes;
    }

    /**
     * @param numOfLikes
     *            the numOfLikes to set
     */
    public void setNumOfLikes(Long numOfLikes) {
	this.numOfLikes = numOfLikes;
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
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    

}
