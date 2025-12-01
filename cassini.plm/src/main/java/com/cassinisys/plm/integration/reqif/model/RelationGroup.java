package com.cassinisys.plm.integration.reqif.model;

import java.util.List;

public class RelationGroup extends BaseObject {
    private Specification sourceSpecification;
    private Specification targetSpecification;
    private RelationGroupType type;
    private List<SpecRelation> specRelations;

    public Specification getSourceSpecification() {
        return sourceSpecification;
    }

    public void setSourceSpecification(Specification sourceSpecification) {
        this.sourceSpecification = sourceSpecification;
    }

    public Specification getTargetSpecification() {
        return targetSpecification;
    }

    public void setTargetSpecification(Specification targetSpecification) {
        this.targetSpecification = targetSpecification;
    }

    public RelationGroupType getType() {
        return type;
    }

    public void setType(RelationGroupType type) {
        this.type = type;
    }

    public List<SpecRelation> getSpecRelations() {
        return specRelations;
    }

    public void setSpecRelations(List<SpecRelation> specRelations) {
        this.specRelations = specRelations;
    }
}
