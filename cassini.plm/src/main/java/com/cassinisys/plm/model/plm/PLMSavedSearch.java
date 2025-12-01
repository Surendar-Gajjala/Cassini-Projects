package com.cassinisys.plm.model.plm;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by Rajabrahmachary on 09-06-2016.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PLM_SAVEDSEARCH")
@PrimaryKeyJoinColumn(name = "SEARCH_ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLMSavedSearch extends CassiniObject {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SEARCH_TYPE")
    private String searchType;

    @Column(name = "QUERY")
    private String query;

    @Column(name = "OBJECT_TYPE")
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.platform.model.core.ObjectType")})
    private ObjectType searchObjectType;

    @Column(name = "OWNER")
    private Integer owner;

    @Column(name = "TYPE")
    private String type;

    protected PLMSavedSearch() {
        super(PLMObjectType.SAVEDSEARCH);
    }


}
