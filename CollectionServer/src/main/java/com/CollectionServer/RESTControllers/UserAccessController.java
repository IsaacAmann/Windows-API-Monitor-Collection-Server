package com.CollectionServer.RESTControllers;


import com.CollectionServer.DataPointEntity;
import com.CollectionServer.UserManagement.UserAccount;
import com.CollectionServer.UserManagement.UserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import com.CollectionServer.Services.UserAuthenticationService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserAccessController
{
    @Autowired
    private UserAuthenticationService userAuthenticationService;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @PostMapping("/inspectUser")
    public Map<String,Object> inspectUser(@RequestBody Map<String, Object> payload, HttpServletRequest request)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        String token = (String)payload.get("token");
        String username = (String)payload.get("username");
        //Authenticate
        if(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.ADMIN) == true)
        {
            //Get requested data point
            UserAccount userAccount = userAccountRepository.findByUsername(username);
            if(userAccount != null)
            {
                //Fill output with fields
                output.put("username", userAccount.username);
                output.put("userRole", userAccount.userRole);
                output.put("id", userAccount.id);
            }
            else
            {
                output.put("error", "Could not find a user with username: " + username);
            }
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }

        return output;
    }

    @PostMapping("/getUserPage")
    public Map<String,Object> getUserPage(@RequestBody Map<String, Object> payload, HttpServletRequest request)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        String token = (String)payload.get("token");
        int pageNumber = (int)payload.get("pageNumber");
        int pageSize = (int)payload.get("pageSize");
        if(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.ADMIN) == true)
        {
            Page<UserAccount> points = userAccountRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "id"));

            output.put("values", points);
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }

        return output;
    }
}
