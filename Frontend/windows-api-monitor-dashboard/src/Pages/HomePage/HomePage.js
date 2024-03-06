import "./HomePage.css"

import React from 'react'
import {useContext} from 'react'
import {useState} from 'react'

import {LoginInfoContext} from "../../App.js";

import Box from "@mui/material/Box";
import Container from "@mui/material/Container"
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import CircularProgress from '@mui/material/CircularProgress';
import LinearProgress from '@mui/material/LinearProgress';
import { DataGrid, GridRowsProp, GridColDef } from '@mui/x-data-grid';

import APICallContainer from "../../APICallContainer.js";





import Navbar from "../../Components/Navbar/Navbar.js"

function SampleTable()
{
	const [isLoading, setIsLoading] = useState(true);
	const [sampleResult, setSampleResult] = useState(null);
	
	if(isLoading)
	{
		var response = APICallContainer.getDataSample().then(
			function(value)
			{			
				setIsLoading(false);
				setSampleResult(value);
				//console.log(value);

			}
		);
	}
	
	if(isLoading == true)
	{
		return(
			<div style={{display: "flex", justifyContent: "center"}}>
			<CircularProgress color="secondary" sx={{my: 10}}/>
			</div>
		);
	}
	else
	{
		//Grabbing windows api counts
		var apiRows = [];
		
		var dataArray = sampleResult.values.content;
		
		//console.log(dataArray[0].WinAPICounts);
		//Set up columns
		Object.keys(dataArray[0].WinAPICounts).forEach(function(key,index) 
		{
			apiRows[index] = {};
			apiRows[index].field = key;
			apiRows[index].headerName = key;
			apiRows[index].width = 200;
		});
		
		console.log(apiRows);
		
		var colDefs = [
				{field: 'id', headerName: "id", width: 50},
				{field: 'executablePath', headerName: "executablePath", width: 1000},
				{field: 'origin', headerName: 'originClientId', width: 325},
				{field: 'dateCreated', headerName: 'dateCreated', width: 150}
			];
		
		colDefs = colDefs.concat(apiRows);
		//console.log(colDefs);
		
		var columns: GridColDef[] = colDefs;
		
		//Set up rows
		var rowObjects = [];
		console.log(dataArray);

		for(let i = 0; i < dataArray.length; i++)
		{
			rowObjects[i] = {};
			rowObjects[i].id = dataArray[i].id;
			rowObjects[i].executablePath = dataArray[i].executablePath;
			rowObjects[i].origin = dataArray[i].originClientId;
			rowObjects[i].dateCreated = dataArray[i].dateCreated;
			//Get winapi values
			Object.keys(dataArray[i].WinAPICounts).forEach(function(key,index)
			{
				rowObjects[i][key] = dataArray[i].WinAPICounts[key];
			});
		}
		var rows: GridRowsProp = rowObjects;
		
		
		return(
			<>
				<DataGrid rows={rows} columns={columns} sx={{my: 5}}/>
			</>
		);
	}
}

function HomePage()
{
	
	
	return(
		<>
			<Navbar/>
			<Container maxWidth="lg">
				<Typography variant="h3">
					Description
				</Typography>
				<Typography>
					A user-level process monitor that collects Windows API usage data from Windows pocesses for behavior analysis. A sample
					of the dataset can be found below:
				</Typography>
				<SampleTable/>
				<Divider />
				
				<Typography variant="h3">
					Motivation
				</Typography>
				<Typography>
					The system creates data points that consist of counts of how many times a process called a monitored Windows API function. Using cluster
					analysis, it may be possible to group processes by their behavior. The main motivation of this would be malware detection. For example, ransomware
					would make a large amount of file access API calls. Other programs that made similiar numbers of file access calls would cluster with the ransomware sample.
					Other processes that cluster with malware samples may also be malware.
				</Typography>
				<Divider />
				
				<Typography variant="h3">
					How it Works
				</Typography>
				<Typography>
					The process monitor obtains a list of all processes, creating an object for each to contain collected information
				</Typography>
				<Typography variant="h5">
					DLL Injection
				</Typography>
				<Typography>
					Each monitored process is injected with a DLL containing code for hooking monitored Windows API functions and for 
					communicating with the monitor. I used the common technique of opening a remote thread on the process that would then call 
					LoadLibrary to load the DLL.
				</Typography>
				
				<Typography variant="h5">
					Function Hooking
				</Typography>
				<Typography>
					Function hooking was implemented using the Microsoft Detours library. The hook simply increments the counter for 
					the corresponding Windows API function. 
				</Typography>
				
				<Typography variant="h5">
					Interprocess Communication
				</Typography>
				<Typography>
					Initially used a named pipe to use message passing for communication. Switched over to using shared memory instead.
				</Typography>
				
				<Typography variant="h5">
					Posting Datapoints
				</Typography>
				<Typography>
					Collected data is posted to the server through HTTP using Curl.
				</Typography>
				<Divider />
				
			</Container>
		</>
	);
}

export default HomePage;