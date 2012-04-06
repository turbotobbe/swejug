package se.lingonskogen.gae.swejug.account;

import se.lingonskogen.gae.swejug.account.err.AccountNotFoundException;
import se.lingonskogen.gae.swejug.account.model.Account;
import se.lingonskogen.gae.swejug.account.model.Credential;
import se.lingonskogen.gae.swejug.account.model.Tokens;
import se.lingonskogen.gae.swejug.system.model.Tupel;

public interface AccountService
{
   String ACCESS_TOKEN = "access-token";

   /**
    * Login using an activated account. If refresh is <code>true</code> the
    * account refresh token will be returned. If access is <code>true</code> the
    * account access token will be generated and returned. If refresh and access
    * is <code>false</code>, no tokens will be returned.
    * 
    * @param credential
    *           The credentials
    * @param refresh
    *           true if requesting a refresh token
    * @param access
    *           true if requesting an access token
    * @return The requested tokens
    * @throws AccountNotFoundException
    */
   Tokens login(Credential credential, Boolean refresh, Boolean access)
         throws AccountNotFoundException;

   /**
    * Clear the access token from cache.
    * 
    * @param accessToken
    *           The access token
    * @return <code>true</code> if cache was modified, <code>false</code>
    *         otherwise.
    */
   boolean clear(String accessToken);

   /**
    * Authenticate using an tokens. If the access token is not <code>null</code>
    * the service will first try to authenticate against the cached access
    * token. If this fails of the access token is <code>null</code> the service
    * will try to authenticate against the store. If the refresh token is
    * <code>null</code> and the access token is <code>null</code> of the access
    * token authentication fails service will fail to authenticate. If
    * authentication with the refresh token succeeds, a new access token will be
    * generated and returned.
    * 
    * @param tokens
    *           The tokens
    * @return The account and optionally a new access token
    * @throws AccountNotFoundException
    */
   Tupel<Account, String> authenticate(Tokens tokens) throws AccountNotFoundException;
}
