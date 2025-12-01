package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;
import com.cassinisys.plm.model.pqm.QCRFor;

/**
 * Created by subramanyam reddy on 19-09-2018.
 */
public class QCRProblemSourceCriteria extends Criteria {

    private Integer type;

    private String product;

    private String problem;

    private QCRFor qcrFor;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public QCRFor getQcrFor() {
        return qcrFor;
    }

    public void setQcrFor(QCRFor qcrFor) {
        this.qcrFor = qcrFor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
