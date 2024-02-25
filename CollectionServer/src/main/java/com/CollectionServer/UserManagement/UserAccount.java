package com.CollectionServer.UserManagement;

import com.CollectionServer.Services.UserAuthenticationService;

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
	@Transient
	@Autowired
	private UserAuthenticationService authService;


	//User roles to track permissions for certain API calls
	public enum UserRole
	{
		USER,
		ADMIN
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer Id;

	public String username;
		
	@Column(columnDefinition="BLOB(32)")
	private byte passwordHash[];

	private String passwordSalt;
	

	public UserRole userRole;
	

	public UserAccount()
	{
		this.username = null;
		this.passwordHash = null;
		this.userRole = UserRole.USER;
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
	
	public boolean checkPassword(String enteredPassword) throws NoSuchAlgorithmException
	{
		boolean output = false;
		//Append salt to entered password
		String saltedPassword = enteredPassword + passwordSalt;
		//Hash salted password
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(saltedPassword.getBytes(StandardCharsets.UTF_8));
		byte enteredPasswordHash[] = messageDigest.digest();

		//Compare stored hash
		output = Arrays.equals(passwordHash, enteredPasswordHash);

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

	public String getPasswordSalt()
	{
		return passwordSalt;
	}
}
