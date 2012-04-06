package se.lingonskogen.gae.swejug.forms;

public class Input
{
   public static final String ID = "id";
   
   public static final String LABEL = "label";
   
   public static final String DESCRIPTION = "description";
   
   public static final String TYPE = "type";
   
   public static final String REQUIRED = "required";
   
   private final String id;
   
   private String label;
   
   private String description;
   
   private Type type;
   
   private Boolean required;
   
   public Input(String id)
   {
      this.id = id;
   }
   
   public String getId()
   {
      return id;
   }

   public String getLabel()
   {
      return label;
   }

   public void setLabel(String label)
   {
      this.label = label;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public Type getType()
   {
      return type;
   }

   public void setType(Type type)
   {
      this.type = type;
   }

   public Boolean getRequired()
   {
      return required;
   }

   public void setRequired(Boolean required)
   {
      this.required = required;
   }

   public static enum Type
   {
      STRING, PASSWORD
   }
}
