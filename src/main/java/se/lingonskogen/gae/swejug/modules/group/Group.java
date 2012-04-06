package se.lingonskogen.gae.swejug.modules.group;

import se.lingonskogen.gae.swejug.beans.Content;

public class Group extends Content
{
   public static final String TYPE = Group.class.getSimpleName();
   
   public static final String TITLE = "title";
   
   private String title;
   
   public Group()
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

}
