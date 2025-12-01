<style>
    .view-content .responsive-table {
        padding: 10px;
        position: absolute;
        bottom: 40px;
        top: 42px;
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
        bottom: 0 !important;
        height: 40px !important;
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

    span.activeView {
        text-decoration: none;
        font-weight: bold;
        color: #467aff;
        font-size: 16px !important;
    }

    .item-notification {
        text-decoration: none;
        position: relative;
        display: inline-block;
        border-radius: 2px;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

    .popover-title {
        font-size: 14px;
        font-weight: 600;
        text-align: center;
        line-height: 25px;
    }

    .popover {
        max-width: 500px;
        width: 500px;
    }

    .popover-content {
        max-height: 220px;
        overflow-y: auto;
    }

    .popover table {
        width: 497px;
        max-width: 100% !important;
    }

    .popover.bottom > .arrow::after {
        border-bottom-color: #f7f7f7;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;">{{externalUserVm.itemTitle}}</span>

        <button class="btn btn-sm btn-info">
            <span class="item-notification" ng-class="{'activeView':externalUserVm.sharedItemView == true}"
                  ng-click='externalUserVm.sharedObjectFilter.searchQuery = null;externalUserVm.searchText = null;sharedItem()'
                  title="Items">
                <i class="fa fa-cogs" style=""></i></span>
        </button>

        <button class="btn btn-sm btn-info" ng-if="externalUserVm.sharedCounts.mfrPart > 0">
            <span class="item-notification" ng-class="{'activeView':externalUserVm.mfrPartView == true}"
                  ng-click='externalUserVm.sharedObjectFilter.searchQuery = null;externalUserVm.searchText = null;sharedMfrPart()'
                  title="Manufacturer Parts">
                <i class="fa fa-cubes" style=""></i>
            </span>
        </button>

        <button class="btn btn-sm btn-info">
            <span class="item-notification" ng-class="{'activeView':externalUserVm.programView == true}"
                  ng-click='externalUserVm.sharedObjectFilter.searchQuery = null;externalUserVm.searchText = null;sharedProgram()'
                  title="Programs">
                  <i class="fa fa-calendar-check-o" aria-hidden="true"></i>
            </span>
        </button>


        <button class="btn btn-sm btn-info" ng-if="externalUserVm.sharedCounts.customObject > 0">
            <span class="item-notification" ng-class="{'activeView':externalUserVm.customObjectView == true}"
                  ng-click='externalUserVm.sharedObjectFilter.searchQuery = null;externalUserVm.searchText = null;sharedCustomObject()'
                  title="Custom Objects">
                <i class="fa fa-copyright nav-icon-font" style=""></i>
            </span>
        </button>

        <button class="btn btn-sm btn-info" ng-if="externalUserVm.sharedCounts.supplier > 0">
            <span class="item-notification" ng-class="{'activeView':externalUserVm.supplierView == true}"
                  ng-click='externalUserVm.sharedObjectFilter.searchQuery = null;externalUserVm.searchText = null;sharedSupplier()'
                  title="Suppliers">
                <i class="fa flaticon-office42" style=""></i>
            </span>
        </button>


        <%--<button class="btn btn-sm btn-info" ng-if="externalUserVm.sharedCounts.folder > 0">
            <span class="item-notification" ng-class="{'activeView':externalUserVm.folderView == true}"
                  ng-click='externalUserVm.sharedObjectFilter.searchQuery = null;externalUserVm.searchText = null;sharedFolder()'
                  title="Folders">
                <i class="fa fa-folder-open-o" style=""></i>
            </span>
        </button>--%>

        <button class="btn btn-sm btn-info"
                ng-if="externalUserVm.sharedCounts.sharedFolders > 0 || externalUserVm.sharedCounts.folder > 0">
            <span class="item-notification" ng-class="{'activeView':externalUserVm.sharedFoldersView == true}"
                  ng-click='sharedExternalUsersFoldersAndFiles()'
                  title="Shared folders and files">
                <i class="fa fa-file" style=""></i>
            </span>
        </button>

        <button class="btn btn-sm btn-info" title="{{downloadTitle}}"
            ng-show="externalUserVm.sharedFoldersView == true"
                ng-click="externalUserVm.downloadObjectFilesAsZip()">
              <i class="fa fa-download" aria-hidden="true" style=""></i>
        </button>
        <button class="btn btn-sm btn-info" ng-if="externalUserVm.sharedCounts.declaration > 0">
            <span class="item-notification" ng-class="{'activeView':externalUserVm.declarationView == true}"
                  ng-click='externalUserVm.sharedObjectFilter.searchQuery = null;externalUserVm.searchText = null;showSharedDeclaration()'
                  title="Declarations">
                <i class="fa fa-table" style=""></i>
            </span>
        </button>

        <button class="btn btn-sm btn-info" ng-if="externalUserVm.sharedCounts.project > 0 || externalUserVm.sharedCounts.activity > 0
                    || externalUserVm.sharedCounts.task > 0">
            <span class="item-notification" ng-class="{'activeView':externalUserVm.sharedProjectView == true}"
                  ng-click='externalUserVm.sharedObjectFilter.searchQuery = null;externalUserVm.searchText = null;showSharedProjects()'
                  title="Projects">
                <i class="fa fa-calendar" style=""></i>
            </span>
        </button>
        <%--<div ng-if="externalUserVm.sharedFoldersView == true">
            <free-text-search search-term="freeTextQuerys" on-clear="externalUserVm.resetPage"

                              on-search="externalUserVm.freeTextSearchSharedExternalUserFolders"></free-text-search>
        </div>--%>


        <free-text-search on-clear="externalUserVm.resetPage" search-term="externalUserVm.searchText"
                          ng-hide="externalUserVm.sharedProjectView == true"
                          on-search="externalUserVm.freeTextSearch"
                          filter-search="externalUserVm.filterSearch"></free-text-search>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;"
         ng-show="externalUserVm.sharedItemView">
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
                    <%--<th style="width: 100px;" translate="THUMBNAIL">
                        </th>--%>
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

                <tr ng-if="externalUserVm.loading == false && externalUserVm.sharedCounts.item == 0">
                    <td colspan="25" translate>NO_SHARING_ITEMS</td>
                </tr>

                <tr ng-repeat="item in externalUserVm.sharedObjects.content">
                    <td style="width:1px !important;white-space: nowrap;text-align: left;">
                        <all-view-icons object="item.objectType"></all-view-icons>
                    </td>
                    <td style="width: 1% !important;white-space: nowrap;">
                        <a href="" ng-click="externalUserVm.showItem(item)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span
                                    ng-bind-html="item.number  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>
                    <td style="width:150px;">
                        <item-class item="item.itemClass"></item-class>
                    </td>
                    <td class="column-width-150" title="{{item.type}}"><span
                            ng-bind-html="item.type  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                        {{item.type.length > 20 ? '...' : ''}}
                    </td>
                    <td class="col-width-200" title="{{item.name}}"><span
                            ng-bind-html="item.name  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                        {{item.name.length > 20 ? '...' : ''}}
                    </td>
                    <td class="col-width-200" title="{{item.objectIdObject.description}}"><span
                            ng-bind-html="item.description  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td style="width: 150px; text-align: center;">
                        {{item.revision}}
                    </td>
                    <td style="width: 100px; text-align: center;">{{item.shareType}}</td>


                    <%-- <td style="width: 100px">
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
                            &lt;%&ndash;
                            <div id="myModal234" class="img-model modal">
                                <span class="closeImage">&times;</span>
                                <img class="modal-content" id="img134">
                            </div>&ndash;%&gt;
                            <div id="item-thumbnail{{item.id}}" class="item-thumbnail modal">
                                <div class="item-thumbnail-content">
                                    <div class="thumbnail-content" style="display: flex;width: 100%;">
                                        <div class="thumbnail-view" id="thumbnail-view{{item.id}}">
                                            <div id="thumbnail-image{{item.id}}"
                                                style="display: table-cell;vertical-align: middle;text-align: center;">
                                                <img ng-src="{{item.thumbnailImage}}"
                                                    style="height: auto;width: auto;max-width: 100%;max-height: 90%" />
                                                <span class="thumbnail-close" id="thumbnail-close{{item.id}}"></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        </td>--%>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': item.permission === 'READ','label-success': item.permission === 'WRITE'}">
                            {{item.permission}}
                        </div>
                    </td>
                    <td style="width: 150px">
                        <item-status item="item"></item-status>
                    </td>
                    <td style="width: 150px">
                        <span>{{item.releaseDate}}</span>
                    </td>
                    <td style="width: 100px">{{item.sharedBy}}</td>
                    <td style="width: 100px">{{item.sharedOn}}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="externalUserVm.sharedObjects" pageable="externalUserVm.pageable"
                          previous-page="externalUserVm.previousPage" next-page="externalUserVm.nextPage"
                          page-size="externalUserVm.pageSize"></table-footer>
        </div>
    </div>

    <%--<div class="view-content no-padding" style="overflow-y: auto;padding: 10px;"
        ng-show="externalUserVm.sharedProjectView">
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
                            <div ng-if="project.percentComplete < 100" class="project-progress progress text-center">
                                <div style="width:{{project.percentComplete}}%"
                                    class="progress-bar progress-bar-primary progress-bar-striped active" role="progressbar"
                                    aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                                    <span style="margin-left: 2px;">{{project.percentComplete}}%</span>
                                </div>
                            </div>
                            <div ng-if="project.percentComplete == 100" class="project-progress progress text-center">
                                <div style="width:{{project.percentComplete}}%"
                                    class="progress-bar progress-bar-success progress-bar-striped active" role="progressbar"
                                    aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
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
        </div>--%>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="externalUserVm.activityView">
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
                        <div ng-if="activity.percentComplete == 100" class="project-progress progress text-center">
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
                            <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                                <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                    style="z-index: 9999 !important;">
                                    <li ng-class="{'disabled':wbs.percentComplete == 100 || wbs.status =='FINISHED'}"
                                        ng-click="externalUserVm.editActivity(activity)">
                                        <a href="" translate>EDIT_ACTIVITY</a>
                                    </li>
                                    <%-- <li
                                        ng-class="{'disabled':wbs.activityTasks.length > 0 || wbs.activityFiles.length > 0
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
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="externalUserVm.taskView">
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
                                <input type="text" class="form-control" style="width: 150px;"
                                       ng-model="task.description"/>
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
                                <input type="number" class="form-control" style="width: 150px;"
                                       ng-model="task.percentComplete"/>
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
                                    <li ng-if="task.editMode == true" ng-click="externalUserVm.updateTask(task)">
                                        <a translate>SAVE_TASK</a>
                                    </li>
                                    <li ng-if="task.editMode == true" ng-click="externalUserVm.cancelChanges(task)">
                                        <a translate>CANCEL</a>
                                    </li>

                                    <li ng-class="{'disabled':task.status == 'FINISHED'}" ng-if="task.editMode == false"
                                        ng-click="externalUserVm.editTask(task)">
                                        <a translate>EDIT_TASK</a>
                                    </li>

                                    <li ng-click="externalUserVm.deleteTask(task)" ng-if="task.editMode == false"
                                        ng-class="{'disabled':task.status == 'INPROGRESS' || task.status == 'FINISHED'}">
                                        <a translate>TASK_DIALOG_TITLE</a>
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
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="externalUserVm.mfrView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="col-width-250" translate>MANUFACTURER_NAME</th>
                    <th style="width: 200px;" translate>MANUFACTURER_TYPE</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th translate="ITEM_ALL_LIFECYCLE"></th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
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

                <tr ng-if="externalUserVm.loading == false && externalUserVm.sharedCounts.mfr == 0">
                    <td colspan="25" translate>NO_SHARING_MFRS</td>
                </tr>

                <tr ng-repeat="mfr in externalUserVm.sharedMfrs.content">
                    <td style="width: 150px">
                        <a href="" ng-click="externalUserVm.showMfrDetails(mfr)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">{{mfr.name}}</a>
                    </td>
                    <td>{{mfr.type}}</td>
                    <td id="td">{{mfr.description}}</td>
                    <td>
                        <item-status item="mfr"></item-status>
                    </td>
                    <td style="width: 100px; text-align: center;">{{mfr.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': mfr.permission === 'READ','label-success': mfr.permission === 'WRITE'}">
                            {{mfr.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{mfr.sharedBy}}</td>
                    <td style="width: 100px">{{mfr.sharedOn}}</td>
                </tr>
                </tbody>
            </table>


        </div>
        <div class="table-footer">
            <table-footer objects="externalUserVm.sharedMfrs" pageable="externalUserVm.pageable"
                          previous-page="externalUserVm.previousPage" next-page="externalUserVm.nextPage"
                          page-size="externalUserVm.pageSize"></table-footer>
        </div>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="externalUserVm.mfrPartView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 200px;" translate>PART_NUMBER</th>
                    <th style="width: 200px;" translate>MANUFACTURER</th>
                    <th style="width: 200px;" translate>TYPE</th>
                    <th class="col-width-250" translate>PART_NAME</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th translate="ITEM_ALL_LIFECYCLE"></th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
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

                <tr ng-if="externalUserVm.loading == false && externalUserVm.sharedCounts.mfrPart == 0">
                    <td colspan="25" translate>NO_SHARING_MFR_PARTS</td>
                </tr>

                <tr ng-repeat="mfrPart in externalUserVm.sharedMfrParts.content">
                    <td style="width: 150px">
                        <a href="" ng-click="externalUserVm.showMfrPartDetails(mfrPart)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                <span
                                        ng-bind-html="mfrPart.number  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>
                    <td ng-bind-html="mfrPart.mfr  | highlightText: externalUserVm.sharedObjectFilter.searchQuery">
                    </td>
                    <td class="col-width-150">
                            <span
                                    ng-bind-html="mfrPart.type  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-200">
                            <span
                                    ng-bind-html="mfrPart.name  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-250">
                            <span
                                    ng-bind-html="mfrPart.description  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td>
                        <item-status item="mfrPart"></item-status>
                    </td>
                    <td style="width: 100px; text-align: center;">{{mfrPart.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': mfrPart.permission === 'READ','label-success': mfrPart.permission === 'WRITE'}">
                            {{mfrPart.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{mfrPart.sharedBy}}</td>
                    <td style="width: 100px">{{mfrPart.sharedOn}}</td>
                </tr>
                </tbody>
            </table>


        </div>
        <div class="table-footer">
            <table-footer objects="externalUserVm.sharedMfrParts" pageable="externalUserVm.pageable"
                          previous-page="externalUserVm.previousPage" next-page="externalUserVm.nextPage"
                          page-size="externalUserVm.pageSize"></table-footer>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="externalUserVm.programView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 200px;" translate>PROGRAM_NAME</th>
                    <th style="width: 200px;" translate>TYPE</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th class="col-width-250" translate>PROGRAM_MANAGER</th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
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

                <tr ng-if="externalUserVm.loading == false && externalUserVm.sharedPrograms.content.length == 0">
                    <td colspan="25" translate>NO_SHARING_PROGRAMS</td>
                </tr>

                <tr ng-repeat="program in externalUserVm.sharedPrograms.content">
                    <td style="width: 150px">
                        <a href="" ng-click="externalUserVm.showProgramDetails(program)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                <span
                                        ng-bind-html="program.name  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                            <span
                                    ng-bind-html="program.type  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-250">
                            <span
                                    ng-bind-html="program.description  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-200">
                        <span
                                ng-bind-html="program.manager  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td style="width: 100px; text-align: center;">{{program.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': program.permission === 'READ','label-success': program.permission === 'WRITE'}">
                            {{program.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{program.sharedBy}}</td>
                    <td style="width: 100px">{{program.sharedOn}}</td>
                </tr>
                </tbody>
            </table>


        </div>
        <div class="table-footer">
            <table-footer objects="externalUserVm.sharedPrograms" pageable="externalUserVm.pageable"
                          previous-page="externalUserVm.previousPage" next-page="externalUserVm.nextPage"
                          page-size="externalUserVm.pageSize"></table-footer>
        </div>
    </div>


    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="externalUserVm.supplierView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>


                    <th class="col-width-150" translate>NAME</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th class="col-width-150" translate>LIFECYCLE</th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
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

                <tr ng-if="externalUserVm.loading == false && externalUserVm.sharedCounts.supplier == 0">
                    <td colspan="25" translate>NO_SHARING_SUPPLIERS</td>
                </tr>

                <tr ng-repeat="supplier in externalUserVm.sharedSuppliers.content">
                    <td style="width: 150px">
                        <a href="" ng-click="externalUserVm.showSupplierDetails(supplier)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                <span
                                        ng-bind-html="supplier.name  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>

                    <td class="col-width-150">
                            <span
                                    ng-bind-html="supplier.type  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>

                    <td class="col-width-250">
                            <span
                                    ng-bind-html="supplier.description  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td>
                        <item-status item="supplier"></item-status>
                    </td>
                    <td style="width: 100px; text-align: center;">{{supplier.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': supplier.permission === 'READ','label-success': supplier.permission === 'WRITE'}">
                            {{supplier.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{supplier.sharedBy}}</td>
                    <td style="width: 100px">{{supplier.sharedOn}}</td>
                </tr>
                </tbody>
            </table>


        </div>
        <div class="table-footer">
            <table-footer objects="externalUserVm.sharedSuppliers" pageable="externalUserVm.pageable"
                          previous-page="externalUserVm.previousPage" next-page="externalUserVm.nextPage"
                          page-size="externalUserVm.pageSize"></table-footer>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;"
         ng-show="externalUserVm.customObjectView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>


                    <th class="col-width-150" translate>NAME</th>
                    <th class="col-width-150" translate>TYPE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
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

                <tr ng-if="externalUserVm.loading == false && externalUserVm.sharedCounts.customObject == 0">
                    <td colspan="25" translate>NO_SHARING_CUSTOM_OBJECTS</td>
                </tr>

                <tr ng-repeat="supplier in externalUserVm.sharedCustomObjects.content">
                    <td style="width: 150px">
                        <a href="" ng-click="externalUserVm.showCustomObjectDetails(supplier)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                <span
                                        ng-bind-html="supplier.name  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>

                    <td class="col-width-150">
                            <span
                                    ng-bind-html="supplier.type  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>

                    <td class="col-width-250">
                            <span
                                    ng-bind-html="supplier.description  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>

                    <td style="width: 100px; text-align: center;">{{supplier.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': supplier.permission === 'READ','label-success': supplier.permission === 'WRITE'}">
                            {{supplier.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{supplier.sharedBy}}</td>
                    <td style="width: 100px">{{supplier.sharedOn}}</td>
                </tr>
                </tbody>
            </table>


        </div>
        <div class="table-footer">
            <table-footer objects="externalUserVm.sharedSuppliers" pageable="externalUserVm.pageable"
                          previous-page="externalUserVm.previousPage" next-page="externalUserVm.nextPage"
                          page-size="externalUserVm.pageSize"></table-footer>
        </div>
    </div>


    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;" ng-show="externalUserVm.folderView">
        <div class="responsive-table" style="padding: 10px;">

            <table id="itemFilesTable" class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th class="col-width-250 file-name sticky-col sticky-actions-col" style="z-index: 11;" translate>
                        TAB_FILES_FILE_NAME
                    </th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th style="text-align: center" translate>TAB_FILES_VERSION</th>
                    <th style="text-align: center"
                        translate>LIFECYCLE
                    </th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
                    <th style="width: 100px" translate="SHARED_BY"></th>
                    <th style="width: 100px" translate="SHARED_ON"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="loading == true">
                    <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                          class="mr5"><span translate>LOADING_FILES</span>
                    </td>
                </tr>

                <tr id="{{$index}}" ng-show="externalUserVm.sharedFolders.content.length > 0"
                    ng-repeat="file in externalUserVm.sharedFolders.content">

                    <td class="file-name sticky-col sticky-actions-col" style="z-index: 12">
                    <span class="level{{file.level}}" ng-show="file.fileDto.fileType == 'FOLDER'"
                          ng-click="toggleNode(file)">
                        <i class="mr5 fa" title="{{ExpandCollapse}}"
                           style="cursor: pointer !important; font-size: 18px;color: limegreen !important;"
                           ng-class="{'fa-folder': (file.expanded == false || file.expanded == null || file.expanded == undefined),
                           'fa-folder-open': file.expanded == true}">
                        </i>
                        <span class="autoClick" title="{{file.fileDto.name}}"
                              style="cursor: pointer !important;white-space: normal;word-wrap: break-word;"
                              ng-bind-html="file.fileDto.name">
                        </span>

                    </span>

                    <span class="imageTooltip level{{file.level}}" ng-show="file.fileDto.fileType == 'FILE'">
                                <a href="" ng-click="downloadFile(file.fileDto)"
                                   title="{{downloadFileTitle}}">
                                    <span ng-if="file.fileDto.fileType == 'FILE'"
                                          ng-bind-html="file.fileDto.name"></span>
                                </a>

                    </span>
                    </td>
                    <td class="description-column">
                        <span ng-if="file.fileDto.fileType == 'FILE'">{{file.fileDto.description}}</span>
                        <span ng-if="file.fileDto.fileType == 'FOLDER'">{{file.fileDto.description}}</span>
                    </td>


                    <td style="text-align: center">
                        <span ng-if="file.fileDto.fileType == 'FILE'">
                            <span>{{file.fileDto.revision}}.{{file.fileDto.version}}
                            </span>
                        </span>
                    </td>
                    <td style="text-align: center">
                        <span ng-if="file.fileDto.fileType == 'FILE'">
                           <item-status
                                   item="file.fileDto"></item-status>
                            </span>
                        </span>
                    </td>
                    <td style="width: 100px; text-align: center;">{{file.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': file.permission === 'READ','label-success': file.permission === 'WRITE'}">
                            {{file.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{file.sharedBy}}</td>
                    <td style="width: 100px">{{file.sharedOn}}</td>
                </tr>
                </tbody>
            </table>


        </div>
        <div class="table-footer">
            <table-footer objects="externalUserVm.sharedFolders" pageable="externalUserVm.pageable"
                          previous-page="externalUserVm.previousPage" next-page="externalUserVm.nextPage"
                          page-size="externalUserVm.pageSize"></table-footer>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;"
         ng-show="externalUserVm.declarationView">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 200px;" translate>NUMBER</th>
                    <th style="width: 200px;" translate>TYPE</th>
                    <th class="col-width-250" translate>NAME</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th style="width: 100px;" translate>STATUS</th>
                    <th class="description-column" translate>SUPPLIER</th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_SHARED_TYPE"></th>
                    <th style="width: 150px;" translate="ITEM_ALL_PERMISSION"></th>
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
                                <span translate>LOADING_DECLARATIONS</span>
                            </span>
                    </td>
                </tr>

                <tr ng-if="externalUserVm.loading == false && externalUserVm.sharedCounts.declaration == 0">
                    <td colspan="25" translate>NO_SHARING_DECLARATIONS</td>
                </tr>

                <tr ng-repeat="declaration in externalUserVm.sharedDeclarations.content">
                    <td style="width: 150px">
                        <a href="" ng-click="externalUserVm.showDeclaration(declaration)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                                <span
                                        ng-bind-html="declaration.number  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">
                            <span
                                    ng-bind-html="declaration.type  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-200">
                            <span
                                    ng-bind-html="declaration.name  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td class="col-width-250"><span
                            ng-bind-html="declaration.description  | highlightText: externalUserVm.sharedObjectFilter.searchQuery"></span>
                    </td>
                    <td>
                        <declaration-status object="declaration"></declaration-status>
                    </td>
                    <td>{{declaration.supplier}}</td>
                    <td style="width: 100px; text-align: center;">{{declaration.shareType}}</td>
                    <td>
                        <div class="label" style="width: 100px; text-align: center;"
                             ng-class="{'label-info': declaration.permission === 'READ','label-success': declaration.permission === 'WRITE'}">
                            {{declaration.permission}}
                        </div>
                    </td>
                    <td style="width: 100px">{{declaration.sharedBy}}</td>
                    <td style="width: 100px">{{declaration.sharedOn}}</td>
                </tr>
                </tbody>
            </table>

        </div>
        <div class="table-footer">
            <table-footer objects="externalUserVm.sharedDeclarations" pageable="externalUserVm.pageable"
                          previous-page="externalUserVm.previousPage" next-page="externalUserVm.nextPage"
                          page-size="externalUserVm.pageSize"></table-footer>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow: hidden;" ng-show="externalUserVm.sharedProjectView">
        <div class="row row-eq-height">
            <div class="col-sm-12" style="padding: 0px;">
                <div class="item-details-tabs">
                    <uib-tabset active="externalUserVm.active">
                        <uib-tab id="sharedProject" heading="{{externalUserVm.projectHeading}}"
                                 active="externalUserVm.tabs.project.active"
                                 ng-show="externalUserVm.sharedCounts.project > 0"
                                 select="externalUserVm.selectTab(externalUserVm.tabs.project.id)">
                            <div ng-include="externalUserVm.tabs.project.template"></div>
                        </uib-tab>
                        <uib-tab id="sharedActivity" heading="{{externalUserVm.activityHeading}}"
                                 active="externalUserVm.tabs.activity.active"
                                 ng-show="externalUserVm.sharedCounts.activity > 0"
                                 select="externalUserVm.selectTab(externalUserVm.tabs.activity.id)">
                            <div ng-include="externalUserVm.tabs.activity.template"></div>
                        </uib-tab>
                        <uib-tab id="sharedTask" heading="{{externalUserVm.taskHeading}}"
                                 active="externalUserVm.tabs.task.active" ng-show="externalUserVm.sharedCounts.task > 0"
                                 select="externalUserVm.selectTab(externalUserVm.tabs.task.id)">
                            <div ng-include="externalUserVm.tabs.task.template"></div>
                        </uib-tab>
                    </uib-tabset>
                </div>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" ng-show="externalUserVm.sharedFoldersView == true">

        <style scoped>

            #classificationContainer .search-input i.fa-search {
                position: absolute;
                margin-top: 13px;
                margin-left: 10px;
                color: grey;
                opacity: 0.5;
                font-size: 12px;
            }

            #classificationContainer .search-input .search-form {
                padding-left: 25px;
                padding-right: 25px;
            }

            .search-form {
                border-radius: 3px;
                background-color: #eaeaea;
                border: 1px solid #fff;
            }

            .search-form:focus {
                box-shadow: none;
                border: 1px solid #c5cfd5;
            }

            #classificationContainer .search-input .search-form {
                padding-left: 25px;
                padding-right: 25px;
            }

            i.clear-search {
                margin-left: 205px;
                color: #aab4b7;
                cursor: pointer;
                z-index: 4 !important;
                position: absolute;
                margin-top: -26px;
            }

            .sticky-col {
                position: sticky !important;
                position: -webkit-sticky !important;
            }

            .sticky-actions-col {
                right: -5px;
            }

        </style>

        <div id="foldersContextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
        </div>
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane"
                 style="padding: 0;min-width: 250px;max-width: 250px;overflow: auto;">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <div class="search-input" style="height: 30px;margin: 10px 10px 20px 10px;width: 230px;">
                        <i class="fa fa-search"></i>
                        <input type="search" class="form-control input-sm search-form"
                               ng-model="externalUserVm.searchValue"
                               ng-change="externalUserVm.searchTree()">
                        <i class="las la-times-circle clear-search"
                           ng-show="externalUserVm.searchValue.length > 0"
                           ng-click="externalUserVm.searchValue = '';externalUserVm.searchTree()"></i>
                    </div>
                    <ul id="documentFolderTree" class="easyui-tree classification-tree">
                    </ul>
                    <ul id="sharedFolderTree" class="easyui-tree classification-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider" style="left: 250px;z-index: 1;">
            </div>
            <div class="split-pane-component split-right-pane noselect"
                 style="left:250px;padding: 0;overflow-y: hidden;">


                <div class="responsive-table">

                    <table id="externalFilesTable" class="table table-striped highlight-row">
                        <thead>
                        <tr>
                            <th></th>
                            <th class="col-width-250 file-name sticky-col sticky-actions-col" style="z-index: 99;"
                                translate>
                                TAB_FILES_FILE_NAME
                            </th>
                            <th translate>TAB_FILES_FILE_SIZE</th>
                            <th class="description-column" translate>DESCRIPTION</th>
                            <th style="text-align: center" translate>TAB_FILES_VERSION</th>
                            <th class="description-column"
                                ng-if="folderObject.objectType == 'FILE' || selectedType=='FILE'" translate>Object
                                Name
                            </th>
                            <th style="text-align: center"
                                ng-if="folderObject.objectType == 'FILE' || selectedType=='FILE'" translate>Object
                                Type
                            </th>
                            <th style="text-align: center" ng-if="folderObject.objectType=='DOCUMENT'" translate>
                                LIFECYCLE
                            </th>
                            <th translate>CREATED_DATE</th>
                            <th translate>CREATED_BY</th>
                            <th translate>MODIFIED_DATE</th>
                            <th translate>MODIFIED_BY</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="externalUserVm.loading == true">
                            <td colspan="10"><img
                                    src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_FILES</span>
                            </td>
                        </tr>

                        <tr ng-if="externalUserVm.sharedExternalUserFolders.length == 0">
                            <td colspan="10" style="background-color: #f9fbfe  !important;color: unset !important;">
                                <div class="no-data">
                                    <img src="app/assets/no_data_images/Files.png" alt="" class="image">

                                    <div class="message">{{
                                        'NO_FILES' | translate}}
                                    </div>
                                </div>
                            </td>
                        </tr>

                        <tr ng-show="externalUserVm.sharedExternalUserFolders.length > 0"
                            ng-repeat="file in externalUserVm.sharedExternalUserFolders">
                            <td style="width: 20px;">
                                <span style="text-align: center;width: 20px;"
                                      ng-show="file.fileType != 'FOLDER'"
                                      ng-click="filePreview(file)" title="Preview File">
                                    <i class="{{getFileIcon(file.name)}}"
                                       ng-style="{{getFileIconColor(file.name)}}"></i>
                                </span>
                            </td>
                            <td class="file-name sticky-col sticky-actions-col" style="z-index: 12">
                                <a href="" ng-click="externalUserVm.sharedFileDownload(file)"
                                   title="{{downloadFileTitle}}">
                                    <span
                                            ng-bind-html="file.name | highlightText: searchText"></span>
                                </a>

                            </td>
                            <td>
                                <span
                                        style="cursor: move">{{file.size.toFileSize()}}</span>
                            </td>
                            <td class="description-column">
                                <span>{{file.description}}</span>
                            </td>
                            <td style="text-align: center">
                                <span ng-if="folderObject.objectType=='FILE' || selectedType=='FILE'">{{file.version}}</span>
                                <span ng-if="folderObject.objectType=='DOCUMENT'">{{file.revision}}.{{file.version}}</span>
                            </td>
                            <td ng-if="folderObject.objectType=='FILE' || selectedType=='FILE'"
                                class="description-column">
                                <span>{{file.parentObjectName}}</span>
                            </td>
                            <td ng-if="folderObject.objectType=='FILE' || selectedType=='FILE'"
                                style="text-align: center">
                                <object-type-status object="file"></object-type-status>
                            </td>


                            <td ng-if="folderObject.objectType=='DOCUMENT'" style="text-align: center">
                                <item-status item="file"></item-status>
                            </td>

                            <td>{{file.createdDate}}</td>
                            <td>{{file.createdByName}}</td>
                            <td>{{file.modifiedDate}}</td>
                            <td>{{file.modifiedByName}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>


            </div>
        </div>
    </div>


</div>