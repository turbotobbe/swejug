package se.lingonskogen.gae.swejug.account.err;

import org.codehaus.jettison.json.JSONException;

public class UnexpectedJsonException extends RuntimeException
{
   private static final long serialVersionUID = 1L;

   public UnexpectedJsonException(String message, JSONException cause)
   {
      super(message, cause);
   }

}
