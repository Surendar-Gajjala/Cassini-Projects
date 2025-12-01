package com.cassinisys.platform.model.plugin;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "ENABLED_PLUGIN")
public class EnabledPlugin implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;
}
