package se.lingonskogen.gae.swejug.json;

public class ContentNotUniqueException extends Exception
{
    private static final long serialVersionUID = 1L;

    public ContentNotUniqueException(String msg)
    {
        super(msg);
    }

}
