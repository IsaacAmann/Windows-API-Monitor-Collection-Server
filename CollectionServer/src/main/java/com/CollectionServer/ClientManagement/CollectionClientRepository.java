package com.CollectionServer.ClientManagement;

import org.springframework.data.repository.CrudRepository;

public interface CollectionClientRepository extends CrudRepository<CollectionClient, Integer>
{
	CollectionClient findByUUID(UUID uuid);
}
