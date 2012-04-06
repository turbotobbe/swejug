package se.lingonskogen.gae.swejug.account.model;

public class Tokens
{
   public static final String KIND = Tokens.class.getSimpleName();
   
   public static final String ACCESS_TOKEN = "access-token";
   
   public static final String REFRESH_TOKEN = "refresh-token";
   
   private String accessToken;
   
   private String refreshToken;

   public String getAccessToken()
   {
      return accessToken;
   }

   public void setAccessToken(String accessToken)
   {
      this.accessToken = accessToken;
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
      return "Tokens [accessToken=" + accessToken + ", refreshToken=" + refreshToken + "]";
   }
   
}
