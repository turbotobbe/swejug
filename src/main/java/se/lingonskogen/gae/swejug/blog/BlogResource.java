package se.lingonskogen.gae.swejug.blog;

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
import se.lingonskogen.gae.swejug.user.User;
import se.lingonskogen.gae.swejug.user.UserResource;

@Path ("/rest/blogs")
@Produces (MediaType.APPLICATION_JSON)
public class BlogResource
{
    @Context UriInfo uriInfo;
    
    public static final List<Blog> db = new ArrayList<Blog>();
    static
    {
        String as = "Monkies,Cats,Dogs,Pigs";
        String vs = "Jump,Bark,Eat,Smell";
        String[] animals = as.split(",");
        String[] verbs= vs.split(",");
        for (String animal : animals)
        {
            for (String verb : verbs)
            {
                Blog foo = new Blog();
                foo.setName("Why " + animal + " " + verb);
                db.add(foo);
            }
        }
    }
    
    @GET
    @Path ("{userUrlkey}")
    public Response getAll(@PathParam ("userUrlkey") String userUrlkey,
            @QueryParam ("limit") @DefaultValue ("10") Integer limit, 
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
        User user = existsUser(userUrlkey);
        if (user == null)
        {
            return builder.status(Status.NOT_FOUND).build();
        }
        {
            String urlkey = toUrlkey(user);
            Link link = new Link();
            link.setType(MediaType.APPLICATION_JSON);
            link.setRel("self");
            link.setHref(uriInfo.getAbsolutePathBuilder().path(urlkey).build());
            user.setLinks(Collections.singletonList(link));
        }

        Blogs blogs = new Blogs();
        blogs.setBlogs(new ArrayList<Blog>());
        blogs.setLinks(new ArrayList<Link>());
        blogs.setKind(Blogs.KIND);
        blogs.setUser(user);
        for (int i = offset; i < offset+limit; i++)
        {
            if (db.size() > i)
            {
                Blog blog = db.get(i);
                String urlkey = toUrlkey(blog);
                Link link = new Link();
                link.setType(MediaType.APPLICATION_JSON);
                link.setRel("self");
                link.setHref(uriInfo.getAbsolutePathBuilder().path(urlkey).build());
                blog.setLinks(Collections.singletonList(link));
                blogs.getBlogs().add(blog);
            }
        }
        blogs.setTotal(db.size());
        blogs.setCount(blogs.getBlogs().size());
        if (offset > 0)
        {
            Link link = new Link();
            link.setType(MediaType.APPLICATION_JSON);
            link.setRel("prev");
            int os = offset-limit;
            link.setHref(uriInfo.getAbsolutePathBuilder().queryParam("limit", limit).queryParam("offset", Math.max(0, os)).build());
            blogs.getLinks().add(link);
        }
        if (offset+limit < db.size())
        {
            Link link = new Link();
            link.setType(MediaType.APPLICATION_JSON);
            link.setRel("next");
            int os = offset+limit;
            link.setHref(uriInfo.getAbsolutePathBuilder().queryParam("limit", limit).queryParam("offset", Math.min(db.size()-1, os)).build());
            blogs.getLinks().add(link);
        }
        return builder.entity(blogs).build();
    }
    
    @GET
    @Path ("{userUrlkey}/{urlkey}")
    public Response getByKey(@PathParam ("userUrlkey") String userUrlkey,
            @PathParam ("urlkey") String urlkey)
    {
        ResponseBuilder builder = Response.status(Status.NOT_FOUND);
        User user = existsUser(userUrlkey);
        if (user == null)
        {
            return builder.build();
        }
        {
            Link link = new Link();
            link.setType(MediaType.APPLICATION_JSON);
            link.setRel("self");
            link.setHref(uriInfo.getAbsolutePathBuilder().path(userUrlkey).build());
            user.setLinks(Collections.singletonList(link));
        }
        Blog b = null;
        for (Blog blog : db)
        {
            String uk = toUrlkey(blog);
            if (uk.equals(urlkey))
            {
                Link link = new Link();
                link.setType(MediaType.APPLICATION_JSON);
                link.setRel("self");
                link.setHref(uriInfo.getAbsolutePath());
                b = new Blog();
                b.setKind(Blog.KIND);
                b.setName(blog.getName());
                b.setLinks(Collections.singletonList(link));
                b.setUser(user);
                builder.status(Status.OK).entity(b);
                break;
            }
        }
        return builder.build();
    }

    private User existsUser(String userUrlkey)
    {
        for (User user : UserResource.db)
        {
            String uk = toUrlkey(user);
            if (uk.equals(userUrlkey))
            {
                return user;
            }
        }
        return null;
    }

    private String toUrlkey(User user)
    {
        return user.getName().toLowerCase().replaceAll(" ", "-");
    }

    private String toUrlkey(Blog blog)
    {
        return blog.getName().toLowerCase().replaceAll(" ", "-");
    }

    
    //URI uri = uriInfo.getAbsolutePathBuilder().path(urlkey).build();
}
