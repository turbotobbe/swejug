package se.lingonskogen.gae.swejug;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import se.lingonskogen.gae.swejug.group.GroupInstaller;

public class InstallListener implements ServletContextListener
{
    private static final Logger LOG = Logger.getLogger(InstallListener.class.getName());
    
    @Override
    public void contextInitialized(ServletContextEvent event)
    {
        String className = GroupInstaller.class.getName();
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

    @Override
    public void contextDestroyed(ServletContextEvent event)
    {
    }
}
