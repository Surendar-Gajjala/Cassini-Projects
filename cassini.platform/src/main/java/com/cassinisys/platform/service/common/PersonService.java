package com.cassinisys.platform.service.common;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.filtering.PersonCriteria;
import com.cassinisys.platform.filtering.PersonPredicateBuilder;
import com.cassinisys.platform.model.common.*;
import com.cassinisys.platform.model.core.AppDetails;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.repo.common.*;
import com.cassinisys.platform.repo.core.AppDetailsRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SecurityService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.DecryptSerializer;
import com.mysema.query.types.Predicate;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by reddy on 7/18/15.
 */
@Service
public class PersonService implements CrudService<Person, Integer>,
        PageableService<Person, Integer> {

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private SessionWrapper sessionWrapper;
    @Autowired
    private PersonPredicateBuilder predicateBuilder;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PersonAddressRepository personAddressRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private PersonGroupRepository personGroupRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AppDetailsRepository appDetailsRepository;
    @Autowired
    private DecryptSerializer decryptSerializer;
    @Autowired
    private UserPreferencesRepository userPreferencesRepository;
    @Autowired
    private SecurityService securityService;

    @Override
    @Transactional
    public Person create(Person person) {
        checkNotNull(person);
        person.setId(null);
        Person alreadyExistPhoneNumber = null;
        Person alreadyExistEmail = null;
        List<AppDetails> detailsList = appDetailsRepository.findAll();
        if (person.isActive()) {
            if (detailsList.size() > 0) {
                for (AppDetails details : detailsList) {
                    if (details.getOptionKey() == 12) {
                        String value = decryptSerializer.decrypt(details.getValue());
                        Integer noOfLicences = Integer.parseInt(value);
                        if (noOfLicences != null) {
                            Integer activeLicences = loginRepository.findIsActiveLogins(person.isActive());
                            if (activeLicences != null) {
                                if (activeLicences >= noOfLicences) {
                                    throw new CassiniException(
                                            messageSource.getMessage("max_licences_reached_error", null, "Email already exist", LocaleContextHolder.getLocale()));
                                }
                            }
                        }
                    }
                }
            }
        }

        Person existPerson = this.personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(person.getFirstName(), person.getLastName());
        if (existPerson != null) {
            String message = messageSource.getMessage("full_name_already_exist", null, "{0} : full name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existPerson.getFirstName() + " " + existPerson.getLastName());
            throw new CassiniException(result);
        }

        if (person.getPhoneMobile() != null && person.getPhoneMobile() != "") {
            alreadyExistPhoneNumber = personRepository.findByPhoneMobile(person.getPhoneMobile().trim());
            if (alreadyExistPhoneNumber != null) {
                throw new CassiniException(("Phone Number already exist"));
            } else if (alreadyExistPhoneNumber == null && person.getEmail() != null && person.getEmail() != "") {
                alreadyExistEmail = personRepository.findByEmailIgnoreCase(person.getEmail().trim());
                if (alreadyExistEmail != null) {

                    throw new CassiniException(
                            messageSource.getMessage("email_already_used_with_another_user", null, "Email already exist", LocaleContextHolder.getLocale()));
                } else {
                    person = personRepository.save(person);
                    GroupMember groupMember = new GroupMember();
                    groupMember.setPerson(person);
                    if (person.getDefaultGroup() != null) {
                        groupMember.setPersonGroup(personGroupRepository.findOne(person.getDefaultGroup()));
                        groupMemberRepository.save(groupMember);
                    }
                }
            }
        } else if (person.getEmail() != null && person.getEmail() != "") {
            alreadyExistEmail = personRepository.findByEmailIgnoreCase(person.getEmail().trim());
            if (alreadyExistEmail != null) {
                throw new CassiniException(
                        messageSource.getMessage("email_already_used_with_another_user", null, "Email already exist", LocaleContextHolder.getLocale()));
            } else {
                person = personRepository.save(person);
                GroupMember groupMember = new GroupMember();
                groupMember.setPerson(person);
                if (person.getDefaultGroup() != null) {
                    groupMember.setPersonGroup(personGroupRepository.findOne(person.getDefaultGroup()));
                    groupMemberRepository.save(groupMember);
                }
            }
        }

        return person;
    }

    @Override
    @Transactional
    public Person update(Person person) {
        checkNotNull(person);
        checkNotNull(person.getId());
        Person alreadyExistPhoneNumber = null;
        Person alreadyExistEmail = null;
        Boolean sendPasscode = person.getSendPasscode();
        if ((person.getFirstName() != null && person.getFirstName() != "") && (person.getLastName() != null && person.getLastName() != "")) {
            this.checkIfPersonExist(person);
        }
        Person existPerson = personRepository.findOne(person.getId());
        if (existPerson != null) {
            person.setImage(existPerson.getImage());
        }

        List<AppDetails> detailsList = appDetailsRepository.findAll();
        if (person.isActive()) {
            if (detailsList.size() > 0) {
                for (AppDetails details : detailsList) {
                    if (details.getOptionKey() == 12) {
                        String value = decryptSerializer.decrypt(details.getValue());
                        Integer noOfLicences = Integer.parseInt(value);
                        if (noOfLicences != null) {
                            Integer activeLicences = loginRepository.findIsActiveLogins(person.isActive());
                            if (activeLicences != null) {
                                if (activeLicences >= noOfLicences) {
                                    throw new CassiniException(
                                            messageSource.getMessage("max_licences_reached_error", null, "Email already exist", LocaleContextHolder.getLocale()));
                                }
                            }
                        }
                    }
                }
            }
        }

        if (person.getDefaultGroup() != null) {
            PersonGroup personGroup = personGroupRepository.findOne(person.getDefaultGroup());
            GroupMember groupMember = groupMemberRepository.findByPersonAndPersonGroup(person, personGroup);
            if (groupMember == null) {
                groupMember = new GroupMember();
                groupMember.setPersonGroup(personGroup);
                groupMember.setPerson(person);
                groupMemberRepository.save(groupMember);
            }
        }

        if (person.getPhoneMobile() != null && person.getPhoneMobile() != "") {
            alreadyExistPhoneNumber = personRepository.findByPhoneMobile(person.getPhoneMobile().trim());
            if (alreadyExistPhoneNumber != null && !alreadyExistPhoneNumber.getId().equals(person.getId())) {
                throw new CassiniException(("Phone Number already exist"));
            } else {
                if (person.getEmail() != null && person.getEmail() != "") {
                    alreadyExistEmail = personRepository.findByEmailIgnoreCase(person.getEmail().trim());
                    if (alreadyExistEmail != null && !alreadyExistEmail.getId().equals(person.getId())) {
                        throw new CassiniException(
                                messageSource.getMessage("email_already_used_with_another_user", null, "Email already exist", LocaleContextHolder.getLocale()));
                    } else {
                        if (existPerson != null && existPerson.getEmail() != null && !existPerson.getEmail().equals("") && !existPerson.getEmail().equals(person.getEmail())) {
                            person.setEmailVerified(false);
                        }
                        person = personRepository.save(person);
                    }
                }
            }
        } else if (person.getEmail() != null && person.getEmail() != "") {
            alreadyExistEmail = personRepository.findByEmailIgnoreCase(person.getEmail().trim());
            if (alreadyExistEmail != null && !alreadyExistEmail.getId().equals(person.getId())) {
                throw new CassiniException(
                        messageSource.getMessage("email_already_used_with_another_user", null, "Email already exist", LocaleContextHolder.getLocale()));
            } else {
                if (existPerson != null && existPerson.getEmail() != null && !existPerson.getEmail().equals("") && !existPerson.getEmail().equals(person.getEmail())) {
                    person.setEmailVerified(false);
                }
                person = personRepository.save(person);
            }
        }
        if (sendPasscode) {
            securityService.sendEmailVerifyPassword(person.getId());
        }
        return person;
    }


    private void checkIfPersonExist(Person person) {
        Person existPerson = this.personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(person.getFirstName(), person.getLastName());
        if (existPerson != null && !person.getId().equals(existPerson.getId())) {
            String message = messageSource.getMessage("full_name_already_exist", null, "{0} : full name already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", person.getFirstName() + " " + person.getLastName());
            throw new CassiniException(result);
        }
    }

    @Transactional
    public Person updatePerson(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    public void deletePersonImage(Integer id) {
        Person person = personRepository.findOne(id);
        person.setImage(null);
        person = personRepository.save(person);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        checkNotNull(id);
        Person person = personRepository.findOne(id);
        if (person == null) {
            throw new ResourceNotFoundException();
        }
        if (person.getDefaultGroup() != null) {
            PersonGroup personGroup = personGroupRepository.findOne(person.getDefaultGroup());
            GroupMember groupMember = groupMemberRepository.findByPersonAndPersonGroup(person, personGroup);
            if (groupMember != null) {
                groupMemberRepository.delete(groupMember);
            }
        }
        personRepository.delete(person);
    }


    @Override
    @Transactional(readOnly = true)
    public Person get(Integer id) {
        checkNotNull(id);
        Person person = personRepository.findOne(id);
        if (person == null) {
            throw new ResourceNotFoundException();
        }
        return person;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    //@Override
    @Transactional(readOnly = true)
    public List<Person> getAllActivePerson() {
        List<Person> listPersons = new ArrayList<>();
        List<Login> activeLogins = loginRepository.findByIsActive(Boolean.TRUE);
        for(Login login: activeLogins){

            listPersons.add(login.getPerson());

        }
        
      
        return listPersons;
    }


    @Transactional(readOnly = true)
    public Page<Person> getPersonsByPersonType(Integer personType, Pageable pageable) {

        return personRepository.findByPersonType(personType, pageable);
    }

    @Transactional(readOnly = true)
    public List<Person> getAllPersonsByPersonType(Integer personType) {

        return personRepository.findAllByPersonType(personType);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<Person> findAll(Pageable pageable) {
        checkNotNull(pageable);

        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort("id"));
        }
        return personRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Person> findMultiple(List<Integer> ids) {
        return personRepository.findByIdIn(ids);
    }


    @Transactional(readOnly = true)
    public Page<Address> getAddresses(Integer personId, Pageable pageable) {
        checkNotNull(personId);
        checkNotNull(pageable);
        if (personRepository.findOne(personId) == null) {
            throw new ResourceNotFoundException();
        }
        Page<Integer> addressesIdPage = personAddressRepository.findByPersonId(
                personId, pageable);
        List<Address> addresses = addressRepository.findAll(addressesIdPage
                .getContent());
        return new PageImpl<Address>(addresses, pageable,
                addressesIdPage.getTotalElements());
    }

    @Transactional
    public PersonAddress createAddress(Integer personId, Address address) {
        checkNotNull(personId);
        checkNotNull(address);
        Person person = personRepository.findOne(personId);
        if (person == null) {
            throw new ResourceNotFoundException();
        }
        address.setId(null);
        address = addressRepository.save(address);
        PersonAddress personAddress = new PersonAddress(person.getId(),
                address.getId());
        personAddress = personAddressRepository.save(personAddress);

        return personAddress;
    }

    @Transactional
    public PersonAddress addAddress(Integer personId, Integer addressId) {
        checkNotNull(personId);
        checkNotNull(addressId);
        Person person = personRepository.findOne(personId);
        if (person == null) {
            throw new ResourceNotFoundException();
        }
        Address address = addressRepository.findOne(addressId);
        if (address == null) {
            throw new ResourceNotFoundException();
        }
        PersonAddress personAddress = new PersonAddress(person.getId(),
                address.getId());
        personAddress = personAddressRepository.save(personAddress);

        return personAddress;
    }

    @Transactional
    public void deleteAddress(Integer personId, Integer addressId) {
        checkNotNull(personId);
        checkNotNull(addressId);
        Person person = personRepository.findOne(personId);
        if (person == null) {
            throw new ResourceNotFoundException();
        }
        personAddressRepository.delete(new PersonAddressId(personId,
                addressId));

    }

    @Transactional(readOnly = true)
    public Page<Person> findAll(PersonCriteria criteria, Pageable pageable) {
        Predicate predicate = predicateBuilder.build(criteria, QPerson.person);
        Page<Person> persons = personRepository.findAll(predicate, pageable);
        return persons;
    }

    @Transactional(readOnly = true)
    public Person findByEmail(String email) {
        Person person = personRepository.findByEmailIgnoreCase(email);
        return person;
    }

    @Transactional
    public Person createIssuePerson(Person person) {
        Person person1 = personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(person.getFirstName(), person.getLastName());
        if (person1 == null) {
            person1 = personRepository.save(person);
        }
        return person1;
    }

    public void downloadPersonImage(Integer id, HttpServletResponse response) {
        Person person = personRepository.findOne(id);
        if (person != null) {
            InputStream is = new ByteArrayInputStream(person.getImage());
            try {
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public Integer getPersonCountWithoutLogin() {
        Integer personsWithoutLogin = 0;
        List<Integer> personIds = loginRepository.getLoginPersonIds();

        if (personIds.size() > 0) {
            personsWithoutLogin = personRepository.getPersonCountByIdNotIn(personIds);
        }
        return personsWithoutLogin;
    }
}
