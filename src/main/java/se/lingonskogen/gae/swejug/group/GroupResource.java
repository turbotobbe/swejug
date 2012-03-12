package se.lingonskogen.gae.swejug.group;

import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.api.datastore.Key;

import se.lingonskogen.gae.swejug.err.KeyFoundException;
import se.lingonskogen.gae.swejug.err.KeyNotFoundException;
import se.lingonskogen.gae.swejug.err.ParentKeyNotFoundException;

@Path("/rest/groups")
public class GroupResource
{
    private static final Logger LOG = Logger.getLogger(GroupResource.class.getName());
    
    @Context
    UriInfo uriInfo;

    public UriInfo getUriInfo()
    {
        return uriInfo;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Group group)
    {
        GroupStore store = new GroupStore();
        String urlkey = null;
        try
        {
            urlkey = store.create(group, getParentKey());
        }
        catch (ParentKeyNotFoundException e)
        {
            LOG.log(Level.SEVERE, "Unable to create group!", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (KeyFoundException e)
        {
            LOG.log(Level.INFO, "Unable to create group!", e);
            return Response.status(Status.NOT_ACCEPTABLE).build();
        }
        URI uri = uriInfo.getAbsolutePathBuilder().path(urlkey).build();
        return Response.created(uri).build();
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{urlkey}")
    public Response update(@PathParam("urlkey") String urlkey, Group group)
    {
        GroupStore store = new GroupStore();
        try
        {
            store.update(urlkey, group, getParentKey());
        }
        catch (ParentKeyNotFoundException e)
        {
            LOG.log(Level.SEVERE, "Unable to update group!", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (KeyNotFoundException e)
        {
            LOG.log(Level.INFO, "Unable to update group!", e);
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.ACCEPTED).build();
    }
    
    @DELETE
    @Path("{urlkey}")
    public Response delete(@PathParam("urlkey") String urlkey)
    {
        GroupStore store = new GroupStore();
        try
        {
            store.delete(urlkey, getParentKey());
        }
        catch (ParentKeyNotFoundException e)
        {
            LOG.log(Level.SEVERE, "Unable to delete group!", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (KeyNotFoundException e)
        {
            LOG.log(Level.INFO, "Unable to delete group!", e);
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{urlkey}")
    public Response findByUrlkey(@PathParam("urlkey") String urlkey)
    {
        GroupStore store = new GroupStore();
        Group group = null;
        try
        {
            group = store.findByKey(urlkey, getParentKey());
        }
        catch (ParentKeyNotFoundException e)
        {
            LOG.log(Level.SEVERE, "Unable to find group by urlkey!", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (KeyNotFoundException e)
        {
            LOG.log(Level.INFO, "Unable to find group by urlkey!", e);
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).entity(group).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("limit") @DefaultValue("10") Integer limit,
            @QueryParam("offset") @DefaultValue("0") Integer offset)
    {
        GroupsStore groupsStore = new GroupsStore();
        String urlkey = groupsStore.getUtil().createUrlkey(null);
        Key key = groupsStore.getUtil().createKey(null, urlkey);
        Groups groups = null;
        try
        {
            groups = groupsStore.findByKey(urlkey, null);
        }
        catch (ParentKeyNotFoundException e)
        {
            LOG.log(Level.SEVERE, "Unable to find groups!", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        catch (KeyNotFoundException e)
        {
            LOG.log(Level.SEVERE, "Unable to find groups!", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        
        GroupStore store = new GroupStore();
        List<Group> list = null;
        try
        {
            list = store.findAll(limit, offset, key);
        }
        catch (ParentKeyNotFoundException e)
        {
            LOG.log(Level.SEVERE, "Unable to find all group!", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        for (Group group : list)
        {
            //allLink(group);
            groups.getGroups().add(group);
        }
        groups.setCount(new Long(groups.getGroups().size()));
        return Response.status(Status.OK).entity(groups).build();
    }

    private Key getParentKey()
    {
        GroupsUtil util = new GroupsUtil();
        String urlkey = util.createUrlkey(null);
        return util.createKey(null, urlkey);
    }

}
