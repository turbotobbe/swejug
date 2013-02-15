package se.lingonskogen.gae.swejug.domain.dao;

import se.lingonskogen.gae.swejug.domain.api.Blog;
import se.lingonskogen.gae.swejug.domain.api.BlogCrudDao;

import com.google.appengine.api.datastore.Entity;

public class StoreBlogCrudDao extends StoreContentCrudDao<Blog> implements BlogCrudDao
{
    @Override
    protected String urlify(Blog content)
    {
        return urlify(content.getTitle());
    }

    @Override
    protected void populate(Entity entity, Blog content)
    {
        entity.setProperty(Blog.TITLE, content.getTitle());
        entity.setProperty(Blog.SUMMARY, content.getSummary());
        entity.setProperty(Blog.CONTENT, content.getContent());
    }
}
