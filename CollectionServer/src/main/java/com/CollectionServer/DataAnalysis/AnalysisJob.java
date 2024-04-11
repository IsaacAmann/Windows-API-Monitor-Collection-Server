package com.CollectionServer.DataAnalysis;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import com.CollectionServer.DataPointRepository;
import com.CollectionServer.Services.AnalysisJobService;

import java.util.Date;

import java.util.Map;
import java.util.HashMap;

@Entity
public class AnalysisJob 
{
	public enum JobStatus
	{
		SUBMITTED,
		RUNNING,
		COMPLETED,
		FAILED;
	}
	
	public enum AnalysisType
	{
		DBSCAN_COSINE,
		SOW_AND_GROW;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;
	
	@Transient
	public AnalysisMethod analysisMethod;
	
	//Maps data point ID's to cluster ID's generated by the clustering algrorithm
	@ElementCollection(fetch = FetchType.EAGER)
	public Map<Integer, Integer> clusters;
	
	//Map for storing parameters passed to the analysis method for later reference
	@ElementCollection(fetch = FetchType.EAGER)
	public Map<String, String> parameters;
	
	@Transient
	public DataPointRepository dataPointRepository;
	
	@Transient 
	public AnalysisJobService analysisJobService;
	@JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
	public Date timeStarted;
	@JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
	public Date timeFinished;
		
	public AnalysisType analysisType;
	
	public JobStatus jobStatus;
	
	public AnalysisJob()
	{
		
	}
	
	public AnalysisJob(AnalysisMethod analysisMethod, DataPointRepository dataPointRepository, AnalysisJobService analysisJobService)
	{
		analysisMethod.parentJob = this;
		this.analysisMethod = analysisMethod;
		parameters = new HashMap<String, String>();
		this.analysisType = analysisMethod.analysisType;
		jobStatus = JobStatus.SUBMITTED;
		this.dataPointRepository = dataPointRepository;
		this.analysisJobService = analysisJobService;
	}
	
	public void startJob()
	{
		if(jobStatus == JobStatus.SUBMITTED)
		{
			//Start running analysisMethod
			analysisMethod.start();
			jobStatus = JobStatus.RUNNING;
			timeStarted = new Date();
		}
	}
}
