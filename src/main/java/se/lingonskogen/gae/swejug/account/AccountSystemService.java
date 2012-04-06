package se.lingonskogen.gae.swejug.account;

import se.lingonskogen.gae.swejug.account.model.Account;
import se.lingonskogen.gae.swejug.system.SystemService;
import se.lingonskogen.gae.swejug.system.model.Root;

public interface AccountSystemService extends SystemService
{
   boolean installAccount(Root root, Account account);
}
