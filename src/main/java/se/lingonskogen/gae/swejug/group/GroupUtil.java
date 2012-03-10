package se.lingonskogen.gae.swejug.group;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class GroupUtil
{
    public Entity toEntity(Key key, Group group)
    {
        Entity entity = new Entity(key);
        entity.setProperty(Group.PROP_NAME, group.getName());
        return entity;
    }

    public Group toBean(Entity entity)
    {
        Group group = new Group();
        group.setKind(Group.KIND);
        group.setName((String) entity.getProperty(Group.PROP_NAME));
        return group;
    }

    public String createUrlkey(Group group)
    {
        return group.getName().trim().toLowerCase().replaceAll("[^a-zA-Z0-9]", "-");
    }

    public Key createKey(Key parent, String urlkey)
    {
        return KeyFactory.createKey(parent, Group.KIND, urlkey);
    }

}
