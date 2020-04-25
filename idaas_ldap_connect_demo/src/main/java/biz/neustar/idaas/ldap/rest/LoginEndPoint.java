/**
 * 
 */
package biz.neustar.idaas.ldap.rest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import biz.neustar.idaas.ldap.common.BooleanResult;
import biz.neustar.idaas.ldap.common.Credentials;
import biz.neustar.idaas.ldap.common.LoginResponse;
import biz.neustar.idaas.ldap.domain.User;
import biz.neustar.idaas.ldap.jwt.JwtProvider;
import biz.neustar.idaas.ldap.persistence.UserPersistence;
import biz.neustar.idaas.ldap.service.LDAPService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chandan singh
 *
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class LoginEndPoint {
	
	
	@Autowired
	private LDAPService ldapService;
	
	@Autowired
	private UserPersistence userPersistence;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Value("${ldap.enabled}")
	private boolean ldapEnabled;

	@PostConstruct
	 private void init() {
		log.debug("Activated: " + LoginEndPoint.class.getName());
	 }
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody Credentials creds, HttpServletRequest request) {
		
		log.debug("attempt to logon as: {} remote from ip: {} ",creds.getUsername(), request.getRemoteHost());
		
		LoginResponse response = new LoginResponse();
		ResponseEntity<LoginResponse> authResponse= ResponseEntity.ok(response);
		
		try {
			BooleanResult auth = BooleanResult.FALSE;
			
			//Check if LDAP authentication is enable then, make a call to LDAP server to authenticate the user.
			if(ldapEnabled) {
				auth =ldapService.authenticateUser(creds);
			}else {
				auth = BooleanResult.TRUE;
			}
			 
			//Check if LDAP authentication is successful
			if(auth.getResult() == true) {
				//
				// Okay the user has passed the first level of authentication (LDAP Authentication), now we need to ensure that
				// they are a valid and active user of our application.
				//
				User user = userPersistence.getUserByLogonID(creds.getUsername());
				
				if(user != null) {
					//Okay, The user exist in application database.
					//Now need to issue a JWT token so that user should be able to make any security enabled APIs call with that.
					final String token = jwtProvider.generateToken(user);
					log.debug("Issued token: {} for logon request by: {}",token,creds.getUsername());
					
					response.setUser(user);
					response.setLoginStatus(true);
					response.setLoginText(LoginResponse.SUCCESS);
					response.setToken(token);
				}else {
					log.warn("login for credentials: {}; not found in database",creds.getUsername());
					response.setLoginStatus(false);
					response.setLoginText(LoginResponse.FAIL);
				}
			}
		}catch(Throwable e) {
			response.setLoginStatus(false);
			response.setLoginText(LoginResponse.FAIL);
			log.error("Error in authentication user {} ",creds.getUsername(), e);
		}
		return authResponse;
	}

}
