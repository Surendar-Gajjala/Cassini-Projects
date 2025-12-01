<div style="display: flex; flex-direction: row;height: 440px">
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

        .reqstemp-table {
            flex: 1;
            height: 100%;
            position: unset !important;
            padding: 10px 0 0 0 !important;
            scroll-behavior: smooth;
        }

        .reqstemp-table .req-name {
            font-size: 15px;
            font-weight: 600;
            color: #0b58a2;
            padding: 3px 5px;
            white-space: nowrap;
            cursor: pointer;
        }

        .reqstemp-table .req-name[contenteditable=true],
        .reqstemp-table .req-desc[contenteditable=true] {
            background-color: #fff;
        }

        /*
        [contenteditable]:focus {
            outline: 0px solid transparent;
        }

        .reqstemp-table .req-name[contenteditable="true"] {
            border: 1px dotted #ddd;
        }*/

        .reqstemp-table .req-name a {
            color: #0b58a2;
        }

        .reqstemp-table .req-desc {
            white-space: pre-wrap;
            word-break: break-word !important;
            padding-left: 8.4px;
            cursor: pointer;
            padding-bottom: 7px;
        }

        .reqstemp-table .req-type {

        }

        .reqstemp-table .req-status {

        }

        .reqstemp-table .req-desc p:last-child {
            margin-bottom: 0;
        }

        .reqstemp-table .aligin-top {
            vertical-align: initial !important;
        }

        .reqstemp-table td .row-actions-btn {
            visibility: hidden;
        }

        .reqstemp-table tbody tr td .la-times {
            font-size: 17px;
        }

        .reqstemp-table tbody tr:hover td .row-actions-btn {
            visibility: visible;
        }

        .reqstemp-table tbody tr:hover > td {
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

        .reqstemptree-container {
            flex: 1;
            max-width: 400px;
            border-right: 1px solid #ddd;
        }

        .reqstemptree-container .reqstemp-tree {
            position: absolute;
            left: 0;
            top: 10px;
            bottom: 0;
            padding-left: 0;
            margin: 0;
            width: 400px;
            overflow-y: auto;
        }

        .reqstemptree-container .reqstemp-tree .tree-indent,
        .reqstemptree-container .reqstemp-tree .tree-folder,
        .reqstemptree-container .reqstemp-tree .tree-file {
            height: 16px !important;
        }

        .reqstemptree-container .reqstemp-tree .tree-title {
            font-size: 13.5px;
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
        #descriptionColumn{
            width:390px;
        }

    </style>
    <div class="reqstemptree-container">
        <ul id="requirementsTree" class="easyui-tree reqstemp-tree"></ul>
    </div>
    <div id="requirementsTemplateTable" class='responsive-table reqstemp-table'>
        <table class='table'>
            <thead>
            <tr>
                <th style="width:50px;">
                    <i class="la la-plus"
                       style="cursor: pointer" title="{{ 'ADD_NEW_REQUIREMENT' | translate}}"
                       ng-click="reqDocTemplateRequirementVm.newRequirement(null)"></i>
                </th>
                <th id="descriptionColumn" style=""><span style="margin-left: 8.4px;" translate>NAME</span>/<span
                        translate>DESCRIPTION</span></th>
                <th class="col-width-150" translate>TYPE</th>
                <th style="width: 100px;" translate>STATUS</th>
                <th style="width: 100px;" translate>PRIORITY</th>
                <th class="col-width-150">
                    <span translate>ASSIGNED_TO</span>
                </th>

            </tr>
            </thead>
            <tbody>
            <tr ng-if="!reqDocTemplateRequirementVm.loading && reqDocTemplateRequirementVm.requirements.length == 0">
                <td colspan="12"><span translate>NO_REQUIREMENTS</span></td>
            </tr>
            <tr ng-if="reqDocTemplateRequirementVm.loading">
                <td colspan="12">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_REQUIREMENTS</span>
                        </span>
                </td>
            </tr>
            <tr id="{{requirement.id}}" ng-if="reqDocTemplateRequirementVm.requirements.length > 0"
                ng-repeat="requirement in reqDocTemplateRequirementVm.requirements"
                ng-mouseleave="reqDocTemplateRequirementVm.onMouseLeave()">
                <td>
                    <i class="la la-plus row-actions-btn"
                       style="cursor: pointer" title="{{ 'ADD_NEW_REQUIREMENT' | translate}}"
                       ng-click="reqDocTemplateRequirementVm.newRequirement(requirement)"></i>
                        <span uib-dropdown>
                            <i class="la la-gear row-actions-btn" style="cursor: pointer" dropdown-toggle
                               uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu" role="menu"
                                style="z-index: 9999 !important;">
                                <li title="{{requirement.master.lifeCyclePhase.phaseType == 'RELEASED'? cannotDeleteApprovedReq:''}}">
                                    <a href=""
                                       ng-click="reqDocTemplateRequirementVm.deleteRequirement(requirement)" translate>DELETE_REQUIREMENT</a>
                                </li>
                                <li><a href=""
                                       ng-click="reqDocTemplateRequirementVm.showReqTemplateReviewers(requirement)"
                                       translate>REVIEWERS_AND_APPROVERS</a></li>
                                <li><a href="" ng-click="reqDocTemplateRequirementVm.showComments(requirement)"
                                       translate>SHOW_COMMENTS</a>
                                </li>
                            </ul>
                        </span>
                </td>
                <td class="aligin-top">
                    <div class="d-flex">
                        <div class="req-name req-seq"
                             title="{{ 'SHOW_REQ_DETAILS' | translate }} "
                             ng-click="reqDocTemplateRequirementVm.showRequirementDetails(requirement)">
                            {{requirement.path}}
                        </div>
                        <div class="req-name wd-100p" ng-if="freeTextQuerys == null"
                             ng-model="requirement.name"
                             contenteditable="false" title="{{ 'DOUBLE_CLICK_TO_EDIT' | translate }} "
                             ondblclick="this.contentEditable=true;this.focus()"
                             ng-blur="reqDocTemplateRequirementVm.destroyContentEditable($event, requirement)"
                             ng-keypress="reqDocTemplateRequirementVm.preventEnterKey($event, requirement)">
                        </div>
                        <div class="req-name wd-100p"
                             ng-if="freeTextQuerys != null"
                             ng-click="reqDocTemplateRequirementVm.showRequirementDetails(requirement)"
                             ng-bind-html="requirement.name | highlightText: freeTextQuerys"></div>
                    </div>
                    <div class="req-desc" title="{{ 'DOUBLE_CLICK_TO_EDIT' | translate }} "
                         contenteditable="false" ng-model="requirement.description"
                         ng-dblclick="reqDocTemplateRequirementVm.initCkeditor($event, requirement)">
                    </div>
                </td>
                <td class="aligin-top" >
                    <span ng-bind-html="requirement.type.name | highlightText: freeTextQuerys"></span>
                </td>
                <td class="aligin-top">
                    <reqstatus object="requirement"></reqstatus>
                </td>
                <td class="aligin-top">
                    <priority object="requirement"></priority>
                </td>
                <td class="col-width-150">
                    {{requirement.assignedToObject.fullName}}
                </td>

            </tr>
            </tbody>
        </table>
    </div>
</div>