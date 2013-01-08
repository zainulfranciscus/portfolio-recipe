package com.safe.stack.domain;

import java.io.Serializable;

public class RecipeSummary implements Serializable{

    private String name;

    private String author;

    private String diet;

    private int numOfLikes;

    private String authorLink;

    private String picture;
    
    public RecipeSummary(String name, String author, String diet, int numOfLikes,
	    String authorLink, String picture) {
	super();
	this.name = name;
	this.author = author;
	this.diet = diet;
	this.numOfLikes = numOfLikes;
	this.authorLink = authorLink;
	this.picture = picture;
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
    public int getNumOfLikes() {
	return numOfLikes;
    }

    /**
     * @param numOfLikes
     *            the numOfLikes to set
     */
    public void setNumOfLikes(int numOfLikes) {
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

}
