package com.CollectionServer.DataAnalysis;

import java.util.HashMap;

public abstract class AnalysisMethod
{
	public enum AnalysisType
	{
		SOW_AND_GROW;
	}
	
	//Map datapoint ID's to a cluster ID using the clustering algroithm implemented
	public HashMap<Integer, Integer> clusters;
	public AnalysisType analysisType;
	public AnalysisJob parentJob;
	
	public AnalysisMethod()
	{
		clusters = new HashMap<Integer, Integer>();
	}
	
	public abstract void start();
	
}
