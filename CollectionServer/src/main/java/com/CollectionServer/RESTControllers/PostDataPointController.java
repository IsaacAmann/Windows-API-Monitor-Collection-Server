package com.CollectionServer.RESTControllers;


import com.CollectionServer.ClientManagement.CollectionClient;
import com.CollectionServer.ClientManagement.CollectionClientRepository;
import com.CollectionServer.DataPointRepository;
import com.CollectionServer.UserManagement.UserAccount;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class PostDataPointController
{

    @Autowired
    CollectionClientRepository collectionClientRepository;
    @Autowired
    DataPointRepository dataPointRepository;

    @PostMapping("/postDatapoint")
    public Map<String,Object> login(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws NoSuchAlgorithmException
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        String uuid = (String)payload.get("uuid");
        String token = (String)payload.get("token");
        //This should be a LinkedHashMap
        String winAPICalls = (String)payload.get("WinAPICounts");

        System.out.println(winAPICalls);

        //Authenticate client
        //CollectionClient client = collectionClientRepository.findByClientID(UUID.fromString(uuid));


        return output;
    }
}
