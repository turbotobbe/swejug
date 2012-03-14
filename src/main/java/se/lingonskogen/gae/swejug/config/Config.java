package se.lingonskogen.gae.swejug.config;

import java.util.List;

public interface Config
{
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
}
