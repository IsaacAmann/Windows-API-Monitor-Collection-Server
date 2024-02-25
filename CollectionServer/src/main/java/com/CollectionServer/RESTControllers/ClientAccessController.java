package com.CollectionServer.RESTControllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.CollectionServer.Services.UserAuthenticationService;


import java.util.HashMap;
import java.util.Map;

@RestController
public class ClientAccessController
{
    @Autowired
    UserAuthenticationService userAuthenticationService;

    @PostMapping("/inspectClient")
    public Map<String,Object> inspectClient(@RequestBody Map<String, Object> payload, HttpServletRequest request)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();


        return output;
    }
}
