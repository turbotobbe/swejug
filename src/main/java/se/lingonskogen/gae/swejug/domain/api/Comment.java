package se.lingonskogen.gae.swejug.domain.api;

public interface Comment extends Content
{
    String INDEX = "index";
    
    String CONTENT = "content";
    
    Long getIndex();
    
    String getContent();
}
