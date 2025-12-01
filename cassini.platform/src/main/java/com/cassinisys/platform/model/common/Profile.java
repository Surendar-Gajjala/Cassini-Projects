package com.cassinisys.platform.model.common;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by Nageshreddy on 08-12-2020.
 */
@Entity
@Table(name = "PROFILE")
@Data
@PrimaryKeyJoinColumn(name = "PROFILE_ID")
public class Profile extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DEFAULT_PROFILE")
    private Boolean defaultProfile = Boolean.FALSE;

    @Column(name = "PROFILE_DATA")
    @Type(type = "com.cassinisys.platform.util.converter.StringArrayType")
    private String[] profileData;

    public Profile() {
        super(ObjectType.PROFILE);
    }

}