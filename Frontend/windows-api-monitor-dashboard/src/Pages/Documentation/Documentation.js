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
import { DataGrid, GridRowsProp, GridColDef } from '@mui/x-data-grid';


import APICallContainer from "../../APICallContainer.js";

import Navbar from "../../Components/Navbar/Navbar.js"


function Documentation()
{
	
	
	return(
		<>
			<Navbar/>
			<Container>
				<Typography variant="h3">
					Documentation
				</Typography>
				
				<Divider sx={{my: 2}}/>
				
				<Typography variant="h4">
					Project Proposal
				</Typography>
				<a href="../Documents/Senior_Design_Proposal.pdf">PDF Download</a>
				
				<Divider sx={{my: 2}}/>
				
				<Typography variant="h4">
					Milestone #1
				</Typography>
				<a href="../Documents/Senior_Project_Milestone_1.pdf">PDF Download</a>
				
				<Divider sx={{my: 2}}/>
				
				<Typography variant="h4">
					Milestone #2
				</Typography>
				<a href="../Documents/Senior_Project_Milestone_2.pdf">PDF Download</a>
				
				<Divider sx={{my: 2}}/>
				
				<Typography variant="h4">
					Milestone #3
				</Typography>
				<a href="../Documents/Senior_Project_Milestone_3.pdf">PDF Download</a>

				
				<Divider sx={{my: 2}}/>
				
				<Typography variant="h4">
					Final Report
				</Typography>
				<a href="../Documents/SeniorProjectFinalReport.pdf">PDF Download</a>

				
			</Container>
		</>
	);
}

export default Documentation;
