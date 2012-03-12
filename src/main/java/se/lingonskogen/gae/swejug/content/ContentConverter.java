package se.lingonskogen.gae.swejug.content;

import com.google.appengine.api.datastore.Entity;

public interface ContentConverter<T>
{
    Entity toEntity(T content, Entity entity);
    
    T toContent(Entity entity);

}
