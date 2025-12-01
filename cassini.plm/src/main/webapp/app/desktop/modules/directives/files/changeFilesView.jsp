<div>
    <style scoped>
        a.disabled {
            cursor: not-allowed !important;
            color: lightgrey !important;
        }

        table.highlight-row tr.dropzone-row:hover, table.highlight-row tr.dropzone-row:hover td {
            background-color: inherit !important;
            background-image: none !important;
            color: rgb(99, 110, 123) !important;
        }

        .imageTooltip {
            position: relative;
            display: inline-block;
        }

        .imageTooltip .imageTooltiptext {
            visibility: hidden;
            width: 90px !important;
            background-color: #b4e5f1d4;
            border: 1px solid grey;
            color: #141f9f;
            text-align: left;
            border-radius: 6px;
            padding: 5px 0;
            position: absolute;
            z-index: 10;
            top: -35px;
            /*bottom: auto;*/
            left: 100%;
            opacity: 0;
            transition: opacity 1s;
        }

        .imageTooltip .imageTooltiptext::after {
            content: "";
            position: absolute;
            top: 5%;
            right: 102%;
            margin-left: -5px;
            border-width: 5px;
            color: #090909;
            /*border-style: solid;*/
            /*border-color: transparent #3639f4 transparent transparent;*/
        }

        .imageTooltip:hover .imageTooltiptext {
            visibility: visible;
            opacity: 1;
        }

        table {
            table-layout: auto !important;
        }

        .file-container {
            overflow: hidden;
            position: relative;
            width: 100%;
            cursor: pointer;
            margin-bottom: 0 !important;
            float: left;
            padding: 7px 10px;
            height: 34px;
        }

        .file-container [type=file] {
            cursor: inherit;
            display: block;
            font-size: 999px;
            filter: alpha(opacity=0);
            opacity: 0;
            position: absolute;
            right: 0;
            text-align: right;
            top: 0;
        }

        .file-container:hover {
            background-color: #e7e7e7;

        }

        .notification1 {
            text-decoration: none;
            padding: 9px 0;
            position: relative;
            display: inline-block;
            border-radius: 2px;
        }

        .notification1 .folder-count {
            position: absolute;
            top: -1px;
            left: -15px;
            padding: 5px 7px 3px;
            border-radius: 50%;
            background-color: #636963;
            color: white;
            font-size: 14px !important;
        }

        .dropdown-menu > li > a:hover {
            /*color: #333 !important;*/
        }

        .actions-col {
            width: 100px;
            text-align: center;
            position: sticky !important;
            right: -5px;
        }

        .center {
            display: block;
            margin-left: auto;
            margin-right: auto;
            margin-top: 4%;
            width: 300px;
        }

        .no-conversations .no-conversations-message {
            font-size: 20px;
            font-weight: 300 !important;
            text-align: center;
        }

        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -5px;
        }

        .inspectionreport-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 100px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .inspectionreport-model .documentModel-content {
            margin: auto;
            display: block;
            height: 260px;
            width: 500px;
            background-color: white;
            border-radius: 7px !important;
        }

        .document-header {
            padding: 10px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
            font-weight: bold;
            font-size: 22px;
        }

        .document-footer {
            padding: 10px;
            text-align: right;
            height: 50px;
        }

        .document-content {
            height: 160px;
            vertical-align: middle;
            display: table-cell;
            width: 500px;
        }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" href="https://developer.api.autodesk.com/modelderivative/v2/viewers/style.min.css"
          type="text/css">
    <div id="folderFiles" style="display: none;">

    </div>
    <div ng-show="showDropZone == false" id="changeObjectFiles">
        <div class="responsive-table" id="changeFilesTableContainer">
            <div style="display: flex;width: 100%;">
                <div id="parentFile" style="font-style: italic; text-align: center;width: 100%;padding: 10px;" dragfile
                     files="files" update-file="updateFile"
                     ng-if="hasPermission && objectType != 'ITEM' && hasCreate && !isExternal">
                    {{dragAndDropFilesTitle}}
                    <a href="" ng-click="selectFiles()">({{clickToAddFilesTitle}})</a>
                </div>
                <div id="parentFile1" style="font-style: italic; text-align: center;width: 100%;padding: 10px;" dragfile
                     files="files" update-file="updateFile"
                     ng-if="hasPermission && objectType == 'ITEM' && lockedOwner && hasCreate && !isExternal">
                    {{dragAndDropFilesTitle}}
                    <a href="" ng-click="selectFiles()">({{clickToAddFilesTitle}})</a>
                </div>
                <div style="font-style: italic; text-align: center;width: 100%;padding: 10px;"
                     ng-if="(externalPermission == 'WRITE' && isExternal) && hasCreate">
                    {{dragAndDropFilesTitle}}
                    <a href="" ng-click="selectFiles()">({{clickToAddFilesTitle}})</a>
                </div>
            </div>

            <div>
                <tr id="attachmentsContainer" ng-show="showReportDropzone == true"
                    class="attachments-dropzone-container">
                    <div id="attachmentsDropZone" style="display: table; width: 100%;">
                        <div id="report-previews">
                            <div id="report-template">
                                <div style="width:100%;display:inline-flex;border-bottom: 1px dotted lightgrey; margin-bottom: 10px;padding-bottom: 10px;">
                                    <div style="width:50%;text-align: right;">
                                        <span class="name" data-dz-name></span> :
                                        <span class="size" data-dz-size></span>
                                        <small class="error text-danger" data-dz-errormessage></small>
                                    </div>
                                    <div style="width: 30%;margin-left: 10px;">
                                        <div id="total-progress" class="progress progress-striped active"
                                             role="progressbar"
                                             aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"
                                             style="margin-bottom: 0">
                                            <div class="progress-bar progress-bar-success" style="width:0%;"
                                                 data-dz-uploadprogress></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </tr>
            </div>
            <table id="changeFilesTable" class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="text-align: center;z-index: 11;" class="select-input sticky-col sticky-actions-col">
                    <span uib-dropdown dropdown-append-to-body style="width: 100%;"
                          ng-if="hasPermission && hasCreate && (!isExternal || (isExternal && externalPermission  != 'READ'))"
                          title="{{addFolderOrFiles}}">
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                           ng-if="objectType != 'ITEM' && objectType != 'DOCUMENT'"
                           style="padding-left: 3px;"></i>
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                           ng-if="objectType == 'ITEM' && lockedOwner" style="padding-left: 3px;"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li>
                                <a href="" ng-click="addItemFolder(null,'Add')"
                                   translate>NEW_FOLDER</a>
                            </li>
                            <li ng-if="(objectType != 'DOCUMENT' && external.external == false) || objectType == 'MFRPARTINSPECTIONREPORT'"
                                ng-hide="isExternal">
                                <a href="" ng-click="showDocuments(null)">
                                    <span translate>ADD_DOCUMENTS</span></a>
                            </li>
                            <li ng-if="clipboardFiles.length > 0">
                                <a href=""
                                   ng-click="pasteFilesFromClipboard(objectId,objectType)">
                                    <span translate>PASTE_FROM_CLIPBOARD</span> ({{clipboardFiles.length}})</a>
                            </li>
                        </ul>
                    </span>
                    </th>
                    <th class="col-width-250 file-name sticky-col sticky-actions-col" style="z-index: 11;" translate>
                        TAB_FILES_FILE_NAME
                    </th>
                    <th translate>TAB_FILES_FILE_SIZE</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th style="text-align: center" translate>TAB_FILES_VERSION</th>
                    <th style="text-align: center"
                        ng-if="objectType == 'DOCUMENT' || objectType == 'MFRPARTINSPECTIONREPORT' || objectType == 'PPAPCHECKLIST'"
                        translate>LIFECYCLE
                    </th>
                    <th translate>CREATED_DATE</th>
                    <!-- <th translate>CREATED_BY</th> -->
                    <th style="width: 150px;z-index: auto !important;">
                        <span ng-if="selectedPerson != null"
                              style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                                ({{selectedPerson.fullName}})
                                <i class="fa fa-times-circle" ng-click="clearCreatedBy()"
                                   title="{{removeTitle}}"></i>
                        </span>
                        <br>

                        <div class="dropdown" uib-dropdown style="display: inline-block">
                            <span uib-dropdown-toggle><span translate>CREATED_BY</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="person in createdByPersons"
                                    ng-click="onSelectCreatedBy(person)"><a
                                        href="">{{person.fullName}}</a>
                                </li>
                            </ul>
                        </div>
                    </th>
                    <th translate>MODIFIED_DATE</th>
                    <th translate>MODIFIED_BY</th>
                    <th style="text-align: center" translate>TAB_FILES_IS_LOCKED</th>
                    <th translate>TAB_FILES_LOCKED_BY</th>
                    <th class="actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="loading == true">
                    <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                          class="mr5"><span translate>LOADING_FILES</span>
                    </td>
                </tr>
                <tr ng-if="showReportDropzone == false && loading == false && files.length == 0">
                    <td colspan="14" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Files.png" alt="" class="image">

                            <div class="message" ng-if="searchText == null || searchText == ''">{{
                                'NO_FILES' | translate}}
                            </div>
                            <div class="message" ng-if="searchText != null && searchText != ''">{{
                                'NO_SEARCH_RESULT_FOUND' | translate}}
                            </div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>
                <tr id="{{$index}}" ng-show="showReportDropzone == false && files.length > 0"
                    ng-repeat="file in files" dragfile files="files"
                    update-file="updateFile">

                    <td style="display: flex;z-index: 12;width: 50px"
                        class="select-input sticky-col sticky-actions-col">
                        <div class="ckbox ckbox-default" ng-hide="file.fileType == 'FOLDER'"
                             style="display: inline-block;">
                            <input id="file-{{$index}}" name="file-selected" type="checkbox" ng-value="true"
                                   ng-model="file.selected" ng-change="selectFile(file)">
                            <label for="file-{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    <span style="text-align: center;width: 20px;"
                          ng-show="file.fileType != 'FOLDER' && ((hasPreview && !isExternal) || (hasPreview && isExternal && externalPermission == 'WRITE'))"
                          ng-click="filePreview(file)" title="{{previewFile}}">
                        <i class="{{getFileIcon(file.name)}}" ng-if="file.fileType == 'FILE'"
                           ng-style="{{getFileIconColor(file.name)}}"></i>
                    </span>
                        <span style="text-align: center;width: 20px;"
                              ng-show="file.fileType != 'FOLDER' && ((!hasPreview && !isExternal) || (isExternal && externalPermission == 'READ'))">
                        <i title="{{noPermission}}" ng-if="file.fileType == 'FILE'" class="{{getFileIcon(file.name)}}"
                           ng-style="{{getFileIconColor(file.name)}}"></i>
                        </span>
                    <span ng-if="file.fileType == 'FOLDER' && hasPermission && hasCreate && (!isExternal || (isExternal && externalPermission  != 'READ'))"
                          ng-hide="(objectType == 'ITEM' && file.object != objectId)"
                          uib-dropdown dropdown-append-to-body style="width: 100%;">
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle title=" {{title}}"
                           ng-click="loadFolderFile(file)" style="padding-left: 3px;"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;"
                            id="folder-dropdown-{{file.id}}">
                            <li>
                                <a href="" ng-click="addFolder(file,'Add')"
                                   translate>ADD_FOLDER</a></li>
                            <li>
                                <label class="file-container"><span translate>ADD_FILES</span>
                                    <input type="file" id="{{file.id}}" style="width: 100%;height: 34px;"
                                           value="file" multiple="true" title="{{addFilesTitle}}"/>
                                </label>
                            </li>
                            <li ng-if="objectType != 'DOCUMENT'">
                                <a href="" ng-click="showDocuments(file)"><span translate>ADD_DOCUMENTS</span></a>
                            </li>
                            <li ng-if="clipboardFiles.length > 0">
                                <a href="" ng-click="pasteFilesFromClipboard(file,'FOLDER')">
                                    <span translate>PASTE_FROM_CLIPBOARD</span>({{clipboardFiles.length}})
                                </a>
                            </li>
                        </ul>
                    </span>
                    </td>
                    <td class="file-name sticky-col sticky-actions-col" style="z-index: 12">
                    <span class="level{{file.level}}" ng-show="file.fileType == 'FOLDER'"
                          ng-click="toggleNode(file)">
                        <i class="mr5 fa" title="{{ExpandCollapse}}"
                           style="cursor: pointer !important; font-size: 18px;color: limegreen !important;"
                           ng-class="{'fa-folder': (file.expanded == false || file.expanded == null || file.expanded == undefined),
                           'fa-folder-open': file.expanded == true}">
                        </i>
                        <span class="autoClick" title="{{file.name}}"
                              style="cursor: pointer !important;white-space: normal;word-wrap: break-word;"
                              ng-bind-html="file.name | highlightText: searchText">
                        </span>
                        <span class="label label-default"
                              style="font-size: 12px;background-color: #e4dddd;font-style: italic;"
                              ng-if="file.count > 0" ng-bind-html="file.count"></span>
                    </span>

                    <span class="imageTooltip level{{file.level}}" ng-show="file.fileType == 'FILE'">
                        <i ng-if="file.objectType != 'OBJECTDOCUMENT' && file.objectType != 'DOCUMENT'"
                           class="la la-arrow-circle-up" title="Uploaded File"
                           style="padding-right: 10px;"></i>
                        <i ng-if="file.objectType == 'OBJECTDOCUMENT'" class="la la-clone" title="Managed File"
                           style="padding-right: 10px;"></i>
                        <span ng-if="((hasDownload && !isExternal) || (isExternal && externalPermission == 'WRITE'))">
                                <a href="" ng-click="downloadFile(file)"
                                   title="{{file.objectType == 'OBJECTDOCUMENT' ? file.filePath : file.name}} - {{downloadFileTitle}}">
                                    <span ng-if="file.fileType == 'FILE'"
                                          ng-bind-html="file.name | highlightText: searchText"></span>
                                </a>
                        </span>

                         <span ng-if="((!hasDownload && !isExternal) || (isExternal && externalPermission == 'READ'))">
                             <span ng-if="file.fileType == 'FILE'"
                                   ng-bind-html="file.name | highlightText: searchText"></span>
                        </span>

                        <%--<div class="imageTooltiptext" ng-show="checkFileExt(file) && file.thumbnail != null">
                            <img src="data:image/png;base64,{{file.thumbnail}}"
                                 ng-click="showAutoDeskFile(file)"/>
                        </div>--%>
                    </span>
                    </td>
                    <td>
                        <span ng-if="file.fileType == 'FILE'" style="cursor: move">{{file.size.toFileSize()}}</span>
                    </td>
                    <td class="description-column">
                        <span ng-if="file.fileType == 'FILE'">{{file.description}}</span>
                        <span ng-if="file.fileType == 'FOLDER'">{{file.description}}</span>
                    </td>
                    <td style="text-align: center">
                        <span ng-if="file.fileType == 'FILE'">
                            <span ng-if="objectType == 'DOCUMENT' || file.objectType == 'OBJECTDOCUMENT' || objectType == 'MFRPARTINSPECTIONREPORT' || objectType == 'PPAPCHECKLIST'">{{file.revision}}.{{file.version}}
                                <item-status
                                        ng-if="file.objectType == 'OBJECTDOCUMENT' && objectType != 'MFRPARTINSPECTIONREPORT' && objectType != 'PPAPCHECKLIST'"
                                        item="file"></item-status>
                            </span>
                            <span ng-if="objectType != 'DOCUMENT' && file.objectType != 'OBJECTDOCUMENT' && objectType != 'MFRPARTINSPECTIONREPORT' && objectType != 'PPAPCHECKLIST'">{{file.version}}</span>
                        </span>
                    </td>
                    <td style="text-align: center"
                        ng-if="objectType == 'DOCUMENT' || objectType == 'MFRPARTINSPECTIONREPORT' || objectType == 'PPAPCHECKLIST'">
                        <span ng-if="file.fileType == 'FILE'">
                            <item-status item="file"></item-status>
                        </span>
                        <item-status ng-if="file.fileType == 'FOLDER' && objectType == 'PPAPCHECKLIST'"
                                     item="file"></item-status>
                    </td>
                    <td>{{file.createdDate}}</td>
                    <td>{{file.createdByName}}</td>
                    <td>{{file.modifiedDate}}</td>
                    <td>{{file.modifiedByName}}</td>
                    <td style="text-align: center;min-width: 120px;">
                        <input type="checkbox" ng-model="file.locked" ng-if="file.fileType == 'FILE'"
                               ng-disabled="(file.locked && (file.lockedBy != personDetails.id)) || file.objectType == 'OBJECTDOCUMENT'"
                               ng-change="lockFile(file, file.locked)">
                    </td>
                    <td>
                        <span ng-if="file.fileType != 'FOLDER'">{{file.lockedByName}}</span>
                    </td>
                    <td class="actions-col">
                     <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                           ng-hide="file.fileType == 'FOLDER' && (!hasPermission || (objectType == 'ITEM' && file.object != objectId))">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle
                           ng-click="loadFileDropdown(file)"></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;" id="file-dropdown-{{file.id}}">
                                <li ng-class="{'cursor-override': !hasSignOff}"
                                    ng-if="(file.fileType == 'FILE' && (file.objectType == 'DOCUMENT' || file.objectType == 'MFRPARTINSPECTIONREPORT' || file.objectType == 'PPAPCHECKLIST') && (file.signOffCount > 0 || file.lifeCyclePhase.phaseType != 'RELEASED')) ||
                                           (file.fileType == 'FOLDER' && file.objectType == 'PPAPCHECKLIST' && (file.signOffCount > 0 || file.lifeCyclePhase.phaseType != 'RELEASED'))"
                                    title="{{hasSignOff ? '' : noPermission}}">
                                    <a ng-if="!file.lockObject" ng-click="showReviewers(file)"
                                       ng-class="{'disabled': !hasSignOff || file.locked || !hasPermission}"
                                       style="color: #333 !important;">
                                        <span translate>SIGN_OFF</span>
                                        <span class="label label-default label-count"
                                              ng-if="file.signOffCount > 0"
                                              style="font-size: 12px;background-color: #e4dddd;padding: 1px 4px !important;margin-left: 3px;"
                                              ng-bind-html="file.signOffCount"></span>
                                    </a>
                                </li>
                                <li ng-if="(file.fileType == 'FILE' || (file.fileType == 'FOLDER' && file.objectType == 'PPAPCHECKLIST')) && (file.objectType == 'DOCUMENT' || file.objectType == 'MFRPARTINSPECTIONREPORT' || file.objectType == 'PPAPCHECKLIST') && file.lifeCyclePhase.phaseType == 'REVIEW' && file.approver">
                                    <a ng-if="!file.lockObject" ng-click="showDocumentSubmitDialog('Approve',file)"
                                       style="color: #333 !important;" translate>APPROVE</a>
                                </li>
                                <li ng-if="(file.fileType == 'FILE' || (file.fileType == 'FOLDER' && file.objectType == 'PPAPCHECKLIST')) && (file.objectType == 'DOCUMENT' || file.objectType == 'MFRPARTINSPECTIONREPORT' || file.objectType == 'PPAPCHECKLIST') && file.lifeCyclePhase.phaseType == 'REVIEW' && file.approver">
                                    <a ng-if="!file.lockObject" ng-click="showDocumentSubmitDialog('Reject',file)"
                                       style="color: #333 !important;" translate>REJECT</a>
                                </li>
                                <li ng-if="(file.fileType == 'FILE' || (file.fileType == 'FOLDER' && file.objectType == 'PPAPCHECKLIST')) && (file.objectType == 'DOCUMENT' || file.objectType == 'MFRPARTINSPECTIONREPORT' || file.objectType == 'PPAPCHECKLIST') && file.lifeCyclePhase.phaseType == 'REVIEW' && file.reviewer">
                                    <a ng-if="!file.lockObject" ng-click="showDocumentSubmitDialog('Review',file)"
                                       style="color: #333 !important;" translate>REVIEW</a>
                                </li>
                                <li ng-class="{'cursor-override': !hasPromote}"
                                    ng-if="(file.fileType == 'FILE' || (file.fileType == 'FOLDER' && file.objectType == 'PPAPCHECKLIST')) && (file.objectType == 'DOCUMENT' || file.objectType == 'MFRPARTINSPECTIONREPORT' || file.objectType == 'PPAPCHECKLIST') && (file.lifeCyclePhase.phaseType == 'PRELIMINARY' || file.lifeCyclePhase.phaseType == 'REVIEW')"
                                    title="{{hasPromote ? '' : noPermission}}">
                                    <a ng-if="!file.lockObject" ng-click="promoteDocument(file)"
                                       ng-class="{'disabled': !hasPromote || file.locked || !hasPermission || (file.fileType == 'FOLDER' && file.objectType == 'PPAPCHECKLIST' && file.childFileCount == 0)}"
                                       style="color: #333 !important;" translate>PROMOTE</a>
                                </li>
                                <li ng-class="{'cursor-override': !hasDemote}"
                                    ng-if="(file.fileType == 'FILE' || (file.fileType == 'FOLDER' && file.objectType == 'PPAPCHECKLIST')) && (file.objectType == 'DOCUMENT' || file.objectType == 'MFRPARTINSPECTIONREPORT' || file.objectType == 'PPAPCHECKLIST') && file.lifeCyclePhase.phaseType == 'REVIEW'"
                                    title="{{hasDemote ? '' : noPermission}}">
                                    <a ng-if="!file.lockObject" ng-click="demoteDocument(file)"
                                       ng-class="{'disabled': !hasDemote || file.locked || !hasPermission}"
                                       style="color: #333 !important;" translate>DEMOTE</a>
                                </li>
                                <li ng-class="{'cursor-override': !hasRevise}"
                                    ng-if="(file.fileType == 'FILE') && (file.objectType == 'DOCUMENT' || file.objectType == 'MFRPARTINSPECTIONREPORT' || file.objectType == 'PPAPCHECKLIST') && file.lifeCyclePhase.phaseType == 'RELEASED'"
                                    title="{{hasRevise ? '' : noPermission}}">
                                    <a ng-if="!file.lockObject" ng-click="reviseDocument(file)"
                                       ng-class="{'disabled': !hasRevise || file.locked || !hasPermission || (file.objectType == 'PPAPCHECKLIST' && file.parentFolder.lifeCyclePhase.phaseType == 'RELEASED')}"
                                       style="color: #333 !important;" translate>REVISE</a>
                                </li>
                                <li ng-class="{'cursor-override': !hasEdit}"
                                    ng-if="file.fileType == 'FILE' && file.objectType != 'OBJECTDOCUMENT' && (!isExternal || (isExternal && externalPermission == 'WRITE'))"
                                    title="{{hasEdit ? '' : noPermission}}">
                                    <a ng-if="!file.lockObject" ng-click="showFileEdit(file)"
                                       ng-class="{'disabled': !hasEdit || file.locked || !hasPermission
                                       || (objectType == 'ITEM' && file.object != objectId)}"
                                       style="color: #333 !important;" translate>EDIT_FILE_TITLE</a>
                                </li>
                                <li ng-if="file.fileType == 'FOLDER' && hasPermission && (!isExternal || (isExternal && externalPermission == 'WRITE'))">
                                    <a ng-click="addItemFolder(file,'update')"
                                       style="color: #333 !important;" translate>UPDATE_FOLDER</a>
                                </li>
                                <li ng-class="{'cursor-override': file.latest}"
                                    title="{{file.latest ? '':'Update to latest version'}}"
                                    ng-if="file.objectType == 'OBJECTDOCUMENT' && hasPermission && (!isExternal || (isExternal && externalPermission == 'WRITE'))">
                                    <a ng-click="updateToLatestDocument(file)"
                                       ng-class="{'disabled': file.latest}"
                                       style="color: #333 !important;" translate>UPDATE_TO_LATEST</a>
                                </li>
                                <%--<li ng-click="showAutoDeskFile(file)"
                                    ng-show="checkFileExt(file)">
                                    <a href="" style="color: #333 !important;" translate>SHOW_CAD_FILE</a>
                                </li>--%>
                                <li ng-click="setFileAsItemImage(file)"
                                    ng-show="objectType == 'ITEM' && (item.thumbnail == '' || item.thumbnail == null) && file.thumbnail != null && file.type == 'FILE'">
                                    <a href="" style="color: #333 !important;" translate>SET_FILE_AS_ITEM_IMAGE</a>
                                </li>
                                <li ng-class="{'cursor-override': !hasReplace}"
                                    ng-if="((hasPermission && file.fileType == 'FILE') || (file.locked && file.lockedBy == personDetails.id)) && (!isExternal || (isExternal && externalPermission == 'WRITE'))"
                                    ng-hide="file.objectType == 'OBJECTDOCUMENT' || file.lifeCyclePhase.phaseType == 'RELEASED'"
                                    title="{{hasReplace ? '' : noPermission}}">
                                    <a href="" ng-click="fileReplace(file)"
                                       ng-class="{'disabled': !hasReplace || file.locked || !hasPermission || (objectType == 'ITEM' && file.object != objectId)}"
                                       style="color: #333 !important;">
                                        <span ng-if="objectType != 'MFRPARTINSPECTIONREPORT'" translate>FILE_REPLACE_TITLE</span>
                                        <span ng-if="objectType == 'MFRPARTINSPECTIONREPORT'" translate>REPLACE_REPORT_TITLE</span>
                                    </a>
                                </li>
                                <li ng-click="showFileHistory(file)"
                                    ng-if="file.fileType == 'FILE' || file.objectType == 'OBJECTDOCUMENT'">
                                    <a href="" style="color: #333 !important;">
                                        <span ng-if="objectType != 'MFRPARTINSPECTIONREPORT'" translate>FILE_HISTORY_TITLE</span>
                                        <span ng-if="objectType == 'MFRPARTINSPECTIONREPORT'" translate>REPORT_HISTORY_TITLE</span>
                                    </a>
                                </li>
                                <li ng-click="showFileDownloadHistory(file)"
                                    ng-if="file.fileType == 'FILE'|| file.objectType == 'OBJECTDOCUMENT'">
                                    <a href=""
                                       style="color: #333 !important;">
                                        <span ng-if="objectType != 'MFRPARTINSPECTIONREPORT'" translate>FILE_DOWNLOAD_HISTORY_TITLE</span>
                                        <span ng-if="objectType == 'MFRPARTINSPECTIONREPORT'" translate>REPORT_DOWNLOAD_HISTORY_TITLE</span>
                                    </a>
                                </li>
                                <li ng-class="{'cursor-override': !hasRename}"
                                    ng-if="((hasPermission && file.fileType == 'FILE') || (file.locked && file.lockedBy == personDetails.id)) && (!isExternal || (isExternal && externalPermission == 'WRITE'))"
                                    ng-hide="file.objectType == 'OBJECTDOCUMENT' || file.lifeCyclePhase.phaseType == 'RELEASED'"
                                    title="{{hasRename ? '' : noPermission}}">
                                    <a href="" ng-click="selectFiletoRename(file)"
                                       ng-class="{'disabled': !hasRename || file.locked || (objectType == 'ITEM' && file.object != objectId)}"
                                       style="color: #333 !important;">
                                        <span ng-if="objectType != 'MFRPARTINSPECTIONREPORT'" translate>FILE_RENAME_TITLE</span>
                                        <span ng-if="objectType == 'MFRPARTINSPECTIONREPORT'" translate>RENAME_REPORT_TITLE</span>
                                    </a>
                                </li>
                                <li title="{{hasDelete ? '' : noPermission}}"
                                    ng-if="hasPermission && file.fileType == 'FILE' && (!isExternal || (isExternal && externalPermission == 'WRITE'))"
                                    ng-class="{'cursor-override': (!hasDelete && !isExternal) || (!hasPermission && isExternal)}"
                                    ng-hide="file.lifeCyclePhase.phaseType != 'PRELIMINARY' && file.objectType != 'OBJECTDOCUMENT'">
                                    <a href="" ng-click="deleteFile(file)"
                                       style="color: #333 !important;"
                                       ng-class="{'disabled': (!hasDelete && !isExternal) || (isExternal && file.createdBy != personDetails.id) || file.locked || !hasPermission || (objectType == 'ITEM' && file.object != objectId)}">
                                        <span ng-if="file.objectType != 'OBJECTDOCUMENT'" translate>DELETE</span>
                                        <span ng-if="file.objectType == 'OBJECTDOCUMENT'" translate>REMOVE</span>
                                    </a>
                                </li>
                                <li ng-if="file.fileType == 'FOLDER' && hasPermission && (!isExternal || (isExternal && externalPermission == 'WRITE'))"
                                    ng-hide="(objectType == 'ITEM' && file.object != objectId) || file.lifeCyclePhase.phaseType != 'PRELIMINARY'"
                                    ng-click="deleteFolder(file)">
                                    <a href="" style="color: #333 !important;" translate>DELETE_FOLDER</a>
                                </li>
                                <plugin-table-actions context="object.file" object-value="file"></plugin-table-actions>
                            </ul>
                     </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <div id="inspectionreport-modal" class="inspectionreport-model modal">
        <div class="documentModel-content">
            <div class="document-header">
                <span>{{documentApproveType}}</span>
            </div>
            <div class="document-content">
                <p ng-if="selectedDocumentReviewer.notes == null && error != ''"
                   style="margin-left: 80px; color: red;width:auto;font-size: 14px;">{{error}}
                </p>

                <div class="form-group">
                    <label class="col-sm-4 control-label" style="text-align: right;margin-top: 8px;">
                        <span translate>COMMENT</span>
                        <span class="asterisk">*</span> : </label>

                    <div class="col-sm-7">
                        <textarea type="req" class="form-control input-sm" style="resize: none"
                                  placeholder="{{'ENTER_COMMENT' | translate}}" rows="3"
                                  ng-model="selectedDocumentReviewer.notes"></textarea>
                    </div>
                </div>
            </div>
            <div class="document-footer">
                <button class="btn btn-xs btn-default"
                        ng-click="hideDocumentSubmitDialog()" translate>
                    CANCEL
                </button>
                <button class="btn btn-xs btn-success"
                        ng-click="submitDocumentReview()" translate>
                    SUBMIT
                </button>
            </div>
        </div>
    </div>
</div>

<div>
    <style scoped>

        .forge-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            /*  z-index: 1; /!* Sit on top *!/*/
            /*padding-top: 100px; /!* Location of the box *!/*/
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .forge-model .closeImage1 {
            position: absolute;
            top: 10px !important;
            right: 10px !important;;
            color: blanchedalmond;
            font-size: 30px;
            font-weight: bold;
            transition: 0.3s;
            border: 1px solid lightgrey;
            border-radius: 50%;
            background: #988282a6;
            padding: 5px;
        }

        .forge-model .closeImage1:hover,
        .forge-model .closeImage1:focus {
            color: white;
            text-decoration: none;
            cursor: pointer;
        }

        #forgeFrame {
            width: 100%;
            height: 96%;
            margin: 0;
            background-color: #F0F8FF;
        }
    </style>
    <div id="forgeModel" class="forge-model modal">
        <span class="closeImage1">&times;</span>
        <iframe id="forgeFrame"
                src=""
                frameborder="0" height="100%" width="100%"></iframe>
    </div>
</div>

