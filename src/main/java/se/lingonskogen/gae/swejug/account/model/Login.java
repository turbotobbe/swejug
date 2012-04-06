package se.lingonskogen.gae.swejug.account.model;

public class Login extends Credential
{
   public static final String REFRESH = "refresh";
   
   public static final String ACCESS = "access";
   
   private Boolean refresh = Boolean.FALSE;
   
   private Boolean access = Boolean.TRUE;

   public Boolean getRefresh()
   {
      return refresh;
   }

   public void setRefresh(Boolean refresh)
   {
      this.refresh = refresh;
   }

   public Boolean getAccess()
   {
      return access;
   }

   public void setAccess(Boolean access)
   {
      this.access = access;
   }

   @Override
   public String toString()
   {
      return "LoginForm [refresh=" + refresh + ", access=" + access + ", password=" + getPassword() + ", username=" + getUsername() + "]";
   }

   
}
