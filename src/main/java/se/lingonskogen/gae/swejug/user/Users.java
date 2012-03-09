package se.lingonskogen.gae.swejug.user;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import se.lingonskogen.gae.swejug.rest.Beans;

@XmlRootElement
public class Users extends Beans
{
    public static final String KIND = Users.class.getSimpleName();

    private List<User> users = new ArrayList<User>();

    public List<User> getUsers()
    {
        return users;
    }

}
