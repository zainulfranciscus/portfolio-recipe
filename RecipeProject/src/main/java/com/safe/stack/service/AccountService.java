package com.safe.stack.service;

import com.safe.stack.domain.Account;
import com.safe.stack.service.security.RecipeUser;

/**
 * An interface intended to define a number of methods for authenticating a user, 
 * creating an account, and retrieving users relation information.
 * 
 * @author Zainul Franciscus
 *
 */
public interface AccountService {

    /**
     * Create a new user account 
     * @param acc an instance of Account
     * @return the newly created account
     */
    Account save(Account acc);

    /**
     * @param email of a user
     * @return an account with an email adress that matches the provided email
     */
    Account findByEmail(String email);

    /**
     * set the like indicator of a recipe to true
     * 
     * @param email address of a user 
     * @param recipeId an id of a recipe
     */
    void likeARecipe(String userName, Long recipeId);

    /**
     * set the like indicator of a recipe to false
     * 
     * @param email address of a user 
     * @param recipeId an id of a recipe
     */
    void unlikeARecipe(String email, Long recipeId);
    
    /**
     * @return true if this user has not been authenticated.
     */
    boolean isAnonymousUser();
    
    /**
     * @return a user who has been authenticated.
     */
    RecipeUser getUser();
    
    /**
     * authenticate a user based on the provided user name and password
     * @param userName 
     * @param password
     */
    void authenticate(String userName, String password);

}
