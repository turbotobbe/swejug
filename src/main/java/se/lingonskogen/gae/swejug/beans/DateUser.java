package se.lingonskogen.gae.swejug.beans;

import java.util.Date;

import se.lingonskogen.gae.swejug.modules.user.User;

/**
 * 
 * {
 *    "date" : "2011-01-01 11:11:11",
 *    "user" : {...}
 * }
 *
 */
public class DateUser
{
   public static final String DATE = "date";

   public static final String USER = "user";
   
   private Date date;
   
   private User user;

   public Date getDate()
   {
      return date;
   }

   public void setDate(Date date)
   {
      this.date = date;
   }

   public User getUser()
   {
      return user;
   }

   public void setUser(User user)
   {
      this.user = user;
   }

}
