package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.pqm.PQMCustomer;
import com.cassinisys.plm.model.pqm.PQMCustomerFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class CustomerEvents {
    @Data
    @AllArgsConstructor
    public static class CustomerCreatedEvent {
        private PQMCustomer customer;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerBasicInfoUpdatedEvent {
        private PQMCustomer oldCustomer;
        private PQMCustomer newCustomer;
    }

    // Customer files event
    @Data
    @AllArgsConstructor
    public static class CustomerFilesAddedEvent {
        private Integer customer;
        private List<PQMCustomerFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerFoldersAddedEvent {
        private Integer customer;
        private PQMCustomerFile file;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerFoldersDeletedEvent {
        private Integer customer;
        private PQMCustomerFile file;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerFileDeletedEvent {
        private Integer customer;
        private PQMCustomerFile file;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerFilesVersionedEvent {
        private Integer customer;
        private List<PQMCustomerFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerFileRenamedEvent {
        private Integer customer;
        private PQMCustomerFile oldFile;
        private PQMCustomerFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerFileLockedEvent {
        private Integer customer;
        private PQMCustomerFile file;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerFileUnlockedEvent {
        private Integer customer;
        private PQMCustomerFile file;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerFileDownloadedEvent {
        private Integer customer;
        private PQMCustomerFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class CustomerCommentAddedEvent {
        private PQMCustomer customer;
        private Comment comment;
    }

}