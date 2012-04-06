package se.lingonskogen.gae.swejug.beans;

import java.util.ArrayList;
import java.util.List;

public class PropertyBuilder
{
   private static final String DIV = "_";
   
   private List<String> parts = new ArrayList<String>();
   
   public PropertyBuilder add(String part)
   {
      parts.add(part);
      return this;
   }

   public PropertyBuilder clear(String part)
   {
      parts.clear();
      return add(part);
   }

   public String build()
   {
      StringBuilder sb = new StringBuilder();
      for (String part : parts)
      {
         if (sb.length() > 0)
         {
            sb.append(DIV);
         }
         sb.append(part);
      }
      return sb.toString();
   }
}
