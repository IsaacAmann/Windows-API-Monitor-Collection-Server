package com.CollectionServer;



import com.CollectionServer.ClientManagement.CollectionClient;
import com.CollectionServer.ClientManagement.CollectionClientRepository;
import com.CollectionServer.UserManagement.UserAccount;
import com.CollectionServer.UserManagement.UserAccountRepository;
import org.springframework.shell.standard.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.NoSuchAlgorithmException;

@ShellComponent
public class ShellCommands
{
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private CollectionClientRepository collectionClientRepository;

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
}
