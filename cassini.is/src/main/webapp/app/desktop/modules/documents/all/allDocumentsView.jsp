<style scoped>
    .pane-visible {
        visibility: visible !important;
    }

    .pane-hidden {
        visibility: hidden !important;;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        z-index: 5;
    }

    table {
        table-layout: fixed;
    }

    .responsive-table {
        width: 100%;
        margin-bottom: 0;
        padding-bottom: 20px;
        overflow-x: inherit;
        overflow-y: inherit;
        -webkit-overflow-scrolling: touch;
        -ms-overflow-style: -ms-autohiding-scrollbar;
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

    .imageTooltip a {
        display: block;
        width: 200px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
    }


</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="col-sm-10" <%--ng-show="allDocumentsVm.selectedFolder == null"--%>>
            <div ng-show="allDocumentsVm.showDropzone == false && allDocumentsVm.permission == false">
                <span style="font-size: 20px;font-weight: bold;color: black;padding: 13px 10px 14px;vertical-align: middle;border-right: 1px solid lightgray;">{{viewInfo.title}}</span>

                <div class="btn-group">
                    <button class="btn btn-sm btn-warning min-width"
                            ng-disabled="!(hasPermission('permission.topDocuments.addPermissions') || allDocumentsVm.loginPerson.isAdmin)"
                            ng-click="allDocumentsVm.addPermission()">Permissions
                    </button>
                </div>
            </div>
        </div>
        <div class="col-sm-10" ng-show="allDocumentsVm.permission">
            <span style="font-size: 20px;font-weight: bold;color: black;padding: 13px 10px 14px;vertical-align: middle;border-right: 1px solid lightgray;">{{viewInfo.title}}</span>

            <div class="btn-group">
                <button class="min-width btn btn-sm btn-default"
                        ng-click="allDocumentsVm.backTo()">Back
                </button>
                <button class="min-width btn btn-sm btn-success"
                        style="border: 1px solid green"
                        ng-click="allDocumentsVm.selectPermissions()">Save
                </button>
            </div>
        </div>
        <h4 style="text-align:right; margin-right:10px;margin-top:4px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
            title="{{allDocumentsVm.selectedFolder.name}}">
            {{allDocumentsVm.selectedFolder.name}}
        </h4>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             ng-if="allDocumentsVm.hasPermission('addFolder') == true || allDocumentsVm.hasPermission('deleteFolder') == true"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" ng-click="allDocumentsVm.addFolder()"
                       ng-disabled="!(allDocumentsVm.hasPermission('addFolder') || allDocumentsVm.loginPerson.create || allDocumentsVm.loginPerson.write || allDocumentsVm.loginPerson.isAdmin)">Add
                    Folder</a></li>
                <li><a tabindex="-1" href="" ng-click="allDocumentsVm.deleteFolder()"
                       ng-if="allDocumentsVm.hasPermission('deleteFolder') == true || allDocumentsVm.loginPerson.isAdmin"
                       ng-hide="allDocumentsVm.showDeleteFolderButton == false || allDocumentsVm.selectedFolder == null">Delete
                    Folder</a></li>
            </ul>
        </div>

        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane" id="folders-pane" style="overflow-x: auto">
                <div id="foldersContainer" class="folders-pane" data-toggle="context" data-target="#context-menu">
                    <ul id="docFoldersTree" class="easyui-tree">
                    </ul>
                </div>
            </div>


            <div class="split-pane-divider"></div>


            <div class="split-pane-component split-right-pane noselect" style="left:300px;" id="files-pane">

                <div ng-show="noFolders == true">
                    <span>No folders to see or add files</span>
                </div>
                <div ng-show="allDocumentsVm.selectedFolder == null && noFolders == false">
                    <span>Select folder to see or add files</span>
                </div>
                <div ng-class="{'pane-visible': !allDocumentsVm.permission,'pane-hidden' : allDocumentsVm.permission}"
                     ng-hide="allDocumentsVm.selectedFolder == null">
                    <div style="font-style: italic; text-align: center;margin-bottom: 20px;" id="dropzoneForm"
                         ng-show="!allDocumentsVm.permission && allDocumentsVm.showFiles"
                         ng-hide="!(allDocumentsVm.showDocumentsButton || allDocumentsVm.loginPerson.isAdmin || allDocumentsVm.loginPerson.write || allDocumentsVm.loginPerson.all || allDocumentsVm.loginPerson.create || allDocumentsVm.hasPermission('addFolder'))">
                        Drag and drop files on the table or <a href="" ng-click="allDocumentsVm.selectFiles()">(Click
                        here to add
                        files)</a>
                    </div>
                    <div style="background-color: indianred;color: white; text-align: center; line-height: 35px;"
                         ng-if="allDocumentsVm.hasError">
                        {{allDocumentsVm.message}} files were locked, cannot upload these files
                        <span style="color: white; opacity: 0.6; padding: 5px;" class="close"
                              ng-click="allDocumentsVm.closeNotification()">x</span>
                    </div>
                    <div>
                        <tr id="attachmentsContainer" ng-if="!allDocumentsVm.showFileDropzone"
                            class="attachments-dropzone-container">
                            <div id="attachmentsDropZone" style="display: table; width: 100%;">
                                <div id="previews">
                                    <div id="template">
                                        <div class="dz-preview dz-file-preview">
                                            <div class="row"
                                                 style="border-bottom: 1px dotted lightgrey; margin-bottom: 10px;padding-bottom: 10px;">
                                                <div class="col-sm-7">
                                                    <p class="name" data-dz-name></p>
                                                    <small class="error text-danger" data-dz-errormessage></small>
                                                </div>
                                                <div class="col-sm-3">
                                                    <p class="size" data-dz-size></p>

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
                            </div>
                        </tr>
                    </div>
                    <div id="itemFilesTableContainer" ng-hide="allDocumentsVm.selectedFolder == null">
                        <div class="responsive-table" ng-if="!allDocumentsVm.permission">
                            <table class='responsive-table table table-striped highlight-row'>
                                <thead>
                                <tr ng-show="allDocumentsVm.showFiles">
                                    <th style="width: 100px;">File Type</th>
                                    <th style="width: 200px;">File Name</th>
                                    <th style="text-align: center;width: 100px;">File Size</th>
                                    <th style="text-align: center;width: 100px;">Version</th>
                                    <th style="text-align: center;width: 150px;">Date Created</th>
                                    <th style="text-align: center;width: 150px;">Created By</th>
                                    <th style="text-align: center;width: 150px;">Last Modified</th>
                                    <th style="text-align: center;width: 150px;">Modified By</th>
                                    <th style="text-align: center;width: 100px;">Is Locked</th>
                                    <th style="text-align: center;width: 100px;">Locked By</th>
                                    <th style="text-align: center;width: 100px;">Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-if="allDocumentsVm.loading == true">
                                    <td colspan="12"><img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading
                                        documents..
                                    </td>
                                </tr>
                                <tr ng-if="allDocumentsVm.loading == false && allDocumentsVm.documents.length == 0">
                                    <td colspan="12">No files are available in this folder</td>
                                </tr>
                                <tr ng-if="allDocumentsVm.documents.length > 0 && allDocumentsVm.loading == false && allDocumentsVm.showFiles"
                                    ng-repeat="document in allDocumentsVm.documents">
                                    <td style="width: 200px;cursor: pointer"> <span
                                            ng-click="allDocumentsVm.filePreview(document)"
                                            title="Click to Preview File">
                        <i class="{{getIcon(document.name)}}" ng-style="{{getIconColor(document.name)}}"></i>
                    </span></td>
                                    <td ng-if="document.urn != null" title="{{document.name}}">
                                        <div class="imageTooltip">
                                            <span><a href="">{{document.name}}</a></span>

                                            <div class="imageTooltiptext" ng-show="document.thumbnail != null">
                                                <img src="data:image/png;base64,{{document.thumbnail}}"
                                                     ng-click="showAutoDeskFile(document)"/>
                                            </div>
                                        </div>
                                    </td>
                                    <td ng-if="document.urn == null" title="Download Document">
                                        <span class="imageTooltip"
                                              ng-click="allDocumentsVm.downloadDocument(document)"><a href="">
                                            {{document.name}}</a></span>

                                    </td>
                                    <td style="text-align: center">{{allDocumentsVm.fileSizeToString(document.size)}}
                                    </td>
                                    <td style="text-align: center">{{document.version}}</td>
                                    <td>{{document.createdDate}}</td>
                                    <td style="text-align: center">{{document.createdByPerson.fullName}}</td>
                                    <td>{{document.modifiedDate}}</td>
                                    <td style="text-align: center">{{document.modifiedByPerson.fullName}}</td>
                                    <td style="text-align: center">
                                        <input type="checkbox" ng-model="document.locked"
                                               ng-disabled="!(allDocumentsVm.loginPerson.isAdmin || (document.enableLockButton) || allDocumentsVm.loginPerson.all || allDocumentsVm.loginPerson.write)"
                                               ng-change="allDocumentsVm.lockDocument(document, document.locked)">
                                    </td>
                                    <td><span ng-show='document.locked'>{{document.lockedByPerson.fullName}}</span></td>
                                    <td class="text-center">
                                    <span class="row-menu" uib-dropdown style="min-width: 50px">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-click='allDocumentsVm.showComments(document)'>
                                            <a class="dropdown-item" title="Show Comments">
                                                <span style="padding-left: 3px;color: #666">Comments</span>
                                            </a></li>
                                        <li ng-click="allDocumentsVm.showFileHistory(document.id)">
                                            <a class="dropdown-item" type="button"
                                               title="Show File History">
                                                <span style="padding-left: 3px;color: #666">File History</span>
                                            </a>
                                        </li>
                                        <li ng-click="showAutoDeskFile(document)"
                                            ng-if="document.urn != null">
                                            <a class="dropdown-item" type="button"
                                               title="Show CAD Drawing"><span
                                                    style="padding-left: 3px;color: #666">Show CAD Drawing</span>
                                            </a>
                                        </li>
                                        <li ng-click="allDocumentsVm.downloadDocument(document)"
                                            ng-if="(allDocumentsVm.loginPerson.all || allDocumentsVm.loginPerson.read || allDocumentsVm.loginPerson.isAdmin)">
                                            <a title="Download File" type="button"
                                               class="dropdown-item">
                                                <span style="padding-left: 3px;color: #666">Download</span>
                                            </a>
                                        </li>
                                        <li ng-click="allDocumentsVm.deleteDocument(document)"
                                            ng-if="!document.locked && (allDocumentsVm.loginPerson.all
                                                    || allDocumentsVm.loginPerson.isAdmin || allDocumentsVm.loginPerson.delete)">
                                            <a title="Delete File"
                                               type="button"
                                               class="dropdown-item">
                                                <span style="padding-left: 3px;color: #666">Delete</span>
                                            </a>
                                        </li>
                                    </ul>
                                        </span>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div ng-class="{'pane-visible': allDocumentsVm.permission,'pane-hidden' : !allDocumentsVm.permission}">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 200px;">Person</th>
                            <th style="width: 200px;">All</th>
                            <th style="width: 200px;">None</th>
                            <th style="width: 200px;">Read</th>
                            <th style="width: 200px;">Write</th>
                            <th style="width: 200px;">Delete</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="allDocumentsVm.persons.length == 0">
                            <td colspan="11">No Data</td>
                        </tr>
                        <tr ng-repeat="person in allDocumentsVm.persons">
                            <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                title="{{person.person.fullName}}">{{person.person.fullName}}
                            </td>
                            <td><input type="checkbox" ng-model="person.allSelected"
                                       ng-click="allDocumentsVm.checkAllPersonPermissions(person)"
                                       ng-disabled="person.allDisabled"
                                       ng-readonly="person.allSelected">
                            </td>
                            <td style="width: 200px;"><input type="checkbox" ng-model="person.noneSelected"
                                                             ng-click="allDocumentsVm.checkAllPersonPermissions(person)"
                                                             ng-disabled="person.noneDisabled"
                                                             ng-readonly="person.noneSelected"></td>
                            <td style="width: 200px;"><input type="checkbox" ng-model="person.readSelected"
                                                             ng-click="allDocumentsVm.checkPersonPermissions(person)"
                                                             ng-disabled="person.readDisabled"
                                                             ng-readonly="person.readSelected"></td>
                            <td style="width: 200px;"><input type="checkbox" ng-model="person.writeSelected"
                                                             ng-click="allDocumentsVm.checkPersonPermissions(person)"
                                                             ng-disabled="person.writeDisabled"
                                                             ng-readonly="person.writeSelected"></td>
                            <td style="width: 200px;"><input type="checkbox" ng-model="person.deleteSelected"
                                                             ng-click="allDocumentsVm.checkPersonPermissions(person)"
                                                             ng-disabled="person.deleteDisabled"
                                                             ng-readonly="person.deleteSelected"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<show-forge-file register-call-back="registerCallBack(callback)"></show-forge-file>