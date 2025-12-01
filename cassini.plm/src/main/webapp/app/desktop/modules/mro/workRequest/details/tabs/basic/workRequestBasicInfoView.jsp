<div>
    <style scoped>
        .workrequest-attachment {
            padding: 3px;
            border: 1px solid lightgrey;
            width: fit-content;
            background-color: #f5f9f5bd;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.16), 0 1px 2px rgba(0, 0, 0, 0.22);
            border-radius: 3px;
            cursor: pointer;
        }

        .workrequest-attachment .fa-times-circle {
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

        .workrequest-attachment:hover .image-style {
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

        .item-details .files-preview {
            display: flex;
            flex-wrap: wrap;
            align-items: flex-start;
        }

        .item-details .files-preview .dz-preview {
            flex: 1 0 0;
            white-space: nowrap;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 3px;
            margin: 10px;
        }

        .item-details .files-preview .dz-preview .progress {
            height: 5px;
            margin-bottom: 0;
        }

        .item-details .workrequests-file-dropzone {
            min-height: 72px;
            border: 1px dashed #b6b6b6;
            border-radius: 5px;
            margin: 2px 0px 20px 1px;
            cursor: pointer;
        }

        .item-details .workrequests-file-dropzone i.la-close {
            margin-top: 5px;
            padding: 5px;
            font-size: 14px;
            position: absolute;
            right: 28px;
        }

        .item-details .workrequests-file-dropzone i.la-close:hover {
            background-color: #ddd;
            border-radius: 50%;
            font-weight: 600;
        }

        .item-details .workrequests-file-dropzone .drop-files-label {
            font-style: italic;
            text-align: center;
            line-height: 72px
        }

        .thumbnail-container {
            border: 1px solid #ddd;
            height: 380px;
            width: 380px;
            position: absolute;
            right: 30px;
            background-color: #fff;
            z-index: 10 !important;
        }

        .medium-image {
            width: 378px;
            max-height: 378px;
            position: absolute;
            top: 50%;
            transform: translateY(-50%);
        }
    </style>
    <div ng-if="workRequestBasicVm.loading == true" style="padding: 30px;">
        <br/>
    <span style="font-size: 15px;">
        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader6.gif" class="mr5">
       <span translate>LOADING_WORK_REQUEST_DETAILS</span>
    </span>
        <br/>
    </div>
    <div class="item-details" style="padding: 30px" ng-if="workRequestBasicVm.loading == false">
        <div class="thumbnail-container">
            <div>
                <a ng-if="workRequestBasicVm.asset.resourceObject.image != null && workRequestBasicVm.asset.resourceObject.image != ''"
                   href=""
                   ng-click="workRequestBasicVm.showImage(workRequestBasicVm.asset)"
                   title="{{workRequestBasicVm.titleImage}}">
                    <img class="medium-image"
                         ng-src="{{workRequestBasicVm.asset.imagePath}}">
                </a>

                <div ng-if="workRequestBasicVm.asset.resourceObject.image == null"
                     class="no-thumbnail">
                    <span translate>NO_IMAGE</span>
                </div>
                <div id="item-thumbnail-basic{{workRequestBasicVm.asset.id}}" class="item-thumbnail modal">
                    <div class="item-thumbnail-content">
                        <div class="thumbnail-content" style="display: flex;width: 100%;">
                            <div class="thumbnail-view" id="thumbnail-view-basic{{workRequestBasicVm.asset.id}}">
                                <div id="thumbnail-image-basic{{workRequestBasicVm.asset.id}}"
                                     style="display: table-cell;vertical-align: middle;text-align: center;">
                                    <img ng-src="{{workRequestBasicVm.asset.imagePath}}"
                                         style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                            <span class="thumbnail-close"
                                                  id="thumbnail-close-basic{{workRequestBasicVm.asset.id}}"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>NUMBER</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{workRequestBasicVm.workRequest.number}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>TYPE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{workRequestBasicVm.workRequest.type.name}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>ASSET</span> :
            </div>
            <div class="value col-xs-4 col-sm-5">
                <span>{{workRequestBasicVm.workRequest.assetName}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>NAME</span> :
            </div>
            <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('mroworkrequest','edit')}"
                 title="{{hasPermission('mroworkrequest','edit') ? '' : noPermission}}">
                <a href="" e-style="width:250px" ng-if="workRequest.status == 'PENDING'"
                   ng-class="{'permission-text-disabled': !hasPermission('mroworkrequest','edit')}"
                   onaftersave="workRequestBasicVm.updateWorkRequest()"
                   editable-text="workRequestBasicVm.workRequest.name">
                    <span ng-bind-html="workRequestBasicVm.workRequest.name "
                          title="{{workRequestBasicVm.workRequest.name}}"></span>
                </a>
                <span ng-if="workRequest.status != 'PENDING'">{{workRequestBasicVm.workRequest.name}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>DESCRIPTION</span> :
            </div>
            <div class="value col-xs-4 col-sm-5" ng-class="{'cursor-override': !hasPermission('mroworkrequest','edit')}"
                 title="{{hasPermission('mroworkrequest','edit') ? '' : noPermission}}">

                <a href="" ng-if="workRequest.status == 'PENDING'"
                   ng-class="{'permission-text-disabled': !hasPermission('mroworkrequest','edit')}"
                   onaftersave="workRequestBasicVm.updateWorkRequest()"
                   editable-textarea="workRequestBasicVm.workRequest.description"><span ng-bind-html="(workRequestBasicVm.workRequest.description ) || 'CLICK_TO_ENTER_DESCRIPTION' |
                translate" title="{{workRequestBasicVm.workRequest.description}}"></span>
                </a>
                <span ng-if="workRequest.status != 'PENDING'">{{workRequestBasicVm.workRequest.description}}</span>
            </div>
        </div>


        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>REQUESTOR</span> :
            </div>
            <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mroworkrequest','edit')}"
                 title="{{hasPermission('mroworkrequest','edit') ? '' : noPermission}}">
                <a href="#" e-style="width: 250px" ng-if="workRequest.status == 'PENDING'"
                   ng-class="{'permission-text-disabled': !hasPermission('mroworkrequest','edit')}"
                   onaftersave="workRequestBasicVm.updateWorkRequest()"
                   editable-select="workRequestBasicVm.workRequest.requestorObject"
                   title="{{ecrBasicVm.clickToUpdatePerson}}"
                   e-ng-options="person as person.fullName for person in workRequestBasicVm.persons track by person.id">
                    {{workRequestBasicVm.workRequest.requestorObject.fullName}}
                </a>
                <span ng-if="workRequest.status != 'PENDING'">{{workRequestBasicVm.workRequest.requestorObject.fullName}}</span>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>NOTES</span> :
            </div>
            <div class="value col-xs-8 col-sm-9" ng-class="{'cursor-override': !hasPermission('mroworkrequest','edit')}"
                 title="{{hasPermission('mroworkrequest','edit') ? '' : noPermission}}">

                <a href="" ng-if="workRequest.status == 'PENDING'"
                   ng-class="{'permission-text-disabled': !hasPermission('mroworkrequest','edit')}"
                   onaftersave="workRequestBasicVm.updateWorkRequest()"
                   editable-textarea="workRequestBasicVm.workRequest.notes">
                    <span ng-bind-html="(workRequestBasicVm.workRequest.notes) || 'CLICK_TO_ENTER_NOTES' | translate"
                          title="{{workRequestBasicVm.workRequest.notes}}"></span>
                </a>
                <span ng-if="workRequest.status != 'PENDING'">{{workRequestBasicVm.workRequest.notes}}</span>
            </div>
        </div>


        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>PRIORITY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <priority object="workRequestBasicVm.workRequest"></priority>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>STATUS</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <wo-status object="workRequestBasicVm.workRequest"></wo-status>
            </div>
        </div>

        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{workRequestBasicVm.workRequest.createdByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>CREATED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{workRequestBasicVm.workRequest.createDateDe}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_BY</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{workRequestBasicVm.workRequest.modifiedByObject.fullName}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>MODIFIED_DATE</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <span>{{workRequestBasicVm.workRequest.modifiedDate}}</span>
            </div>
        </div>
        <div class="row">
            <div class="label col-xs-4 col-sm-3 text-right">
                <span translate>ATTACHMENTS</span> :
            </div>
            <div class="value col-xs-8 col-sm-9">
                <div class="attachments-panel">
                    <div class="attachment-container">
                        <div class="attachment-text">
                            <div style="white-space: normal;word-wrap: break-word;">
                                <div style="overflow: auto;">
                                    <div style="display: flex;">
                                        <div ng-repeat="image in workRequestBasicVm.workRequest.images">
                                            <div class="workrequest-attachment"
                                                 style="margin: 8px 5px 0;padding: 0;position: relative">
                                                <img ng-src="api/col/comments/image/{{image.id}}"
                                                     title="Preview {{image.fileName}}" class="image-style"
                                                     ng-click="workRequestBasicVm.showImages(workRequestBasicVm.workRequest,image)"/>
                                                <i class="fa fa-times-circle" ng-if="workRequest.status == 'PENDING'"
                                                   ng-click="workRequestBasicVm.deleteImage(image)"
                                                   title="Delete Attachment"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="attachments-container">
                                    <div ng-repeat="attachment in workRequestBasicVm.workRequest.attachmentList"
                                         style="padding: 5px;">
                                        <div class="workrequest-attachment">
                                            <div class="attachment-name" title="Preview {{attachment.name}}"
                                                 style="position: relative">
                                                <i class="{{getIcon(attachment.name)}}"
                                                   ng-click="workRequestBasicVm.filePreview(attachment)"
                                                   style="padding-right: 5px;font-size: 16px;"
                                                   ng-style="{{getIconColor(attachment.name)}}"></i>
                                                <span ng-click="workRequestBasicVm.filePreview(attachment)">{{attachment.name}}</span>
                                                <i class="fa fa-times-circle" ng-if="workRequest.status == 'PENDING'"
                                                   ng-click="workRequestBasicVm.deleteAttachment(attachment)"
                                                   title="Delete Attachment"></i>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div>
                    <a href="" class="fa fa-pencil row-edit-btn"
                       ng-if="!workRequestBasicVm.editAttachment && workRequest.status == 'PENDING'"
                       ng-click="workRequestBasicVm.editWrAttachments()" title="{{EDIT | translate}}"></a>
                </div>
                <div ng-show="workRequestBasicVm.editAttachment && workRequest.status == 'PENDING'">
                    <div style="margin-right: 40%" id="workReqBasicFiles" class="workrequests-file-dropzone"
                         ng-click="workRequestBasicVm.selectWrFiles()"
                         ng-if="workRequestBasicVm.showFilesDropZone">
                        <%--<i class="la la-close" title="Close" ng-click="newWorkRequestVm.loadDropZoneFiles()"></i>--%>
                        <div class="drop-files-label">{{"DROP_FILES_OR_CLICK" | translate}}</div>

                        <div id="fileUploadPreviews" class="files-preview">
                            <div class="dz-preview dz-file-preview" id="fileUploadTemplate">
                                <div class="dz-details">
                                    <div class="dz-filename"><span data-dz-name></span></div>
                                    <div class="dz-size" data-dz-size></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <button class="btn btn-sm btn-primary" title="{{SAVE | translate}}"
                            ng-click="workRequestBasicVm.saveAttachments()">
                        <i class="fa fa-check"></i>
                    </button>
                    <button class="btn btn-sm btn-default" title="{{CANCEL | translate}}"
                            ng-click="workRequestBasicVm.cancelAttachments()">
                        <i class="fa fa-times"></i>
                    </button>
                </div>
            </div>
        </div>
        <div id="attachment-image" class="attachment-image modal">
            <div class="attachment-image-content">
                <div class="image-content" style="display: flex;width: 100%;">
                    <div class="image-view">
                        <div id="{{image.id}}" ng-repeat="image in workRequestBasicVm.selectedImages"
                             ng-show="image.showImage"
                             style="display: table-cell;vertical-align: middle;text-align: center;">
                            <i class="fa fa-angle-left" ng-click="showPreviousImage(image)"
                               ng-if="workRequestBasicVm.selectedImages.length > 1"
                               style="font-size: 50px;color: white;position: absolute;top: 45%;left: 5%;cursor: pointer;"></i>
                            <img ng-src="api/col/comments/image/{{image.id}}"
                                 style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                            <a href="" ng-click="hideImagesView()"
                               class="attachment-image-close pull-right"
                               style="display: inline-block"></a>
                            <i class="fa fa-angle-right" ng-click="showNextImage(image)"
                               ng-if="workRequestBasicVm.selectedImages.length > 1"
                               style="font-size: 50px;color: white;position: absolute;top: 45%;right: 5%;cursor: pointer;"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <object-attribute-details-view object-type="MROOBJECTTYPE" show-attributes="true"
                                       actual-object-type="{{workRequest.objectType}}"
                                       has-permission="workRequest.status == 'PENDING'"
                                       object-type-id="workRequest.type.id"
                                       object-id="workRequest.id"></object-attribute-details-view>
    </div>

</div>