package se.lingonskogen.gae.swejug.beans;

import com.google.appengine.api.datastore.Key;

/**
 * {
 *    "meta" : { ... }
 *    "links" : [ ... ]
 * }
 */
public abstract class Content
{
   public static final String KIND = "Content";

   public static final String META = "meta";
   
   public static final String PARENT = "parent";
   
   public static final String LINK = "link";

   /* helper constants */
   public static final String META_TYPE;

   public static final String META_TOTAL;
   
   public static final String META_CREATED_DATE;
   
   public static final String META_CREATED_USER;
   
   public static final String META_UPDATED_DATE;
   
   public static final String META_UPDATED_USER;

   static
   {
      PropertyBuilder pb = new PropertyBuilder();
      
      META_TYPE = pb.clear(Content.META).add(Meta.TYPE).build();
      META_TOTAL = pb.clear(Content.META).add(Meta.TOTAL).build();
      META_CREATED_DATE= pb.clear(Content.META).add(Meta.CREATED).add(DateUser.DATE).build();
      META_CREATED_USER= pb.clear(Content.META).add(Meta.CREATED).add(DateUser.USER).build();
      META_UPDATED_DATE= pb.clear(Content.META).add(Meta.UPDATED).add(DateUser.DATE).build();
      META_UPDATED_USER= pb.clear(Content.META).add(Meta.UPDATED).add(DateUser.USER).build();
   }
   
   private final Meta meta;
   
   private Link link;
   
   private Content parent;
   
   private Key key;
   
   public Content(String type)
   {
      this.meta = new Meta(type);
   }

   public Meta getMeta()
   {
      return meta;
   }

   public Link getLink()
   {
      return link;
   }

   public void setLink(Link link)
   {
      this.link = link;
   }

   public Content getParent()
   {
      return parent;
   }

   public void setParent(Content parent)
   {
      this.parent = parent;
   }

   public Key getKey()
   {
      return key;
   }

   public void setKey(Key key)
   {
      this.key = key;
   }

   public abstract String getLabel();

}
