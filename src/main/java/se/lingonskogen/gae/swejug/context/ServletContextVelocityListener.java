package se.lingonskogen.gae.swejug.context;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.view.WebappResourceLoader;

public class ServletContextVelocityListener implements ServletContextListener
{
   private static final String CLASS = ServletContextVelocityListener.class.getName();
   private static final Logger LOG = Logger.getLogger(CLASS);

   @Override
   public void contextInitialized(ServletContextEvent event)
   {
      LOG.log(Level.INFO, "Running velocity init ...");

      ServletContext context = event.getServletContext();
      
      VelocityEngine engine = new VelocityEngine();
      engine.setApplicationAttribute("javax.servlet.ServletContext", context);
      
      Properties properties = new Properties();
      properties.put("resource.loader", "webapp");
      properties.put("webapp.resource.loader.class", WebappResourceLoader.class.getName());
      properties.put("webapp.resource.loader.path", "/WEB-INF/velocity");
      
      engine.init(properties);
      
      context.setAttribute("velocityEngine", engine);
   }

   @Override
   public void contextDestroyed(ServletContextEvent event)
   {
      // not run in GAE
   }
}
