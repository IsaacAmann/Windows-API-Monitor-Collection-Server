package com.CollectionServer.DataAnalysis;

import org.springframework.beans.factory.annotation.Autowired;

import com.CollectionServer.DataPointRepository;
import com.CollectionServer.DataPointEntity;
import java.util.ArrayList;

import java.lang.Thread;
import java.lang.Process;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter; 
import java.util.Scanner;

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
		this.analysisType = AnalysisJob.AnalysisType.SOW_AND_GROW;
		
	}
	
	@Override
	public void start()
	{
		//Place parameters into parent job object
		parentJob.parameters.put("epsilon", Integer.toString(epsilon));
		parentJob.parameters.put("MinimumPoints", Integer.toString(minPoints));
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
				
				ArrayList<Integer> seedPointIndexes = new ArrayList<Integer>();
				//Create input CSV file from database
				File dataPointFile = new File(directoryPath, "input.csv");
				dataPointFile.getParentFile().mkdirs();
				dataPointFile.createNewFile();
				FileWriter dataWriter = new FileWriter(dataPointFile);
				
				ArrayList<DataPointEntity> dataPoints = new ArrayList<DataPointEntity>();
				for(DataPointEntity datapoint : parentJob.dataPointRepository.findAll())
				{
					dataPoints.add(datapoint);
					for(int i = 0; i < seedPoints.size(); i++)
					{
						if(datapoint.id == seedPoints.get(i))
						{
							//Add seed point's index into seed point indexes
							seedPointIndexes.add(dataPoints.size()-1);
						}
					}
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
				
				//Create seed file
				File seedFile = new File(directoryPath, "seeds");
				//seedFile.getParentFile().mkdirs();
				seedFile.createNewFile();
				//Push seeds into seed file
				FileWriter seedWriter = new FileWriter(seedFile);
				for(int i = 0; i < seedPointIndexes.size(); i++)
				{
					System.out.println(Integer.toString(seedPointIndexes.get(i)));
					seedWriter.write(Integer.toString(seedPointIndexes.get(i)));
					seedWriter.write("\n");
				}
				seedWriter.close();
				
				//Run SowAndGrow 
				String command = "./analysis/bsng -z 5 -t 1 ";
				String commandDirectory = "./analysis/job" + parentJob.Id +"/";
				command = command + "-o " + commandDirectory + "out.txt ";
				command = command + "-i " + commandDirectory + "input.csv ";
				command = command + "-e " + epsilon +" ";
				command = command + "-m " + minPoints +" ";
				command = command + "-u " + commandDirectory + "clusterOut.csv ";
				command = command + "-l " + commandDirectory + "seeds ";
				
				System.out.println(command);
				
				Process process = Runtime.getRuntime().exec(command);
				process.waitFor();
				
				//Parse output and write clusters
				//Values should appear in the same order as the dataPoints arraylist
				File outFile = new File(directoryPath, "clusterOut.csv");
				Scanner outReader = new Scanner(outFile);
				int currentIndex = 0;
				while(outReader.hasNextLine())
				{
					String currentLine = outReader.nextLine();
					//Get cluster label from line (first entry in csv row)
					String[] subStrings = currentLine.split(",");
					int clusterLabel = Integer.parseInt(subStrings[0]);
					System.out.println(clusterLabel);
					
					//If datapoint clustered, place it in clusters map
					if(clusterLabel != 0)
					{
						clusters.put(dataPoints.get(currentIndex).id, clusterLabel);
					}
					
					currentIndex++;
				}
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finish();
		}
	}
}
