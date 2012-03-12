package se.lingonskogen.gae.swejug.content;

import java.util.Iterator;
import java.util.Map;
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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import se.lingonskogen.gae.swejug.json.ContentNotFoundException;
import se.lingonskogen.gae.swejug.json.ContentNotUniqueException;

import com.google.appengine.api.datastore.Entity;

//@Path("content")
public class ContentResource
{
    private static final Logger LOG = Logger.getLogger(ContentResource.class.getName());
    
    @Context
    UriInfo uriInfo;

    /*
     * GET 
     */
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find()
    {
        return handleFind();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{uk1}")
    public Response find(@PathParam("uk1") String uk1)
    {
        return handleFind(uk1);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{uk1}/{uk2}")
    public Response find(@PathParam("uk1") String uk1, @PathParam("uk2") String uk2)
    {
        return handleFind(uk1, uk2);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{uk1}/{uk2}/{uk3}")
    public Response find(@PathParam("uk1") String uk1, @PathParam("uk2") String uk2, @PathParam("uk3") String uk3)
    {
        return handleFind(uk1, uk2, uk3);
    }

    /*
     * POST
     */
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(JSONObject content)
    {
        return handleCreate(content);
    }
 
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{uk1}")
    public Response create(@PathParam("uk1") String uk1, JSONObject content)
    {
        return handleCreate(content, uk1);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{uk1}/{uk2}")
    public Response create(@PathParam("uk1") String uk1, @PathParam("uk1") String uk2, JSONObject content)
    {
        return handleCreate(content, uk1, uk2);
    }

    /*
     * PUT
     */
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{uk1}")
    public Response update(@PathParam("uk1") String uk1, JSONObject content)
    {
        return handleUpdate(content, uk1);
    }
 
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{uk1}/{uk2}")
    public Response update(@PathParam("uk1") String uk1, @PathParam("uk2") String uk2, JSONObject content)
    {
        return handleUpdate(content, uk1, uk2);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{uk1}/{uk2}/{uk3}")
    public Response update(@PathParam("uk1") String uk1, @PathParam("uk2") String uk2, @PathParam("uk3") String uk3, JSONObject content)
    {
        return handleUpdate(content, uk1, uk2, uk3);
    }

    /*
     * DELETE
     */
    
    @DELETE
    @Path("{uk1}")
    public Response delete(@PathParam("uk1") String uk1)
    {
        return handleDelete(uk1);
    }
 
    @DELETE
    @Path("{uk1}/{uk2}")
    public Response delete(@PathParam("uk1") String uk1, @PathParam("uk2") String uk2)
    {
        return handleDelete(uk1, uk2);
    }

    @DELETE
    @Path("{uk1}/{uk2}/{uk3}")
    public Response delete(@PathParam("uk1") String uk1, @PathParam("uk2") String uk2, @PathParam("uk3") String uk3)
    {
        return handleDelete(uk1, uk2, uk3);
    }

    /*
     * HANDLERS
     */
    
    private Response handleFind(String... urlkeys)
    {
        LOG.log(Level.INFO, "Find: ", urlkeys.toString());
        JSONObject content = null;
        try
        {
            ContentStore<JSONObject> store = getContentStore();
            content = store.find(urlkeys);
        }
        catch (ParentContentNotFoundException e)
        {
            LOG.log(Level.INFO, "Unable to find!", e);
            return Response.status(Status.NOT_FOUND).build();
        }
        catch (ContentNotFoundException e)
        {
            LOG.log(Level.INFO, "Unable to find!", e);
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(content).build();
    }

    private Response handleCreate(JSONObject content, String... urlkeys)
    {
        LOG.log(Level.INFO, "Create: ", urlkeys.toString() + " " + content.toString());
        String urlkey = null;
        try
        {
            ContentStore<JSONObject> store = getContentStore();
            urlkey = store.create(content, urlkeys);
        }
        catch (ContentNotUniqueException e)
        {
            LOG.log(Level.INFO, "Unable to create!", e);
            return Response.status(Status.NOT_ACCEPTABLE).build();
        }
        catch (ParentContentNotFoundException e)
        {
            LOG.log(Level.INFO, "Unable to create!", e);
            return Response.status(Status.NOT_FOUND).build();
        }
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        for (int i = 0; i < urlkeys.length; i++)
        {
            builder = builder.fragment(urlkeys[i]);
        }
        return Response.created(builder.build(urlkey)).build();
    }

    private Response handleUpdate(JSONObject content, String... urlkeys)
    {
        LOG.log(Level.INFO, "Update: ", urlkeys.toString() + " " + content.toString());
        try
        {
            ContentStore<JSONObject> store = getContentStore();
            store.update(content, urlkeys);
        }
        catch (ParentContentNotFoundException e)
        {
            LOG.log(Level.INFO, "Unable to update!", e);
            return Response.status(Status.NOT_FOUND).build();
        }
        catch (ContentNotFoundException e)
        {
            LOG.log(Level.INFO, "Unable to update!", e);
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).build();
    }

    private Response handleDelete(String... urlkeys)
    {
        LOG.log(Level.INFO, "Delete: ", urlkeys.toString());
        try
        {
            ContentStore<JSONObject> store = getContentStore();
            store.delete(urlkeys);
        }
        catch (ParentContentNotFoundException e)
        {
            LOG.log(Level.INFO, "Unable to delete!", e);
            return Response.status(Status.NOT_FOUND).build();
        }
        catch (ContentNotFoundException e)
        {
            LOG.log(Level.INFO, "Unable to delete!", e);
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).build();
    }

    private ContentStore<JSONObject> getContentStore()
    {
        ContentStore<JSONObject> store = new ContentStore<JSONObject>();
        store.setContentConverter(new ContentConverter<JSONObject>()
        {
            
            @Override
            public Entity toEntity(JSONObject content, Entity entity)
            {
                @SuppressWarnings("unchecked")
                Iterator<String> keys = content.keys();
                while (keys.hasNext())
                {
                    String key = keys.next();
                    try
                    {
                        entity.setProperty(key, content.get(key));
                    }
                    catch (JSONException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return entity;
            }
            
            @Override
            public JSONObject toContent(Entity entity)
            {
                JSONObject content = new JSONObject();
                Map<String, Object> properties = entity.getProperties();
                for (String key : properties.keySet())
                {
                    try
                    {
                        content.put(key, properties.get(key));
                    }
                    catch (JSONException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                return content;
            }
        });
        store.setContentUrlKeyCreator(new ContentUrlKeyCreator<JSONObject>()
        {
            @Override
            public String createUrlKey(JSONObject content)
            {
                String base = "noname";
                try
                {
                    base = content.getString("name");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                return base.trim().toLowerCase().replaceAll("[^a-zA-Z0-9]", "-");
            }
        });
        return store;
    }
}
