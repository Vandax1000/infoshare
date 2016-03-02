/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infoshare.app.facade;


import infoshare.domain.person.PersonContact;
import infoshare.domain.person.PersonDemographics;
import infoshare.domain.person.PersonImages;
import infoshare.domain.person.PersonLanguage;
import infoshare.services.people.*;
import infoshare.services.people.Impl.*;

public class PeopleFacade {

    public final static PersonRoleService personRoleService = new PersonRoleServiceImpl();
    public final static PersonService personService = new PersonServiceImpl();
    public final static PersonAddressService personAddressService = new PersonAddressServiceImpl();
    public final static PersonContactService personContactService = new PersonContactServiceImpl();
    public final static PersonDemographicsService personDemographicsService = new PersonDemographicsServiceImpl();
    public final static PersonImagesService personImagesService = new PersonImagesServiceImpl();
    public final static PersonLanguageService personLanguageService = new PersonLanguageServiceImpl();






}
