package se.lingonskogen.gae.swejug.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class CachedFileConfig extends FileConfig
{
   private static final Logger LOG = Logger.getLogger(CachedFileConfig.class.getName());
   
   protected JSONObject getJSONConfig() throws JSONException
   {
      if (config == null)
      {
         MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
         String value = (String) cache.get("config");
         if (value == null)
         {
            LOG.log(Level.INFO, "No config in cache");
            JSONObject json = super.getJSONConfig();
            cache.put("config", json.toString());
         }
         else
         {
            LOG.log(Level.INFO, "Setting config from cache");
            setJSONConfig(new JSONObject(value));
         }
      }
      return super.getJSONConfig();
   }
}
