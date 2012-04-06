package se.lingonskogen.gae.swejug.system;

import se.lingonskogen.gae.swejug.system.model.Root;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class DefaultSystemService implements SystemService
{

   @Override
   public boolean installRoot(Root root)
   {
      boolean modified = false;
      
      String keyName = root.makeKeyName();
      Key rootKey = KeyFactory.createKey(Root.KIND, keyName);
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      try
      {
         store.get(rootKey);
      }
      catch (EntityNotFoundException e)
      {
         Entity RootEntity = new Entity(rootKey);
         RootEntity.setProperty(Root.MODULE, root.getModule());
         RootEntity.setProperty(Root.LABEL, root.getLabel());
         store.put(RootEntity);
         modified = true;
      }
      return modified;
   }
}
