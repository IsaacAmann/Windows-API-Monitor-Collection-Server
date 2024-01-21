package com.CollectionServer;

import org.springframework.data.repository.CrudRepository;

public interface DataPointRepository extends CrudRepository<DataPointEntity, Integer>
{
	DataPointEntity findById(int Id);
}
