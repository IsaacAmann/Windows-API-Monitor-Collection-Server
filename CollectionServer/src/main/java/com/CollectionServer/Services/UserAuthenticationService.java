package com.CollectionServer.Services;

import com.CollectionServer.UserManagement.UserAccount;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.UUID;

//see https://www.javadoc.io/doc/com.auth0/java-jwt/3.1.0/
@Service
public class UserAuthenticationService
{
    //Set token to expire after a day
    final long TOKEN_LIFE_TIME = 86400000;

    private Algorithm signingAlgorithm = Algorithm.HMAC512("TESTKEYCHANGETHIS");

    private JWTVerifier verifier = JWT.require(signingAlgorithm).withIssuer("WindowsAPIMonitorProject").build();

    //Should assume that calling method has authenticated with password already
    public String issueToken(UserAccount user)
    {
        //Generate and sign token
        String token = JWT.create()
                .withIssuer("WindowsAPIMonitorProject")
                .withClaim("username", user.username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_LIFE_TIME))
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("userRole", user.userRole.toString())
                .sign(signingAlgorithm);

        return token;
    }

    public DecodedJWT verifyToken(String token)
    {
        try
        {
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        }
        catch (JWTVerificationException e)
        {
            System.out.println("Failed to verify Token");
            return null;
        }
    }

    public boolean authenticateRequest(String token, UserAccount.UserRole requiredUserRole)
    {
        boolean output = false;

        DecodedJWT jwt = verifyToken(token);
        if(jwt != null)
        {
            //Check that user role matches
            if(requiredUserRole == UserAccount.UserRole.valueOf(jwt.getClaim("userRole").asString()))
            {
                output = true;
            }
        }


        return output;
    }

}

