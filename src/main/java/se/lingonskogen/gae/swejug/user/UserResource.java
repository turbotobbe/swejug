package se.lingonskogen.gae.swejug.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import se.lingonskogen.gae.swejug.rest.Link;

@Path ("/rest/users")
@Produces (MediaType.APPLICATION_JSON)
public class UserResource
{
    @Context UriInfo uriInfo;
    
    public static final List<User> db = new ArrayList<User>();
    static
    {
        String names = "Adam,Bertil,Cesar,David,Erik";
        String[] firstnames = names.split(",");
        String[] lastnames = names.split(",");
        for (String firstname : firstnames)
        {
            for (String lastname : lastnames)
            {
                User foo = new User();
                foo.setName(firstname + " " + lastname + "sson");
                db.add(foo);
            }
        }
    }
    
    @GET
    public Response getAll(@QueryParam ("limit") @DefaultValue ("10") Integer limit, 
                        @QueryParam ("offset") @DefaultValue ("0") Integer offset)
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
        else if (offset >= db.size())
        {
            return builder.status(Status.NOT_FOUND).build();
        }
        Users users = new Users();
        users.setUsers(new ArrayList<User>());
        users.setLinks(new ArrayList<Link>());
        users.setKind(Users.KIND);
        for (int i = offset; i < offset+limit; i++)
        {
            if (db.size() > i)
            {
                User user = db.get(i);
                String urlkey = toUrlkey(user);
                Link link = new Link();
                link.setType(MediaType.APPLICATION_JSON);
                link.setRel("self");
                link.setHref(uriInfo.getAbsolutePathBuilder().path(urlkey).build());
                // add blogs link
                user.setLinks(Collections.singletonList(link));
                users.getUsers().add(user);
            }
        }
        users.setTotal(db.size());
        users.setCount(users.getUsers().size());
        if (offset > 0)
        {
            Link link = new Link();
            link.setType(MediaType.APPLICATION_JSON);
            link.setRel("prev");
            int os = offset-limit;
            link.setHref(uriInfo.getAbsolutePathBuilder().queryParam("limit", limit).queryParam("offset", Math.max(0, os)).build());
            users.getLinks().add(link);
        }
        if (offset+limit < db.size())
        {
            Link link = new Link();
            link.setType(MediaType.APPLICATION_JSON);
            link.setRel("next");
            int os = offset+limit;
            link.setHref(uriInfo.getAbsolutePathBuilder().queryParam("limit", limit).queryParam("offset", Math.min(db.size()-1, os)).build());
            users.getLinks().add(link);
        }
        return builder.entity(users).build();
    }
    
    @GET
    @Path ("{urlkey}")
    public Response getByKey(@PathParam ("urlkey") String urlkey)
    {
        ResponseBuilder builder = Response.status(Status.NOT_FOUND);
        User u = null;
        for (User user : db)
        {
            String uk = toUrlkey(user);
            if (uk.equals(urlkey))
            {
                Link link = new Link();
                link.setType(MediaType.APPLICATION_JSON);
                link.setRel("self");
                link.setHref(uriInfo.getAbsolutePath());
                u = new User();
                u.setKind(User.KIND);
                u.setName(user.getName());
                u.setLinks(Collections.singletonList(link));
                builder.status(Status.OK).entity(u);
                break;
            }
        }
        return builder.build();
    }

    private String toUrlkey(User user)
    {
        return user.getName().toLowerCase().replaceAll(" ", "-");
    }

    
    //URI uri = uriInfo.getAbsolutePathBuilder().path(urlkey).build();
}
