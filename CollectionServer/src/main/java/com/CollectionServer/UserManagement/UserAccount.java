package com.CollectionServer.UserManagement;

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

import java.time.*;

@Entity
public class UserAccount
{
	//User roles to track permissions for certain API calls
	public enum UserRole
	{
		USER,
		ADMIN
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer Id;

	private String username; 
		
	@Column(columnDefinition="BLOB(32)")
	private byte passwordHash[];

	private String passwordSalt;
	
	private byte[] sessionToken;
		
	private String tokenExpiration;
	
	public UserRole userRole;
	

	public UserAccount()
	{
		this.username = null;
		this.passwordHash = null;
		SecureRandom tokenGen = new SecureRandom();
		byte[] salt = new byte[32];
		tokenGen.nextBytes(salt);
		Base64.Encoder encoder = Base64.getUrlEncoder();
		this.passwordSalt = encoder.encodeToString(salt);
	}

	public void setPassword(String newPassword) throws NoSuchAlgorithmException
	{
		String saltedPassword = newPassword + this.passwordSalt;
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(saltedPassword.getBytes(StandardCharsets.UTF_8));
		byte newPasswordHash[] = messageDigest.digest();
		
		passwordHash = newPasswordHash;
	}
	
	//Check if a given session token is valid
	public boolean isValidSessionToken(String token)
	{
		boolean output = false;
		//Check that current session token is not expired
		if(LocalDateTime.now().isBefore(LocalDateTime.parse(tokenExpiration)))
		{
			//Decode token
			Base64.Decoder decoder = Base64.getUrlDecoder();
			byte[] tokenBytes = decoder.decode(token);
			//Compare passed token to stored token
			if(Arrays.equals(tokenBytes, this.sessionToken))
			{
				output = true;
			}
		}
		return output;
	}
	
	public void setUsername(String newUsername)
	{
		this.username = newUsername;
	}
	
	public byte[] getPasswordHash()
	{
		return passwordHash;
	}
	
	public void setSessionToken(byte[] newToken)
	{
		this.sessionToken = newToken;
	}
	
	public byte[] getSessionToken()
	{
		return sessionToken;
	}
	
	public void setTokenExpiration(String expirationDate)
	{
		this.tokenExpiration = expirationDate;
	}
	
	public String getTokenExpiration()
	{
		return tokenExpiration;
	}

	public String getPasswordSalt()
	{
		return passwordSalt;
	}
}
