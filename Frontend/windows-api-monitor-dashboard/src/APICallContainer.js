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
	
	
}

export default new APICallContainer();