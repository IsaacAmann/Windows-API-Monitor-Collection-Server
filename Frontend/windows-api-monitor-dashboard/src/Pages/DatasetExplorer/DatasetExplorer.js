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
	const elementsPerPage = 25;
	
	function FullDataTable()
	{
		const [tableState, setTableState] = useState( 
		{
			currentResponse: null,
			loadPage: true,
			isLoading: true,
			rows: [],
			total: 0,
			page: 0, 
			pageSize: elementsPerPage
		});
		
		function handlePageChange(model, details)
		{
			setTableState({...tableState, page: model.page, loadPage: true, isLoading: true});
		}
		//Request a page from the server
		if(tableState.loadPage == true)
		{
			tableState.loadPage = false;

			var response = APICallContainer.getDataPage(loginInfo.token, tableState.page, elementsPerPage).then(
				function(value)
				{			
					console.log(value);
					//Set up rows
					var rowObjects = [];
					var dataArray = value.values.content;
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
					setTableState({...tableState, isLoading: false, currentResponse: value.values.content, rows: rows, total: value.values.totalElements})
				}
			);
		}
		
		if(tableState.isLoading == false)
		{
			//Grabbing windows api counts
			var apiRows = [];
		
			var dataArray = tableState.currentResponse;
			
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
			
			colDefs = colDefs.concat(apiRows);
		
			var columns: GridColDef[] = colDefs;
			
			return(
				<>
					<DataGrid
						rows={tableState.rows}
						loading={tableState.isLoading} 
						columns={columns} 
						paginationMode="server"
						sx={{my:3}}
						paginationModel={{page: tableState.page, pageSize: tableState.pageSize}}
						onPaginationModelChange={handlePageChange}
						pageSizeOptions={[elementsPerPage]}
						rowCount={tableState.total}
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
