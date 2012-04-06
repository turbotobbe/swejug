package se.lingonskogen.gae.swejug.account.rest;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import se.lingonskogen.gae.swejug.account.AbstractAccountResource;
import se.lingonskogen.gae.swejug.account.err.AccountNotFoundException;
import se.lingonskogen.gae.swejug.account.model.Account;
import se.lingonskogen.gae.swejug.account.model.Credential;
import se.lingonskogen.gae.swejug.account.model.Login;
import se.lingonskogen.gae.swejug.account.model.Tokens;
import se.lingonskogen.gae.swejug.system.model.Tupel;

@Path("account")
public class AccountRestResource extends AbstractAccountResource
{
   private static final String CLASS = AccountRestResource.class.getName();
   private static final Logger LOG = Logger.getLogger(CLASS);

   @POST
   @Path("login")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response login(@QueryParam("redirect") @DefaultValue(EMPTY) String redirect, JSONObject jsonLoginForm)
   {
      Login login = new Login();
      try
      {
         if (jsonLoginForm.has(Login.USERNAME))
         {
            login.setUsername(jsonLoginForm.getString(Login.USERNAME));
         }
         if (jsonLoginForm.has(Credential.PASSWORD))
         {
            login.setPassword(jsonLoginForm.getString(Login.PASSWORD));
         }
         if (jsonLoginForm.has(Login.REFRESH))
         {
            login.setRefresh(jsonLoginForm.getBoolean(Login.REFRESH));
         }
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to create credential from json", e);
         return Response.status(Status.BAD_REQUEST).build();
      }
      return handleLogin(login, redirect);
   }

   @GET
   @Path("logout")
   public Response logout(@CookieParam(Tokens.ACCESS_TOKEN) String accessToken, @QueryParam("redirect") @DefaultValue(EMPTY) String redirect)
   {
      return handleLogout(accessToken, redirect);
   }

   @GET
   @Path("secret")
   @Produces(MediaType.APPLICATION_JSON)
   public Response secret(@CookieParam(Tokens.REFRESH_TOKEN) String refreshToken, @CookieParam(Tokens.ACCESS_TOKEN) String accessToken)
   {
      Account account = null;
      NewCookie cookie = null;
      try
      {
         Tupel<Account, NewCookie> tupel = authenticate(refreshToken, accessToken);
         account = tupel.getLeft();
         cookie = tupel.getRight();
      }
      catch (AccountNotFoundException e)
      {
         LOG.log(Level.INFO, "Unable to authenticate with refresh token {0} and access token {1}", new String[]{refreshToken, accessToken});
         return Response.status(Status.UNAUTHORIZED).build();
      }
      JSONObject jsonObject = new JSONObject();
      try
      {
         JSONObject jsonAccount = new JSONObject();
         jsonAccount.put(Account.USERNAME, account.getUsername());
         jsonAccount.put(Account.ROLE, account.getRole().toString());
         
         JSONObject jsonLogout = new JSONObject();
         jsonLogout.put("method", "GET");
         jsonLogout.put("url", getPath(AccountRestResource.class, "logout").toString());
         jsonLogout.put("label", "Logout");

         JSONArray jsonLinks = new JSONArray();
         jsonLinks.put(jsonLogout);
         
         jsonObject.put("account", jsonAccount);
         jsonObject.put("links", jsonLinks);
      }
      catch (JSONException e)
      {
         LOG.log(Level.INFO, "Unable to create json from account {0}", account.toString());
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      return Response.ok().cookie(cookie).entity(jsonObject).build();
   }

}
