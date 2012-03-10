package se.lingonskogen.gae.swejug.group;

import java.util.logging.Level;
import java.util.logging.Logger;

import se.lingonskogen.gae.swejug.Installer;
import se.lingonskogen.gae.swejug.err.KeyFoundException;
import se.lingonskogen.gae.swejug.err.ParentKeyNotFoundException;

public class GroupInstaller implements Installer
{
    private static final Logger LOG = Logger.getLogger(GroupInstaller.class.getName());
    
    @Override
    public void install()
    {
        GroupsStore groupsStore = new GroupsStore();
        Groups groups = new Groups();
        groups.setKind(Groups.KIND);
        try
        {
            groupsStore.create(groups, null);
            LOG.log(Level.INFO, "Created " + groups.toString());
        }
        catch (ParentKeyNotFoundException e)
        {
            LOG.log(Level.INFO, "Skipping creating " + groups.toString());
        }
        catch (KeyFoundException e)
        {
            LOG.log(Level.INFO, "Skipping creating " + groups.toString());
        }
    }

}
