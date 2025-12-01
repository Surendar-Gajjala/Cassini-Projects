<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<div class="view-container" fitcontent>
    <style scoped>
        .pane-visible {
            visibility: visible;
        }

        .pane-hidden {
            visibility: hidden;
        }

        table.highlight-row tr.dropzone-row:hover, table.highlight-row tr.dropzone-row:hover td {
            background-color: inherit !important;
            background-image: none !important;
            color: rgb(99, 110, 123) !important;
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
    </style>
    <div class="view-toolbar">
        <div class="col-sm-10" ng-hide="homeVm.selectedFolder == null">
            <div ng-show="homeVm.showDropzone == false && homeVm.permission == false">
                <div class="btn-group">
                    <button class="btn btn-sm btn-warning min-width"
                            ng-disabled="!(hasPermission('permission.topDocuments.addPermissions') || homeVm.loginPerson.isAdmin)"
                            ng-click="homeVm.addPermission()">Permissions
                    </button>
                </div>
            </div>
        </div>
        <div class="col-sm-10" ng-show="homeVm.permission">
            <div class="btn-group">
                <button class="min-width btn btn-sm btn-default"
                        ng-click="homeVm.backTo()">Back
                </button>
                <button class="min-width btn btn-sm btn-success"
                        style="border: 1px solid green"
                        ng-click="homeVm.selectPermissions()">Save
                </button>
            </div>
        </div>
        <h4 style="text-align:right; margin-right:10px;margin-top:4px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;" title="{{homeVm.selectedFolder.name}}">
            {{homeVm.selectedFolder.name}}
        </h4>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div id="contextMenu" class="context-menu dropdown clearfix" ng-if="homeVm.hasPermission('addFolder') == true || homeVm.hasPermission('deleteFolder') == true"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" ng-click="homeVm.addFolder()"
                       ng-disabled="!(homeVm.hasPermission('addFolder') || homeVm.loginPerson.create || homeVm.loginPerson.write || homeVm.loginPerson.isAdmin)">Add Folder</a></li>
                <li><a tabindex="-1" href="" ng-click="homeVm.deleteFolder()" ng-if="homeVm.hasPermission('deleteFolder') == true || homeVm.loginPerson.isAdmin"
                       ng-hide="homeVm.showDeleteFolderButton == false || homeVm.selectedFolder == null">Delete
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


            <div class="split-pane-divider" id="docs-split-pane-divider"></div>


            <div class="split-pane-component split-right-pane noselect" style="left:300px;" id="files-pane">

                <div ng-show="noFolders == true">
                    <span>No folders to see or add Documents</span>
                </div>
                <div ng-show="homeVm.selectedFolder == null && noFolders == false">
                    <span>Select folder to see or add Documents</span>
                </div>
                <div ng-hide="homeVm.selectedFolder == null && homeVm.showDropzone == false && !homeVm.permission" id="dropzoneForm">
                    <div style="font-style: italic; text-align: center;margin-bottom: 20px;" ng-if="!homeVm.permission" ng-hide="!(homeVm.showDocumentsButton || homeVm.loginPerson.isAdmin || homeVm.loginPerson.write || homeVm.loginPerson.all || homeVm.loginPerson.create)">
                        Drag and drop files on to the table or <a href="" ng-click="homeVm.selectFiles()">(Click here to add
                        files)</a>
                    </div>
                    <div style="background-color: indianred;color: white; text-align: center; line-height: 35px;" ng-if="homeVm.hasError" >
                        {{homeVm.message}} files were locked, cannot upload these files
                        <span style="color: white; opacity: 0.6; padding: 5px;" class="close" ng-click="homeVm.closeNotification()">x</span>
                    </div>
                    <div>
                        <tr id="attachmentsContainer" ng-if="!homeVm.showFileDropzone"
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

                                                    <div id="total-progress" class="progress progress-striped active" role="progressbar"
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
                    <div id="itemFilesTableContainer" ng-hide="homeVm.selectedFolder == null">
                        <div class="responsive-table" ng-if="!homeVm.permission">
                            <table class="table table-striped highlight-row">
                                <thead>
                                <tr>
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
                                <tr ng-if="homeVm.loading == true">
                                    <td colspan="12"><img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading
                                        documents ...
                                    </td>
                                </tr>
                                <tr ng-if="homeVm.loading == false && homeVm.documents.length == 0">
                                    <td colspan="12">No documents in this folder</td>
                                </tr>
                                <tr ng-if="homeVm.documents.length > 0 && homeVm.loading == false"
                                    ng-repeat="document in homeVm.documents">
                                    <td style="width: 150px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;" title="{{document.name}}">{{document.name}}</td>
                                    <td style="text-align: center">{{homeVm.fileSizeToString(document.size)}}</td>
                                    <td style="text-align: center">{{document.version}}</td>
                                    <td>{{document.createdDate}}</td>
                                    <td>{{document.createdByPerson.fullName}}</td>
                                    <td>{{document.modifiedDate}}</td>
                                    <td>{{document.modifiedByPerson.fullName}}</td>
                                    <td style="text-align: center">
                                        <input type="checkbox" ng-model="document.locked" ng-disabled="!(homeVm.loginPerson.isAdmin || (document.enableLockButton) || homeVm.loginPerson.all || homeVm.loginPerson.write)"
                                               ng-change="homeVm.lockDocument(document, document.locked)">
                                    </td>
                                    <td><span ng-show='document.locked'>{{document.lockedByPerson.fullName}}</span></td>
                                    <td style="text-align: center">
                                        <div class="btn-group" style="display: inline-flex">
                                            <comments-button object-type="FILE" class="btn btn-xs" style="padding-right: 0 !important;border: 0;"
                                                             object-id="document.id"></comments-button>
                                            <button class="btn btn-xs btn-warning" title="Show file history"
                                                    ng-click="homeVm.showFileHistory(document.id)"><i class="fa fa-copy"></i>
                                            </button>
                                            <button title="Download document"
                                                    class="btn btn-xs btn-primary"  ng-disabled="!(homeVm.loginPerson.all || homeVm.loginPerson.read || homeVm.loginPerson.isAdmin)"
                                                    ng-click="homeVm.downloadDocument(document)">
                                                <i class="fa fa-download"></i>
                                            </button>
                                            <button title="Delete document" ng-if="document.locked == false"
                                                    ng-disabled="!(homeVm.loginPerson.all || homeVm.loginPerson.isAdmin || document.locked || homeVm.loginPerson.delete)"
                                                    class="btn btn-xs btn-danger"
                                                    ng-click="homeVm.deleteDocument(document)"><i
                                                    class="fa fa-trash"></i>
                                            </button>
                                            <button ng-if="document.locked == true"
                                                    ng-disabled="document.locked == true"
                                                    title="We can't delete locked file"
                                                    class="btn btn-xs btn-danger"
                                                    ng-click="homeVm.deleteDocument(document)"><i
                                                    class="fa fa-trash"></i>
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div ng-show="homeVm.permission">

                    <div class="responsive-table">
                        <table class="table table-striped">
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
                            <tr ng-if="homeVm.persons.length == 0">
                                <td colspan="11">No Data</td>
                            </tr>
                            <tr ng-repeat="person in homeVm.persons">
                                <td>{{person.person.fullName}}</td>
                                <td><input type="checkbox" ng-model="person.allSelected"
                                           ng-click="homeVm.checkAllPersonPermissions(person)"
                                           ng-disabled="person.allDisabled"
                                           ng-readonly="person.allSelected">
                                </td>
                                <td style="width: 200px;"><input type="checkbox" ng-model="person.noneSelected"
                                                                 ng-click="homeVm.checkAllPersonPermissions(person)"
                                                                 ng-disabled="person.noneDisabled"
                                                                 ng-readonly="person.noneSelected"></td>
                                <td style="width: 200px;"><input type="checkbox" ng-model="person.readSelected"
                                                                 ng-click="homeVm.checkAllPersonPermissions(person)"
                                                                 ng-disabled="person.readDisabled"
                                                                 ng-readonly="person.readSelected"></td>
                                <td style="width: 200px;"><input type="checkbox" ng-model="person.writeSelected"
                                                                 ng-click="homeVm.checkAllPersonPermissions(person)"
                                                                 ng-disabled="person.writeDisabled"
                                                                 ng-readonly="person.writeSelected"></td>
                                <td style="width: 200px;"><input type="checkbox" ng-model="person.deleteSelected"
                                                                 ng-click="homeVm.checkAllPersonPermissions(person)"
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
</div>