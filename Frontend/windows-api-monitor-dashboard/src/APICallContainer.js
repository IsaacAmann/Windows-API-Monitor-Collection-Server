import axios from 'axios'
import Cookies from 'universal-cookie';

const apiURL = 'http://localhost:8080';
const cookies = new Cookies();

class APICallContainer
{
	async login(inputUsername, inputPassword)
	{
		var output = {loggedIn: false};
		var url = "";
		url = url.concat(apiURL, "/login");
		
		var json = {
			username: inputUsername,
			password: inputPassword
		};
		
		var result = await axios.post(url, json);
		
		if(result.data.token != null)
		{
			output.token = result.data.token;
			output.loggedIn = true;
			cookies.set('token', result.data.token, {path:'/', maxAge:1000000});
		}
		else
		{
			output.error = result.data.errorMessage;
		}
		
		console.log(output);
		return output;
	}
	
	getLoginInfo()
	{
		var output = null;
		
		var storedToken = cookies.get("token");
		
		if(storedToken != null && storedToken != undefined)
		{
			//Verify token is not expired
			output = storedToken;
		}
		
		return output;
	}
	
	logout()
	{
		cookies.remove('token');
	}
	
	async getDataSample()
	{
		var url = "";
		url = url.concat(apiURL, "/getDataSample");
		
		var result = await axios.post(url, {});
		
		//console.log(result);
		return result.data;
	}
	
	async getDataPage(token, pageNumber, pageSize)
	{
		var url = "";
		url = url.concat(apiURL, "/getDataPointPage");
		var json = {};
		json.token = token;
		json.pageNumber = pageNumber;
		json.pageSize = pageSize;
		
		var result = await axios.post(url, json);
		
		//console.log(result);
		return result.data;
	}
	
	async getAnalysisJobPage(token, pageNumber, pageSize)
	{
		var url = "";
		url = url.concat(apiURL, "/getAnalysisJobPage");
		var json = {};
		json.token = token;
		json.pageNumber = pageNumber;
		json.pageSize = pageSize;
		
		var result = await axios.post(url, json);
		
		//console.log(result);
		return result.data;
	}
	
	async getClientPage(token, pageNumber, pageSize)
	{
		var url = "";
		url = url.concat(apiURL, "/getClientPage");
		var json = {};
		json.token = token;
		json.pageNumber = pageNumber;
		json.pageSize = pageSize;
		
		var result = await axios.post(url, json);
		
		//console.log(result);
		return result.data;
	}
	
	async inspectAnalysisJob(token, id)
	{
		var url = "";
		url = url.concat(apiURL, "/inspectAnalysisJob");
		var json = {};
		json.token = token;
		
		var result = await axios.post(url, json);
		
		//console.log(result);
		return result.data;
	}
	
	async postAnalysisJob(token, inputValues)
	{
		var url = "";
		url = url.concat(apiURL, "/postAnalysisJob");
		var json = {};
		json.token = token;
		json.epsilon = inputValues.epsilon;
		json.jobType = inputValues.jobType;
		json.minPoints = inputValues.minPoints;
		json.seedPoints = inputValues.seedPoints;
		
		var result = await axios.post(url, json);
		
		//console.log(result);
		return result.data;
	}
	
	async shellCommand(inputToken, inputCommand)
    {
        var url = "";
        url = url.concat(apiURL, "/shellCommand");
        var result = await axios.post(url,
        {
           token: inputToken,
           command: inputCommand

        });
        console.log(result);
        return result.data;
    }
    
    async systemInfo(inputToken)
    {
        var url = "";
        url = url.concat(apiURL, "/systemInfo");
        var result = await axios.post(url,
        {
           token: inputToken
        });
        //console.log(result);
        return result.data;
    }
	
	
}

export default new APICallContainer();
