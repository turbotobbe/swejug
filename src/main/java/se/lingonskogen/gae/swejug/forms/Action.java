package se.lingonskogen.gae.swejug.forms;

public class Action
{
   public static final String LABEL = "label";
   
   public static final String METHOD = "method";
   
   public static final String URL = "url";
   
   public static final String REDIRECT = "redirect";
   
   private String label;
   
   private Method method;
   
   private String url;
   
   private Boolean redirect;
   
   public String getLabel()
   {
      return label;
   }

   public void setLabel(String label)
   {
      this.label = label;
   }

   public Method getMethod()
   {
      return method;
   }

   public void setMethod(Method method)
   {
      this.method = method;
   }

   public String getUrl()
   {
      return url;
   }

   public void setUrl(String url)
   {
      this.url = url;
   }

   public Boolean getRedirect()
   {
      return redirect;
   }

   public void setRedirect(Boolean redirect)
   {
      this.redirect = redirect;
   }

   public static enum Method
   {
      POST, PUT, GET, DELETE
   }
}
