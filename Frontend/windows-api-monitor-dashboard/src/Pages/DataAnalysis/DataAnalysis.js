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
import Backdrop from '@mui/material/Backdrop';
import { FormControl } from '@mui/material';
import Select from '@mui/material/Select';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import TextField from '@mui/material/TextField';


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

		const [submitJobActive, setSubmitJobActive] = useState(false);
		function SubmitJobForm()
		{
			const [inputValues, setInputValues] = useState(
			{
				epsilon: null,
				minPoints: null,
				seedPoints: [],
				jobType: null
			}
			);
			
			const [currentSeedPoint, setCurrentSeedPoint] = useState(null);
			
			function handleSubmit()
			{
				console.log(inputValues);
				var response = APICallContainer.postAnalysisJob(loginInfo.token, inputValues).then(
					function(value)
					{			
						console.log(value);
					}
				);
			}
			
			function addSeed()
			{
				if(currentSeedPoint != null || currentSeedPoint != undefined)
				{
					console.log(currentSeedPoint);
					let newArray = inputValues.seedPoints;
					newArray.push(currentSeedPoint);
					setInputValues({...inputValues, seedPoints: newArray});
					setCurrentSeedPoint(0);
				}
			}
			
			if(submitJobActive == true)
			{
				return(
					<>
						<Backdrop sx={{color: '#fff', zIndex: 5, display: "block"}} open={submitJobActive} >
							<Button variant="contained" onClick={() => setSubmitJobActive(false)}>Close</Button>
							<Container sx={{bgcolor: "grey", maxWidth: "lg"}}>
								<Typography variant="h3"> Submit New Analysis Job </Typography>
								<FormControl fullWidth size="medium">
									<InputLabel id="analysisType">Analysis Type</InputLabel>
									<Select  labelId="analysisType" value={inputValues.jobType} label="Job Type" required onChange={(e) => setInputValues({...inputValues, jobType: e.target.value})}>
										<MenuItem value={"SOW_AND_GROW"}>Sow and Grow</MenuItem>
										<MenuItem value={"DBSCAN_COSINE"}>DBSCAN with Cosine Similarity</MenuItem>
									</Select>
									
									<TextField  type="number" label="Epsilon" value={inputValues.epsilon} required onChange={(e) => setInputValues({...inputValues, epsilon: Number(e.target.value)})} />
									<TextField  type="number" label="minPoints" value={inputValues.minPoints} required onChange={(e) => setInputValues({...inputValues, minPoints: Number(e.target.value)})} />
									<Container sx={{display: "flex"}}>
										<TextField type="number" label="seedPoints" value={currentSeedPoint} onChange={(e) => setCurrentSeedPoint(Number(e.target.value))}  />
										<Button variant="outline" onClick={addSeed}>Add Seed</Button>
									</Container>
									
									<Button variant="outline" onClick={handleSubmit}>Submit Job</Button>
								</FormControl>
							</Container>
						</Backdrop>
					</>
				);
			}
			else
			{
				return null;
			}
		}
		
		function InspectJobButton()
		{
			const [displayJob, setDisplayJob] = useState(false);
			
			function handleClick()
			{
				setDisplayJob(true);
			}
			
			function getJobString()
			{
				let values = tableState.currentResponse.filter((e) => {return e.id === selectedElement.id});
				console.log(values);
				return JSON.stringify(values[0], null, 2);
			}
			
			if(selectedElement != null && displayJob == false)
			{
				return(
					<>
						<Button variant="contained" onClick={() => handleClick()}>Inspect Selected Job</Button>
						
					</>
				);
			}
			else if(selectedElement != null && displayJob == true)
			{
				return(
					<Backdrop sx={{color: '#ffff', zIndex: 5, display: "block"}} open={displayJob} >
												<Button variant="contained" onClick={() => setDisplayJob(false)}>Close</Button>

						<Container sx={{bgcolor: "grey", maxWidth: "lg"}}>
							<Typography variant="h3">Data Analysis Job</Typography>
						
							<Typography>
								<pre>
									{
										getJobString()
									}
								</pre>
							</Typography>
							
						</Container>
					</Backdrop>
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
					<Button variant="contained" onClick={() => setSubmitJobActive(true)}>Submit New Job</Button>
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
					<SubmitJobForm/>
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