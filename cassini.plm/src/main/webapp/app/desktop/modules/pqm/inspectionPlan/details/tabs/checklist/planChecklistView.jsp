<div>
    <style scoped>
        .sticky-col {
            position: sticky !important;
            position: -webkit-sticky !important;
        }

        .sticky-actions-col {
            right: -10px !important;
        }

        .bold-item {
            font-weight: bold;;
        }

        .checklist-model.modal {
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

        .checklist-model .checklist-content {
            margin: auto;
            display: block;
            height: 94%;
            width: 97%;
            background-color: white;
            border-radius: 7px !important;
        }

        .checklist-header {
            padding: 5px;
            text-align: center;
            border-bottom: 1px solid lightgrey;
            height: 50px;
        }

        .procedure-header {
            font-weight: bold;
            font-size: 22px;
            /*position: absolute;*/
            display: inline-block;
            /*left: 44%;*/
            margin-top: 7px;
        }

        .procedure-content {
            padding: 10px;
            overflow: auto;
            min-width: 100%;
            width: auto;
            height: 100%;
        }

        .checklist-close {
            position: absolute;
            right: 35px;
            top: 25px;
            width: 38px;
            height: 38px;
            opacity: 0.3;
        }

        .checklist-close:hover {
            opacity: 0.6;
            border-radius: 50%;
            background-color: #ddd;
        }

        .checklist-close:before, .checklist-close:after {
            position: absolute;
            top: 7px;
            left: 18px;
            content: ' ';
            height: 22px;
            width: 2px;
            background-color: #333;
        }

        .checklist-close:before {
            transform: rotate(45deg) !important;
        }

        .checklist-close:after {
            transform: rotate(-45deg) !important;
        }

        input:disabled, textarea:disabled {
            background-color: #155887 !important;
        }


    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row'>
            <thead>
            <tr>
                <th style="width: 30px;"
                    ng-if="!inspectionPlanRevision.released && hasPermission('inspectionplan','edit') && !inspectionPlanRevision.rejected">
                    <i class="la la-plus" title="{{addSectionTitle}}" style="cursor: pointer"
                       ng-click="planChecklistVm.addSection()"></i>
                </th>
                <th>
                    <i class="fa" title="{{itemBomVm.ExpandCollapse}}" style="cursor: pointer;"
                       ng-if="inspectionDetailsCount.checklists > 0" ng-click="planChecklistVm.expandAllSections()"
                       ng-class="{'fa-caret-right': (planChecklistVm.expandAll == false || planChecklistVm.expandAll == null || planChecklistVm.expandAll == undefined),
                       'fa-caret-down': planChecklistVm.expandAll == true}"></i>
                    <span translate>TITLE</span>
                </th>
                <th translate>SUMMARY</th>
                <th style="width: 180px;" translate>PROCEDURE</th>
                <th style="width: 100px;text-align: center;" translate>PARAMS</th>
                <th style="width: 100px;text-align: center;" translate>ATTACHMENTS</th>
                <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;" translate>
                    ACTIONS
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="planChecklistVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_CHECKLIST</span>
                </td>
            </tr>
            <tr ng-if="planChecklistVm.loading == false && planChecklistVm.checklists.length == 0">
                <td colspan="12" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/Checklist.png" alt="" class="image">

                        <div class="message">{{ 'NO_CHECKLIST' | translate}}</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="checklist in planChecklistVm.checklists"
                ng-class="{'bold-item':checklist.type == 'SECTION'}">
                <td style="width: 30px;"
                    ng-if="!inspectionPlanRevision.released && hasPermission('inspectionplan','edit') && !inspectionPlanRevision.rejected">
                    <i class="la la-plus" ng-if="checklist.editMode == false && checklist.type == 'SECTION'"
                       title="{{addChecklistTitle}}" style="cursor: pointer"
                       ng-click="planChecklistVm.addChecklist(checklist)"></i>
                </td>
                <td>
                    <span class="level{{checklist.level}}"
                          ng-if="checklist.isNew == false && checklist.editMode == false">
                        <i ng-if="checklist.hasChecklist" class="mr5 fa" style="cursor: pointer;"
                           title="{{planChecklistVm.ExpandCollapse}}" ng-click="planChecklistVm.toggleNode(checklist)"
                           ng-class="{'fa-caret-right': (checklist.expanded == false || checklist.expanded == null || checklist.expanded == undefined),
                                      'fa-caret-down': checklist.expanded == true}"></i>
                    </span>
                    <span ng-if="!checklist.editMode" title="{{checklist.title}}" id="checklistTitle">
                    {{checklist.title}}</span>

                    <form>
                        <input class="form-control" ng-if="checklist.editMode" ng-model="checklist.title"
                               style="width: 300px;" autofocus/>
                    </form>
                </td>
                <td>
                    <span ng-if="!checklist.editMode && checklist.type == 'CHECKLIST'" title="{{checklist.summary}}">
                    {{checklist.summary}}</span>

                    <form><input class="form-control" ng-if="checklist.editMode && checklist.type == 'CHECKLIST'"
                                 ng-model="checklist.summary"
                                 style="width: 300px;"/></form>
                </td>
                <td style="width: 180px;">
                    <i class="fa fa-pencil" ng-if="checklist.type == 'CHECKLIST' && checklist.editMode"
                       ng-click="showChecklistProcedure(checklist)"></i>
                    <a href="" ng-if="!checklist.editMode && checklist.procedure != null"
                       ng-click="showChecklistProcedure(checklist)">Click to show procedure</a>
                </td>
                <td style="width: 100px;text-align: center;">
                    <a href="" ng-if="checklist.type == 'CHECKLIST' && checklist.paramsCount > 0"
                       title="Show Parameters" style="font-size: 14px;"
                       ng-click="planChecklistVm.addChecklistParameters(checklist)">{{checklist.paramsCount}}</a>
                    <i class="fa fa-pencil-square-o" ng-if="checklist.type == 'CHECKLIST' && checklist.paramsCount == 0"
                       ng-click="planChecklistVm.addChecklistParameters(checklist)" title="Add Parameters"
                       style="font-size: 16px;"></i>
                </td>
                <td style="width: 100px;text-align: center;">
                    <i class="fa fa-paperclip"
                       ng-if="checklist.type == 'CHECKLIST' && !checklist.isNew && checklist.attachmentCount == 0"
                       title="Attachments" style="font-size: 16px;cursor: pointer;"
                       ng-click="planChecklistVm.addAttachments(checklist)">
                    </i>
                    <a href="" title="Attachments" style="font-size: 14px;"
                       ng-if="checklist.type == 'CHECKLIST' && checklist.attachmentCount > 0"
                       ng-click="planChecklistVm.addAttachments(checklist)">{{checklist.attachmentCount}}</a>
                </td>
                <td class="text-center actions-col sticky-col sticky-actions-col"
                    style="text-align:center; width: 80px;">
                <span class="btn-group"
                      ng-if="checklist.isNew == true || checklist.editMode == true"
                      style="margin: -1px">
                    <i title="{{'SAVE' | translate}}"
                       ng-click="planChecklistVm.onOk(checklist)"
                       class="la la-check">
                    </i>
                    <i title="{{'CANCEL' | translate}}"
                       ng-click="planChecklistVm.onCancel(checklist)"
                       class="la la-times">
                    </i>
                </span>
                <span ng-if="checklist.editMode == false && !inspectionPlanRevision.released && hasPermission('inspectionplan','edit') && !inspectionPlanRevision.rejected"
                      class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="planChecklistVm.editChecklist(checklist)">
                            <a href="" translate>EDIT</a>
                        </li>

                        <li ng-click="planChecklistVm.deleteChecklist(checklist)">
                            <a href="" translate>DELETE</a>
                        </li>
                        <plugin-table-actions context="inspectionPlan.checklist" object-value="checklist"></plugin-table-actions>
                    </ul>
                </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <div id="checklist-procedure" class="checklist-model modal">
        <div class="checklist-content">
            <div class="checklist-header">
                <span class="procedure-header" translate>CHECKLIST_PROCEDURE</span>
                <a href="" ng-click="hideProcedureDialog()" class="checklist-close pull-right"
                   style="display: inline-block"></a>
            </div>
            <div class="procedure-content" style="padding: 10px">
                <div ng-if="planChecklistVm.checklistProcedure.editMode">
                    <summernote ng-model="planChecklistVm.checklistProcedure.oldProcedure"></summernote>
                </div>
                <span ng-if="planChecklistVm.checklistProcedure.editMode == false && planChecklistVm.checklistProcedure.encodedProcedure != null"
                      ng-bind-html="planChecklistVm.checklistProcedure.encodedProcedure">
                </span>
            </div>
        </div>
    </div>
</div>