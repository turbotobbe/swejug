package se.lingonskogen.gae.swejug.beans;

/**
 * {
 *    "rel" : "display",
 *    "label" : "Foo",
 *    "url" : "/foo";
 * }
 */
public class Link
{
   public static final String REL = "rel";
   
   public static final String LABEL = "label";
   
   public static final String URL = "url";
   
   private String rel;
   
   private String label;
   
   private String url;

   public String getRel()
   {
      return rel;
   }

   public void setRel(String rel)
   {
      this.rel = rel;
   }

   public String getLabel()
   {
      return label;
   }

   public void setLabel(String label)
   {
      this.label = label;
   }

   public String getUrl()
   {
      return url;
   }

   public void setUrl(String url)
   {
      this.url = url;
   }
   
}
