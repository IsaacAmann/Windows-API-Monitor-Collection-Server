package com.CollectionServer;



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
}
