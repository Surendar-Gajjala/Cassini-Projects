<style scoped>
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
</style>
<div id="taskDropzoneForm">
    <div style="font-style: italic;text-align: center;padding: 0 5px;"
         ng-hide="(task.status == 'FINISHED' || selectedProject.locked == true) || !(hasPermission('permission.tasks.addFiles') || login.person.isProjectOwner || login.person.isTaskOwner)">
        Drag and Drop Files on to the table or <a href="" ng-click="taskAttachmentVm.selectFiles()">(Click
        here to add
        Files)</a>
    </div>
    <div style="background-color: indianred;color: white; text-align: center; line-height: 35px;"
         ng-if="taskAttachmentVm.hasError">
        {{taskAttachmentVm.message}} Files were locked, cannot upload these files
        <span style="color: white; opacity: 0.6; padding: 5px;padding-top: 10px;" class="close"
              ng-click="taskAttachmentVm.closeNotification()">x</span>
    </div>
    <div>
        <tr id="attachmentsContainer" ng-if="!taskAttachmentVm.showFileDropzone"
            class="attachments-dropzone-container">
            <div id="attachmentsDropZone" style="display: table; width: 100%;">
                <div id="taskPreviews">
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
    <div id="taskFilesDropzoneContainer" style="padding-top: 10px;">
        <table class='table table-striped'>
            <thead>
            <tr ng-if="taskAttachmentVm.showFiles">
                <th>File Type</th>
                <th>File Name</th>
                <th>File Size</th>
                <th>Version</th>
                <th>Created by</th>
                <%--<th>Description</th>--%>
                <th>Created Date</th>
                <th style="text-align: center">Is Locked</th>
                <th style="width: 200px;">Locked By</th>
                <th style="width: 100px;">Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="taskAttachmentVm.loading == true">
                <td colspan="10"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5">Loading Files..
                </td>
            </tr>
            <tr ng-if="taskAttachmentVm.loading == false && taskAttachmentVm.taskFiles.length == 0">
                <td colspan="10">No Files are available to view</td>
            </tr>
            <tr ng-if="taskAttachmentVm.taskFiles.length > 0 && taskAttachmentVm.showFiles"
                ng-repeat="taskFile in taskAttachmentVm.taskFiles">
                <td style="width: 200px;cursor: pointer"> <span
                        ng-click="taskAttachmentVm.filePreview(taskFile)"
                        title="Click to Preview File">
                        <i class="{{getIcon(taskFile.name)}}" ng-style="{{getIconColor(taskFile.name)}}"></i>
                    </span></td>
                <td ng-if="taskFile.urn != null"
                    style="vertical-align: middle;width: 400px;display: run-in;white-space: normal;word-wrap: break-word;">
                    <div class="imageTooltip">
                        <a href="">{{taskFile.name}}</a>

                        <div class="imageTooltiptext" ng-show="taskFile.thumbnail != null">
                            <img src="data:image/png;base64,{{taskFile.thumbnail}}"
                                 ng-click="showAutoDeskFile(taskFile)"/>
                        </div>
                    </div>
                </td>
                <td ng-if="taskFile.urn == null"
                    style="vertical-align: middle;width: 400px;display: run-in;white-space: normal;word-wrap: break-word;">
                    <span class="imageTooltip" title="Download File"
                          ng-click="taskAttachmentVm.downloadFile(taskFile)"><a href="">
                        {{taskFile.name}}</a></span>
                </td>
                <td style="vertical-align: middle;width: 150px;">{{taskFile.size/1000}} KB</td>
                <td style="text-align: center;width: 100px;">{{taskFile.version}}</td>
                <td style="vertical-align: middle;width: 200px;display: run-in;white-space: normal;word-wrap: break-word;">
                    {{taskFile.createdByObject.fullName}}
                </td>
                <td style="vertical-align: middle;width: 200px;">{{taskFile.createdDate}}</td>
                <td style="text-align: center;width:150px">
                    <input type="checkbox" ng-model="taskFile.locked"
                           ng-disabled="!(taskAttachmentVm.loginPerson.id == (task.person) || taskAttachmentVm.loginPerson.isAdmin)"
                           ng-change="taskAttachmentVm.lockFile(taskFile, taskFile.locked)">
                </td>
                <td style="width: 200px;display: run-in;white-space: normal;word-wrap: break-word;"><span
                        ng-show='taskFile.locked'>{{taskFile.lockedByObject.fullName}}</span></td>
                <td class="text-center">

                <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">

                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>

                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click='taskAttachmentVm.showComments(taskFile)'>
                            <a class="dropdown-item" title="Show comments">
                                    <span
                                            style="padding-left: 3px;">Comments</span>
                            </a></li>
                        <li ng-click="taskAttachmentVm.showFileHistory(taskFile.id)"
                            ng-if="selectedProject.locked == false">
                            <a class="dropdown-item" type="button"
                               title="Show file history"><span
                                    style="padding-left: 3px;">File History</span>
                            </a>
                        </li>
                        <li ng-click="showAutoDeskFile(taskFile)"
                            ng-if="taskFile.urn != null">
                            <a class="dropdown-item" type="button"
                               title="Show CAD Drawing"><span
                                    style="padding-left: 3px;">Show CAD Drawing</span>
                            </a>
                        </li>
                        <li ng-click="taskAttachmentVm.downloadFile(taskFile)"
                            ng-if="(taskAttachmentVm.loginPerson.read || taskAttachmentVm.loginPerson.all ||
                                taskAttachmentVm.loginPerson.isAdmin ||taskAttachmentVm.loginPerson.isProjectOwner)">
                            <a title="Download File" type="button"
                               class="dropdown-item">
                                <span style="padding-left: 3px;">Download</span>
                            </a>
                        </li>
                        <li ng-click="taskAttachmentVm.deleteFile(taskFile.id)"
                            ng-if="((taskAttachmentVm.loginPerson.delete || taskAttachmentVm.loginPerson.all || taskAttachmentVm.loginPerson.isAdmin
                                || taskAttachmentVm.loginPerson.isProjectOwner) && (!selectedProject.locked && !taskFile.locked))">
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
</div>

<show-forge-file register-call-back="registerCallBack(callback)"></show-forge-file>