package se.lingonskogen.gae.swejug.domain.dao;

import se.lingonskogen.gae.swejug.domain.api.User;
import se.lingonskogen.gae.swejug.domain.api.UserCrudDao;

import com.google.appengine.api.datastore.Entity;

public class StoreUserCrudDao extends StoreContentCrudDao<User> implements UserCrudDao
{
    @Override
    protected String urlify(User content)
    {
        return urlify(content.getFirstName() + " " + content.getLastName());
    }

    @Override
    protected void populate(Entity entity, User content)
    {
        entity.setProperty(User.FIRSTNAME, content.getFirstName());
        entity.setProperty(User.LASTNAME, content.getLastName());
    }
}
