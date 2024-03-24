import React from 'react'
import {useContext} from 'react'
import {useState, useCallback} from 'react'

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
import Button from '@mui/material/Button';


import Navbar from "../../Components/Navbar/Navbar.js"

import APICallContainer from "../../APICallContainer.js";

function DataAnalysis()
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
		const [selectedElement, setSelectedElement] = useState(null);

		function InspectJobButton()
		{
			if(selectedElement != null)
			{
				return(
					<>
						<Button variant="contained">Inspect Selected Job</Button>
					</>
				);
			}
			else
			{
				return(null);
			}
		}
		
		function handlePageChange(model, details)
		{
			setTableState({...tableState, page: model.page, loadPage: true, isLoading: true});
		}
		
		/*
		function handleSelectionChange(model, details)
		{
			setSelectedElement(tableState.rows[model[0]]);
			console.log(InspectJobButton);
		}
		*/
		
		const handleSelectionChange = useCallback((model, details) => {
			//setSelectedElement(tableState.rows[model[0]]);
			//console.log(tableState.rows[model[0]]);
			//console.log(model);
			//model contains id's and not indexs
			let value = tableState.rows.filter((e) => {return e.id === model[0]});
			setSelectedElement(value[0]);
		});
		
		//Request a page from the server
		if(tableState.loadPage == true)
		{
			tableState.loadPage = false;

			var response = APICallContainer.getAnalysisJobPage(loginInfo.token, tableState.page, elementsPerPage).then(
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
						rowObjects[i].JobStatus = dataArray[i].jobStatus;
						rowObjects[i].TimeStarted = dataArray[i].timeStarted;
						rowObjects[i].TimeFinished = dataArray[i].timeFinished;
						rowObjects[i].AnalysisType = dataArray[i].analysisType;
					}
					
					var rows: GridRowsProp = rowObjects;
					setTableState({...tableState, isLoading: false, currentResponse: value.values.content, rows: rows, total: value.values.totalElements})
				}
			);
		}
		
		if(tableState.isLoading == false)
		{
			var dataArray = tableState.currentResponse;
			//Set up columns
			var colDefs = [
				{field: 'id', headerName: "id", width: 50},
				{field: 'TimeStarted', headerName: 'TimeStarted', width: 150},
				{field: 'TimeFinished', headerName: 'TimeFinished', width: 150},
				{field: 'AnalysisType', headerName: 'AnalysisType', width: 150},
				{field: 'JobStatus', headerName: 'JobStatus', width: 150}
			

			];
					
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
						onRowSelectionModelChange={handleSelectionChange}
					/>
					<InspectJobButton/>
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
					<p>Data Analysis</p>
					<FullDataTable />
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


export default DataAnalysis;