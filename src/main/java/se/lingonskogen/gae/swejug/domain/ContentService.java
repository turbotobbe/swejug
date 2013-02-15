package se.lingonskogen.gae.swejug.domain;

import java.util.List;
import java.util.Map;

import se.lingonskogen.gae.swejug.domain.api.Content;

public interface ContentService
{
    String create(List<String> path, Map<String, Object> content);
    
    Content find(List<String> path);
}
