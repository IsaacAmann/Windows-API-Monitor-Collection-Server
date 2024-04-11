
import React from 'react';
import {useContext, useEffect} from 'react';
import {useState, useCallback} from 'react';

import APICallContainer from "../../APICallContainer.js";
import {LoginInfoContext} from "../../App.js"
import Box from '@mui/material/Box';
import { Gauge } from '@mui/x-charts/Gauge';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import { BarChart } from '@mui/x-charts/BarChart';
import { axisClasses } from '@mui/x-charts';
import Button from '@mui/material/Button';
import CircularProgress from '@mui/material/CircularProgress';
import { DataGrid, GridRowsProp, GridColDef } from '@mui/x-data-grid';



import Tab from '@mui/material/Tab';
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';


function AdminTabMenu()
{
	const elementsPerPage = 25;
	
	const loginInfo = useContext(LoginInfoContext);

	const [currentTab, setCurrentTab] = useState("1");
	
	function handleTabChange(event, newValue)
	{
		setCurrentTab(newValue);
	}
	
	function ClientDataTable()
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
		
		function handlePageChange(model, details)
		{
			setTableState({...tableState, page: model.page, loadPage: true, isLoading: true});
		}
		
		const handleSelectionChange = useCallback((model, details) => {
			//model contains id's and not indexs
			let value = tableState.rows.filter((e) => {return e.id === model[0]});
			setSelectedElement(value[0]);
		});
		
		//Request a page from the server
		if(tableState.loadPage == true)
		{
			tableState.loadPage = false;

			var response = APICallContainer.getClientPage(loginInfo.token, tableState.page, elementsPerPage).then(
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
						rowObjects[i].uuid = dataArray[i].clientID;
						rowObjects[i].dataPoints = dataArray[i].dataPointsCreated;
						rowObjects[i].lastSeen = dataArray[i].lastSeen;
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
				{field: 'id', headerName: "ID", width: 50},
				{field: 'uuid', headerName: 'Client UUID', width: 325},
				{field: 'dataPoints', headerName: 'Points Created', width: 150},
				{field: 'lastSeen', headerName: 'Last Seen', width: 150}
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
	
	function UserDataTable()
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
		
		function handlePageChange(model, details)
		{
			setTableState({...tableState, page: model.page, loadPage: true, isLoading: true});
		}
		
		const handleSelectionChange = useCallback((model, details) => {
			//model contains id's and not indexs
			let value = tableState.rows.filter((e) => {return e.id === model[0]});
			setSelectedElement(value[0]);
		});
		
		//Request a page from the server
		if(tableState.loadPage == true)
		{
			tableState.loadPage = false;

			var response = APICallContainer.getUserPage(loginInfo.token, tableState.page, elementsPerPage).then(
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
						rowObjects[i].username = dataArray[i].username;
						rowObjects[i].userRole = dataArray[i].userRole;
					}
					
					var rows: GridRowsProp = rowObjects;
					setTableState({...tableState, isLoading: false, currentResponse: value.logEntries, rows: rows, total: value.totalElements})
				}
			);
		}
		
		if(tableState.isLoading == false)
		{
			var dataArray = tableState.currentResponse;
			//Set up columns
			var colDefs = [
				{field: 'id', headerName: "ID", width: 50},
				{field: 'username', headerName: 'Username', width: 300},
				{field: 'userRole', headerName: 'User Role', width: 300}

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
	
	function LogDataTable()
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
		
		function handlePageChange(model, details)
		{
			setTableState({...tableState, page: model.page, loadPage: true, isLoading: true});
		}
		
		const handleSelectionChange = useCallback((model, details) => {
			//model contains id's and not indexs
			let value = tableState.rows.filter((e) => {return e.id === model[0]});
			setSelectedElement(value[0]);
		});
		
		//Request a page from the server
		if(tableState.loadPage == true)
		{
			tableState.loadPage = false;

			var response = APICallContainer.getLogPage(loginInfo.token, tableState.page, elementsPerPage).then(
				function(value)
				{			
					console.log(value);
					//Set up rows
					var rowObjects = [];
					var dataArray = value.logEntries;
					for(let i = 0; i < dataArray.length; i++)
					{
						rowObjects[i] = {};
						rowObjects[i].id = dataArray[i].id;
						rowObjects[i].logLevel = dataArray[i].logLevel;
						rowObjects[i].loggerName = dataArray[i].loggerName;
						rowObjects[i].message = dataArray[i].message;
						rowObjects[i].dateCreated = dataArray[i].dateCreated;
					}
					
					var rows: GridRowsProp = rowObjects;
					setTableState({...tableState, isLoading: false, currentResponse: value.logEntries, rows: rows, total: value.totalElements})
				}
			);
		}
		
		if(tableState.isLoading == false)
		{
			var dataArray = tableState.currentResponse;
			//Set up columns
			var colDefs = [
				{field: 'id', headerName: "ID", width: 50},
				{field: 'logLevel', headerName: 'Log Level', width: 90},
				{field: 'loggerName', headerName: 'Source', width: 325},
				{field: 'dateCreated', headerName: 'Date Created', width: 200},
				{field: 'message', headerName: 'Message', width: 500}
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
	
	return(
		<>
			<Box sx={{width: '100%'}}>
				<TabContext value={currentTab}>
					<Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
						<TabList onChange={handleTabChange} aria-label="Admin tab menu">
							<Tab label="Client List" value="1" />
							<Tab label="User List" value="2" />
							<Tab label="Log Viewer" value = "3" />
						</TabList>
					</Box>
					<TabPanel value="1">
						<ClientDataTable/>
					</TabPanel>
					<TabPanel value="2">
						<UserDataTable/>
					</TabPanel>
					<TabPanel value="3">
						<LogDataTable />
					</TabPanel>
				</TabContext>
			</Box>
		</>
	);
}

export default AdminTabMenu;
