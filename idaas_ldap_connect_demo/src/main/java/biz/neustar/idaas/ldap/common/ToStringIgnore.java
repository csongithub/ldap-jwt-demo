/**
 * 
 */
package biz.neustar.idaas.ldap.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used in conjunction with 
 * the {@link Strings.toString(Object)} method.
 * Annotate fields that you do not want to be part of the toString
 * output.
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface ToStringIgnore
{
    /**
     * when a field is marked with this annotation and mask is true,
     * then the field name is part of the output and the value is 
     * written as: "???"
     * @return
     */
    boolean mask() default false;
}
