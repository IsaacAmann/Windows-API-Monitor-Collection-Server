
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

import GitHubIcon from '@mui/icons-material/GitHub';

import {LoginInfoContext} from "../../App.js"
import Backdrop from '@mui/material/Backdrop';
import Container from "@mui/material/Container"
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';

import APICallContainer from "../../APICallContainer.js";




function Navbar()
{
	const loginInfo = useContext(LoginInfoContext);
	const [loginUp, setLoginUp] = useState(false);
	
	const [username, setUsername] = useState(null);
	const [password, setPassword] = useState(null);
	const [loginError, setLoginError] = useState(null);
	
	const usernameRef = React.useRef(null);
	const passwordRef = React.useRef(null);
	
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
					setLoginUp(false);
					console.log("L");
				}
				else
				{
					
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
					<Avatar>N</Avatar>
				</>
			);
		}
	}
	
	function LoginForm()
	{
		return(
		<Backdrop open={loginUp} sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1 }}>
			<Container maxWidth='md' sx={{bgcolor: 'grey'}}>
				<IconButton onClick={() => setLoginUp(false)} size="medium" >
					<CloseIcon/>
				</IconButton>
				<Typography variant="h3">Sign In</Typography>
				<Typography>Login to access dataset explorer and analysis jobs</Typography>
				<Typography>Contact isaac.amann@siu.edu for access</Typography>
				<form onSubmit={handleLogin}>
					<Stack>
						<TextField id="usernameField" label="Username" variant="filled" inputRef={usernameRef}/>
						<TextField id="passwordField" label="Password" variant="filled" inputRef={passwordRef}/>

						<Button variant="outlined" onClick={() => handleLogin()}>Login</Button>
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
				<Typography component="div" sx={{flexGrow: 1}}>
					Behavior Based Process Analysis{loginInfo.token}
				</Typography>
				<a href="https://github.com/IsaacAmann/Windows-API-Monitor">
					<GitHubIcon fontSize="large" sx={{mx: 2}}/>
				</a>
				<LoginButton/>
			
			</Toolbar>
		</AppBar>
	</Box>
	);
}

export default Navbar;