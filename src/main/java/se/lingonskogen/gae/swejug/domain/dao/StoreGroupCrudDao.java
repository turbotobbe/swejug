package se.lingonskogen.gae.swejug.domain.dao;

import se.lingonskogen.gae.swejug.domain.api.Group;
import se.lingonskogen.gae.swejug.domain.api.GroupCrudDao;

import com.google.appengine.api.datastore.Entity;

public class StoreGroupCrudDao extends StoreContentCrudDao<Group> implements GroupCrudDao
{
    @Override
    protected String urlify(Group content)
    {
        return urlify(content.getName());
    }

    @Override
    protected void populate(Entity entity, Group content)
    {
        entity.setProperty(Group.NAME, content.getName());
    }
}
