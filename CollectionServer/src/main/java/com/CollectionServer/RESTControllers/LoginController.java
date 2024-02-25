package com.CollectionServer.RESTControllers;

import com.CollectionServer.UserManagement.UserAccount;
import com.CollectionServer.UserManagement.UserAccountRepository;
import com.CollectionServer.Services.UserAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController
{
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UserAuthenticationService userAuthenticationService;



    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws NoSuchAlgorithmException
    {
        HashMap<String, Object> output = new HashMap<String, Object>();

        String username = (String)payload.get("username");
        String password = (String)payload.get("password");

        UserAccount user = userAccountRepository.findByUsername(username);

        if(user != null && user.checkPassword(password))
        {
            //Generate a token
            String token = userAuthenticationService.issueToken(user);
            output.put("token", token);
        }
        else
        {
            output.put("errorMessage", "Invalid username or password");
        }

        return output;
    }
}
