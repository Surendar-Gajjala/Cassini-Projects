package com.cassinisys.irste.service;

import com.cassinisys.irste.filtering.ComplaintCriteria;
import com.cassinisys.irste.filtering.ComplaintPredicateBuilder;
import com.cassinisys.irste.filtering.UserCriteria;
import com.cassinisys.irste.filtering.UserPredicateBuilder;
import com.cassinisys.irste.model.*;
import com.cassinisys.irste.model.QIRSTEComplaint;
import com.cassinisys.irste.model.QIRSTEUser;
import com.cassinisys.irste.repo.IRSTEUserRepository;
import com.cassinisys.irste.repo.IRSTEUserUtilityRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonType;
import com.cassinisys.platform.model.core.Login;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PersonTypeRepository;
import com.cassinisys.platform.repo.security.LoginRepository;
import com.cassinisys.platform.service.security.SecurityService;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.cassinisys.platform.util.PageRequest;
import com.cassinisys.platform.util.PageRequestConverter;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nageshreddy on 19-11-2018.
 */
@Service
public class IRSTEUserService {

    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL_CHARS = "@$&";
    private static SecureRandom random = new SecureRandom();
    public Integer passwordLength = 10;

    @Autowired
    private SecurityService securityService;
    @Autowired
    private MailService mailService;
    @Autowired
    private PageRequestConverter pageRequestConverter;
    @Autowired
    private IRSTEUserRepository userRepository;
    @Autowired
    private PersonTypeRepository personTypeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private UserPredicateBuilder userPredicateBuilder;
    @Autowired
    private IRSTEUserUtilityRepository userUtilityRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private UserPredicateBuilder predicateBuilder;
    @Autowired
    private IRSTEUserRepository irsteUserRepository;

    public static String generatePassword(Integer len, String dic) {
        String result = "";
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(dic.length());
            result += dic.charAt(index);
        }
        return result;
    }

    @Transactional(readOnly = false)
    public void deleteResponder(Integer id) {
        userRepository.delete(id);
    }

    @Transactional(readOnly = false)
    public IRSTEUser createComplainant(IRSTEUser responder) {
        IRSTEUser responder1 = userRepository.save(responder);
        return responder1;
    }

    @Transactional(readOnly = false)
    public IRSTEUser createResponder(IRSTEUser responder) {
//        responder.setPersonType((personTypeRepository.findByName("Responder")).getId());
        IRSTEUser responder1 = userRepository.save(responder);

        String password = generatePassword(passwordLength, ALPHA_CAPS + SPECIAL_CHARS + NUMERIC + ALPHA);

        Login login = new Login();
        login.setPerson(responder1);
        if (responder1.getLastName() == null) {
            responder1.setLastName("");
        }
        login.setLoginName(responder1.getFirstName().replace(" ", "").toLowerCase() + "." + responder1.getLastName().replace(" ", "").toLowerCase());
        login.setPassword(password);
        login.setIsActive(true);
        login = securityService.createLogin(login, login.getPerson().getPhoneMobile(), login.getPerson().getEmail());
        PersonType personType = personTypeRepository.findOne(responder1.getPersonType());
        String userName = responder1.getFirstName().replace(" ", "").toLowerCase() + "." + responder1.getLastName().replace(" ", "").toLowerCase();
//        System.out.println(userName);
//        System.out.println(password);

        String email = responder1.getEmail();
        if (email == null) {
            throw new CassiniException("No Email is associated");
        }
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        StringBuffer url = request.getRequestURL();
        new Thread(() -> {
            Map<String, Object> model = new HashMap();
            model.put("responder", responder1.getFullName());
            model.put("loginName", userName);
            model.put("password", password);
            model.put("role", personType.getName());
            Mail mail = new Mail();
            mail.setMailTo(email);

            mail.setMailSubject("IRSTE Login Details");
            mail.setTemplatePath("newResponderNotification.html");
            mail.setModel(model);

            mailService.sendEmail(mail);
        }).start();


        return responder1;
    }

    @Transactional(readOnly = false)
    public List<IRSTEUserUtilities> createResponderUtilities(Integer id, String[] utilities) {
        Person person = personRepository.findOne(id);
        List<IRSTEUserUtilities> responderUtilities1 = new ArrayList();
        for (String utility : utilities) {
            IRSTEUserUtilities responderUtilities = new IRSTEUserUtilities();
            responderUtilities.setId(new ResponderUtilityId(utility, id));
            responderUtilities.setPersonType(person.getPersonType());
            responderUtilities1.add(userUtilityRepository.save(responderUtilities));
        }
        return responderUtilities1;
    }

    public Page<IRSTEUser> getResponders(PageRequest pageRequest) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Integer integer = personTypeRepository.findByName("Responder").getId();
        Page<IRSTEUser> responders = userRepository.findByPersonType(pageable, integer);
        for (IRSTEUser responder : responders) {
            List<String> utilities = userUtilityRepository.findByResponder(responder.getId());
            if (utilities.size() > 0) {
                responder.setUtilities(utilities);
            }
        }
        return responders;
    }

    public List<IRSTEUser> getAllResponders(String personType) {
        Integer integer = null;
        if (personType.toLowerCase().equals("responder")) {
            integer = personTypeRepository.findByName("Responder").getId();
        } else if (personType.toLowerCase().equals("assistor")) {
            integer = personTypeRepository.findByName("Assistor").getId();
        } else {
            integer = personTypeRepository.findByName("Facilitator").getId();
        }


        List<IRSTEUser> responders = userRepository.findByPersonType(integer);
        for (IRSTEUser responder : responders) {
            List<String> utilities = userUtilityRepository.findByResponder(responder.getId());
            if (utilities.size() > 0) {
                responder.setUtilities(utilities);
            }
        }
        return responders;
    }

    /*public List<IRSTEUser> getAllFacilitators() {
        Integer integer = personTypeRepository.findByName("Facilitator").getId();


        List<IRSTEUser> facilitators = userRepository.findByPersonType(integer);
        for (IRSTEUser facilitator : facilitators) {
            List<String> utilities = userUtilityRepository.findByResponder(facilitator.getId());
            if (utilities.size() > 0) {
                facilitator.setUtilities(utilities);
            }
        }
        return facilitators;
    }

    public List<IRSTEUser> getAllAssistors() {
        Integer integer = personTypeRepository.findByName("Assistor").getId();

        List<IRSTEUser> assistors = userRepository.findByPersonType(integer);
        for (IRSTEUser assistor : assistors) {
            List<String> utilities = userUtilityRepository.findByResponder(assistor.getId());
            if (utilities.size() > 0) {
                assistor.setUtilities(utilities);
            }
        }
        return assistors;
    }*/

    public List<String> getUtilitiesByResponder(Integer integer) {
        return userUtilityRepository.findByResponder(integer);
    }

    public IRSTEUserUtilities addResponderUtility(String utility, Integer responder, Integer personType) {
        IRSTEUserUtilities responderUtilities = new IRSTEUserUtilities(new ResponderUtilityId(utility, responder));
        responderUtilities.setPersonType(personType);
        return userUtilityRepository.save(responderUtilities);
    }

    public void deleteResponderUtility(String utility, Integer responder) {
        IRSTEUserUtilities responderUtilities = new IRSTEUserUtilities(new ResponderUtilityId(utility, responder));
        userUtilityRepository.delete(responderUtilities);
    }

    @Transactional(readOnly = true)
    public Page<IRSTEUser> getFilterUsers(PageRequest pageRequest, UserCriteria criteria) {
        Pageable pageable = pageRequestConverter.convert(pageRequest);
        Predicate predicate = userPredicateBuilder.build(criteria, QIRSTEUser.iRSTEUser);
        pageable = new org.springframework.data.domain.PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(new Sort.Order(Sort.Direction.ASC, "createdDate")));
        Page<IRSTEUser> irstePersons = userRepository.findAll(predicate, pageable);
        if (irstePersons.getContent().size() > 0) {
            for (IRSTEUser person : irstePersons.getContent()) {
                List<String> utilities = userUtilityRepository.findByResponder(person.getId());
                if (utilities.size() > 0) {
                    person.setUtilities(utilities);
                }
                PersonType personType = personTypeRepository.findOne(person.getPersonType());
                Login login = loginRepository.findByPersonId(person.getId());
                if (login != null) {
                    person.setLogin(login.getLoginName());
                }
                person.setType(personType);
            }
        }
        return irstePersons;
    }

    @Transactional(readOnly = true)
    public List<Integer> getByUtilityAndPersonType(String utility, Integer personType) {
        return userUtilityRepository.findByUtilityAndPersonType(utility, personType);
    }

    @Transactional(readOnly = true)
    public Page<IRSTEUser> freeTextSearch(UserCriteria criteria, Pageable pageable) {

        pageable = new org.springframework.data.domain.PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = predicateBuilder.build(criteria, QIRSTEUser.iRSTEUser);

        Page<IRSTEUser> irsteUserPage = userRepository.findAll(predicate, pageable);

        return irsteUserPage;
    }





}
