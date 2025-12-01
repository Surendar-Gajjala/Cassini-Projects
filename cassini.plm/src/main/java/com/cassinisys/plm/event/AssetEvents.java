package com.cassinisys.plm.event;

import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.plm.model.mro.MROAsset;
import com.cassinisys.plm.model.mro.MROAssetSparePart;
import com.cassinisys.plm.model.mro.MROObjectAttribute;
import com.cassinisys.plm.model.plm.PLMFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

public final class AssetEvents {
    @Data
    @AllArgsConstructor
    public static class AssetCreatedEvent {
        private MROAsset asset;
    }

    @Data
    @AllArgsConstructor
    public static class AssetBasicInfoUpdatedEvent {
        private MROAsset oldAsset;
        private MROAsset newAsset;
    }

    @Data
    @AllArgsConstructor
    public static class AssetAttributesUpdatedEvent {
        private String parentType;
        private Integer objectId;
        private Enum objectType;
        private MROObjectAttribute oldAttribute;
        private MROObjectAttribute newAttribute;
    }

    @Data
    @AllArgsConstructor
    public static class AssetSparePartCreatedEvent {
        private Integer asset;
        private List<MROAssetSparePart> assetSpareParts;
    }

    @Data
    @AllArgsConstructor
    public static class AssetSparePartDeletedEvent {
        private Integer asset;
        private MROAssetSparePart assetSparePart;
    }

    // Asset files event
    @Data
    @AllArgsConstructor
    public static class AssetFilesAddedEvent {
        private Integer asset;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class AssetFoldersAddedEvent {
        private Integer asset;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class AssetFoldersDeletedEvent {
        private Integer asset;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class AssetFileDeletedEvent {
        private Integer asset;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class AssetFilesVersionedEvent {
        private Integer asset;
        private List<PLMFile> files;
    }

    @Data
    @AllArgsConstructor
    public static class AssetFileRenamedEvent {
        private Integer asset;
        private PLMFile oldFile;
        private PLMFile newFile;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class AssetFileLockedEvent {
        private Integer asset;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class AssetFileUnlockedEvent {
        private Integer asset;
        private PLMFile file;
    }

    @Data
    @AllArgsConstructor
    public static class AssetFileDownloadedEvent {
        private Integer asset;
        private PLMFile file;
    }

    // Comment event
    @Data
    @AllArgsConstructor
    public static class AssetCommentAddedEvent {
        private MROAsset asset;
        private Comment comment;
    }

}