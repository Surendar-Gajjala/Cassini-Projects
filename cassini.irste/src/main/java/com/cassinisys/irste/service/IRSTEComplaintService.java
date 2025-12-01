package com.cassinisys.irste.service;

import com.cassinisys.irste.filtering.ComplaintCriteria;
import com.cassinisys.irste.filtering.ComplaintPredicateBuilder;
import com.cassinisys.irste.model.*;
import com.cassinisys.irste.model.QIRSTEComplaint;
import com.cassinisys.irste.notification.sms.NewComplaintSMS;
import com.cassinisys.irste.repo.IRSTEComplaintHistoryRepository;
import com.cassinisys.irste.repo.IRSTEComplaintRepository;
import com.cassinisys.irste.repo.IRSTEGroupUtilityRepository;
import com.cassinisys.irste.repo.IRSTEUserUtilityRepository;
import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Export;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.common.PersonType;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.repo.common.PersonTypeRepository;
import com.cassinisys.platform.service.common.ExportService;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import com.mysema.query.types.Predicate;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Nageshreddy on 06-11-2018.
 */
@Service
public class IRSTEComplaintService implements CrudService<IRSTEComplaint, Integer> {

    public static Map fileMap = new HashMap();
    @Autowired
    Configuration fmConfiguration;
    List<IRSTEComplaint> complaints = new ArrayList();
    
    @Autowired
    private IRSTEComplaintRepository complaintRepository;
    @Autowired
    private IRSTEUserUtilityRepository userUtilityRepository;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonTypeRepository personTypeRepository;
    @Autowired
    private ComplaintPredicateBuilder predicateBuilder;
    @Autowired
    private MailService mailService;
    @Autowired
    private IRSTEComplaintHistoryRepository complaintHistoryRepository;
    @Autowired
    private IRSTEGroupUtilityRepository irsteGroupUtilityRepository;
    @Autowired
    private ExportService exportService;

    @Override
    @Transactional(readOnly = false)
    public IRSTEComplaint create(IRSTEComplaint irsteComplaint) {
        autoNumberService.getNextNumberByName("Default Complaint Number");
        String utility = irsteComplaint.getUtility();
        List<Integer> responders = userUtilityRepository.findByUtilityAndPersonType(utility, personTypeRepository.findByName("Responder").getId());
        Person person1 = personRepository.findOne(irsteComplaint.getPerson());
        String groupUtility = irsteGroupUtilityRepository.findGroupByUtility(irsteComplaint.getUtility());
        irsteComplaint.setGroupUtility(groupUtility);
        sendNewComplaintEmail(person1, irsteComplaint);
        if (responders.size() > 0) {
            irsteComplaint.setResponder(responders.get(0));
            if (responders.size() == 1) {
                Person person = personRepository.findOne(responders.get(0));
                sendNewComplaintEmail(person, irsteComplaint);
            } else if (responders.size() > 1) {
                List<Person> persons = personRepository.findByIdIn(responders);
                sendNewComplaintEmails(persons, irsteComplaint);
            }
        } else {
            throw new CassiniException("No Responder is available for this utility");
        }

//        sendNewComplaintSMS(person, irsteComplaint);
        irsteComplaint = complaintRepository.save(irsteComplaint);
        createHistory(irsteComplaint);
        return irsteComplaint;
    }

    private void createHistory(IRSTEComplaint complaint) {
        Integer[] ids = null;
        IRSTEComplaintHistory complaintHistory = IRSTEComplaintHistory.getInstance();
        complaintHistory.setComplaint(complaint.getId());
        complaintHistory.setNewStatus(complaint.getStatus());
        if (complaint.getStatus().equals(ComplaintStatus.NEW)) {
            complaintHistory.setSubmittedBy(complaint.getPerson());
            complaintHistory.setAssignedTo(complaint.getResponder());
        } else if (complaint.getStatus().equals(ComplaintStatus.INPROGRESS)) {
            complaintHistory.setAssignedTo(complaint.getResponder());
            complaintHistory.setSubmittedBy(complaint.getResponder());
        } else if (complaint.getStatus().equals(ComplaintStatus.AT_FACILITATOR)) {
            complaintHistory.setAssignedTo(complaint.getFacilitator());
            complaintHistory.setSubmittedBy(complaint.getAssistor());
            ids = new Integer[]{complaint.getFacilitator()};
            if (ids != null) {
                sendComplaintAssignedMail(complaint, ids);
            }
        } else if (complaint.getStatus().equals(ComplaintStatus.AT_ASSISTOR)) {
            complaintHistory.setAssignedTo(complaint.getAssistor());
            complaintHistory.setSubmittedBy(complaint.getResponder());
            ids = new Integer[]{complaint.getAssistor()};
            if (ids != null) {
                sendComplaintAssignedMail(complaint, ids);
            }
        } else if (complaint.getStatus().equals(ComplaintStatus.FACILITATED)) {
            complaintHistory.setAssignedTo(complaint.getAssistor());
            complaintHistory.setSubmittedBy(complaint.getFacilitator());
            ids = new Integer[]{complaint.getAssistor()};
            if (ids != null) {
                sendComplaintCompletedEmail(complaint, ids);
            }
        } else if (complaint.getStatus().equals(ComplaintStatus.ASSISTED)) {
            complaintHistory.setAssignedTo(complaint.getResponder());
            complaintHistory.setSubmittedBy(complaint.getAssistor());
            ids = new Integer[]{complaint.getResponder()};
            if (ids != null) {
                sendComplaintCompletedEmail(complaint, ids);
            }
        } else if (complaint.getStatus().equals(ComplaintStatus.COMPLETED)) {
            complaintHistory.setAssignedTo(complaint.getPerson());
            complaintHistory.setSubmittedBy(complaint.getResponder());
            ids = new Integer[]{complaint.getResponder(), complaint.getPerson()};
            sendComplaintCompletedEmail(complaint, ids);
        }
        complaintHistory.setModifiedDate(new Date());
        complaintHistoryRepository.save(complaintHistory);
    }

    private void sendNewComplaintEmail(Person person, IRSTEComplaint irsteComplaint) {

        if (person != null) {

            String email = person.getEmail();
            if (email == null) {
                throw new CassiniException("No Email is associated");
            }
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String createdDate = df.format(irsteComplaint.getCreatedDate());

            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();

                model.put("complaint", irsteComplaint);
                model.put("responder", person.getFullName());
                model.put("date", createdDate);

                Mail mail = new Mail();
                mail.setMailTo(email);

                mail.setMailSubject("Complaint Generated");
                mail.setTemplatePath("newComplaintNotificationEmail.html");
                mail.setModel(model);

                mailService.sendEmail(mail);
            }).start();
        }
    }

    private void sendNewComplaintEmails(List<Person> persons, IRSTEComplaint irsteComplaint) {

        String[] recipientAddress = new String[persons.size()];
        String email = "";
        if (persons.size() > 0) {

            for (int i = 0; i < persons.size(); i++) {
                Person subscribe = persons.get(i);
                if (email == "") {
                    email = subscribe.getEmail();
                } else {
                    email = email + "," + subscribe.getEmail();
                }
            }

            String[] recipientList = email.split(",");

            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = recipient;
                counter++;
            }

            new Thread(() -> {
                Map<String, Object> model = new HashMap<>();
                model.put("complaint", irsteComplaint);
                model.put("responder", "Team");

                Mail mail = new Mail();
                mail.setMailToList(recipientAddress);

                mail.setMailSubject("Complaint Generated");
                mail.setTemplatePath("newComplaintNotificationEmail.html");
                mail.setModel(model);

                mailService.sendEmail(mail);
            }).start();
        }
    }


    private void sendNewComplaintSMS(Person person, IRSTEComplaint irsteComplaint) {

        String phone = person.getPhoneMobile();
        if (phone != null && !phone.trim().isEmpty()) {
            //Remove any spaces
            phone = phone.trim().replaceAll(" ", "");
            //Remove any dashes
            phone = phone.trim().replaceAll("-", "");

            //Make sure +91 is prepended if missing
            if (!phone.startsWith("+91")) {
                phone = "+91" + phone;
            }
            new NewComplaintSMS(person.getFullName(), irsteComplaint.getComplaintNumber()).sendTo(phone);
        }
    }

    public IRSTEComplaint updateStatus(IRSTEComplaint complaint) {
        if (complaint.getStatus().equals(ComplaintStatus.COMPLETED)) {

        }
        complaint = complaintRepository.save(complaint);
        createHistory(complaint);
        return complaint;
    }


    public IRSTEComplaint finishComplaint(IRSTEComplaint complaint) {
        createHistory(complaint);
        Integer[] ids = null;
        complaint = complaintRepository.save(complaint);

        if (complaint.getPerson() != null) {
            complaint.setPersonObject(personRepository.findOne(complaint.getPerson()));
        }

        if (complaint.getAssistor() != null) {
            complaint.setAssistorPersonObject(personRepository.findOne(complaint.getAssistor()));
        }

        if (complaint.getFacilitator() != null) {
            complaint.setFacilitatorPersonObject(personRepository.findOne(complaint.getFacilitator()));
        }
        if (complaint.getStatus().equals(ComplaintStatus.FACILITATED)) {
            ids = new Integer[]{complaint.getAssistor()};
            if (ids != null) {
                sendComplaintFinishedByFacilitatorEmailtoAssistor(complaint, ids);
            }
        } else {
            ids = new Integer[]{complaint.getResponder()};
            if (ids != null) {
                sendComplaintFinishedByAssistorEmailtoResponder(complaint, ids);
            }
        }

        return complaint;
    }

    private void sendComplaintFinishedByFacilitatorEmailtoAssistor(IRSTEComplaint complaint, Integer[] ids) {
        List<Person> persons = personRepository.findByIdIn(Arrays.asList(ids));
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (person != null) {
                String email = person.getEmail();
                if (email == null) {
                    throw new CassiniException("No Email is associated");
                }

                new Thread(() -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put("complaint", complaint);
                    model.put("assistor", person.getFullName());

                    Mail mail = new Mail();
                    mail.setMailTo(email);

                    mail.setMailSubject("Complaint Finished By " + complaint.getFacilitatorPersonObject().getFullName());
                    mail.setTemplatePath("complaintFinishedByFacilitatorToAssistorNotification.html");
                    mail.setModel(model);

                    mailService.sendEmail(mail);
                }).start();
            }
        }
    }


    private void sendComplaintFinishedByAssistorEmailtoResponder(IRSTEComplaint complaint, Integer[] ids) {
        List<Person> persons = personRepository.findByIdIn(Arrays.asList(ids));
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (person != null) {
                String email = person.getEmail();
                if (email == null) {
                    throw new CassiniException("No Email is associated");
                }

                new Thread(() -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put("complaint", complaint);
                    model.put("responder", person.getFullName());

                    Mail mail = new Mail();
                    mail.setMailTo(email);

                    mail.setMailSubject("Complaint Finished By " + complaint.getAssistorPersonObject().getFullName());
                    mail.setTemplatePath("complaintFinishedByAssistorToResponderNotification.html");
                    mail.setModel(model);

                    mailService.sendEmail(mail);
                }).start();
            }
        }
    }


    @Override
    public IRSTEComplaint update(IRSTEComplaint complaint) {
        createHistory(complaint);
//        sendComplaintMail(complaint);
        complaint = complaintRepository.save(complaint);

        if (complaint.getPerson() != null) {
            complaint.setPersonObject(personRepository.findOne(complaint.getPerson()));
        }

        if (complaint.getAssistor() != null) {
            complaint.setAssistorPersonObject(personRepository.findOne(complaint.getAssistor()));
        }

        if (complaint.getFacilitator() != null) {
            complaint.setFacilitatorPersonObject(personRepository.findOne(complaint.getFacilitator()));
        }

        return complaint;
    }


    private void sendComplaintAssignedMail(IRSTEComplaint complaint, Integer[] ids) {
        List<Person> persons = personRepository.findByIdIn(Arrays.asList(ids));
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (person != null) {
                String email = person.getEmail();
                if (email == null) {
                    throw new CassiniException("No Email is associated");
                }

                new Thread(() -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put("complaint", complaint);
                    model.put("person", person.getFullName());

                    Mail mail = new Mail();
                    mail.setMailTo(email);

                    mail.setMailSubject("Complaint Assigned");
                    mail.setTemplatePath("compliantAssignedNotification.html");
                    mail.setModel(model);

                    mailService.sendEmail(mail);
                }).start();
            }
        }
    }


    private void sendComplaintCompletedEmail(IRSTEComplaint complaint, Integer[] ids) {
        List<Person> persons = personRepository.findByIdIn(Arrays.asList(ids));
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (person != null) {
                String email = person.getEmail();
                if (email == null) {
                    throw new CassiniException("No Email is associated");
                }

                new Thread(() -> {
                    Map<String, Object> model = new HashMap<>();
                    model.put("complaint", complaint);
                    model.put("responder", person.getFullName());

                    Mail mail = new Mail();
                    mail.setMailTo(email);

                    mail.setMailSubject("Complaint Completed");
                    mail.setTemplatePath("compliantCompletedNotification.html");
                    mail.setModel(model);

                    mailService.sendEmail(mail);
                }).start();
            }
        }
    }


    @Override
    public void delete(Integer integer) {
        complaintRepository.delete(integer);
    }

    @Override
    public IRSTEComplaint get(Integer integer) {
        IRSTEComplaint complaint = complaintRepository.findOne(integer);

        if (complaint.getPerson() != null) {
            complaint.setPersonObject(personRepository.findOne(complaint.getPerson()));
        }

        if (complaint.getAssistor() != null) {
            complaint.setAssistorPersonObject(personRepository.findOne(complaint.getAssistor()));
        }

        if (complaint.getFacilitator() != null) {
            complaint.setFacilitatorPersonObject(personRepository.findOne(complaint.getFacilitator()));
        }

        return complaint;
    }

    @Override
    public List<IRSTEComplaint> getAll() {
        List<IRSTEComplaint> complaints = complaintRepository.findAllByOrderByIdDesc();
        for (IRSTEComplaint complaint : complaints) {
            if (complaint.getPerson() != null) {
                complaint.setPersonObject(personRepository.findOne(complaint.getPerson()));
            }
            if (complaint.getAssistor() != null) {
                complaint.setAssistorPersonObject(personRepository.findOne(complaint.getAssistor()));
            }
            if (complaint.getFacilitator() != null) {
                complaint.setFacilitatorPersonObject(personRepository.findOne(complaint.getFacilitator()));
            }
        }
        return complaints;
    }

    public List<Person> getAllFacilitators() {
        PersonType personType = personTypeRepository.findByName("Facilitator");
        List<Person> persons = personRepository.findAllByPersonType(personType.getId());
        return persons;
    }

    public List<Person> getAllAssistors() {
        PersonType personType = personTypeRepository.findByName("Assistor");
        List<Person> persons = personRepository.findAllByPersonType(personType.getId());
        return persons;
    }


    @Transactional(readOnly = true)
    public Page<IRSTEComplaint> freeTextSearch(ComplaintCriteria criteria, Pageable pageable) {
        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Predicate predicate = predicateBuilder.build(criteria, QIRSTEComplaint.iRSTEComplaint);

        Page<IRSTEComplaint> irsteComplaintPage = complaintRepository.findAll(predicate, pageable);

        return irsteComplaintPage;
    }

    @Transactional(readOnly = true)
    public Page<IRSTEComplaint> getComplaintsByFilter(ComplaintCriteria criteria, Pageable pageable) {
        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(new Sort.Order(Sort.Direction.ASC, "groupUtility")));
        Predicate predicate = predicateBuilder.build(criteria, QIRSTEComplaint.iRSTEComplaint);

        Page<IRSTEComplaint> irsteComplaintPage = complaintRepository.findAll(predicate, pageable);

        for (IRSTEComplaint complaint : irsteComplaintPage.getContent()) {
            if (complaint.getPerson() != null) {
                complaint.setPersonObject(personRepository.findOne(complaint.getPerson()));
            }

            if (complaint.getAssistor() != null) {
                complaint.setAssistorPersonObject(personRepository.findOne(complaint.getAssistor()));
            }

            if (complaint.getFacilitator() != null) {
                complaint.setFacilitatorPersonObject(personRepository.findOne(complaint.getFacilitator()));
            }
        }
        complaints = irsteComplaintPage.getContent();
        return irsteComplaintPage;
    }


    @Transactional(readOnly = true)
    public Page<IRSTEComplaint> findAll(Pageable pageable) {
        pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                new Sort(new Sort.Order(Sort.Direction.DESC, "modifiedDate")));
        Page<IRSTEComplaint> irsteComplaintPage = complaintRepository.findAll(pageable);
        complaints = irsteComplaintPage.getContent();
        return irsteComplaintPage;
    }

    public IRSTEComplaint getByComplaintNumber(String number) {
        IRSTEComplaint complaints = complaintRepository.findByComplaintNumberIgnoreCase(number);
        return complaints;
//        return complaintRepository.findByComplaintNumberIgnoreCase(number);
    }

    public Page<IRSTEComplaint> getByResponder(Integer id, Pageable pageable) {
        Page<IRSTEComplaint> complaints = complaintRepository.findByResponder(pageable, id);
        return complaints;

       /* return complaintRepository.findByResponder(pageable, id);*/

    }

    public List<IRSTEComplaint> getAllComplaintsByResponder(Integer responder) {
        complaints = complaintRepository.findByResponder(responder);
        return complaints;
    }


    /*private Date stringToDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }*/

    public String exportTaskReport(String fileType, HttpServletResponse response) {
        List<String> columns = new ArrayList<String>(Arrays.asList("Complaint Number", "Complainant", "Status", "Location", "Group Utility", "Utility", "Created Date", "Details"));
        Export export = new Export();
        export.setFileName("Utility-Report");
        export.setHeaders(columns);
        for(IRSTEComplaint complaint:  complaints){
            complaint.setComplainant(personRepository.findOne(complaint.getPerson()).getFullName());
        }
        exportService.createExportObject(complaints, columns, export);
        return exportService.exportFile(fileType, export, response);
    }







}

