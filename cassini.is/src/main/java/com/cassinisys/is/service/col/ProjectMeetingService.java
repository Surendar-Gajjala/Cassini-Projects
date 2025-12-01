package com.cassinisys.is.service.col;
/**
 * The Class is for ProjectMeetingService
 **/

import com.cassinisys.is.model.col.ISProjectMeeting;
import com.cassinisys.is.model.pm.ISProject;
import com.cassinisys.is.repo.col.ProjectMeetingRepository;
import com.cassinisys.is.repo.pm.ProjectRepository;
import com.cassinisys.platform.exceptions.ResourceNotFoundException;
import com.cassinisys.platform.model.col.Attachment;
import com.cassinisys.platform.model.col.Meeting;
import com.cassinisys.platform.model.col.MeetingAttendee;
import com.cassinisys.platform.model.col.MeetingAttendeeId;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.repo.col.MeetingAttendeeRepository;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.col.AttachmentService;
import com.cassinisys.platform.service.common.PersonService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.service.core.PageableService;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.cassinisys.platform.util.Mail;
import com.cassinisys.platform.util.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional
public class ProjectMeetingService implements
        CrudService<ISProjectMeeting, Integer>,
        PageableService<ISProjectMeeting, Integer> {

    @Autowired
    private ProjectMeetingRepository projectMeetingRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MeetingAttendeeRepository meetingAttendeeRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private MailService mailService;

    @Autowired
    private SessionWrapper sessionWrapper;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private PersonRepository personRepository;

    /**
     * The method used to create ISProjectMeeting
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProjectMeeting create(ISProjectMeeting meeting) {
        checkNotNull(meeting);
        meeting.setId(null);
        List<Person> meetingAttendees = meeting.getAttendees();
        ISProject project = projectRepository.findOne(meeting.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        meeting = projectMeetingRepository.save(meeting);
        for (Person person : meetingAttendees) {
            MeetingAttendeeId meetingAttendeeId = new MeetingAttendeeId(meeting.getId(), person.getId());
            MeetingAttendee meetingAttendee = new MeetingAttendee(meetingAttendeeId);
            meetingAttendeeRepository.save(meetingAttendee);
        }
        return meeting;
    }

    public void createMail(Integer meetingId) {
        Meeting meeting = projectMeetingRepository.findOne(meetingId);
        int counter = 0;
        List<Person> persons = new ArrayList<>();
        List<MeetingAttendee> meetingAttendees = meetingAttendeeRepository.findByMeetingId(meetingId);
        String[] recipientAddress = new String[meetingAttendees.size()];
        if (meetingAttendees.size() > 0) {
            for (MeetingAttendee meetingAttendee : meetingAttendees) {
                Person person = personRepository.findOne(meetingAttendee.getId().getPersonId());
                persons.add(person);
                recipientAddress[counter] = person.getEmail();
                counter++;
            }
        }
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        StringBuffer url = request.getRequestURL();
        String uri = request.getRequestURI();
        String host = url.substring(0, url.indexOf(uri));
        Map<String, Object> model = new HashMap<>();
        Mail mail = new Mail();
        mail.setMailToList(recipientAddress);
        List<Object> attachmentObjects = new ArrayList<>();
        List<Attachment> attachments = (attachmentService.findAll(meeting.getId(), ObjectType.MEETING));
        for (Attachment attachment : attachments) {
            attachmentObjects.add(attachment);
        }
        mail.setAttachments(attachmentObjects);
        model.put("host", host);
        model.put("title", meeting.getTitle());
        model.put("from", sessionWrapper.getSession().getLogin().getPerson().getFullName());
        model.put("description", meeting.getDescription());
        model.put("attendees", persons);
        model.put("meeting", meeting);
        mail.setMailSubject("Cassini Meeting: " + meeting.getTitle());
        mail.setTemplatePath("email/newMeeting.html");
        mail.setModel(model);
        mailService.sendEmail(mail);
    }

    /**
     * The method used to update ISProjectMeeting
     **/
    @Override
    @Transactional(readOnly = false)
    public ISProjectMeeting update(ISProjectMeeting meeting) {
        checkNotNull(meeting);
        checkNotNull(meeting.getId());
        ISProject project = projectRepository.findOne(meeting.getProject());
        if (project == null) {
            throw new ResourceNotFoundException();
        }
        for (Person person : meeting.getAttendees()) {
            MeetingAttendeeId meetingAttendeeId = new MeetingAttendeeId();
            meetingAttendeeId.setPersonId(person.getId());
//            meetingAttendeeId.setTaskId(meeting.getId());
            MeetingAttendee meetingAttendee = new MeetingAttendee(meetingAttendeeId);
            meetingAttendeeRepository.save(meetingAttendee);
        }
        meeting = projectMeetingRepository.save(meeting);
        createMail(meeting.getId());
        return meeting;
    }

    /**
     * The method used to delete ISProjectMeeting
     **/
    @Override
    @Transactional(readOnly = false)
    public void delete(Integer id) {
        checkNotNull(id);
        ISProjectMeeting meeting = projectMeetingRepository.findOne(id);
        if (meeting == null) {
            throw new ResourceNotFoundException();
        }
        ISProject project = projectRepository.findOne(meeting.getProject());
        projectMeetingRepository.delete(meeting);
    }

    /**
     * The method used to get the value of ISProjectMeeting
     **/
    @Transactional(readOnly = true)
    @Override
    public ISProjectMeeting get(Integer id) {
        checkNotNull(id);
        ISProjectMeeting meeting = projectMeetingRepository.findOne(id);
        if (meeting == null) {
            throw new ResourceNotFoundException();
        }
        List<MeetingAttendee> attendees = meetingAttendeeRepository.findByMeetingId(meeting.getId());
        List<Person> persons = new ArrayList<>();
        attendees.forEach(a -> {
            Person p = personService.get(a.getId().getPersonId());
            if (p != null) {
                persons.add(p);
            }
        });
        meeting.setAttendees(persons);
        return meeting;
    }

    /**
     * The method used to getall the values from the list of ISProjectMeeting
     **/
    @Transactional(readOnly = true)
    @Override
    public List<ISProjectMeeting> getAll() {
        return projectMeetingRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ISProjectMeeting> findAll(Pageable pageable) {
        checkNotNull(pageable);
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Order(Direction.DESC,
                    "meetingDate")));
        }
        return projectMeetingRepository.findAll(pageable);
    }

    /**
     * The method used to findmultiple values from the list of ISProjectMeeting
     **/
    @Transactional(readOnly = true)
    public List<ISProjectMeeting> findMultiple(List<Integer> ids) {
        return projectMeetingRepository.findByIdIn(ids);
    }

    @Transactional
    public void deleteAttendee(Integer person, Integer meetingId) {
        List<MeetingAttendee> meetingAttendee = meetingAttendeeRepository.findByMeetingId(meetingId);
        for (MeetingAttendee meetingAttendee1 : meetingAttendee) {
            if (meetingAttendee1.getId().getPersonId() == person)
                meetingAttendeeRepository.delete(meetingAttendee);
        }
    }
}
