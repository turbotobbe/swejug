package se.lingonskogen.gae.swejug.rest;

import java.net.URI;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Link
{
    private String type;
    
    private URI href;
    
    private String rel;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public URI getHref()
    {
        return href;
    }

    public void setHref(URI href)
    {
        this.href = href;
    }

    public String getRel()
    {
        return rel;
    }

    public void setRel(String rel)
    {
        this.rel = rel;
    }
    
}
