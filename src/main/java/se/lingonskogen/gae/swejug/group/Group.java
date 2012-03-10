package se.lingonskogen.gae.swejug.group;

import javax.xml.bind.annotation.XmlRootElement;

import se.lingonskogen.gae.swejug.rest.Bean;

@XmlRootElement
public class Group extends Bean
{
    public static final String KIND = Group.class.getSimpleName();
    
    public static final String PROP_NAME = "name";
    
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
