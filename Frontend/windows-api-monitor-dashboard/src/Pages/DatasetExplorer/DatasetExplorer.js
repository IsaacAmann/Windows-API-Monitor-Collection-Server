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

import Navbar from "../../Components/Navbar/Navbar.js"

import APICallContainer from "../../APICallContainer.js";

function DatasetExplorer()
{
	const loginInfo = useContext(LoginInfoContext);
	
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
	
	function FullDataTable()
	{
		const [tableState, setTableState] = useState( 
		{
			requestedFirst: false,
			isLoading: true,
			rows: [],
			total: 0,
			page: 1, 
			pageSize: 10
		});
		
		const [firstRequest, setFirstRequest] = useState(null);
		
		if(tableState.requestedFirst == false)
		{
			var response = APICallContainer.getDataPage(loginInfo.token, 0, 10).then(
				function(value)
				{			
					setFirstRequest(value);
					//console.log(value);
					setFirstRequest(value);
					setTableState({...tableState, isLoading: false})
				}
			);
		}
		
		
		
		if(tableState.isLoading == false)
		{
			//Grabbing windows api counts
			var apiRows = [];
		
			var dataArray = firstRequest.values.content;
			
			//console.log(dataArray[0].WinAPICounts);
			//Set up columns
			Object.keys(dataArray[0].WinAPICounts).forEach(function(key,index) 
			{
				apiRows[index] = {};
				apiRows[index].field = key;
				apiRows[index].headerName = key;
				apiRows[index].width = 200;
			});
		
			var colDefs = [
				{field: 'id', headerName: "id", width: 50},
				{field: 'executablePath', headerName: "executablePath", width: 1000},
				{field: 'origin', headerName: 'originClientId', width: 325},
				{field: 'dateCreated', headerName: 'dateCreated', width: 150}
			];
		
			var columns: GridColDef[] = colDefs;
			return(
				<>
					<DataGrid
						rows={tableState.rows} 
						columns={columns} 
						sx={{my:3}}
					/>
				</>
			);
		}
		else
		{
			return(
				<div style={{display: "flex", justifyContent: "center"}}>
					<CircularProgress color="secondary" sx={{my: 10}}/>
				</div>
			);
		}
	}


	if(loginInfo.token != null)
	{
		return(
			<>
					<Navbar/>
					<p>Dataset Explorer</p>
					<FullDataTable/>
			</>
		);
	}
	else
	{
		return (
			<>
				<Navbar/>
				<p>Please login to access this</p>
			</>
		);
	}
}


export default DatasetExplorer;