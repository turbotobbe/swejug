package se.lingonskogen.gae.swejug.domain.api;

import java.util.List;

public interface Blogs
{
    String BLOGS = "blogs";
    
    List<Blog> getBlogs();
}
