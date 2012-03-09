package se.lingonskogen.gae.swejug.blog;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import se.lingonskogen.gae.swejug.rest.Beans;
import se.lingonskogen.gae.swejug.user.User;

@XmlRootElement
public class Blogs extends Beans
{
    public static final String KIND = Blogs.class.getSimpleName();

    private User user;

    private List<Blog> blogs = new ArrayList<Blog>();

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public List<Blog> getBlogs()
    {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs)
    {
        this.blogs = blogs;
    }
    
}
