package se.lingonskogen.gae.swejug.content;

import java.util.logging.Level;
import java.util.logging.Logger;

import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;
import se.lingonskogen.gae.swejug.content.err.ContentNotUniqueException;
import se.lingonskogen.gae.swejug.context.Installer;
import se.lingonskogen.gae.swejug.modules.blog.Blog;
import se.lingonskogen.gae.swejug.modules.event.Event;
import se.lingonskogen.gae.swejug.modules.group.Group;
import se.lingonskogen.gae.swejug.modules.note.Note;
import se.lingonskogen.gae.swejug.modules.root.Root;
import se.lingonskogen.gae.swejug.modules.user.User;

import com.google.appengine.api.datastore.Key;

public class ContentInstaller implements Installer
{
   private static final Logger LOG = Logger.getLogger(ContentInstaller.class.getName());

   @Override
   public void install()
   {
      ContentStore store = new ContentStore(Root.Label.DEFAULT.toString());
      
      try
      {
         store.getContent();
         LOG.log(Level.INFO, "Already installed.");
         return;
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to install.", e);
         return;
      }
      catch (ContentNotFoundException e)
      {
         // not installed
      }

      Key rootKey = null;
      Key adminKey = null;
      try
      {
         Root root = new Root();
         root.setLabel(Root.Label.DEFAULT.toString());
         rootKey = store.create(null, root);
         LOG.log(Level.INFO, "Created root: " + rootKey.toString());

         User admin = new User();
         admin.setFirstname("Administrator");
         adminKey = store.create(rootKey, admin);
         LOG.log(Level.INFO, "Created user: " + adminKey.toString());
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to install.", e);
      }
      catch (ContentNotUniqueException e)
      {
         LOG.log(Level.WARNING, "Unable to install.", e);
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to install.", e);
      }

      try
      {
         store.setUserKey(adminKey);
         store.setCreated(adminKey, rootKey);
         store.setCreated(adminKey, adminKey);

         User tobbe = new User();
         tobbe.setFirstname("Thobias");
         tobbe.setLastname("Bergqvist");
         Key tobbeKey = store.create(rootKey, tobbe);
         LOG.log(Level.INFO, "Created user: " + tobbeKey.toString());

         Blog jump = new Blog();
         jump.setTitle("I just jumped.");
         jump.setContent("... and it was funny!");
         Key jumpKey = store.create(tobbeKey, jump);
         LOG.log(Level.INFO, "Created blog: " + jumpKey.toString());

         for (int i = 0; i < 3; i++)
         {
            Note note = new Note();
            note.setIndex((long) i);
            note.setContent(tobbe.getFullname() + " : " + jump.getTitle() + " : " + i);
            Key noteKey = store.create(jumpKey, note);
            LOG.log(Level.INFO, "Created note: " + noteKey.toString());
         }

         User pelle = new User();
         pelle.setFirstname("Pelle");
         pelle.setLastname("Pellesson");
         Key pelleKey = store.create(rootKey, pelle);
         LOG.log(Level.INFO, "Created user: " + pelleKey.toString());

         Blog java = new Blog();
         java.setTitle("I just java.");
         java.setContent("... and it was funny!");
         Key javaKey = store.create(pelleKey, java);
         LOG.log(Level.INFO, "Created blog: " + javaKey.toString());

         for (int i = 0; i < 3; i++)
         {
            Note note = new Note();
            note.setIndex((long) i);
            note.setContent(pelle.getFullname() + " : " + java.getTitle() + " : " + i);
            Key noteKey = store.create(javaKey, note);
            LOG.log(Level.INFO, "Created note: " + noteKey.toString());
         }

         Blog ada = new Blog();
         ada.setTitle("I just ada.");
         ada.setContent("... and it was not funny!");
         Key adaKey = store.create(pelleKey, ada);
         LOG.log(Level.INFO, "Created blog: " + adaKey.toString());

         for (int i = 0; i < 3; i++)
         {
            Note note = new Note();
            note.setIndex((long) i);
            note.setContent(pelle.getFullname() + " : " + ada.getTitle() + " : " + i);
            Key noteKey = store.create(adaKey, note);
            LOG.log(Level.INFO, "Created note: " + noteKey.toString());
         }

         Group linkan = new Group();
         linkan.setTitle("Linkoping");
         Key linkanKey = store.create(rootKey, linkan);
         LOG.log(Level.INFO, "Created group: " + linkanKey.toString());

         Event beer = new Event();
         beer.setTitle("Drink Beer");
         beer.setContent("For the king.");
         Key beerKey = store.create(linkanKey, beer);
         LOG.log(Level.INFO, "Created event: " + beerKey.toString());

         for (int i = 0; i < 4; i++)
         {
            Note note = new Note();
            note.setIndex((long) i);
            note.setContent(linkan.getTitle() + " : " + beer.getTitle() + " : " + i);
            Key noteKey = store.create(beerKey, note);
            LOG.log(Level.INFO, "Created note: " + noteKey.toString());
         }

         Event water = new Event();
         water.setTitle("Drink Water");
         water.setContent("For the queen.");
         Key waterKey = store.create(linkanKey, water);
         LOG.log(Level.INFO, "Created event: " + waterKey.toString());

         for (int i = 0; i < 4; i++)
         {
            Note note = new Note();
            note.setIndex((long) i);
            note.setContent(linkan.getTitle() + " : " + water.getTitle() + " : " + i);
            Key noteKey = store.create(waterKey, note);
            LOG.log(Level.INFO, "Created note: " + noteKey.toString());
         }

         Group norpan = new Group();
         norpan.setTitle("Norrkoping");
         Key norpanKey = store.create(rootKey, norpan);
         LOG.log(Level.INFO, "Created group: " + norpanKey.toString());

         Event meat = new Event();
         meat.setTitle("Eat meat");
         meat.setContent("For the people.");
         Key meatKey = store.create(norpanKey, meat);
         LOG.log(Level.INFO, "Created event: " + meatKey.toString());

         for (int i = 0; i < 4; i++)
         {
            Note note = new Note();
            note.setIndex((long) i);
            note.setContent(norpan.getTitle() + " : " + meat.getTitle() + " : " + i);
            Key noteKey = store.create(meatKey, note);
            LOG.log(Level.INFO, "Created note: " + noteKey);
         }

         Event fruit = new Event();
         fruit.setTitle(".öåä..Eat ( àèé ) fruit..ÅÄÖ.");
         fruit.setContent("For the animals.");
         Key fruitKey = store.create(norpanKey, fruit);
         LOG.log(Level.INFO, "Created event: " + fruitKey.toString());

         for (int i = 0; i < 4; i++)
         {
            Note note = new Note();
            note.setIndex((long) i);
            note.setContent(norpan.getTitle() + " : " + fruit.getTitle() + " : " + i);
            Key noteKey = store.create(fruitKey, note);
            LOG.log(Level.INFO, "Created note: " + noteKey.toString());
         }

         Blog swim = new Blog();
         swim.setTitle("I just swim.");
         swim.setContent("... and it was not funny!");
         Key swimKey = store.create(tobbeKey, swim);
         LOG.log(Level.INFO, "Created blog: " + swimKey.toString());

         for (int i = 0; i < 3; i++)
         {
            Note note = new Note();
            note.setIndex((long) i);
            note.setContent(tobbe.getFullname() + " : " + swim.getTitle() + " : " + i);
            Key noteKey = store.create(swimKey, note);
            LOG.log(Level.INFO, "Created note: " + noteKey.toString());
         }
      }
      catch (ConfigException e)
      {
         LOG.log(Level.WARNING, "Unable to install.", e);
      }
      catch (ContentNotUniqueException e)
      {
         LOG.log(Level.WARNING, "Unable to install.", e);
      }
      catch (ContentNotFoundException e)
      {
         LOG.log(Level.WARNING, "Unable to install.", e);
      }
   }
}
