package com.cassinisys.is.api.common;

import com.cassinisys.is.BaseTest;
import com.cassinisys.is.model.col.ISProjectMeeting;
import com.cassinisys.is.repo.col.ProjectMeetingRepository;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by reddy on 12/1/15.
 */
public class MeetingServiceTest extends BaseTest {
    @Autowired
    private ProjectMeetingRepository projectMeetingRepository;

    @Test
    @Rollback(false)
    public void testMeeting() throws Exception {
        ISProjectMeeting meeting = new ISProjectMeeting();
        meeting.setProject(34);
        meeting.setTitle("Project weekly status");
        meeting.setStartDate(LocalDate.now());
        meeting.setStartTime(LocalTime.now());
        meeting.setEndDate(LocalDate.now());
        meeting.setEndTime(LocalTime.now());
        meeting.setDescription("Weekly status meeting");

        meeting = projectMeetingRepository.save(meeting);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        String json = writer.writeValueAsString(meeting);
        System.out.println(json);

        meeting = mapper.readValue(json, ISProjectMeeting.class);
        meeting.setId(null);
        meeting = projectMeetingRepository.save(meeting);

        writer = mapper.writer(new DefaultPrettyPrinter());
        json = writer.writeValueAsString(meeting);
        System.out.println(json);
    }
}
