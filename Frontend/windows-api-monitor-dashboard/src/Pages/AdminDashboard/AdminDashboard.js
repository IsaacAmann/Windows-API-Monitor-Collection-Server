import React from 'react'
import {useContext} from 'react'
import {useState, useCallback} from 'react'

import Navbar from "../../Components/Navbar/Navbar.js"

import APICallContainer from "../../APICallContainer.js";

import {LoginInfoContext} from "../../App.js";
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';


function AdminDashboard()
{
	const loginInfo = useContext(LoginInfoContext);
	const [consoleText, setConsoleText] = useState(null);
	function CommandConsole()
    {
        return(
            <>
                <h3> Command Console </h3>
                <p>
                    run showCommands to list methods available. View the ShellCommands class in the backend
                    code for information on available methods or to add methods.
                </p>
                <textarea value={consoleText} readOnly={true} rows={20} cols={90} className="commandConsole">

                </textarea>
                <form className="login-form" onSubmit={sendCommand}>
                    <input name="command" type="text"  />

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
					<Typography variant="h3">Admin Dashboard</Typography>
					<Box sx={{flexGrow: 1}}>
						<Grid container spacing={2}>
							<Grid item xs={10}>
								<CommandConsole/>
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