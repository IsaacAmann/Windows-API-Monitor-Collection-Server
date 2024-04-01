import React from 'react'
import {useContext} from 'react'
import {useState, useCallback} from 'react'

import Navbar from "../../Components/Navbar/Navbar.js"

import APICallContainer from "../../APICallContainer.js";

import {LoginInfoContext} from "../../App.js";
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';

import "./AdminDashboard.css"


function AdminDashboard()
{
	const loginInfo = useContext(LoginInfoContext);
	const [consoleText, setConsoleText] = useState(null);
	function CommandConsole()
    {
        return(
            <>
                
                <textarea value={consoleText} readOnly={true}  className="commandConsole">
					Command Console. Run showCommands to show available commands. Run clear to clear text
                </textarea>
                <form id="commandField" className="login-form" onSubmit={sendCommand}>
                    <input autoFocus name="command" type="text"  />

                </form>
            </>
        );
    }
	
    function sendCommand(e)
    {
        e.preventDefault();
        var data = new FormData(e.target);
        var entries = Object.fromEntries(data.entries());
        console.log(entries.command);
        if(entries.command != null && entries.command != "")
        {
            if(consoleText == null)
            {
                setConsoleText(">" + entries.command);
            }
            else
            {
                setConsoleText(consoleText + "\n>" + entries.command);
            }

            if(entries.command == "clear")
            {
                setConsoleText(null);
            }
            else
            {
                //attempt to run command through API
                var result = APICallContainer.shellCommand(loginInfo.token, entries.command).then(
                    function(value)
                    {
                    console.log(value);
                        setConsoleText(consoleText + "\n" + value.result);
                    }
                );
            }
        }
    }
	
	if(loginInfo.username != null)
	{
		if(loginInfo.userRole == "ADMIN")
		{
			return(
				<>
					<Navbar/>
					<Box sx={{flexGrow: 1, m: 1}}>
						<Grid container spacing={2}>
							<Grid item xs={6}>
								<CommandConsole/>
							</Grid>
							<Grid item xs={6}>
								<Box sx={{bgcolor: 'red'}}>
									
								</Box>
							</Grid>
						</Grid>
					</Box>
				</>
			);
		}
		else
		{
			return (
				<>
					<Navbar/>
					<Typography> Invalid user role</Typography>
				</>
			);
		}
	}
	else
	{
		return(
			<>
				<Navbar/>
				<Typography> Please login to access this</Typography>
			</>
		);
	}
}

export default AdminDashboard;
