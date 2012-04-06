package se.lingonskogen.gae.swejug.modules.blog;

import se.lingonskogen.gae.swejug.beans.Content;

public class Blog extends Content
{
   public static final String TYPE = Blog.class.getSimpleName();
   
   public static final String TITLE = "title";
   
   public static final String CONTENT = "content";
   
   private String title;
   
   private String content;

   public Blog()
   {
      super(TYPE);
   }
   
   @Override
   public String getLabel()
   {
      return getTitle();
   }
   
   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   public String getContent()
   {
      return content;
   }

   public void setContent(String content)
   {
      this.content = content;
   }

}
