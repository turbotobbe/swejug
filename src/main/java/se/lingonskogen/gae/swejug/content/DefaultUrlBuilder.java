package se.lingonskogen.gae.swejug.content;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;

public class DefaultUrlBuilder implements UrlBuilder
{
   @Override
   public String build(Key key)
   {
      List<String> urlkeys = makePath(key);
      StringBuilder sb = new StringBuilder();
      for (String urlkey : urlkeys)
      {
         sb.append("/");
         sb.append(urlkey);
      }
      String url = sb.toString();
      return url;
   }

   protected List<String> makePath(Key key)
   {
      List<String> urlkeys = new ArrayList<String>();
      while (key != null)
      {
         urlkeys.add(0, key.getName());
         key = key.getParent();
      }
      return urlkeys;
   }
}
