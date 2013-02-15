package se.lingonskogen.gae.swejug.domain.dao;

import java.util.ArrayList;
import java.util.List;

import se.lingonskogen.gae.swejug.domain.api.Blog;
import se.lingonskogen.gae.swejug.domain.api.Comment;
import se.lingonskogen.gae.swejug.domain.api.Content;
import se.lingonskogen.gae.swejug.domain.api.ContentDao;
import se.lingonskogen.gae.swejug.domain.api.Event;
import se.lingonskogen.gae.swejug.domain.api.Group;
import se.lingonskogen.gae.swejug.domain.api.Root;
import se.lingonskogen.gae.swejug.domain.api.User;
import se.lingonskogen.gae.swejug.domain.bean.BlogBean;
import se.lingonskogen.gae.swejug.domain.bean.CommentBean;
import se.lingonskogen.gae.swejug.domain.bean.EventBean;
import se.lingonskogen.gae.swejug.domain.bean.GroupBean;
import se.lingonskogen.gae.swejug.domain.bean.RootBean;
import se.lingonskogen.gae.swejug.domain.bean.UserBean;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyFactory.Builder;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class StoreContentDao<T extends Content> implements ContentDao<T>
{
    @Override
    public T find(List<String> path)
    {
        Entity entity = null;
        try
        {
            Key key = makeKey(path);
            DatastoreService store = DatastoreServiceFactory.getDatastoreService();
            entity = store.get(key);
        }
        catch (EntityNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return build(entity);
    }

    public List<T> findAll(List<String> path)
    {
        Key key = makeKey(path);
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(Content.class.getSimpleName(), key);
        PreparedQuery preparedQuery = store.prepare(query);
        List<T> roots = new ArrayList<T>();
        for (Entity entity : preparedQuery.asIterable())
        {
            roots.add(build(entity));
        }
        return roots;
    }
    
    protected Key makeKey(List<String> path)
    {
        Builder builder = new KeyFactory.Builder(Content.class.getSimpleName(), path.get(0));
        for (int i = 1; i < path.size(); i++)
        {
            builder.addChild(Content.class.getSimpleName(), path.get(i));
        }
        return builder.getKey();
    }

    private T build(Entity entity)
    {
        String type = (String) entity.getProperty(Content.TYPE);
        if (Root.class.getSimpleName().equals(type))
        {
            RootBean bean = new RootBean();
            bean.setId(entity.getKey().getName());
            bean.setName((String) entity.getProperty(Root.NAME));
            return (T) bean;
        }
        else if (Group.class.getSimpleName().equals(type))
        {
            GroupBean bean = new GroupBean();
            bean.setId(entity.getKey().getName());
            bean.setName((String) entity.getProperty(Group.NAME));
            return (T) bean;
        }
        else if (User.class.getSimpleName().equals(type))
        {
            UserBean bean = new UserBean();
            bean.setId(entity.getKey().getName());
            bean.setFirstName((String) entity.getProperty(User.FIRSTNAME));
            bean.setLastName((String) entity.getProperty(User.LASTNAME));
            return (T) bean;
        }
        else if (Event.class.getSimpleName().equals(type))
        {
            EventBean bean = new EventBean();
            bean.setId(entity.getKey().getName());
            bean.setTitle((String) entity.getProperty(Event.TITLE));
            bean.setSummary((String) entity.getProperty(Event.SUMMARY));
            bean.setContent((String) entity.getProperty(Event.CONTENT));
            return (T) bean;
        }
        else if (Blog.class.getSimpleName().equals(type))
        {
            BlogBean bean = new BlogBean();
            bean.setId(entity.getKey().getName());
            bean.setTitle((String) entity.getProperty(Blog.TITLE));
            bean.setSummary((String) entity.getProperty(Blog.SUMMARY));
            bean.setContent((String) entity.getProperty(Blog.CONTENT));
            return (T) bean;
        }
        else if (Comment.class.getSimpleName().equals(type))
        {
            CommentBean bean = new CommentBean();
            bean.setId(entity.getKey().getName());
            bean.setIndex((Long) entity.getProperty(Comment.INDEX));
            bean.setContent((String) entity.getProperty(Comment.CONTENT));
            return (T) bean;
        }
        return null;
    }

}
