package com.safe.stack.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Iterator;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.AutoPopulatingList;

/**
 * A POJO mapped to the RECIPE table. 
 * JsonIgnoreProperties annotation records getter methods that should not be marshalled into key-value pairs
 * in a JSON message. 
 * 
 * @author Zainul Franciscus
 * 
 */
@Entity
@Table(name = "Recipe")
@JsonIgnoreProperties({"authorNameWithoutSpace","formattedAuthorURL","numberOfLikes"})
public class Recipe {

	private static final int MAX_LENGTH_AUTHOR_LINK = 69;

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
	 * Indicate whether a recipe has been liked by a user. 0 means that a user
	 * has not liked this recipe; 1 otherwise.
	 */
	private Long likedByUser = new Long(0);
	
	/**
	 * The number of users who have liked a recipe 
	 */
	private Long numberOfLikes;

	/**
	 * @return the ingredients
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "RecipeIngredient", joinColumns = { @JoinColumn(name = "recipeId") }, inverseJoinColumns = { @JoinColumn(name = "ingredientId") })
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
	@NotEmpty(message = "{validation.recipeName.NotEmpty.message}")
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
	@NotEmpty(message = "{validation.recipeAuthor.NotEmpty.message}")
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

	public boolean isLikedByUser(String userEmail) {
		AccountPredicate accPredicate = new AccountPredicate();
		accPredicate.email = userEmail;

		Iterator accountIterator = new FilterIterator(account.iterator(), accPredicate);
		return accountIterator.hasNext();
	}

	@Transient
	public String getAuthorNameWithoutSpace() {
		return this.author.toLowerCase().replaceAll("\\s+", "").replaceAll("[^\\dA-Za-z ]", "");
	}

	@Transient
	public String getFormattedAuthorURL() {
		if (StringUtils.isNotEmpty(authorLink) && authorLink.length() <= MAX_LENGTH_AUTHOR_LINK) {
			return authorLink;
		}

		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(authorLink.substring(0, MAX_LENGTH_AUTHOR_LINK - 3)).append("...");

		return strBuilder.toString();
	}
}


class AccountPredicate implements Predicate {

	String email = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
	 */
	@Override
	public boolean evaluate(Object obj) {

		if (obj instanceof Account) {
			Account acc = (Account) obj;

			return acc.getEmail().equalsIgnoreCase(email);
		}

		return false;
	}

}
