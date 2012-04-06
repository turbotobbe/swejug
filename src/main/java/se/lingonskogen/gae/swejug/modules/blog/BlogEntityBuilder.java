package se.lingonskogen.gae.swejug.modules.blog;

import se.lingonskogen.gae.swejug.config.ConfigException;
import se.lingonskogen.gae.swejug.content.ContentEntityBuilder;
import se.lingonskogen.gae.swejug.content.ContentStore;
import se.lingonskogen.gae.swejug.content.err.ContentNotFoundException;

import com.google.appengine.api.datastore.Entity;

public class BlogEntityBuilder extends ContentEntityBuilder<Blog>
{
   @Override
   public Blog buildContent(ContentStore store, Entity entity, boolean resolve) throws ConfigException, ContentNotFoundException
   {
      Blog blog = new Blog();
      blog.setTitle((String) entity.getProperty(Blog.TITLE));
      blog.setContent((String) entity.getProperty(Blog.CONTENT));
      return super.buildContent(store, entity, blog, resolve);
   }
   
   @Override
   public Entity buildEntity(Blog blog, Entity entity)
   {
      entity = super.buildEntity(blog, entity);
      entity.setProperty(Blog.TITLE, blog.getTitle());
      entity.setProperty(Blog.CONTENT, blog.getContent());
      return entity;
   }
}
