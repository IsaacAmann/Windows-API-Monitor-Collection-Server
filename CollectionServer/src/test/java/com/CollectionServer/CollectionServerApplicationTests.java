package com.CollectionServer;

import com.CollectionServer.ClientManagement.CollectionClient;
import com.CollectionServer.DataAnalysis.AnalysisJob;
import com.CollectionServer.Services.UserAuthenticationService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import com.CollectionServer.UserManagement.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
class CollectionServerApplicationTests {

	@Test
	public void testLogin() throws NoSuchAlgorithmException
	{
		String testUsername = "Test";
		String testPassword = "longpassword";

		UserAccount testAccount = new UserAccount();
		testAccount.setUsername(testUsername);
		testAccount.setPassword(testPassword);

		//Correct password
		assertTrue(testAccount.checkPassword(testPassword));
		//Test with invalid password
		assertFalse(testAccount.checkPassword("Wrongpassword"));

	}

	@Test
	public void testRequestAuthentication() throws NoSuchAlgorithmException
	{
		UserAuthenticationService userAuthenticationService = new UserAuthenticationService();

		String username = "TestUser";
		String testPassword = "longpassword";

		UserAccount testAccount = new UserAccount();
		testAccount.setUsername(username);
		testAccount.setPassword(testPassword);

		String token = userAuthenticationService.issueToken(testAccount);

		//Generate and sign token
		Algorithm signingAlgorithm = Algorithm.HMAC512("WrongKey");
		String badToken = JWT.create()
				.withIssuer("WindowsAPIMonitorProject")
				.withClaim("username", testAccount.username)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + 10000))
				.withJWTId(UUID.randomUUID().toString())
				.withClaim("userRole", testAccount.userRole.toString())
				.sign(signingAlgorithm);

		//Matching token, matching UserRole
		assertTrue(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.USER));

		//Matching token, mismatching UserRole
		assertFalse(userAuthenticationService.authenticateRequest(token, UserAccount.UserRole.ADMIN));

		//Valid token, signed with different private key
		assertFalse(userAuthenticationService.authenticateRequest(badToken, UserAccount.UserRole.USER));

		//Malformed token
		assertFalse(userAuthenticationService.authenticateRequest("random text", UserAccount.UserRole.USER));


	}

	@Test
	public void testCollectionClientAuth()
	{
		CollectionClient testClient = new CollectionClient();
		CollectionClient otherClient = new CollectionClient();

		//Correct token
		assertTrue(testClient.isValidAPIToken(testClient.getEncodedAPIToken()));

		//Test for correctly encoded text, but wrong token
		assertFalse(testClient.isValidAPIToken(otherClient.getEncodedAPIToken()));

		//Test for malformed token
		assertFalse(testClient.isValidAPIToken("Randomtext"));

	}
}





