package se.lingonskogen.gae.swejug.beans;

import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.content.ContentStore;
import se.lingonskogen.gae.swejug.content.UrlBuilder;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;

import com.google.appengine.api.datastore.Entity;

public interface EntityBuilder<T extends Content>
{
   T buildContent(ContentStore store, Entity entity, boolean resolve) throws ConfigException, ContentNotFoundException;
   
   Entity buildEntity(T content, Entity entity);

   void setUrlBuilder(UrlBuilder urlBuilder);
}
