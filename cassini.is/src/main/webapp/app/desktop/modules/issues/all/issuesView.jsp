<style>

    table .ui-select-choices {
        position: absolute !important;;
        top: auto !important;
        left: auto !important;
        width: auto !important;
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

    .added-column:hover i {
        display: inline-block;
    }

    .responsive-table {
        width: 100%;
        margin-bottom: 0;
        padding-bottom: 20px;
        overflow-y: visible;
        overflow-x: visible;
    }

    .img-model .closeImage {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage:hover,
    .img-model .closeImage:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeImage1 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
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

    .img-model.modal {
        display: none; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 100px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0, 0, 0); /* Fallback color */
        background-color: rgba(0, 0, 0, 0.4); /* Black w/ opacity */
    }

    .img-model.modal1 {
        display: block; /* Hidden by default */
    }

    /* Modal Content (image) */
    .img-model .modal-content {
        margin: auto;
        display: block;
        height: 90%;
        width: 60%;
        /*max-width: 70%;*/
    }

    /* Caption of Modal Image */
    .img-model #caption {
        margin: auto;
        display: block;
        width: 80%;
        max-width: 700px;
        text-align: center;
        color: #ccc;
        padding: 10px 0;
        height: 150px;
    }

    /* Add Animation */
    .img-model .modal-content, #caption {
        -webkit-animation-name: zoom;
        -webkit-animation-duration: 0.6s;
        animation-name: zoom;
        animation-duration: 0.6s;
    }

    .view-content {
        position: relative;
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
        top: -15px;
        background-color: #fff;
    }

    .view-content .responsive-table table tbody #search {
        position: -webkit-sticky;
        position: sticky;
        top: 25px;
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

    .ui-select-bootstrap > .ui-select-match > .btn {
        text-align: left !important;
        line-height: 15px;
    }

    .ui-select-container .ui-select-placeholder {
        width: 50px !important;
    }

    .glyphicon-remove :hover {
        color: red;;
    }

    .caret :hover {
        color: red;;
    }

    table .ui-select-choices {
        position: absolute !important;
        top: auto !important;
        left: auto !important;
        width: auto !important;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    th.actions-column, td.actions-column {
        width: 150px;
        text-align: center;
    }

    .responsive-table .dropdown-content {
        display: none;
        position: absolute;
        background-color: #fff !important;
        min-width: 100px;
        box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
        z-index: 1 !important;
        border-radius: 5px;
        text-align: left;
        color: black !important;
        margin-top: -20px;

    }

    .responsive-table .dropdown-content a {
        text-decoration: none;
        display: block;
        color: black !important;
    }

    .responsive-table .dropdown-content a:hover {
        background-color: #0f3ff3;
        color: white !important;

    }

    .responsive-table .dropdown-content i:hover {
        background-color: #0f3ff3;
        color: white !important;

    }

    th.actions-column, td.actions-column {
        width: 150px;
        text-align: center;
    }

    .responsive-table .dropdown:hover .dropdown-content {
        display: block;
        color: black !important;
    }

    .responsive-table .dropdown-content {
        margin-left: -35px !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }
</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="row">
            <div class="col-sm-3">
                <button class="btn btn-sm btn-success" ng-click="issuesVm.newIssue()"
                        ng-disabled="selectedProject.locked == true || hasPermission ('permission.issues.add') == false">
                    New Problem
                </button>
                <button class="btn btn-sm btn-primary" ng-click="issuesVm.showProblemAttributes()"
                        ng-disabled="selectedProject.locked == true || hasPermission('permission.issues.problemAttributes') == false">
                    Show Attributes
                </button>
            </div>
        </div>

        <free-text-search on-clear="issuesVm.resetPage"
                          on-search="issuesVm.freeTextSearch"></free-text-search>
    </div>
    <div class="view-content no-padding" style="padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">

            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Assigned To</th>
                    <th>Status</th>
                    <th>Task</th>
                    <th>Priority</th>
                    <th>Reported By</th>
                    <th style="width: 150px;" ng-repeat="reqObjectAttribute in issuesVm.requiredObjectAttributes">
                        {{reqObjectAttribute.name}}
                    </th>

                    <th style="width: 150px;" class='added-column'
                        ng-repeat="objectAttribute in issuesVm.objectAttributes">
                        {{objectAttribute.name}}
                        <i class="fa fa-times-circle" style="cursor: pointer;"
                           ng-click="issuesVm.removeAttribute(objectAttribute)"
                           title="Remove this column"></i>
                    </th>
                    <th style="text-align: center; width: 100px;">Actions</th>
                </tr>
                </thead>
                <tbody>

                <tr ng-if="issuesVm.loading == true">
                    <td colspan="12">
                        <span style="font-size: 15px;">
                            <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Problems...
                        </span>
                    </td>
                </tr>
                <tr ng-if="issuesVm.issues.content.length == 0 && issuesVm.loading == false">
                    <td colspan="12">No Problems are available to view</td>
                </tr>

                <tr ng-repeat="issue in issuesVm.issues.content" ng-if="issuesVm.loading == false">
                    <td><a href="" ng-click="issuesVm.openIssue(issue)" title="Click to open problem details"><span
                            ng-bind-html="issue.title | highlightText: freeTextQuery"></span></a></td>
                    <td><span
                            ng-bind-html="issue.description.length > 50 ? issue.description.trunc(50,true) : issue.description | highlightText: freeTextQuery"></span>
                    </td>
                    <td>{{issue.assignedToObject.fullName}}</td>
                    <td>
                        <span class="label" ng-class="{
                                    'label-success': issue.status == 'NEW',
                                    'label-info': issue.status == 'ASSIGNED',
                                    'label-warning': issue.status == 'INPROGRESS',
                                    'label-danger': issue.status == 'CLOSED'}">
                            {{issue.status}}
                        </span>
                    </td>
                    <td><a ng-click="issuesVm.taskDetails(issue.task)">
                        {{issue.taskObject.name}}</a></td>
                    <td>
                        <span class="label" ng-class="{
                                    'label-info': issue.priority == 'LOW',
                                    'label-warning': issue.priority == 'MEDIUM',
                                    'label-danger': issue.priority == 'HIGH'}">
                            {{issue.priority}}
                        </span>
                    </td>
                    <td>{{issue.createdByObject.fullName}}</td>

                    <%-- adding attribute values--%>
                    <td class="added-column" ng-repeat="objectAttribute in issuesVm.requiredObjectAttributes">

                        <div class="attributeTooltip"
                             ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">
                            <p>
                                <a ng-if="issue[attrName].length > 0" href="">
                                    {{issue[attrName].length}} Attachments
                                </a>
                            </p>

                            <div class="attributeTooltiptext">
                                <ul>
                                    <li ng-repeat="attachment in issue[attrName]">
                                        <a href="" ng-click="issuesVm.openAttachment(attachment)"
                                           title="Click to download file"
                                           style="margin-bottom: 5px;width:200px;display: run-in;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                            {{attachment.name}}
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
                             ng-init="attrName = objectAttribute.name">
                            <a href="" ng-click="issuesVm.showImage(issue[attrName])"
                               title="Click to show large Image">
                                <img ng-if="issue[attrName] != null"
                                     ng-src="{{issue[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="probModal" class="img-model modal" style="z-index: 11">
                                <span class="closeImage">&times;</span>
                                <img class="modal-content" id="probImg">
                            </div>

                        </div>

                        <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name">
                            {{issue[attrName]}}
                        </p>

                        <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                           ng-init="attrName = objectAttribute.name">
                            {{issue[attrName]}}
                        </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="issue[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{issue[attrName]}}
                        </span>
                    </td>
                    <td class="added-column" ng-repeat="objectAttribute in issuesVm.objectAttributes">

                        <div class="attributeTooltip"
                             ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">
                            <p>
                                <a ng-if="issue[attrName].length > 0" href="">
                                    {{issue[attrName].length}} Attachments
                                </a>
                            </p>

                            <div class="attributeTooltiptext">
                                <ul>
                                    <li ng-repeat="attachment in issue[attrName]">
                                        <a href="" ng-click="issuesVm.openAttachment(attachment)"
                                           title="Click to download file"
                                           style="margin-bottom: 5px;width:200px;display: run-in;white-space: normal;word-wrap: break-word;color: #141f9f !important;">
                                            {{attachment.name}}
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
                             ng-init="attrName = objectAttribute.name">
                            <a href="" ng-click="issuesVm.showRequiredImage(issue[attrName])"
                               title="Click to show large Image">
                                <img ng-if="issue[attrName] != null"
                                     ng-src="{{issue[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="probModal1" class="img-model modal">
                                <span class="closeImage1">&times;</span>
                                <img class="modal-content" id="probImg1">
                            </div>
                        </div>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name" title="{{issue[attrName]}}">
                            {{issue[attrName]}}
                        </p>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                           ng-init="attrName = objectAttribute.name" title="{{issue[attrName]}}">
                            {{issue[attrName]}}
                        </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="issue[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{issue[attrName]}}
                        </span>
                    </td>
                    <%-- end attributes values--%>

                    <td class="text-center" style="text-align: center;">
                            <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-click="!(hasPermission('permission.issues.delete') || login.person.isProjectOwner) || issuesVm.deleteProjectIssue(issue)"
                                            ng-class="{'disabled':!(hasPermission('permission.issues.delete') || login.person.isProjectOwner)}">
                                            <a class="dropdown-item" title="Delete this problem">
                                                <span style="padding-left: 3px;">Delete</span>
                                            </a>
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
                    <h5 style="font-weight: 700">Displaying {{issuesVm.issues.numberOfElements}} of
                        {{issuesVm.issues.totalElements}}</h5>
                </div>

                <div class="text-right">
                    <span class="mr10">Page {{issuesVm.issues.totalElements != 0 ? issuesVm.issues.number+1:0}} of {{issuesVm.issues.totalPages}}</span>
                    <a href="" ng-click="issuesVm.previousPage()"
                       ng-class="{'disabled': issuesVm.issues.first}"><i class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="issuesVm.nextPage()"
                       ng-class="{'disabled': issuesVm.issues.last}"><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>
