package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.pgc.PGCObject;
import com.cassinisys.plm.model.pgc.PGCObjectAttribute;
import com.cassinisys.plm.model.pgc.PGCSpecification;
import com.cassinisys.plm.model.pgc.PGCSubstance;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Suresh Cassini on 30-11-2020.
 */

public final class SpecificationEvents {

    @Data
    @AllArgsConstructor
    public static class SpecificationCreatedEvent {
        private PGCSpecification specification;
    }


    @Data
    @AllArgsConstructor
    public static class SpecificationBasicInfoUpdatedEvent {
        private PGCSpecification oldSpecification;
        private PGCSpecification specification;
    }

    @Data
    @AllArgsConstructor
    public static class SpecificationAttributesUpdatedEvent {
        private PGCObject specification;
        private PGCObjectAttribute oldAttribute;
        private PGCObjectAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class SpecificationCommentAddedEvent {
        private PGCSpecification specification;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class SubstanceAddEvent {
        private PGCSubstance substance;
        private PGCSpecification specification;
    }

    @Data
    @AllArgsConstructor
    public static class SubstanceDeletedEvent {
        private PGCSubstance substance;
        private PGCSpecification specification;
    }

}
