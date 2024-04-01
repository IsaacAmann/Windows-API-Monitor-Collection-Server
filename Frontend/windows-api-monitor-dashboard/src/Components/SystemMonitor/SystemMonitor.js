import React from 'react';
import {useContext, useEffect} from 'react';
import {useState} from 'react';

import APICallContainer from "../../APICallContainer.js";
import {LoginInfoContext} from "../../App.js"
import Box from '@mui/material/Box';
import { Gauge } from '@mui/x-charts/Gauge';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';


function SystemMonitor()
{
	const loginInfo = useContext(LoginInfoContext);
	const [systemStats, setSystemStats] = useState(
		{
			isLoading: true
		}
	);
	
	useEffect( () => {
			let interval = setInterval(() => {getSystemInfo()}, 7000);
			
			return () => clearInterval(interval);
		
	}, []);
	
	function getSystemInfo()
	{
		var response = APICallContainer.systemInfo(loginInfo.token).then(
			function(value)
			{
				
			}
		);
	}
	
	function SystemGauge(props)
	{
		
		
		return(
			<>
				<Gauge
					width={125}
					height={125}
					value={75}
					startAngle={0}
					endAngle={360}
					innerRadius="80%"
					outerRadius="100%"
				/>
			</>
		);
	}

	return(
	<>
		<Box sx={{}}>
			<Grid container spacing={1}>
				<Grid item xs={12}>
					<p> System stats</p>
				</Grid>
				<Grid item xs={3}>
					<SystemGauge/>
				</Grid>
				<Grid item xs={3}>
					<SystemGauge/>
				</Grid>
				<Grid item xs={3}>
					<SystemGauge/>
				</Grid>
				<Grid item xs={3}>
					<SystemGauge/>
				</Grid>
			</Grid>
		</Box>
	</>
	);
}

export default SystemMonitor;
