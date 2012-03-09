package se.lingonskogen.gae.swejug.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public abstract class Bean
{
   private String kind;

   private List<Link> links = new ArrayList<Link>();

   public String getKind()
   {
      return kind;
   }

   public void setKind(String kind)
   {
      this.kind = kind;
   }

   public List<Link> getLinks()
   {
      return links;
   }

}
