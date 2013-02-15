package se.lingonskogen.gae.swejug.domain.api;

import java.util.List;

public interface ContentDao<T extends Content>
{
    T find(List<String> path);
    
    List<T> findAll(List<String> path);
}
