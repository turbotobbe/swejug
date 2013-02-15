package se.lingonskogen.gae.swejug.domain.bean;

import java.util.ArrayList;
import java.util.List;

import se.lingonskogen.gae.swejug.domain.api.Comment;
import se.lingonskogen.gae.swejug.domain.api.Event;

public class EventBean extends ContentBean implements Event
{
    private String title;
    
    private String summary;
    
    private String content;
    
    private final List<Comment> comments = new ArrayList<Comment>();

    public EventBean()
    {
        super(Event.class.getSimpleName());
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public String getSummary()
    {
        return summary;
    }

    @Override
    public String getContent()
    {
        return content;
    }

    @Override
    public List<Comment> getComments()
    {
        return comments;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

}
