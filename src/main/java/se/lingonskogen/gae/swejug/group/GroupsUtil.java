package se.lingonskogen.gae.swejug.group;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class GroupsUtil
{
    public Entity toEntity(Key key, Groups groups)
    {
        Entity entity = new Entity(key);
        entity.setProperty(Groups.PROP_TOTAL, groups.getTotal());
        return entity;
    }

    public Groups toBean(Entity entity)
    {
        Groups groups = new Groups();
        groups.setKind(Groups.KIND);
        groups.setTotal((Long) entity.getProperty(Groups.PROP_TOTAL));
        return groups;
    }

    public String createUrlkey(Groups groups)
    {
        return "default";
    }

    public Key createKey(Key parent, String urlkey)
    {
        if (parent == null)
        {
            return KeyFactory.createKey(Groups.KIND, urlkey);
        }
        else
        {
            return KeyFactory.createKey(parent, Groups.KIND, urlkey);
        }
    }

}
