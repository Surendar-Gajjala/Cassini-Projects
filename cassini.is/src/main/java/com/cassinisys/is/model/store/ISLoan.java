package com.cassinisys.is.model.store;

import com.cassinisys.is.model.core.ISObjectType;
import com.cassinisys.platform.model.core.CassiniObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.jsondoc.core.annotation.ApiObject;

import javax.persistence.*;
import java.util.List;

/**
 * Created by swapna on 10/08/18.
 */
@Entity
@Table(name = "IS_LOAN")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiObject(group = "IS")
public class ISLoan extends CassiniObject {

    @Column(name = "LOAN_NO")
    private String loanNumber;

    @Column(name = "FROM_PROJECT")
    private Integer fromProject;

    @Column(name = "TO_PROJECT")
    private Integer toProject;

    @Column(name = "FROM_STORE")
    private Integer fromStore;

    @Column(name = "TO_STORE")
    private Integer toStore;

    @Column
    @Type(type = "com.cassinisys.platform.util.EnumUserType", parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName", value = "com.cassinisys.is.model.store.LoanStatus")})
    private LoanStatus status;

    @Transient
    private List<ISLoanIssueItem> loanIssueItems;

    public ISLoan() {
        super(ISObjectType.LOAN);
    }

    public String getLoanNumber() {
        return loanNumber;
    }

    public void setLoanNumber(String loanNumber) {
        this.loanNumber = loanNumber;
    }

    public Integer getFromProject() {
        return fromProject;
    }

    public void setFromProject(Integer fromProject) {
        this.fromProject = fromProject;
    }

    public Integer getToProject() {
        return toProject;
    }

    public void setToProject(Integer toProject) {
        this.toProject = toProject;
    }

    public Integer getFromStore() {
        return fromStore;
    }

    public void setFromStore(Integer fromStore) {
        this.fromStore = fromStore;
    }

    public Integer getToStore() {
        return toStore;
    }

    public void setToStore(Integer toStore) {
        this.toStore = toStore;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public List<ISLoanIssueItem> getLoanIssueItems() {
        return loanIssueItems;
    }

    public void setLoanIssueItems(List<ISLoanIssueItem> loanIssueItems) {
        this.loanIssueItems = loanIssueItems;
    }
}
