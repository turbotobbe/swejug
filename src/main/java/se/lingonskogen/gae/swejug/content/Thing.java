package se.lingonskogen.gae.swejug.content;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Thing
{
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
}
