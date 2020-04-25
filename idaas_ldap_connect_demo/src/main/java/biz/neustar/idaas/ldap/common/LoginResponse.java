/**
 * 
 */
package biz.neustar.idaas.ldap.common;

import java.io.Serializable;

import biz.neustar.idaas.ldap.domain.User;

/**
 * @author chandan singh
 *
 */
public class LoginResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6944385185046255856L;
	
	
	public static final String SUCCESS 	= "Login Successful";
	public static final String FAIL		= "Login Failed";
	
	private User user;
	
	private String token;
	
	private boolean loginStatus;
	
	private String loginText;

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the loginStatus
	 */
	public boolean isLoginStatus() {
		return loginStatus;
	}

	/**
	 * @param loginStatus the loginStatus to set
	 */
	public void setLoginStatus(boolean loginStatus) {
		this.loginStatus = loginStatus;
	}

	/**
	 * @return the loginText
	 */
	public String getLoginText() {
		return loginText;
	}

	/**
	 * @param loginText the loginText to set
	 */
	public void setLoginText(String loginText) {
		this.loginText = loginText;
	}
}	
