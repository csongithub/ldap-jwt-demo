package biz.neustar.idaas.ldap.common;

import java.util.Arrays;

public enum EDeleteIndicator
{
	ACTIVE, INACTIVE, DELETED;

	public static EDeleteIndicator fromValue(final String val)
	{
		return Arrays.stream(EDeleteIndicator.values()).filter(e -> e.toString().equals(val)).findFirst().orElse(null);
	}
}
