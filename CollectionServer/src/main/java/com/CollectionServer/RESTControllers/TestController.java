package com.CollectionServer.RESTControllers;


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
	@PostMapping("/test")
	public Map<String,Object> test(@RequestBody Map<String, Object> payload, HttpServletRequest request)
	{
		HashMap<String, Object> output = new HashMap<String, Object>();
		
		System.out.println("Test called by " + request.getRemoteAddr());
		
		output.put("message", "Test call return message");
		
		return output;
	}
}
