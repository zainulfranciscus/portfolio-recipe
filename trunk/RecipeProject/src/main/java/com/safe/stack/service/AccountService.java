package com.safe.stack.service;

import com.safe.stack.domain.Account;

public interface AccountService {

	void save(Account acc);

	Account findByUserName(String name);

	void likeARecipe(String userName, Long recipeId);



}
