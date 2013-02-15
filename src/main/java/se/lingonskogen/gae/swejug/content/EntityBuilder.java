package se.lingonskogen.gae.swejug.content;

import se.lingonskogen.gae.swejug.content.err.EntityBuilderException;
import se.lingonskogen.gae.swejug.oldstuff.Content;

import com.google.appengine.api.datastore.Entity;

public interface EntityBuilder<T extends ContentBean>
{
   Entity build(T content) throws EntityBuilderException;
   
   T build(String baseUrl, Entity entity) throws EntityBuilderException;
}
