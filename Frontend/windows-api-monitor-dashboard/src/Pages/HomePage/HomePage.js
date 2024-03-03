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

import GitHubIcon from '@mui/icons-material/GitHub';


import Navbar from "../../Components/Navbar/Navbar.js"


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
					A user-level process monitor that collects Windows API usage data from Windows pocesses for behavior analysis.
				</Typography>
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