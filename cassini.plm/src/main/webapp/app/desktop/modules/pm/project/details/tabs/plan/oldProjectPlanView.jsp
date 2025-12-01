<div>
    <style scoped>
        .wbs-node {
            background: transparent url("app/assets/images/wbsIcon.png") no-repeat !important;
            height: 20px;
            padding-left: 20px;
        }

        .activity-node {
            background: transparent url("app/assets/images/activityIcon.png") no-repeat !important;
            height: 16px;
            padding-left: 20px;
        }

        .milestone-node {
            background: transparent url("app/assets/images/milestoneIcon.png") no-repeat !important;
            height: 16px;
            padding-left: 20px;
        }

        .projectPlan-progress.progress {
            background-color: #B0C7CF;
            height: 20px !important;
            margin: 0 !important;
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

        .popover table {
            width: 497px;
            max-width: 100% !important;
        }

        #description {
            word-wrap: break-word;
            width: 300px;
            white-space: normal;
            text-align: left;
        }

        .row-menu i {
            cursor: pointer;
        }

        .space {
            /*background: none !important;*/
        }

        .space::after {
            /*background: none !important;*/
        }

        .space:hover {
            /*background: none !important;*/
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

        table {
            table-layout: fixed;
        }

        table.highlight-row tbody tr:hover td span i {
            color: white !important;
        }

        .label.label-default {
            padding: 1px 4px !important;
        }

    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row' style="overflow-y: auto !important;">
            <thead>
            <tr>
                <%-- <th style="width: 50px;"><i style="margin-left: 5px;" class="la la-plus"
                                             title={{oldProjectPlanVm.createWbs}}
                                             ng-click="addWbs()"></i></th>--%>

                <th style="width: 50px;">
                   <span> <i style="margin-left: 5px;cursor: pointer !important;" class="la la-plus"
                             title={{oldProjectPlanVm.createWbs}}
                             ng-click="addWbs()"></i></span>

                </th>
                <th style="width: 300px;" translate>NAME</th>
                <th class="description-column" translate>DESCRIPTION</th>
                <th style="width: 150px;" translate>ASSIGNED_TO</th>
                <th style="width: 100px;" translate>STATUS</th>
                <th style="width: 150px;" translate>PERCENT_COMPLETE</th>
                <th style="width: 150px;" translate>PLANNED_START_DATE</th>
                <th style="width: 150px;" translate>PLANNED_FINISH_DATE</th>
                <th style="width: 150px;" translate>ACTUAL_START_DATE</th>
                <th style="width: 150px;" translate>ACTUAL_FINISH_DATE</th>
                <th class='added-column'
                    style="width: 150px;"
                    ng-repeat="selectedAttribute in oldProjectPlanVm.selectedAttributes">
                    {{selectedAttribute.name}}
                    <i class="fa fa-times-circle" style="margin-left: 9px;"
                       ng-click="oldProjectPlanVm.removeAttribute(selectedAttribute)"
                       title="{{RemoveColumnTitle}}"></i>
                </th>
                <th style="min-width: 100px !important;" class="actions-col" translate>ACTIONS</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="oldProjectPlanVm.loading == true">
                <td colspan="16">
                    <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                         class="mr5">
                    <span translate>LOADING_WBS</span>
                </td>
            </tr>
            <tr ng-if="oldProjectPlanVm.loading == false && oldProjectPlanVm.wbsItems.length == 0">
                <td colspan="16"><span translate>NO_WBS</span></td>
            </tr>
            <tr id="{{$index}}" ng-repeat="wbs in oldProjectPlanVm.wbsItems"
                dragprojectwbs wbs-items="oldProjectPlanVm.wbsItems" update-wbs-seq="oldProjectPlanVm.updateWbsSeq">

                <td style="width: 50px;" ng-if="wbs.objectType == 'PROJECTWBSELEMENT'">
                <span uib-dropdown dropdown-append-to-body>
                        <i class="la la-plus dropdown-toggle" uib-dropdown-toggle
                           title={{oldProjectPlanVm.addActivity}} style="margin-left: 5px;"></i>
                        <ul uib-dropdown-menu class="dropdown-menu" role="menu" style="z-index: 9999 !important;">
                            <li><a href ng-click="addActivity(wbs)" translate>ADD_ACTIVITY</a></li>
                            <li><a href ng-click="addMilestone(wbs)" translate>ADD_MILESTONE</a></li>
                        </ul>
                    </span>

                </td>
                <td class="space" ng-if="wbs.objectType != 'PROJECTWBSELEMENT'">

                </td>


                <td style="width: 300px;" class="name-col">
                <span class="level{{wbs.level}}">
                    <i class="mr5 fa" ng-if="wbs.objectType == 'PROJECTWBSELEMENT'"
                       title="{{oldProjectPlanVm.ExpandCollapse}}"
                       style="cursor: pointer; font-size: 15px;"
                       ng-class="{'fa-caret-right': (wbs.expanded == false || wbs.expanded == null || wbs.expanded == undefined),
                           'fa-caret-down': wbs.expanded == true}"
                       ng-click="oldProjectPlanVm.toggleNode(wbs)"></i>
                    <span ng-if="wbs.objectType == 'PROJECTWBSELEMENT'" ng-click="oldProjectPlanVm.toggleNode(wbs)">
                         {{wbs.name}}
                    </span>
                    <span ng-if="wbs.objectType == 'PROJECTACTIVITY'"
                          style="">
                        <%--<a href="" ng-click="oldProjectPlanVm.openActivityDetails(wbs)">{{wbs.name}}</a>--%>


                         <a class="activity-node"
                            style="padding-right: 4px; overflow-y: hidden !important;"
                            title="{{oldProjectPlanVm.showTasks}}"
                            ng-if="hasPermission('project','view') || ((sharedProjectPermission == 'READ' || sharedProjectPermission == 'WRITE') && external.external == true)"
                            uib-popover-template="oldProjectPlanVm.activityPopover.templateUrl"
                            popover-append-to-body="true"
                            popover-popup-delay="1000"
                            popover-placement="top-left"
                            popover-title="({{wbs.name}}) {{oldProjectPlanVm.Tasks}}"
                            popover-trigger="'outsideClick'" popover-placement="right">
                             <span ng-click="oldProjectPlanVm.openActivityDetails(wbs)">{{wbs.name}}</span>
                         </a>
                         <span class="label label-default" ng-if="wbs.count > 0" title="Tasks"
                               style="font-size: 12px;background-color: #e4dddd;"
                               ng-bind-html="wbs.count"></span>
                         <span ng-if="hasPermission('project','view') == false && external.external == false">{{wbs.name}}</span>

                       <%-- <a ng-click="oldProjectPlanVm.openActivityDetails(wbs)"
                           ng-if="hasPermission('project','view')"
                           uib-popover-template="oldProjectPlanVm.activityPopover.templateUrl"
                           popover-popup-delay="1000"
                           popover-title="({{wbs.name}}) Tasks"
                           popover-trigger="'mouseenter'" popover-placement="right">{{wbs.name}}</a>
                        <span ng-if="hasPermission('project','view') == false">{{wbs.name}}</span>--%>
                    </span>

                    <span class="milestone-node" ng-if="wbs.objectType == 'PROJECTMILESTONE'"
                          style="">
                        {{wbs.name}}
                    </span>

                </span>
                </td>


                <td class="description-column">
                    <span>{{wbs.description}}</span>
                </td>
                <td style="width: 150px;">
                    <span ng-if="wbs.objectType == 'PROJECTACTIVITY' || wbs.objectType == 'PROJECTMILESTONE'">{{wbs.person.firstName || '?'}}</span>
                    <span ng-if="wbs.objectType == 'PROJECTWBSELEMENT'"></span>
                </td>
                <td style="width: 100px;">
                <span ng-if="wbs.objectType != 'PROJECTWBSELEMENT'">
                    <task-status task="wbs"></task-status>
                </span>
                    <span ng-if="wbs.objectType == 'PROJECTWBSELEMENT'"></span>
                </td>
                <td style="width: 150px;">
                    <div ng-if="wbs.objectType == 'PROJECTACTIVITY' && wbs.percentComplete < 100"
                         class="projectPlan-progress progress text-center">
                        <div style="width:{{wbs.percentComplete}}%"
                             class="progress-bar progress-bar-primary progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 2px;">{{wbs.percentComplete}}%</span>
                        </div>
                    </div>
                    <div ng-if="wbs.objectType == 'PROJECTACTIVITY' && wbs.percentComplete == 100"
                         class="projectPlan-progress progress text-center">
                        <div style="width:{{wbs.percentComplete}}%"
                             class="progress-bar progress-bar-success progress-bar-striped active"
                             role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100">
                            <span style="margin-left: 2px;">{{wbs.percentComplete}}%</span>
                        </div>
                    </div>
                    <span ng-if="wbs.objectType == 'PROJECTWBSELEMENT'"></span>
                </td>
                <td style="width: 150px;">
                    <span ng-if="wbs.objectType == 'PROJECTACTIVITY' && currentLang == 'en'">{{wbs.plannedStartDate || '?'}}</span>
                    <span ng-if="wbs.objectType == 'PROJECTACTIVITY' && currentLang == 'de'">{{wbs.plannedStartDatede || '?'}}</span>
                    <span ng-if="wbs.objectType == 'PROJECTMILESTONE' || wbs.objectType == 'PROJECTWBSELEMENT'"></span>
                </td>
                <td style="width: 150px;">
                    <span ng-if="(wbs.objectType == 'PROJECTACTIVITY' || wbs.objectType == 'PROJECTMILESTONE') &&  currentLang == 'en'">{{wbs.plannedFinishDate || '?'}}</span>
                    <span ng-if="(wbs.objectType == 'PROJECTACTIVITY' || wbs.objectType == 'PROJECTMILESTONE') &&  currentLang == 'de'">{{wbs.plannedFinishDatede || '?'}}</span>
                    <span ng-if="wbs.objectType == 'PROJECTWBSELEMENT'"></span>
                </td>
                <td style="width: 150px;">
                    <span ng-if="wbs.objectType == 'PROJECTACTIVITY'">{{wbs.actualStartDate}}</span>
                    <span ng-if="wbs.objectType == 'PROJECTMILESTONE' || wbs.objectType == 'PROJECTWBSELEMENT'"></span>
                </td>
                <td style="width: 150px;">
                    <span ng-if="(wbs.objectType == 'PROJECTACTIVITY' || wbs.objectType == 'PROJECTMILESTONE')">{{wbs.actualFinishDate}}</span>
                    <span ng-if="wbs.objectType == 'PROJECTWBSELEMENT'"></span>
                </td>


                <td class="added-column"
                    ng-repeat="objectAttribute in oldProjectPlanVm.selectedAttributes">
                    <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST'
                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                       ng-init="attrName = objectAttribute.id">
                        <a href=""
                           ng-if="objectAttribute.refType == 'ITEM'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="oldProjectPlanVm.showAttributeDetails(wbs[attrName])">{{wbs[attrName].itemNumber}}
                        </a>
                        <a href=""
                           ng-if="objectAttribute.refType == 'ITEMREVISION'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="oldProjectPlanVm.showAttributeDetails(wbs[attrName])">
                            {{wbs[attrName].wbsMaster+" "+wbs[attrName].revision+" "+
                            wbs[attrName].lifeCyclePhase.phase}}
                        </a>
                        <a href=""
                           ng-if="objectAttribute.refType == 'CHANGE'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="oldProjectPlanVm.showAttributeDetails(wbs[attrName])">{{wbs[attrName].ecoNumber}}
                        </a>
                        <a href=""
                           ng-if="objectAttribute.refType == 'MANUFACTURER'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="oldProjectPlanVm.showAttributeDetails(wbs[attrName])">{{wbs[attrName].name}}
                        </a>
                        <a href=""
                           ng-if="objectAttribute.refType == 'MANUFACTURERPART'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="oldProjectPlanVm.showAttributeDetails(wbs[attrName])">{{wbs[attrName].partNumber}}
                        </a>
                        <a href=""
                           ng-if="objectAttribute.refType == 'WORKFLOW'"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}"
                           ng-click="oldProjectPlanVm.showAttributeDetails(wbs[attrName])">{{wbs[attrName].name}}
                        </a>

                    </p>

                    <div class="attributeTooltip"
                         ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT' && objectAttribute.dataType != 'RICHTEXT'"
                         ng-init="attrName = objectAttribute.id">
                        <p>
                            <a ng-if="wbs[attrName].length > 0" href="">
                                {{wbs[attrName].length}} Attachments
                            </a>
                        </p>

                        <div class="attributeTooltiptext">
                            <ul>
                                <li ng-repeat="attachment in wbs[attrName]">
                                    <a href="" ng-click="oldProjectPlanVm.openAttachment(attachment)"
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
                            <a ng-if="wbs[attrName].length > 0" href="">
                                {{wbs[attrName].length}} listOfValue
                            </a>
                        </p>

                        <div class="attributeTooltiptext">
                            <ul>
                                <li ng-repeat="listValue in wbs[attrName]">
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
                                {{wbs[attrName]}}
                            </a>
                        </p>
                    </div>

                    <div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST'
                         && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE' && objectAttribute.dataType != 'RICHTEXT'"
                         ng-init="attrName = objectAttribute.id">
                        <a href="" ng-click="oldProjectPlanVm.showImage(wbs[attrName])"
                           title="{{'CLICK_TO_SHOW_IMAGE' | translate}}">
                            <img ng-if="wbs[attrName] != null"
                                 ng-src="{{wbs[attrName]}}"
                                 style="height: 30px;width: 40px;margin-bottom: 5px;">
                        </a>

                        <div id="myModal23" class="img-model modal">
                            <span class="closeImage1">&times;</span>
                            <img class="modal-content" id="img03">
                        </div>
                    </div>

                    <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy' && objectAttribute.dataType != 'LIST'
                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'&& objectAttribute.dataType != 'LONGTEXT'&& objectAttribute.dataType != 'HYPERLINK'
                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType != 'RICHTEXT' && objectAttribute.dataType != 'TEXT'"
                       ng-init="attrName = objectAttribute.id">
                        {{wbs[attrName]}}
                    </p>


                    <p ng-if="objectAttribute.dataType == 'LONGTEXT'"
                       ng-init="attrName = objectAttribute.id" title="{{wbs[attrName]}}"
                       style="height: 30px;width: 70px;margin-bottom: -7px;">
                        {{wbs[attrName]}}
                    </p>

                    <p ng-if="objectAttribute.dataType == 'TEXT'"
                       ng-init="attrName = objectAttribute.id" title="{{wbs[attrName]}}"
                       style="height: 30px;width: 70px;margin-bottom: -7px;">
                        {{wbs[attrName]}}
                    </p>

                    <p ng-if="objectAttribute.dataType == 'HYPERLINK'"
                       ng-init="attrName = objectAttribute.id">
                        <a href=""
                           ng-click="showHyperLink(wbs[attrName])">{{wbs[attrName]}}</a>
                    </p>

                    <%--  Show Rich text content in modal --%>
                     <span style="height: 30px;width: 40px;margin-bottom: 5px;" ng-init="attrName = objectAttribute.id"
                           ng-if="objectAttribute.dataType == 'RICHTEXT'">
                          <a href=""
                             ng-if="wbs[attrName] != null && wbs[attrName] != undefined && wbs[attrName] != ''"
                             data-toggle="modal"
                             ng-click="showRichTextSidePanel(wbs[attrName],objectAttribute,wbs)">Click to show
                              RichText</a>
                     </span>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="wbs[currencyType]"
                                >
                        </span>

                 <span ng-init="attrName = objectAttribute.id"
                       ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{wbs[attrName]}}
                        </span>

                    <p ng-if="objectAttribute.name == 'ModifiedBy'">
                        {{wbs.modifiedByObject.firstName}}</p>

                    <p ng-if="objectAttribute.name == 'CreatedBy'">
                        {{wbs.createdByObject.firstName}}</p>

                    <p ng-if="objectAttribute.name == 'CreatedDate' && wbs.objectType == 'PROJECTACTIVITY'">
                        {{wbs.createdDate}}</p>

                    <p ng-if="objectAttribute.name == 'ModifiedDate' && wbs.objectType == 'PROJECTACTIVITY'">
                        {{wbs.modifiedDate}}</p>

                </td>
                <td class="text-center">
                <span class="row-menu" uib-dropdown dropdown-append-to-body
                      style="min-width: 50px">
                    <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                      <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                          style="z-index: 9999 !important;">
                          <li ng-if="wbs.objectType == 'PROJECTMILESTONE' && hasPermission('project','edit') && wbs.status != 'FINISHED'"
                              ng-click="oldProjectPlanVm.finishMilestone(wbs)"
                              ng-class="{'disabled':wbs.finishMilestone == false}">
                              <a href="" translate>FINISH_MILESTONE</a>
                          </li>
                          <li ng-if="wbs.objectType == 'PROJECTWBSELEMENT' && hasPermission('project','edit') && external.external== false"
                              ng-class="{'disabled':wbs.activities.length > 0 || wbs.milestones.length > 0 || wbs.percentComplete == 100}"
                              ng-click="oldProjectPlanVm.editWbs(wbs)">
                              <a href="" translate>EDIT_WBS</a>
                          </li>

                          <li ng-if="wbs.objectType == 'PROJECTMILESTONE' && (loginPersonDetails.person.id == wbs.assignedTo && external.external== true)"
                              ng-class="{'disabled':wbs.status =='FINISHED' || wbs.percentComplete == 100}"
                              ng-click="oldProjectPlanVm.editWbs(wbs)">
                              <a href="" translate>EDIT_MILESTONE</a>
                          </li>

                          <li ng-if="wbs.objectType == 'PROJECTMILESTONE' && hasPermission('project','delete') && external.external== false"
                              ng-class="{'disabled':wbs.status == 'FINISHED' || wbs.percentComplete == 100}"
                              ng-click="oldProjectPlanVm.deleteWbs(wbs)">
                              <a href="" translate>DELETE_MILESTONE</a>
                          </li>

                          <li ng-if="wbs.objectType == 'PROJECTMILESTONE' && hasPermission('project','edit') && external.external== false"
                              ng-class="{'disabled':wbs.status =='FINISHED' || wbs.percentComplete == 100}"
                              ng-click="oldProjectPlanVm.editWbs(wbs)">
                              <a href="" translate>EDIT_MILESTONE</a>
                          </li>

                          <li ng-if="wbs.objectType == 'PROJECTMILESTONE' && (loginPersonDetails.person.id == wbs.assignedTo && external.external== true)"
                              ng-class="{'disabled':wbs.status == 'FINISHED' || wbs.percentComplete == 100}"
                              ng-click="oldProjectPlanVm.deleteWbs(wbs)">
                              <a href="" translate>DELETE_MILESTONE</a>
                          </li>
                          <li ng-if="wbs.objectType == 'PROJECTACTIVITY' && hasPermission('project','edit') && external.external== false"
                              ng-class="{'disabled':wbs.percentComplete == 100 || wbs.status =='FINISHED'}"
                              ng-click="oldProjectPlanVm.editWbs(wbs)">
                              <a href="" translate>EDIT_ACTIVITY</a>

                          </li>
                          <li ng-if="wbs.objectType == 'PROJECTACTIVITY' && (loginPersonDetails.person.id == wbs.assignedTo && external.external== true)"
                              ng-class="{'disabled':wbs.percentComplete == 100 || wbs.status =='FINISHED'}"
                              ng-click="oldProjectPlanVm.editWbs(wbs)">
                              <a href="" translate>EDIT_ACTIVITY</a>
                          </li>
                          <li ng-if="wbs.objectType == 'PROJECTACTIVITY' && hasPermission('project','edit') && external.external== false"
                              ng-class="{'disabled':wbs.percentComplete == 100 || wbs.status =='FINISHED'}"
                              ng-click="oldProjectPlanVm.finnishActivity(wbs)">
                              <a href="" translate>FinishActivity</a>

                          </li>
                          <li ng-if="wbs.objectType == 'PROJECTACTIVITY' && hasPermission('project','delete') && external.external== false"
                              ng-class="{'disabled':wbs.activityTasks.length > 0 || wbs.activityFiles.length > 0
                                        || wbs.activityDeliverables.length > 0 || wbs.activityItemReferences.length > 0 || wbs.status == 'FINISHED' || wbs.percentComplete == 100}"
                              ng-click="oldProjectPlanVm.deleteWbs(wbs)">
                              <a href="" translate>DELETE_ACTIVITY</a>
                          </li>
                          <li ng-if="wbs.objectType == 'PROJECTACTIVITY' && (loginPersonDetails.person.id == wbs.assignedTo && external.external== true)"
                              ng-class="{'disabled':wbs.activityTasks.length > 0 || wbs.activityFiles.length > 0
                                        || wbs.activityDeliverables.length > 0 || wbs.activityItemReferences.length > 0 || wbs.status == 'FINISHED' || wbs.percentComplete == 100}"
                              ng-click="oldProjectPlanVm.deleteWbs(wbs)">
                              <a href="" translate>DELETE_ACTIVITY</a>
                          </li>
                          <li ng-if="wbs.objectType == 'PROJECTWBSELEMENT' && hasPermission('project','delete') && external.external== false"
                              ng-click="oldProjectPlanVm.deleteWbs(wbs)"
                              ng-class="{'disabled':wbs.activities.length > 0 || wbs.milestones.length > 0 || wbs.percentComplete == 100}">
                              <a href="" translate>DELETE_WBS</a>
                          </li>
                      </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>