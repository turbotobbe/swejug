package se.lingonskogen.gae.swejug.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import com.google.appengine.api.datastore.Entity;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class ContentMessageBodyReader implements MessageBodyReader<Entity>
{
   private static final Logger LOG = Logger.getLogger(ContentMessageBodyReader.class.getName());
   
   @Override
   public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotation,
         MediaType mediaType)
   {
      return true;
   }

   @Override
   public Entity readFrom(Class<Entity> type, Type genericType, Annotation[] annotation,
         MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
         throws IOException, WebApplicationException
   {
      Writer writer = new StringWriter();
      IOUtils.copy(entityStream, writer);
      JSONTokener tokener = new JSONTokener(writer.toString());
      Entity entity = null;
      try
      {
         JSONObject content = new JSONObject(tokener);
         entity = jsonToEntity(content);
      }
      catch (JSONException e)
      {
         LOG.log(Level.INFO, "Unable to read json", e);
         throw new WebApplicationException(e, Status.BAD_REQUEST);
      }
      catch (UrlKeyGeneratorException e)
      {
         LOG.log(Level.INFO, "Unable to read json", e);
         throw new WebApplicationException(e, Status.BAD_REQUEST);
      }
      return entity;
   }

   private Entity jsonToEntity(JSONObject content) throws JSONException, UrlKeyGeneratorException
   {
      // check for required property
      if (!content.has(ContentStore.PROP_TYPE))
      {
         throw new JSONException("Missing required property " + ContentStore.PROP_TYPE); 
      }
      
      // generate urlkey
      UrlKeyGenerator generator = getUrlKeyGenerator();
      String urlkey = generator.generate(content);
      
      // create entity
      Entity entity = new Entity(ContentStore.KIND, urlkey);
      
      // copy properties
      @SuppressWarnings("unchecked")
      Iterator<String> keys = content.keys();
      while (keys.hasNext())
      {
         String key = keys.next();
         entity.setProperty(key, content.get(key));
      }
      return entity;
   }

   private UrlKeyGenerator getUrlKeyGenerator()
   {
      return new NameUrlKeyGenerator();
   }

}
