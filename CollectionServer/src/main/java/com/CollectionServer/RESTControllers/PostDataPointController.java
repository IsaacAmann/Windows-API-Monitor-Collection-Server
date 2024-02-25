package com.CollectionServer.RESTControllers;


import com.CollectionServer.ClientManagement.CollectionClient;
import com.CollectionServer.ClientManagement.CollectionClientRepository;
import com.CollectionServer.DataPointEntity;
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
import java.util.LinkedHashMap;
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
    public Map<String,Object> postDatapoint(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws NoSuchAlgorithmException
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        String uuid = (String)payload.get("uuid");
        String token = (String)payload.get("token");
        String executablePath = (String)payload.get("executablePath");
        //This should be a LinkedHashMap
        LinkedHashMap<String, Integer> winAPICalls = (LinkedHashMap<String, Integer>) payload.get("WinAPICounts");

        System.out.println(winAPICalls.get("GetCurrentProcessId"));

        //Authenticate client
        CollectionClient client = collectionClientRepository.findByClientID(UUID.fromString(uuid));
        if(client != null && client.isValidAPIToken(token))
        {
            //Create new datapoint
            DataPointEntity dataPoint= new DataPointEntity();
            dataPoint.WinAPICounts = winAPICalls;
            dataPoint.originClientId = UUID.fromString(uuid);
            dataPoint.executablePath = executablePath;
            dataPointRepository.save(dataPoint);
            output.put("message", "Datapoint posted to database");
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }

        return output;
    }
}
