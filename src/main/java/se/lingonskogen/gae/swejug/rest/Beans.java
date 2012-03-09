package se.lingonskogen.gae.swejug.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Beans extends Bean
{
   public static final String PROP_TOTAL = "total";

   private Integer total = 0;

   private Integer count = 0;

   public Integer getTotal()
   {
      return total;
   }

   public void setTotal(Integer total)
   {
      this.total = total;
   }

   public Integer getCount()
   {
      return count;
   }

   public void setCount(Integer count)
   {
      this.count = count;
   }

}
