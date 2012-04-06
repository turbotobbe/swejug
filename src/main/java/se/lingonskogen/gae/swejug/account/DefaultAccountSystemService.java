package se.lingonskogen.gae.swejug.account;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import se.lingonskogen.gae.swejug.account.model.Account;
import se.lingonskogen.gae.swejug.system.DefaultSystemService;
import se.lingonskogen.gae.swejug.system.model.Root;

public class DefaultAccountSystemService extends DefaultSystemService implements AccountSystemService
{
   @Override
   public boolean installAccount(Root root, Account account)
   {
      boolean modified = false;
      Key rootKey = KeyFactory.createKey(Root.KIND, root.makeKeyName());
      Key accountKey = KeyFactory.createKey(rootKey, Account.KIND, account.getUsername());
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      try
      {
         store.get(accountKey);
      }
      catch (EntityNotFoundException e)
      {
         Entity accountEntity = new Entity(accountKey);
         accountEntity.setProperty(Account.USERNAME, account.getUsername());
         accountEntity.setProperty(Account.PASSWORD, account.getPassword());
         accountEntity.setProperty(Account.ROLE, account.getRole().toString());
         accountEntity.setProperty(Account.REFRESH_TOKEN, account.getRefreshToken());
         store.put(accountEntity);
         modified = true;
      }
      return modified;
   }
}
