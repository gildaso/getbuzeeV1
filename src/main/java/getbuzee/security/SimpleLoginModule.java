package getbuzee.security;

import getbuzee.entity.Person;
import getbuzee.service.PersonService;

import java.util.Map;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

/**
 * @author blep
 *         Date: 12/02/12
 *         Time: 11:59
 */

public class SimpleLoginModule implements LoginModule {

    // ======================================
    // =             Attributes             =
    // ======================================

    private CallbackHandler callbackHandler;

    private PersonService personService;

    private BeanManager beanManager;

    // ======================================
    // =          Business methods          =
    // ======================================

    private PersonService getPersonService() {
        if (personService != null) {
            return personService;
        }
        try {
            Context context = new InitialContext();
            beanManager = (BeanManager) context.lookup("java:comp/BeanManager");
            Bean<?> bean = beanManager.getBeans(PersonService.class).iterator().next();
            CreationalContext cc = beanManager.createCreationalContext(bean);
            personService = (PersonService) beanManager.getReference(bean, PersonService.class, cc);

        } catch (NamingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return personService;

    }

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> stringMap, Map<String, ?> stringMap1) {
        this.callbackHandler = callbackHandler;
        getPersonService();
    }

    @Override
    public boolean login() throws LoginException {

        NameCallback nameCallback = new NameCallback("Name : ");
        PasswordCallback passwordCallback = new PasswordCallback("Password : ", false);
        try {
            callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
            String username = nameCallback.getName();
            String password = new String(passwordCallback.getPassword());
            nameCallback.setName("");
            passwordCallback.clearPassword();
            Person person = personService.findPersonByLoginPassword(username, password);

            if (person == null) {
                throw new LoginException("Authentication failed");
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginException(e.getMessage());
        }
    }

    @Override
    public boolean commit() throws LoginException {
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        return true;
    }
}
