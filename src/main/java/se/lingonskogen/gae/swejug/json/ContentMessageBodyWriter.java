package se.lingonskogen.gae.swejug.json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.appengine.api.datastore.Entity;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContentMessageBodyWriter implements MessageBodyWriter<Entity>
{
   private static final Logger LOG = Logger.getLogger(ContentMessageBodyWriter.class.getName());

   @Override
   public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotation,
         MediaType mediaType)
   {
      return true;
   }

   @Override
   public long getSize(Entity entity, Class<?> type, Type genericType, Annotation[] annotation,
         MediaType mediaType)
   {
      JSONObject json;
      try
      {
         json = entityToJson(entity);
      }
      catch (JSONException e)
      {
         LOG.log(Level.INFO, "Unable to write json", e);
         throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
      }
      return json.toString().length();
   }

   @Override
   public void writeTo(Entity entity, Class<?> type, Type genericType, Annotation[] annotation,
         MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
         throws IOException, WebApplicationException
   {
      JSONObject json;
      try
      {
         json = entityToJson(entity);
      }
      catch (JSONException e)
      {
         LOG.log(Level.INFO, "Unable to write json", e);
         throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
      }
      Reader reader = new StringReader(json.toString());
      IOUtils.copy(reader, entityStream);
   }

   private JSONObject entityToJson(Entity entity) throws JSONException
   {
      // check for required property
      if (!entity.hasProperty(ContentStore.PROP_META_TYPE))
      {
         throw new JSONException("Missing required meta type");
      }

      JsonNode node = new JsonNode();
      Map<String, Object> properties = entity.getProperties();
      for (String key : properties.keySet())
      {
         Object val = properties.get(key);
         node.add(key, val);
      }
      return node.toJSON();

   }

}
