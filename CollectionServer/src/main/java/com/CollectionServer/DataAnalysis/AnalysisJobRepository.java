package com.CollectionServer.DataAnalysis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; 

public interface AnalysisJobRepository extends CrudRepository<AnalysisJob, Integer>
{
	AnalysisJob findById(int Id);
	public Page<AnalysisJob> findAll(Pageable pageable);
}
