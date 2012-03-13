package se.lingonskogen.gae.swejug.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Stack;
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
import org.codehaus.jettison.json.JSONArray;
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
      if (!content.has(ContentStore.PROP_META))
      {
         throw new JSONException("Missing required meta"); 
      }
      JSONObject meta = content.getJSONObject(ContentStore.PROP_META);
      if (!meta.has(ContentStore.PROP_TYPE))
      {
         throw new JSONException("Missing required meta type"); 
      }
      String type = meta.getString(ContentStore.PROP_META_TYPE);
      
      // generate urlkey
      UrlKeyGenerator generator = getUrlKeyGenerator(type);
      String urlkey = generator.generate(content);
      
      // create entity
      Entity entity = new Entity(ContentStore.KIND, urlkey);
      Stack<String> keys = new Stack<String>();
      buildEntity(content, entity, keys);

      return entity;
   }

   private void buildEntity(JSONObject jObj, Entity entity, Stack<String> keys) throws JSONException
   {
      for (@SuppressWarnings("unchecked")
      Iterator<String> it = jObj.keys(); it.hasNext();)
      {
         String key = (String) it.next();
         Object val = jObj.get(key);
         keys.push(key);
         if (val instanceof JSONObject)
         {
            JSONObject jo = (JSONObject) val;
            buildEntity(jo, entity, keys);
         }
         else if (val instanceof JSONArray)
         {
            JSONArray ja = (JSONArray) val;
            buildentity(ja, entity, keys);
         }
         else
         {
            String propertyKey = makePropertyKey(keys);
            entity.setProperty(propertyKey, val);
         }
         keys.pop();
      }
   }

   private void buildentity(JSONArray jArr, Entity entity, Stack<String> keys) throws JSONException
   {
      for (int i = 0; i < jArr.length(); i++)
      {
         keys.push(Integer.toString(i));
         Object obj = jArr.get(i);
         if (obj instanceof JSONObject)
         {
            JSONObject jo = (JSONObject) obj;
            buildEntity(jo, entity, keys);
         }
         else if (obj instanceof JSONArray)
         {
            JSONArray ja = (JSONArray) obj;
            buildentity(ja, entity, keys);
         }
         else
         {
            String propertyKey = makePropertyKey(keys);
            entity.setProperty(propertyKey, obj);
         }
         keys.pop();
      }
   }

   private String makePropertyKey(Stack<String> keys)
   {
      StringBuilder sb = new StringBuilder();
      for (String k : keys)
      {
         if (sb.length() > 0)
         {
            sb.append(ContentStore.DIV);
         }
         sb.append(k);
      }
      String propertyKey = sb.toString();
      return propertyKey;
   }

   private UrlKeyGenerator getUrlKeyGenerator(String type)
   {
      return new NameUrlKeyGenerator();
   }

}
