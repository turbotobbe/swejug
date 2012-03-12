package se.lingonskogen.gae.swejug.json;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.appengine.api.datastore.Entity;

public class NameUrlKeyGenerator extends StringUrlKeyGenerator implements UrlKeyGenerator
{
   private static final String PROP_NAME = "name";

   @Override
   public String generate(Entity entity) throws UrlKeyGeneratorException
   {
      if (!entity.hasProperty(PROP_NAME))
      {
         throw new UrlKeyGeneratorException("Missing property " + PROP_NAME);
      }
      String name = (String) entity.getProperty(PROP_NAME);
      return name.trim().toLowerCase().replaceAll("[^a-z0-9]", "-");
   }

   @Override
   public String generate(JSONObject content) throws UrlKeyGeneratorException
   {
      if (!content.has(PROP_NAME))
      {
         throw new UrlKeyGeneratorException("Missing property " + PROP_NAME);
      }
      String name;
      try
      {
         name = (String) content.get(PROP_NAME);
      }
      catch (JSONException e)
      {
         throw new UrlKeyGeneratorException(e);
      }
      return generate(name);
   }

}
