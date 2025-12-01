package com.cassinisys.platform.model.col;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam on 24-08-2021.
 */

@Data
@Entity
@Table(name = "USER_READ_COMMENT")
public class UserReadComment implements Serializable {
    @Id
    @SequenceGenerator(name = "USER_READ_COMMENT_ID_SEQ", sequenceName = "USER_READ_COMMENT_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_READ_COMMENT_ID_SEQ")
    private Integer id;

    @Column(name = "COMMENT")
    private Integer comment;

    @Column(name = "PERSON")
    private Integer person;

    @Column(name = "READ")
    private Boolean read = Boolean.FALSE;
}
