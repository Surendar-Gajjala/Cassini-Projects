package com.cassinisys.platform.model.common;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Nageshreddy on 08-12-2020.
 */
@Entity
@Table(name = "APP_NAVIGATION")
@Data
public class AppMenu implements Serializable {

    @Id
    @Column(name = "MENU_ID")
    private String id;

    @Column(name = "NAME")
    private String name;

}