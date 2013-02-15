package se.lingonskogen.gae.swejug.content.html;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import se.lingonskogen.gae.swejug.beans.Content;
import se.lingonskogen.gae.swejug.beans.Order;
import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.content.ContentStore;
import se.lingonskogen.gae.swejug.content.ContentUtil;
import se.lingonskogen.gae.swejug.content.RootLessUrlBuilder;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;
import se.lingonskogen.gae.swejug.modules.blog.Blog;
import se.lingonskogen.gae.swejug.modules.event.Event;
import se.lingonskogen.gae.swejug.modules.group.Group;
import se.lingonskogen.gae.swejug.modules.note.Note;
import se.lingonskogen.gae.swejug.modules.root.Root;
import se.lingonskogen.gae.swejug.modules.user.User;
import se.lingonskogen.gae.swejug.web.WebPathBuilder;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class ContentHtmlResource
{
   private static final String CLASS = ContentHtmlResource.class.getName();
   private static final Logger LOG = Logger.getLogger(CLASS);

   private static final int NUM_SMALL = 5;
   private static final int NUM_LARGE = 20;
   
   @Context
   private ServletContext servletContext;
   
   @Context
   private HttpServletRequest httpServletRequest;
   private User activeUser;

   private ContentStore getContentStore() throws ConfigException, ContentNotFoundException
   {
      ContentStore store = new ContentStore(Root.Label.DEFAULT.toString());
      store.setUrlBuilder(new RootLessUrlBuilder());
      
      activeUser = (User) httpServletRequest.getAttribute("active-user");
      if (activeUser == null)
      {
         // get this from permit
         activeUser = new User();
         activeUser.setLabel("Guest");
      }
      store.setUserKey(activeUser.getKey());
      return store;
   }
   
   @GET
   public Response getRoot()
   {
      String html = null;
      VelocityContext vc = new VelocityContext();
      ContentStore store;
      try
      {
         store = getContentStore();
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get active user from content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      Content content;
      try
      {
         content = store.getContent();
         vc.put("self", content);
         vc.put("root", content);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get roots path.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get roots path.", e);
         return Response.status(Status.NOT_FOUND).build();
      }
      
      try
      {
         List<? extends Content> list;
         Integer limit = NUM_SMALL;
         
         List<Order> order = new ArrayList<Order>();
         order.add(new Order(User.LASTNAME));
         order.add(new Order(User.FIRSTNAME));
         list = store.getContentList(content.getKey(), User.TYPE, order, limit, 0);
         vc.put("users", list);

         list = store.getContentList(content.getKey(), Blog.TYPE, limit, 0);
         vc.put("blogs", list);

         order = Collections.singletonList(new Order(Group.TITLE));
         list = store.getContentList(content.getKey(), Group.TYPE, limit, 0);
         vc.put("groups", list);

         list = store.getContentList(content.getKey(), Event.TYPE, limit, 0);
         vc.put("events", list);

         list = store.getContentList(content.getKey(), Note.TYPE, limit, 0);
         vc.put("notes", list);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get roots children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get roots children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }

      html = generateHtml("view", vc, Root.TYPE);
      return Response.status(Status.OK).entity(html).build();
   }

   @GET
   @Path("users")
   public Response getUsers(@QueryParam("page") @DefaultValue("1") Integer page)
   {
      String html = null;
      VelocityContext vc = new VelocityContext();
      ContentStore store;
      try
      {
         store = getContentStore();
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get active user from content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      Content content;
      try
      {
         content = store.getContent();
         vc.put("self", content);
         vc.put("root", content);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get roots path.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get roots path.", e);
         return Response.status(Status.NOT_FOUND).build();
      }

      try
      {
         List<? extends Content> list;
         Integer limit = NUM_LARGE;
         Integer offset = (page - 1) * limit;
         list = store.getContentList(content.getKey(), User.TYPE, limit, offset);
         vc.put("users", list);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get users children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get users children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }

      html = generateHtml("list", vc, Root.TYPE, "users");
      return Response.status(Status.OK).entity(html).build();
   }

   @GET
   @Path("blogs")
   public Response getBlogs(@QueryParam("page") @DefaultValue("1") Integer page)
   {
      String html = null;
      VelocityContext vc = new VelocityContext();
      ContentStore store;
      try
      {
         store = getContentStore();
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get active user from content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      Content content;
      try
      {
         content = store.getContent();
         vc.put("self", content);
         vc.put("root", content);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get blogs path.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get blogs path.", e);
         return Response.status(Status.NOT_FOUND).build();
      }
      
      try
      {
         List<? extends Content> list;
         Integer limit = NUM_LARGE;
         Integer offset = (page - 1) * limit;
         list = store.getContentList(content.getKey(), Blog.TYPE, limit, offset);
         vc.put("blogs", list);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get blogs children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get blogs children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }

      html = generateHtml("list", vc, Root.TYPE, "blogs");
      return Response.status(Status.OK).entity(html).build();
   }

   @GET
   @Path("groups")
   public Response getGroups(@QueryParam("page") @DefaultValue("1") Integer page)
   {
      String html = null;
      VelocityContext vc = new VelocityContext();
      ContentStore store;
      try
      {
         store = getContentStore();
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get active user from content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      Content content;
      try
      {
         content = store.getContent();
         vc.put("self", content);
         vc.put("root", content);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get groups path.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get groups path.", e);
         return Response.status(Status.NOT_FOUND).build();
      }

      try
      {
         List<? extends Content> list;
         Integer limit = NUM_LARGE;
         Integer offset = (page - 1) * limit;
         list = store.getContentList(content.getKey(), Group.TYPE, limit, offset);
         vc.put("groups", list);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get groups children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get groups children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }

      html = generateHtml("list", vc, Root.TYPE, "groups");
      return Response.status(Status.OK).entity(html).build();
   }

   @GET
   @Path("events")
   public Response getEvents(@QueryParam("page") @DefaultValue("1") Integer page)
   {
      String html = null;
      VelocityContext vc = new VelocityContext();
      ContentStore store;
      try
      {
         store = getContentStore();
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get active user from content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      Content content;
      try
      {
         content = store.getContent();
         vc.put("self", content);
         vc.put("root", content);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get events path.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get events path.", e);
         return Response.status(Status.NOT_FOUND).build();
      }

      try
      {
         List<? extends Content> list;
         Integer limit = NUM_LARGE;
         Integer offset = (page - 1) * limit;
         list = store.getContentList(content.getKey(), Event.TYPE, limit, offset);
         vc.put("events", list);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get events children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get events children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }

      html = generateHtml("list", vc, Root.TYPE, "events");
      return Response.status(Status.OK).entity(html).build();
   }

   @GET
   @Path("notes")
   public Response getNotes(@QueryParam("page") @DefaultValue("1") Integer page)
   {
      String html = null;
      VelocityContext vc = new VelocityContext();
      ContentStore store;
      try
      {
         store = getContentStore();
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get active user from content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      Content content;
      try
      {
         content = store.getContent();
         vc.put("self", content);
         vc.put("root", content);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get events path.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get events path.", e);
         return Response.status(Status.NOT_FOUND).build();
      }

      try
      {
         List<? extends Content> list;
         Integer limit = NUM_LARGE;
         Integer offset = (page - 1) * limit;
         list = store.getContentList(content.getKey(), Note.TYPE, limit, offset);
         vc.put("notes", list);
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get events children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get events children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }

      html = generateHtml("list", vc, Root.TYPE, "notes");
      return Response.status(Status.OK).entity(html).build();
   }

   @GET
   @Path("{urlkey1}")
   public Response getAlpha(@PathParam("urlkey1") String urlkey1, @QueryParam("page") @DefaultValue("1") Integer page)
   {
      String html = null;
      VelocityContext vc = new VelocityContext();
      ContentStore store;
      try
      {
         store = getContentStore();
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get active user from content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      Content content;
      try
      {
         content = store.getContent(urlkey1);
         vc.put("self", content);
         vc.put("root", content.getParent());
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get path.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.FINE, "Unable to get path.", e);
         return Response.status(Status.NOT_FOUND).build();
      }
      
      try
      {
         List<? extends Content> list;
         if (checkTypes(content, Root.TYPE, User.TYPE))
         {
            vc.put("user", content);
            Integer limit = NUM_SMALL;
            Integer offset = (page-1)*limit; // TODO: same page for blogs and notes
            list = store.getContentList(content.getKey(), Blog.TYPE, limit, offset);
            vc.put("blogs", list);
            list = store.getContentList(content.getKey(), Note.TYPE, limit, offset);
            vc.put("notes", list);
            html = generateHtml("view", vc, Root.TYPE, User.TYPE);
         }
         else if (checkTypes(content, Root.TYPE, Group.TYPE))
         {
            vc.put("group", content);
            Integer limit = NUM_SMALL;
            Integer offset = (page-1)*limit; // TODO: same page for events and notes
            list = store.getContentList(content.getKey(), Event.TYPE, limit, offset);
            vc.put("events", list);
            list = store.getContentList(content.getKey(), Note.TYPE, limit, offset);
            vc.put("notes", list);
            html = generateHtml("view", vc, Root.TYPE, Group.TYPE);
         }
         else
         {
            LOG.log(Level.WARNING, "Unknown content type");
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
         }
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get blog children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get blog children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      return Response.status(Status.OK).entity(html).build();
   }

   @GET
   @Path("{urlkey1}/{urlkey2}")
   public Response getBeta(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @QueryParam("page") @DefaultValue("1") Integer page)
   {
      String html = null;
      VelocityContext vc = new VelocityContext();
      ContentStore store;
      try
      {
         store = getContentStore();
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get active user from content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      Content content;
      try
      {
         content = store.getContent(urlkey1, urlkey2);
         vc.put("self", content);
         vc.put("root", content.getParent().getParent());
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get path.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.FINE, "Unable to get path.", e);
         return Response.status(Status.NOT_FOUND).build();
      }

      try
      {
         List<? extends Content> list;
         if (checkTypes(content, Root.TYPE, User.TYPE, Blog.TYPE))
         {
            vc.put("blog", content);
            vc.put("user", content.getParent());
            Integer limit = NUM_SMALL;
            Integer offset = (page-1)*limit;
            list = store.getContentList(content.getKey(), Note.TYPE, limit, offset);
            vc.put("notes", list);
            html = generateHtml("view", vc, Root.TYPE, User.TYPE, Blog.TYPE);
         }
         else if (checkTypes(content, Root.TYPE, Group.TYPE, Event.TYPE))
         {
            vc.put("event", content);
            vc.put("group", content.getParent());
            Integer limit = NUM_SMALL;
            Integer offset = (page-1)*limit;
            list = store.getContentList(content.getKey(), Note.TYPE, limit, offset);
            vc.put("notes", list);
            html = generateHtml("view", vc, Root.TYPE, Group.TYPE, Event.TYPE);
         }
         else
         {
            LOG.log(Level.WARNING, "Unknown content type");
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
         }
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get note children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get note children.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      return Response.status(Status.OK).entity(html).build();
   }

   @GET
   @Path("{urlkey1}/{urlkey2}/{urlkey3}")
   public Response getGamma(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3)
   {
      String html = null;
      VelocityContext vc = new VelocityContext();
      ContentStore store;
      try
      {
         store = getContentStore();
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to get active user from content store.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      Content content;
      try
      {
         content = store.getContent(urlkey1, urlkey2, urlkey3);
         vc.put("self", content);
         vc.put("root", content.getParent().getParent().getParent());
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to get path.", e);
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.FINE, "Unable to get path.", e);
         return Response.status(Status.NOT_FOUND).build();
      }

      if (checkTypes(content, Root.TYPE, User.TYPE, Blog.TYPE, Note.TYPE))
      {
         vc.put("note", content);
         vc.put("blog", content.getParent());
         vc.put("user", content.getParent().getParent());
         html = generateHtml("view", vc, Root.TYPE, User.TYPE, Blog.TYPE, Note.TYPE);
      }
      else if (checkTypes(content, Root.TYPE, Group.TYPE, Event.TYPE, Note.TYPE))
      {
         vc.put("note", content);
         vc.put("event", content.getParent());
         vc.put("group", content.getParent().getParent());
         html = generateHtml("view", vc, Root.TYPE, User.TYPE, Blog.TYPE, Note.TYPE);
      }
      else
      {
         LOG.log(Level.WARNING, "Unknown content type");
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      return Response.status(Status.OK).entity(html).build();
   }
   
   private boolean checkTypes(Content content, String... types)
   {
      boolean match = true;
      for (int i = types.length-1; i >= 0; i--)
      {
         match &= types[i].equals(content.getMeta().getType());
         content = content.getParent();
      }
      match &= content == null;
      return match;
   }

   private String generateHtml(String view, VelocityContext context, String... type)
   {
      context.put("me", activeUser);
      context.put("util", new ContentUtil());
      context.put("path", new WebPathBuilder());
      Writer writer = new StringWriter();
      VelocityEngine engine = (VelocityEngine) getServletContext().getAttribute("velocityEngine");
      String templateName = "demo.html";
      //      StringBuilder sb = new StringBuilder();
//      for (String t : type)
//      {
//         sb.append(t.toLowerCase());
//         sb.append(File.separatorChar);
//      }
//      sb.append(view);
//      sb.append(".html");
//      String templateName = sb.toString();
      Template template = engine.getTemplate(templateName);
      template.merge(context, writer);
      return writer.toString();
   }

   protected ServletContext getServletContext()
   {
      return servletContext;
   }
}
