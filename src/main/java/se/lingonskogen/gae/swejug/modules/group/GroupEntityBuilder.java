package se.lingonskogen.gae.swejug.modules.group;

import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.content.ContentEntityBuilder;
import se.lingonskogen.gae.swejug.content.ContentStore;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;

import com.google.appengine.api.datastore.Entity;

public class GroupEntityBuilder extends ContentEntityBuilder<Group>
{
   @Override
   public Group buildContent(ContentStore store, Entity entity, boolean resolve) throws ConfigException, ContentNotFoundException
   {
      Group group = new Group();
      group.setTitle((String) entity.getProperty(Group.TITLE));
      return super.buildContent(store, entity, group, resolve);
   }
   
   @Override
   public Entity buildEntity(Group group, Entity entity)
   {
      entity = super.buildEntity(group, entity);
      entity.setProperty(Group.TITLE, group.getTitle());
      return entity;
   }
}
