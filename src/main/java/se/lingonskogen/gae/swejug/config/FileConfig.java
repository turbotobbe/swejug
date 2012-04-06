package se.lingonskogen.gae.swejug.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class FileConfig extends AbstractConfig implements Config
{
   private static final Logger LOG = Logger.getLogger(FileConfig.class.getName());
   
   private static final String CONFIG_FILE = "se.lingonskogen.gae.swejug.config.file";

   private static final String DEFAULT_CONFIG_FILE = "WEB-INF/config.json";
   
   protected JSONObject config;
   
   protected void setJSONConfig(JSONObject config)
   {
      this.config = config;
   }
   
   protected JSONObject getJSONConfig() throws JSONException
   {
      if (config == null)
      {
         String filename = System.getProperty(CONFIG_FILE, DEFAULT_CONFIG_FILE);
         InputStream in = null;
         try
         {
            LOG.log(Level.INFO, "Loading config from " + filename);
            in = new FileInputStream(filename);
            Writer writer = new StringWriter();
            IOUtils.copy(in, writer);
            config = new JSONObject(writer.toString());
         }
         catch (IOException e)
         {
            LOG.log(Level.WARNING, "Unable to read config file " + filename);
         }
         finally
         {
            IOUtils.closeQuietly(in);
         }
      }
      return config;
   }

}
