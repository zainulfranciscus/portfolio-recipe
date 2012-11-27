package com.safe.stack.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;

@Entity
@Table (name = "LikedRecipe")
public class LikedRecipe {
	
	private Long id;
	private Long recipeId;
	private String userName;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue (strategy = IDENTITY)
	@Column (name = "id")
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the recipeId
	 */
	@Column (name = "recipeId")
	public Long getRecipeId() {
		return recipeId;
	}
	/**
	 * @param recipeId the recipeId to set
	 */
	public void setRecipeId(Long recipeId) {
		this.recipeId = recipeId;
	}
	/**
	 * @return the email
	 */
	@Column (name = "userName")
	public String getUserName() {
		return userName;
	}
	/**
	 * @param email the email to set
	 */
	public void setUserName(String name) {
		this.userName = name;
	}
	
	

}
