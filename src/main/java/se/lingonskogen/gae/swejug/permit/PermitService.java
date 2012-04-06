package se.lingonskogen.gae.swejug.permit;

import se.lingonskogen.gae.swejug.permit.model.Permit;
import se.lingonskogen.gae.swejug.permit.model.Subject;

public interface PermitService
{
   /**
    * Grant a permit.
    * 
    * @param permit
    *           The permit
    */
   void grantPermit(Permit permit);

   /**
    * Revoke a permit
    * 
    * @param permit
    *           The permit
    */
   void revokePermit(Permit permit);

   /**
    * Register a new subject. This will be inactive.
    * 
    * @param subject
    *           The subject.
    * @return The activation token
    */
   String register(Subject subject);

   /**
    * Activate a registered subject.
    * 
    * @param token
    *           The activation token.
    */
   void activate(String token);

   /**
    * Login the subject.
    * 
    * @param subject
    *           The subject
    * @return An access token
    */
   String login(Subject subject);

   /**
    * Check if the subject has a permit.
    * 
    * @param token
    *           The access token
    * @param permit
    *           The permit
    * @return true if the subject has a permit.
    */
   boolean hasPermit(String token, Permit permit);

}
