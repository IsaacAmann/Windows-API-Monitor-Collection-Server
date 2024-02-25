package com.CollectionServer.RESTControllers;

import com.CollectionServer.Services.UserAuthenticationService;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

//API call for testing connection from client
@RestController
public class TestController
{
	@Autowired
	UserAuthenticationService userAuthenticationService;

	@PostMapping("/test")
	public Map<String,Object> test(@RequestBody Map<String, Object> payload, HttpServletRequest request)
	{
		HashMap<String, Object> output = new HashMap<String, Object>();
		
		System.out.println("Test called by " + request.getRemoteAddr());

		//Print each value in the message
		for(String field : payload.keySet())
		{
			String value = payload.get(field).toString();
			System.out.println(field + ": " + value);
		}
		output.put("message", "Test call return message");
		
		return output;
	}

	@PostMapping("/checkToken")
	public Map<String,Object> checkToken(@RequestBody Map<String, Object> payload, HttpServletRequest request)
	{
		HashMap<String, Object> output = new HashMap<String, Object>();

		String token = (String)payload.get("token");

		DecodedJWT decodedToken = userAuthenticationService.verifyToken(token);

		if(decodedToken != null)
		{
			String username = decodedToken.getClaim("username").asString();
			String userRole = decodedToken.getClaim("userRole").asString();

			output.put("Username", username);
			output.put("userrole", userRole);
		}
		else
		{
			output.put("error", "Failed to verify token");
		}

		return output;
	}
}
