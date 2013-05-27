package getbuzee.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Entity
@Table(name="PERSON")
@NamedQueries({
@NamedQuery(name=Person.FIND_ALL_PERSONS, query="select p from Person p order by p.personId"),
@NamedQuery(name=Person.FIND_BY_LOGIN_PASSWORD, query="select p from Person p where p.login = :login and p.password = :password order by p.personId"),
@NamedQuery(name=Person.FIND_BY_LOGIN, query="select p from Person p where p.login= :login order by p.personId")
})
public class Person implements Serializable{
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long personId;
    @getbuzee.constraints.Login
    private String login;
    @Column(nullable = false, length = 10)
    @NotNull
    @Size(min = 1, max = 10)
    private String password;
	private String firstName;
	private String name;
	private String email;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="FRIENDSHIP",joinColumns=
		@JoinColumn(name="FRIENDSASKEDME_ID",referencedColumnName="personId"),
		inverseJoinColumns=
		@JoinColumn(name="FRIENDSIASKED_ID",referencedColumnName="personId"))
	private List<Person> friendsAskedMe = new ArrayList<Person>(0);
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="FRIENDSHIP",joinColumns=
		@JoinColumn(name="FRIENDSIASKED_ID",referencedColumnName="personId"),
		inverseJoinColumns=
		@JoinColumn(name="FRIENDSASKEDME_ID",referencedColumnName="personId"))
	private List<Person> friendsIAsked = new ArrayList<Person>(0);
	@Transient
	private List<Person> friends = new ArrayList<Person>(0);
	
    public static final String FIND_BY_LOGIN = "Person.findByLogin";
    public static final String FIND_BY_LOGIN_PASSWORD = "Person.findByLoginAndPassword";
    public static final String FIND_ALL_PERSONS = "Person.findAll";
	
    public Person(){    	
    }
    
	/**
	 * @param personId
	 * @param firstName
	 * @param name
	 * @param email
	 */
	public Person(Long personId, String firstName, String name, String email) {
		this.personId = personId;
		this.firstName = firstName;
		this.name = name;
		this.email = email;
	}
	
	public Person(String login, String password) {
		this.login = login;
		this.password = password;
	}
	

	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public List<Person> getFriendsAskedMe() {
		return friendsAskedMe;
	}

	public void setFriendsAskedMe(List<Person> friendsAskedMe) {
		this.friendsAskedMe = friendsAskedMe;
	}

	public List<Person> getFriendsIAsked() {
		return friendsIAsked;
	}

	public void setFriendsIAsked(List<Person> friendsIAsked) {
		this.friendsIAsked = friendsIAsked;
	}

	
	public List<Person> getFriends() {
		List<Person> myFriends = new ArrayList<Person>(); 
		myFriends = getFriendsAskedMe();
		myFriends.retainAll(getFriendsIAsked());
		return myFriends;
	}

	public void List(List<Person> friends) {
		this.friends = friends;
	}			
	
}
