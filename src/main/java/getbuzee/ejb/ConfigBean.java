/*
 * Copyright 2013 Oracle and/or its affiliates.
 * All rights reserved.  You may not modify, use,
 * reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://developers.sun.com/license/berkeley_license.html
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package getbuzee.ejb;

import getbuzee.entity.Person;
import getbuzee.service.PersonService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;


/**
 *
 * @author gildaso
 */
@Singleton
@Startup
public class ConfigBean {
    @EJB
    private PersonService personService;

    @PostConstruct
    public void createData() {
    	//personService.dropTablePerson();
    	if (personService.findPersonByLoginPassword("gildaso","tonton") == null && personService.findPersonByLoginPassword("roberto","tonton") == null && personService.findPersonByLoginPassword("ernesto","tonton")== null){
	    	personService.createPerson(new Person("gildaso","tonton"));
	    	personService.createPerson(new Person("roberto","tonton"));
	    	personService.createPerson(new Person("ernesto","tonton"));
    	}
    	Person gildaso = personService.findPersonByLoginPassword("gildaso","tonton");
    	Person roberto = personService.findPersonByLoginPassword("roberto","tonton");
    	Person ernesto = personService.findPersonByLoginPassword("ernesto","tonton");
    	List<Person> gildasoFriends = new ArrayList<Person>();
    	List<Person> robertoFriends = new ArrayList<Person>();
    	List<Person> ernestoFriends = new ArrayList<Person>();
    	
    	gildasoFriends.add(roberto);
    	gildasoFriends.add(ernesto);
    	robertoFriends.add(gildaso);
    	ernestoFriends.add(gildaso);
    	
    	gildaso.setFriendsIAsked(gildasoFriends);
    	roberto.setFriendsIAsked(robertoFriends);
    	ernesto.setFriendsIAsked(ernestoFriends);
    	
    	personService.updatePerson(gildaso);
    	personService.updatePerson(roberto);
    	personService.updatePerson(ernesto);
    }

    @PreDestroy
    public void deleteData() {
    }
}
