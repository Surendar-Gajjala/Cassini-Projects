<style>

    .added-column {
        text-align: left;
        width: 150px;
    }

    .added-column i {
        display: none;
        cursor: pointer;
        margin-left: 5px;
    }

    .added-column:hover i {
        display: inline-block;
    }

    .activityTask-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

    i.fa-times-circle {
        margin-left: -1px !important;
        color: gray !important;
        cursor: pointer !important;
    }

    .attributeTooltip .attributeTooltiptext {
        visibility: hidden;
        width: 200px;
        background-color: #7BB7EB;
        color: #141f9f;
        text-align: left;
        border-radius: 6px;
        padding: 5px 0;
        position: absolute;
        z-index: 1;
        top: -5px;
        bottom: auto;
        right: 100%;
        opacity: 0;
        transition: opacity 1s;
    }

    .attributeTooltip .attributeTooltiptext::after {
        content: "";
        position: absolute;
        top: 25%;
        left: 102%;
        margin-left: -5px;
        border-width: 5px;
        border-style: solid;
        border-color: transparent transparent transparent #7BB7EB;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }

    /* The Close Button */
    .img-model .closeImage,
    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus,
    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;

        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage1:hover,
    .img-model .closeImage1:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
    }
</style>
<div class="responsive-table" style="padding: 10px;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="width: 20px;">
                <span ng-if="external.external == false">
                    <i class="la la-plus" style="cursor: pointer"
                       ng-if="hasPermission('project','edit')"
                       title="{{activityDetailsVm.addTask}}" class="btn btn-sm btn-success min-width"
                       ng-click="addActivityTasks()"></i>
                </span>
               <span ng-if="external.external == true && sharedProjectPermission == 'WRITE'">
                   <i class="la la-plus" style="cursor: pointer"
                      ng-if="hasPermission('project','edit')"
                      title="{{activityDetailsVm.addTask}}"
                      class="btn btn-sm btn-success min-width" ng-click="addActivityTasks()"></i>
               </span>
            </th>
            <th class="col-width-250" translate>NAME</th>
            <th class="description-column" translate>DESCRIPTION</th>
            <th translate>ASSIGNED_TO</th>
            <th translate>PERCENT_COMPLETE</th>
            <th style="width: 100px;" translate>STATUS</th>
            <th class='added-column'
                style="width: 150px;"
                ng-repeat="selectedAttribute in activityTasksVm.selectedAttributes">
                {{selectedAttribute.name}}
                <i class="fa fa-times-circle"
                   ng-click="activityTasksVm.removeAttribute(selectedAttribute)"
                   title="{{RemoveColumnTitle}}"></i>
            </th>
            <th style="width: 100px;" translate>ACTIONS</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="activityTasksVm.loading == true">
            <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_TASKS</span>
                        </span>
            </td>
        </tr>

        <tr ng-if="activityTasksVm.loading == false && activityTasksVm.activityTasks.length == 0">
            <td colspan="25" translate>NO_TASKS</td>
        </tr>

        <tr id="{{$index}}" ng-repeat="task in activityTasksVm.activityTasks"
            dragactivitytask activity-tasks="activityTasksVm.activityTasks"
            update-task-seq="activityTasksVm.updateTaskSeq">
            <td></td>
            <td class="col-width-250">
                <a ng-if="task.editMode == false ||  task.percentComplete > 0" href=""
                   ng-click="activityTasksVm.showTaskDetails(task)">{{task.name}}</a>
                <span ng-if="task.editMode == true && task.percentComplete == 0">
                    <input type="text" class="form-control" style="width: 150px;" ng-model="task.name"/>
                </span>
            </td>
            <td class="description-column">
                <span ng-if="task.editMode == false">{{task.description}}</span>
                <span ng-if="task.editMode == true">
                    <input type="text" class="form-control" style="width: 150px;" ng-model="task.description"/>
                </span>
            </td>
            <td>{{task.assignedToObject.fullName}}</td>
            <td>
                <div ng-if="task.percentComplete < 100 && task.editMode == false"
                     class="activityTask-progress progress text-center">
                    <div style="width:{{task.percentComplete}}%"
                         class="progress-bar progress-bar-primary progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                        <span style="margin-left: 2px;">{{task.percentComplete}}%</span>
                    </div>
                </div>
                <div ng-if="task.percentComplete == 100 && task.editMode == false"
                     class="activityTask-progress progress text-center">
                    <div style="width:{{task.percentComplete}}%"
                         class="progress-bar progress-bar-success progress-bar-striped active"
                         role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                        <span style="margin-left: 2px;">{{task.percentComplete}}%</span>
                    </div>
                </div>
                <span ng-if="task.editMode == true">
                    <input type="number" class="form-control" style="width: 150px;" ng-model="task.percentComplete"/>
                </span>
            </td>
            <td>
                <task-status task="task"></task-status>
            </td>


            <td class="added-column"
                ng-repeat="objectAttribute in activityTasksVm.selectedAttributes">
                <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST'
                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.id">
                    <a href=""
                       ng-if="objectAttribute.refType == 'ITEM'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="activityTasksVm.showAttributeDetails(task[attrName])">{{task[attrName].itemNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'ITEMREVISION'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="activityTasksVm.showAttributeDetails(task[attrName])">
                        {{task[attrName].taskMaster+" "+task[attrName].revision+" "+
                        task[attrName].lifeCyclePhase.phase}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'CHANGE'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="activityTasksVm.showAttributeDetails(task[attrName])">{{task[attrName].ecoNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'MANUFACTURER'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="activityTasksVm.showAttributeDetails(task[attrName])">{{task[attrName].name}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'MANUFACTURERPART'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="activityTasksVm.showAttributeDetails(task[attrName])">{{task[attrName].partNumber}}
                    </a>
                    <a href=""
                       ng-if="objectAttribute.refType == 'WORKFLOW'"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                       ng-click="activityTasksVm.showAttributeDetails(task[attrName])">{{task[attrName].name}}
                    </a>
                    <a href="#" ng-if="objectAttribute.refType == 'PERSON'">
                        {{task[attrName].firstName}}
                    </a>
                </p>

                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'"
                     ng-init="attrName = objectAttribute.id">
                    <p>
                        <a ng-if="task[attrName].length > 0" href="">
                            {{task[attrName].length}} Attachments
                        </a>
                    </p>

                    <div class="attributeTooltiptext">
                        <ul>
                            <li ng-repeat="attachment in task[attrName]">
                                <a href="" ng-click="activityTasksVm.openAttachment(attachment)"
                                   title="{{'CLICK_TO_DOWNLOAD_ATTACHMENT' | translate}}"
                                   style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                    {{attachment.name}}
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>


                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'&&
                             objectAttribute.dataType == 'LIST' && objectAttribute.listMultiple == true"
                     ng-init="attrName = objectAttribute.id">
                    <p>
                        <a ng-if="task[attrName].length > 0" href="">
                            {{task[attrName].length}} listOfValue
                        </a>
                    </p>

                    <div class="attributeTooltiptext">
                        <ul>
                            <li ng-repeat="listValue in task[attrName]">
                                <a href=""
                                   style="margin-bottom: 5px;width:200px;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                    {{listValue}}
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>

                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'&&
                             objectAttribute.dataType == 'LIST' && objectAttribute.listMultiple != true"
                     ng-init="attrName = objectAttribute.id">
                    <p>
                        <a href="">
                            {{task[attrName]}}
                        </a>
                    </p>
                </div>

                <div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                         && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
                     ng-init="attrName = objectAttribute.id">
                    <a href="" ng-click="activityTasksVm.showImage(task[attrName])"
                       title="{{'CLICK_TO_SHOW_IMAGE' | translate}}">
                        <img ng-if="task[attrName] != null"
                             ng-src="{{task[attrName]}}"
                             style="height: 30px;width: 40px;margin-bottom: 5px;">
                    </a>

                    <div id="myModal2" class="img-model modal">
                        <span class="closeImage1">&times;</span>
                        <img class="modal-content" id="img03">
                    </div>
                </div>

                <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT' && objectAttribute.dataType != 'LONGTEXT'
                         && objectAttribute.dataType != 'TEXT'
                          && objectAttribute.dataType != 'HYPERLINK'
                          && objectAttribute.dataType != 'LIST'
                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.id">
                    {{task[attrName]}}
                </p>

                <p ng-if="objectAttribute.dataType == 'LONGTEXT'"
                   ng-init="attrName = objectAttribute.id" title="{{task[attrName]}}"
                   style="height: 30px;width: 70px;margin-bottom: -7px;">
                    {{task[attrName]}}
                </p>

                <p ng-if="objectAttribute.dataType == 'TEXT'"
                   ng-init="attrName = objectAttribute.id" title="{{task[attrName]}}"
                   style="height: 30px;width: 70px;margin-bottom: -7px;">
                    {{task[attrName]}}
                </p>

                <p ng-if="objectAttribute.dataType == 'HYPERLINK'"
                   ng-init="attrName = objectAttribute.id">
                    <a href=""
                       ng-click="showHyperLink(task[attrName])">{{task[attrName]}}</a>
                </p>

                        <span ng-init="currencyType = objectAttribute.id+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="task[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.id"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{task[attrName]}}
                        </span>


                <%--  Show Rich text content in modal --%>


                           <span ng-init="attrName = objectAttribute.id"
                                 ng-if="objectAttribute.dataType == 'RICHTEXT'">
                               <a href=""
                                  ng-if="task[attrName] != null && task[attrName] != undefined && task[attrName] != ''"
                                  data-toggle="modal"
                                  ng-click="showRichTextSidePanel(task[attrName],objectAttribute,task)"
                                       >Click to show RichText</a>
                        </span>

                <p ng-if="objectAttribute.name == 'ModifiedBy'">{{task.modifiedByObject.firstName}}</p>

                <p ng-if="objectAttribute.name == 'CreatedBy'">{{task.createdByObject.firstName}}</p>

                <p ng-if="objectAttribute.name == 'CreatedDate'">{{task.createdDate}}</p>

                <p ng-if="objectAttribute.name == 'ModifiedDate'">{{task.modifiedDate}}</p>


            </td>
            <td class="text-center">
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-if="task.editMode == true"
                                    ng-click="activityTasksVm.updateTask(task)">
                                    <a translate>SAVE_TASK</a>
                                </li>
                                <li ng-if="task.editMode == true"
                                    ng-click="activityTasksVm.cancelChanges(task)">
                                    <a translate>CANCEL</a>
                                </li>

                                <li ng-if="task.editMode == false && (hasPermission('project','all') || loginPersonDetails.person.id == task.assignedTo)"
                                    ng-class="{'disabled':task.status == 'FINISHED'}"
                                    ng-click="activityTasksVm.editTask(task)">
                                    <a translate>EDIT_TASK</a>
                                </li>
                                <li ng-if="task.editMode == false && external.external == true && sharedProjectPermission == 'WRITE'"
                                    ng-class="{'disabled':task.status == 'FINISHED'}"
                                    ng-click="activityTasksVm.editTask(task)">
                                    <a translate>EDIT_TASK</a>
                                </li>

                                <li ng-if="task.editMode == false && hasPermission('project','delete')"
                                    ng-click="activityTasksVm.deleteTask(task)"
                                    ng-class="{'disabled':task.status == 'INPROGRESS' || task.status == 'FINISHED'}"><a
                                        translate>TASK_DIALOG_TITLE</a>
                                </li>

                                <li ng-if="task.editMode == false && external.external == true && sharedProjectPermission == 'WRITE'"
                                    ng-click="activityTasksVm.deleteTask(task)"
                                    ng-class="{'disabled':task.status == 'INPROGRESS' || task.status == 'FINISHED'}"><a
                                        translate>TASK_DIALOG_TITLE</a>
                                </li>
                                <li ng-if="task.editMode == false && ((hasPermission('project','all') || loginPersonDetails.person.id == task.assignedTo) && task.status != 'FINISHED')"
                                    ng-click="activityTasksVm.finishActivityTask(task)">
                                    <a translate>FINISH</a>
                                </li>
                            </ul>
                        </span>
            </td>

        </tr>
        </tbody>
    </table>
</div>