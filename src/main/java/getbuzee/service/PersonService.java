package getbuzee.service;

import getbuzee.entity.Person;
import getbuzee.exception.ValidationException;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import utils.Loggable;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Stateless
@Loggable
public class PersonService implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private EntityManager em;
    
    private List<Person> allPersons;

    // ======================================
    // =              Public Methods        =
    // ======================================

    public boolean doesLoginAlreadyExist(final String login) {

        if (login == null)
            throw new ValidationException("Login cannot be null");

        // Login has to be unique
        TypedQuery<Person> typedQuery = em.createNamedQuery(Person.FIND_BY_LOGIN, Person.class);
        typedQuery.setParameter("login", login);
        try {
            typedQuery.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public Person createPerson(final Person Person) {

        if (Person == null)
            throw new ValidationException("Person object is null");

        em.persist(Person);

        return Person;
    }

    public Person findPersonByLogin(final String login) {

        if (login == null)
            throw new ValidationException("Invalid login");
        em.flush();
        TypedQuery<Person> typedQuery = em.createNamedQuery(Person.FIND_BY_LOGIN,Person.class);
        typedQuery.setParameter("login", login);

        try {
            Person person = (Person)typedQuery.getSingleResult();
            em.refresh(person);
            return person;
        } catch (NoResultException e) {
            return null;
        }
    }

    public Person findPersonByLoginPassword(final String login, final String password) {

        if (login == null)
            throw new ValidationException("Invalid login");
        if (password == null)
            throw new ValidationException("Invalid password");

        TypedQuery<Person> typedQuery = em.createNamedQuery(Person.FIND_BY_LOGIN_PASSWORD, Person.class);
        typedQuery.setParameter("login", login);
        typedQuery.setParameter("password", password); 
        
        try {
        Person person = typedQuery.getSingleResult();
        em.refresh(person);
        return person;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Person> findAllPersons() {
        TypedQuery<Person> typedQuery = em.createNamedQuery(Person.FIND_ALL_PERSONS, Person.class);
        this.allPersons = typedQuery.getResultList();
        return allPersons;
    }

    public Person updatePerson(Person person) {

        // Make sure the object is valid
        if (person == null)
            throw new ValidationException("Person object is null");
        // Update the object in the database        
        person = em.merge(person);       

        return person;
    }
    
    public void removeFriend(final Person friend1,final Person friend2){
    	Query q = em.createNativeQuery("delete from friendship where (friendsiasked_id= ? and friendsaskedme_id= ?) or (friendsiasked_id= ? and friendsaskedme_id= ?)");
    	q.setParameter(1, friend1.getPersonId());
    	q.setParameter(2, friend2.getPersonId());
    	q.setParameter(3, friend2.getPersonId());
    	q.setParameter(4, friend1.getPersonId());
    	q.executeUpdate();
    }

    public void removePerson(final Person person) {
        if (person == null)
            throw new ValidationException("Person object is null");

        em.remove(em.merge(person));
    }    
    
    public List<Person> findFriends(Person person){
    	Query q = em.createQuery("select p from Person p where p.friendsIAsked= :person and p.friendsAskedMe = :person");
    	q.setParameter("person", person);
    	List<Person> friends = (List<Person>) q.getResultList();
    	return friends;
    }
    
    public void dropTablePerson(){
    	em.createNativeQuery("delete from friendship").executeUpdate();
    	em.createNativeQuery("delete from person").executeUpdate();
    	em.flush();
    }

	public List<Person> getAllPersons() {
		return allPersons;
	}

	public void setAllPersons(List<Person> allPersons) {
		this.allPersons = allPersons;
	}
    
    
}
