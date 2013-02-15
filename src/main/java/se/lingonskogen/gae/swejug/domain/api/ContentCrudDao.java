package se.lingonskogen.gae.swejug.domain.api;

import java.util.List;


public interface ContentCrudDao<T extends Content> extends ContentDao<T>
{
    String create(List<String> path, T content);
    
    void update(List<String> path, T content);
    
    void delete(List<String> path);
}
