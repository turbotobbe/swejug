package se.lingonskogen.gae.swejug.domain.bean;

import java.util.ArrayList;
import java.util.List;

import se.lingonskogen.gae.swejug.domain.api.Event;
import se.lingonskogen.gae.swejug.domain.api.Group;

public class GroupBean extends ContentBean implements Group
{
    private String name;
    
    private final List<Event> events = new ArrayList<Event>();

    public GroupBean()
    {
        super(Group.class.getSimpleName());
    }
    
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public List<Event> getEvents()
    {
        return events;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
