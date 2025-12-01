<div style="display: flex; flex-direction: row;height: 100%">
    <style scoped>
        table {
            table-layout: fixed;
        }

        .ui-select-bootstrap .ui-select-match-text span {
            vertical-align: bottom;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {

        }

        .ui-select-bootstrap > .ui-select-choices,
        .ui-select-bootstrap > .ui-select-no-choice {
            position: absolute !important;
        }

        .reqs-table {
            flex: 1;
            height: 100%;
            position: unset !important;
            padding: 10px 0 0 0 !important;
            scroll-behavior: smooth;
        }

        .reqs-table .req-name {
            font-size: 15px;
            font-weight: 600;
            color: #0b58a2;
            padding: 3px 5px;
            white-space: nowrap;
            cursor: pointer;
        }

        .reqs-table .req-name[contenteditable=true],
        .reqs-table .req-desc[contenteditable=true] {
            background-color: #fff;
        }

        /*
        [contenteditable]:focus {
            outline: 0px solid transparent;
        }

        .reqs-table .req-name[contenteditable="true"] {
            border: 1px dotted #ddd;
        }*/

        .reqs-table .req-name a {
            color: #0b58a2;
        }

        .reqs-table .req-desc {
            white-space: pre-wrap;
            word-break: break-word !important;
            padding-left: 8.4px;
            cursor: pointer;
            padding-bottom: 7px;
        }

        .reqs-table .req-type {

        }

        .reqs-table .req-status {

        }

        .reqs-table .req-desc p:last-child {
            margin-bottom: 0;
        }

        .reqs-table .aligin-top {
            vertical-align: initial !important;
        }

        .reqs-table td .row-actions-btn {
            visibility: hidden;
        }

        .reqs-table tbody tr td .la-times {
            font-size: 17px;
        }

        .reqs-table tbody tr:hover td .row-actions-btn {
            visibility: visible;
        }

        .reqs-table tbody tr:hover > td {
            background-color: var(--cassini-form-controls-color) !important;
        }

        .ck.ck-editor__editable_inline > :first-child {
            margin-top: 0 !important;
        }

        .ck.ck-editor__editable_inline > :last-child {
            margin-bottom: 0 !important;
        }

        .ck.ck-editor__editable_inline {
            overflow-x: hidden !important;
            word-break: break-word !important;
        }

        .ck-content.ck-editor__editable.ck-focused {
            padding-top: 8.4px !important;
            padding-bottom: 8.4px !important;
        }

        .reqstree-container {
            flex: 1;
            max-width: 400px;
            height: 100%;
            border-right: 1px solid #ddd;
        }

        .reqstree-container .reqs-tree {
            position: absolute;
            left: 0;
            top: 10px;
            bottom: 0;
            padding-left: 0;
            margin: 0;
            width: 400px;
            overflow-y: auto;
        }

        .reqstree-container .reqs-tree .tree-indent,
        .reqstree-container .reqs-tree .tree-folder,
        .reqstree-container .reqs-tree .tree-file {
            height: 16px !important;
            margin-top: 4px;
        }

        .reqstree-container .reqs-tree .tree-title {
            font-size: 14px;
            margin-top: 4px;
        }

        .requirements-root {
            background: transparent url("app/assets/images/req.png") no-repeat !important;
            height: 16px;
        }

        .requirements-node {
            background: transparent url("app/assets/images/requirements.png") no-repeat !important;
            height: 16px;
        }

        .req-node {
            background: transparent url("app/assets/images/req.png") no-repeat !important;
            height: 16px;
        }

        .reqdoc-node {
            background: transparent url("app/assets/images/document.png") no-repeat !important;
            height: 16px;
        }

        .priority {
            padding: 5px;
        }

        .priority-low {
            background-color: #1A70BE;
        }

        .ck.ck-reset_all.ck-widget__type-around,
        .ck.ck-reset_all.ck-widget__resizer,
        .ck-editor__editable.ck-editor__nested-editable.ck-placeholder {
            display: none !important;
        }

        .ck.ck-editor__editable .ck-reset_all.ck-widget__resizer {
            display: unset !important;
        }

        .req-desc .table > table {
            width: 100% !important;
        }

        .ck.ck-widget__selection-handle {
            display: none !important;
        }

        .ck.ck-editor__editable .ck-widget__selection-handle {
            display: block !important;
        }

        .req-desc .image.image_resized > img {
            width: 100%;
            height: 100%;
        }

        .req-desc p .marker-yellow {
            background-color: yellow;
        }

        .mx-width-50 {
            max-width: 50px;
        }

        .mx-width-100 {
            max-width: 100px;
        }

        .req-seq {
            margin-left: 3px;
        }

        .req-number {
            margin-left: auto;
        }

        .wd-100p {
            width: 100%;
        }

        .mr-5 {
            margin-right: 5px;
        }

    </style>
    <div class="reqstree-container">
        <ul id="requirementsTree" class="easyui-tree reqs-tree"></ul>
    </div>
    <div id="requirementsTable" class='responsive-table reqs-table'>
        <table class='table'>
            <thead>
            <tr>
                <th style="width:50px;">
                    <i ng-show="reqDocumentRevision.lifeCyclePhase.phase == 'Draft'" class="la la-plus"
                       style="cursor: pointer" title="{{ 'ADD_NEW_REQUIREMENT' | translate}}"
                       ng-click="requirementsVm.newRequirement(null, null)"></i>
                </th>
                <%--<th style="width: 50px;" translate>SEQ</th>
                <th style="width: 100px;" translate>NUMBER</th>--%>
                <th id="descriptionColumn" style=""><span style="margin-left: 8.4px;" translate>NAME</span>/<span
                        translate>DESCRIPTION</span></th>
                <th>
                    <span ng-if="requirementsVm.selectedRequirementType != null"
                      style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                        ({{requirementsVm.selectedRequirementType.name}})
                        <i class="fa fa-times-circle" ng-click="requirementsVm.clearTypeSelection()"
                           title="{{removeTitle}}"></i>
                    </span>
                        <br>
                        <div class="dropdown" uib-dropdown style="display: inline-block"
                             ng-if="requirementsVm.search.searchType != 'advanced'">
                            <span uib-dropdown-toggle><span translate>TYPE</span>
                                <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                            </span>
                                        <div class="dropdown-menu" role="menu">
                                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;width: 300px;height: 200px;">
                                                <req-management-type-tree
                                                on-select-type="requirementsVm.onSelectRequirementType"
                                                object-type="REQUIREMENTTYPE"></req-management-type-tree>
                                            </div>
                                        </div>
                        </div>
                </th>
                <th style="width: 40px;" translate>VERSION</th>
                <th style="width: 150px;z-index: auto !important;">
                    <span ng-if="requirementsVm.selectedPhase != null"
                          style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                            ({{requirementsVm.selectedPhase}})
                            <i class="fa fa-times-circle" ng-click="requirementsVm.clearPhase()"
                               title="{{removeTitle}}"></i>
                    </span>
                    <br>
                    <div class="dropdown" uib-dropdown style="display: inline-block">
                                <span uib-dropdown-toggle><span translate>STATUS</span>
                                    <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                                </span>
                            <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                                style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                                <li ng-repeat="phase in requirementsVm.lifecyclePhases"
                                    ng-click="requirementsVm.onSelectPhase(phase)" style="text-transform: uppercase;"><a
                                        href="">{{phase}}</a>
                                </li>
                            </ul>
                    </div>
                    
                </th>
                <th style="width: 150px;z-index: auto !important;">
                    <span ng-if="selectedPriority != null"
                          style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                            ({{selectedPriority}})
                            <i class="fa fa-times-circle" ng-click="clearPriority()"
                               title="{{removeTitle}}"></i>
                    </span>
                    <br>
                    <div class="dropdown" uib-dropdown style="display: inline-block">
                        <span uib-dropdown-toggle><span translate>PRIORITY</span>
                            <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                        </span>
                        <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                            style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                            <li ng-repeat=" priority in priority"
                                ng-click="onSelectPriority(priority)"><a
                                    href="">{{priority}}</a>
                            </li>
                        </ul>
                    </div>
                </th>
                <th style="width: 150px;z-index: auto !important;">
                    <span ng-if="selectedPerson != null"
                          style="font-weight:normal;font-size: 13px;cursor: pointer !important;">
                            ({{selectedPerson.fullName}})
                            <i class="fa fa-times-circle" ng-click="clearAssignedTo()"
                               title="{{removeTitle}}"></i>
                    </span>
                    <br>
                    <div class="dropdown" uib-dropdown style="display: inline-block">
                        <span uib-dropdown-toggle><span translate>ASSIGNED_TO</span>
                            <i class="caret dropdown-toggle" style="margin-left: 5px;cursor: pointer;"></i>
                        </span>
                        <ul uib-dropdown-menu class="page-size-dropdown dropdown-menu dropdown-menu-right"
                            style="max-height:250px;overflow-y: auto;left:0;margin-top:5px;">
                            <li ng-repeat=" person in assignedTo"
                                ng-click="onSelectAssignedTo(person)"><a
                                    href="">{{person.fullName}}</a>
                            </li>
                        </ul>
                    </div>
                </th>
                <th style="width: 80px;">
                    <span translate>DUE_DATE</span>
                </th>
            </tr>
            </thead>
            <tbody>
            <tr ng-if="!requirementsVm.loading && requirementsVm.requirements.length == 0">
                <td colspan="12"><span translate>NO_REQUIREMENTS</span></td>
            </tr>
            <tr ng-if="requirementsVm.loading">
                <td colspan="12">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_REQUIREMENTS</span>
                        </span>
                </td>
            </tr>
            <tr id="{{requirement.id}}" ng-if="requirementsVm.requirements.length > 0"
                ng-repeat="requirement in requirementsVm.requirements"
                ng-mouseleave="requirementsVm.onMouseLeave()">
                <td>
                    <i ng-show="reqDocumentRevision.lifeCyclePhase.phaseType == 'PRELIMINARY'"
                       class="la la-plus row-actions-btn"
                       style="cursor: pointer" title="{{ 'ADD_NEW_REQUIREMENT' | translate}}"
                       ng-click="requirementsVm.newRequirement(requirement, requirement.requirementVersion)"></i>
                        <span uib-dropdown>
                            <i class="la la-gear row-actions-btn" style="cursor: pointer" dropdown-toggle
                               uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu" role="menu"
                                style="z-index: 9999 !important;">
                                <li title="{{requirement.requirementVersion.lifeCyclePhase.phaseType == 'RELEASED'? cannotDeleteApprovedReq:''}}">
                                    <a href=""
                                       ng-class="{'disabled':requirement.requirementVersion.lifeCyclePhase.phaseType == 'RELEASED'}"
                                       ng-click="requirementsVm.deleteRequirement(requirement)" translate>DELETE_REQUIREMENT</a>
                                </li>
                                <li><a href="" ng-click="requirementsVm.showReviewers(requirement.requirementVersion)"
                                    
                                       translate>REVIEWERS_AND_APPROVERS</a>
                                </li>
                                <li><a href="" ng-click="requirementsVm.showComments(requirement)" translate>SHOW_COMMENTS</a>
                                </li>
                                <li>
                                    <a ng-show="requirement.requirementVersion.master.ignoreReqBtn == true && requirement.requirementVersion.master.ignoreForRelease != true
                                     && requirement.assignedTo ==loginPersonDetails.person.id"
                                       href="" ng-click="requirementsVm.ignoreRequirement(requirement.master)"
                                       translate>IGNORE_FOR_RELEASE </a>
                                </li>
                                <plugin-table-actions context="req.all" object-value="requirement"></plugin-table-actions>
                            </ul>
                        </span>
                </td>
                <%--<td class="aligin-top">{{requirement.master.path}}</td>--%>
                <%--<td class="aligin-top">
                    <a href=""
                       title="Show requirement details"
                       ng-click="requirementsVm.showRequirementDetails(requirement)">{{requirement.master.number}}</a>
                </td>--%>
                <td class="aligin-top">
                    <div class="d-flex">
                        <div class="req-name req-seq">
                            {{requirement.requirementVersion.master.path}}
                        </div>
                        <div class="req-name wd-100p" ng-if="freeTextQuerys == null"
                             ng-model="requirement.requirementVersion.name"
                             contenteditable="false" title="{{ 'DOUBLE_CLICK_TO_EDIT' | translate }} "
                             ondblclick="this.contentEditable=true;this.focus()"
                             ng-blur="requirementsVm.destroyContentEditable($event, requirement.id, requirement.requirementVersion)"
                             ng-keypress="requirementsVm.preventEnterKey($event, requirement.requirementVersion)">
                        </div>
                        <div class="req-name wd-100p"
                             ng-if="freeTextQuerys != null"
                             ng-bind-html="requirement.requirementVersion.name | highlightText: freeTextQuerys"></div>
                        <div class="req-name req-number">
                            <a href=""
                               title="{{ 'SHOW_REQ_DETAILS' | translate }} "
                               ng-click="requirementsVm.showRequirementDetails(requirement)">
                                <span ng-bind-html="requirement.requirementVersion.master.number | highlightText: freeTextQuerys"></span></a>
                        </div>
                    </div>
                    <div class="req-desc" title="{{ 'DOUBLE_CLICK_TO_EDIT' | translate }} "
                         contenteditable="false" ng-model="requirement.requirementVersion.description"
                         ng-dblclick="requirementsVm.initCkeditor($event, requirement.id, requirement.requirementVersion)">
                    </div>
                </td>
                <td class="aligin-top">
                    <span ng-bind-html="requirement.requirementVersion.master.type.name"></span>
                </td>
                <td style="text-align: center;width: 40px">
                    <a href="" ng-click="requirementsVm.showReqVersionHistory(requirement.requirementVersion)"
                       title="{{'ITEM_DETAILS_VERSION_HISTORY' | translate}}">
                        {{requirement.requirementVersion.version}}
                    </a>
                </td>
                <td class="aligin-top">
                    <reqstatus object="requirement.requirementVersion"></reqstatus>
                </td>
                <td class="aligin-top">
                    <priority object="requirement.requirementVersion"></priority>
                </td>
                <td>
                    {{requirement.requirementVersion.assignedToObject.fullName}}
                </td>
                <td>
                    {{requirement.requirementVersion.plannedFinishDate}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>