package com.CollectionServer.RESTControllers;

import com.CollectionServer.Services.AdminNotificationService;
import com.CollectionServer.UserManagement.UserAccount;
import com.CollectionServer.ShellCommands;
import com.CollectionServer.UserManagement.UserAccountRepository;
import com.CollectionServer.Services.UserAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.lang.management.*;

import java.lang.reflect.*;

@RestController
public class RemoteShellController
{

    @Autowired
    private UserAuthenticationService userAuthenticationService;
    @Autowired
    private ShellCommands shellCommands;
    @Autowired
    private AdminNotificationService adminNotificationService;


    @PostMapping("/logPage")
    public Map<String,Object> logPage(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws NoSuchAlgorithmException
    {
        HashMap<String, Object> output = new HashMap<String, Object>();

        String token = (String)payload.get("token");
        int pageNumber = (int)payload.get("pageNumber");
        int pageSize = (int)payload.get("pageSize");

        //Authenticate
        if(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.ADMIN) == true)
        {
            output.put("logEntries", adminNotificationService.getLogPage(pageSize, pageNumber));
            output.put("totalElements", adminNotificationService.logsArray.size());
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }

        return output;
    }

    @PostMapping("/systemInfo")
    public Map<String,Object> getSystemInfo(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws NoSuchAlgorithmException, IOException
    {
        HashMap<String, Object> output = new HashMap<String, Object>();

        String token = (String)payload.get("token");

        //Authenticate
        if(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.ADMIN) == true)
        {
            JacksonJsonParser parser = new JacksonJsonParser();

            Process process = Runtime.getRuntime().exec("mpstat -u -o JSON -P ALL 1 1");
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String json = "";
            String currentLine = input.readLine();
            while(currentLine  != null)
            {
                json += currentLine;
                currentLine = input.readLine();
            }
            output.put("stats", parser.parseMap(json));

            process = Runtime.getRuntime().exec("cat /proc/meminfo");
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));

            currentLine = input.readLine();
            while(currentLine  != null)
            {
                //Parse current line of /proc/meminfo
                String[] strings = currentLine.split(":");

                String[] otherStrings = strings[1].split("kB");
               // System.out.println(otherStrings[0]);
                output.put(strings[0], Long.parseLong(otherStrings[0].trim()));
                currentLine = input.readLine();
            }


            input.close();
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }

        return output;
    }

    @PostMapping("/shellCommand")
    public Map<String,Object> shellCommand(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws NoSuchAlgorithmException
    {
        HashMap<String, Object> output = new HashMap<String, Object>();

        String token = (String)payload.get("token");
        String command = (String)payload.get("command");


        //Authenticate
        if(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.ADMIN) == true)
        {
            Method[] methods = shellCommands.getClass().getMethods();
            String[] tokenStrings = command.split(" ");
            Object[] tokens = new Object[tokenStrings.length];

            for(int i = 0; i < tokens.length; i++)
            {
                tokens[i] = tokenStrings[i];
            }

            boolean valid = false;
            Method method = null;

            for(int i = 0; i < methods.length; i++)
            {
                if(methods[i].getName().equals(tokens[0]))
                {
                    valid = true;
                    method = methods[i];
                    break;
                }
            }
            //Run command and return result
            if(valid == true) {
                try {
                    Class<?>[] types = method.getParameterTypes();
                    for (int i = 1; i < tokens.length; i++) {
                        System.out.println(types[i - 1]);
                        System.out.println(Integer.TYPE);
                        if (types[i - 1] == Integer.TYPE) {
                            tokens[i] = Integer.parseInt((String) tokens[i]);

                        } else if (types[i - 1] == Float.TYPE) {
                            tokens[i] = Float.parseFloat((String) tokens[i]);
                        } else if (types[i - 1] == Double.TYPE) {
                            tokens[i] = Double.parseDouble((String) tokens[i]);
                        }
                    }
                    String returnMessage = (String) method.invoke(shellCommands, Arrays.copyOfRange(tokens, 1, tokens.length));
                    output.put("result", returnMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                    output.put("result", e.getMessage());
                }
            }
        }
        else
        {
            output.put("error", "Failed to authenticate");
        }
        return output;
    }
}
