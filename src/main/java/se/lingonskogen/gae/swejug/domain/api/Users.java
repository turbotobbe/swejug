package se.lingonskogen.gae.swejug.domain.api;

import java.util.List;

public interface Users
{
    String USERS = "users";
    
    List<User> getUsers();
}
