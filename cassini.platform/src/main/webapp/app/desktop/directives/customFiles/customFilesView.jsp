<div>
    <style scoped>

        table.highlight-row tr.dropzone-row:hover, table.highlight-row tr.dropzone-row:hover td {
            background-color: inherit !important;
            background-image: none !important;
            color: rgb(99, 110, 123) !important;
        }

        .responsive-table .dropdown-content {
            margin-left: -80px !important;
        }

        .responsive-table .dropdown-content a i {

        }

        body {
            margin: 0;
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

        .fileContainer:hover {
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
        }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link rel="stylesheet" href="https://developer.api.autodesk.com/modelderivative/v2/viewers/style.min.css"
          type="text/css">
    <div id="folderFiles" style="display: none;">

    </div>
    <div ng-show="showDropzone == false" id="itemFiles">
        <div class="responsive-table" id="itemFilesTableContainer">
            <div style="display: flex;width: 100%;">
                <div id="parentFile" style="font-style: italic; text-align: center;width: 100%;padding: 10px;" dragfile
                     files="files" update-file="updateFile"
                     ng-if="hasPermission && hasCreate">
                    {{dragAndDropFilesTitle}}
                    <a href="" ng-click="selectFiles()">({{clickToAddFilesTitle}})</a>
                </div>
                <div style="font-style: italic; text-align: center;width: 100%;padding: 10px;"
                     ng-if="(sharedPermission == 'WRITE' && external.external == true) && hasCreate">
                    {{dragAndDropFilesTitle}}
                    <a href="" ng-click="selectFiles()">({{clickToAddFilesTitle}})</a>
                </div>
            </div>

            <div>
                <tr id="attachmentsContainer" ng-show="showFileDropzone == true"
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
                    <th style="text-align: center;">
                    <span uib-dropdown dropdown-append-to-body style="width: 100%;" ng-if="hasPermission && hasCreate">
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                           title="{{addFolderAndClipboard}}" style="padding-left: 3px;"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li>
                                <a href="" ng-click="addItemFolder(null,'Add')"
                                   translate>NEW_FOLDER</a>
                            </li>
                            <li ng-if="clipBoardChangesFiles.length > 0">
                                <a href=""
                                   ng-click="pasteFilesFromClipboard(objectId,objectType)">
                                    <span translate>PASTE_FROM_CLIPBOARD</span> ({{clipBoardCustomFiles.length}})</a>
                            </li>
                        </ul>
                    </span>
                    </th>
                    <th class="name-column" translate>TAB_FILES_FILE_NAME</th>
                    <th translate>TAB_FILES_FILE_SIZE</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th style="text-align: center" translate>TAB_FILES_VERSION</th>
                    <th translate>CREATED_DATE</th>
                    <th translate>CREATED_BY</th>
                    <th translate>MODIFIED_DATE</th>
                    <th translate>MODIFIED_BY</th>
                    <th style="text-align: center" translate>TAB_FILES_IS_LOCKED</th>
                    <th translate>TAB_FILES_LOCKED_BY</th>
                    <th style="text-align:center; width: 80px;"
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
                <tr ng-if="showFileDropzone == false && loading == false && files.length == 0">
                    <td colspan="14">
                        <span ng-if="searchText == null || searchText == ''" translate>NO_FILES</span>
                        <span ng-if="searchText != null && searchText != ''" translate>NO_SEARCH_RESULT_FOUND</span>
                    </td>
                </tr>
                <tr id="{{$index}}" ng-show="showFileDropzone == false && files.length > 0"
                    ng-repeat="file in files" dragfile files="files"
                    update-file="updateFile">

                    <td style="display: flex;">
                        <div class="ckbox ckbox-default" ng-hide="file.fileType == 'FOLDER'"
                             style="display: inline-block;">
                            <input id="file{{$index}}" name="fileSelected" type="checkbox" ng-value="true"
                                   ng-model="file.selected" ng-change="selectFile(file)">
                            <label for="file{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    <span style="text-align: center;width: 100%;" ng-show="file.fileType != 'FOLDER' && hasPreview"
                          ng-click="filePreview(file)" title="{{previewFile}}">
                        <i class="{{getIcon(file.name)}}" ng-style="{{getIconColor(file.name)}}"></i>
                    </span>
                    <span ng-show="file.fileType == 'FOLDER' && hasPermission" uib-dropdown dropdown-append-to-body
                          style="width: 100%;">
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle title=" {{title}}"
                           ng-click="loadFolderFile(file)" style="padding-left: 3px;"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li>
                                <a href="" ng-click="addFolder(file,'Add')"
                                   translate>ADD_FOLDER</a></li>
                            <li>
                                <label class="fileContainer"><span translate>ADD_FILES</span>
                                    <input type="file" id="{{file.id}}" style="width: 100%;height: 34px;"
                                           value="file" multiple="true"
                                           title="{{addFilesTitle}}"/>
                                </label>
                            </li>
                            <li ng-if="clipBoardChangesFiles.length > 0">
                                <a href="" ng-click="pasteFilesFromClipboard(file,'FOLDER')">
                                    <span translate>PASTE_FROM_CLIPBOARD</span> ({{clipBoardCustomFiles.length}})</a>
                            </li>
                        </ul>
                    </span>
                    </td>
                    <td>
                    <span class="level{{file.level}}" ng-show="file.fileType == 'FOLDER'"
                          ng-click="toggleNode(file)">
                        <i class="mr5 fa" title="{{ExpandCollapse}}"
                           style="cursor: pointer !important; font-size: 18px;color: limegreen !important;"
                           ng-class="{'fa-folder': (file.expanded == false || file.expanded == null || file.expanded == undefined),
                           'fa-folder-open': file.expanded == true}">
                        </i>
                        <span class="autoClick"
                              style="cursor: pointer !important;white-space: normal;word-wrap: break-word;"
                              ng-bind-html="file.name | highlightText: searchText">
                        </span>
                        <span class="label label-default"
                              style="font-size: 12px;background-color: #e4dddd;font-style: italic;"
                              ng-if="file.count > 0" ng-bind-html="file.count"></span>
                    </span>

                    <span class="imageTooltip level{{file.level}}" ng-show="file.fileType == 'FILE'">
                        <span ng-if="hasDownload">
                                <a href="" ng-click="downloadFile(file)" title="{{downloadFileTitle}}">
                                    <span ng-bind-html="file.name | highlightText: searchText"></span>
                                </a>
                        </span>

                         <span ng-if="!hasDownload">
                                    <span ng-bind-html="file.name | highlightText: searchText"></span>
                        </span>

                        <div class="imageTooltiptext" ng-show="file.thumbnail != null">
                            <img src="data:image/png;base64,{{file.thumbnail}}"
                                 ng-click="showAutoDeskFile(file)"/>
                        </div>
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
                        <span ng-if="file.fileType == 'FILE'">{{file.version}}</span>
                    </td>
                    <td>{{file.createdDate}}</td>
                    <td>{{file.createdByObject.firstName}}</td>
                    <td>{{file.modifiedDate}}</td>
                    <td>{{file.modifiedByObject.firstName}}</td>
                    <td style="text-align: center;min-width: 120px;">
                        <input type="checkbox" ng-model="file.locked" ng-if="file.fileType == 'FILE'"
                               ng-disabled="(file.locked && file.lockedBy != personDetails.id)"
                               ng-change="lockFile(file, file.locked)">
                    </td>
                    <td>
                        <span ng-if="file.fileType != 'FOLDER'">{{file.lockedByObject.firstName}}</span>
                    </td>
                    <td class="text-center">
                     <span class="row-menu" uib-dropdown style="min-width: 50px"
                           ng-hide="file.fileType == 'FOLDER' && !hasPermission">
                         <span dropdown-append-to-body></span>
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-class="{'cursor-override': !hasEdit}"
                                    title="{{hasEdit ? '' : noPermission}}">
                                    <a ng-if="!file.lockObject" ng-click="showFileEdit(file)"
                                       ng-hide="file.locked || file.fileType != 'FILE' || !hasPermission"
                                       ng-class="{'disabled': !hasEdit}" style="color: #333 !important;" translate>EDIT_FILE_TITLE</a>
                                </li>
                                <li ng-if="file.fileType == 'FOLDER' && hasPermission">
                                    <a ng-click="addItemFolder(file,'update')"
                                       style="color: #333 !important;" translate>UPDATE_FOLDER</a>
                                </li>
                                <li ng-click="showAutoDeskFile(file)"
                                    ng-show="file.urn != null && file.fileType == 'FILE'">
                                    <a href="" style="color: #333 !important;" translate>SHOW_FILE</a>
                                </li>
                                <li ng-class="{'cursor-override': !hasReplace}"
                                    title="{{hasReplace ? '' : noPermission}}">
                                    <a ng-if="(hasPermission && file.fileType == 'FILE') || (file.locked && file.lockedBy == personDetails.id)"
                                       ng-class="{'disabled': !hasReplace}" ng-click="fileReplace(file)"
                                       ng-hide="file.locked || !hasPermission" href="" style="color: #333 !important;"
                                       translate>FILE_REPLACE_TITLE</a>
                                </li>
                                <li ng-click="showFileHistory(file)" ng-if="file.fileType == 'FILE'">
                                    <a href="" style="color: #333 !important;" translate>FILE_HISTORY_TITLE</a>
                                </li>
                                <li ng-click="showFileDownloadHistory(file)"
                                    ng-if="file.fileType == 'FILE'">
                                    <a href=""
                                       style="color: #333 !important;" translate>FILE_DOWNLOAD_HISTORY_TITLE</a>
                                </li>
                                <li ng-class="{'cursor-override': !hasRename}"
                                    title="{{hasRename ? '' : noPermission}}">
                                    <a ng-click="selectFiletoRename(file)"
                                       ng-hide="file.locked || !hasPermission"
                                       ng-if="(hasPermission && file.fileType == 'FILE') || (file.locked && file.lockedBy == personDetails.id)"
                                       ng-class="{'disabled': !hasRename}" href="" style="color: #333 !important;"
                                       translate>FILE_RENAME_TITLE</a>
                                </li>
                                <li ng-class="{'cursor-override': !hasDelete}"
                                    title="{{hasDelete ? '' : noPermission}}">
                                    <a ng-if="hasPermission && file.fileType == 'FILE'"
                                       ng-hide="file.locked || !hasPermission"
                                       ng-class="{'disabled': !hasDelete}" ng-click="deleteFile(file)" href=""
                                       style="color: #333 !important;" translate>FILE_DELETE_TITLE</a>
                                </li>
                                <li ng-if="file.fileType == 'FOLDER' && hasPermission"
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
</div>