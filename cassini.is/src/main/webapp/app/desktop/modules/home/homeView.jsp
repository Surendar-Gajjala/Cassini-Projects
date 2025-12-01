<style scoped>
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

    .cassini-panel {
        margin-bottom: 10px;
        border: 1px solid #ddd;
        position: relative;
        overflow: auto;
    }

    .cassini-panel-heading {
        padding: 1px;
        line-height: 50px;
        background-color: #fff;
        padding-left: 10px;
    }

    .cassini-panel-body {
        padding: 10px;
        overflow: auto;
    }

    .view-content {
        padding: 10px !important;
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

    #description {
        display: run-in;
        word-wrap: break-word;
        width: 300px;
        white-space: normal;
        text-align: left;
    }

    #name {
        display: run-in;
        word-wrap: break-word;
        width: 250px;
        white-space: normal;
        text-align: left;
    }

    .browse-control {
        -moz-border-radius: 3px;
        -webkit-border-radius: 3px;
        border-radius: 3px;
        padding: 5px;
        height: auto;
        -moz-box-shadow: none;
        -webkit-box-shadow: none;
        box-shadow: none;
        font-size: 13px;
        border: 1px solid #ccc;
    }

    .cassini-panel-body .responsive-table table thead th {
        position: -webkit-sticky;
        position: sticky;
        top: -10px;
        background-color: #fff;
    }

    table {
        table-layout: fixed;
    }

    .lockedProject {
        background: #ff6666 !important;
        color: black;
    }

    table td {
        vertical-align: middle !important;
    }

    .row-menu {
        cursor: pointer;
    }
</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-size: 20px;font-weight: bold;color: black;padding: 13px 10px 14px;vertical-align: middle;border-right: 1px solid lightgray;">{{viewInfo.title}}</span>
        <button class="btn btn-sm btn-success" ng-click="homeVm.showNewPortfolio(null,'NEW')"
                ng-disabled="hasPermission('permission.dashboard.newPortfolio') == false">New Portfolio
        </button>
        <button class="btn btn-sm btn-success" ng-click="homeVm.showNewProjectDialog()"
                ng-disabled="!(hasPermission('permission.dashboard.newProject') || hasPermission('permission.dashboard.newPortfolio'))">
            New Project
        </button>
        <button class="btn btn-sm btn-primary" ng-click="homeVm.showProjectAttributes()"
                ng-disabled="hasPermission('permission.dashboard.addAttributes') == false">Show Attributes
        </button>
    </div>
    <div class="view-content no-padding" style="overflow-y: auto; overflow-x: hidden;">
        <div ng-repeat="portfolio in homeVm.portfolios.content">
            <div class="cassini-panel">
                <div class="cassini-panel-heading">
                    <h4>
                        <a href="" ng-click="homeVm.showNewPortfolio(portfolio,'EDIT')"
                           title="Click to edit portfolio details">
                            {{portfolio.name}}</a>
                    </h4>
                </div>
                <hr style="border-top: 1px solid #dddddd;margin: 0;">
                <div class="cassini-panel-body">
                    <div class="responsive-table">
                        <table class='responsive-table table table-striped highlight-row'>
                            <thead>
                            <tr>
                                <th style="width: 150px;">Name</th>
                                <th style="width: 200px;">Description</th>
                                <th style="width: 150px;">Project Owner</th>
                                <th style="width: 120px;">Planned Start Date</th>
                                <th style="width: 120px;">Planned Finish Date</th>
                                <th style="width: 120px;">Percentage Complete</th>
                                <th style="width: 150px;"
                                    ng-repeat="requiredAttribute in homeVm.requiredProjectAttributes">
                                    {{requiredAttribute.name}}
                                </th>
                                <th style="width: 150px;" class='added-column'
                                    ng-repeat="projectAttribute in homeVm.projectAttributes">
                                    {{projectAttribute.name}}
                                    <i class="fa fa-times-circle" style="cursor: pointer;"
                                       ng-click="homeVm.removeAttribute(projectAttribute)"
                                       title="Remove this column"></i>
                                </th>
                                <th style="width:100px; text-align: center">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-if="homeVm.loadPortfolios == true">
                                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Portfolios</span>
                        </span>
                                </td>
                            </tr>

                            <tr ng-if="portfolio.loading == true">
                                <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span>Loading Projects</span>
                        </span>
                                </td>
                            </tr>
                            <tr ng-if="portfolio.projects.length == 0 && portfolio.loading == false">
                                <td colspan="7">No Projects</td>
                            </tr>

                            <tr ng-repeat="project in portfolio.projects">
                                <td id="name" ng-class="{'lockedProject':project.locked == true}"
                                    style="width: 150px;">
                                    <a href="" ng-click="homeVm.openProject(project)"
                                       title="Click to view Project details">
                                        {{project.name}}
                                    </a>
                                </td>
                                <td title="{{project.description}}" id="description"
                                    style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                    ng-class="{'lockedProject':project.locked == true}">
                                    {{project.description}}
                                </td>
                                <td style="width: 150px;display: run-in;white-space: normal;word-wrap: break-word;"
                                    ng-class="{'lockedProject':project.locked == true}">
                                    {{project.projectOwnerObject.fullName}}
                                </td>
                                <td style="width: 120px;" ng-class="{'lockedProject':project.locked == true}">
                                    {{project.plannedStartDate}}
                                </td>
                                <td style="width: 120px;" ng-class="{'lockedProject':project.locked == true}">
                                    {{project.plannedFinishDate}}
                                </td>
                                <td style="width: 150px;" ng-class="{'lockedProject':project.locked == true}">
                                    <div class="task-progress progress text-center">
                                        <div ng-if="project.percentComplete != 'NaN'"
                                             style="width:{{project.percentComplete}}%"
                                             class="progress-bar progress-bar-primary"
                                             role="progressbar" aria-valuenow="40" aria-valuemin="0"
                                             aria-valuemax="100">
                                            <span style="margin-left: 2px;">{{project.percentComplete}}%</span>
                                        </div>

                                    </div>
                                </td>
                                <td style="width: 150px;" class="added-column"
                                    ng-class="{'lockedProject':project.locked == true}"
                                    ng-repeat="objectAttribute in homeVm.requiredProjectAttributes">

                                    <div class="attributeTooltip"
                                         ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                          && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                                         ng-init="attrName = objectAttribute.name">
                                        <p>
                                            <a ng-if="project[attrName].length > 0" href="">
                                                {{project[attrName].length}} Attachments
                                            </a>
                                        </p>

                                        <div class="attributeTooltiptext">
                                            <ul>
                                                <li ng-repeat="attachment in project[attrName]">
                                                    <a href="" ng-click="homeVm.openAttachment(attachment)"
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
                                        <a href="" ng-click="homeVm.showRequiredImage(project[attrName])"
                                           title="Click to show large image">
                                            <img ng-if="project[attrName] != null"
                                                 ng-src="{{project[attrName]}}"
                                                 style="height: 30px;width: 40px;margin-bottom: 5px;">
                                        </a>

                                        <div id="myModal1" class="img-model modal">
                                            <span class="closeImage1">&times;</span>
                                            <img class="modal-content" id="img02">
                                        </div>
                                    </div>

                                    <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                                       ng-init="attrName = objectAttribute.name">
                                        {{project[attrName]}}
                                    </p>

                                    <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                                       ng-init="attrName = objectAttribute.name">
                                        {{project[attrName]}}
                                    </p>

                                    <span ng-init="currencyType = objectAttribute.name+'type'"
                                          ng-if="objectAttribute.dataType == 'CURRENCY'"
                                          ng-bind-html="project[currencyType]">
                                    </span>
                                    <span ng-init="attrName = objectAttribute.name"
                                          ng-if="objectAttribute.dataType == 'CURRENCY'">
                                           {{project[attrName]}}
                                    </span>
                                </td>
                                <td style="width: 150px;" class="added-column"
                                    ng-class="{'lockedProject':project.locked == true}"
                                    ng-repeat="objectAttribute in homeVm.projectAttributes">

                                    <div class="attributeTooltip"
                                         ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                         && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                                         ng-init="attrName = objectAttribute.name">
                                        <p>
                                            <a ng-if="project[attrName].length > 0" href="">
                                                {{project[attrName].length}} Attachments
                                            </a>
                                        </p>

                                        <div class="attributeTooltiptext">
                                            <ul>
                                                <li ng-repeat="attachment in project[attrName]">
                                                    <a href="" ng-click="homeVm.openAttachment(attachment)"
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
                                        <a href="" ng-click="homeVm.showImage(project[attrName])"
                                           title="Click to show large image">
                                            <img ng-if="project[attrName] != null"
                                                 ng-src="{{project[attrName]}}"
                                                 style="height: 30px;width: 40px;margin-bottom: 5px;">
                                        </a>

                                        <div id="homeModal" class="img-model modal">
                                            <span class="closeImage">&times;</span>
                                            <img class="modal-content" id="homeImg">
                                        </div>
                                    </div>

                                    <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                       ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                                       ng-init="attrName = objectAttribute.name" title="{{project[attrName]}}">
                                        {{project[attrName]}}
                                    </p>

                                    <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                                       ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                                       ng-init="attrName = objectAttribute.name" title="{{project[attrName]}}">
                                        {{project[attrName]}}
                                    </p>

                                    <span ng-init="currencyType = objectAttribute.name+'type'"
                                          ng-if="objectAttribute.dataType == 'CURRENCY'"
                                          ng-bind-html="project[currencyType]">
                        </span>
                                    <span ng-init="attrName = objectAttribute.name"
                                          ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{project[attrName]}}
                        </span>
                                </td>
                                <td class="text-center" ng-class="{'lockedProject':project.locked == true}"
                                    style="width: 100px;">
                                    <span class="row-menu" uib-dropdown dropdown-append-to-body>
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-click="!hasPermission('permission.dashboard.lockProject') || homeVm.lockProject(project)"
                                            ng-class="{'disabled':!hasPermission('permission.dashboard.lockProject')}"
                                            ng-if="!project.locked">
                                            <a class="dropdown-item" title="Lock Project">
                                                <span style="padding-left: 3px;">Lock</span>
                                            </a>
                                        </li>
                                        <li ng-click="!hasPermission('permission.dashboard.unLockProject') || homeVm.lockProject(project)"
                                            ng-class="{'disabled':!hasPermission('permission.dashboard.unLockProject')}"
                                            ng-if="project.locked">
                                            <a class="dropdown-item" title="Unlock Project">
                                                <span style="padding-left: 3px;">Unlock</span>
                                            </a>
                                        </li>
                                    </ul>
                                 </span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>



