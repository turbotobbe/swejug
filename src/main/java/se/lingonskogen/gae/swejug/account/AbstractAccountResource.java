package se.lingonskogen.gae.swejug.account;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import se.lingonskogen.gae.swejug.account.err.AccountNotFoundException;
import se.lingonskogen.gae.swejug.account.model.Account;
import se.lingonskogen.gae.swejug.account.model.Login;
import se.lingonskogen.gae.swejug.account.model.Tokens;
import se.lingonskogen.gae.swejug.account.rest.AccountRestResource;
import se.lingonskogen.gae.swejug.system.model.Tupel;

public class AbstractAccountResource
{
   protected static final String EMPTY = "";

   private static final String CLASS = AbstractAccountResource.class.getName();
   private static final Logger LOG = Logger.getLogger(CLASS);
   
   @Context
   protected UriInfo uriInfo;
   
   protected Response handleLogin(Login login, String redirect)
   {
      URI location = null;
      if (redirect != null && !EMPTY.equals(redirect))
      {
         try
         {
            location = new URI(redirect);
         }
         catch (URISyntaxException e)
         {
            LOG.log(Level.INFO, "Unable to create redirect uri {0}", redirect);
            return Response.status(Status.BAD_REQUEST).build();
         }
      }
      else
      {
         location = uriInfo.getRequestUri();
      }

      LOG.log(Level.INFO, "Login with form {0}", login.toString());
      AccountService accountService = AccountServiceFactory.getAccountService();
      NewCookie[] cookies = null;
      try
      {
         Tokens tokens = accountService.login(login, login.getRefresh(), login.getAccess());
         LOG.log(Level.INFO, "Token {0}", tokens.toString());
         List<NewCookie> cookieList = new ArrayList<NewCookie>();
         if (tokens.getRefreshToken() != null)
         {
            cookieList.add(makeRefreshCookie(tokens.getRefreshToken()));
         }
         if (tokens.getAccessToken() != null)
         {
            cookieList.add(makeAccessCookie(tokens.getAccessToken()));
         }
         cookies = cookieList.toArray(new NewCookie[cookieList.size()]);
      }
      catch (AccountNotFoundException e)
      {
         LOG.log(Level.INFO, "Unable to login with login form {0}", login.toString());
         return Response.status(Status.UNAUTHORIZED).build();
      }
      if (location != null)
      {
         return Response.seeOther(location).cookie(cookies).build();
      }
      return Response.ok().cookie(cookies).build();
   }

   protected Response handleLogout(String accessToken, String redirect)
   {
      URI location = null;
      if (!EMPTY.equals(redirect))
      {
         try
         {
            location = new URI(redirect);
         }
         catch (URISyntaxException e)
         {
            LOG.log(Level.INFO, "Unable to create redirect uri {0}", redirect);
            return Response.status(Status.BAD_REQUEST).build();
         }
      }
      
      AccountService accountService = AccountServiceFactory.getAccountService();
      accountService.clear(accessToken);
      
      List<NewCookie> cookieList = new ArrayList<NewCookie>();
      cookieList.add(makeRefreshCookie(null));
      cookieList.add(makeAccessCookie(null));
      NewCookie[] cookies = cookieList.toArray(new NewCookie[cookieList.size()]);
      if (location != null)
      {
         return Response.seeOther(location).cookie(cookies).build();
      }
      return Response.ok().cookie(cookies).build();
   }
   
   protected Tupel<Account, NewCookie> authenticate(String refreshToken, String accessToken) throws AccountNotFoundException
   {
      AccountService accountService = AccountServiceFactory.getAccountService();
      Tokens tokens = new Tokens();
      tokens.setRefreshToken(refreshToken);
      tokens.setAccessToken(accessToken);
      Tupel<Account, String> tupel = accountService.authenticate(tokens);
      NewCookie accessCookie = null;
      if (tupel.getRight() != null)
      {
         accessCookie = makeAccessCookie(tupel.getRight());
      }
      return new Tupel<Account, NewCookie>(tupel.getLeft(), accessCookie);
   }

   private NewCookie makeRefreshCookie(String token)
   {
      int maxAge = (60*60*24)*100; // 100 days
      return makeCookie(Tokens.REFRESH_TOKEN, token, maxAge);
   }
   
   private NewCookie makeAccessCookie(String token)
   {
      return makeCookie(Tokens.ACCESS_TOKEN, token, NewCookie.DEFAULT_MAX_AGE);
   }

   private NewCookie makeCookie(String name, String token, int maxAge)
   {
      String domain = uriInfo.getBaseUri().getHost();
      if (token == null)
      {
         return new NewCookie(name, token, "/", domain, null, 0, false);
      }
      return new NewCookie(name, token, "/", domain, null, maxAge, false);
   }
   
   protected URI getPath(Class<?> resource, String path)
   {
      return uriInfo.getBaseUriBuilder().path(resource).path(path).build();
   }
}
