package se.lingonskogen.gae.swejug.account;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import se.lingonskogen.gae.swejug.account.err.AccountNotFoundException;
import se.lingonskogen.gae.swejug.account.err.UnexpectedJsonException;
import se.lingonskogen.gae.swejug.account.err.UnexpectedStoreException;
import se.lingonskogen.gae.swejug.account.model.Account;
import se.lingonskogen.gae.swejug.account.model.Credential;
import se.lingonskogen.gae.swejug.account.model.Role;
import se.lingonskogen.gae.swejug.account.model.Tokens;
import se.lingonskogen.gae.swejug.system.model.Root;
import se.lingonskogen.gae.swejug.system.model.Tupel;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class DefaultAccountService implements AccountService
{
   //accessToken.setValue(String.format("%x-%x", account.makeKeyName().hashCode(), System.currentTimeMillis()));
   
   private static final String CLASS = DefaultAccountService.class.getName();
   private static final Logger LOG = Logger.getLogger(CLASS);
   
   @Override
   public Tokens login(Credential credential, Boolean refresh, Boolean access) throws AccountNotFoundException
   {
      // validate
      if (credential.getUsername() == null || credential.getUsername().trim().isEmpty())
      {
         LOG.log(Level.INFO, "Missing username in {0}", credential.toString());
         throw new AccountNotFoundException();
      }
      if (credential.getPassword() == null || credential.getPassword().trim().isEmpty())
      {
         LOG.log(Level.INFO, "Missing password in {0}", credential.toString());
         throw new AccountNotFoundException();
      }
      DatastoreService store = DatastoreServiceFactory.getDatastoreService();
      
      // create root and root key
      Root root = AccountRoots.ACTIVATED.getRoot();
      Key rootKey = KeyFactory.createKey(Root.KIND, root.makeKeyName());

      // find account
      Query query = new Query(Account.KIND, rootKey);
      query.addFilter(Account.USERNAME, FilterOperator.EQUAL, credential.getUsername());
      query.addFilter(Account.PASSWORD, FilterOperator.EQUAL, credential.getPassword());
      LOG.log(Level.FINE, "Query: " + query.toString());
      PreparedQuery preparedQuery = store.prepare(query);
      List<Entity> list = preparedQuery.asList(FetchOptions.Builder.withDefaults());
      Tokens tokens = new Tokens();
      switch (list.size())
      {
         case 0:
            throw new AccountNotFoundException();
         case 1:
            Entity accountEntity = list.get(0);
            if (refresh)
            {
               tokens.setRefreshToken((String) accountEntity.getProperty(Tokens.REFRESH_TOKEN));
            }
            if (access)
            {
               JSONObject jsonAccount = new JSONObject();
               try
               {
                  jsonAccount.put(Account.USERNAME, accountEntity.getProperty(Account.USERNAME));
                  jsonAccount.put(Account.PASSWORD, accountEntity.getProperty(Account.PASSWORD));
                  jsonAccount.put(Account.ROLE, accountEntity.getProperty(Account.ROLE));
                  jsonAccount.put(Tokens.REFRESH_TOKEN, accountEntity.getProperty(Tokens.REFRESH_TOKEN));
               }
               catch (JSONException e)
               {
                  LOG.log(Level.WARNING, "Unable to create json account from entity", e);
                  throw new UnexpectedJsonException("Unable to create json account from entity", e);
               }
               MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
               String accessToken = "11." + accountEntity.getProperty(Account.USERNAME);
               cache.put(AccountService.ACCESS_TOKEN + "-" + accessToken, jsonAccount.toString());
               tokens.setAccessToken(accessToken);
            }
            break;
         default:
            throw new UnexpectedStoreException("Found duplicate accounts!");
      }
      return tokens;
   }
   
   @Override
   public boolean clear(String accessToken)
   {
      boolean deleted = false;
      if (accessToken != null && accessToken.length() > 0)
      {
         MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
         deleted = cache.delete(AccountService.ACCESS_TOKEN + "-" + accessToken);
      }
      return deleted;
   }

   @Override
   public Tupel<Account, String> authenticate(Tokens tokens) throws AccountNotFoundException
   {
      MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
      Tupel<Account, String> tupel = new Tupel<Account, String>();
      
      // get account by access token
      String jsonAccountValue = null;
      if (tokens.getAccessToken() != null && tokens.getAccessToken().length() > 0)
      {
         jsonAccountValue = (String) cache.get(AccountService.ACCESS_TOKEN + "-" + tokens.getAccessToken());
      }
      // get account by refresh token
      if (jsonAccountValue == null && tokens.getRefreshToken() != null && tokens.getRefreshToken().length() > 0)
      {
         DatastoreService store = DatastoreServiceFactory.getDatastoreService();
         
         // create root and root key
         Root root = AccountRoots.ACTIVATED.getRoot();
         Key rootKey = KeyFactory.createKey(Root.KIND, root.makeKeyName());

         // find account
         Query query = new Query(Account.KIND, rootKey);
         //query.setKeysOnly();
         query.addFilter(Account.REFRESH_TOKEN, FilterOperator.EQUAL, tokens.getRefreshToken());
         LOG.log(Level.FINE, "Query: " + query.toString());
         PreparedQuery preparedQuery = store.prepare(query);
         List<Entity> list = preparedQuery.asList(FetchOptions.Builder.withDefaults());
         String accessToken = null;
         switch (list.size())
         {
            case 0:
               throw new AccountNotFoundException();
            case 1:
               Entity accountEntity = list.get(0);
               JSONObject jsonAccount = new JSONObject();
               try
               {
                  jsonAccount.put(Account.USERNAME, accountEntity.getProperty(Account.USERNAME));
                  jsonAccount.put(Account.PASSWORD, accountEntity.getProperty(Account.PASSWORD));
                  jsonAccount.put(Account.ROLE, accountEntity.getProperty(Account.ROLE));
                  jsonAccount.put(Tokens.REFRESH_TOKEN, accountEntity.getProperty(Tokens.REFRESH_TOKEN));
               }
               catch (JSONException e)
               {
                  LOG.log(Level.WARNING, "Unable to create json account from entity", e);
                  throw new UnexpectedJsonException("Unable to create json account from entity", e);
               }
               cache = MemcacheServiceFactory.getMemcacheService();
               accessToken = "11." + accountEntity.getProperty(Account.USERNAME);
               cache.put(AccountService.ACCESS_TOKEN + "-" + accessToken, jsonAccount.toString());
               tupel.setRight(accessToken);
               break;
            default:
               throw new UnexpectedStoreException("Found duplicate accounts!");
         }
         jsonAccountValue = (String) cache.get(AccountService.ACCESS_TOKEN + "-" + accessToken);
      }
      if (jsonAccountValue == null)
      {
         throw new AccountNotFoundException();
      }
      try
      {
         Account account = new Account();
         JSONObject jsonAccount = new JSONObject(jsonAccountValue);
         if (jsonAccount.has(Account.USERNAME))
         {
            account.setUsername(jsonAccount.getString(Account.USERNAME));
         }
         if (jsonAccount.has(Account.PASSWORD))
         {
            account.setPassword(jsonAccount.getString(Account.PASSWORD));
         }
         if (jsonAccount.has(Account.ROLE))
         {
            account.setRole(Role.valueOf(jsonAccount.getString(Account.ROLE)));
         }
         if (jsonAccount.has(Tokens.REFRESH_TOKEN))
         {
            account.setRefreshToken(jsonAccount.getString(Tokens.REFRESH_TOKEN));
         }
         tupel.setLeft(account);
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to create account from json", e);
         throw new UnexpectedJsonException("Unable to create account from json", e);
      }
      return tupel;
   }

}
