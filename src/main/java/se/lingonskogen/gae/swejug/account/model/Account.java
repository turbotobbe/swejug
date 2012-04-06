package se.lingonskogen.gae.swejug.account.model;

public class Account extends Credential
{
   public static final String KIND = Account.class.getSimpleName();
   
   public static final String ROLE = "role";

   public static final String REFRESH_TOKEN = Tokens.REFRESH_TOKEN;
   
   private Role role;
   
   private String refreshToken;
   
   public Role getRole()
   {
      return role;
   }

   public void setRole(Role role)
   {
      this.role = role;
   }

   public String getRefreshToken()
   {
      return refreshToken;
   }

   public void setRefreshToken(String refreshToken)
   {
      this.refreshToken = refreshToken;
   }

   @Override
   public String toString()
   {
      return "Account [role=" + role + ", refreshToken=" + refreshToken + ", password=" + getPassword() + ", identifier=" + getUsername() + "]";
   }

}
