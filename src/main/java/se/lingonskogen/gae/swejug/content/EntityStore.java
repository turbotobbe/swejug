package se.lingonskogen.gae.swejug.content;

import java.text.Normalizer;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import se.lingonskogen.gae.swejug.beans.Content;
import se.lingonskogen.gae.swejug.beans.Order;
import se.lingonskogen.gae.swejug.beans.PropertyBuilder;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;
import se.lingonskogen.gae.swejug.content.err.ContentNotUniqueException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

public class EntityStore
{
   private static final String CLASS = EntityStore.class.getName();
   private static final Logger LOG = Logger.getLogger(CLASS);
   
   private Key userKey;
   private Key rootKey;
   
   public EntityStore(String rootLabel)
   {
      rootKey = KeyFactory.createKey(Content.KIND, makeUrlKey(rootLabel));
   }
   
   public void setUserKey(Key userKey)
   {
      this.userKey = userKey;
   }

   public Entity getEntity(Key key) throws ContentNotFoundException
   {
      LOG.log(Level.INFO, "Getting entity {0}", key.toString());
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      Entity entity = null;
      try
      {
         entity = store.get(key);
      }
      catch (EntityNotFoundException e)
      {
         throw new ContentNotFoundException("Unable to get entity", e);
      }
      return entity;
   }

   public Entity getEntity(String... urlkeys) throws ContentNotFoundException
   {
      LOG.log(Level.INFO, "Getting path...");
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      Entity entity = null;
      Builder builder = new KeyFactory.Builder(rootKey);
      try
      {
         Key key = builder.getKey();
         entity = store.get(key);
         LOG.log(Level.INFO, "Found entry with key {0}", key.toString());
         for (int i = 0; i < urlkeys.length; i++)
         {
            builder.addChild(Content.KIND, urlkeys[i]);
            key = builder.getKey();
            LOG.log(Level.INFO, "Found entry with key {0}", key.toString());
            entity = store.get(key);
         }
      }
      catch (EntityNotFoundException e)
      {
         throw new ContentNotFoundException("Unable to get path", e);
      }
      LOG.log(Level.INFO, "Found entity {0}.", entity.getKey().toString());
      return entity;
   }

   public List<Entity> getEntityList(Key parentKey, String type, List<Order> order, Integer limit, Integer offset)
   {
      LOG.log(Level.INFO, "Getting children of type {0} under key {1}", new String[]{type, parentKey.toString()});
      
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      Query query = new Query(Content.KIND, parentKey);
      // TODO: add other filters
      query.addFilter(Content.META_TYPE, FilterOperator.EQUAL, type);
      for (Order sort : order)
      {
         query.addSort(sort.property, sort.direction);
      }
      query.addSort(Content.META_CREATED_DATE, SortDirection.DESCENDING); // newest first
      LOG.log(Level.INFO, "Query: " + query.toString());
      FetchOptions options = FetchOptions.Builder.withDefaults().limit(limit).offset(offset);
      List<Entity> list = store.prepare(query).asList(options);
      LOG.log(Level.INFO, "Found {0} children.", list.size());
      return list;
   }
   
   public Key create(Entity entity) throws ContentNotUniqueException, ContentNotFoundException
   {
      LOG.log(Level.INFO, "Create entity with key {0}", entity.getKey().toString());
      
      String type = (String) entity.getProperty(Content.META_TYPE);
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      try
      {
         store.get(entity.getKey());
         LOG.log(Level.WARNING, "Found duplicate entity with key {0}", entity.getKey().toString());
         throw new ContentNotUniqueException("Unable to create entity " + entity.getKey().toString());
      }
      catch (EntityNotFoundException e)
      {
         entity.setProperty(Content.META_CREATED_DATE, new Date());
         entity.setProperty(Content.META_CREATED_USER, userKey);
         store.put(entity);
         try
         {
            updateParent(store, entity.getParent(), type, +1L);
         }
         catch (EntityNotFoundException e1)
         {
            LOG.log(Level.WARNING, "Unable to update parent total count", e1);
            throw new ContentNotFoundException("Unable to update parent", e1);
         }
      }
      return entity.getKey();
   }

   public void update(Entity entity) throws ContentNotFoundException
   {
      LOG.log(Level.INFO, "Update entity with key {0}", entity.getKey().toString());
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      try
      {
         Entity ent = store.get(entity.getKey());
         mergeEntities(ent, entity);
         ent.setProperty(Content.META_UPDATED_DATE, new Date());
         ent.setProperty(Content.META_UPDATED_USER, userKey);
         store.put(ent);
      }
      catch (EntityNotFoundException e)
      {
         LOG.log(Level.WARNING, "Missing entity with key {0}", entity.getKey().toString());
         throw new ContentNotFoundException("Unable to update content " + entity.getKey().toString(), e);
      }
   }
   
   public void delete(Entity entity) throws ContentNotFoundException
   {
      LOG.log(Level.INFO, "Delete entity with key {0}", entity.getKey().toString());
      String type = (String) entity.getProperty(Content.META_TYPE);
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      try
      {
         store.get(entity.getKey());
         store.delete(entity.getKey());
         try
         {
            updateParent(store, entity.getParent(), type, -1L);
         }
         catch (EntityNotFoundException e1)
         {
            LOG.log(Level.WARNING, "Unable to update parent total count", e1);
            throw new ContentNotFoundException("Unable to update parent", e1);
         }
      }
      catch (EntityNotFoundException e)
      {
         LOG.log(Level.WARNING, "Missing entity with key {0}", entity.getKey().toString());
         throw new ContentNotFoundException("Unable to delete content " + entity.getKey().toString(), e);
      }
   }
   
   public String makeUrlKey(String label)
   {
      label = Normalizer.normalize(label, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
      label = label.replaceAll("^[^a-zA-Z0-9]+", "");
      label = label.replaceAll("[^a-zA-Z0-9]+$", "");
      label = label.replaceAll("[^a-zA-Z0-9]+", "-");
      return label.toLowerCase();
   }

   private void mergeEntities(Entity target, Entity source)
   {
      for (String key : source.getProperties().keySet())
      {
         target.setProperty(key, source.getProperty(key));
      }
   }

   private void updateParent(DatastoreService store, Key key, String type, Long value) throws EntityNotFoundException
   {
      PropertyBuilder pb = new PropertyBuilder();
      String totalPropertyName = pb.clear(Content.META_TOTAL).add(type).build();
      if (key != null)
      {
         Entity entity = store.get(key);
         Long total = (Long) entity.getProperty(totalPropertyName);
         if (total == null)
         {
            total = 0L;
         }
         total += value;
         entity.setProperty(totalPropertyName, total);
         store.put(entity);
         updateParent(store, key.getParent(), type, value);
      }
   }

}
