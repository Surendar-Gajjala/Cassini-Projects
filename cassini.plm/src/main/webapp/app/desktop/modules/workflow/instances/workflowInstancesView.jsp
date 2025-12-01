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

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

    .wf-props-panel {
        border-left: 1px dotted #D3D3D3;
        padding: 10px;
    }

    .djs-group {
        cursor: pointer !important;
    }

    .djs-element.inprocess-status .djs-visual rect {
        fill: #60b6ff !important;
    }

    .djs-element.inprocess-status .djs-visual circle {
        fill: #60b6ff !important;
    }

    .djs-element.onhold-status .djs-visual rect {
        fill: #efc86a !important;
    }

    .djs-element.onhold-status .djs-visual circle {
        fill: #efc86a !important;
    }

    .djs-element.terminated-status .djs-visual rect {
        fill: #ee6965 !important;
    }

    .djs-element.terminated-status .djs-visual circle:nth-child(2) {
        fill: #ee6965 !important;
    }

    .djs-element.completed-status .djs-visual rect {
        fill: #3AD0BA !important;
    }

    .djs-element.completed-status .djs-visual circle {
        fill: #3AD0BA !important;
    }

    .wf-instances .wf-types-filter {
        background-color: var(--cassini-form-contrast-color);
        padding: 1px 5px;
        margin-left: 5px;
        border: 0;
    }

    .wf-instances th .page-size-dropdown {
        margin-top: 6px;
    }


</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>WORKFLOW_ALL_INSTANCE_TITLE</span>

        <button class="btn btn-sm btn-default"
                style="" title="{{preferredPage}}"
                ng-click="savePreferredPage()">
            <i class="fa fa fa-anchor" style=""></i>
        </button>

        <free-text-search on-clear="workflowInstancesVm.resetPage" search-term="workflowInstancesVm.searchText"
                          on-search="workflowInstancesVm.freeTextSearch"></free-text-search>

    </div>

    <div class="view-content no-padding wf-instances" style="overflow-y: auto;padding: 10px;">
        <div class='responsive-table' style="padding: 10px;">
            <table class='table table-striped highlight-row'>
                <thead>
                <tr>
                    <th style="width: 100px" translate>NUMBER</th>
                    <th style="width: 200px" translate>NAME</th>
                    <th style="width: 100px;text-align: center" translate>REVISION</th>
                    <th style="width: 150px;z-index: auto !important">
                        <span translate>OBJECT_TYPE</span>

                        <div class="dropdown" uib-dropdown
                             style="display: inline-block">
                            <button uib-dropdown-toggle class="btn btn-sm btn-default dropdown-toggle wf-types-filter"
                                    aria-haspopup="true" aria-expanded="false"
                                    style="">{{selecteObject}}
                                <i class="caret" style="margin-left: 5px"></i>
                            </button>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="height:200px; overflow: auto">
                                <li ng-repeat="object in objectTypes"
                                    ng-click="workflowInstancesVm.objectFilter(object)"><a href="">{{object}}</a></li>
                            </ul>
                        </div>


                    </th>
                    <th style="width: 150px" translate>OBJECT_NUMBER</th>
                    <th style="width: 250px;" translate>OBJECT_NAME</th>
                    <th style="width: 150px;" translate>STARTED_ON</th>
                    <th style="width: 150px;" translate>FINISHED_ON</th>
                    <th style="width: 150px;" translate>CANCELLED_ON</th>
                    <th style="width: 150px;" translate>CURRENT_ACTIVITY</th>
                    <th style="width: 150px;" translate>PREVIOUS_ACTIVITY</th>
                    <th style="width: 150px;" translate>TIME_STAMP</th>
                    <th style="width: 150px;" translate>HOLD_BY</th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>VIEW
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="workflowInstancesVm.loading == true">
                    <td colspan="6">
                        <span style="font-size: 20px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5" translate>LOADING_WORKFLOW
                        </span>
                    </td>
                </tr>

                <tr ng-if="workflowInstancesVm.loading == false && workflowInstancesVm.workflowInstances.content.length == 0">
                    <td colspan="15" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/Workflow.png" alt="" class="image">

                            <div class="message">{{ 'NO_WORKFLOW_INSTANCES' | translate}}</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="workflow in workflowInstancesVm.workflowInstances.content | orderBy: '-timeStamp'">
                    <td style="width: 100px" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                        <a href="" ng-click="workflowInstancesVm.gotoWorkflow(workflow.workflow)">
                            <span ng-bind-html="workflow.workflow.workflowRevisionObject.master.number"></span></a>
                    </td>
                    <td style="width: 200px;">
                        <span ng-bind-html="workflow.workflow.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="width: 100px;text-align: center">
                        {{workflow.workflow.workflowRevisionObject.revision}}
                    </td>
                    <td style="width: 100px">
                        <span ng-bind-html="workflow.type | highlightText: freeTextQuery"></span>
                    </td>
                    <td>
                        <a href="" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="workflowInstancesVm.showInstanceDetails(workflow)">{{workflow.number}}</a>
                        <span ng-if="workflow.revision"> - {{workflow.revision}}</span>
                    </td>
                    <td class="col-width-200" title="{{workflow.description}}">
                        <a href="" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="workflowInstancesVm.showInstanceDetails(workflow)">{{workflow.description}}</a>
                    </td>

                    <td>{{workflow.workflow.startedOn}}</td>
                    <td>{{workflow.workflow.finishedOn}}</td>
                    <td>{{workflow.workflow.cancelledOn}}</td>
                    <td style="width: 100px">
                        <workflow-status workflow="workflow"></workflow-status>
                        <span class="badge badge-warning bg-light-warning" ng-if="workflow.workflow.onhold">HOLD</span>
                    </td>
                    <td style="width: 100px">
                        <span class="label label-success label-outline bg-light-success"
                              style="text-transform: uppercase">{{workflow.previousStatus}}</span>
                    </td>
                    <td style="width: 100px">{{workflow.timeStamp}}</td>
                    <td>{{workflow.holdBy}}</td>
                    <td style="width: 50px; text-align: center" class="actions-col sticky-col sticky-actions-col"
                        ng-click="workflowInstancesVm.showWorkflow(workflow)"
                        title="{{wfHistory}}">
                        <i style="color: #337ab7;font-size: 18px;font-weight: 900"
                           class="fa flaticon-plan2 nav-icon-font"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="workflowInstancesVm.workflowInstances" pageable="workflowInstancesVm.pageable"
                          previous-page="workflowInstancesVm.previousPage"
                          next-page="workflowInstancesVm.nextPage"
                          page-size="workflowInstancesVm.pageSize"></table-footer>
        </div>
    </div>
</div>


<div>
    <style scoped>
        .workflow-model.modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            padding-top: 20px; /* Location of the box */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0, 0, 0); /* Fallback color */
            background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
        }

        .workflow-model .workflowRollup-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .workflow-header {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        .configuration-header {
            font-weight: bold;
            font-size: 22px;
            /*position: absolute;*/
            display: inline-block;
            /*left: 44%;*/
            margin-top: 7px;
        }

        .workflow-content {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
        }

        .config-close {
            position: absolute;
            right: 35px;
            top: 25px;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .config-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .config-close:before, .config-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .config-close:before {
            transform: rotate(45deg) !important;
        }

        .config-close:after {
            transform: rotate(-45deg) !important;
        }

        #canvas .djs-shape .djs-hit {
            pointer-events: none !important;
        }

        .wf-row {
            display: flex;
            height: 100%;
        }

        .wf-row .workflow-viewer {
            width: calc(100% - 500px)
        }

        .wf-row .workflow-history {
            min-width: 500px;
            height: 100%;
        }

        /* ================ The Timeline ================ */

        .direction-l {
            position: static !important;
            float: none !important;
            margin: -0 30px 0px 99px !important;
        }

        .direction-r {
            position: static !important;
            float: none !important;
            margin: -0 30px 0px 99px !important;
        }

    </style>
    <div id="workflow-rollup" class="workflow-model modal">
        <div class="workflowRollup-content">
            <div class="workflow-header">
                <span class="configuration-header">
                    {{workflowInstancesVm.number}} <span ng-if="workflowInstancesVm.revision != null">({{workflowInstancesVm.revision}})</span> - {{workflowInstancesVm.workflowName}}
                </span>
                <a href="" ng-click="hideWorkflowPreview()" class="config-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="workflow-content" style="padding: 0">
                <div class="wf-row">
                    <div id="canvas" class="workflow-viewer" style="width: 100%; padding: 18px;"></div>
                    <div class="workflow-history" ng-if="workflowInstancesVm.workflow != null"
                         ng-include="'app/desktop/modules/workflow/history/workflowHistoryView.jsp'"
                         ng-controller="InstancesWorkflowHistoryController as wfHistoryVm"></div>
                </div>
            </div>
        </div>
    </div>
</div>