<div class="view-container" fitcontent>
    <style>
        .pane-visible {
            visibility: visible;
        }

        .pane-hidden {
            visibility: hidden;
        }

        .search-form {
            border-radius: 30px;
        }

        .item-number {
            display: inline-block;
        }

        .nav-tabs > .uib-tab.active > a, .nav-tabs > .uib-tab.active > a:focus, .nav-tabs > .uib-tab.active > a:hover {
            color: #555 !important;
            cursor: default !important;
            background-color: transparent !important;
            border-color: #ddd #ddd transparent #ddd !important;
            border-bottom: 2px solid #2a6fa8 !important;
        }

        .item-rev {
            font-size: 16px;
            font-weight: normal;
        }

        .tab-content {
            padding: 0px !important;
        }

        .tab-content .tab-pane {
            overflow: auto !important;
        }

        .tab-pane {
            position: relative;
        }

        .tab-content .tab-pane .responsive-table {
            height: 100%;
            position: absolute;
            overflow: auto !important;
            padding: 5px;
        }

        .tab-content .tab-pane .responsive-table table thead th {
            position: -webkit-sticky;
            position: sticky;
            top: -10px !important;
            z-index: 5;
            background-color: #fff;
        }

        #freeTextSearchDirective {
            top: 7px !important;
        }
    </style>
    <div class="view-toolbar">
        <div class="row" style="margin-bottom: 20px">
            <div class="col-sm-6">
                <div<%-- ng-hide="vaultVm.selectedFolder == null"--%>>
                    <div ng-show="vaultVm.showDropzone == false && vaultVm.permission == false">
                        <div class="row" style="margin-bottom: 20px">
                            <div class="col-sm-6">
                                <div class="btn-group" style="margin-left: -17px;">
                                    <%--   <button class="btn btn-sm btn-success" ng-click="vaultVm.addFiles()"
                                               ng-disabled="vaultVm.checkAddFilesPermissions()">
                                           Add Files
                                       </button>--%>
                                    <button class="btn btn-sm btn-warning min-width"
                                            <%--ng-disabled="vaultVm.checkPermissionChanges()"--%>
                                            ng-click="vaultVm.addPermission();"> Add
                                        Permissions
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div ng-show="vaultVm.permission">
                    <div class="btn-group">
                        <button class="min-width btn btn-sm btn-default"
                                ng-click="vaultVm.backTo()">Back
                        </button>
                        <button class="min-width btn btn-sm btn-success"
                                style="border: 1px solid green"
                                ng-click="vaultVm.selectPermissions()">Save
                        </button>
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <h4 class="text-primary" style="text-align: right; margin: 5px 10px 0 0;">
                    {{vaultVm.selectedFolder.name}}
                </h4>
            </div>
            <free-text-search on-clear="vaultVm.resetPage" ng-if="vaultVm.filesSerach == true"
                              on-search="vaultVm.freeTextSearch"></free-text-search>
        </div>


    </div>

    <div class="view-content" style="padding:0">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999"
             <%--ng-show="vaultVm.checkAddFilesPermissions()==false || vaultVm.checkDeleteFilesPermissions()==false"--%>>
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" id="addFolder" href="" ng-click="vaultVm.addFolder()"
                       <%--ng-hide="vaultVm.checkAddFilesPermissions()"--%>>Add Folder</a></li>
                <li><a tabindex="-1" id="deleteFolder" href="" ng-click="vaultVm.deleteFolder()"
                       <%--ng-hide="vaultVm.checkDeleteFilesPermissions()"--%>>Delete Folder</a></li>
            </ul>
        </div>

        <div class="split-pane fixed-left">
            <div class="split-pane-component" id="folders-pane">
                <div style="height: 40px;">
                    <input type="search" class="form-control input-sm search-form" placeholder="Search"
                           ng-model="vaultVm.searchValue" ng-change="vaultVm.searchTree()">
                </div>
                <div id="foldersContainer" class="folders-pane" data-toggle="context" data-target="#context-menu">
                    <ul id="vaultFoldersTree" class="easyui-tree">
                    </ul>
                </div>
            </div>


            <div class="split-pane-divider" id="docs-split-pane-divider"></div>


            <div class="split-pane-component" id="files-pane" ng-class="{'pane-visible': !vaultVm.permission,
                'pane-hidden' : vaultVm.permission}">

                <div ng-show="vaultVm.selectedFolder == null">
                    <span>Select a folder to see or add Files</span>
                </div>

                <div ng-show="vaultVm.selectedFolder != null && vaultVm.checkFileViewPermissions()">
                    <span>You don't have permissions to view files</span>
                </div>


                <div ng-hide="vaultVm.selectedFolder == null || vaultVm.checkFileViewPermissions()==true">
                    <div ng-show="vaultVm.showDropzone == false" id="itemFiles">
                        <div style="font-style: italic; text-align: center;margin-bottom: 20px;">
                            Drag and drop files on to the table or <a href="" ng-click="vaultVm.selectFile()">(Click
                            here to add files)</a>
                        </div>
                        <div class="responsive-table">
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Permissions</th>
                                    <th>File Name</th>
                                    <th>File Size</th>
                                    <th style="text-align: center">Version</th>
                                    <th>Date Created</th>
                                    <th>Created By</th>
                                    <th>Last Modified</th>
                                    <th>Modified By</th>
                                    <th style="text-align: center">Is Locked</th>
                                    <th>Locked By</th>
                                    <th style="text-align: center">Actions</th>
                                </tr>
                                </thead>
                                <tbody>

                                <tr ng-show="vaultVm.showFileDropzone == true" class="dropzone-row">
                                    <td colspan="14">
                                        <div style="height: 300px; border: slategray dashed 2px; overflow-y: auto">
                                            <div class="table table-striped table-bordered" class="files previews"
                                                 id="previews">
                                                <div id="template" class="file-row">
                                                    <!-- This is used as the file preview template -->
                                                    <div style="width:10px">
                                                        <span class="preview"><img data-dz-thumbnail/></span>
                                                    </div>
                                                    <div>
                                                        <p class="name" data-dz-name></p>
                                                        <strong class="error text-danger" data-dz-errormessage></strong>
                                                    </div>
                                                    <div>
                                                        <p class="size" data-dz-size></p>

                                                        <div class="progress progress-striped active" role="progressbar"
                                                             aria-valuemin="0" aria-valuemax="100" aria-valuenow="0"
                                                             style="margin-bottom: 0">
                                                            <div class="progress-bar progress-bar-success"
                                                                 style="width:0%;"
                                                                 data-dz-uploadprogress></div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>


                                <tr ng-if="vaultVm.loading == true">
                                    <td colspan="11"><img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading
                                        Files...
                                    </td>
                                </tr>
                                <tr ng-if="vaultVm.loading == false && vaultVm.files.length == 0">
                                    <td colspan="11">No Files in this folder</td>
                                </tr>
                                <tr ng-if="vaultVm.files.length > 0"
                                    ng-repeat="file in vaultVm.files">
                                    <td style="width: 80px; text-align: center">
                                        <input name="item" type="checkbox" ng-click="vaultVm.selectCheck(file)">
                                    </td>
                                    <%-- <td>
                                         <a href="" &lt;%&ndash;ui-sref="app.pm.project.documentdetails({folderId: document.folder, documentId: document.id})"&ndash;%&gt;
                                            ng-click="vaultVm.downloadFile(file)">{{file.name}}</a>
                                     </td>--%>
                                    <td><a href="" ng-click="vaultVm.downloadFile(file)">{{file.name}}</a></td>
                                    <td>{{vaultVm.fileSizeToString(file.size)}}</td>
                                    <td style="text-align: center">{{file.version}}</td>
                                    <td>{{file.createdDate}}</td>
                                    <td>{{file.createdByPerson.fullName}}</td>
                                    <td>{{file.modifiedDate}}</td>
                                    <td>{{file.modifiedByPerson.fullName}}</td>
                                    <td style="text-align: center">
                                        <input type="checkbox" ng-model="file.locked"
                                               ng-change="vaultVm.lockFile(file, file.locked)">
                                    </td>
                                    <td>{{file.lockedByPerson.firstName}}</td>
                                    <td style="text-align: center">
                                        <comments-button object-type="FILE" object-id="file.id"></comments-button>
                                        <button title="Show file history" class="btn btn-xs btn-warning"
                                                ng-click="vaultVm.showFileHistory(file.id)">
                                            <i class="fa fa-copy"></i>
                                        </button>
                                        <button title="Delete this file" class="btn btn-xs btn-danger"
                                                ng-click="vaultVm.deleteFile(file)">
                                            <i class="fa fa-trash"></i>
                                        </button>
                                        <%--   <button title="Download file"
                                                   class="btn btn-xs btn-primary"
                                                   ng-click="vaultVm.downloadFile(file)">
                                               <i class="fa fa-download"></i>
                                           </button>--%>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div ng-show="vaultVm.showDropzone == true">
                        <%@include file="dropzoneComponent.jsp" %>
                    </div>
                </div>
            </div>

            <div class="split-pane-component" ng-class="{'pane-visible': vaultVm.permission,
                'pane-hidden' : !vaultVm.permission}" style="width: 78%;margin-top: 12px;">


                <div <%--style="border: 1px solid lightgrey; border-bottom: 0px;margin-top: 10px"--%>>
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th style="width: 200px;">Role</th>
                            <th style="width: 200px;">All</th>
                            <th style="width: 200px;">None</th>
                            <th style="width: 200px;">Create</th>
                            <th style="width: 200px;">Read</th>
                            <th style="width: 200px;">Delete</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="vaultVm.roles.length == 0">
                            <td colspan="11">No Data</td>
                        </tr>
                        <tr ng-repeat="role in vaultVm.roles">
                            <td style="width: 200px;">{{role.name}}</td>
                            <td><input id="all" type="checkbox" ng-model="role.allSelected"
                                       ng-click="vaultVm.checkAllRolePermissions(role)"
                                       ng-disabled="role.allDisabled || !vaultVm.show"
                                       ng-readonly="role.allSelected"></td>
                            <td style="width: 200px;"><input id="none" type="checkbox"
                                                             ng-model="role.noneSelected"
                                                             ng-click="vaultVm.checkNoneRolePermissions(role)"
                                                             ng-disabled="role.noneDisabled || !vaultVm.show"
                                                             ng-readonly="role.noneSelected">
                            </td>
                            <td style="width: 200px;"><input type="checkbox" ng-model="role.createSelected"
                                                             ng-disabled="role.createDisabled || !vaultVm.show"
                                                             ng-readonly="role.createSelected"></td>
                            <td style="width: 200px;"><input id="read" type="checkbox"
                                                             ng-model="role.readSelected"
                                                             ng-disabled="role.readDisabled || !vaultVm.show"
                                                             ng-readonly="role.readSelected">
                            <td style="width: 200px;"><input id="delete" type="checkbox"
                                                             ng-model="role.deleteSelected"
                                                             ng-disabled="role.deleteDisabled || !vaultVm.show"
                                                             ng-readonly="role.deleteSelected">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <br>

                <div>
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th style="width: 200px;">Person</th>
                            <th style="width: 200px;">All</th>
                            <th style="width: 200px;">None</th>
                            <th style="width: 200px;">Create</th>
                            <th style="width: 200px;">Read</th>
                            <th style="width: 200px;">Delete</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="vaultVm.persons.length == 0">
                            <td colspan="11">No Data</td>
                        </tr>
                        <tr ng-repeat="person in vaultVm.persons">
                            <td>{{person.firstName}}</td>
                            <td><input type="checkbox" ng-model="person.allSelected"
                                       ng-click="vaultVm.checkAllPersonPermissions(person)"
                                       ng-disabled="person.allDisabled || !vaultVm.show"
                                       ng-readonly="person.allSelected">
                            </td>
                            <td style="width: 200px;"><input type="checkbox" ng-model="person.noneSelected"
                                                             ng-click="vaultVm.checkNonePersonPermissions(person)"
                                                             ng-disabled="person.noneDisabled || !vaultVm.show"
                                                             ng-readonly="person.noneSelected"></td>
                            <td style="width: 200px;"><input type="checkbox" ng-model="person.createSelected"
                                                             ng-disabled="person.createDisabled || !vaultVm.show"
                                                             ng-readonly="person.createSelected"></td>
                            <td style="width: 200px;"><input type="checkbox" ng-model="person.readSelected"
                                                             ng-disabled="person.readDisabled || !vaultVm.show"
                                                             ng-readonly="person.readSelected"></td>
                            <td style="width: 200px;"><input type="checkbox" ng-model="person.deleteSelected"
                                                             ng-disabled="person.deleteDisabled || !vaultVm.show"
                                                             ng-readonly="person.deleteSelected"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>

        </div>
    </div>
</div>