package com.CollectionServer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DataPointRepository extends CrudRepository<DataPointEntity, Integer>
{
	DataPointEntity findById(int Id);
	public Page<DataPointEntity> findAll(Pageable pageable);
}
