/**
 * 
 */
package biz.neustar.idaas.ldap.connector;

import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;
import org.springframework.beans.factory.annotation.Autowired;

import biz.neustar.idaas.ldap.common.BooleanResult;
import biz.neustar.idaas.ldap.common.TypedResult;

/**
 * @author chandan singh
 *
 */
public abstract class LDAPBaseConnector {

	@Autowired
	protected ILDAPConnector	connector;
	
	protected DirContext		ctx;



	/**
	 * The connect method will attempt to make a connection to the LDAP
	 * server(s) based on properties derived by the interface.
	 *
	 * @throws Exception
	 *             if a connection can not be established.
	 */	
	protected TypedResult<DirContext> connect(String principal, String password)
	{
		try
		{

			Hashtable<String, String> env = createEnvironment(principal, password);

			ClassLoader contextClassLoader = null;

			try
			{
				if (connector.isSSL() && connector.isSkipHostVerification())
				{
					contextClassLoader = Thread.currentThread().getContextClassLoader();
					Thread.currentThread().setContextClassLoader(SSLUnverifiedSocketFactory.class.getClassLoader());
				}

				DirContext ctx = new InitialLdapContext(env, null);
				return new TypedResult<DirContext>(ctx);
			}
			finally
			{
				if (contextClassLoader != null)
					Thread.currentThread().setContextClassLoader(contextClassLoader);
			}
		}
		catch (Throwable t)
		{
			return new TypedResult<DirContext>(t);
		}
		
	}


	/**
	 * The connect method will attempt to make a connection to the LDAP
	 * server(s) based on properties derived by the interface.
	 *
	 * @throws Exception
	 *             if a connection can not be established.
	 */

	protected BooleanResult connect()
	{
		TypedResult<DirContext> dirContext =  connect(connector.getPrincipal(), connector.getPassword());
		if (dirContext.isValid() && dirContext.getResult() != null)
		{
			ctx = dirContext.getResult();
			return BooleanResult.TRUE;
		}
		else if (dirContext.getThrowable() != null)
			return new BooleanResult(dirContext.getThrowable());
		return BooleanResult.FALSE;
	}





	/**
	 * Basic way of building an Environment object to allow us to connect to an
	 * LDAP server.
	 * 
	 * @param principal
	 * @param password
	 * @return
	 */
	protected Hashtable<String, String> createEnvironment(String principal, String password)
	{
		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, connector.getURL());
		env.put(Context.SECURITY_AUTHENTICATION, connector.getScheme());
		// env.put(Context.SECURITY_PRINCIPAL, connector.getPrincipal());
		// env.put(Context.SECURITY_CREDENTIALS, connector.getPassword());
		env.put(Context.SECURITY_PRINCIPAL, principal);
		env.put(Context.SECURITY_CREDENTIALS, password);

		//
		// Check SSL settings
		//
		if (connector.isSSL() && connector.isSkipHostVerification())
			env.put("java.naming.ldap.factory.socket", SSLUnverifiedSocketFactory.class.getName());

		if (connector.isSSL())
			env.put(Context.SECURITY_PROTOCOL, "ssl");

		return env;
	}





	/**
	 * The isConnected method will return the state of the LDAP context
	 * connection. This method will return the current state of the context, but
	 * does not actually test it. Therefore, eventhough this method may report
	 * that a connection is established, the next time the connection is
	 * actually used, the application may find that it is no longer connected
	 * due to external system time outs, or other issues.
	 * 
	 * @return true if the context is defined, false if the context is null
	 *         which implies that there is no current connection.
	 */

	protected boolean isConnected()
	{
		return false;     //(ctx != null);
	}





	/**
	 * The disconnect method will close the current LDAP context connection. And
	 * set the internal context attribute to null.
	 * 
	 */
	protected void disconnect()
	{
		disconnect(ctx);
		ctx = null;
	}
	
	
	
	
	
	/**
	 * The disconnect method will close the current LDAP context connection. And
	 * set the internal context attribute to null.
	 * 
	 */
	protected void disconnect(DirContext someContext)
	{
		try
		{
			if (someContext != null)
				someContext.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			someContext = null;
		}
	}





	protected TypedResult<NamingEnumeration<?>> search(String searchFrom, String searchFilter, Object[] searchArgs, String attribute)
	{
		try
		{
			String[] attributeArray = null;

			SearchControls controls = new SearchControls();
			NamingEnumeration<?> ne = null;

			if (attribute == null)
				attributeArray = new String[1];
			else if (attribute.indexOf(",") == -1)
				attributeArray = new String[] { attribute };
			else
			{
				StringTokenizer st = new StringTokenizer(attribute, ",");
				attributeArray = new String[st.countTokens()];
				for (int i = 0; st.hasMoreTokens(); i++)
					attributeArray[i] = st.nextToken();
			}

			controls.setReturningAttributes(attributeArray);
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			//
			// make sure we are connected before doing a search of the context.
			//

			if (isConnected() == false)
				connect();

			try
			{
				ne = ctx.search(searchFrom, searchFilter, searchArgs, controls);
			}
			catch (Throwable t)
			{
				disconnect();
				// connect();
				return new TypedResult<NamingEnumeration<?>>(t);
			}

			return new TypedResult<NamingEnumeration<?>>(ne);
		}
		catch (Throwable t)
		{
			return new TypedResult<NamingEnumeration<?>>(t);
		}
	}





	public void setConnector(ILDAPConnector connector)
	{
		this.connector = connector;
	}





	protected String getGeneralOU()
	{
		return connector.getOU(); // "DC=comany,DC=local";
	}





	protected String getGeneralSearchFilter()
	{
		return connector.getLogonFilter(); // "(&(objectClass=*)(sAMAccountName={0}))";
	}





	protected String getSurNameFilter()
	{
		return connector.getSurNameFilter(); // "(&(objectClass=*)(sn={0}*))"
	}


}
