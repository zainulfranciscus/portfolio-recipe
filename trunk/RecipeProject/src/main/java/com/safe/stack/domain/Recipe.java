package com.safe.stack.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.springframework.util.AutoPopulatingList;

@Entity
@Table(name = "Recipe")
@NamedQueries({ @NamedQuery(name = "Recipe.findAll", query = "select r from Recipe r") })
public class Recipe {

	private Long id;
	private String name;
	private String author;
	private String diet;
	private String authorLink;
	private String picture;
	private int version;
	private Set<Account> account;
	private List<Ingredient> ingredients = new AutoPopulatingList<Ingredient>(Ingredient.class);

	/**
	 * @return the ingredients
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * @param ingredients
	 *            the ingredients to set
	 */
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	/**
	 * @return the account
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "LikedRecipe", joinColumns = @JoinColumn(name = "recipeId"), inverseJoinColumns = @JoinColumn(name = "email"))
	public Set<Account> getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(Set<Account> account) {
		this.account = account;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
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

	/**
	 * @return the name
	 */
	@Column(name = "name")
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
	@Column(name = "author")
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
	@Column(name = "diet")
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
	 * @return the authorLink
	 */
	@Column(name = "authorLink")
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
	 * @return the picture
	 */
	@Column(name = "picture")
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

}
