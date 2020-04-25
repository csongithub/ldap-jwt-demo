/**
 * 
 */
package biz.neustar.idaas.ldap.common;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Strings
{

    /**
     * Basic 0 length but non null String Object/
     */
    public static final String   EMPTY           = "";
    public static final String   COMMA           = ",";
    public static final String   UNDERSCORE      = "_";
    public static final String   QUOTED          = "\"";
    public static final String   DOT             = ".";
    public static final String   ZERO            = "0";
    public static final String   STAR            = "*";
    public static final String   JSON_START      = "{";
    public static final String   JSON_END        = "}";
    public static final String   JSON_SEPARATOR  = " : ";
    public static final String[] TABS            = { "", "\t", "\t\t", "\t\t\t", "\t\t\t\t", "\t\t\t\t\t", "\t\t\t\t\t\t", "\t\t\t\t\t\t\t" };
    public static final String[] NEWLINES        = { "", "\n", "\n\n", "\n\n\n", "\n\n\n\n", "\n\n\n\n\n", "\n\n\n\n\n\n", "\n\n\n\n\n\n\n" };
    public static final String   DURATION_FORMAT = "%02d:%02d:%02d";
    public static final String   CRLF            = "\r\n";
    public static final String   CR              = "\r";
    public static final String   LF              = "\n";
    public static final String   VAR_SUB_START   = "${";
    public static final String   VAR_SUB_END     = "}";
    public static final String   EXCEPTION       = "EXCEPTION";
    public static final String   SLASH           = "/";
    public static final String   COLON           = ":";

    public static final String UTF8 = "UTF-8";





    /**
     * simple method to shorten the size of a string to a maximum size. if the input string is null or if the input size
     * is < 1 then the input string is simply returned. If the input size requested is greater than the length of the
     * string then the string is returned unchopped. This method is useful in cases like logging where you want to show
     * some output by not dump the entire contents to the log file.
     * 
     * 
     * @param s
     * @param size
     * @return the input string chopped to a size of "size"
     */
    public static final String chop(String s, int size)
    {
        if (isEmpty(s) || isLengthLessThan(s, 1))
            return s;
        return s.length() > size ? s.substring(0, size) : s;
    }





    /**
     * Concatenate strings together. If any of the string values to be concatenated are null, they are excluded.
     * 
     * @param strings
     * @return
     */
    public static final String concat(String... strings)
    {
        int size = 0;
        for (String s : strings)
            if (s != null)
                size += s.length();
        StringBuilder sb = new StringBuilder(size);
        for (String s : strings)
            if (s != null)
                sb.append(s);
        return sb.toString();
    }





    /**
     * Concatenate strings together. If any of the string values to be concatenated are null, they are excluded.
     * 
     * @param strings
     * @return
     */
    public static final String concatObjects(Object... objects)
    {
        StringBuilder sb = new StringBuilder(1024);
        for (Object o : objects)
            if (o != null)
                sb.append(o);
        return sb.toString();
    }





    /**
     * Concatenate strings together but separated with the given separator character. If any of the string values to be
     * concatenated are null, they are excluded.
     * 
     * @param strings
     * @return
     */
    public static final String concat(char separator, String... strings)
    {
        int size = 0;
        for (String s : strings)
            if (s != null)
                size += s.length();
        size += strings.length;
        StringBuilder sb = new StringBuilder(size);
        for (String s : strings)
            if (s != null)
                sb.append(s).append(separator);
        if (sb.length() > 1)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }





    /**
     * Method to determine if the given input String is either null or has a trimmed length of 0. If so, return true,
     * otherwise false.
     * 
     * @param s
     * @return
     */
    public static final Boolean isEmpty(String s)
    {
        return s == null || s.length() == 0 || s.trim().length() == 0;
    }





    /**
     * return true if the given string is not null and its trimmed length is greater than 1.
     * 
     * @param s
     * @return
     */
    public static final Boolean isNotNullOrEmpty(String s)
    {
        return !isNullOrEmpty(s);
    }





    /**
     * Method to determine if the given input String is either null or has a trimmed length of 0. If so, return true,
     * otherwise false.
     * 
     * @param s
     * @return
     */
    public static final Boolean isNullOrEmpty(String s)
    {
        return s == null || s.length() == 0 || s.trim().length() == 0;
    }





    /**
     * If the given String is null, then return {@link #EMPTY}, otherwise simply return the given String. This method
     * can be used when you want to ensure that you are not using a null String object
     * 
     * @param s
     * @return
     */
    public static final String returnNonNull(String s)
    {
        return s == null ? Strings.EMPTY : s;
    }





    public static final Boolean wouldUnsetToEmpty(String currentValue, String newValue)
    {
        return Strings.isEmpty(currentValue) != true && Strings.isEmpty(newValue);
    }





    public static final Boolean wouldUnsetToNull(String currentValue, String newValue)
    {
        return Strings.isEmpty(currentValue) != true && newValue == null;
    }





    public static String getTimeDuration(Date start, Date end)
    {
        if (start == null || end == null)
            return EMPTY;

        return getTimeDuration(start.getTime(), end.getTime());
    }





    public static String getTimeDuration(Long start, Long end)
    {
        if (start == null || end == null)
            return EMPTY;

        //
        // determine the time in seconds by dividing
        // by 1000 milliseconds.
        //

        Long duration = (end - start) / 1000;

        //
        // we have a start but no end
        //

        if (duration < 0)
            return EMPTY;

        long hour = duration / 3600L;
        long min = (duration - (hour * 3600)) / 60;
        long sec = (duration - (hour * 3600) - (min * 60));
        return String.format(DURATION_FORMAT, hour, min, sec);
    }





    public static Boolean isLength(String v, int length)
    {
        return isEmpty(v) ? false : v.length() == length;
    }





    public static Boolean isLengthGreaterThan(String v, int length)
    {
        return isEmpty(v) ? false : v.length() > length;
    }





    public static Boolean isLengthLessThan(String v, int length)
    {
        return isEmpty(v) ? false : v.length() < length;
    }





    public static String camelCase(String v)
    {
        if (isEmpty(v))
            return v;
        if (isLength(v, 1))
            return v.toUpperCase();
        return v.substring(0, 1).toUpperCase() + v.substring(1);
    }





    public static String getTabs(int count)
    {
        if (count <= 0)
            return TABS[0];
        if (count >= TABS.length)
            return TABS[TABS.length - 1];
        else
            return TABS[count];
    }





    public static String getNewLines(int count)
    {
        if (count <= 0)
            return NEWLINES[0];
        if (count >= NEWLINES.length)
            return NEWLINES[NEWLINES.length - 1];
        else
            return NEWLINES[count];
    }





    public static void formatLine(StringBuilder sb, int leadingTabs, int trailingNewLines, String... values)
    {
        if (leadingTabs > 0)
            sb.append(getTabs(leadingTabs));
        for (String s : values)
            sb.append(s);
        if (trailingNewLines > 0)
            sb.append(getNewLines(trailingNewLines));
    }





    public static String getValue(Object o, String defaultString)
    {
        if (o == null)
            return defaultString == null ? Strings.EMPTY : defaultString;
        return (o instanceof String) ? (String) o : o.toString();
    }





    public static String getValue(Object o)
    {
        if (o == null)
            return Strings.EMPTY;
        return (o instanceof String) ? (String) o : o.toString();
    }





    public static String toString(Object o)
    {
        if (o == null)
            return Strings.EMPTY;

        StringBuilder sb = new StringBuilder(512);
        sb.append(JSON_START).append(QUOTED).append(o.getClass().getName()).append(QUOTED).append(JSON_SEPARATOR).append(JSON_START);

        if (visited.get().contains(o.hashCode()))
                return "ref@" + o.hashCode();
        else
            visited.get().add(o.hashCode());
        
        Class<?> c = o.getClass();
        while (c != null)
        {
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields)
            {
                int m = f.getModifiers();
                if (Modifier.isStatic(m))
                    continue;
                if (f.isAnnotationPresent(ToStringIgnore.class) && f.getAnnotation(ToStringIgnore.class).mask() == false)
                    continue;
                try
                {
                    sb.append(QUOTED).append(f.getName()).append(QUOTED).append(JSON_SEPARATOR);
                    if (f.isAnnotationPresent(ToStringIgnore.class) && f.getAnnotation(ToStringIgnore.class).mask() == true)
                    {
                        sb.append("???").append(COMMA);
                        continue;
                    }
                    f.setAccessible(true);
                    Object oValue = f.get(o);
                    if (oValue == null)
                        sb.append("null,");
                    else if (oValue instanceof Number)
                        sb.append(oValue).append(COMMA);
                    else
                        sb.append(QUOTED).append(oValue).append(QUOTED).append(Strings.COMMA);
                }
                catch (Throwable t)
                {
                    log.error("Unable to produce toString", t);
                    continue;
                }
            }
            c = c.getSuperclass();
            if (c == Object.class)
                break;
        }
        visited.get().remove(o.hashCode());
        sb.setLength(sb.length() - 1);
        sb.append(JSON_END).append(JSON_END);
        return sb.toString();
    }

    private static ThreadLocal<Set<Integer>> visited = ThreadLocal.withInitial(() -> new HashSet<Integer>());
}

