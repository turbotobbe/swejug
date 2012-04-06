package se.lingonskogen.gae.swejug.content;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.Key;

public class RootLessUrlBuilder extends DefaultUrlBuilder
{
   protected List<String> makePath(Key key)
   {
      List<String> urlkeys = new ArrayList<String>();
      while (key != null && key.getParent() != null)
      {
         urlkeys.add(0, key.getName());
         key = key.getParent();
      }
      return urlkeys;
   }
}
