package se.lingonskogen.gae.swejug.system.model;

public class Root
{
   public static final String KIND = Root.class.getSimpleName();
   
   public static final String MODULE = "module";
   
   public static final String LABEL = "label";

   private String module;
   
   private String label;

   public String getModule()
   {
      return module;
   }

   public void setModule(String module)
   {
      this.module = module;
   }

   public String getLabel()
   {
      return label;
   }

   public void setLabel(String label)
   {
      this.label = label;
   }

   public String makeKeyName()
   {
      return getModule() + "-" + getLabel();
   }
   
   @Override
   public String toString()
   {
      return "Root [module=" + module + ", label=" + label + "]";
   }

}
