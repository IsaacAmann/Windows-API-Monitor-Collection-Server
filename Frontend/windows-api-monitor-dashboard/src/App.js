import './App.css'
import ReactDOM from "react-dom/client"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import {createContext, useContext, useState, useEffect} from 'react'

import {colors, ThemeProvider} from "@mui/material"
import {createTheme} from "@mui/material/styles"
import CssBaseline from '@mui/material/CssBaseline';

import APICallContainer from "./APICallContainer.js";

import HomePage from "./Pages/HomePage/HomePage.js";
import DatasetExplorer from "./Pages/DatasetExplorer/DatasetExplorer.js";
import DataAnalysis from "./Pages/DataAnalysis/DataAnalysis.js";
import AdminDashboard from "./Pages/AdminDashboard/AdminDashboard.js";
import Documentation from "./Pages/Documentation/Documentation.js";

import { jwtDecode } from "jwt-decode";


export const LoginInfoContext = createContext({token: null, setToken: () => {}});

export const themeOptions: ThemeOptions = {
  palette: {
	mode: 'dark',
    primary: {
      main: '#9b77ff',
    },
    secondary: {
      main: '#7100a1',
      dark: '#37252d',
    },
    warning: {
      main: '#4a3612',
    },
    info: {
      main: '#0288d1',
    },
    divider: 'rgba(90,35,35,0.12)',
    text: {
      primary: '#d9ceff',
      disabled: 'd9ceff'
    },
    background: {
      default: '#03000b',
	  paper: '#03000b'
    },
    error: {
      main: '#d32f2f',
    },
  }
};

export const mainTheme = createTheme({
	palette: {
    primary: {
      main: '#96e6bd',
    },
    secondary: {
      main: '#5e1f86',
      dark: '#5e1f86',
    },
    divider: '#9b77ff',
    text: {
      primary: '#e2f8ec',
      disabled: '#0a2918',
      secondary: '#145230'
    },
    background: {
      default: '#020a07',
      paper: '#11553c'
    },
    
  }
}
);

function App() {
	const [token, setToken] = useState(null);
	const [username, setUsername] = useState(null);
	const [userRole, setUserRole] = useState(null);
	const loginInfo = useContext(LoginInfoContext);
	
	//Check for token in cookie
	useEffect(() => 
	{
		var currentToken = APICallContainer.getLoginInfo();
		if(currentToken != null)
		{
			//Decode token and set fields
			var decodedToken = jwtDecode(currentToken);
			setUsername(decodedToken.username);
			setToken(currentToken);
			setUserRole(decodedToken.userRole);
		}
	}, []);
	
	return (
	<ThemeProvider theme={mainTheme}>
	<CssBaseline/>
		<LoginInfoContext.Provider value={{token: token, setToken: setToken, username: username, setUsername: setUsername, userRole: userRole, setUserRole: setUserRole}}>
			<BrowserRouter>
				<Routes>
					<Route path="/">
						<Route index element={<HomePage />} />
						<Route path="DatasetExplorer" element={<DatasetExplorer/>} />
						<Route path="DataAnalysis" element={<DataAnalysis/>}/>
						<Route path="AdminDashboard" element={<AdminDashboard/>}/>
						<Route path="Documentation" element={<Documentation/>}/>
					</Route>
				</Routes>
			</BrowserRouter>
		</LoginInfoContext.Provider>
	</ThemeProvider>
  );
}

export default App;
