package se.lingonskogen.gae.swejug.domain.bean;

import java.util.ArrayList;
import java.util.List;

import se.lingonskogen.gae.swejug.domain.api.Group;
import se.lingonskogen.gae.swejug.domain.api.Root;
import se.lingonskogen.gae.swejug.domain.api.User;

public class RootBean extends ContentBean implements Root
{
    private String name;
    
    private final List<Group> groups = new ArrayList<Group>();

    public RootBean()
    {
        super(Root.class.getSimpleName());
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public List<Group> getGroups()
    {
        return groups;
    }

    @Override
    public List<User> getUsers()
    {
        return null;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
