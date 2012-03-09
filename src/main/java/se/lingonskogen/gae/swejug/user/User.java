package se.lingonskogen.gae.swejug.user;

import javax.xml.bind.annotation.XmlRootElement;

import se.lingonskogen.gae.swejug.rest.Bean;

@XmlRootElement
public class User extends Bean
{
    public static final String KIND = User.class.getSimpleName();
    
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
