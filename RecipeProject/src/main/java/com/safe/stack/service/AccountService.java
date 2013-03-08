package com.safe.stack.service;

import com.safe.stack.domain.Account;

public interface AccountService {

    Account save(Account acc);

    Account findByEmail(String email);

    void likeARecipe(String userName, Long recipeId);

    void unlikeARecipe(String email, Long recipeId);

}
