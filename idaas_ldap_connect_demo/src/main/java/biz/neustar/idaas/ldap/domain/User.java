/**
 * 
 */
package biz.neustar.idaas.ldap.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import biz.neustar.idaas.ldap.common.EDeleteIndicator;
import biz.neustar.idaas.ldap.common.Strings;


/**
 * @author chandan singh
 *
 */
@Entity
@Table(name = "USERS")
public class User implements Serializable {

	private static final long	serialVersionUID	= 1305242075920295990L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	Long						id;

	@Column(name = "LOGON_ID")
	String						logonID;

	@Column(name = "USER_NAME")
	String						name;

	@Column(name = "EMAIL_ADDRESS")
	private String				emailAddress;

	@Column(name = "CREATOR_USER_ID")
	private Long				createdBy;

	@Column(name = "CREATE_TS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				createdTimestamp;

	@Column(name = "LAST_LOGON_TS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastLogonTimestamp;

	@Column(name = "SERVICE_ACCOUNT")
	private Integer				serviceAccountIndicator;

	@Column(name = "EXTERNAL_ACCOUNT")
	private Integer				externalAccountIndicator;

	@Column(name = "SOFT_DEL_CD")
	@Enumerated()
	private EDeleteIndicator	deletedIndicator;





	public Long getId()
	{
		return id;
	}





	public void setId(Long id)
	{
		this.id = id;
	}





	public String getLogonID()
	{
		return logonID;
	}





	public void setLogonID(String logonID)
	{
		this.logonID = logonID;
	}





	public String getName()
	{
		return name;
	}





	public void setName(String name)
	{
		this.name = name;
	}





	public String getEmailAddress()
	{
		return emailAddress;
	}





	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}





	public Long getCreatedBy()
	{
		return createdBy;
	}





	public void setCreatedBy(Long createdBy)
	{
		this.createdBy = createdBy;
	}





	public Date getCreatedTimestamp()
	{
		return createdTimestamp;
	}





	public void setCreatedTimestamp(Date createdTimestamp)
	{
		this.createdTimestamp = createdTimestamp;
	}





	public Date getLastLogonTimestamp()
	{
		return lastLogonTimestamp;
	}





	public void setLastLogonTimestamp(Date lastLogonTimestamp)
	{
		this.lastLogonTimestamp = lastLogonTimestamp;
	}





	public Integer getServiceAccountIndicator()
	{
		return serviceAccountIndicator;
	}





	public void setServiceAccountIndicator(Integer serviceAccountIndicator)
	{
		this.serviceAccountIndicator = serviceAccountIndicator;
	}





	public Integer getExternalAccountIndicator()
	{
		return externalAccountIndicator;
	}





	public void setExternalAccountIndicator(Integer externalAccountIndicator)
	{
		this.externalAccountIndicator = externalAccountIndicator;
	}





	public EDeleteIndicator getDeletedIndicator()
	{
		return deletedIndicator;
	}





	public void setDeletedIndicator(EDeleteIndicator deletedIndicator)
	{
		this.deletedIndicator = deletedIndicator;
	}





	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}





	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}





	public String toString()
	{
		return Strings.toString(this);
	}


}
