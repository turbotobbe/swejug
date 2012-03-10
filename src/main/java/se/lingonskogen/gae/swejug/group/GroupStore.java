package se.lingonskogen.gae.swejug.group;

import java.util.ArrayList;
import java.util.List;

import se.lingonskogen.gae.swejug.err.KeyFoundException;
import se.lingonskogen.gae.swejug.err.KeyNotFoundException;
import se.lingonskogen.gae.swejug.err.ParentKeyNotFoundException;
import se.lingonskogen.gae.swejug.rest.AbstractBeanStore;
import se.lingonskogen.gae.swejug.rest.BeanStore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class GroupStore extends AbstractBeanStore implements BeanStore<Group>
{
    private GroupUtil util = new GroupUtil();
    
    public GroupUtil getUtil()
    {
        return util;
    }

    @Override
    public String create(Group group, Key parent)
        throws ParentKeyNotFoundException, KeyFoundException
    {
        assertParentExists(parent);
        String urlkey = getUtil().createUrlkey(group);
        Key key = getUtil().createKey(parent, urlkey);
        assertKeyDontExists(key);
        Entity entity = getUtil().toEntity(key, group);
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        store.put(entity);
        return urlkey;
    }

    @Override
    public void update(String urlkey, Group group, Key parent)
        throws ParentKeyNotFoundException, KeyNotFoundException
    {
        assertParentExists(parent);
        Key key = getUtil().createKey(parent, urlkey);
        assertKeyExists(key);
        Entity entity = getUtil().toEntity(key, group);
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        store.put(entity);
    }

    @Override
    public void delete(String urlkey, Key parent) throws ParentKeyNotFoundException,
            KeyNotFoundException
    {
        assertParentExists(parent);
        Key key = getUtil().createKey(parent, urlkey);
        assertKeyExists(key);
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        store.delete(key);
    }

    @Override
    public Group findByKey(String urlkey, Key parent) throws ParentKeyNotFoundException,
            KeyNotFoundException
    {
        assertParentExists(parent);
        Key key = getUtil().createKey(parent, urlkey);
        //assertKeyExists(key);
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        Entity entity = null;
        try
        {
            entity = store.get(key);
        }
        catch (EntityNotFoundException e)
        {
            throw new KeyNotFoundException();
        }
        return getUtil().toBean(entity);
    }

    @Override
    public List<Group> findAll(Integer limit, Integer offset, Key parent)
            throws ParentKeyNotFoundException
    {
        List<Group> list = new ArrayList<Group>();
        assertParentExists(parent);
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        Query query = parent == null ? new Query(Group.KIND) : new Query(Group.KIND, parent);
        PreparedQuery prepare = store.prepare(query);
        FetchOptions options = FetchOptions.Builder.withDefaults();
        if (limit != null)
        {
            options = options.limit(limit);
        }
        if (offset != null)
        {
            options = options.offset(offset);
        }
        for (Entity entity : prepare.asIterable(options))
        {
            Group group = getUtil().toBean(entity);
            list.add(group);
        }
        return list;
    }

}
