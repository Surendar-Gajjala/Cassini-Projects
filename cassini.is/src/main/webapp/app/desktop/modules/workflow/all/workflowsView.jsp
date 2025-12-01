<style>
    #freeTextSearchDirective {
        top: 7px !important;
    }

    .view-content {
        position: relative;
    }

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
        background-color: #fff;
    }

    .view-content .table-footer {
        padding: 0 10px 0 10px;
        position: absolute;
        bottom: 0;
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
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-size: 20px;font-weight: bold;color: black;padding: 13px 10px 14px;vertical-align: middle;border-right: 1px solid lightgray;">{{viewInfo.title}}</span>

        <div class="btn-group">
            <button class='btn btn-sm btn-success' ng-if="hasPermission('permission.workflow.new')"
                    ng-click='workflowsVm.newWorkflow()'>New Workflow
            </button>
        </div>
        <%-- <p class="blink"
            ng-show="searchModeType == true;"
            style="color: #0390fd;margin-top: -28px;margin-left: 700px; font-size: 16px;" translate>ALL_VIEW_ALERT
         </p>--%>

        <free-text-search on-clear="workflowsVm.resetPage" on-search="workflowsVm.freeTextSearch"></free-text-search>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class='responsive-table' style="padding: 10px;">
            <table class='table table-striped highlight-row'>
                <thead>
                <tr>
                    <th class="name-column">Name</th>
                    <th class="description-column">Description</th>
                    <th style="width: 150px;">Created Date</th>
                    <th style="width: 150px;">Created By</th>
                    <th style="width: 150px;">Modified Date</th>
                    <th style="width: 150px;">Modified By</th>
                    <th style="width: 150px;">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="workflowsVm.loading == true">
                    <td colspan="6">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Workflows..
                        </span>
                    </td>
                </tr>

                <tr ng-if="workflowsVm.loading == false && workflowsVm.workflows.content.length == 0">
                    <td colspan="10">No Workflows are available to view</td>
                </tr>
                <tr ng-repeat="workflow in workflowsVm.workflows.content">
                    <td class="name-column">
                        <a href="" ng-click="workflowsVm.editWorkflow(workflow)"
                           ng-if="hasPermission('permission.workflow.view')"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="workflow.name | highlightText: freeTextQuery"></span>
                        </a>
                        <span ng-if="!hasPermission('permission.workflow.view')">
                             <span ng-bind-html="workflow.name | highlightText: freeTextQuery"></span>
                        </span>
                    </td>
                    <td class="description-column"><span
                            ng-bind-html="workflow.description | highlightText: freeTextQuery"></span></td>
                    <td>{{workflow.createdDate}}</td>
                    <td>{{workflow.createdByObject.firstName}}</td>
                    <td>{{workflow.modifiedDate}}</td>
                    <td>{{workflow.modifiedByObject.firstName}}</td>
                    <td>
                     <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <%--  <li ng-if="hasPermission('permission.workflow.edit')"
                                      ng-click='workflowsVm.showSaveAsPanel(workflow)'>
                                      <a>Copy Workflow</a>
                                  </li>--%>
                                <li ng-if="hasPermission('permission.workflow.delete')"
                                    ng-click='workflowsVm.deleteWorkflow(workflow)'>
                                    <a>Delete Workflow</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <div>
                <div>
                    <h5><span style="padding-right: 5px">Displaying</span>
                        <span ng-if="workflowsVm.workflows.totalElements ==0">
                            {{(workflowsVm.pageable.page*workflowsVm.pageable.size)}}
                        </span>
                        <span ng-if="workflowsVm.workflows.totalElements > 0">
                            {{(workflowsVm.pageable.page*workflowsVm.pageable.size)+1}}
                        </span>
                        -
                        <span ng-if="workflowsVm.workflows.last ==false">{{((workflowsVm.pageable.page+1)*workflowsVm.pageable.size)}}</span>
                        <span ng-if="workflowsVm.workflows.last == true">{{workflowsVm.workflows.totalElements}}</span>
                        <span translate>of</span>
                        {{workflowsVm.workflows.totalElements}}
                    </h5>
                </div>

                <div class="text-right">
                    <span class="mr10"><span>Page</span> {{workflowsVm.workflows.totalElements != 0 ? workflowsVm.workflows.number+1:0}} <span
                            >of</span> {{workflowsVm.workflows.totalPages}}</span>
                    <a href="" ng-click="workflowsVm.previousPage()"
                       ng-class="{'disabled': workflowsVm.workflows.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="workflowsVm.nextPage()"
                       ng-class="{'disabled': workflowsVm.workflows.last}"><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>