package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.plm.model.mfr.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class SupplierEvents {
    @Data
    @AllArgsConstructor
    public static class SupplierCreatedEvent {
        private PLMSupplier supplier;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierBasicInfoUpdatedEvent {
        private PLMSupplier oldSupplier;
        private PLMSupplier newSupplier;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierAttributesUpdatedEvent {
        private PLMSupplier supplier;
        private PLMSupplierAttribute oldAttribute;
        private PLMSupplierAttribute newAttribute;
    }

    // Supplier files event
    @Data
    @AllArgsConstructor
    public static class SupplierFilesAddedEvent {
        private Integer supplier;
        private List<PLMSupplierFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierFoldersAddedEvent {
        private Integer supplier;
        private PLMSupplierFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierFoldersDeletedEvent {
        private Integer supplier;
        private PLMSupplierFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierFileDeletedEvent {
        private Integer supplier;
        private PLMSupplierFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierFilesVersionedEvent {
        private Integer supplier;
        private List<PLMSupplierFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierFileRenamedEvent {
        private Integer supplier;
        private PLMSupplierFile oldFile;
        private PLMSupplierFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierFileLockedEvent {
        private Integer supplier;
        private PLMSupplierFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierFileUnlockedEvent {
        private Integer supplier;
        private PLMSupplierFile file;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierFileDownloadedEvent {
        private Integer supplier;
        private PLMSupplierFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class SupplierCommentAddedEvent {
        private PLMSupplier supplier;
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierContactCreatedEvent {
        private PLMSupplier supplier;
        private Person person;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierContactDeletedEvent {
        private PLMSupplier supplier;
        private Person person;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierPartAddEvent {
        private PLMSupplier supplier;
        private List<PLMManufacturerPart> parts;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierPartUpdatedEvent {
        private Integer supplier;
        private PLMSupplierPart oldSupplierPart;
        private PLMSupplierPart supplierPart;
    }

    @Data
    @AllArgsConstructor
    public static class SupplierPartDeletedEvent {
        private PLMSupplier supplier;
        private PLMManufacturerPart manufacturerPart;
    }
}