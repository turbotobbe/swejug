package se.lingonskogen.gae.swejug.permit.model;

import java.util.Date;


public class Subject
{
   public static final String KIND = Subject.class.getSimpleName();
   
   public static final String DATE = "date";
   
   public static final String ROLE = "role";
   
   public static final String MAIL = "mail";
   
   public static final String PASS = "pass";
   
   public static final String TOKEN = "token";
   
   private Date date;
   
   private Role role;
   
   private String mail;
   
   private String pass;

   
   public Date getDate()
   {
      return date;
   }

   public void setDate(Date date)
   {
      this.date = date;
   }

   public Role getRole()
   {
      return role;
   }

   public void setRole(Role role)
   {
      this.role = role;
   }

   public String getMail()
   {
      return mail;
   }

   public void setMail(String mail)
   {
      this.mail = mail;
   }

   public String getPass()
   {
      return pass;
   }

   public void setPass(String pass)
   {
      this.pass = pass;
   }
   
}
