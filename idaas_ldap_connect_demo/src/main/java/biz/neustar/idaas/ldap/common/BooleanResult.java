package biz.neustar.idaas.ldap.common;

/**
 * Simple wrapper object to be used in cases where you want a method to return a
 * boolean value, but also be able to inspect a Throwable that might have been
 * caught during the method call when the return value is false. Thus it leaves
 * the caller in charge of what to do in the case of an exception.
 * 
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public final class BooleanResult
{
    private boolean                   result;
    private Throwable                 throwable;

    /**
     * Static convenience object to return the "true" condition without having
     * to explicitly create a "new" object each time.
     */
    public static final BooleanResult TRUE = new BooleanResult();


    /**
     * Static convenience object to return the "false" condition without having
     * to explicitly create a "new" object each time.
     */
    public static final BooleanResult FALSE = new BooleanResult(false);



    /**
     * Default constructor which sets the result to <code>true</code> and the
     * throwable to <code>null</code>
     */
    public BooleanResult()
    {
        result = true;
        throwable = null;
    }


    
    
    
    
    /**
     * Constructor which sets the result to given boolean value and the
     * throwable to <code>null</code>
     */
    public BooleanResult(boolean result)
    {
        this.result = result;
    }



    
    
    
    /**
     * Constructor which sets the result to <code>false</code> and the
     * throwable to the given <code>throwable</code>
     */
    public BooleanResult(Throwable t)
    {
        result = false;
        throwable = t;
    }




    /**
     * Get the boolean result of this object
     * 
     * @return boolean value
     */
    public boolean getResult()
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

}

