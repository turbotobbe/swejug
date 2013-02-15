package se.lingonskogen.gae.swejug.domain.bean;

import java.util.ArrayList;
import java.util.List;

import se.lingonskogen.gae.swejug.domain.api.Blog;
import se.lingonskogen.gae.swejug.domain.api.User;

public class UserBean extends ContentBean implements User
{
    private String firstName;
    
    private String lastName;

    private final List<Blog> blogs = new ArrayList<Blog>();

    public UserBean()
    {
        super(User.class.getSimpleName());
    }

    @Override
    public String getFirstName()
    {
        return firstName;
    }

    @Override
    public String getLastName()
    {
        return lastName;
    }

    @Override
    public List<Blog> getBlogs()
    {
        return blogs;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

}
