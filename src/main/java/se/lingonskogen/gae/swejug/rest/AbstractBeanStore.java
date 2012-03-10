package se.lingonskogen.gae.swejug.rest;

import se.lingonskogen.gae.swejug.err.KeyFoundException;
import se.lingonskogen.gae.swejug.err.KeyNotFoundException;
import se.lingonskogen.gae.swejug.err.ParentKeyNotFoundException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class AbstractBeanStore
{
    public void assertParentExists(Key parent) throws ParentKeyNotFoundException
    {
        if (parent != null)
        {
            DatastoreService store = DatastoreServiceFactory.getDatastoreService();
            try
            {
                store.get(parent);
                // ok!
            }
            catch (EntityNotFoundException e)
            {
                throw new ParentKeyNotFoundException();
            }
        }
    }

    public void assertKeyExists(Key key) throws KeyNotFoundException
    {
        if (key != null)
        {
            DatastoreService store = DatastoreServiceFactory.getDatastoreService();
            try
            {
                store.get(key);
                // ok!
            }
            catch (EntityNotFoundException e)
            {
                throw new KeyNotFoundException();
            }
        }
    }

    public void assertKeyDontExists(Key key) throws KeyFoundException
    {
        if (key != null)
        {
            DatastoreService store = DatastoreServiceFactory.getDatastoreService();
            try
            {
                store.get(key);
                throw new KeyFoundException();
            }
            catch (EntityNotFoundException e)
            {
                // ok!
            }
        }
    }

}
