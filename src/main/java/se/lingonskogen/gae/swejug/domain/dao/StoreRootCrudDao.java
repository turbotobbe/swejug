package se.lingonskogen.gae.swejug.domain.dao;

import se.lingonskogen.gae.swejug.domain.api.Root;
import se.lingonskogen.gae.swejug.domain.api.RootCrudDao;

import com.google.appengine.api.datastore.Entity;

public class StoreRootCrudDao extends StoreContentCrudDao<Root> implements RootCrudDao
{
    @Override
    protected String urlify(Root content)
    {
        return urlify(content.getName());
    }

    @Override
    protected void populate(Entity entity, Root content)
    {
        entity.setProperty(Root.NAME, content.getName());
    }
}
