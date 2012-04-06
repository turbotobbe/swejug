package se.lingonskogen.gae.swejug.account;

public class AccountServiceFactory
{
   public static AccountService getAccountService()
   {
      AccountService service = new DefaultAccountService();
      return service;
   }

   public static AccountSystemService getAccountSystemService()
   {
      AccountSystemService service = new DefaultAccountSystemService();
      return service;
   }
   
}
