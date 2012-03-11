package se.lingonskogen.gae.swejug.content;

public class ParentContentNotFoundException extends Exception
{
    private static final long serialVersionUID = 1L;

    public ParentContentNotFoundException(String msg, Throwable e)
    {
        super(msg, e);
    }

}
