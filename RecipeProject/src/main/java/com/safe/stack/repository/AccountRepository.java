package com.safe.stack.repository;

import org.springframework.data.repository.CrudRepository;

import com.safe.stack.domain.Account;

/**
 * An implementation of  CrudRepository intended to provide CRUD operations 
 * for Account table
 * 
 * @author Zainul Franciscus
 *
 */
public interface AccountRepository extends CrudRepository<Account, Long>{
	
	/**
	 * Find an account based on the provided email
	 * 
	 * @param email of a user
	 * @return An account object that matches the provided email.
	 */
	Account findByEmail(String email);
	/**
	 * Find an account based on the provided email and user name
	 * @param email of a user
	 * @param userName of a user
	 * @return an Account object that matches the provided email and username
	 */
	Account findByEmailAndUserName(String email, String userName);	

}
