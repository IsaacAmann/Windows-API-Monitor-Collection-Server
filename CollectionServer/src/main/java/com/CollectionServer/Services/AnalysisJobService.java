package com.CollectionServer.Services;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.CollectionServer.DataAnalysis.*;

@Service
public class AnalysisJobService
{
	//Thread pool for handling analysis jobs
	private static ExecutorService threadPool = Executors.newCachedThreadPool();
	
	public void submitAnalysisJob(AnalysisJob newJob)
	{
	
	}
}


