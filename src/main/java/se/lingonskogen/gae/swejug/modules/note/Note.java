package se.lingonskogen.gae.swejug.modules.note;

import se.lingonskogen.gae.swejug.beans.Content;

public class Note extends Content
{
   public static final String TYPE = Note.class.getSimpleName();
   
   public static final String INDEX = "index";
   
   public static final String CONTENT = "content";

   private Long index;
   
   private String content;

   public Note()
   {
      super(TYPE);
   }

   @Override
   public String getLabel()
   {
      return getIndex().toString();
   }
   
   public Long getIndex()
   {
      return index;
   }

   public void setIndex(Long index)
   {
      this.index = index;
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
