package se.lingonskogen.gae.swejug.domain.api;

public interface Root extends Content, Groups, Users
{
    String NAME = "name";
    
    String getName();
}
