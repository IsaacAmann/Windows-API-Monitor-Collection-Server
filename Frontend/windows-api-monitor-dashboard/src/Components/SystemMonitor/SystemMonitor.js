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
			xAxis: [],
			architecture: "",
			hostname: "",
			date: "",
			kernel: "",
			systemName: "",
			numberProcessor: 0,
			memoryUsed: 0,
			memoryMax: 0
		}
	);
	
	useEffect( () => {
			getSystemInfo();
			let interval = setInterval(() => {getSystemInfo()}, 5000);
			
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
						skipAnimation
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
				
				
				//Get memory use values
				let memTotal = value.MemTotal;
				let memLeft = value.MemFree;
				
				//Convert to megabytes
				memTotal *= 0.001;
				memLeft *= 0.001;
				
				let memUsed = memTotal - memLeft;
				
				let node = value.stats.sysstat.hosts[0];
				//Update state
				setSystemStats({
					...systemStats, isLoading: false,
					iowait: newIowait, irq: newIrq,
					soft: newSoft, sys: newSys, 
					usr: newUsr, xAxis: newxAxis,
					architecture: node.machine, date: node.date,
					kernel: node.release, systemName: node.sysname,
					numberProcessor: node["number-of-cpus"], hostname: node.nodename,
					memoryMax: Math.floor(memTotal), memoryUsed: Math.floor(memUsed)
				});
			}
		);
	}
	
	function MemoryGauge()
	{
		
		
		return(
			<>
				<Gauge
					width={200}
					height={200}
					value={systemStats.memoryUsed}
					valueMax={systemStats.memoryMax}
					startAngle={0}
					endAngle={360}
					innerRadius="90%"
					outerRadius="100%"
					text = {
						({ value, valueMax }) => `${value} MB / ${valueMax} MB`
					}
				/>
			</>
		);
	}

	return(
	<>
		<Box sx={{}}>
			<Grid container spacing={1}>
				<Grid item xs={7}>
					<Typography variant="h4"> Host System Information </Typography>
					<Typography>Hostname: {systemStats.hostname}</Typography>
					<Typography>Date: {systemStats.date}</Typography>
					<Typography>Architecture: {systemStats.architecture}</Typography>
					<Typography>Kernel: {systemStats.kernel}</Typography>
					<Typography>System Name: {systemStats.systemName}</Typography>
					<Typography>Processors: {systemStats.numberProcessor}</Typography>
				</Grid>
				<Grid item xs={5}>
					<Typography>Memory Usage</Typography>
					<MemoryGauge/>
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
