package com.CollectionServer.DataAnalysis;

import org.springframework.beans.factory.annotation.Autowired;

import com.CollectionServer.DataPointRepository;
import com.CollectionServer.DataPointEntity;
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
		@Autowired
		private DataPointRepository dataPointRepository;
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
				seedFile.createNewFile();
				//Push seeds into seed file
				FileWriter seedWriter = new FileWriter(seedFile);
				for(int i = 0; i < seedPoints.size(); i++)
				{
					System.out.println(Integer.toString(seedPoints.get(i)));
					seedWriter.write(Integer.toString(seedPoints.get(i)));
					seedWriter.write("\n");
				}
				seedWriter.close();
				//Create input CSV file from database
				File dataPointFile = new File(directoryPath, "input.csv");
				dataPointFile.createNewFile();
				FileWriter dataWriter = new FileWriter(dataPointFile);
				
				ArrayList<DataPointEntity> dataPoints = new ArrayList<DataPointEntity>();
				for(DataPointEntity datapoint : parentJob.dataPointRepository.findAll())
				{
					dataPoints.add(datapoint);
				}
				
				for(int i = 0; i < dataPoints.size(); i++)
				{
					int column = 0;
					for(Float value : dataPoints.get(i).winAPIRatios.values())
					{
						if(column > 0)
						{
							dataWriter.write(", ");
						}
						dataWriter.write(Float.toString(value));
						column++;
					}
					dataWriter.write("\n");
				}
				dataWriter.close();
				//Run SowAndGrow 
			
				//Parse output and write clusters
				//Values should appear in the same order as the dataPoints arraylist
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finish();
		}
	}
}
