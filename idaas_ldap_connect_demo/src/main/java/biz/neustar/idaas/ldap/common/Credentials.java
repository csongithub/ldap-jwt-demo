/**
 * 
 */
package biz.neustar.idaas.ldap.common;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chandan
 *
 */
public class Credentials implements Serializable {
	private static final long serialVersionUID = 8142524464988115028L;
	
	private String	username;
	private String	password;
	private Long	userID;
	private Date    loginDate;
	


	public Date getLoginDate()
	{
		return loginDate;
	}





	public void setLoginDate(Date loginDate)
	{
		this.loginDate = loginDate;
	}





	public Long getUserID()
	{
		return userID;
	}





	public void setUserID(Long userID)
	{
		this.userID = userID;
	}





	public String getUsername()
	{
		return username;
	}





	public void setUsername(String username)
	{
		this.username = username;
	}





	public String getPassword()
	{
		return password;
	}





	public void setPassword(String password)
	{
		this.password = password;
	}

}
