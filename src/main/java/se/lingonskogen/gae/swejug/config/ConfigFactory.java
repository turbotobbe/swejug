package se.lingonskogen.gae.swejug.config;

public class ConfigFactory
{
   public static Config getConfig()
   {
      return new CachedFileConfig();
   }
}
