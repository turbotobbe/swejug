package se.lingonskogen.gae.swejug.domain.bean;

import se.lingonskogen.gae.swejug.domain.api.Comment;

public class CommentBean extends ContentBean implements Comment
{
    private Long index;
    
    private String content;

    public CommentBean()
    {
        super(Comment.class.getSimpleName());
    }

    @Override
    public Long getIndex()
    {
        return index;
    }

    @Override
    public String getContent()
    {
        return content;
    }

    public void setIndex(Long index)
    {
        this.index = index;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

}
