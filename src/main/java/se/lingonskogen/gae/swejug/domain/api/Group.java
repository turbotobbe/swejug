package se.lingonskogen.gae.swejug.domain.api;

public interface Group extends Content, Events
{
    String NAME = "name";
    
    String getName();
}
