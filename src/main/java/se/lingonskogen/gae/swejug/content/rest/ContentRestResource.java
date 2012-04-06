package se.lingonskogen.gae.swejug.content.rest;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import se.lingonskogen.gae.swejug.AbstractResource;
import se.lingonskogen.gae.swejug.config.Config;
import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.config.ConfigFactory;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;
import se.lingonskogen.gae.swejug.content.err.ContentNotUniqueException;
import se.lingonskogen.gae.swejug.content.err.EntityBuilderException;
import se.lingonskogen.gae.swejug.oldstuff.Content;
import se.lingonskogen.gae.swejug.oldstuff.JsonBuilder;
import se.lingonskogen.gae.swejug.oldstuff.JsonBuilderException;
import se.lingonskogen.gae.swejug.oldstuff.Meta;
import se.lingonskogen.gae.swejug.oldstuff.OldContentStore2;
import se.lingonskogen.gae.swejug.oldstuff.OldContentStore2.Roots;

@Path("content")
public class ContentRestResource extends AbstractResource
{
   private static final Logger LOG = Logger.getLogger(ContentRestResource.class.getName());
   
   private static final String CONTENT = "content";
   
   private JsonBuilder<? extends Content> getJsonBuilder(String type) throws ConfigException
   {
      Config config = ConfigFactory.getConfig();
      return config.getInstance(Config.JSON_BUILDERS, type);
   }

   private JsonBuilder<? extends Content> getJsonBuilder(JSONObject json) throws ConfigException, JsonBuilderException
   {
      String type = null;
      try
      {
         JSONObject meta = json.getJSONObject(Content.PROP_META);
         type = meta.getString(Meta.PROP_TYPE);
      }
      catch (JSONException e)
      {
         throw new JsonBuilderException(e);
      }
      return getJsonBuilder(type);
   }

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public Response find()
   {
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

   @SuppressWarnings("unchecked")
   private Response handleFind(String... urlkeys)
   {
      enter("Find", urlkeys);
      JSONObject json = null;
      try
      {
         OldContentStore2 store = getContentStore(CONTENT);
         Content content = store.find(Roots.DEFAULT, urlkeys);
         @SuppressWarnings("rawtypes")
         JsonBuilder builder = getJsonBuilder(content.getMeta().getType());
         json = builder.build(content);
      }
      catch (ContentNotFoundException e)
      {
         error("Find", e, urlkeys);
         return Response.status(Status.NOT_FOUND).build();
      }
      catch (ConfigException e)
      {
         error("Find", e, urlkeys);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (JsonBuilderException e)
      {
         error("Find", e, urlkeys);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (EntityBuilderException e)
      {
         error("Find", e, urlkeys);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      return Response.status(Status.OK).entity(json).build();
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   public Response create(JSONObject json)
   {
      return handleCreate(json);
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}")
   public Response create(JSONObject json, @PathParam("urlkey1") String urlkey1)
   {
      return handleCreate(json, urlkey1);
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}")
   public Response create(JSONObject json, @PathParam("urlkey1") String urlkey1,@PathParam("urlkey2") String urlkey2)
   {
      return handleCreate(json, urlkey1, urlkey2);
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}")
   public Response create(JSONObject json, @PathParam("urlkey1") String urlkey1,@PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3)
   {
      return handleCreate(json, urlkey1, urlkey2, urlkey3);
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}/{urlkey4}")
   public Response create(JSONObject json, @PathParam("urlkey1") String urlkey1,@PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3, @PathParam("urlkey4") String urlkey4)
   {
      return handleCreate(json, urlkey1, urlkey2, urlkey3, urlkey4);
   }

   private Response handleCreate(JSONObject json, String... urlkeys)
   {
      enter("Create", urlkeys);
      String urlkey = null;
      try
      {
         JsonBuilder<? extends Content> builder = getJsonBuilder(json);
         Content content = builder.build(json);
         OldContentStore2 store = getContentStore(CONTENT);
         urlkey = store.create(Roots.DEFAULT, content, urlkeys);
      }
      catch (JsonBuilderException e)
      {
         error("Create", e, urlkeys);
         return Response.status(Status.BAD_REQUEST).build();
      }
      catch (ContentNotUniqueException e)
      {
         error("Create", e, urlkeys);
         return Response.status(Status.BAD_REQUEST).build();
      }
      catch (ContentNotFoundException e)
      {
         error("Create", e, urlkeys);
         return Response.status(Status.NOT_FOUND).build();
      }
      catch (ConfigException e)
      {
         error("Create", e, urlkeys);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (EntityBuilderException e)
      {
         error("Create", e, urlkeys);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      UriBuilder builder = getUriInfo().getAbsolutePathBuilder();
      URI uri = builder.path(urlkey).build();
      return Response.created(uri).build();
   }
   
   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}")
   public Response update(JSONObject json, @PathParam("urlkey1") String urlkey1)
   {
      return handleUpdate(json, urlkey1);
   }

   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}")
   public Response update(JSONObject json, @PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2)
   {
      return handleUpdate(json, urlkey1, urlkey2);
   }

   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}")
   public Response update(JSONObject json, @PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3)
   {
      return handleUpdate(json, urlkey1, urlkey2, urlkey3);
   }

   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}/{urlkey4}")
   public Response update(JSONObject json, @PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3, @PathParam("urlkey4") String urlkey4)
   {
      return handleUpdate(json, urlkey1, urlkey2, urlkey3, urlkey4);
   }

   @PUT
   @Consumes(MediaType.APPLICATION_JSON)
   @Path("{urlkey1}/{urlkey2}/{urlkey3}/{urlkey4}/{urlkey5}")
   public Response update(JSONObject json, @PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3, @PathParam("urlkey4") String urlkey4, @PathParam("urlkey5") String urlkey5)
   {
      return handleUpdate(json, urlkey1, urlkey2, urlkey3, urlkey4, urlkey5);
   }

   private Response handleUpdate(JSONObject json, String... urlkeys)
   {
      enter("Update", urlkeys);
      try
      {
         JsonBuilder<? extends Content> builder = getJsonBuilder(json);
         Content content = builder.build(json);
         OldContentStore2 store = getContentStore(CONTENT);
         store.update(Roots.DEFAULT, content, urlkeys);
      }
      catch (JsonBuilderException e)
      {
         error("Create", e, urlkeys);
         return Response.status(Status.BAD_REQUEST).build();
      }
      catch (ContentNotFoundException e)
      {
         error("Update", e, urlkeys);
         return Response.status(Status.NOT_FOUND).build();
      }
      catch (ConfigException e)
      {
         error("Update", e, urlkeys);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (EntityBuilderException e)
      {
         error("Update", e, urlkeys);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
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
         OldContentStore2 store = getContentStore(CONTENT);
         store.delete(Roots.DEFAULT, urlkeys);
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
