package se.lingonskogen.gae.swejug.account;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import se.lingonskogen.gae.swejug.account.model.Account;
import se.lingonskogen.gae.swejug.account.model.Role;
import se.lingonskogen.gae.swejug.system.model.Root;

public class AccountInstaller implements ServletContextListener
{
   private static final String CLASS = AccountInstaller.class.getName();
   private static final Logger LOG = Logger.getLogger(CLASS);
   
   @Override
   public void contextInitialized(ServletContextEvent event)
   {
      AccountSystemService service = AccountServiceFactory.getAccountSystemService();
      for (AccountRoots accountRoot : AccountRoots.values())
      {
         Root root = accountRoot.getRoot();
         if (service.installRoot(root))
         {
            LOG.log(Level.INFO, "Installed root {0}", root.toString());
            if (accountRoot.equals(AccountRoots.ACTIVATED))
            {
               Account admin = new Account();
               admin.setUsername("admin");
               admin.setPassword("admin");
               admin.setRole(Role.ADMIN);
               admin.setRefreshToken("00." + admin.getUsername());
               if (service.installAccount(root, admin))
               {
                  LOG.log(Level.INFO, "Installed account {0}", admin.toString());
               }
               else
               {
                  LOG.log(Level.INFO, "Account {0} already installed.", admin.toString());
               }
            }
         }
         else
         {
            LOG.log(Level.INFO, "Root {0} already installed.", root.toString());
         }
      }
   }

   @Override
   public void contextDestroyed(ServletContextEvent event)
   {
   }

}
