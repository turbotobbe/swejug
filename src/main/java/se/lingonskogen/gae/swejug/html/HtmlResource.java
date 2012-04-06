package se.lingonskogen.gae.swejug.html;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import se.lingonskogen.gae.swejug.AbstractResource;
import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.content.ContentUtil;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;
import se.lingonskogen.gae.swejug.content.err.EntityBuilderException;
import se.lingonskogen.gae.swejug.oldstuff.Content;
import se.lingonskogen.gae.swejug.oldstuff.OldContentStore2;
import se.lingonskogen.gae.swejug.oldstuff.OldContentStore2.Roots;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class HtmlResource extends AbstractResource
{
   private static final String CLASS = HtmlResource.class.getName();
   private static final Logger LOG = Logger.getLogger(CLASS);
   
   private static final String VIEW_DISPLAY = "display";
   
   @GET
   public Response get()
   {
      return handleGet();
   }

   @GET
   @Path("{urlkey1}")
   public Response get(@PathParam("urlkey1") String urlkey1)
   {
      return handleGet(urlkey1);
   }

   @GET
   @Path("{urlkey1}/{urlkey2}")
   public Response get(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2)
   {
      return handleGet(urlkey1, urlkey2);
   }

   @GET
   @Path("{urlkey1}/{urlkey2}/{urlkey3}")
   public Response get(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3)
   {
      return handleGet(urlkey1, urlkey2, urlkey3);
   }

   @GET
   @Path("{urlkey1}/{urlkey2}/{urlkey3}/{urlkey4}")
   public Response get(@PathParam("urlkey1") String urlkey1, @PathParam("urlkey2") String urlkey2, @PathParam("urlkey3") String urlkey3, @PathParam("urlkey4") String urlkey4)
   {
      return handleGet(urlkey1, urlkey2, urlkey3, urlkey4);
   }

   private Response handleGet(String... urlkeys)
   {
      OldContentStore2 store = getContentStore(null);
      
      // get entity
      Content content = null;
      try
      {
         content = store.find(Roots.DEFAULT, urlkeys);
      }
      catch (ContentNotFoundException e)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      catch (ConfigException e)
      {
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }
      catch (EntityBuilderException e)
      {
         return Response.status(Status.INTERNAL_SERVER_ERROR).build();
      }

      // create velocity context
      VelocityContext velocityContext = new VelocityContext();
      velocityContext.put("util", new ContentUtil());
      velocityContext.put("content", content);
      velocityContext.put("url", getUriInfo().getAbsolutePath().toString());
      
      // generate html
      String html = generateHtml(VIEW_DISPLAY, content.getMeta().getType(), velocityContext);
      return Response.ok().entity(html).build();
   }

   private String generateHtml(String view, String type, VelocityContext context)
   {
      Writer writer = new StringWriter();
      VelocityEngine engine = (VelocityEngine) getServletContext().getAttribute("velocityEngine");
      
      // TODO merge header
      // <view>/<type>.html i.e. display/user.html
      String templateName = view + File.separatorChar + type.toLowerCase() + ".html";
      Template template = engine.getTemplate(templateName);
      template.merge(context, writer);
      // TODO merge footer
      return writer.toString();
   }
   
}
