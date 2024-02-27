package com.CollectionServer.UserManagement;

import com.CollectionServer.ClientManagement.CollectionClient;
import org.springframework.data.repository.CrudRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAccountRepository extends CrudRepository<UserAccount, Integer>
{
	UserAccount findByUsername(String username);
	Page<UserAccount> findAll(Pageable pageable);

}
