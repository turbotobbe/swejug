package se.lingonskogen.gae.swejug.json;


public class ContentNotFoundException extends Exception
{
    private static final long serialVersionUID = 1L;

    public ContentNotFoundException(String msg, Throwable e)
    {
        super(msg, e);
    }

}
