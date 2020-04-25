/**
 * 
 */
package biz.neustar.idaas.ldap.connector;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Basic implementation of the LDAPConnector interface. This class can be
 * injected as the implementation of the ILDAPConnector interface.
 * 
 * The configuration to establish the LDAP connection to a server is externally
 * read and values should be encrypted with Hex Encoding. Thus this component
 * has a dependency on a IPasswordEncryptor (hex encoding)
 * 
 * @author Scott Brand
 * 
 */
@Component
@Slf4j
public class LDAPConnector implements ILDAPConnector {
	//public static final String				PID			= "biz.neustar.dna.common.provider.ldap";

	@Value("${ldap.url}")
	protected String						url;
	
	@Value("${ldap.principal}")
	protected String						principal;
	
	@Value("${ldap.password}")
	protected String						password;
	
	@Value("${ldap.scheme}")
	protected String						scheme;
	
	@Value("${ldap.ou}")
	protected String						ou;
	
	@Value("${ldap.logonFilter}")
	protected String						logonFilter;
	
	@Value("${ldap.surNameFilter}")
	protected String						surNameFilter;
	
	@Value("${ldap.useSSL}")
	protected boolean						useSSL;
	
	@Value("${ldap.skipHostVerification}")
	protected boolean						skipHostVerification;

	
	
	
	@PostConstruct
	 private void init() {
		log.debug("Activated: " + LDAPConnector.class.getName());
	 }
	 


	@Override
	public String getURL()
	{
		return url;
	}





	public void setUrl(String url)
	{
		this.url = url;
	}





	@Override
	public String getPrincipal()
	{
		return principal;
	}





	@Override
	public String getPassword()
	{
		return password;
	}





	@Override
	public String getScheme()
	{
		return scheme;
	}





	@Override
	public String getOU()
	{
		return ou;
	}





	@Override
	public String getLogonFilter()
	{
		return logonFilter;
	}





	public String getOu()
	{
		return ou;
	}





	public void setOu(String ou)
	{
		this.ou = ou;
	}





	public String getUrl()
	{
		return url;
	}





	public void setPrincipal(String principal)
	{
		this.principal = principal;
	}





	public void setPassword(String password)
	{
		this.password = password;
	}





	public void setScheme(String scheme)
	{
		this.scheme = scheme;
	}





	public void setLogonFilter(String logonFilter)
	{
		this.logonFilter = logonFilter;
	}





	@Override
	public String getSurNameFilter()
	{
		return surNameFilter;
	}





	public void setSurNameFilter(String surNameFilter)
	{
		this.surNameFilter = surNameFilter;
	}





	public boolean isSSL()
	{
		return useSSL;
	}





	public boolean isSkipHostVerification()
	{
		return skipHostVerification;
	}
}
