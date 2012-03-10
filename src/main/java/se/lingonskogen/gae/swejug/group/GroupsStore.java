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
import com.google.appengine.api.datastore.Key;

public class GroupsStore extends AbstractBeanStore implements BeanStore<Groups>
{
    private GroupsUtil util = new GroupsUtil();

    public GroupsUtil getUtil()
    {
        return util;
    }

    @Override
    public String create(Groups groups, Key parent)
            throws ParentKeyNotFoundException, KeyFoundException
    {
        assertParentExists(parent);
        String urlkey = getUtil().createUrlkey(groups);
        Key key = getUtil().createKey(parent, urlkey);
        assertKeyDontExists(key);
        Entity entity = getUtil().toEntity(key, groups);
        DatastoreService store = DatastoreServiceFactory.getDatastoreService();
        store.put(entity);
        return urlkey;
    }

    @Override
    public void update(String urlkey, Groups groups, Key parent)
            throws ParentKeyNotFoundException, KeyNotFoundException
    {
        assertParentExists(parent);
        Key key = getUtil().createKey(parent, urlkey);
        assertKeyExists(key);
        Entity entity = getUtil().toEntity(key, groups);
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
    public Groups findByKey(String urlkey, Key parent) throws ParentKeyNotFoundException,
            KeyNotFoundException
    {
        assertParentExists(parent);
        Key key = getUtil().createKey(parent, urlkey);
        // assertKeyExists(key);
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
    public List<Groups> findAll(Integer limit, Integer offset, Key parent)
            throws ParentKeyNotFoundException
    {
        List<Groups> list = new ArrayList<Groups>();
        return list;
    }

}
