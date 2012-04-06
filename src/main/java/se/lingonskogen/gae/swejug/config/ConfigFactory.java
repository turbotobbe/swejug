package se.lingonskogen.gae.swejug.config;

public class ConfigFactory
{
   private static FileConfig config;

   public static Config getConfig()
   {
      if (config == null)
      {
         config = new FileConfig();
      }
      return config;
   }
}
