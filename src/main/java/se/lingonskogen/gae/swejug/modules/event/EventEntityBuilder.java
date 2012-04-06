package se.lingonskogen.gae.swejug.modules.event;

import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.content.ContentEntityBuilder;
import se.lingonskogen.gae.swejug.content.ContentStore;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;

import com.google.appengine.api.datastore.Entity;

public class EventEntityBuilder extends ContentEntityBuilder<Event>
{
   @Override
   public Event buildContent(ContentStore store, Entity entity, boolean resolve) throws ConfigException, ContentNotFoundException
   {
      Event event = new Event();
      event.setTitle((String) entity.getProperty(Event.TITLE));
      event.setContent((String) entity.getProperty(Event.CONTENT));
      return super.buildContent(store, entity, event, resolve);
   }

   @Override
   public Entity buildEntity(Event event, Entity entity)
   {
      entity = super.buildEntity(event, entity);
      entity.setProperty(Event.TITLE, event.getTitle());
      entity.setProperty(Event.CONTENT, event.getContent());
      return entity;
   }

}
