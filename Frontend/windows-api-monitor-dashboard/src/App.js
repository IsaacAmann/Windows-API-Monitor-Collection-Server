import './App.css'
import ReactDOM from "react-dom/client"
import { BrowserRouter, Routes, Route } from "react-router-dom"

import HomePage from "./Pages/HomePage/HomePage.js"

function App() {
  return (
	<LoginInfoContext.Provider >
        <BrowserRouter>
            <Routes>
                <Route path="/" >
                    <Route index element={<HomePage />} />
                </Route>
            </Routes>
        </BrowserRouter>
    </LoginInfoContext.Provider>
  );
}

export default App;
