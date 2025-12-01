<style scoped>
    .pane-visible {
        visibility: visible;
    }

    .pane-hidden {
        visibility: hidden;
    }

    .view-content {
        position: relative;
    }

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        overflow: auto;
        width: 100% !important;
    }

    .split-pane {
        height: 100%;
        width: 100%;
        overflow: hidden;
        z-index: 0;
        position: absolute;

    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        z-index: 5;
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
        <div class="col-sm-8" ng-hide="documentsVm.selectedFolder == null">
            <div ng-show="documentsVm.showDropzone == false && documentsVm.permission == false">
                <div class="btn-group">
                    <button class="btn btn-sm btn-warning min-width"
                            ng-click="documentsVm.addPermission();"
                            ng-disabled="selectedProject.locked == true || !(documentsVm.loginPerson.isProjectOwner)">
                        Permissions
                    </button>
                </div>
            </div>
        </div>
        <div class="col-sm-8" ng-show="documentsVm.permission">
            <div class="btn-group">
                <button class="min-width btn btn-sm btn-default"
                        ng-click="documentsVm.backTo()">Back
                </button>
                <button class="min-width btn btn-sm btn-success"
                        style="border: 1px solid green"
                        ng-click="documentsVm.selectPermissions()" ng-disabled="selectedProject.locked == true">Save
                </button>
            </div>
        </div>

        <h4 style="text-align:right; margin-right:10px;margin-top:4px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
            title="{{documentsVm.selectedFolder.name}}">
            {{documentsVm.selectedFolder.name}}
        </h4>

    </div>

    <div class="view-content no-padding">

        <div id="contextMenu" class="context-menu dropdown clearfix"
             ng-if="documentsVm.hasPermission('addFolder') == true || documentsVm.hasPermission('deleteFolder') == true"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li ng-class="{'disabled':selectedProject.locked == true}"
                    ng-if="documentsVm.hasPermission('addFolder') == true || documentsVm.loginPerson.write || documentsVm.loginPerson.isProjectOwner">
                    <a tabindex="-1" href="" ng-click="documentsVm.addFolder()">Add Folder</a></li>
                <li ng-class="{'disabled':selectedProject.locked == true}"
                    ng-if="documentsVm.hasPermission('deleteFolder') || documentsVm.loginPerson.isProjectOwner">
                    <a tabindex="-1" href="" ng-click="documentsVm.deleteFolder()"
                       ng-hide="documentsVm.selectedFolder == null">Delete Folder</a></li>
            </ul>
        </div>

        <div class="split-pane fixed-left">

            <div class="split-pane-component split-left-pane" style="overflow-x: auto">
                <div id="foldersContainer" class="folders-pane" data-toggle="context" data-target="#context-menu">
                    <ul id="docFoldersTree" class="easyui-tree">
                    </ul>
                </div>
            </div>

            <div class="split-pane-divider"></div>

            <div class="split-pane-component split-right-pane noselect" style="left:300px;">
                <div ng-show="documentsVm.selectedFolder == null">
                    <span>Please select any folder to see or add files</span>
                </div>
                <div style="font-style: italic;text-align: center;padding: 0 5px;padding-bottom: 8px;" id="dropzoneForm"
                     ng-hide="!documentsVm.showFiles || documentsVm.hasError || documentsVm.permission || documentsVm.selectedFolder == null || selectedProject.locked == true || !(documentsVm.loginPerson.create || documentsVm.loginPerson.write || documentsVm.loginPerson.isProjectOwner || documentsVm.hasPermission('addFolder'))">
                    Drag and drop files on to the table or <a href="" ng-click="documentsVm.selectFiles()">(Click
                    here to add
                    files)</a>
                </div>
                <div style="background-color: indianred;color: white; text-align: center; line-height: 35px;"
                     ng-if="documentsVm.hasError">
                    {{documentsVm.message}} files were locked, cannot upload these files
                    <span style="color: white; opacity: 0.6; padding: 5px;" class="close"
                          ng-click="documentsVm.closeNotification()">x</span>
                </div>
                <div>
                    <tr id="attachmentsContainer" ng-if="!documentsVm.showFileDropzone"
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
                <div id="itemFilesTableContainer"
                     ng-class="{'pane-visible': !documentsVm.permission,'pane-hidden' : documentsVm.permission}"
                     ng-hide="documentsVm.selectedFolder == null">
                    <table ng-show="documentsVm.showDropzone == false && documentsVm.permission == false"
                           class='responsive-table table table-striped highlight-row'>
                        <thead>
                        <tr ng-if="documentsVm.showFiles">
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
                        <tr ng-if="documentsVm.loading == true">
                            <td colspan="10"><img src="app/assets/images/loaders/loader19.gif"
                                                  class="mr5">Loading Files...
                            </td>
                        </tr>
                        <tr ng-if="documentsVm.loading == false && documentsVm.documents.length == 0">
                            <td colspan="12">No files are available in this folder</td>
                        </tr>
                        <tr ng-if="documentsVm.documents.length > 0 && documentsVm.loading == false && documentsVm.showFiles"
                            ng-repeat="document in documentsVm.documents">

                            <td ng-if="document.urn != null" title="{{document.name}}">
                                <div class="imageTooltip">
                                    <a href="">{{document.name}}</a>

                                    <div class="imageTooltiptext" ng-show="document.thumbnail != null">
                                        <img src="data:image/png;base64,{{document.thumbnail}}"
                                             ng-click="showAutoDeskFile(document)"/>
                                    </div>
                                </div>
                            </td>
                            <td ng-if="document.urn == null" title="Download File">
                                <div class="imageTooltip">
                                    <span
                                            ng-click="documentsVm.downloadDocument(document)"><a href="">
                                        {{document.name}}</a></span>
                                </div>


                            </td>
                            <td style="text-align: center">
                                {{documentsVm.fileSizeToString(document.size)}}
                            </td>
                            <td style="text-align: center">{{document.version}}</td>
                            <td style="text-align: center">{{document.createdDate}}</td>
                            <td style="text-align: center">{{document.createdByPerson.fullName}}</td>
                            <td style="text-align: center">{{document.modifiedDate}}</td>
                            <td style="text-align: center">{{document.modifiedByPerson.fullName}}</td>
                            <td style="text-align: center">
                                <input type="checkbox" ng-model="document.locked"
                                       ng-disabled="!(documentsVm.loginPerson.write || documentsVm.loginPerson.id == document.lockedBy || documentsVm.loginPerson.isAdmin || documentsVm.loginPerson.isProjectOwner)"
                                       ng-change="documentsVm.lockDocument(document, document.locked)">
                            </td>
                            <td style="text-align: center"><span
                                    ng-show='document.locked'>{{document.lockedByPerson.fullName}}</span>
                            </td>
                            <td class="text-center">
                                    <span class="row-menu" uib-dropdown dropdown-append-to-body
                                          style="min-width: 50px;">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-click='documentsVm.showComments(document)'>
                                            <a class="dropdown-item" title="Show comments">
                                                <span
                                                        style="padding-left: 3px;">Comments</span>
                                            </a></li>
                                        <li ng-click="documentsVm.showFileHistory(document.id)"
                                            ng-if="selectedProject.locked == false">
                                            <a class="dropdown-item" type="button"
                                               title="Show file history"><span
                                                    style="padding-left: 3px;">File History</span>
                                            </a>
                                        </li>
                                        <li ng-click="showAutoDeskFile(document)"
                                            ng-if="document.urn != null">
                                            <a class="dropdown-item" type="button"
                                               title="Show CAD Drawing"><span
                                                    style="padding-left: 3px;">Show CAD Drawing</span>
                                            </a>
                                        </li>
                                        <li ng-click="documentsVm.downloadDocument(document)"
                                            ng-if="(documentsVm.loginPerson.read || documentsVm.loginPerson.all || documentsVm.loginPerson.isAdmin ||documentsVm.loginPerson.isProjectOwner)">
                                            <a title="Download File" type="button"
                                               class="dropdown-item">
                                                <span style="padding-left: 3px;">Download</span>
                                            </a>
                                        </li>
                                        <li ng-click="documentsVm.deleteDocument(document)"
                                            ng-if="((documentsVm.loginPerson.delete || documentsVm.loginPerson.all || documentsVm.loginPerson.isAdmin || documentsVm.loginPerson.isProjectOwner) && (!selectedProject.locked && !document.locked))">
                                            <a title="Delete File"
                                               type="button"
                                               class="dropdown-item">
                                                    <span
                                                            style="padding-left: 3px;">Delete</span>
                                            </a>
                                        </li>
                                    </ul>
                                        </span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div ng-class="{'pane-visible': documentsVm.permission,'pane-hidden' : !documentsVm.permission}">
                    <table class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th style="width: 200px;">Role</th>
                            <th style="width: 200px;">All</th>
                            <th style="width: 200px;">None</th>
                            <th style="width: 200px;">Create</th>
                            <th style="width: 200px;">Read</th>
                            <th style="width: 200px;">Write</th>
                            <th style="width: 200px;">Delete</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="documentsVm.projectRoles.length == 0">
                            <td colspan="11">No Data</td>
                        </tr>
                        <tr ng-repeat="projectRole in documentsVm.projectRoles">
                            <td style="width: 200px;">{{projectRole.role}}</td>
                            <td><input id="all" type="checkbox" ng-model="projectRole.allSelected"
                                       ng-click="documentsVm.checkAllRolePermissions(projectRole)"
                                       ng-disabled="projectRole.allDisabled"
                                       ng-readonly="projectRole.allSelected"></td>
                            <td style="width: 200px;"><input id="none" type="checkbox"
                                                             ng-model="projectRole.noneSelected"
                                                             ng-click="documentsVm.checkAllRolePermissions(projectRole)"
                                                             ng-disabled="projectRole.noneDisabled"
                                                             ng-readonly="projectRole.noneSelected">
                            </td>
                            <td style="width: 200px;"><input type="checkbox"
                                                             ng-model="projectRole.createSelected"
                                                             ng-click="documentsVm.checkRolePermissions(projectRole)"
                                                             ng-disabled="projectRole.createDisabled"
                                                             ng-readonly="projectRole.createSelected">
                            </td>
                            <td style="width: 200px;"><input id="read" type="checkbox"
                                                             ng-model="projectRole.readSelected"
                                                             ng-click="documentsVm.checkRolePermissions(projectRole)"
                                                             ng-disabled="projectRole.readDisabled"
                                                             ng-readonly="projectRole.readSelected">
                            <td style="width: 200px;"><input id="write" type="checkbox"
                                                             ng-model="projectRole.writeSelected"
                                                             ng-click="documentsVm.checkRolePermissions(projectRole)"
                                                             ng-disabled="projectRole.writeDisabled"
                                                             ng-readonly="projectRole.writeSelected">
                            </td>
                            <td style="width: 200px;"><input id="delete" type="checkbox"
                                                             ng-model="projectRole.deleteSelected"
                                                             ng-click="documentsVm.checkRolePermissions(projectRole)"
                                                             ng-disabled="projectRole.deleteDisabled"
                                                             ng-readonly="projectRole.deleteSelected">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <br>
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th style="width: 200px;">Person</th>
                            <th style="width: 200px;">All</th>
                            <th style="width: 200px;">None</th>
                            <th style="width: 200px;">Create</th>
                            <th style="width: 200px;">Read</th>
                            <th style="width: 200px;">Write</th>
                            <th style="width: 200px;">Delete</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="documentsVm.projectPersons.length == 0">
                            <td colspan="11">No Data</td>
                        </tr>
                        <tr ng-repeat="projectPerson in documentsVm.projectPersons">
                            <td style="vertical-align: middle;width: 200px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                title="{{projectPerson.personObject.fullName}}">{{projectPerson.personObject.fullName}}
                            </td>
                            <td><input type="checkbox" ng-model="projectPerson.allSelected"
                                       ng-click="documentsVm.checkAllRolePermissions(projectPerson)"
                                       ng-disabled="projectPerson.allDisabled"
                                       ng-readonly="projectPerson.allSelected">
                            </td>
                            <td style="width: 200px;"><input type="checkbox"
                                                             ng-model="projectPerson.noneSelected"
                                                             ng-click="documentsVm.checkAllRolePermissions(projectPerson)"
                                                             ng-disabled="projectPerson.noneDisabled"
                                                             ng-readonly="projectPerson.noneSelected">
                            </td>
                            <td style="width: 200px;"><input type="checkbox"
                                                             ng-model="projectPerson.createSelected"
                                                             ng-click="documentsVm.checkRolePermissions(projectPerson)"
                                                             ng-disabled="projectPerson.createDisabled"
                                                             ng-readonly="projectPerson.createSelected">
                            </td>
                            <td style="width: 200px;"><input type="checkbox"
                                                             ng-model="projectPerson.readSelected"
                                                             ng-click="documentsVm.checkRolePermissions(projectPerson)"
                                                             ng-disabled="projectPerson.readDisabled"
                                                             ng-readonly="projectPerson.readSelected">
                            </td>
                            <td style="width: 200px;"><input type="checkbox"
                                                             ng-model="projectPerson.writeSelected"
                                                             ng-click="documentsVm.checkRolePermissions(projectPerson)"
                                                             ng-disabled="projectPerson.writeDisabled"
                                                             ng-readonly="projectPerson.writeSelected">
                            </td>
                            <td style="width: 200px;"><input type="checkbox"
                                                             ng-model="projectPerson.deleteSelected"
                                                             ng-click="documentsVm.checkRolePermissions(projectPerson)"
                                                             ng-disabled="projectPerson.deleteDisabled"
                                                             ng-readonly="projectPerson.deleteSelected">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    </div>
</div>

<show-forge-file register-call-back="registerCallBack(callback)"></show-forge-file>