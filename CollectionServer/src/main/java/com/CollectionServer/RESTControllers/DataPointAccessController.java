package com.CollectionServer.RESTControllers;

import com.CollectionServer.DataPointEntity;
import com.CollectionServer.DataPointRepository;
import com.CollectionServer.UserManagement.UserAccount;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import com.CollectionServer.Services.UserAuthenticationService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class DataPointAccessController
{
    @Autowired
    UserAuthenticationService userAuthenticationService;
    @Autowired
    DataPointRepository dataPointRepository;

    @PostMapping("/inspectDataPoint")
    public Map<String,Object> inspectDataPoint(@RequestBody Map<String, Object> payload, HttpServletRequest request)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        String token = (String)payload.get("token");
        int dataPointId = (int)payload.get("dataPointId");
        //Authenticate
        if(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.USER) == true)
        {
            //Get requested data point
            DataPointEntity datapoint = dataPointRepository.findById(dataPointId);
            if(datapoint != null)
            {
                //Fill output with fields
                output.put("DateCreated", datapoint.dateCreated.toString());
                output.put("Executable", datapoint.executablePath);
                output.put("OriginClient", datapoint.originClientId.toString());

                //Put api call counts
                output.put("WinAPICalls", datapoint.WinAPICounts);
            }
            else
            {
                output.put("error", "Could not find a datapoint with Id: " + dataPointId);
            }
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }

        return output;
    }

    @PostMapping("/getDataPointPage")
    public Map<String,Object> getDataPointPage(@RequestBody Map<String, Object> payload, HttpServletRequest request)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        String token = (String)payload.get("token");
        int pageNumber = (int)payload.get("pageNumber");
        int pageSize = (int)payload.get("pageSize");
        if(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.USER) == true)
        {
            Page<DataPointEntity> points = dataPointRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "Id"));

            output.put("values", points);
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }


        return output;
    }
}
