package com.CollectionServer.ClientManagement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.Arrays;
import java.util.Base64; 


@Entity
public class CollectionClient
{	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer Id;
	
	public UUID clientID;
		
	private byte[] APIToken;

	
	public CollectionClient()
	{
		SecureRandom tokenGen = new SecureRandom();
		Base64.Encoder encoder = Base64.getUrlEncoder();
		
		byte[] tokenArray = new byte[32];
		tokenGen.nextBytes(tokenArray);

		clientID = UUID.randomUUID();

		//Set new API token
		APIToken = tokenArray;
	}
	
	//Check if a given API Token is valid
	public boolean isValidAPIToken(String token)
	{
		boolean output = false;
		//Decode token
		Base64.Decoder decoder = Base64.getUrlDecoder();
		byte[] tokenBytes = decoder.decode(token);
		//Compare passed token to stored token
		if(Arrays.equals(tokenBytes, this.APIToken))
		{
			output = true;
		}
		
		return output;
	}
	
}
