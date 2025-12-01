<style>

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

    #freeTextSearchDirective {
        top: 7px !important;
    }

    .sticky-col {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-col {
        right: -10px !important;
    }

    .table-striped > tbody > tr:nth-child(2n) > td.actions-col {
        /*background-color: #fff;*/
    }

    .table-striped > tbody > tr:nth-child(2n):hover > td.sticky-col {
        background-color: #d6e1e0;
    }

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>WORKFLOW_ALL_TEMPLATE_TITLE</span>

        <button class="btn btn-sm new-button" ng-click='workflowsVm.newWorkflow()'
                title="{{'WORKFLOW_NEW' | translate}}"
                ng-if="hasPermission('plmworkflowdefinition','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{'NEW' | translate}} {{'WORKFLOW' | translate }}</span>
        </button>
        <button class="btn btn-sm btn-default"
                style="" title="{{preferredPage}}"
                ng-click="savePreferredPage()">
            <i class="fa fa fa-anchor" style=""></i>
        </button>

        <free-text-search on-clear="workflowsVm.resetPage" on-search="workflowsVm.freeTextSearch"></free-text-search>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class='responsive-table' style="padding: 10px;">
            <table class='table table-striped highlight-row'>
                <thead>
                <tr>
                    <th style="width: 100px" translate>Number</th>
                    <th class="col-width-250" translate>WORKFLOW_ALL_NAME</th>
                    <th style="width: 150px;" translate>WORKFLOW_TYPE</th>
                    <th class="description-column" translate>DESCRIPTION</th>
                    <th style="width: 150px" translate>Lifecycle</th>
                    <th style="width: 50px;text-align: center" translate>Revision</th>
                    <th style="width: 10px;text-align: center;" translate>INSTANCE</th>
                    <th style="width: 150px;" translate>CREATED_DATE</th>
                    <th style="width: 150px;" translate>CREATED_BY</th>
                    <th style="width: 150px;" translate>MODIFIED_DATE</th>
                    <th style="width: 150px;" translate>MODIFIED_BY</th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="workflowsVm.loading == true">
                    <td colspan="12">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5" translate>
                            <span translate>LOADING_WORKFLOW</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="workflowsVm.loading == false && workflowsVm.workflows.content.length == 0">
                    <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Workflow.png" alt="" class="image">

                            <div class="message">{{ 'MO_WORKFLOW' | translate}}</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>NO_PERMISSION_MESSAGE</div>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="workflow in workflowsVm.workflows.content">
                    <td>
                        <a href="" ng-click="workflowsVm.editWorkflow(workflow)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="workflow.number | highlightText: freeTextQuery"></span>
                        </a>
                        <%--<span ng-if="!hasPermission('plmworkflow','view')">--%>
                             <%--<span ng-bind-html="workflow.number | highlightText: freeTextQuery"></span>--%>
                        <%--</span>--%>
                    </td>
                    <td class="col-width-250">
                        <span ng-bind-html="workflow.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td><span ng-bind-html="workflow.type | highlightText: freeTextQuery"></span></td>
                    <td class="description-column">
                        <span title="{{workflow.description}}"><span
                                ng-bind-html="workflow.description | highlightText: freeTextQuery"></span>
                     </span>
                    </td>
                    <td class="column-width-100">
                        <item-status item="workflow"></item-status>
                    </td>

                    <td style="width: 100px;text-align: center">
                        <a href="" ng-click="workflowsVm.showWorkflowRevisionHistory(workflow)"
                           title="{{'ITEM_DETAILS_REVISION_HISTORY' | translate}}">
                            {{workflow.revision}}
                        </a>
                    </td>


                    <td style="text-align: center;"><a href="" title="{{workflowInstanceTitle}}"
                                                       ng-click="workflowsVm.showInstances(workflow)">{{workflow.instances}}</a>
                    </td>
                    <td>{{workflow.createdDate}}</td>
                    <td>{{workflow.createdBy}}</td>
                    <td>{{workflow.modifiedDate}}</td>
                    <td>{{workflow.modifiedBy}}</td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                     <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-if="hasPermission('plmworkflowdefinition','edit')"
                                    ng-click='workflowsVm.showSaveAsPanel(workflow)'>
                                    <a translate>COPY_WORKFLOW</a>
                                </li>
                                <li ng-class="{'disabled': workflow.instances >0}"
                                    ng-click='workflowsVm.deleteWorkflow(workflow)'>
                                    <a translate>DELETE_WORKFLOW</a>
                                </li>
                                <plugin-table-actions context="workflow.all" object-value="workflow"></plugin-table-actions>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="workflowsVm.workflows" pageable="workflowsVm.pageable"
                          previous-page="workflowsVm.previousPage"
                          next-page="workflowsVm.nextPage" page-size="workflowsVm.pageSize"></table-footer>
        </div>
    </div>
</div>