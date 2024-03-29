package com.CollectionServer.DataAnalysis;

import java.util.HashMap;
import java.util.Date;


public abstract class AnalysisMethod
{
	//Map datapoint ID's to a cluster ID using the clustering algroithm implemented
	public HashMap<Integer, Integer> clusters;
	public AnalysisJob parentJob;
	public AnalysisJob.AnalysisType analysisType;
	
	public AnalysisMethod()
	{
		clusters = new HashMap<Integer, Integer>();
	}
	
	public void finish()
	{
		parentJob.clusters = this.clusters;
		parentJob.timeFinished = new Date();
		parentJob.jobStatus = AnalysisJob.JobStatus.COMPLETED;
		
		parentJob.analysisJobService.handleJobComplete(parentJob);
	}
	
	public abstract void start();
	
}
