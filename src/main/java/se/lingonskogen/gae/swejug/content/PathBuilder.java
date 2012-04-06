package se.lingonskogen.gae.swejug.content;

import java.util.List;

import se.lingonskogen.gae.swejug.beans.Content;
import se.lingonskogen.gae.swejug.beans.Link;

public interface PathBuilder
{
   List<Link> build(Content content);
}
