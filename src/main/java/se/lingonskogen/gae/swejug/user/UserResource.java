package se.lingonskogen.gae.swejug.user;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.google.appengine.api.datastore.EntityNotFoundException;

import se.lingonskogen.gae.swejug.rest.Link;

@Path("/rest/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource
{
   @Context
   UriInfo uriInfo;

   @GET
   public Response getAll(@QueryParam("limit") @DefaultValue("10") Integer limit,
         @QueryParam("offset") @DefaultValue("0") Integer offset)
   {
      ResponseBuilder builder = Response.status(Status.OK);
      if (offset < 0)
      {
         return builder.status(Status.BAD_REQUEST).build();
      }
      else if (limit <= 0)
      {
         return builder.status(Status.BAD_REQUEST).build();
      }

      UserStore userStore = new UserStore();
      Users users;
      try
      {
         users = userStore.findAll(limit, offset);
      }
      catch (EntityNotFoundException e)
      {
         return builder.status(Status.NOT_FOUND).build();
      }

      users.setKind(Users.KIND);
      for (User user : users.getUsers())
      {
         String urlkey = toUrlkey(user);
         Link link = new Link();
         link.setType(MediaType.APPLICATION_JSON);
         link.setRel("self");
         link.setHref(uriInfo.getAbsolutePathBuilder().path(urlkey).build());
         user.getLinks().add(link);
      }

      if (offset > 0)
      {
         Link link = new Link();
         link.setType(MediaType.APPLICATION_JSON);
         link.setRel("prev");
         Integer o = Math.max(0, offset-limit);
         link.setHref(uriInfo.getAbsolutePathBuilder().queryParam("limit", limit).queryParam("offset", o).build());
         users.getLinks().add(link);
      }
      if (offset + limit < users.getTotal())
      {
         Link link = new Link();
         link.setType(MediaType.APPLICATION_JSON);
         link.setRel("next");
         Integer o = Math.min(users.getTotal() - 1, offset+limit);
         link.setHref(uriInfo.getAbsolutePathBuilder().queryParam("limit", limit).queryParam("offset", o).build());
         users.getLinks().add(link);
      }
      return builder.entity(users).build();
   }

   @GET
   @Path("{urlkey}")
   public Response getByKey(@PathParam("urlkey") String urlkey)
   {
      ResponseBuilder builder = Response.status(Status.NOT_FOUND);
      
      UserStore userStore = new UserStore();
      User user;
      try
      {
         user = userStore.findByKey(urlkey);
      }
      catch (EntityNotFoundException e)
      {
         return builder.build();
      }
      
      Link link = new Link();
      link.setType(MediaType.APPLICATION_JSON);
      link.setRel("self");
      link.setHref(uriInfo.getAbsolutePathBuilder().path(urlkey).build());
      user.getLinks().add(link);

      return builder.status(Status.OK).entity(user).build();
   }

   private String toUrlkey(User user)
   {
      return user.getName().toLowerCase().replaceAll(" ", "-");
   }

   // URI uri = uriInfo.getAbsolutePathBuilder().path(urlkey).build();
}
