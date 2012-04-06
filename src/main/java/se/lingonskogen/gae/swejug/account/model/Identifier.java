package se.lingonskogen.gae.swejug.account.model;

public class Identifier
{
   public static final String USERNAME = "username";
   
   private String username;

   public String getUsername()
   {
      return username;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   @Override
   public String toString()
   {
      return "Identifier [username=" + username + "]";
   }

}
