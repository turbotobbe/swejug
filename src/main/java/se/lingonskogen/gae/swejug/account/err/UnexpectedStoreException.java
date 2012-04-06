package se.lingonskogen.gae.swejug.account.err;

import com.google.appengine.api.datastore.EntityNotFoundException;

public class UnexpectedStoreException extends RuntimeException
{
   private static final long serialVersionUID = 1L;

   public UnexpectedStoreException(String message)
   {
      super(message);
   }

   public UnexpectedStoreException(String message, EntityNotFoundException cause)
   {
      super(message, cause);
   }

}
