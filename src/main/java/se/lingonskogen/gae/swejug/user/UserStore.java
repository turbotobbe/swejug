package se.lingonskogen.gae.swejug.user;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class UserStore
{
   private static final Logger LOG = Logger.getLogger(UserStore.class.getName());
   
   public void install()
   {
      Key usersKey = getUsersKey();
      Entity entity = new Entity(usersKey);
      entity.setProperty(Users.PROP_TOTAL, 0);
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      store.put(entity);
   }

   public User findByKey(String urlkey) throws EntityNotFoundException
   {
      User user = null;
      
      Key usersKey = getUsersKey();
      Key userKey = KeyFactory.createKey(usersKey, User.KIND, urlkey);
      
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      try
      {
         Entity entity = store.get(userKey);
         user = toUser(entity);
      }
      catch (EntityNotFoundException e)
      {
         LOG.log(Level.INFO, "Unable to find user by key! key:" + urlkey);
         throw e;
      }
      return user;
   }

   public Users findAll(Integer limit, Integer offset) throws EntityNotFoundException
   {
      Users users = null;
      
      Key usersKey = getUsersKey();
      
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      try
      {
         Entity entity = store.get(usersKey);
         users = toUsers(entity);
      }
      catch (EntityNotFoundException e)
      {
         LOG.log(Level.INFO, "Unable to find all users! limit:" + limit + ", offset:" + offset);
         throw e;
      }

      PreparedQuery preparedQuery = store.prepare(new Query(User.KIND, usersKey));
      FetchOptions options = FetchOptions.Builder.withDefaults();
      if (limit != null)
      {
         options = options.limit(limit);
      }
      if (offset != null)
      {
         options = options.offset(offset);
      }
      int count = 0;
      for (Entity entity : preparedQuery.asIterable(options))
      {
         users.getUsers().add(toUser(entity));
         count++;
      }
      users.setCount(count);
      return users;
   }
   
   private Users toUsers(Entity entity)
   {
      Users users = new Users();
      users.setKind(Users.KIND);
      users.setTotal((Integer) entity.getProperty(Users.PROP_TOTAL));
      // users.setLinks(links)
      return users;
   }

   private User toUser(Entity entity)
   {
      User user = new User();
      user.setKind(User.KIND);
      user.setName((String) entity.getProperty(User.PROP_NAME));
      // user.setLinks(links)
      return user;
   }

   private Key getUsersKey()
   {
      Key usersKey = KeyFactory.createKey(Users.KIND, "default");
      return usersKey;
   }
}
