package se.lingonskogen.gae.swejug.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 *    "type" : "Foo",
 *    "total" : { "Foo" : 2, "Bar" : 3 }
 *    "created" : { ... }
 *    "updated" : { ... }
 * }
 */
public class Meta
{
   public static final String TYPE = "type";
   
   public static final String TOTAL = "total";
   
   public static final String CREATED = "created";
   
   public static final String UPDATED = "updated";
   
   private final String type;
   
   private final Map<String, Long> total = new HashMap<String, Long>();

   private DateUser created;
   
   private DateUser updated;

   public Meta(String type)
   {
      this.type = type;
   }

   public String getType()
   {
      return type;
   }

   public Map<String, Long> getTotal()
   {
      return total;
   }

   public DateUser getCreated()
   {
      return created;
   }

   public void setCreated(DateUser created)
   {
      this.created = created;
   }

   public DateUser getUpdated()
   {
      return updated;
   }

   public void setUpdated(DateUser updated)
   {
      this.updated = updated;
   }

   @Override
   public String toString()
   {
      return "Meta [type=" + type + ", total=" + total + ", created=" + created + ", updated=" + updated + "]";
   }
}
