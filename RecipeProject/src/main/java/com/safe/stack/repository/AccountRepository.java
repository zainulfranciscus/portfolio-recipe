package com.safe.stack.repository;

import org.springframework.data.repository.CrudRepository;

import com.safe.stack.domain.Account;

public interface AccountRepository extends CrudRepository<Account, Long>{
	
	Account findByEmail(String email);
	Account findByEmailAndUserName(String email, String userName);	

}
