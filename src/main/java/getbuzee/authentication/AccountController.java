package getbuzee.authentication;

import getbuzee.entity.Person;
import getbuzee.exception.CatchException;
import getbuzee.security.LoggedIn;
import getbuzee.service.PersonService;
import getbuzee.web.Credentials;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import utils.Loggable;
import getbuzee.web.Controller;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Named
@SessionScoped
@Loggable
@CatchException
public class AccountController extends Controller implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private PersonService personService;

    @Inject
    private Credentials credentials;

    @Inject
    private Conversation conversation;

    @Produces
    @LoggedIn
    private Person loggedInPerson;

    @Inject
    @SessionScoped
    private transient LoginContext loginContext;

    // ======================================
    // =              Public Methods        =
    // ======================================

    public String doLogin() throws LoginException {
        if ("".equals(credentials.getLogin())) {
            addWarningMessage("id_filled");
            return null;
        }
        if ("".equals(credentials.getPassword())) {
            addWarningMessage("pwd_filled");
            return null;
        }

        loginContext.login();
        loggedInPerson = personService.findPersonByLogin(credentials.getLogin());
        return "main";
    }

    public String doCreateNewAccount() {

        // Login has to be unique
        if (personService.doesLoginAlreadyExist(credentials.getLogin())) {
            addWarningMessage("login_exists");
            return null;
        }

        // Id and password must be filled
        if ("".equals(credentials.getLogin()) || "".equals(credentials.getPassword()) || "".equals(credentials.getPassword2())) {
            addWarningMessage("id_pwd_filled");
            return null;
        } else if (!credentials.getPassword().equals(credentials.getPassword2())) {
            addWarningMessage("both_pwd_same");
            return null;
        }

        // Login and password are ok
        loggedInPerson = new Person();
        loggedInPerson.setLogin(credentials.getLogin());
        loggedInPerson.setPassword(credentials.getPassword());
        doCreatePerson();

        return "succes";
    }

    public String doCreatePerson() {
        loggedInPerson = personService.createPerson(loggedInPerson);
        return "main.faces";
    }


    public String doLogout() {
        loggedInPerson = null;
        // Stop conversation
        if (!conversation.isTransient()) {
            conversation.end();
        }
        addInformationMessage("been_loggedout");
        return "main.faces";
    }

    public String doUpdateAccount() {
        loggedInPerson = personService.updatePerson(loggedInPerson);
        addInformationMessage("account_updated");
        return "showaccount.faces";
    }

    public boolean isLoggedIn() {
        return loggedInPerson != null;
    }

    public Person getloggedInPerson() {
        return loggedInPerson;
    }

    public void setloggedInPerson(Person loggedInPerson) {
        this.loggedInPerson = loggedInPerson;
    }
}
