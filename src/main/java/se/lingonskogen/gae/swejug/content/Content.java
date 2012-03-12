package se.lingonskogen.gae.swejug.content;

import java.util.HashMap;
import java.util.Map;

public class Content
{
    public static final String KIND = Content.class.getSimpleName();

    public static final String PROP_TYPE = "type";

    public static final String PROP_TOTAL = "total";

    private String type;
    
    private Map<String, Long> total = new HashMap<String, Long>();

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Map<String, Long> getTotal()
    {
        return total;
    }

    @Override
    public String toString()
    {
        return "Content [type=" + type + ", total=" + total + "]";
    }
    
}
