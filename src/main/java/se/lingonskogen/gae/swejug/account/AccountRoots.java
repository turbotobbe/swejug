package se.lingonskogen.gae.swejug.account;

import se.lingonskogen.gae.swejug.system.model.Root;

public enum AccountRoots
{
   REGISTERED, ACTIVATED, DEACTIVATED;

   private static final String ACCOUNT = "account";

   private Root root;

   private AccountRoots()
   {
      root = new Root();
      root.setModule(ACCOUNT);
      root.setLabel(name().toLowerCase());
   }
   
   public Root getRoot()
   {
      return root;
   }
   
   @Override
   public String toString()
   {
      return getRoot().toString();
   }
}
