package se.lingonskogen.gae.swejug.config;

import java.util.List;

public interface Config
{
   String META = "meta";
   
   String INSTALLERS = "installers";
   
   String JSON_BUILDERS = "jsonBuilders";

   String ENTITY_BUILDERS = "entityBuilders";

   String getVersion();
   
   String getString(String... keys);
   
   List<String> getStrings(String... keys);

   Boolean getBoolean(String... keys);
   
   List<Boolean> getBooleans(String... keys);
   
   Double getDouble(String... keys);
   
   List<Double> getDoubles(String... keys);
   
   Integer getInteger(String... keys);
   
   List<Integer> getIntegers(String... keys);
   
   Long getLong(String... keys);
   
   List<Long> getLongs(String... keys);
   
   <T> T getInstance(String... keys) throws ConfigException;
}
