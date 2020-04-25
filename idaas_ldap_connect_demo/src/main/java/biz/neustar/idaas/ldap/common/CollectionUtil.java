/**
 * 
 */
package biz.neustar.idaas.ldap.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author chandan singh
 *
 */
public class CollectionUtil {

	public static final Boolean isListNullOrEmpty(Collection<?> list)
	{
		return list == null || list.isEmpty();
	}
	
	
	public static final Boolean isPopulatedList(Collection<?> list)
	{
		return ! isListNullOrEmpty(list);
	}
	
	
	
	public static final int getCount(Collection<?> list)
	{
		return list == null ? -1 : list.size();
	}
	
	
	public static final Boolean isMapNullOrEmpty(Map<?,?> map)
	{
		return map == null || map.isEmpty();
	}
	
	
	
	public static final Boolean isMapPopulated(Map<?,?> map)
	{
		return ! isMapNullOrEmpty(map);
	}
	
	
	public static final int getCount(Map<?,?> map)
	{
		return map == null ? -1 : map.size();
	}
	
	
	public static final <T> Boolean isArrayNullOrEmpty(T[] array)
	{
		return array == null || array.length == 0;
	}
	
	
	
	public static final <T> Boolean isArrayPopulated(T[] array)
	{
		return ! isArrayNullOrEmpty(array);
	}
	
	
	
	
	
	/**
	 * Generic Type list creator that will return a List from a 
	 * given Array of types <T>
	 * 
	 * @param clazz
	 * @param values
	 * @return
	 */
			
	@SafeVarargs
	public static final <T> List<T> createList(Class<T> clazz, T ...values)
	{
		if (values == null || values.length == 0)
			return null;
		
		List<T> list = new ArrayList<T>();
		list.addAll(Arrays.asList(values));
		return list;
	}

}
