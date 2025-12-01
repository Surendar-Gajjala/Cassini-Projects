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

    .sticky-col {
        position: sticky !important;
        position: -webkit-sticky !important;
    }

    .sticky-actions-col {
        right: -10px !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
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

    .attributeTooltip:hover .attributeTooltiptext {
        visibility: visible;
        opacity: 1;
    }

    .gantt_row_project {
        font-weight: bold;
    }

    .gantt-info ul {
        line-height: 150%;
    }

    .gantt_row_placeholder i.action-btn {
        display: none;
        cursor: pointer !important;
    }

    .gantt_row_project .gantt_cell {
        font-weight: bolder !important;
    }

    .gant-editor-frame {
        width: 100%;
        height: 100%;
    }

    .action-btn {
        color: #a1a4a5;
        margin-right: 5px;
        cursor: pointer;
    }

    .gantt_task_progress {
        text-align: left !important;
        padding-left: 10px !important;
    }

    .gantt_task_progress_drag {
        display: none !important;
    }

    .gantt_grid_data .gantt_row {
        background-color: #f9fbfe !important;
    }

    .gantt_task_row {
        background-color: #f9fbfe !important;
    }

    .gantt_grid_data {
        background-color: #f9fbfe !important;
    }

    .gantt_data_area {
        background-color: #f9fbfe !important;
    }

    .gantt_grid_scale {
        background-color: #f9fbfe !important;
    }

    .gantt_task_scale {
        background-color: #f9fbfe !important;
    }

    .gantt_grid_data .gantt_row_placeholder {
        border-bottom: 1px solid #d7d7d7 !important;
    }

    .gantt_task_progress {
        background-color: rgb(54, 54, 54);
        opacity: 0.9;
    }

    .nested_task .gantt_add {
        display: none !important;
    }

    .hide_icon #icon {
        display: none !important;
    }

    .gantt_grid_data .gantt_row.gantt_selected {
        background-color: #fff3a1 !important;
    }

    .gantt_tooltip {
        background-color: rgb(248, 228, 147);
        border-radius: 5px;
        z-index: 9999;
    }

    .gantt_grid_head_object_type {
        text-align: left;
        margin-left: 5px;
    }

    .gantt_task_cell.week_end {
        background-color: #EFF5FD !important;
    }

    .gantt_layout_cell {
        padding: 1px;
    }

    .project-meetings.fc th {
        line-height: 40px !important;
        background-color: #F3F3F3 !important;
    }

    .project-meetings.fc .fc-other-month.fc-future, .fc-other-month.fc-past {
        background-color: #FBFBFB;
    }

    .fc-day-grid-event .fc-time {
        display: none;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>PRODUCTION_ORDER_TITLE</span>

        <button class="btn btn-sm new-button" ng-click="allProductionOrderVm.newProductionOrder()"
                title="{{newProductionOrder}}" ng-if="hasPermission('productionorder','create')">
            <i class="las la-plus" aria-hidden="true"></i>
            <span>{{newProductionOrder}}</span>
        </button>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-if="allProductionOrderVm.ganttEnable==false"
                    ng-click="allProductionOrderVm.showTypeAttributes()"
                    title="{{'ALL_VIEW_ADD_ATTRIBUTES' | translate}}">
                <i class="fa fa-newspaper-o" style=""></i>
            </button>
        </div>
        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-if="allProductionOrderVm.showCalender==false"
                    ng-click="allProductionOrderVm.showCalenderView()"
                    title="Calender View">
                <i class="fa fa-calendar-check-o" aria-hidden="true"></i>
            </button>
            <button class="btn btn-sm btn-success" ng-if="allProductionOrderVm.showCalender==true"
                    ng-click="allProductionOrderVm.showCalender = false"
                    title="showTableView">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
        </div>
        <%--
                <div class="btn-group">
                    <button class="btn btn-sm btn-success" ng-if="allProductionOrderVm.ganttEnable==false"
                            ng-click="allProductionOrderVm.showProductionGantt()"
                            title="showGantt">
                        <i class="fa fa-calendar-check-o" aria-hidden="true"></i>
                    </button>
                </div>--%>
        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-if="allProductionOrderVm.ganttEnable==true"
                    ng-click="allProductionOrderVm.showTableView()"
                    title="showTableView">
                <i class="fa fa-table" aria-hidden="true"></i>
            </button>
        </div>

        <div class="btn-group">
            <button class="btn btn-sm btn-success" ng-if="allProductionOrderVm.ganttEnable==true"
                    ng-click="allProductionOrderVm.savePrGantt()"
                    title="save">
                <i class="fa fa-save" aria-hidden="true"></i>
            </button>
        </div>

        <button id="showPrGantt" ng-if="allProductionOrderVm.ganttEnable==true"
                class="btn btn-sm btn-default"
                style="margin-top: 1px;"
                title="{{showPrGantt ? 'Hide Gantt' : 'Show Gantt'}}"
                ng-click="allProductionOrderVm.toggleGrid(showPrGantt)">
            <i class="fa fa-list" style=""></i>
        </button>

         <span ng-if="allProductionOrderVm.ganttEnable==true" class="form-check"
               style="padding:10px;border-right: none;">
              <label class="form-check-label" style="margin-right: 5px">
                  <input class="form-check-input" type="radio" name="all"
                         id="all"
                         ng-click="allProductionOrderVm.selectScheduleType('ALL', $event)"
                         checked>
                  <span style="padding: 2px;margin-left: 5px;" translate>All</span>
              </label>
            <label class="form-check-label" style="margin-right: 5px">
                <input class="form-check-input" type="radio" name="schedule"
                       id="schedule"
                       ng-click="allProductionOrderVm.selectScheduleType('SCHEDULE', $event)">
                <span style="padding: 2px;margin-left: 5px;" translate>Schedule</span>
            </label>
            <label class="form-check-label" style="margin-right: 5px">
                <input class="form-check-input" type="radio" name="unSchedule"
                       id="unSchedule"
                       ng-click="allProductionOrderVm.selectScheduleType('UNSCHEDULE', $event)">
                <span style="padding: 2px;margin-left: 5px;" translate>UnSchedule</span>
            </label>
        </span>

        <div class="btn-group" ng-show="showPrGantt" style="width: 220px;">
            <div class="switch-toggle switch-candy">
                <input id="day" name="scale" type="radio" checked
                       ng-click="allProductionOrderVm.setScales('day', $event)">
                <label for="day" onclick="" translate>DAY</label>

                <input id="week" name="scale" type="radio"
                       ng-click="allProductionOrderVm.setScales('week', $event)">
                <label for="week" onclick="" translate>WEEK</label>

                <input id="month" name="scale" type="radio"
                       ng-click="allProductionOrderVm.setScales('month', $event)">
                <label for="month" onclick="" translate>MONTH</label>

                <input id="year" name="scale" type="radio"
                       ng-click="allProductionOrderVm.setScales('year', $event)">
                <label for="year" onclick="" translate>YEAR</label>
                <a href=""></a>
            </div>
        </div>


        <free-text-search on-clear="allProductionOrderVm.resetPage" search-term="allProductionOrderVm.searchText"
                          on-search="allProductionOrderVm.freeTextSearch"
                          filter-search="allProductionOrderVm.filterSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" ng-show="allProductionOrderVm.showCalender"
         style="overflow-y: auto;padding: 10px;">
        <div id="project-meetings" class="project-meetings calendar"
             ng-if="allProductionOrderVm.showCalender && allProductionOrderVm.renderView"
             ui-calendar="allProductionOrderVm.uiConfig.calendar"
             ng-model="allProductionOrderVm.eventSources" style="padding: 10px;"></div>

    </div>
    <div class="view-content no-padding" ng-show="allProductionOrderVm.showCalender==false"
         style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 150px" translate>NUMBER</th>
                    <th style="width: 150px" translate>TYPE</th>
                    <th style="width: 150px" translate>NAME</th>
                    <th translate>DESCRIPTION</th>
                    <%--<th translate>ASSIGNED_TO</th>--%>
                    <th translate>PLANT</th>
                    <%--<th style="width: 150px" translate>Shift</th>--%>
                    <th translate>STATUS</th>
                    <th style="width: 150px" translate>APPROVED_REJECTED_DATE</th>
                    <th style="width: 150px" translate>PLANNED_START_DATE</th>
                    <th style="width: 150px" translate>PLANNED_FINISH_DATE</th>
                    <th style="width: 150px" translate>TOTAL_PRODUCTION_TIME</th>
                    <th style="width: 150px" translate>ACTUAL_START_DATE</th>
                    <th style="width: 150px" translate>ACTUAL_FINISH_DATE</th>
                    <th style="width: 150px" translate>MODIFIED_BY</th>
                    <th style="width: 150px" translate>MODIFIED_DATE</th>
                    <th class='added-column'
                        style="width: 150px;z-index: auto !important;"
                        ng-repeat="selectedAttribute in allProductionOrderVm.selectedAttributes">
                        {{selectedAttribute.name}}
                        <i class="fa fa-times-circle"
                           ng-click="allProductionOrderVm.removeAttribute(selectedAttribute)"
                           title={{allProductionOrderVm.RemoveColumnTitle}}></i>
                    </th>
                    <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;"
                        translate>ACTIONS
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="allProductionOrderVm.loading == true">
                    <td colspan="25">
            <span style="font-size: 15px;">
              <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                   class="mr5">
              <span translate>LOADING_PRODUCTION_ORDER</span>
            </span>
                    </td>
                </tr>
                <tr ng-if="allProductionOrderVm.loading == false && allProductionOrderVm.productionOrders.content.length == 0">
                    <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                        <div class="no-data">
                            <img src="app/assets/no_data_images/ProductionOrder.png" alt="" class="image">

                            <div class="message" translate>NO_PRODUCTION_ORDERS</div>
                            <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                                NO_PERMISSION_MESSAGE
                            </div>
                        </div>
                    </td>
                </tr>
                <tr ng-repeat="productionOrder in allProductionOrderVm.productionOrders.content">
                    <td style="width: 1% !important;white-space: nowrap;">
                        <a href="" ng-click="allProductionOrderVm.showProductionOrder(productionOrder)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="productionOrder.number | highlightText: freeTextQuery"></span>
                        </a>
                    </td>
                    <td class="col-width-150">{{productionOrder.typeName}}</td>
                    <td class="col-width-200">{{productionOrder.name}}</td>
                    <td class="col-width-300" title="{{productionOrder.description}}">
                        <span ng-bind-html="productionOrder.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <%--<td>{{productionOrder.assignedToName}}</td>--%>
                    <td class="col-width-150">{{productionOrder.plantName}}</td>
                    <%--<td>
                        <a href="" title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="allProductionOrderVm.showShift(productionOrder)">
                            {{productionOrder.shiftName}}
                        </a>
                    </td>--%>
                    <td>
                        <item-status item="productionOrder"></item-status>
                    </td>
                    <td>{{productionOrder.approvedDate}}</td>
                    <td>{{productionOrder.plannedStartDate}}</td>
                    <td>{{productionOrder.plannedFinishDate}}</td>
                    <td>{{productionOrder.productionTime}}</td>
                    <td>{{productionOrder.actualStartDate}}</td>
                    <td>{{productionOrder.actualFinishDate}}</td>
                    <td>{{productionOrder.modifiedByName}}</td>
                    <td>{{productionOrder.modifiedDate}}</td>
                    <td class="text-center actions-col sticky-col sticky-actions-col">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-class="{'cursor-override': !hasPermission('productionorder','delete') || productionOrder.approved}">
                                    <a href="" ng-click="allProductionOrderVm.deleteProductionOrder(productionOrder)"
                                       ng-class="{'disabled':productionOrder.approved}"
                                       translate>DELETE</a>
                                </li>
                            </ul>
                        </span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="table-footer">
            <table-footer objects="allProductionOrderVm.productionOrders" pageable="allProductionOrderVm.pageable"
                          previous-page="allProductionOrderVm.previousPage"
                          next-page="allProductionOrderVm.nextPage"></table-footer>
        </div>
    </div>
</div>
