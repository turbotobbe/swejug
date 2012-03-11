package se.lingonskogen.gae.swejug.content;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;

public class ContentStore<T>
{
    private static final Logger LOG = Logger.getLogger(ContentStore.class.getName());

    private static final String ROOT = "content";
    
    private ContentUrlKeyCreator<T> creator;
    
    private ContentConverter<T> converter;

    public ContentUrlKeyCreator<T> getContentUrlKeyCreator()
    {
        return this.creator;
    }

    public void setContentUrlKeyCreator(ContentUrlKeyCreator<T> creator)
    {
        this.creator = creator;
    }
    
    public ContentConverter<T> getContentConverter()
    {
        return this.converter;
    }
    
    public void setContentConverter(ContentConverter<T> converter)
    {
        this.converter = converter;
    }

    public T find(String[] urlkeys)
        throws ParentContentNotFoundException, ContentNotFoundException
    {
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        
        Key contentKey = makeKey(urlkeys);
        
        Entity contentEntity = null;
        try
        {
            contentEntity = store.get(contentKey);
        }
        catch (EntityNotFoundException e)
        {
            String msg = "Unable to find content. " + contentKey.toString();
            LOG.log(Level.INFO, msg);
            throw new ContentNotFoundException(msg, e);
        }
        
        ContentConverter<T> converter = getContentConverter();
        return converter.toContent(contentEntity);
    }
    
    public String create(T content, String... urlkeys)
            throws ParentContentNotFoundException, ContentNotUniqueException
    {
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        
        Key parentKey = makeKey(urlkeys);

        // get parent entity
        Entity parentEntity = null;
        try
        {
            parentEntity = store.get(parentKey);
        }
        catch (EntityNotFoundException e)
        {
            String msg = "Unable to find parent. " + parentKey.toString();
            LOG.log(Level.INFO, msg);
            throw new ParentContentNotFoundException(msg, e);
        }

        // check if entity already exists
        ContentUrlKeyCreator<T> creator = getContentUrlKeyCreator();
        String urlkey = creator.createUrlKey(content);
        Key contentKey = KeyFactory.createKey(parentKey, Content.KIND, urlkey);
        try
        {
            store.get(contentKey);
            String msg = "Content already exists. " + contentKey.toString();
            LOG.log(Level.INFO, msg);
            throw new ContentNotUniqueException(msg);
        }
        catch (EntityNotFoundException e)
        {
            // expected!
        }

        // create and store entity
        ContentConverter<T> converter = getContentConverter();
        Entity contentEntity = converter.toEntity(content, new Entity(contentKey));
        store.put(contentEntity);

        // update parent entity
        String propertyName = Content.PROP_TOTAL + contentEntity.getProperty(Content.KIND);
        Map<String, Object> properties = parentEntity.getProperties();
        Long total = 0L;
        if (properties.containsKey(propertyName))
        {
            total = (Long) parentEntity.getProperty(propertyName);
        }
        parentEntity.setProperty(propertyName, new Long(total + 1));
        store.put(parentEntity);
        
        return urlkey;
    }

    public void update(T content, String... urlkeys)
            throws ParentContentNotFoundException, ContentNotFoundException
    {
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        
        Key contentKey = makeKey(urlkeys);

        // get existing entity
        Entity contentEntity = null;
        try
        {
            contentEntity = store.get(contentKey);
        }
        catch (EntityNotFoundException e)
        {
            String msg = "Unable to find content. " + contentKey.toString();
            LOG.log(Level.INFO, msg);
            throw new ContentNotFoundException(msg, e);
        }

        // create and store entity
        ContentConverter<T> converter = getContentConverter();
        contentEntity = converter.toEntity(content, contentEntity);
        store.put(contentEntity);
    }

    public void delete(String... urlkeys)
            throws ParentContentNotFoundException, ContentNotFoundException
    {
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        
        Key contentKey = makeKey(urlkeys);

        // check if entity already exists
        Entity contentEntity = null;
        try
        {
            contentEntity = store.get(contentKey);
        }
        catch (EntityNotFoundException e)
        {
            String msg = "Unable to find content. " + contentKey.toString();
            LOG.log(Level.INFO, msg);
            throw new ContentNotFoundException(msg, e);
        }

        // delete the entity
        store.delete(contentKey);

        // update parent entity
        Key parentKey = contentKey.getParent();
        Entity parentEntity;
        try
        {
            parentEntity = store.get(parentKey);
        }
        catch (EntityNotFoundException e)
        {
            String msg = "Unable to find parent. " + parentKey.toString();
            LOG.log(Level.INFO, msg);
            throw new ParentContentNotFoundException(msg, e);
        }
        
        String propertyName = Content.PROP_TOTAL + contentEntity.getProperty(Content.PROP_TYPE);
        Map<String, Object> properties = parentEntity.getProperties();
        Long total = 1L;
        if (properties.containsKey(propertyName))
        {
            total = (Long) parentEntity.getProperty(propertyName);
        }
        parentEntity.setProperty(propertyName, new Long(total - 1));
        store.put(parentEntity);
    }

    private Key makeKey(String... urlkeys)
    {
        Builder builder = new KeyFactory.Builder(Content.KIND, ROOT);
        for (int i = 0; i < urlkeys.length; i++)
        {
            builder = builder.addChild(Content.KIND, urlkeys[i]);
        }
        return builder.getKey();
    }
}
