package se.lingonskogen.gae.swejug.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Beans extends Bean
{
    public static final String PROP_TOTAL = "total";

    private Long total = 0L;
    
    private Long count = 0L;

    public Long getTotal()
    {
        return total;
    }

    public void setTotal(Long total)
    {
        this.total = total;
    }

    public Long getCount()
    {
        return count;
    }

    public void setCount(Long count)
    {
        this.count = count;
    }

}
