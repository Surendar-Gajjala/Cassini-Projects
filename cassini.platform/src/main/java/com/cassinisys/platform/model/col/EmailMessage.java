package com.cassinisys.platform.model.col;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailMessage implements Serializable {
    private Integer id;
    private String from;
    private List<String> recipients = new ArrayList<>();
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd hh:mm a")
    private Date timestamp;
    private String subject;
    private String message;
    private String messageText;
    private List<String> attachments = new ArrayList<>();
    private Integer totalMessages;

}
