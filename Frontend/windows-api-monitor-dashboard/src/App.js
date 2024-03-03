import './App.css'
import ReactDOM from "react-dom/client"
import { BrowserRouter, Routes, Route } from "react-router-dom"
import {createContext, useContext, useState} from 'react'

import {colors, ThemeProvider} from "@mui/material"
import {createTheme} from "@mui/material/styles"
import CssBaseline from '@mui/material/CssBaseline';


import HomePage from "./Pages/HomePage/HomePage.js"

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
    },
    background: {
      default: '#020a07',
    },
  }
}
);

function App() {
	const [token, setToken] = useState(null);
	const loginInfo = useContext(LoginInfoContext);
	
	return (
	<ThemeProvider theme={mainTheme}>
	<CssBaseline/>
		<LoginInfoContext.Provider value={{token: token, setToken: setToken}}>
			<BrowserRouter>
				<Routes>
					<Route path="/">
						<Route index element={<HomePage />} />
					</Route>
				</Routes>
			</BrowserRouter>
		</LoginInfoContext.Provider>
	</ThemeProvider>
  );
}

export default App;
