package se.lingonskogen.gae.swejug.domain.bean;

import se.lingonskogen.gae.swejug.domain.api.Content;

public class ContentBean implements Content
{
    private final String type;
    
    private String id;

    public ContentBean(String type)
    {
        this.type = type;
    }
    
    @Override
    public String getType()
    {
        return type;
    }

    @Override
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

}
