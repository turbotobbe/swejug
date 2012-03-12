package se.lingonskogen.gae.swejug.json;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class ContentStore
{
   private static final Logger LOG = Logger.getLogger(ContentStore.class.getName());

   public static final String KIND = "Content";
   
   public static final String PROP_TYPE = "type";

   public static final String PROP_TOTAL = "total";

   public static final String ROOT_TYPE = "Root";
   
   public UrlKeyGenerator getUrlKeyGenerator()
   {
      return new NameUrlKeyGenerator();
   }

   public Entity find(String... urlkeys) throws ContentNotFoundException
   {
      enter("Find", urlkeys);
      
      // create key
      Key key = createKey(urlkeys);
      
      // find entity
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      Entity content = null;
      try
      {
         content = store.get(key);
      }
      catch (EntityNotFoundException e)
      {
         throw new ContentNotFoundException("Unable to find entity! key: " + key.toString(), e);
      }
      // TODO add children
      return content;
   }

   public String create(Entity content, String... urlkeys) throws ContentNotUniqueException, UrlKeyGeneratorException, ContentNotFoundException
   {
      enter("Create", urlkeys);
      
      // create keys
      Key parentKey = createKey(urlkeys);
      Key key = createKey(content, parentKey);
      
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      
      // assert parents
      try
      {
         while (parentKey != null)
         {
            store.get(parentKey);
            parentKey = parentKey.getParent();
         }
      }
      catch (EntityNotFoundException e)
      {
         throw new ContentNotFoundException("Unable to create entity! parentKey: " + parentKey.toString(), e);
      }

      // find entity
      try
      {
         store.get(key);
         throw new ContentNotUniqueException("Unable to create entity! key: " + key.toString());
      }
      catch (EntityNotFoundException e)
      {
         // no duplicates
      }
      
      // store entity
      Entity entity = new Entity(key);
      entity.setPropertiesFrom(content);
      store.put(entity);
      
      try
      {
         updateParent(store, entity, 1L);
      }
      catch (EntityNotFoundException e)
      {
         throw new ContentNotFoundException("Unable to create entity! key: " + key.toString(), e);
      }
      
      return key.getName();
   }

   public void update(Entity content, String... urlkeys) throws ContentNotFoundException
   {
      enter("Update", urlkeys);

      // create key
      Key key = createKey(urlkeys);

      // find entity
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      Entity c = null;
      try
      {
         c = store.get(key);
      }
      catch (EntityNotFoundException e)
      {
         throw new ContentNotFoundException("Unable to update entity! key: " + key.toString(), e);
      }
      
      // set properties and store entity
      c.setPropertiesFrom(content);
      store.put(c);
   }
   
   public void delete(String... urlkeys) throws ContentNotFoundException
   {
      enter("Delete", urlkeys);

      // create key
      Key key = createKey(urlkeys);

      // find entity
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      Entity entity = null;
      try
      {
         entity = store.get(key);
      }
      catch (EntityNotFoundException e)
      {
         throw new ContentNotFoundException("Unable to delete entity! key: " + key.toString(), e);
      }
      
      //query.addFilter(Entity.KEY_RESERVED_PROPERTY, Query.FilterOperator.GREATER_THAN, parentKey);

      // collect all children keys
      Query query = new Query(key);
      query.setKeysOnly();
      PreparedQuery prepare = store.prepare(query);
      List<Key> list = new ArrayList<Key>();
      for (Entity e : prepare.asIterable())
      {
         list.add(e.getKey());
      }
      
      // delete all children
      store.delete(list);
      
      // TODO udate parent
      try
      {
         updateParent(store, entity, -1L);
      }
      catch (EntityNotFoundException e)
      {
         throw new ContentNotFoundException("Unable to delete entity! key: " + key.toString(), e);
      }
   }

   private void updateParent(DatastoreService store, Entity entity, Long diff) throws EntityNotFoundException
   {
      Key parentkey = entity.getParent();
      Entity parentEntity = store.get(parentkey);
      String propertyName = PROP_TOTAL + entity.getProperty(PROP_TYPE);
      Long total = 0L;
      if (parentEntity.hasProperty(propertyName))
      {
         total = (Long) parentEntity.getProperty(propertyName);
      }
      total += diff;
      parentEntity.setProperty(propertyName, total);
      store.put(parentEntity);
   }
   
   private String createUrlKey(Entity content) throws UrlKeyGeneratorException
   {
      UrlKeyGenerator generator = getUrlKeyGenerator();
      return generator.generate(content);
   }

   private Key createKey(Entity content, Key parentKey) throws UrlKeyGeneratorException
   {
      String urlkey = createUrlKey(content);
      Key key = KeyFactory.createKey(parentKey, KIND, urlkey);
      return key;
   }

   private Key createKey(String... urlkeys)
   {
      Builder builder = new KeyFactory.Builder(getRootKey());
      for (String key : urlkeys)
      {
         builder = builder.addChild(KIND, key);
      }
      Key key = builder.getKey();
      return key;
   }
   
   private Key getRootKey()
   {
      return KeyFactory.createKey(KIND, ROOT_TYPE);
   }

   private void enter(String method, String... urlkeys)
   {
      StringBuilder sb = new StringBuilder();
      for (String urlkey : urlkeys)
      {
         if (sb.length() > 0)
         {
            sb.append("/");
         }
         sb.append(urlkey);
      }
      LOG.log(Level.FINER, "Entering " + method + "! urlkeys: [" + sb.toString() + "]");
   }

}
