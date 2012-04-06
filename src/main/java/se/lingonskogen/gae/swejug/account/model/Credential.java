package se.lingonskogen.gae.swejug.account.model;

public class Credential extends Identifier
{
   public static final String PASSWORD = "password";
   
   private String password;

   public String getPassword()
   {
      return password;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }

   @Override
   public String toString()
   {
      return "Credential [password=" + password + ", identifier=" + getUsername() + "]";
   }
   
}
