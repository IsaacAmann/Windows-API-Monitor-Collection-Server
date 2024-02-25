package com.CollectionServer.ClientManagement;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CollectionClientRepository extends CrudRepository<CollectionClient, Integer>
{
	CollectionClient findByClientID(UUID uuid);
}
