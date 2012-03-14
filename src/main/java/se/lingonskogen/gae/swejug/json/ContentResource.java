package se.lingonskogen.gae.swejug.json;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import se.lingonskogen.gae.swejug.config.ConfigFactory;

import com.google.appengine.api.datastore.Entity;


@Path("content")
public class ContentResource
{
   private static final Logger LOG = Logger.getLogger(ContentResource.class.getName());
   
   private ContentStore getContentStore()
   {
      return new ContentStore();
   }

   @Context
   UriInfo uriInfo;

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public Response find()
   {
      LOG.log(Level.INFO, ConfigFactory.getConfig().getString("test", "lorem"));
      return handleFind();
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}")
   public Response find(@PathParam("urlkey1") String urlkey1)
   {
      return handleFind(urlkey1);
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}")
   public Response find(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2)
   {
      return handleFind(urlkey1, urlkey2);
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}")
   public Response find(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3)
   {
      return handleFind(urlkey1, urlkey2, urlkey3);
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}/{urlkey4}")
   public Response find(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3, @PathParam("urlkey4") String urlkey4)
   {
      return handleFind(urlkey1, urlkey2, urlkey3, urlkey4);
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}/{urlkey4}/{urlkey5}")
   public Response find(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3, @PathParam("urlkey4") String urlkey4, @PathParam("urlkey5") String urlkey5)
   {
      return handleFind(urlkey1, urlkey2, urlkey3, urlkey4, urlkey5);
   }

   private Response handleFind(String... urlkeys)
   {
      enter("Find", urlkeys);
      Entity content = null;
      try
      {
         ContentStore store = getContentStore();
         content = store.find(urlkeys);
      }
      catch (ContentNotFoundException e)
      {
         error("Find", e, urlkeys);
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.status(Status.OK).entity(content).build();
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   public Response create(Entity content)
   {
      return handleCreate(content);
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}")
   public Response create(Entity content, @PathParam("urlkey1") String urlkey1)
   {
      return handleCreate(content, urlkey1);
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}")
   public Response create(Entity content, @PathParam("urlkey1") String urlkey1,@PathParam("urlkey2") String urlkey2)
   {
      return handleCreate(content, urlkey1, urlkey2);
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}")
   public Response create(Entity content, @PathParam("urlkey1") String urlkey1,@PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3)
   {
      return handleCreate(content, urlkey1, urlkey2, urlkey3);
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}/{urlkey4}")
   public Response create(Entity content, @PathParam("urlkey1") String urlkey1,@PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3, @PathParam("urlkey4") String urlkey4)
   {
      return handleCreate(content, urlkey1, urlkey2, urlkey3, urlkey4);
   }

   private Response handleCreate(Entity content, String... urlkeys)
   {
      enter("Create", urlkeys);
      String urlkey = null;
      try
      {
         ContentStore store = getContentStore();
         urlkey = store.create(content, urlkeys);
      }
      catch (ContentNotUniqueException e)
      {
         error("Create", e, urlkeys);
         return Response.status(Status.BAD_REQUEST).build();
      }
      catch (UrlKeyGeneratorException e)
      {
         error("Create", e, urlkeys);
         return Response.status(Status.BAD_REQUEST).build();
      }
      catch (ContentNotFoundException e)
      {
         error("Create", e, urlkeys);
         return Response.status(Status.NOT_FOUND).build();
      }
      UriBuilder builder = uriInfo.getAbsolutePathBuilder();
      URI uri = builder.path(urlkey).build();
      return Response.created(uri).build();
   }
   
   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}")
   public Response update(Entity content, @PathParam("urlkey1") String urlkey1)
   {
      return handleUpdate(content, urlkey1);
   }

   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}")
   public Response update(Entity content, @PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2)
   {
      return handleUpdate(content, urlkey1, urlkey2);
   }

   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}")
   public Response update(Entity content, @PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3)
   {
      return handleUpdate(content, urlkey1, urlkey2, urlkey3);
   }

   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}/{urlkey4}")
   public Response update(Entity content, @PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3, @PathParam("urlkey4") String urlkey4)
   {
      return handleUpdate(content, urlkey1, urlkey2, urlkey3, urlkey4);
   }

   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}/{urlkey4}/{urlkey5}")
   public Response update(Entity content, @PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3, @PathParam("urlkey4") String urlkey4, @PathParam("urlkey5") String urlkey5)
   {
      return handleUpdate(content, urlkey1, urlkey2, urlkey3, urlkey4, urlkey5);
   }

   private Response handleUpdate(Entity content, String... urlkeys)
   {
      enter("Update", urlkeys);
      try
      {
         ContentStore store = getContentStore();
         store.update(content, urlkeys);
      }
      catch (ContentNotFoundException e)
      {
         error("Update", e, urlkeys);
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.status(Status.OK).build();
   }

   @DELETE
   @Path("{urlkey1}")
   public Response delete(@PathParam("urlkey1") String urlkey1)
   {
      return handleDelete(urlkey1);
   }

   @DELETE
   @Path("{urlkey1}/{urlkey2}")
   public Response delete(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2)
   {
      return handleDelete(urlkey1, urlkey2);
   }

   @DELETE
   @Path("{urlkey1}/{urlkey2}/{urlkey3}")
   public Response delete(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3)
   {
      return handleDelete(urlkey1, urlkey2, urlkey3);
   }

   @DELETE
   @Path("{urlkey1}/{urlkey2}/{urlkey3}/{urlkey4}")
   public Response delete(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3, @PathParam("urlkey4") String urlkey4)
   {
      return handleDelete(urlkey1, urlkey2, urlkey3, urlkey4);
   }

   @DELETE
   @Path("{urlkey1}/{urlkey2}/{urlkey3}/{urlkey4}/{urlkey5}")
   public Response delete(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3, @PathParam("urlkey4") String urlkey4, @PathParam("urlkey5") String urlkey5)
   {
      return handleDelete(urlkey1, urlkey2, urlkey3, urlkey4, urlkey5);
   }

   private Response handleDelete(String... urlkeys)
   {
      enter("Delete", urlkeys);
      try
      {
         ContentStore store = getContentStore();
         store.delete(urlkeys);
      }
      catch (ContentNotFoundException e)
      {
         error("Delete", e, urlkeys);
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.status(Status.OK).build();
   }
   
   private void enter(String method, String... urlkeys)
   {
      StringBuilder sb = new StringBuilder();
      for (String urlkey : urlkeys)
      {
         if (sb.length() > 0)
         {
            sb.append("/");
         }
         sb.append(urlkey);
      }
      LOG.log(Level.FINER, "Entering " + method + "! urlkeys: [" + sb.toString() + "]");
   }

   private void error(String method, Throwable e, String... urlkeys)
   {
      StringBuilder sb = new StringBuilder();
      for (String urlkey : urlkeys)
      {
         if (sb.length() > 0)
         {
            sb.append("/");
         }
         sb.append(urlkey);
      }
      LOG.log(Level.WARNING, "Error in " + method + "! urlkeys: [" + sb.toString() + "]", e);
   }
}
