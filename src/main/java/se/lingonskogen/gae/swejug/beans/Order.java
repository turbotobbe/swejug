package se.lingonskogen.gae.swejug.beans;

import com.google.appengine.api.datastore.Query.SortDirection;

public class Order
{
   public final String property;
   
   public final SortDirection direction;
   
   public Order(String property)
   {
      this(property, SortDirection.ASCENDING); // a...z, 0..9, 2001..2012
   }
   
   public Order(String property, SortDirection direction)
   {
      this.property = property;
      this.direction = direction;
   }
}
