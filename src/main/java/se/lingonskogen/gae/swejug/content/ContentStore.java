package se.lingonskogen.gae.swejug.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.lingonskogen.gae.swejug.beans.Content;
import se.lingonskogen.gae.swejug.beans.EntityBuilder;
import se.lingonskogen.gae.swejug.beans.Order;
import se.lingonskogen.gae.swejug.config.Config;
import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.config.ConfigFactory;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;
import se.lingonskogen.gae.swejug.content.err.ContentNotUniqueException;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class ContentStore
{
   private final EntityStore entityStore;
   
   @SuppressWarnings("rawtypes")
   private Map<String, EntityBuilder> builders = new HashMap<String, EntityBuilder>();

   private UrlBuilder urlBuilder = new DefaultUrlBuilder();

   public ContentStore(String rootLabel)
   {
      entityStore = new EntityStore(rootLabel);
   }

   public void setUrlBuilder(UrlBuilder urlBuilder)
   {
      this.urlBuilder = urlBuilder;
      builders.clear();
   }
   
   public void setUserKey(Key userKey)
   {
      entityStore.setUserKey(userKey);
   }
   
   @SuppressWarnings("rawtypes")
   public Content getContent(Key key, boolean resolve) throws ConfigException, ContentNotFoundException
   {
      Entity entity = entityStore.getEntity(key);
      String type = (String) entity.getProperty(Content.META_TYPE);
      EntityBuilder builder = getEntityBuilder(type);
      Content content = builder.buildContent(this, entity, resolve);
      return content;
   }
   
   public Content getContent(Key key) throws ConfigException, ContentNotFoundException
   {
      return getContent(key, true);
   }

   @SuppressWarnings("rawtypes")
   public Content getContent(String... urlkeys) throws ConfigException, ContentNotFoundException
   {
      Entity entity = entityStore.getEntity(urlkeys);
      String type = (String) entity.getProperty(Content.META_TYPE);
      EntityBuilder builder = getEntityBuilder(type);
      Content content = builder.buildContent(this, entity, true);
      return content;
   }

   public List<? extends Content> getContentList(Key parentKey, String type, Integer limit, Integer offset) throws ConfigException, ContentNotFoundException
   {
      List<Order> order = Collections.emptyList();
      return getContentList(parentKey, type, order, limit, offset);
   }
   
   @SuppressWarnings("unchecked")
   public List<? extends Content> getContentList(Key parentKey, String type, List<Order> order, Integer limit, Integer offset) throws ConfigException, ContentNotFoundException
   {
      List<Content> list = new ArrayList<Content>();
      EntityBuilder<Content> builder = getEntityBuilder(type);
      List<Entity> children = entityStore.getEntityList(parentKey, type, order, limit, offset);
      for (Entity entity : children)
      {
         Content content = builder.buildContent(this, entity, true);
         list.add(content);
      }
      return list;
   }
   
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public Key create(Key parentKey, Content content) throws ConfigException, ContentNotUniqueException, ContentNotFoundException
   {
      EntityBuilder builder = getEntityBuilder(content.getMeta().getType());
      Entity entity = null;
      String urlKey = entityStore.makeUrlKey(content.getLabel());
      if (parentKey == null)
      {
         entity = new Entity(Content.KIND, urlKey);
      }
      else
      {
         entity = new Entity(Content.KIND, urlKey, parentKey);
      }
      entity = builder.buildEntity(content, entity);
      return entityStore.create(entity);
   }
   
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public void update(Key parentKey, Content content) throws ConfigException, ContentNotFoundException
   {
      EntityBuilder builder = getEntityBuilder(content.getMeta().getType());
      String urlKey = entityStore.makeUrlKey(content.getLabel());
      Entity entity = new Entity(Content.KIND, urlKey, parentKey);
      entity = builder.buildEntity(content, entity);
      entityStore.update(entity);
   }
   
   @SuppressWarnings({ "unchecked", "rawtypes" })
   public void delete(Key parentKey, Content content) throws ConfigException, ContentNotFoundException
   {
      EntityBuilder builder = getEntityBuilder(content.getMeta().getType());
      String urlKey = entityStore.makeUrlKey(content.getLabel());
      Entity entity = new Entity(Content.KIND, urlKey, parentKey);
      entity = builder.buildEntity(content, entity);
      entityStore.delete(entity);
   }

   public void setCreated(Key userKey, Key contentKey) throws ConfigException, ContentNotFoundException
   {
      Entity entity = new Entity(contentKey);
      entity.setProperty(Content.META_CREATED_USER, userKey);
      entityStore.update(entity);
   }
   
   @SuppressWarnings("rawtypes")
   private EntityBuilder getEntityBuilder(String type) throws ConfigException
   {
      if (!builders.containsKey(type))
      {
         Config config = ConfigFactory.getConfig();
         EntityBuilder builder = config.getInstance(Config.ENTITY_BUILDERS, type);
         builder.setUrlBuilder(urlBuilder);
         builders.put(type, builder);
      }
      return builders.get(type);
   }

}
