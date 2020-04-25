/**
 * 
 */
package biz.neustar.idaas.ldap.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import biz.neustar.idaas.ldap.common.BooleanResult;
import biz.neustar.idaas.ldap.common.Credentials;
import biz.neustar.idaas.ldap.connector.IAuthenticator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chandan
 *
 */
@Service
@Slf4j
public class LDAPService {
	
	@Autowired
	private IAuthenticator auth;
	
	
	@PostConstruct
	 private void init() {
		log.debug("Activated: " + LDAPService.class.getName());
	 }
	
	
	public BooleanResult authenticateUser(@RequestBody Credentials creds) {
		
		BooleanResult result = BooleanResult.FALSE;
		
		if (auth == null) {
			log.error("Problem in creating authenticator object");
			return result;
		}
		
		result = auth.authenticate(creds.getUsername(),creds.getPassword());
		if (result.getResult() == false)
		{
			log.error("getLogon for credentials: {}; returned exception",creds,result.getThrowable());
		}
		return result;
	}
}
