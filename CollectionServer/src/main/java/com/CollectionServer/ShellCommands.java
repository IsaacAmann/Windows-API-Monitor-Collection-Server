package com.CollectionServer;



import com.CollectionServer.ClientManagement.CollectionClient;
import com.CollectionServer.ClientManagement.CollectionClientRepository;
import com.CollectionServer.UserManagement.UserAccount;
import com.CollectionServer.UserManagement.UserAccountRepository;
import org.springframework.shell.standard.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.CollectionServer.DataAnalysis.*;

import java.lang.reflect.Method;
import com.CollectionServer.Services.AnalysisJobService;

import java.security.NoSuchAlgorithmException;

import java.util.UUID;
import java.util.ArrayList;

@ShellComponent
public class ShellCommands
{
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private CollectionClientRepository collectionClientRepository;
    
    @Autowired 
    private DataPointRepository dataPointRepository;
    
    @Autowired
    private AnalysisJobService analysisJobService;

    @ShellMethod(key = "createUser")
    public String createUser(@ShellOption String username, @ShellOption String password) throws NoSuchAlgorithmException
    {
        String output = "Created new user";

        UserAccount newUser = new UserAccount();
        newUser.setUsername(username);
        newUser.setPassword(password);

        userAccountRepository.save(newUser);

        return output;
    }

    @ShellMethod(key = "registerClient")
    public String registerClient()
    {
        String output = "";
        //Create new CollectionClient
        CollectionClient newClient = new CollectionClient();

        output = output + "UUID: " + newClient.clientID.toString();
        output = output + "API Key: " + newClient.getEncodedAPIToken();

        collectionClientRepository.save(newClient);

        return output;
    }
    @ShellMethod(key = "showCommands")
    public String showCommands()
    {
        String output = "";
        Method[] methods = this.getClass().getMethods();
        System.out.println(methods.length);
        for(int i = 0; i < methods.length; i++)
        {

            output = output.concat(methods[i].getName() + "\n");
        }

        return output;
    }
    @ShellMethod(key = "setUserRole")
    public String setUserRole(String username, String userRole)
    {
        String output = "User " + username + " does not exist";
        UserAccount targetUser = userAccountRepository.findByUsername(username);
        if(targetUser != null)
        {
            //Set user account role to passed role
            UserAccount.UserRole role = UserAccount.UserRole.valueOf(userRole);
            targetUser.userRole = role;
            //Save user to repository
            userAccountRepository.save(targetUser);
            output = username + " user role set to " + role.toString();
        }
        return output;
    }
    
    @ShellMethod(key = "createTestDatapoints")
    public String createTestDatapoints(int numberPoints)
    {
		for(int i = 0; i < numberPoints; i++)
		{
			//Create new datapoint
            DataPointEntity dataPoint= new DataPointEntity();
            //dataPoint.WinAPICounts = winAPICalls;
            dataPoint.originClientId = UUID.randomUUID();
            dataPoint.executablePath = "TEST DATA POINT";
            dataPointRepository.save(dataPoint);
		}
		
		return "Created " + numberPoints + " test data points";
	}
	
	@ShellMethod(key = "testSowAndGrow")
	public String testSowAndGrow()
	{
		ArrayList<Integer> seedList = new ArrayList<Integer>();
		seedList.add(1);
		seedList.add(53);
		
		RunSowAndGrow sowAndGrow = new RunSowAndGrow(seedList, 25, 4);
		
		AnalysisJob testJob = new AnalysisJob(sowAndGrow, dataPointRepository, analysisJobService);
		
		analysisJobService.submitAnalysisJob(testJob);
		
		return "Submitted test job";
	}
	
	@ShellMethod(key = "updateRatios")
	public void updateRatios()
	{
		for(DataPointEntity datapoint : dataPointRepository.findAll())
		{
			datapoint.updateRatios();
			dataPointRepository.save(datapoint);
		}
	}
}
