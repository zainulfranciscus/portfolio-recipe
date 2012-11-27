package com.safe.stack.service;

import com.safe.stack.domain.Account;

public interface AccountService {

	void save(Account acc);

	Account findByUserName(String name);

	void saveRecipe(String userName, Long recipeId);



}
