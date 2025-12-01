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

        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices, .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }

        a.disabled {
            cursor: not-allowed !important;
            color: lightgrey;
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
    </style>
    <div class='responsive-table'>
        <table class='table table-striped highlight-row' style="table-layout: fixed;">
            <thead>
            <tr>
                <th style="width: 200px;">
                    <i class="fa" title="{{itemBomVm.ExpandCollapse}}" style="cursor: pointer;"
                       ng-click="inspectionChecklistVm.expandAllSections()"
                       ng-class="{'fa-caret-right': (inspectionChecklistVm.expandAll == false || inspectionChecklistVm.expandAll == null || inspectionChecklistVm.expandAll == undefined),
                       'fa-caret-down': inspectionChecklistVm.expandAll == true}"></i>
                    <span translate>TITLE</span>
                </th>
                <th style="width: 200px;" translate>SUMMARY</th>
                <th style="width: 200px;" translate>PROCEDURE</th>
                <th style="width: 150px" translate>ASSIGNED_TO</th>
                <th style="width: 100px" translate>STATUS</th>
                <th style="width: 100px" translate>RESULT</th>
                <th style="width: 100px;text-align: center;" translate>PARAMS</th>
                <th style="width: 100px;text-align: center;" translate>ATTACHMENTS</th>
                <th style="width: 200px;" translate>NOTES</th>
                <th class="actions-col sticky-col sticky-actions-col" style="text-align:center; width: 80px;" translate>
                    ACTIONS
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="inspectionChecklistVm.loading == true">
                <td colspan="14"><img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                      class="mr5"><span translate>LOADING_CHECKLIST</span>
                </td>
            </tr>
            <tr ng-if="inspectionChecklistVm.loading == false && inspectionChecklistVm.checklists.length == 0">
                <td colspan="12"><span translate>NO_CHECKLIST</span></td>
            </tr>
            <tr ng-repeat="checklist in inspectionChecklistVm.checklists"
                ng-class="{'bold-item':checklist.planChecklist.type == 'SECTION'}">
                <td>
                <span class="level{{checklist.level}}">
                    <i ng-if="checklist.hasChecklist" class="mr5 fa" style="cursor: pointer;"
                       title="{{itemBomVm.ExpandCollapse}}" ng-click="inspectionChecklistVm.toggleNode(checklist)"
                       ng-class="{'fa-caret-right': (checklist.expanded == false || checklist.expanded == null || checklist.expanded == undefined),
                       'fa-caret-down': checklist.expanded == true}"></i>
                </span>
                    <span title="{{checklist.planChecklist.title}}">
                    {{checklist.planChecklist.title}}</span>
                </td>
                <td>
                    <span ng-if="checklist.planChecklist.type == 'CHECKLIST'"
                          title="{{checklist.planChecklist.summary}}">
                    {{checklist.planChecklist.summary}}</span>
                </td>
                <td>
                    <a href=""
                       ng-if="checklist.planChecklist.type == 'CHECKLIST' && checklist.planChecklist.procedure != null"
                       ng-click="showChecklistProcedure(checklist)">Click to show procedure</a>
                </td>
                <td style="width: 200px;">
                    <ui-select ng-model="checklist.assignedTo" theme="bootstrap"
                               style="width:150px" ng-if="checklist.editMode"
                               on-select="inspectionChecklistVm.selectAssignedTo(checklist,$item)">
                        <ui-select-match placeholder="Select Assigned To">{{$select.selected.fullName}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="person.id as person in inspectionChecklistVm.persons | filter: $select.search">
                            <div ng-bind="person.fullName"></div>
                        </ui-select-choices>
                    </ui-select>
                    <span id="checklistAssignTo"
                          ng-if="checklist.planChecklist.type == 'CHECKLIST' && !checklist.editMode">{{checklist.assignedToObject.fullName}}</span>
                </td>
                <td>
                    <checklist-status ng-if="checklist.planChecklist.type == 'CHECKLIST'"
                                      status="checklist.status"></checklist-status>
                </td>
                <td id="checklistResult">
                    <checklist-status
                            ng-if="checklist.planChecklist.type == 'CHECKLIST' && !checklist.editMode && checklist.paramsCount == 0"
                            status="checklist.result"></checklist-status>
                    <checklist-status ng-if="checklist.planChecklist.type == 'CHECKLIST' && checklist.paramsCount > 0"
                                      status="checklist.result"></checklist-status>
                    <ui-select ng-model="checklist.result" theme="bootstrap"
                               style="width:100px" ng-if="checklist.editMode && checklist.paramsCount == 0">
                        <ui-select-match placeholder="Select Result">{{$select.selected}}
                        </ui-select-match>
                        <ui-select-choices
                                repeat="result in inspectionChecklistVm.results | filter: $select.search">
                            <div ng-bind="result"></div>
                        </ui-select-choices>
                    </ui-select>
                </td>
                <td style="width: 100px;text-align: center;">
                    <a href=""
                       ng-if="checklist.planChecklist.type == 'CHECKLIST' && checklist.paramsCount > 0 && !checklist.editMode"
                       title="{{checklist.assignedTo == null ? 'Select Assigned To' : 'Parameters'}}"
                       ng-click="inspectionChecklistVm.addChecklistParameters(checklist)">{{checklist.paramsCount}}</a>
                    <span ng-if="checklist.planChecklist.type == 'CHECKLIST' && checklist.paramsCount > 0 && checklist.editMode">{{checklist.paramsCount}}</span>
                    <span ng-if="checklist.planChecklist.type == 'CHECKLIST' && checklist.paramsCount == 0">0</span>
                </td>
                <td style="width: 100px;text-align: center;">
                    <i class="fa fa-paperclip"
                       ng-if="checklist.planChecklist.type == 'CHECKLIST' && checklist.attachmentCount == 0"
                       title="Attachments" style="font-size: 14px;cursor: pointer;"
                       ng-click="inspectionChecklistVm.addAttachments(checklist)">
                    </i>
                    <a href="" ng-click="inspectionChecklistVm.addAttachments(checklist)"
                       title="{{checklist.assignedTo == null ? 'Select Assigned To' : 'Attachments'}}"
                       ng-if="checklist.planChecklist.type == 'CHECKLIST' && checklist.attachmentCount > 0 && !checklist.editMode">{{checklist.attachmentCount}}</a>
                    <span ng-if="checklist.planChecklist.type == 'CHECKLIST' && checklist.attachmentCount > 0 && checklist.editMode">{{checklist.attachmentCount}}</span>
                </td>
                <td title="{{checklist.notes}}">
                    <span id="notesField" ng-if="!checklist.editMode && checklist.planChecklist.type == 'CHECKLIST'">
                        {{checklist.notes}}
                    </span>
                    <input type="text" ng-if="checklist.editMode" class="form-control" ng-model="checklist.notes"
                           style="width: 200px;"/>
                </td>
                <td class="text-center actions-col sticky-col sticky-actions-col"
                    style="text-align:center; width: 80px;">
                    <span class="btn-group" ng-if="checklist.editMode == true" style="margin: -1px">
                    <i title="{{'SAVE' | translate}}"
                       ng-click="inspectionChecklistVm.updateChecklist(checklist)"
                       class="la la-check">
                    </i>
                    <i title="{{'CANCEL' | translate}}"
                       ng-click="inspectionChecklistVm.onCancel(checklist)"
                       class="la la-times">
                    </i>
                </span>
                    <span ng-if="!checklist.editMode && checklist.planChecklist.type == 'CHECKLIST'
                                 && (checklist.assignedTo == loginPersonDetails.person.id || hasPermission('admin','all'))
                                 && !inspection.released && inspection.statusType != 'REJECTED' && hasPermission('inspection','edit')"
                          class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                        style="z-index: 9999 !important;">
                        <li ng-click="inspectionChecklistVm.editChecklist(checklist)">
                            <a href="" translate>EDIT</a>
                        </li>
                        <plugin-table-actions context="inspection.checklist" object-value="checklist"></plugin-table-actions>
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
                <span ng-if="inspectionChecklistVm.checklistProcedure.planChecklist.encodedProcedure != null"
                      ng-bind-html="inspectionChecklistVm.checklistProcedure.planChecklist.encodedProcedure">
                </span>
            </div>
        </div>
    </div>
</div>