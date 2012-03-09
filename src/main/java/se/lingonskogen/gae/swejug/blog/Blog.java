package se.lingonskogen.gae.swejug.blog;

import javax.xml.bind.annotation.XmlRootElement;

import se.lingonskogen.gae.swejug.rest.Bean;
import se.lingonskogen.gae.swejug.user.User;

@XmlRootElement
public class Blog extends Bean
{
    public static final String KIND = Blog.class.getSimpleName();
    
    private String name;

    private User user;
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

}
