package se.lingonskogen.gae.swejug.content;

import com.google.appengine.api.datastore.Key;

public interface UrlBuilder
{
   String build(Key key);
}
