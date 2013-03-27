package com.safe.stack.service.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safe.stack.domain.Account;
import com.safe.stack.domain.LikedRecipe;
import com.safe.stack.repository.AccountRepository;
import com.safe.stack.repository.LikedRecipeRepository;
import com.safe.stack.service.AccountService;
import com.safe.stack.service.security.RecipeUser;

@Service("accountService")
@Repository
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private LikedRecipeRepository likedRecipeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private AuthenticationManager authManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.safe.stack.service.AccountService#save(com.safe.stack.domain.Account)
     */
    public Account save(Account acc) {
	return accountRepository.save(acc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.safe.stack.service.AccountService#findByUserName()
     */
    @Transactional(readOnly = true)
    public Account findByEmail(String email) {

	return accountRepository.findByEmail(email);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.safe.stack.service.AccountService#save(java.lang.String, int)
     */
    @Override
    public void likeARecipe(String userName, Long recipeId) {
	LikedRecipe likedRecipe = new LikedRecipe();
	likedRecipe.setEmail(userName);
	likedRecipe.setRecipeId(recipeId);
	likedRecipeRepository.save(likedRecipe);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.safe.stack.service.AccountService#unlikeARecipe(java.lang.String,
     * java.lang.Long)
     */
    @Override
    public void unlikeARecipe(String email, Long recipeId) {

	Query query = entityManager
		.createQuery("delete from LikedRecipe where recipeId = :recipeId and email = :email");
	query.setParameter("recipeId", recipeId);
	query.setParameter("email", email);
	query.executeUpdate();

	entityManager.clear();

    }

    /* returns true if the current user is not logged in.
     * 
     * @see com.safe.stack.service.AccountService#isAnonymousUser()
     */
    @Override
    public boolean isAnonymousUser() {
	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	
	return principal.toString().equals("anonymousUser");
	
    }

    /* return user who is currently logged in. 
     * 
     * @see com.safe.stack.service.AccountService#getUser()
     */
    @Override
    public RecipeUser getUser() {
	
	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	if(principal instanceof RecipeUser){
	    return (RecipeUser) principal;
	}
	return null;
    }

    /* Authenticate a user based on the provided user name and password.
     * 
     * @see com.safe.stack.service.AccountService#authenticate(java.lang.String, java.lang.String)
     */
    @Override
    public void authenticate(String userName, String password) {
	UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
		userName, password);

	Authentication auth = authManager.authenticate(token);
	Object principal = auth.getPrincipal();
	SecurityContextHolder.getContext().setAuthentication(auth);
	
    }

    /* (non-Javadoc)
     * @see com.safe.stack.service.AccountService#signUpAUser(java.lang.String, java.lang.String)
     */
    @Override
    public void signUpAUser(String userName, String password) {
	
	Account acc = Account.getNewAccount(userName, password);
	save(acc);
	authenticate(acc.getUserName(), acc.getPassword());
	
    }
    
    

}
