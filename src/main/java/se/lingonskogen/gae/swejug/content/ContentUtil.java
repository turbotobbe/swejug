package se.lingonskogen.gae.swejug.content;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.lingonskogen.gae.swejug.beans.Content;
import se.lingonskogen.gae.swejug.beans.Link;

public class ContentUtil
{
   public String formatDate(Date date)
   {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      return format.format(date);
   }
   
   public Object nullSafe(Object obj, String placeholder)
   {
      return obj == null ? placeholder : obj;
   }

   public Object nullSafe(Object obj)
   {
      return nullSafe(obj, "");
   }
   
   public List<Link> makePath(Content content)
   {
      List<Link> path = new ArrayList<Link>();
      while(content != null)
      {
         path.add(0, content.getLink());
         content = content.getParent();
      }
      Link link = new Link();
      link.setLabel("Home");
      link.setUrl("/");
      path.add(0, link);
      return path;
   }
}
