package se.lingonskogen.gae.swejug.domain.api;

public interface User extends Content, Blogs
{
    String FIRSTNAME = "firstname";
    
    String LASTNAME = "lastname";
    
    String getFirstName();
    
    String getLastName();
}
