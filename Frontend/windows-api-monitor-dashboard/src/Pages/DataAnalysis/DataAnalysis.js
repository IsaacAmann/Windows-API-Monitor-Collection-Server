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

function DataAnalysis()
{
	const loginInfo = useContext(LoginInfoContext);

	if(loginInfo.token != null)
	{
		return(
			<>
					<Navbar/>
					<p>Data Analysis</p>
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