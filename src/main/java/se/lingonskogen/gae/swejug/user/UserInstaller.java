package se.lingonskogen.gae.swejug.user;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class UserInstaller implements ServletContextListener
{

   @Override
   public void contextInitialized(ServletContextEvent event)
   {
      UserStore userStore = new UserStore();
      userStore.install();
   }
   
   @Override
   public void contextDestroyed(ServletContextEvent event)
   {
   }
}
