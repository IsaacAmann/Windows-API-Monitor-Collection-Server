package com.CollectionServer.RESTControllers;

import com.CollectionServer.ClientManagement.CollectionClient;
import com.CollectionServer.ClientManagement.CollectionClientRepository;
import com.CollectionServer.DataAnalysis.AnalysisJobRepository;
import com.CollectionServer.DataPointEntity;
import com.CollectionServer.DataPointRepository;
import com.CollectionServer.Services.AdminNotificationService;
import com.CollectionServer.Services.UserAuthenticationService;
import com.CollectionServer.UserManagement.UserAccount;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.CollectionServer.DataAnalysis.*;
import com.CollectionServer.Services.AnalysisJobService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DataAnalysisController
{

    @Autowired
    UserAuthenticationService userAuthenticationService;
    @Autowired
    AnalysisJobRepository analysisJobRepository;
    @Autowired
    AnalysisJobService analysisJobService;
    @Autowired
    DataPointRepository dataPointRepository;
    @Autowired
    private AdminNotificationService adminNotificationService;

    @PostMapping("/postAnalysisJob")
    public Map<String,Object> postAnalysisJob(@RequestBody Map<String, Object> payload, HttpServletRequest request)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        String token = (String)payload.get("token");
        String jobType = (String)payload.get("jobType");

        if(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.USER) == true)
        {
            int epsilon;
            int minPoints;
            switch(AnalysisJob.AnalysisType.valueOf(jobType))
            {

                case SOW_AND_GROW:
                    epsilon = (int)payload.get("epsilon");
                    minPoints = (int)payload.get("minPoints");
                    List<Integer> seedIDs = (List<Integer>)payload.get("seedPoints");

                    //Create analysis job object
                    RunSowAndGrow analysisMethod = new RunSowAndGrow(new ArrayList<Integer>(seedIDs), epsilon, minPoints);
                    AnalysisJob newJob = new AnalysisJob(analysisMethod, dataPointRepository, analysisJobService);
                    //Submit job
                    analysisJobService.submitAnalysisJob(newJob);
                    adminNotificationService.submitLog(AdminNotificationService.LogLevel.INFO, this.getClass().toString(), "New SowAndGrow job submitted");

                    break;
                case DBSCAN_COSINE:
                    epsilon = (int)payload.get("epsilon");
                    minPoints = (int)payload.get("minPoints");
                    //Create analysis job object
                    RunDBSCANCosine runDBSCAN = new RunDBSCANCosine(null, epsilon, minPoints);
                    AnalysisJob dbscanJob = new AnalysisJob(runDBSCAN, dataPointRepository, analysisJobService);
                    //Submit job
                    analysisJobService.submitAnalysisJob(dbscanJob);
                    adminNotificationService.submitLog(AdminNotificationService.LogLevel.INFO, this.getClass().toString(), "New DBSCAN Cosine job submitted");

                    break;

                default:
                    output.put("error", "Invalid job type");
            }
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }


            return output;
    }
    @PostMapping("/getAnalysisJobPage")
    public Map<String,Object> getAnalysisJobPage(@RequestBody Map<String, Object> payload, HttpServletRequest request)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        String token = (String)payload.get("token");
        int pageNumber = (int)payload.get("pageNumber");
        int pageSize = (int)payload.get("pageSize");
        if(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.USER) == true)
        {
            Page<AnalysisJob> jobs = analysisJobRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "id"));

            output.put("values", jobs);
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }


        return output;
    }

    @PostMapping("/inspectAnalysisJob")
    public Map<String,Object> inspectAnalysisJob(@RequestBody Map<String, Object> payload, HttpServletRequest request)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        String token = (String)payload.get("token");
        int jobId = (int)payload.get("jobId");
        //Authenticate
        if(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.USER) == true)
        {
            //Get requested data point
            AnalysisJob job = analysisJobRepository.findById(jobId);
            if(job != null)
            {
                //Fill output with fields
                output.put("JobStatus", job.jobStatus.toString());
                output.put("AnalysisType", job.analysisType.toString());
                output.put("Id", job.id);
                output.put("clusters", job.clusters);
                output.put("timeStarted", job.timeStarted);
                output.put("timeFinished", job.timeFinished);
            }
            else
            {
                output.put("error", "Could not find a job with Id: " + jobId);
            }
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }

        return output;
    }
}
