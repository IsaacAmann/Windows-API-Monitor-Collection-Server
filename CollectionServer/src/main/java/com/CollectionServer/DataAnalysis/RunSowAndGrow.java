package com.CollectionServer.DataAnalysis;

import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;

import java.lang.Thread;
import jakarta.servlet.ServletContext;

public class RunSowAndGrow extends AnalysisMethod
{
	@Autowired
	ServletContext servletContext;
	
	public ArrayList<Integer> seedPoints;
	int epsilon;
	int minPoints;
	
	public RunSowAndGrow(ArrayList<Integer> seedPoints, int epsilon, int minPoints)
	{
		super();
		this.seedPoints = seedPoints;
		this.epsilon = epsilon;
		this.minPoints = minPoints;
	}
	
	@Override
	public void start()
	{
		
	}
	
	@Override 
	public void finish()
	{
		super.finish();
	}
	
	private class SowAndGrowThread extends Thread
	{
		
		@Override
		public void run()
		{
			
			finish();
		}
	}
}
