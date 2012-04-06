package se.lingonskogen.gae.swejug.account.html;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import se.lingonskogen.gae.swejug.account.AbstractAccountResource;
import se.lingonskogen.gae.swejug.account.err.AccountNotFoundException;
import se.lingonskogen.gae.swejug.account.model.Account;
import se.lingonskogen.gae.swejug.account.model.Credential;
import se.lingonskogen.gae.swejug.account.model.Login;
import se.lingonskogen.gae.swejug.account.model.Tokens;
import se.lingonskogen.gae.swejug.account.rest.AccountRestResource;
import se.lingonskogen.gae.swejug.system.model.Tupel;

@Path("/")
public class AccountHtmlResource extends AbstractAccountResource
{
   private static final String EMPTY = "";
   
   private static final String CLASS = AccountRestResource.class.getName();
   private static final Logger LOG = Logger.getLogger(CLASS);

   @GET
   @Path("login")
   @Produces(MediaType.TEXT_HTML)
   public Response login()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("<html>");
      sb.append("<head>");
      sb.append("<title>login</title>");
      sb.append("</head>");
      sb.append("<body>");
      sb.append("<form action='" + getPath(AccountHtmlResource.class, "login").toString() + "?redirect=" + getPath(AccountHtmlResource.class, "secret").toString() +"' method='POST'>");
      sb.append("Username: <input type='text' name='" + Login.USERNAME + "' /><br />");
      sb.append("Password: <input type='password' name='" + Login.PASSWORD + "' /><br />");
      sb.append("Remember me: <input type='checkbox' name='" + Login.REFRESH + "' value='" + Boolean.TRUE + "'/><br />");
      sb.append("<input type='submit' value='Login' />");
      sb.append("</form>");
      sb.append("</body>");
      sb.append("</html>");
      return Response.ok().entity(sb.toString()).build();
   }
   
   @POST
   @Path("login")
   @Consumes("application/x-www-form-urlencoded")
   public Response login(@QueryParam("redirect") @DefaultValue(EMPTY) String redirect, @FormParam(Credential.USERNAME) String username, @FormParam(Login.PASSWORD) String password, @FormParam(Login.REFRESH) Boolean refresh)
   {
      Login login = new Login();
      login.setUsername(username);
      login.setPassword(password);
      login.setRefresh(refresh == null ? Boolean.FALSE : refresh);
      LOG.log(Level.INFO, "LOGIN {0}", refresh);
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
   @Produces(MediaType.TEXT_HTML)
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
      StringBuilder sb = new StringBuilder();
      sb.append("<html>");
      sb.append("<head>");
      sb.append("<title>login</title>");
      sb.append("</head>");
      sb.append("<body>");
      sb.append("<h1>" + account.getUsername() + "</h1>");
      sb.append("<a href='" + getPath(AccountHtmlResource.class, "logout").toString() + "?redirect=" + getPath(AccountHtmlResource.class, "secret").toString() + "'>logout</a>");
      sb.append("</body>");
      sb.append("</html>");
      return Response.ok().cookie(cookie).entity(sb.toString()).build();
   }
}
