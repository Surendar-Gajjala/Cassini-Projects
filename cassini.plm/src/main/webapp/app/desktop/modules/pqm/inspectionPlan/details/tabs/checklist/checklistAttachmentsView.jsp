<div>
    <style scoped>
        .drop-area {
            height: 100px;
            border: 2px dashed #ccc;
            margin: 10px;
            cursor: pointer;
        }

        .attachments-panel {
            background-color: #fff;
            /*border-top: 1px solid #ddd;*/
            /*border-bottom: 1px solid #ddd;*/
        }

        .checklist-attachment {
            padding: 3px;
            border: 1px solid lightgrey;
            width: fit-content;
            background-color: #f5f9f5bd;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.16), 0 1px 2px rgba(0, 0, 0, 0.22);
            border-radius: 3px;
            cursor: pointer;
        }

        .checklist-attachment .fa-times-circle {
            font-size: 18px;
            position: absolute;
            margin-left: -7px;
            margin-top: -8px;
            color: #00253f;
        }

        .image-style {
            height: 100px;
            width: 104px;
            padding: 3px;
            cursor: pointer;
        }

        .checklist-attachment:hover .image-style {
            height: 105px;
            width: 110px;
            border: 1px solid lightgrey;
        }

        .attachment-container {
            margin: 10px;
        }

        .attachment-text {
            margin-top: 0;
        }

        .attachment-name {
            padding: 5px;
            background-color: #e6e1e1;
            color: #00253f;
        }

        .attachment-name .fa-times-circle {
            font-size: 18px;
            position: absolute;
            margin-left: 0;
            margin-top: -15px;
        }

        .attachments-panel .attachments-container {
            margin-top: 15px;
            display: flex;
            flex-wrap: wrap;
        }

        .attachments-panel .attachments-container > div {
            flex-grow: 1;
        }

        .attachment-image.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(45, 55, 72, 0.77); /* Black w/ opacity */
        }

        .attachment-image .attachment-image-content {
            margin: auto;
            display: block;
            height: calc(100% - 30px);
            width: 100%;
            border-radius: 7px !important;
        }

        .image-content {
            height: 100%;
        }

        .image-view {
            width: 100%;
        }

        .attachment-image-close {
            position: absolute;
            margin-left: -19px;
            margin-top: -19px;
            width: 38px;
            height: 38px;
            opacity: 0.6;
            background-color: #ddd;
            border-radius: 50%;
            cursor: pointer;
        }

        .attachment-image-close:hover {
            opacity: 0.9;
            border-radius: 50%;
            background-color: #ddd;
        }

        .attachment-image-close:before, .attachment-image-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .attachment-image-close:before {
            transform: rotate(45deg) !important;
        }

        .attachment-image-close:after {
            transform: rotate(-45deg) !important;
        }

        .comments-panel .attachments-container {
            display: flex;
            flex-wrap: wrap;
        }

        .comments-panel .attachments-container > div {
            flex-grow: 1;
        }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <div ng-if="checklistAttachmentsVm.checklist.assignedTo == loginPersonDetails.person.id || hasPermission('admin','all')"
         ng-hide="(checklistMode == 'PLAN' && inspectionPlanRevision.released) || !checklistAttachmentsVm.hasPermission">
        <div class="drop-area">
            <div id="checklistAttachments" style="font-style: italic; text-align: center; line-height: 100px">
                {{"DROP_FILES_OR_CLICK" | translate}}
                <a href="" ng-click="checklistAttachmentsVm.selectFiles()"></a>
            </div>
        </div>
    </div>

    <h4 style="margin-left: 10px;">Plan Attachments : </h4>

    <p ng-if="checklistAttachmentsVm.planChecklistImages.length == 0 && checklistAttachmentsVm.planChecklistAttachments.length == 0"
       style="margin-left: 10px;">No Attachments</p>

    <div class="attachments-panel">
        <div class="attachment-container">
            <div class="attachment-text">
                <div style="white-space: normal;word-wrap: break-word;">
                    <div style="overflow: auto;">
                        <div style="display: flex;" ng-repeat="imageFile in checklistAttachmentsVm.planImageFiles">
                            <div ng-repeat="image in imageFile">
                                <div class="checklist-attachment"
                                     style="margin: 8px 5px 0;padding: 0;position: relative">
                                    <img ng-src="api/col/comments/image/{{image.id}}"
                                         title="Preview {{image.fileName}}" class="image-style"
                                         ng-click="checklistAttachmentsVm.showImages(image,'PLAN')"/>
                                    <i class="fa fa-times-circle"
                                       ng-hide="checklistMode == 'ITEMINSPECTION' || !checklistAttachmentsVm.hasPermission"
                                       ng-click="checklistAttachmentsVm.deleteImage(image)"
                                       title="Delete Attachment"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="attachments-container">
                        <div ng-repeat="attachment in checklistAttachmentsVm.planChecklistAttachments"
                             style="padding: 5px;">
                            <div class="checklist-attachment">
                                <div class="attachment-name" title="Preview {{attachment.name}}"
                                     style="position: relative">
                                    <i class="{{getIcon(attachment.name)}}"
                                       ng-click="checklistAttachmentsVm.filePreview(attachment)"
                                       style="padding-right: 5px;font-size: 16px;"
                                       ng-style="{{getIconColor(attachment.name)}}"></i>
                                    <span ng-click="checklistAttachmentsVm.filePreview(attachment)">{{attachment.name}}</span>
                                    <i class="fa fa-times-circle"
                                       ng-hide="checklistMode == 'ITEMINSPECTION' || !checklistAttachmentsVm.hasPermission"
                                       ng-click="checklistAttachmentsVm.deleteAttachment(attachment)"
                                       title="Delete Attachment"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <h4 ng-show="checklistMode == 'ITEMINSPECTION'" style="margin-left: 10px;">Inspection Attachments : </h4>

    <p ng-if="checklistAttachmentsVm.checklistImages.length == 0 && checklistAttachmentsVm.checklistAttachments.length == 0"
       style="margin-left: 10px;" ng-show="checklistMode == 'ITEMINSPECTION'">No Attachments</p>

    <div class="attachments-panel">
        <div class="attachment-container">
            <div class="attachment-text">
                <div style="white-space: normal;word-wrap: break-word;">
                    <div style="overflow: auto;">

                        <div style="display: flex;" ng-repeat="imageFile in checklistAttachmentsVm.imageFiles">
                            <div ng-repeat="image in imageFile">
                                <div class="checklist-attachment"
                                     style="margin: 8px 5px 0;padding: 0;position: relative">
                                    <img ng-src="api/col/comments/image/{{image.id}}"
                                         title="Preview {{image.fileName}}" class="image-style"
                                         ng-click="checklistAttachmentsVm.showImages(image,checklistMode)"/>
                                    <i class="fa fa-times-circle" ng-click="checklistAttachmentsVm.deleteImage(image)"
                                       title="Delete Attachment"
                                       ng-hide="(checklistMode == 'ITEMINSPECTION' && inspection.released) || (checklistMode == 'PLAN' && inspectionPlanRevision.released) || !checklistAttachmentsVm.hasPermission"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="attachments-container">
                        <div ng-repeat="attachment in checklistAttachmentsVm.checklistAttachments"
                             style="padding: 5px;">
                            <div class="checklist-attachment">
                                <div class="attachment-name" title="Preview {{attachment.name}}"
                                     style="position: relative;">
                                    <i class="{{getIcon(attachment.name)}}"
                                       ng-click="checklistAttachmentsVm.filePreview(attachment)"
                                       style="padding-right: 5px;font-size: 16px;"
                                       ng-style="{{getIconColor(attachment.name)}}"></i>
                                    <span ng-click="checklistAttachmentsVm.filePreview(attachment)">{{attachment.name}}</span>
                                    <i class="fa fa-times-circle"
                                       ng-hide="(checklistMode == 'ITEMINSPECTION' && inspection.released) || (checklistMode == 'PLAN' && inspectionPlanRevision.released) || !checklistAttachmentsVm.hasPermission"
                                       ng-click="checklistAttachmentsVm.deleteAttachment(attachment)"
                                       title="Delete Attachment"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="attachment-image" class="attachment-image modal">
        <div class="attachment-image-content">
            <div class="image-content" style="display: flex;width: 100%;">
                <div class="image-view">
                    <div id="{{image.id}}" ng-repeat="image in checklistAttachmentsVm.selectedImages"
                         ng-show="image.showImage"
                         style="display: table-cell;vertical-align: middle;text-align: center;">
                        <i class="fa fa-angle-left" ng-click="showPreviousImage(image)"
                           ng-if="checklistAttachmentsVm.selectedImages.length > 1"
                           style="font-size: 50px;color: white;position: absolute;top: 45%;left: 5%;cursor: pointer;"></i>
                        <img ng-src="api/col/comments/image/{{image.id}}"
                             style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                        <a href="" ng-click="hideImagesView()"
                           class="attachment-image-close pull-right"
                           style="display: inline-block"></a>
                        <i class="fa fa-angle-right" ng-click="showNextImage(image)"
                           ng-if="checklistAttachmentsVm.selectedImages.length > 1"
                           style="font-size: 50px;color: white;position: absolute;top: 45%;right: 5%;cursor: pointer;"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>