package se.lingonskogen.gae.swejug.content;

import java.util.Date;
import java.util.Map;

import se.lingonskogen.gae.swejug.beans.Content;
import se.lingonskogen.gae.swejug.beans.DateUser;
import se.lingonskogen.gae.swejug.beans.EntityBuilder;
import se.lingonskogen.gae.swejug.beans.Link;
import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;
import se.lingonskogen.gae.swejug.modules.user.User;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public abstract class ContentEntityBuilder<T extends Content> implements EntityBuilder<T>
{
   private UrlBuilder urlBuilder;

   public void setUrlBuilder(UrlBuilder urlBuilder)
   {
      this.urlBuilder = urlBuilder;
   }

   public T buildContent(ContentStore store, Entity entity, T content, boolean resolve) throws ConfigException, ContentNotFoundException
   {
      // key
      content.setKey(entity.getKey());
      
      // parent
      Key parentKey = entity.getParent();
      if (resolve && parentKey != null)
      {
         Content parentContent = store.getContent(parentKey);
         content.setParent(parentContent);
      }

      // meta type
      String type = (String) entity.getProperty(Content.META_TYPE);

      // meta totals
      Map<String, Object> properties = entity.getProperties();
      for (String key : properties.keySet())
      {
         if (key.startsWith(Content.META_TOTAL))
         {
            type = key.substring(Content.META_TOTAL.length()+1);
            content.getMeta().getTotal().put(type, (Long) properties.get(key));
         }
      }
      
      // meta created
      Date createDate = (Date) entity.getProperty(Content.META_CREATED_DATE);
      Key createUserKey = (Key) entity.getProperty(Content.META_CREATED_USER);
      if (createDate != null)
      {
         DateUser created = new DateUser();
         created.setDate(createDate);
         content.getMeta().setCreated(created);
         if (createUserKey != null)
         {
            User createUser = createUserKey.equals(entity.getKey()) ? (User) content : (User) store.getContent(createUserKey, false);
            content.getMeta().getCreated().setUser(createUser);
         }
      }
      
      // meta updated
      Date updateDate = (Date) entity.getProperty(Content.META_UPDATED_DATE);
      Key updateUserKey = (Key) entity.getProperty(Content.META_UPDATED_USER);
      if (updateDate != null)
      {
         DateUser updated = new DateUser();
         updated.setDate(updateDate);
         content.getMeta().setUpdated(updated);
         if (updateUserKey != null)
         {
            User createUser = updateUserKey.equals(entity.getKey()) ? (User) content : (User) store.getContent(updateUserKey, false);
            content.getMeta().getUpdated().setUser(createUser);
         }
      }

      // link
      Link link = new Link();
      link.setLabel(content.getLabel());
      link.setUrl(urlBuilder.build(entity.getKey()));
      link.setRel("Display");
      content.setLink(link);
      
      return content;
   }

   public Entity buildEntity(T content, Entity entity)
   {
      entity.setProperty(Content.META_TYPE, content.getMeta().getType());
      return entity;
   }
}
