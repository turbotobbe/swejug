package se.lingonskogen.gae.swejug.modules.user;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import se.lingonskogen.gae.swejug.content.ContentJsonBuilder;
import se.lingonskogen.gae.swejug.content.JsonBuilder;

public class UserJsonBuilder extends ContentJsonBuilder<User> implements JsonBuilder<User>
{

   @Override
   public User buildContent(JSONObject json) throws JSONException
   {
      User user = new User();
      String firstname = json.getString(User.FIRSTNAME);
      if (firstname != null)
      {
         user.setFirstname(firstname);
      }
      String lastname = json.getString(User.LASTNAME);
      if (lastname != null)
      {
         user.setLastname(lastname);
      }
      return super.buildContent(json, user);
   }

   @Override
   public JSONObject buildJSONObject(User content) throws JSONException
   {
      JSONObject json = new JSONObject();
      String firstname = content.getFirstname();
      if (firstname != null)
      {
         json.put(User.FIRSTNAME, firstname);
      }
      String lastname = content.getLastname();
      if (lastname != null)
      {
         json.put(User.LASTNAME, lastname);
      }
      return super.buildJSONObject(json, content);
   }

}
