package se.lingonskogen.gae.swejug.context;

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import se.lingonskogen.gae.swejug.config.Config;
import se.lingonskogen.gae.swejug.config.ConfigFactory;

public class ServletContextInstallListener implements ServletContextListener
{
   private static final Logger LOG = Logger.getLogger(ServletContextInstallListener.class.getName());

   @Override
   public void contextInitialized(ServletContextEvent event)
   {
      Config config = ConfigFactory.getConfig();
      List<String> classNames = config.getStrings(Config.INSTALLERS);
      for (String className : classNames)
      {
         try
         {
            LOG.info("Running installer: " + className);
            Class<?> cls = getClass().getClassLoader().loadClass(className);
            Installer obj = (Installer) cls.newInstance();
            obj.install();
         }
         catch (Exception e)
         {
            throw new RuntimeException("Unable to install", e);
         }
      }
   }

   @Override
   public void contextDestroyed(ServletContextEvent event)
   {
      // not run in GAE
   }
}
