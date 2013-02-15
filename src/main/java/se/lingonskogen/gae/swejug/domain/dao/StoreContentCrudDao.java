package se.lingonskogen.gae.swejug.domain.dao;

import java.util.List;

import se.lingonskogen.gae.swejug.domain.api.Content;
import se.lingonskogen.gae.swejug.domain.api.ContentCrudDao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public abstract class StoreContentCrudDao<T extends Content> extends StoreContentDao<T> implements ContentCrudDao<T>
{
    protected abstract String urlify(T content);

    protected abstract void populate(Entity entity, T content);

    @Override
    public String create(List<String> path, T content)
    {
        path.add(urlify(content));
        Key key = makeKey(path);
        Entity entity = new Entity(key);
        populate(entity, content);
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        store.put(entity);
        return key.getName();
    }

    @Override
    public void update(List<String> path, T content)
    {
        try
        {
            Key key = makeKey(path);
            DatastoreService store = DatastoreServiceFactory.getDatastoreService();
            Entity entity = store.get(key);
            populate(entity, content);
            store.put(entity);
        }
        catch (EntityNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void delete(List<String> path)
    {
        Key key = makeKey(path);
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        store.delete(key);
    }
    
    protected String urlify(String text)
    {
        return text;
    }
}
