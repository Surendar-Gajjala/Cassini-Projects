package com.cassinisys.platform.model.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lakshmi on 10/18/2016.
 */

@Entity
@Data
@Table(name = "LOGINGROUP")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginGroup {
    @EmbeddedId
    private LoginGroupId id;

}
