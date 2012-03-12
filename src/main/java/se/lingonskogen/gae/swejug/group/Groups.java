package se.lingonskogen.gae.swejug.group;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import se.lingonskogen.gae.swejug.rest.Beans;

@XmlRootElement
public class Groups extends Beans
{
    public static final String KIND = Groups.class.getSimpleName();
    
    private List<Group> groups = new ArrayList<Group>();

    public List<Group> getGroups()
    {
        return groups;
    }
    
}
