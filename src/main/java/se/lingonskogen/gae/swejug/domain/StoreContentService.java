package se.lingonskogen.gae.swejug.domain;

import java.util.List;

import se.lingonskogen.gae.swejug.domain.api.Blog;
import se.lingonskogen.gae.swejug.domain.api.BlogCrudDao;
import se.lingonskogen.gae.swejug.domain.api.Comment;
import se.lingonskogen.gae.swejug.domain.api.CommentCrudDao;
import se.lingonskogen.gae.swejug.domain.api.Content;
import se.lingonskogen.gae.swejug.domain.api.ContentDao;
import se.lingonskogen.gae.swejug.domain.api.Event;
import se.lingonskogen.gae.swejug.domain.api.EventCrudDao;
import se.lingonskogen.gae.swejug.domain.api.Group;
import se.lingonskogen.gae.swejug.domain.api.GroupCrudDao;
import se.lingonskogen.gae.swejug.domain.api.Root;
import se.lingonskogen.gae.swejug.domain.api.User;
import se.lingonskogen.gae.swejug.domain.api.UserCrudDao;
import se.lingonskogen.gae.swejug.domain.dao.StoreBlogCrudDao;
import se.lingonskogen.gae.swejug.domain.dao.StoreCommentCrudDao;
import se.lingonskogen.gae.swejug.domain.dao.StoreContentDao;
import se.lingonskogen.gae.swejug.domain.dao.StoreEventCrudDao;
import se.lingonskogen.gae.swejug.domain.dao.StoreGroupCrudDao;
import se.lingonskogen.gae.swejug.domain.dao.StoreUserCrudDao;

public class StoreContentService implements ContentService
{
    //private RootCrudDao rootCrudDao = new StoreRootCrudDao();
    private GroupCrudDao groupCrudDao = new StoreGroupCrudDao();
    private UserCrudDao userCrudDao = new StoreUserCrudDao();
    private EventCrudDao eventCrudDao = new StoreEventCrudDao();
    private BlogCrudDao blogCrudDao = new StoreBlogCrudDao();
    private CommentCrudDao commentCrudDao = new StoreCommentCrudDao();
    
    @Override
    public Content find(List<String> path)
    {
        ContentDao<Content> contentDao = new StoreContentDao<Content>();
        Content content = contentDao.find(path);
        String type = content.getType();
        if (Root.class.getSimpleName().equals(type))
        {
            ((Root)content).getGroups().addAll(groupCrudDao.findAll(path));
            ((Root)content).getUsers().addAll(userCrudDao.findAll(path));
        }
        else if (Group.class.getSimpleName().equals(type))
        {
            ((Group)content).getEvents().addAll(eventCrudDao.findAll(path));
        }
        else if (User.class.getSimpleName().equals(type))
        {
            ((User)content).getBlogs().addAll(blogCrudDao.findAll(path));
        }
        else if (Event.class.getSimpleName().equals(type))
        {
            ((Event)content).getComments().addAll(commentCrudDao.findAll(path));
        }
        else if (Blog.class.getSimpleName().equals(type))
        {
            ((Blog)content).getComments().addAll(commentCrudDao.findAll(path));
        }
        else if (Comment.class.getSimpleName().equals(type))
        {
        }

        return content;
    }
}
