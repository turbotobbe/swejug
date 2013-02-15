package se.lingonskogen.gae.swejug.domain.api;

import java.util.List;

public interface Comments
{
    String COMMENTS = "comments";
    
    List<Comment> getComments();
}
