package com.cassinisys.drdo.model.transactions;

import com.cassinisys.drdo.model.bom.ItemInstance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam reddy on 26-11-2018.
 */
@Entity
@Table(name = "DISPATCH_ITEM")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "DRDO")
public class DispatchItem implements Serializable {

    @Id
    @ApiObjectField(required = true)
    @SequenceGenerator(name = "DISPATCH_ITEM_ID_GEN", sequenceName = "DISPATCH_ITEM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DISPATCH_ITEM_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @ApiObjectField
    @Column(name = "DISPATCH")
    private Integer dispatch;

    @ApiObjectField
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM")
    private ItemInstance item;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDispatch() {
        return dispatch;
    }

    public void setDispatch(Integer dispatch) {
        this.dispatch = dispatch;
    }

    public ItemInstance getItem() {
        return item;
    }

    public void setItem(ItemInstance item) {
        this.item = item;
    }
}
