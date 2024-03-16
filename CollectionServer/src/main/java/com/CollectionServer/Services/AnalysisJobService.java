package com.CollectionServer.Services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentHashMap;

import com.CollectionServer.DataAnalysis.*;

@Service
public class AnalysisJobService
{
	@Autowired
    private AnalysisJobRepository analysisJobRepository;
    
	//Thread pool for handling analysis jobs
	private static ExecutorService threadPool = Executors.newCachedThreadPool();
	
	private static ConcurrentHashMap<Integer, AnalysisJob> runningJobs = new ConcurrentHashMap<Integer, AnalysisJob>();
	
	public void submitAnalysisJob(AnalysisJob newJob)
	{
		//Save job to database
		analysisJobRepository.save(newJob);
		
		//Put job into hashmap
		runningJobs.put(newJob.Id, newJob);
		
		//Start job
		newJob.startJob();
	}
	
	public void handleJobComplete(AnalysisJob job)
	{
		if(job.jobStatus == AnalysisJob.JobStatus.COMPLETED)
		{
			//Save to data base
			analysisJobRepository.save(job);
			//Find and remove from hashmap
			runningJobs.remove(job.Id);
		}
	}
}


