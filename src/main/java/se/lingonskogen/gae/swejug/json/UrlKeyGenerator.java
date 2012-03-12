package se.lingonskogen.gae.swejug.json;

import org.codehaus.jettison.json.JSONObject;

import com.google.appengine.api.datastore.Entity;

public interface UrlKeyGenerator
{
   String generate(Entity entity) throws UrlKeyGeneratorException;

   String generate(JSONObject content) throws UrlKeyGeneratorException;
}
