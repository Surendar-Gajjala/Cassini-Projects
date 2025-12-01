<style>

    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 0;
        overflow: auto;
    }

    .view-content .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        z-index: 5;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0px !important;
        height: 40px;
        width: 100%;
        border-top: 1px solid #D3D7DB;
        display: table;
    }

    .view-content .table-footer > div {
        display: table-row;
        line-height: 30px;
    }

    .view-content .table-footer > div h5 {
        margin: 0;
    }

    .view-content .table-footer > div > div {
        display: table-cell;
        vertical-align: middle;
    }

    .view-content .table-footer > div > div > i {
        font-size: 16px;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #td {
        word-wrap: break-word;
        width: 300px;
        white-space: normal;
        text-align: left;
    }

    .added-column {
        text-align: left;
        width: 150px;
    }

    .added-column i {
        display: none;
        cursor: pointer;
        margin-left: 5px;
    }

    .project-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

    .added-column:hover i {
        display: inline-block;
    }

    .attributeTooltip {
        position: relative;
        display: inline-block;
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

    .projectPlan-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

    .activityTask-progress.progress {
        background-color: #B0C7CF;
        height: 20px !important;
        margin: 0 !important;
    }

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }

    .product-image {
        width: 100%;
        height: 300px;
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

    .widget {
        padding-right: 50px !important;
        cursor: pointer;
    }

    a.activeView {
        text-decoration: none;
        font-weight: bold;
        color: #121C25;
        font-size: 16px !important;
    }

    .item-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

</style>
<div class="view-toolbar" style="padding-top: 0px;padding-left: 4px;">
    <div style="width: 100%;display: inline-flex;">
        <div style="margin-top: 12px;display: inline-flex;padding-left: 15px;padding-right: 0px;width: 80%;">
            <div class="widget"
                 ng-if="externalUserVm.personProjects.length > 0 || externalUserVm.personActivitys.content.length > 0 || externalUserVm.personTasks.content.length > 0">
                <a class="item-notification" ng-class="{'activeView':externalUserVm.sharedItemView == true}"
                   ng-click='externalUserVm.sharedItem()' translate>
                    ITEMS_ALL_TITLE</a>
            </div>
            <div class="widget" ng-if="externalUserVm.personProjects.length > 0">
                <a class="item-notification" ng-class="{'activeView':externalUserVm.sharedProjectView == true}"
                   ng-click='externalUserVm.sharedProject()' translate>PROJECTS
                </a>
            </div>
            <div class="widget" ng-if="externalUserVm.personActivitys.content.length > 0">
                <a class="item-notification" ng-class="{'activeView':externalUserVm.activityView == true}"
                   ng-click='externalUserVm.sharedActivity()' translate>ACTIVITYS
                </a>
            </div>
            <div class="widget" ng-if="externalUserVm.personTasks.content.length > 0">
                <a class="item-notification" ng-class="{'activeView':externalUserVm.taskView == true}"
                   ng-click='externalUserVm.sharedTask()' translate>TASKS
                </a>
            </div>
        </div>
    </div>
</div>
<div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-if="externalUserVm.sharedItemView">
    <div class="responsive-table" style="padding: 10px;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width:1px;white-space:nowrap;"></th>
                <th style="width: 1% !important;white-space: nowrap;" translate="ITEM_ALL_ITEM_NUMBER"></th>
                <th style="width: 150px" translate="ITEM_CLASS"></th>
                <th style="width: 150px" translate="ITEM_ALL_ITEM_TYPE"></th>
                <th style="width: 150px" translate="ITEM_ALL_ITEM_NAME"></th>
                <th style="" translate="ITEM_ALL_DESCRIPTION"></th>
                <th style="width: 150px; text-align: center;" translate="ITEM_ALL_REVISION"></th>
                <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                <th style="width: 100px;" translate="THUMBNAIL"></th>
                <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
                <th style="width: 150px" translate="ITEM_ALL_LIFECYCLE"></th>
                <th style="width: 150px" translate="ITEM_ALL_RELEASED_DATE"></th>
                <th style="width: 100px" translate="SHARED_BY"></th>
                <th style="width: 100px" translate="SHARED_ON"></th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="externalUserVm.loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="externalUserVm.loading == false && externalUserVm.sharedObjects.length == 0">
                <td colspan="25" translate>NO_SHARING_ITEMS</td>
            </tr>

            <tr ng-repeat="item in externalUserVm.sharedObjects.content">
                <td style="width:1px !important;white-space: nowrap;text-align: left;">
                    <a ng-if="item.objectIdObject.configurable == true"
                       title="{{externalUserVm.configurableItem}}"
                       class="fa fa-cog"
                       aria-hidden="true"></a>
                    <a ng-if="item.objectIdObject.configured == true" title="{{externalUserVm.configuredItem}}"
                       class="fa fa-cogs"
                       aria-hidden="true"></a>
                </td>
                <td style="width: 1% !important;white-space: nowrap;">
                    <a href="" ng-click="externalUserVm.showItem(item)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{item.objectIdObject.itemNumber}}</a>
                </td>
                <td style="width:150px;">
                    <item-class item="item.objectIdObject.itemType.itemClass"></item-class>
                </td>
                <td style="width: 150px">{{item.objectIdObject.itemType.name}}</td>
                <td class="col-width-200" title="{{item.objectIdObject.itemName}}"><span
                        ng-bind-html="item.objectIdObject.itemName  | highlightText: freeTextQuery"></span>
                    {{item.objectIdObject.itemName.length > 20 ? '...' : ''}}
                </td>
                <td class="col-width-200" title="{{item.objectIdObject.description}}"><span
                        ng-bind-html="item.objectIdObject.description  | highlightText: freeTextQuery"></span>
                </td>
                <td style="width: 150px; text-align: center;">
                    {{item.objectIdObject.latestRevisionObject.revision}}
                </td>
                <td style="width: 100px; text-align: center;">{{item.shareType}}</td>


                <td style="width: 100px">
                    <div>
                        <a ng-if="item.thumbnailImage != null" href=""
                           ng-click="externalUserVm.showThumbnailImage(item)"
                           title="{{'CLICK_TO_SHOW_LARGE_IMAGE' | translate}}">
                            <img ng-src="{{item.thumbnailImage}}"
                                 style="height: 30px;width: 40px;margin-bottom: 5px;">
                        </a>
                        <a ng-if="item.thumbnailImage == null" href="">
                            <img src="app/assets/images/cassini-logo-greyscale.png" title="No thumbnail" alt=""
                                 class="no-thumbnail-preview">
                        </a>
                        <%--
                                                <div id="myModal234" class="img-model modal">
                                                    <span class="closeImage">&times;</span>
                                                    <img class="modal-content" id="img134">
                                                </div>--%>
                        <div id="item-thumbnail{{item.id}}" class="item-thumbnail modal">
                            <div class="item-thumbnail-content">
                                <div class="thumbnail-content" style="display: flex;width: 100%;">
                                    <div class="thumbnail-view" id="thumbnail-view{{item.id}}">
                                        <div id="thumbnail-image{{item.id}}"
                                             style="display: table-cell;vertical-align: middle;text-align: center;">
                                            <img ng-src="{{item.thumbnailImage}}"
                                                 style="height: auto;width: auto;max-width: 100%;max-height: 90%"/>
                                                    <span class="thumbnail-close"
                                                          id="thumbnail-close{{item.id}}"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="label" style="width: 100px; text-align: center;"
                         ng-class="{'label-info': item.permission === 'READ','label-success': item.permission === 'WRITE'}">
                        {{item.permission}}
                    </div>
                </td>
                <td style="width: 150px">
                    <item-status item="item.objectIdObject.latestRevisionObject"></item-status>
                </td>
                <td style="width: 150px">
                    <span>{{item.objectIdObject.latestRevisionObject.releasedDate}}</span>
                </td>
                <td style="width: 100px">{{item.sharedByObject.firstName}}</td>
                <td style="width: 100px">{{item.createdDate}}</td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="table-footer">
        <div>
            <div>
                <h5><span style="padding: 5px" translate>DISPLAYING</span>
                    <span ng-if="externalUserVm.sharedObjects.totalElements ==0">{{(externalUserVm.pageable.page*externalUserVm.pageable.size)}}</span>
                    <span ng-if="externalUserVm.sharedObjects.totalElements > 0">{{(externalUserVm.pageable.page*externalUserVm.pageable.size)+1}}</span>
                    -
                    <span ng-if="externalUserVm.sharedObjects.last ==false">{{((externalUserVm.pageable.page+1)*externalUserVm.pageable.size)}}</span>
                    <span ng-if="externalUserVm.sharedObjects.last == true">{{externalUserVm.sharedObjects.totalElements}}</span>
                    <span translate>OF</span> &nbsp;{{externalUserVm.sharedObjects.totalElements}} <span
                            translate>AN</span>
                </h5>
            </div>

            <div class="text-right">
                    <span class="mr10"><span translate>PAGE</span> {{externalUserVm.sharedObjects.totalElements != 0 ? externalUserVm.sharedObjects.number+1:0}} <sapn
                            translate>OF
                    </sapn> {{externalUserVm.sharedObjects.totalPages}}</span>
                <a href="" ng-click="externalUserVm.sharedItemsPreviousPage()"
                   ng-class="{'disabled': externalUserVm.sharedObjects.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="externalUserVm.sharedItemsNextPage()"
                   ng-class="{'disabled': externalUserVm.sharedObjects.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>


        </div>
    </div>
</div>

<div class="view-content no-padding" style="overflow-y: auto;padding: 10px;"
     ng-if="externalUserVm.sharedProjectView">
    <div class="responsive-table" style="padding: 10px;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 150px" translate="PROJECT_NAME"></th>
                <th style="" translate="ITEM_ALL_DESCRIPTION"></th>
                <th style="width: 150px" translate>PROJECT_MANAGER</th>
                <th style="width: 150px" translate>PERCENT_COMPLETE</th>
                <th style="width: 150px" translate>PLANNED_START_DATE</th>
                <th style="width: 150px" translate>PLANNED_FINISH_DATE</th>
                <th style="width: 150px" translate>ACTUAL_START_DATE</th>
                <th style="width: 150px" translate>ACTUAL_FINISH_DATE</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="externalUserVm.loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="externalUserVm.loading == false && externalUserVm.personProjects.length == 0">
                <td colspan="25" translate>NO_SHARING_ITEMS</td>
            </tr>

            <tr ng-repeat="project in externalUserVm.personProjects">
                <td style="width: 150px">
                    <a href="" ng-click="externalUserVm.showProject(project)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{project.name}}</a>
                </td>
                <td id="td">{{project.description}}</td>
                <td style="width: 150px">{{project.projectManagerObject.firstName}}</td>
                <td>
                    <div ng-if="project.percentComplete < 100"
                         class="project-progress progress text-center">
                        <div style="width:{{project.percentComplete}}%"
                             class="progress-bar progress-bar-primary progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 2px;">{{project.percentComplete}}%</span>
                        </div>
                    </div>
                    <div ng-if="project.percentComplete == 100"
                         class="project-progress progress text-center">
                        <div style="width:{{project.percentComplete}}%"
                             class="progress-bar progress-bar-success progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 10px;">{{project.percentComplete}}%</span>
                        </div>
                    </div>
                </td>

                <td style="width: 150px">{{project.plannedStartDate}}</td>

                <td style="width: 150px">{{project.plannedFinishDate}}</td>

                <td style="width: 150px">{{project.actualStartDate}}</td>

                <td style="width: 150px">{{project.actualFinishDate}}</td>
            </tr>
            </tbody>
        </table>


    </div>
</div>
<div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-if="externalUserVm.activityView">
    <div class="responsive-table" style="padding: 10px;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 150px" translate="NAME"></th>
                <th style="" translate="ITEM_ALL_DESCRIPTION"></th>
                <th style="width: 150px" translate>PERCENT_COMPLETE</th>
                <th style="width: 150px" translate>PLANNED_START_DATE</th>
                <th style="width: 150px" translate>PLANNED_FINISH_DATE</th>
                <th style="width: 150px" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="externalUserVm.loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="externalUserVm.loading == false && externalUserVm.personActivitys.length == 0">
                <td colspan="25" translate>NO_SHARING_ITEMS</td>
            </tr>

            <tr ng-repeat="activity in externalUserVm.personActivitys.content">
                <td style="width: 150px">
                    <a href="" ng-click="externalUserVm.openActivityDetails(activity)"
                       title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{activity.name}}</a>
                </td>
                <td id="td">{{activity.description}}</td>
                <td>
                    <div ng-if="activity.percentComplete < 100"
                         class="projectPlan-progress progress text-center">
                        <div style="width:{{activity.percentComplete}}%"
                             class="progress-bar progress-bar-primary progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 2px;">{{activity.percentComplete}}%</span>
                        </div>
                    </div>
                    <div ng-if="activity.percentComplete == 100"
                         class="project-progress progress text-center">
                        <div style="width:{{activity.percentComplete}}%"
                             class="progress-bar progress-bar-success progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 10px;">{{activity.percentComplete}}%</span>
                        </div>
                    </div>
                </td>
                <td>{{activity.plannedStartDate}}</td>
                <td>{{activity.plannedFinishDate}}</td>
                <td>
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                      <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                          style="z-index: 9999 !important;">
                          <li ng-class="{'disabled':wbs.percentComplete == 100 || wbs.status =='FINISHED'}"
                              ng-click="externalUserVm.editActivity(activity)">
                              <a href="" translate>EDIT_ACTIVITY</a>
                          </li>
                          <%-- <li ng-class="{'disabled':wbs.activityTasks.length > 0 || wbs.activityFiles.length > 0
                                         || wbs.activityDeliverables.length > 0 || wbs.activityItemReferences.length > 0 || wbs.status == 'FINISHED' || wbs.percentComplete == 100}"
                               ng-click="externalUserVm.deleteWbs(activity)">
                               <a href="" translate>DELETE_ACTIVITY</a>
                           </li>--%>
                      </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>


    </div>
</div>
<div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-if="externalUserVm.taskView">
    <div class="responsive-table" style="padding: 10px;">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 150px" translate="NAME"></th>
                <th style="" translate="ITEM_ALL_DESCRIPTION"></th>
                <th translate>ASSIGNED_TO</th>
                <th style="width: 150px" translate>PERCENT_COMPLETE</th>
                <th style="width: 150px" translate>STATUS</th>
                <th style="width: 150px" translate>ACTIONS</th>

            </tr>
            </thead>
            <tbody>
            <tr ng-if="externalUserVm.loading == true">
                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                </td>
            </tr>

            <tr ng-if="externalUserVm.loading == false && externalUserVm.personTasks.length == 0">
                <td colspan="25" translate>NO_SHARING_ITEMS</td>
            </tr>

            <tr ng-repeat="task in externalUserVm.personTasks.content">
                <td class="col-width-250">
                    <a ng-if="task.editMode == false ||  task.percentComplete > 0" href=""
                       ng-click="externalUserVm.showTaskDetails(task)">{{task.name}}</a>
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

                <td>
                    <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-if="task.editMode == true"
                                    ng-click="externalUserVm.updateTask(task)">
                                    <a translate>SAVE_TASK</a>
                                </li>
                                <li ng-if="task.editMode == true"
                                    ng-click="externalUserVm.cancelChanges(task)">
                                    <a translate>CANCEL</a>
                                </li>

                                <li ng-class="{'disabled':task.status == 'FINISHED'}" ng-if="task.editMode == false"
                                    ng-click="externalUserVm.editTask(task)">
                                    <a translate>EDIT_TASK</a>
                                </li>

                                <li ng-click="externalUserVm.deleteTask(task)" ng-if="task.editMode == false"
                                    ng-class="{'disabled':task.status == 'INPROGRESS' || task.status == 'FINISHED'}"><a
                                        translate>TASK_DIALOG_TITLE</a>
                                </li>

                                <li ng-if="task.editMode == false && task.status != 'FINISHED'"
                                    ng-click="externalUserVm.finishActivityTask(task)">
                                    <a translate>FINISH</a>
                                </li>
                            </ul>
                        </span>
                </td>
            </tr>
            </tbody>
        </table>


    </div>
</div>
