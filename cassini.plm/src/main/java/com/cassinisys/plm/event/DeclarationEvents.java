package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.pgc.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by Suresh Cassini on 30-11-2020.
 */

public final class DeclarationEvents {

    @Data
    @AllArgsConstructor
    public static class DeclarationCreatedEvent {
        private PGCDeclaration declaration;
    }


    @Data
    @AllArgsConstructor
    public static class DeclarationBasicInfoUpdatedEvent {
        private PGCDeclaration oldDeclaration;
        private PGCDeclaration declaration;
    }

    @Data
    @AllArgsConstructor
    public static class DeclarationAttributesUpdatedEvent {
        private PGCObject declaration;
        private PGCObjectAttribute oldAttribute;
        private PGCObjectAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class DeclarationCommentAddedEvent {
        private PGCDeclaration declaration;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class DeclarationPartAddEvent {
        private PGCDeclaration declaration;
        private List<PGCDeclarationPart> parts;
    }

    @Data
    @AllArgsConstructor
    public static class DeclarationPartDeletedEvent {
        private PGCDeclaration declaration;
        private PGCDeclarationPart declarationPart;
    }

    @Data
    @AllArgsConstructor
    public static class DeclarationSpecificationAddEvent {
        private Integer declaration;
        private List<PGCDeclarationSpecification> declarationSpecifications;
    }

    @Data
    @AllArgsConstructor
    public static class DeclarationSpecificationDeletedEvent {
        private Integer declaration;
        private PGCDeclarationSpecification declarationSpecification;
    }

    @Data
    @AllArgsConstructor
    public static class DeclarationPartSubstanceAddEvent {
        private Integer declaration;
        private PGCDeclarationPart declarationPart;
        private List<PGCBosItem> bosItems;
    }

    @Data
    @AllArgsConstructor
    public static class DeclarationPartSubstanceUpdatedEvent {
        private Integer declaration;
        private PGCDeclarationPart declarationPart;
        private PGCBosItem oldBosItem;
        private PGCBosItem bosItem;
    }

    @Data
    @AllArgsConstructor
    public static class DeclarationPartSubstanceDeletedEvent {
        private Integer declaration;
        private PGCDeclarationPart declarationPart;
        private PGCBosItem bosItem;
    }

}
