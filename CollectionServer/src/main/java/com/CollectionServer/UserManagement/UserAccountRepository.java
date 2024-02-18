package com.CollectionServer.UserManagement;

import org.springframework.data.repository.CrudRepository;

public interface UserAccountRepository extends CrudRepository<UserAccount, Integer>
{
	UserAccount findByUsername(String username);
}
