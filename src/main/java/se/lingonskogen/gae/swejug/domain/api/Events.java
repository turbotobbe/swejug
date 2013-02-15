package se.lingonskogen.gae.swejug.domain.api;

import java.util.List;

public interface Events
{
    String EVENTS = "events";
    
    List<Event> getEvents();
}
