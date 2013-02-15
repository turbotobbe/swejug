package se.lingonskogen.gae.swejug.domain.api;

public interface Event extends Content, Comments
{
    String TITLE = "title";
    
    String SUMMARY = "summary";
    
    String CONTENT = "content";
    
    String getTitle();
    
    String getSummary();
    
    String getContent();
}
