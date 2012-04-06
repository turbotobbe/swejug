package se.lingonskogen.gae.swejug.modules.note;

import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.content.ContentEntityBuilder;
import se.lingonskogen.gae.swejug.content.ContentStore;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;

import com.google.appengine.api.datastore.Entity;

public class NoteEntityBuilder extends ContentEntityBuilder<Note>
{
   @Override
   public Note buildContent(ContentStore store, Entity entity, boolean resolve) throws ConfigException, ContentNotFoundException
   {
      Note note = new Note();
      note.setIndex((Long) entity.getProperty(Note.INDEX));
      note.setContent((String) entity.getProperty(Note.CONTENT));
      return super.buildContent(store, entity, note, resolve);
   }

   @Override
   public Entity buildEntity(Note note, Entity entity)
   {
      entity = super.buildEntity(note, entity);
      entity.setProperty(Note.INDEX, note.getIndex());
      entity.setProperty(Note.CONTENT, note.getContent());
      return entity;
   }

}
