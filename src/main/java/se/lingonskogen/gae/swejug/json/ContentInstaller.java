package se.lingonskogen.gae.swejug.json;

import java.util.logging.Logger;

import se.lingonskogen.gae.swejug.Installer;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ContentInstaller implements Installer
{
   private static final Logger LOG = Logger.getLogger(ContentInstaller.class.getName());
   
   @Override
   public void install()
   {
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      Key key = KeyFactory.createKey(ContentStore.KIND, ContentStore.ROOT_TYPE);
      try
      {
         store.get(key);
      }
      catch (EntityNotFoundException e)
      {
         Entity entity = new Entity(key);
         entity.setProperty(ContentStore.PROP_TYPE, ContentStore.ROOT_TYPE);
         LOG.info("Installing content. " + key.toString());
         store.put(entity);
      }
   }
}
