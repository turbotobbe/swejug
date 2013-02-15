package se.lingonskogen.gae.swejug.domain.dao;

import se.lingonskogen.gae.swejug.domain.api.Comment;
import se.lingonskogen.gae.swejug.domain.api.CommentCrudDao;

import com.google.appengine.api.datastore.Entity;

public class StoreCommentCrudDao extends StoreContentCrudDao<Comment> implements CommentCrudDao
{
    @Override
    protected String urlify(Comment content)
    {
        return urlify(content.getIndex().toString());
    }

    @Override
    protected void populate(Entity entity, Comment content)
    {
        entity.setProperty(Comment.INDEX, content.getIndex());
        entity.setProperty(Comment.CONTENT, content.getContent());
    }
}
