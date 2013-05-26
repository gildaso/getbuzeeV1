package getbuzee.web;

import getbuzee.authentication.AccountController;
import getbuzee.entity.Person;
import getbuzee.exception.CatchException;
import getbuzee.service.PersonService;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import utils.Loggable;

@Named
//@RequestScoped TODO should be request scoped
@SessionScoped
@Loggable
@CatchException
public class PersonManager  implements Serializable{
	
    @Inject
    private PersonService personService;
    
    @Inject
    private AccountController accountController;
    
    private Person currentPerson;
    
    private List<Person> allPersons;
    
    private List<Person> myFriends;
    
    public List<Person> findAllPersons(){
    	this.allPersons = personService.findAllPersons();
    	return allPersons;
    }
    
    public List<Person> findAllMyFriends(){
    	this.myFriends = (List<Person>) accountController.getloggedInPerson().getFriends();
    	return myFriends;
    }

	public List<Person> getAllPersons() {
		this.allPersons = personService.findAllPersons();
		return allPersons;
	}

	public void setAllPersons(List<Person> allPersons) {
		this.allPersons = allPersons;
	}
    
    public boolean isPersonIAsked(Person person){
    	return accountController.getloggedInPerson().getFriendsIAsked().contains(person);
    }
    
    public boolean isPersonAskedMe(Person person){
    	return accountController.getloggedInPerson().getFriendsAskedMe().contains(person);
    }
    
    public boolean isFriend(Person person){
    	return false;//accountController.getloggedInPerson().getFriends().contains(person);
    }
    
    public void addOrConfirmFriend(ActionEvent event){
		UIParameter param = (UIParameter) event.getComponent()
                .findComponent("friendAdded");
		currentPerson = (Person) param.getValue();
		Person loggedInPerson = accountController.getloggedInPerson();
    	List<Person> friendsIAsked = loggedInPerson.getFriendsIAsked();
    	friendsIAsked.add(currentPerson);
    	loggedInPerson.setFriendsIAsked(friendsIAsked);
    	personService.updatePerson(loggedInPerson);
    }
    
//    public void confirmFriend(Person person){
//    	Person loggedInPerson = accountController.getloggedInPerson();
//    	Set<Long> friendsAskedMe = loggedInPerson.getFriendsAskedMe();
//    	friendsAskedMe.add(person.getPersonId());
//    	loggedInPerson.setFriendsIAskedFs(friendsAskedMe);
//    	personService.updatePerson(loggedInPerson);
//    }

	public Person getCurrentPerson() {
		return currentPerson;
	}

	public void setCurrentPerson(Person currentPerson) {
		this.currentPerson = currentPerson;
	}

	public List<Person> getMyFriends() {
		this.myFriends = (List<Person>) accountController.getloggedInPerson().getFriends();
		return myFriends;
	}

	public void setMyFriends(List<Person> myFriends) {
		this.myFriends = myFriends;
	}
    
    

}
