package com.CollectionServer.ClientManagement;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CollectionClientRepository extends CrudRepository<CollectionClient, Integer>
{
	CollectionClient findByClientID(UUID uuid);

	Page<CollectionClient> findAll(Pageable pageable);
}
