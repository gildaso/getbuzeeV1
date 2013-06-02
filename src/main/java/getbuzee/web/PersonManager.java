package getbuzee.web;

import getbuzee.authentication.AccountController;
import getbuzee.entity.Person;
import getbuzee.exception.CatchException;
import getbuzee.service.PersonService;

import java.io.Serializable;
import java.util.ArrayList;
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
    
    private List<Person> personsAskedMe;
    
    private List<Person> personsIAsked;
    
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
    	List<Person> friendsAskedMe = loggedInPerson.getFriendsIAsked();
    	
    	if (!friendsIAsked.contains(currentPerson)){
	    	friendsIAsked.add(currentPerson);
	    	loggedInPerson.setFriendsIAsked(friendsIAsked);
	    	personService.updatePerson(loggedInPerson);
	    	accountController.getloggedInPerson().setFriendsIAsked(friendsIAsked);
//	    	if (friendsAskedMe.contains(currentPerson)){
//	    		accountController.getloggedInPerson().getFriends().add(currentPerson);
//	    	}
    	}
    }
    
    public void suppressFriend(ActionEvent event){
		UIParameter param = (UIParameter) event.getComponent()
                .findComponent("friendAdded");
		Person currentPerson = (Person) param.getValue();		
		Person loggedInPerson = accountController.getloggedInPerson();
		personService.removeFriend(loggedInPerson, currentPerson);
		accountController.getloggedInPerson().getFriendsAskedMe().remove(currentPerson);
		accountController.getloggedInPerson().getFriendsIAsked().remove(currentPerson);
		accountController.getloggedInPerson().getFriends().remove(currentPerson);
    }

	public Person getCurrentPerson() {
		return currentPerson;
	}

	public void setCurrentPerson(Person currentPerson) {
		this.currentPerson = currentPerson;
	}

	public List<Person> getMyFriends() {
		//this.myFriends = (List<Person>) accountController.getloggedInPerson().getFriends();
		this.myFriends = personService.findFriends(accountController.getloggedInPerson());
		return myFriends;
	}
	
	public List<Person> getPersonsAskedMe() throws CloneNotSupportedException{
//		this.personsAskedMe = personService.findPersonsAskedMe(accountController.getloggedInPerson());
		Person loggedInPerson = (Person) accountController.getloggedInPerson().clone();
		this.personsAskedMe = new ArrayList<Person>(loggedInPerson.getFriendsAskedMe());
		this.personsAskedMe.removeAll(myFriends);
		return personsAskedMe;
	}
	
	public List<Person> getPersonsIAsked() throws CloneNotSupportedException{
//		this.personsIAsked = personService.findPersonsIAsked(accountController.getloggedInPerson());
		Person loggedInPerson = (Person) accountController.getloggedInPerson().clone();
		this.personsIAsked = new ArrayList<Person>(loggedInPerson.getFriendsIAsked());
		this.personsIAsked.removeAll(myFriends);
		return personsIAsked;
	}

	public void setMyFriends(List<Person> myFriends) {
		this.myFriends = myFriends;
	}
    
    

}
