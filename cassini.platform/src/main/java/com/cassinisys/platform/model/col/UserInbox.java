package com.cassinisys.platform.model.col;

import com.cassinisys.platform.util.converter.LocalTimeConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "USERINBOX")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserInbox implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "USERINBOX_ID_GEN", sequenceName = "USERINBOX_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERINBOX_ID_GEN")
    @Column(name = "INBOX_ID", nullable = false)
    private Integer id;

    @Column(name = "USER_ID", nullable = false)
    private Integer userId;

    @Column(name = "MESSAGE_TYPE", nullable = false)
    private String messageType;

    @Column(name = "MESSAGE", nullable = false)
    private String message;

    @Column(name = "MESSAGE_READ", nullable = false)
    private boolean messageRead;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm a")
    @Convert(converter = LocalTimeConverter.class)
    @Column(name = "TIMESTAMP", nullable = false)
    private Date timeStamp;

    @Column(name = "OBJECT_TYPE")
    private Enum objectType;

    @Column(name = "OBJECT_ID")
    private Integer objectId;



}
