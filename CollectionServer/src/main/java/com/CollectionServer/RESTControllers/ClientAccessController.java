package com.CollectionServer.RESTControllers;

import com.CollectionServer.ClientManagement.CollectionClient;
import com.CollectionServer.ClientManagement.CollectionClientRepository;
import com.CollectionServer.DataPointEntity;
import com.CollectionServer.UserManagement.UserAccount;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.CollectionServer.Services.UserAuthenticationService;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ClientAccessController
{
    @Autowired
    private UserAuthenticationService userAuthenticationService;
    @Autowired
    private CollectionClientRepository collectionClientRepository;

    @PostMapping("/inspectClient")
    public Map<String,Object> inspectClient(@RequestBody Map<String, Object> payload, HttpServletRequest request)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        String token = (String)payload.get("token");
        UUID clientUUID = UUID.fromString((String)payload.get("clientUUID"));

        //Authenticate
        if(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.ADMIN) == true)
        {
            //Get requested data point
            CollectionClient client = collectionClientRepository.findByClientID(clientUUID);
            if(client != null)
            {
                //Fill output with fields
                output.put("lastSeen", client.lastSeen.toString());
                output.put("dataPointsCreated", client.dataPointsCreated);
            }
            else
            {
                output.put("error", "Could not find a client with Id: " + clientUUID);
            }
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }

        return output;
    }
}
