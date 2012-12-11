package com.safe.stack.service.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safe.stack.domain.Account;
import com.safe.stack.domain.LikedRecipe;
import com.safe.stack.repository.AccountRepository;
import com.safe.stack.repository.LikedRecipeRepository;
import com.safe.stack.service.AccountService;

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.safe.stack.service.AccountService#save(com.safe.stack.domain.Account)
     */
    public void save(Account acc) {
	accountRepository.save(acc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.safe.stack.service.AccountService#findByUserName()
     */
    public Account findByEmail(String name) {

	TypedQuery<Account> typedQuery = entityManager.createNamedQuery("Account.findByEmail",
		Account.class);
	typedQuery.setParameter("email", name);
	return typedQuery.getSingleResult();
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

}
