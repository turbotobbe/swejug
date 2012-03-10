package se.lingonskogen.gae.swejug.rest;

import java.util.List;

import se.lingonskogen.gae.swejug.err.KeyNotFoundException;
import se.lingonskogen.gae.swejug.err.KeyFoundException;
import se.lingonskogen.gae.swejug.err.ParentKeyNotFoundException;

import com.google.appengine.api.datastore.Key;

public interface BeanStore<T>
{
    static final String DEFAULT = "default";

    /**
     * Create a new bean.
     * 
     * @param bean
     *            The bean to create.
     * @param parent
     *            The parent key.
     * @return urlkey of the bean.
     * @throws ParentKeyNotFoundException
     *             If the parent does not exist.
     * @throws KeyFoundException
     *             If the bean already exists.
     */
    String create(T bean, Key parent)
            throws ParentKeyNotFoundException, KeyFoundException;

    /**
     * Update a bean.
     * 
     * @param parent
     *            The parent key.
     * @param urlkey
     *            The bean urlkey.
     * @param bean
     *            The bean.
     * @throws ParentKeyNotFoundException
     *             If the parent does not exist.
     * @throws KeyNotFoundException
     *             If the bean does not exist.
     */
    void update(String urlkey, T bean, Key parent)
            throws ParentKeyNotFoundException, KeyNotFoundException;

    /**
     * Delete a bean.
     * 
     * @param parent
     *            The parent key.
     * @param urlkey
     *            The bean urlkey.
     * @throws ParentKeyNotFoundException
     *             If the parent does not exist.
     * @throws KeyNotFoundException
     *             If the bean does not exist.
     */
    void delete(String urlkey, Key parent)
            throws ParentKeyNotFoundException, KeyNotFoundException;

    /**
     * Find a bean by its urlkey
     * 
     * @param parent
     *            The parent key.
     * @param urlkey
     *            The urlkey.
     * @return The bean.
     * @throws ParentKeyNotFoundException
     *             If the parent does not exist.
     * @throws KeyNotFoundException
     *             If the bean does not exist.
     */
    T findByKey(String urlkey, Key parent)
            throws ParentKeyNotFoundException, KeyNotFoundException;

    /**
     * Find all beans in the range.
     * 
     * @param limit
     *            Max number of beans
     * @param offset
     *            Start of range
     * @return list of beans
     * @throws ParentKeyNotFoundException
     *             If the parent does not exist.
     */
    List<T> findAll(Integer limit, Integer offset, Key parent)
            throws ParentKeyNotFoundException;
}
