package se.lingonskogen.gae.swejug.modules.root;

import se.lingonskogen.gae.swejug.beans.Content;

public class Root extends Content
{
   public static final String TYPE = Root.class.getSimpleName();

   public static final String LABEL = "label";
   
   public static enum Label
   {
      DEFAULT;
   }
   
   public String label;

   public Root()
   {
      super(TYPE);
   }

   public Root(Label label)
   {
      super(TYPE);
      setLabel(label.toString());
   }

   public String getLabel()
   {
      return label;
   }

   public void setLabel(String label)
   {
      this.label = label;
   }

}
