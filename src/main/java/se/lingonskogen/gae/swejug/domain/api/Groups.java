package se.lingonskogen.gae.swejug.domain.api;

import java.util.List;

public interface Groups
{
    String GROUPS = "groups";
    
    List<Group> getGroups();
}
