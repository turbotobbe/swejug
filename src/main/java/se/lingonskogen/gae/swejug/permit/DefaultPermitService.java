package se.lingonskogen.gae.swejug.permit;

import java.util.Date;
import java.util.List;

import se.lingonskogen.gae.swejug.permit.model.Permit;
import se.lingonskogen.gae.swejug.permit.model.Role;
import se.lingonskogen.gae.swejug.permit.model.Subject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class DefaultPermitService implements PermitService
{

   @Override
   public void grantPermit(Permit permit)
   {
      String resource = permit.getResource();
      String operation = permit.getOperation();
      String role = permit.getRole().toString();
      String name = resource + "-" + operation + "-" + role;
      Entity entity = new Entity(Permit.KIND, name, getParentKey("default"));
      entity.setProperty(Permit.RESOURCE, resource);
      entity.setProperty(Permit.OPERATION, operation);
      entity.setProperty(Permit.ROLE, role);
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      store.put(entity);
   }

   @Override
   public void revokePermit(Permit permit)
   {
      String resource = permit.getResource();
      String operation = permit.getOperation();
      String roleName = permit.getRole().toString();
      String name = resource + "-" + operation + "-" + roleName;
      Key key = KeyFactory.createKey(getParentKey("default"), Permit.KIND, name);
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      store.delete(key);
   }

   @Override
   public String register(Subject subject)
   {
      String mail = subject.getMail();
      String pass = subject.getPass();
      String mailToken = Integer.toHexString(mail.hashCode());
      String timeToken = Long.toHexString(System.currentTimeMillis());
      String token = mailToken + "-" + timeToken;
      Entity entity = new Entity(Subject.KIND, mail, getParentKey("registered"));
      entity.setProperty(Subject.MAIL, mail);
      entity.setProperty(Subject.PASS, pass);
      entity.setProperty(Subject.ROLE, Role.REGISTERED.toString());
      entity.setProperty(Subject.DATE, new Date());
      entity.setProperty("token", token);
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      store.put(entity);
      return token;
   }

   @Override
   public void activate(String token)
   {
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      Query query = new Query(Subject.KIND, getParentKey("registered"));
      query.addFilter(Subject.TOKEN, FilterOperator.EQUAL, token);
      FetchOptions options = FetchOptions.Builder.withDefaults();
      List<Entity> result = store.prepare(query).asList(options);
      switch (result.size())
      {
         case 0:
            throw new RuntimeException("Activation token not found");
         case 1:
            Entity entity = result.get(0);
            Entity entity2 = new Entity(entity.getKey().getKind(), entity.getKey().getName(), getParentKey("default"));
            entity2.setPropertiesFrom(entity);
            store.put(entity2);
            break;
         default:
            throw new RuntimeException("Not unique activation token");
      }
   }

   @Override
   public String login(Subject subject)
   {
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      Query query = new Query(Subject.KIND, getParentKey("default"));
      query.addFilter(Subject.MAIL, FilterOperator.EQUAL, subject.getMail());
      query.addFilter(Subject.PASS, FilterOperator.EQUAL, subject.getPass());
      FetchOptions options = FetchOptions.Builder.withDefaults();
      List<Entity> result = store.prepare(query).asList(options);
      Entity entity = null;
      switch (result.size())
      {
         case 0:
            throw new RuntimeException("Invalid credentials");
         case 1:
            entity = result.get(0);
            break;
         default:
            throw new RuntimeException("Not unique credentials");
      }
      String mail = (String) entity.getProperty(Subject.MAIL);
      String mailToken = Integer.toHexString(mail.hashCode());
      String timeToken = Long.toHexString(System.currentTimeMillis());
      String token = mailToken + "-" + timeToken;
      MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
      cache.put(token, entity.getProperty(Subject.ROLE));
      return token;
   }

   @Override
   public boolean hasPermit(String token, Permit permit)
   {
      MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
      String role = (String) cache.get(token);
      String key = permit.getResource() + "-" + permit.getOperation() + "-" + role;
      Boolean permitted = (Boolean) cache.get(key);
      // TODO check db
      return permitted;
   }

   private Key getParentKey(String label)
   {
      return KeyFactory.createKey(Permit.KIND, label);
   }

}
