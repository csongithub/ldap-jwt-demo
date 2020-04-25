/**
 * 
 */
package biz.neustar.idaas.ldap.common;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Simple wrapper object to be used in cases where you want a method to return a
 * typed value of some kind (i.e. String), but also be able to inspect a Throwable that might have been
 * caught during the method call when the return value is false. Thus it leaves
 * the caller in charge of what to do in the case of an exception.
 * 
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public final class TypedResult<T> implements AutoCloseable
{
    private T                         result;
    private Throwable                 throwable;
    
    //
    // declare the valid field, but we don't really use it.
    // However, when Jackson ObjectMapper want to rebuild this object
    // it wants this field because of the "isValid" method.
    //
    @SuppressWarnings("unused")
	private boolean                   valid;






    /**
     * Default constructor which sets the result to <code>null</code> and the
     * throwable to <code>null</code>
     */
    protected TypedResult()
    {
        result    = null;
        throwable = null;
    }


    
    
    
    
    /**
     * Constructor which sets the result to given T value and the
     * throwable to <code>null</code>
     */
    public TypedResult(T result)
    {
        this();
        this.result = result;
    }



    
    
    
    /**
     * Constructor which sets the result to null and the
     * throwable to the given <code>throwable</code>
     */
    public TypedResult(Throwable t)
    {
        this();
        throwable = t;
    }




    /**
     * Get the T result object
     * 
     * @return T value
     */
    public T getResult()
    {
        return result;
    }




    /**
     * Get the throwable if any 
     * @return
     */
    public Throwable getThrowable()
    {
        return throwable;
    }
    
    
    
    /**
     * convenience method to easily detect that the result T is not null
     * and that the throwable is null, which would imply that we have a good
     * result type.
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        return result != null && throwable == null;
    }






    @Override
    public void close() throws Exception
    {
        if (result != null && result instanceof AutoCloseable)
            ((AutoCloseable)result).close();
    }
    
    
    
    
    public String toString()
    {
    	if (isValid() == false)
    		return this.getClass().getName() + "; Returned non-valid result; exception message is: " + throwable.getMessage();
    	return this.getClass().getName() + "; Returned valid result of: " + result;
    }
    
    
    
    
    /**
     * Convenience method when user has request that a TypedResult returns
     * a TypedList<T>
     * When coming back from a RESTful call the Jackson mapper will typically convert a map to
     * a LinkedHashMap and in the case of the TypedList<T> object, the key "list" will
     * contain the List<T> that we want.   
     * This method does the dirty work off casting the object back to a List of V.
     * 
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
	public <V> List<V> getList(Class<V> clazz)
    {
    	return (List<V>)(((LinkedHashMap<?,?>)(Object)result).get("list"));
    }

}
