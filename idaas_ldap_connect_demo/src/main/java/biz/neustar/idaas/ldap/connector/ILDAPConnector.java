/**
 * 
 */
package biz.neustar.idaas.ldap.connector;

/**
 * Simple interface used to pass connection information to an LDAP service.
 * 
 * @author Scott Brand
 *
 */
public interface ILDAPConnector {
	

	String getURL();





	String getPrincipal();





	String getPassword();





	String getScheme();





	String getOU(); // "DC=company,DC=local";





	String getLogonFilter(); // "(&(objectClass=*)(sAMAccountName={0}))";





	String getSurNameFilter(); // "(&(objectClass=*)(sn={0}*))"





	/**
	 * should return true if the LDAP connection is SSL. For example, if the URL
	 * is "ldaps://"
	 * 
	 * @return true is using SSL, false otherwise. The default should be false;
	 */
	boolean isSSL();






	/**
	 * This value only applies when the LDAP url is "ldaps://" and we want to by
	 * pass host verification of the SSL certificate. Normally, you should not
	 * do this and the SSL host certificates should be part of the applications
	 * key store.
	 * 
	 * @return true to by pass verification, false otherwise. The default is
	 *         should be true;
	 */
	boolean isSkipHostVerification();




}
