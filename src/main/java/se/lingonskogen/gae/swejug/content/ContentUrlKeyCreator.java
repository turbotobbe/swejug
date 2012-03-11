package se.lingonskogen.gae.swejug.content;


public interface ContentUrlKeyCreator<T>
{
    String createUrlKey(T content);
}
