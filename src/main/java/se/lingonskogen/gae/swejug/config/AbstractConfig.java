package se.lingonskogen.gae.swejug.config;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public abstract class AbstractConfig implements Config
{
   private static final Logger LOG = Logger.getLogger(AbstractConfig.class.getName());
   private static final String PROP_META_VERSION = "meta_version";
   
   protected abstract JSONObject getJSONConfig() throws JSONException;

   @Override
   public String getVersion()
   {
      return getJSONString(PROP_META_VERSION);
   }
   
   @Override
   public String getString(String... keys)
   {
      return getJSONString(keys);
   }

   @Override
   public List<String> getStrings(String... keys)
   {
      return getJSONStrings(keys);
   }

   @Override
   public Boolean getBoolean(String... keys)
   {
      return getJSONBoolean(keys);
   }

   @Override
   public List<Boolean> getBooleans(String... keys)
   {
      return getJSONBooleans(keys);
   }

   @Override
   public Double getDouble(String... keys)
   {
      return getJSONDouble(keys);
   }

   @Override
   public List<Double> getDoubles(String... keys)
   {
      return getJSONDoubles(keys);
   }

   @Override
   public Integer getInteger(String... keys)
   {
      return getJSONInteger(keys);
   }

   @Override
   public List<Integer> getIntegers(String... keys)
   {
      return getJSONIntegers(keys);
   }

   @Override
   public Long getLong(String... keys)
   {
      return getJSONLong(keys);
   }

   @Override
   public List<Long> getLongs(String... keys)
   {
      return getJSONLongs(keys);
   }
   
   protected String getJSONString(String... keys)
   {
      String value = null;
      try
      {
         JSONObject json = getParent(keys);
         value = json.getString(keys[keys.length-1]);
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to get json value", e);
      }
      return value;
   }

   protected List<String> getJSONStrings(String... keys)
   {
      List<String> list = new ArrayList<String>();
      try
      {
         JSONObject json = getParent(keys);
         JSONArray array = json.getJSONArray(keys[keys.length-1]);
         for (int i = 0; i < array.length(); i++)
         {
            list.add(array.getString(i));
         }
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to get json value", e);
      }
      return list;
   }

   protected Boolean getJSONBoolean(String... keys)
   {
      Boolean value = null;
      try
      {
         JSONObject json = getParent(keys);
         value = json.getBoolean(keys[keys.length-1]);
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to get json value", e);
      }
      return value;
   }

   protected List<Boolean> getJSONBooleans(String... keys)
   {
      List<Boolean> list = new ArrayList<Boolean>();
      try
      {
         JSONObject json = getParent(keys);
         JSONArray array = json.getJSONArray(keys[keys.length-1]);
         for (int i = 0; i < array.length(); i++)
         {
            list.add(array.getBoolean(i));
         }
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to get json value", e);
      }
      return list;
   }

   protected Double getJSONDouble(String... keys)
   {
      Double value = null;
      try
      {
         JSONObject json = getParent(keys);
         value = json.getDouble(keys[keys.length-1]);
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to get json value", e);
      }
      return value;
   }

   protected List<Double> getJSONDoubles(String... keys)
   {
      List<Double> list = new ArrayList<Double>();
      try
      {
         JSONObject json = getParent(keys);
         JSONArray array = json.getJSONArray(keys[keys.length-1]);
         for (int i = 0; i < array.length(); i++)
         {
            list.add(array.getDouble(i));
         }
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to get json value", e);
      }
      return list;
   }

   protected Integer getJSONInteger(String... keys)
   {
      Integer value = null;
      try
      {
         JSONObject json = getParent(keys);
         value = json.getInt(keys[keys.length-1]);
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to get json value", e);
      }
      return value;
   }

   protected List<Integer> getJSONIntegers(String... keys)
   {
      List<Integer> list = new ArrayList<Integer>();
      try
      {
         JSONObject json = getParent(keys);
         JSONArray array = json.getJSONArray(keys[keys.length-1]);
         for (int i = 0; i < array.length(); i++)
         {
            list.add(array.getInt(i));
         }
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to get json value", e);
      }
      return list;
   }

   protected Long getJSONLong(String... keys)
   {
      Long value = null;
      try
      {
         JSONObject json = getParent(keys);
         value = json.getLong(keys[keys.length-1]);
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to get json value", e);
      }
      return value;
   }

   protected List<Long> getJSONLongs(String... keys)
   {
      List<Long> list = new ArrayList<Long>();
      try
      {
         JSONObject json = getParent(keys);
         JSONArray array = json.getJSONArray(keys[keys.length-1]);
         for (int i = 0; i < array.length(); i++)
         {
            list.add(array.getLong(i));
         }
      }
      catch (JSONException e)
      {
         LOG.log(Level.WARNING, "Unable to get json value", e);
      }
      return list;
   }

   private JSONObject getParent(String... keys) throws JSONException
   {
      JSONObject json = getJSONConfig();
      for (int i = 0; i < keys.length - 1; i++)
      {
         String[] split = keys[i].split("_");
         for (int j = 0; j < split.length; j++)
         {
            json = json.getJSONObject(split[j]);
         }
      }
      return json;
   }
}
