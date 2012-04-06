package se.lingonskogen.gae.swejug;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import se.lingonskogen.gae.swejug.modules.user.User;
import se.lingonskogen.gae.swejug.oldstuff.OldContentStore2;

public abstract class AbstractResource
{
   @Context
   private HttpServletRequest httpServletRequest;
   
   @Context
   private UriInfo uriInfo;
   
   @Context
   private ServletContext servletContext;
   
   protected OldContentStore2 getContentStore(String resourcePath)
   {
      String baseUri = getUriInfo().getBaseUri().toString();
      if (resourcePath != null)
      {
         baseUri += resourcePath;
      }
      User user = getLoggedInUser();
      return new OldContentStore2(baseUri, user);
   }

   protected User getLoggedInUser()
   {
      return (User) httpServletRequest.getAttribute("user");
   }
   
   protected ServletContext getServletContext()
   {
      return servletContext;
   }
   
   protected HttpServletRequest getHttpServletRequest()
   {
      return httpServletRequest;
   }

   protected UriInfo getUriInfo()
   {
      return uriInfo;
   }

}
