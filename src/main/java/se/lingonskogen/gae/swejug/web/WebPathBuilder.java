package se.lingonskogen.gae.swejug.web;

import java.util.ArrayList;
import java.util.List;

import se.lingonskogen.gae.swejug.beans.Content;
import se.lingonskogen.gae.swejug.beans.Link;
import se.lingonskogen.gae.swejug.content.PathBuilder;
import se.lingonskogen.gae.swejug.modules.root.Root;

public class WebPathBuilder implements PathBuilder
{
   @Override
   public List<Link> build(Content content)
   {
      List<Link> list = new ArrayList<Link>();
      while (content != null)
      {
         if (Root.TYPE.equals(content.getMeta().getType()))
         {
            list.add(0, build("Home", "/"));
         }
         else
         {
            list.add(0, content.getLink());
         }
         content = content.getParent();
      }
      return list;
   }

   private Link build(String label, String url)
   {
      Link link = new Link();
      link.setLabel(label);
      link.setUrl(url);
      return link;
   }

}
