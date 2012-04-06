package se.lingonskogen.gae.swejug.modules.user;

import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.content.ContentEntityBuilder;
import se.lingonskogen.gae.swejug.content.ContentStore;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;

import com.google.appengine.api.datastore.Entity;

public class UserEntityBuilder extends ContentEntityBuilder<User>
{
   @Override
   public User buildContent(ContentStore store, Entity entity, boolean resolve) throws ConfigException, ContentNotFoundException
   {
      User user = new User();
      user.setFirstname((String) entity.getProperty(User.FIRSTNAME));
      user.setLastname((String) entity.getProperty(User.LASTNAME));
      return super.buildContent(store, entity, user, resolve);
   }

   @Override
   public Entity buildEntity(User user, Entity entity)
   {
      entity = super.buildEntity(user, entity);
      entity.setProperty(User.FIRSTNAME, user.getFirstname());
      entity.setProperty(User.LASTNAME, user.getLastname());
      return entity;
   }

}
