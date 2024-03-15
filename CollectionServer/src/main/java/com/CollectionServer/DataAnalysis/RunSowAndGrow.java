package com.CollectionServer.DataAnalysis;

import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;

import java.lang.Thread;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter; 

public class RunSowAndGrow extends AnalysisMethod
{

	
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
		//Execute SowAndGrow thread
		SowAndGrowThread thread = new SowAndGrowThread();
		thread.start();
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
			String directoryPath = System.getProperty("user.dir") + "/analysis/job";
			directoryPath = directoryPath + parentJob.Id;
			System.out.println(directoryPath);
			try
			{
				//Create seed file
				File seedFile = new File(directoryPath, "seeds");
				seedFile.getParentFile().mkdirs();
				if(seedFile.createNewFile())
				{
					System.out.println("Created");
				}
				else
				{
					System.out.println("Failed");
				}
				FileWriter seedWriter = new FileWriter(seedFile);
				//Create input CSV file from database
			
				//Push seeds into seed file
			
				//Run SowAndGrow 
			
				//Parse output and write clusters
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finish();
		}
	}
}
