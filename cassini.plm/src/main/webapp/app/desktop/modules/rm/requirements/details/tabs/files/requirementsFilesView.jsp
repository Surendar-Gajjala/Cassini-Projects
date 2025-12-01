<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style scoped>

    table.highlight-row tr.dropzone-row:hover, table.highlight-row tr.dropzone-row:hover td {
        background-color: inherit !important;
        background-image: none !important;
        color: rgb(99, 110, 123) !important;
    }

    .description {
        word-wrap: break-word;
        max-width: 400px;
        white-space: normal !important;
        text-align: left;
    }

    .fileContainer {
        overflow: hidden;
        position: relative;
        width: 100%;
        cursor: pointer;
        margin-bottom: 0 !important;
        float: left;
        padding: 7px 10px;
        height: 34px;
    }

    .fileContainer:hover {
        background-color: #e7e7e7;

    }

    .fileContainer [type=file] {
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

</style>
<div ng-show="reqFilesVm.showDropzone == false" id="selectReqFiles">
    <div class="responsive-table">
        <div id="parentFile" style="font-style: italic; text-align: center;"
             ng-hide="selectedRequirement.status == 'FINISHED'"
             ng-if="hasPermission('pgcspecification','edit') || specPermission.editPermission" dragfile
             files="reqFilesVm.files" update-file="reqFilesVm.updateFile">
            {{dragAndDropFilesTitle}} <a href="" ng-click="reqFilesVm.selectReqFile()">({{clickToAddFilesTitle}})</a>
        </div>

        <div>
            <tr id="attachmentsContainer" ng-show="reqFilesVm.showFileDropzone == true"
                class="attachments-dropzone-container">
                <div id="attachmentsDropZone" style="display: table; width: 100%;">
                    <div id="previews">
                        <div id="template">
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


        <table id="itemFilesTable" class="table table-striped highlight-row">
            <thead>
            <tr>
                <th>
                    <span uib-dropdown dropdown-append-to-body style="width: 100%;"
                          ng-hide="selectedRequirement.status == 'FINISHED'">
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                           style="padding-left: 3px;" title="{{addFolderAndClipboard}}"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li>
                                <a href="" ng-click="addReqFolder()"
                                   translate>NEW_FOLDER</a>
                            </li>
                            <li ng-if="clipBoardReqFiles.length > 0">
                                <a href=""
                                   ng-click="reqFilesVm.pasteFilesFromClipboard(reqFilesVm.requirementId,'OBJECT')"
                                   translate>PASTE_FROM_CLIPBOARD</a>
                            </li>
                        </ul>
                    </span>
                </th>
                <th translate>TAB_FILES_FILE_NAME</th>
                <th translate>TAB_FILES_FILE_SIZE</th>
                <th translate>DESCRIPTION</th>
                <th style="text-align: center" translate>TAB_FILES_VERSION</th>
                <th translate>CREATED_DATE</th>
                <th translate>CREATED_BY</th>
                <th translate>MODIFIED_DATE</th>
                <th translate>MODIFIED_BY</th>
                <th style="text-align: center" translate>TAB_FILES_IS_LOCKED</th>
                <th translate>TAB_FILES_LOCKED_BY</th>
                <th style="text-align: center;min-width: 130px;" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="reqFilesVm.loading == true">
                <td colspan="15"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_FILES</span>
                </td>
            </tr>
            <tr ng-if="reqFilesVm.loading == false && reqFilesVm.files.length == 0">
                <td colspan="15" style="color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Files.png" alt="" class="image">

                        <div class="message">{{ 'NO_FILES' | translate}}</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                    </div>
                </td>
            </tr>
            <tr id="{{$index}}" ng-if="reqFilesVm.files.length > 0"
                ng-repeat="file in reqFilesVm.files" dragfile files="reqFilesVm.files"
                update-file="reqFilesVm.updateFile">
                <td style="text-align: center;display: flex;">
                    <div ng-show="file.fileType == 'FILE'" class="ckbox ckbox-default"
                         style="display: inline-block;">
                        <input id="file{{$index}}" name="fileSelected" type="checkbox" ng-value="true"
                               ng-model="file.selected" ng-change="reqFilesVm.selectClipboardFile(file)">
                        <label for="file{{$index}}" class="item-selection-checkbox"></label>
                    </div>
                   <span style="text-align: center;width: 100%;" ng-show="file.fileType != 'FOLDER'"
                         ng-click="reqFilesVm.filePreview(file)" title="{{reqFilesVm.previewFile}}">
                        <i class="{{getIcon(file.name)}}" ng-style="{{getIconColor(file.name)}}"></i>
                    </span>
                    <span ng-show="file.fileType == 'FOLDER'" uib-dropdown dropdown-append-to-body
                          style="width: 100%;text-align: left !important;">
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                           title="{{reqFilesVm.title}}" ng-click="reqFilesVm.loadFolderFile(file)"
                           style="padding-left: 3px"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li>
                                <a href="" ng-click="reqFilesVm.addFolder(file)"
                                   translate>ADD_FOLDER</a></li>
                            <li>
                                <label class="fileContainer"><span translate>ADD_FILES</span>
                                    <input type="file" id="{{file.id}}"
                                           value="file" multiple="true"
                                           title="{{'UPLOAD_FILES' | translate}}"/>
                                </label>
                            </li>
                            <li ng-if="clipBoardReqFiles.length > 0">
                                <a href="" ng-click="reqFilesVm.pasteFilesFromClipboard(file,'FOLDER')"
                                   translate>PASTE_FROM_CLIPBOARD</a>
                            </li>
                        </ul>
                  </span>
                </td>
                <td>
                    <span class="level{{file.level}}" ng-show="file.fileType == 'FOLDER'">
                        <i class="mr5 fa" title="{{reqFilesVm.ExpandCollapse}}"
                           style="cursor: pointer !important; font-size: 18px;color: limegreen !important;"
                           ng-class="{'fa-folder': (file.expanded == false || file.expanded == null || file.expanded == undefined),
                                          'fa-folder-open': file.expanded == true}"
                           ng-click="reqFilesVm.toggleNode(file)">
                            <%--<span class="folder-count">{{file.count}}</span>--%>
                        </i>
                            <span ng-click="reqFilesVm.toggleNode(file)"
                                  class="autoClick"
                                  style="cursor: pointer !important;white-space: normal;word-wrap: break-word;"
                                  ng-bind-html="file.name"></span>
                            <span class="label label-default"
                                  style="font-size: 12px;background-color: #e4dddd;font-style: italic;"
                                  ng-if="file.count > 0" ng-bind-html="file.count"></span>
                    </span>
                    <span class="level{{file.level}}" ng-show="file.fileType == 'FILE'">
                        <a href="" ng-click="reqFilesVm.downloadFile(file)"
                           title="{{downloadFileTitle}}">
                            <span ng-bind-html="file.name | highlightText: freeTextQuerys"></span>
                        </a>
                    </span>
                </td>
                <td><span ng-show="file.fileType == 'FILE'">{{file.size.toFileSize()}}</span></td>
                <td class="description">{{file.description}}</td>
                <td style="text-align: center"><span ng-show="file.fileType == 'FILE'">{{file.version}}</span></td>
                <td>{{file.createdDate}}</td>
                <td ng-if="file.fileType == 'FILE'">{{file.createdByObject.firstName}}</td>
                <td ng-if="file.fileType == 'FOLDER'">{{file.createdByObject.firstName}}</td>
                <td>{{file.modifiedDate}}</td>
                <td ng-if="file.fileType == 'FILE'">{{file.modifiedByObject.firstName}}</td>
                <td ng-if="file.fileType == 'FOLDER'">{{file.modifiedByObject.firstName}}</td>
                <td style="text-align: center">
                    <input type="checkbox" ng-model="file.locked" ng-if="file.fileType == 'FILE'"
                           ng-disabled="selectedSpecification.lifecyclePhase.phaseType == 'RELEASED'"
                           ng-change="reqFilesVm.lockFile(file, file.locked)">
                </td>
                <td>{{file.lockedByObject.firstName}}</td>
                <td class="text-center">
                    <span class="row-menu" uib-dropdown style="min-width: 50px">
                        <span dropdown-append-to-body></span>
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-click="reqFilesVm.showFileEdit(file)"
                                    ng-if="(hasPermission('pgcspecification','edit') || specPermission.editPermission) && selectedSpecification.lifecyclePhase.phaseType != 'RELEASED' && file.fileType == 'FILE'"
                                    ng-hide="file.locked">
                                    <a href="" style="color: #333 !important;" translate>EDIT_FILE_TITLE</a>
                                </li>
                                <li ng-click="reqFilesVm.fileReplace(file)"
                                    ng-if="(hasPermission('pgcspecification','edit') || specPermission.editPermission) && selectedSpecification.lifecyclePhase.phaseType != 'RELEASED' && file.fileType == 'FILE'"
                                    ng-hide="file.locked || selectedRequirement.status == 'FINISHED'">
                                    <a href="" style="color: #333 !important;" translate>FILE_REPLACE_TITLE</a>
                                </li>
                                <li ng-click="reqFilesVm.showFileHistory(file)" ng-if="file.fileType == 'FILE'">
                                    <a href="" style="color: #333 !important;" translate>FILE_HISTORY_TITLE</a>
                                </li>
                                <li ng-click="reqFilesVm.showFileDownloadHistory(file)" ng-if="file.fileType == 'FILE'">
                                    <a href="" style="color: #333 !important;" translate>FILE_DOWNLOAD_HISTORY_TITLE</a>
                                </li>
                                <li ng-click="reqFilesVm.renameFile(file)"
                                    ng-if="(hasPermission('pgcspecification','edit') || specPermission.editPermission) && selectedSpecification.lifecyclePhase.phaseType != 'RELEASED' && file.fileType == 'FILE'"
                                    ng-hide="file.locked || selectedRequirement.status =='FINISHED'">
                                    <a href="" style="color: #333 !important;" translate>FILE_RENAME_TITLE</a>
                                </li>
                                <li ng-hide="file.locked"
                                    ng-if="(hasPermission('pgcspecification','delete') || specPermission.deletePermission) && selectedSpecification.lifecyclePhase.phaseType != 'RELEASED' && file.fileType == 'FILE'"
                                    ng-click="reqFilesVm.deleteFile(file)">
                                    <a href="" style="color: #333 !important;" translate>FILE_DELETE_TITLE</a>
                                </li>
                                <li ng-hide="file.locked"
                                    ng-if="(hasPermission('pgcspecification','delete') || specPermission.deletePermission) && selectedSpecification.lifecyclePhase.phaseType != 'RELEASED' && file.fileType == 'FOLDER'"
                                    ng-click="reqFilesVm.deleteFolder(file)">
                                    <a href="" style="color: #333 !important;" translate>DELETE_FOLDER</a>
                                </li>
                            </ul>
                    </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
