package se.lingonskogen.gae.swejug.domain.dao;

import se.lingonskogen.gae.swejug.domain.api.Event;
import se.lingonskogen.gae.swejug.domain.api.EventCrudDao;

import com.google.appengine.api.datastore.Entity;

public class StoreEventCrudDao extends StoreContentCrudDao<Event> implements EventCrudDao
{
    @Override
    protected String urlify(Event content)
    {
        return urlify(content.getTitle());
    }

    @Override
    protected void populate(Entity entity, Event content)
    {
        entity.setProperty(Event.TITLE, content.getTitle());
        entity.setProperty(Event.SUMMARY, content.getSummary());
        entity.setProperty(Event.CONTENT, content.getContent());
    }
}
