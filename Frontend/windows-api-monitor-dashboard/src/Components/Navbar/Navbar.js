
import React from 'react';
import {useContext} from 'react';
import {useState} from 'react';

import AppBar from "@mui/material/AppBar";
import Avatar from "@mui/material/Avatar";
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Toolbar from "@mui/material/Toolbar";
import Box from "@mui/material/Box";
import Button from '@mui/material/Button';
import { jwtDecode } from "jwt-decode";
import Divider from '@mui/material/Divider';


import GitHubIcon from '@mui/icons-material/GitHub';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

import {LoginInfoContext} from "../../App.js"
import Backdrop from '@mui/material/Backdrop';
import Container from "@mui/material/Container"
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import Drawer from '@mui/material/Drawer';
import LogoutIcon from '@mui/icons-material/Logout';
import { Link } from 'react-router-dom';
import Snackbar from '@mui/material/Snackbar';

import APICallContainer from "../../APICallContainer.js";





function Navbar()
{
	const loginInfo = useContext(LoginInfoContext);
	const [loginUp, setLoginUp] = useState(false);
	const [profileDisplayUp, setProfileDisplayUp] = useState(false);
	const [loginNotify, setLoginNotify] = useState(false);
	
	const [username, setUsername] = useState(null);
	const [password, setPassword] = useState(null);
	const [loginError, setLoginError] = useState(null);
	
	const usernameRef = React.useRef(null);
	const passwordRef = React.useRef(null);
	
	function handleLogout()
	{
		setProfileDisplayUp(false);
		loginInfo.setUsername(null);
		loginInfo.setUserRole(null);
		loginInfo.setToken(null);
		APICallContainer.logout();
	}
	
	function handleLoginNotifyClose()
	{
		setLoginNotify(false);
	}
	
	function ProfileDisplayDrawer()
	{
		const drawerList = (
			<Box sx={{width: 250, bgcolor:'#020a07', minHeight: '100%'}} role="presentation" >
				<Typography variant="h4">Hello {loginInfo.username}!</Typography>
				<Typography variant="h5">User Role: {loginInfo.userRole}</Typography>
				<Divider />
				
				
				<Button variant="contained" onClick={() => handleLogout()}> <LogoutIcon/>Sign Out </Button>
			</Box>
		);
		return(
			<Drawer anchor="right" open={profileDisplayUp} sx={{}} onClose={() => setProfileDisplayUp(false)}>
				{drawerList}
			</Drawer>
		);
	}
	
	function handleLogin()
	{
		console.log(usernameRef.current.value + passwordRef.current.value);
		var response = APICallContainer.login(usernameRef.current.value, passwordRef.current.value);
		
		response.then(
			function(value)
			{
				if(value.token != null)
				{
					loginInfo.setToken(value.token);
					
					//Decode token
					var decodedToken = jwtDecode(value.token);
					console.log(decodedToken);
					loginInfo.setUsername(decodedToken.username);
					loginInfo.setUserRole(decodedToken.userRole);
					
					setLoginError(null);
					setLoginUp(false);
					setLoginNotify(true);
				}
				else
				{
					setLoginError(value.error);
				}
			}
		);
	}
	
	function LoginButton()
	{
		if(loginInfo.token == null)
		{
			return(
				<>

					<Button variant="contained" onClick={() => setLoginUp(true)}>Login</Button>
				</>
			);
		}
		else
		{
			return(
				<>
					<IconButton onClick={() => setProfileDisplayUp(true)} >
						<AccountCircleIcon fontSize="large"/>
					</IconButton>
				</>
			);
		}
	}
	
	function AdminDashboardLink()
	{
		if(loginInfo.userRole == "ADMIN")
		{
			return(
				<Button component={Link} to="/AdminDashboard" variant="contained" color="secondary" sx={{mx: 1}}>
					Admin Dashboard
				</Button>
			);
		}
		else
		{
			return null;
		}
	}
	
	function LoginError()
	{
		if(loginError != null)
		{
			return(
				<>
					<Typography>
						{loginError}
					</Typography>
				</>
			);
		}
		else
		{
			return null;
		}
	}
	
	function LoginForm()
	{
		return(
		<Backdrop open={loginUp} sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}>
			<Container maxWidth='md' sx={{bgcolor: 'grey'}}>
				<IconButton onClick={() => setLoginUp(false)} fontSize="medium" >
					<CloseIcon/>
				</IconButton>
				<Typography variant="h3">Sign In</Typography>
				<Typography>Login to access dataset explorer and analysis jobs</Typography>
				<Typography>Contact isaac.amann@siu.edu for access</Typography>
				<form onSubmit={handleLogin}>
					<Stack>
						<TextField id="usernameField" label="Username" variant="filled" inputRef={usernameRef}/>
						<TextField id="passwordField" label="Password" variant="filled" type="password" inputRef={passwordRef}/>

						<Button variant="outlined" onClick={() => handleLogin()}>Login</Button>
						<LoginError/>
					</Stack>
				</form>
			</Container>
			
		</Backdrop>
		);
	}
	
	return(
	<Box sx={{ flexGrow: 1 }}>
		<AppBar position="static">

			<Toolbar>
				<LoginForm/>
				<Typography component="div" sx={{mr: 2}}>
					Behavior Based Process Analysis
				</Typography>
				<Box sx={{flexGrow: 1}}>
					<Button component={Link} to="/" variant="contained" color="secondary" sx={{mx: 1}}>
						Home
					</Button>
					<Button component={Link} to="/DatasetExplorer" variant="contained" color="secondary" sx={{mx: 1}}>
						Dataset Explorer
					</Button>
					<Button component={Link} to="/DataAnalysis" variant="contained" color="secondary" sx={{mx: 1}}>
						Data Analysis
					</Button>
					<Button component={Link} to="/Documentation" variant="contained" color="secondary" sx={{mx: 1}}>
						Documentation
					</Button>
					<AdminDashboardLink/>
				</Box>
				<a href="https://github.com/IsaacAmann/Windows-API-Monitor">
					<GitHubIcon fontSize="large" sx={{mx: 2}}/>
				</a>
				<LoginButton/>
			</Toolbar>
			<ProfileDisplayDrawer/>
		</AppBar>
		<Snackbar
			open={loginNotify}
			autoHideDuration={1500}
			onClose={handleLoginNotifyClose}
			message="Login Successful"
		/>
		
	</Box>
	);
}

export default Navbar;
