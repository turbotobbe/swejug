package se.lingonskogen.gae.swejug.modules.root;

import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.content.ContentEntityBuilder;
import se.lingonskogen.gae.swejug.content.ContentStore;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;

import com.google.appengine.api.datastore.Entity;

public class RootEntityBuilder extends ContentEntityBuilder<Root>
{
   @Override
   public Root buildContent(ContentStore store, Entity entity, boolean resolve) throws ConfigException, ContentNotFoundException
   {
      Root root = new Root();
      root.setLabel((String) entity.getProperty(Root.LABEL));
      return super.buildContent(store, entity, root, resolve);
   }

   @Override
   public Entity buildEntity(Root root, Entity entity)
   {
      entity = super.buildEntity(root, entity);
      entity.setProperty(Root.LABEL, root.getLabel());
      return entity;
   }

}
