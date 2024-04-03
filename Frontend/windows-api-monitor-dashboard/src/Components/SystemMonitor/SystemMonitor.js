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
import { BarChart } from '@mui/x-charts/BarChart';
import { axisClasses } from '@mui/x-charts';


function SystemMonitor()
{
	const loginInfo = useContext(LoginInfoContext);
	const [systemStats, setSystemStats] = useState(
		{
			isLoading: true,
			iowait: [],
			irq: [],
			soft: [],
			sys: [],
			usr: [],
			xAxis: []
		}
	);
	
	useEffect( () => {
			getSystemInfo();
			let interval = setInterval(() => {getSystemInfo()}, 7000);
			
			return () => clearInterval(interval);
		
	}, []);
	
	function CPUChart()
	{
		
		const dataset = [
			{label: 'iowait', stack: 'iowait', data: systemStats.iowait},
			{label: "irq", stack: "irq", data: systemStats.irq},
			{label: "soft", stack: "soft", data: systemStats.soft},
			{label: "sys", stack: "sys", data: systemStats.sys},
			{label: "usr", stack: "usr", data: systemStats.usr}
		];
	
		if(systemStats.isLoading == true)
		{
			return null;
		}
		else
		{
		return(
				<>
					<BarChart
						series = {dataset}
						xAxis = {systemStats.xAxis}
						yAxis = {[{scaleType: 'linear', tickInterval: [100]}]}
						height = {400}
					/>
				</>
			);
		}
	}
	
	function getSystemInfo()
	{
		var response = APICallContainer.systemInfo(loginInfo.token).then(
			function(value)
			{
				console.log(value);
				let cpuArray = value.stats.sysstat.hosts[0].statistics[0]["cpu-load"];
				console.log(cpuArray);
				
				let newIrq = [];
				let newIowait = [];
				let newSoft = [];
				let newSys = [];
				let newUsr = [];
				let newxAxis = [{scaleType: 'band', data: []}];
				
				//Get CPU usage values
				for(let i = 1; i < cpuArray.length; i++)
				{
					newIrq.push(cpuArray[i].irq);
					newIowait.push(cpuArray[i].iowait);
					newSoft.push(cpuArray[i].soft);
					newSys.push(cpuArray[i].sys);
					newUsr.push(cpuArray[i].usr);
					newxAxis[0].data.push("Processor " + i);
				}
				
				//Update state
				setSystemStats({...systemStats, isLoading: false, iowait: newIowait, irq: newIrq, soft: newSoft, sys: newSys, usr: newUsr, xAxis: newxAxis});
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
					<Typography variant="h4"> Host System Information </Typography>
				</Grid>
				<Grid item xs={3}>
					<SystemGauge/>
				</Grid>
				<Grid item xs={12}>
					<Typography variant="h4"> CPU Usage </Typography>
					<CPUChart/>
				</Grid>
				
			</Grid>
		</Box>
	</>
	);
}

export default SystemMonitor;
