package se.lingonskogen.gae.swejug.modules.user;

import se.lingonskogen.gae.swejug.beans.Content;

public class User extends Content
{
   public static final String TYPE = User.class.getSimpleName();
   
   public static final String FIRSTNAME = "firstname";
   
   public static final String LASTNAME = "lastname";
   
   private String firstname;
   
   private String lastname;

   private String label;

   public User()
   {
      super(TYPE);
   }

   public void setLabel(String label)
   {
      this.label = label;
   }
   
   @Override
   public String getLabel()
   {
      return label != null ? label : getFullname();
   }

   public String getFullname()
   {
      if (getFirstname() != null && getLastname() != null)
      {
         return getFirstname() + " " + getLastname();
      }
      else if (getFirstname() != null)
      {
         return getFirstname();
      }
      else if (getLastname() != null)
      {
         return getLastname();
      }
      return "No Name";
   }

   public String getFirstname()
   {
      return firstname;
   }

   public void setFirstname(String firstname)
   {
      this.firstname = firstname;
   }

   public String getLastname()
   {
      return lastname;
   }

   public void setLastname(String lastname)
   {
      this.lastname = lastname;
   }

}
