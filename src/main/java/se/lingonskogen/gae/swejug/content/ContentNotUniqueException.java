package se.lingonskogen.gae.swejug.content;

public class ContentNotUniqueException extends Exception
{
    private static final long serialVersionUID = 1L;

    public ContentNotUniqueException(String msg)
    {
        super(msg);
    }

}
