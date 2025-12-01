package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.transactions.GatePass;
import com.cassinisys.drdo.model.transactions.Inward;
import org.springframework.data.domain.Page;

/**
 * Created by subramanyam reddy on 28-11-2018.
 */
public class InwardDto {

    private Page<Inward> inwards = null;

    private Page<GatePass> gatePasses = null;

    private Boolean inwardsPage = Boolean.TRUE;

    private Boolean finishedPage = Boolean.FALSE;

    private Boolean gatePassView = Boolean.FALSE;

    private Integer finishedInwards = 0;

    private Integer gatePassLength = 0;

    public Page<Inward> getInwards() {
        return inwards;
    }

    public void setInwards(Page<Inward> inwards) {
        this.inwards = inwards;
    }

    public Page<GatePass> getGatePasses() {
        return gatePasses;
    }

    public void setGatePasses(Page<GatePass> gatePasses) {
        this.gatePasses = gatePasses;
    }

    public Boolean getInwardsPage() {
        return inwardsPage;
    }

    public void setInwardsPage(Boolean inwardsPage) {
        this.inwardsPage = inwardsPage;
    }

    public Integer getFinishedInwards() {
        return finishedInwards;
    }

    public void setFinishedInwards(Integer finishedInwards) {
        this.finishedInwards = finishedInwards;
    }

    public Boolean getFinishedPage() {
        return finishedPage;
    }

    public void setFinishedPage(Boolean finishedPage) {
        this.finishedPage = finishedPage;
    }

    public Integer getGatePassLength() {
        return gatePassLength;
    }

    public void setGatePassLength(Integer gatePassLength) {
        this.gatePassLength = gatePassLength;
    }

    public Boolean getGatePassView() {
        return gatePassView;
    }

    public void setGatePassView(Boolean gatePassView) {
        this.gatePassView = gatePassView;
    }
}
