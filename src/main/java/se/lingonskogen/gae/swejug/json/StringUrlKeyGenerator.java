package se.lingonskogen.gae.swejug.json;

public class StringUrlKeyGenerator
{
   protected String generate(String name)
   {
      return name.trim().toLowerCase().replaceAll("[^a-z0-9]", "-");
   }

}
