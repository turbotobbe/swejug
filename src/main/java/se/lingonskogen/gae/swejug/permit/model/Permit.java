package se.lingonskogen.gae.swejug.permit.model;


public class Permit
{
   public static final String KIND = Permit.class.getSimpleName();
   
   public static final String ROLE = "role";
   
   public static final String RESOURCE = "resource";

   public static final String OPERATION = "operation";

   private Role role;
   
   private String resource;
   
   private String operation;

   public Role getRole()
   {
      return role;
   }

   public void setRole(Role role)
   {
      this.role = role;
   }

   public String getResource()
   {
      return resource;
   }

   public void setResource(String resource)
   {
      this.resource = resource;
   }

   public String getOperation()
   {
      return operation;
   }

   public void setOperation(String operation)
   {
      this.operation = operation;
   }
   
}
