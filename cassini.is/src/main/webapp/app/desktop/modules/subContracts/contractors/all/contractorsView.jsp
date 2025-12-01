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
        background-color: #fff;
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

    .dropdown-menu li:hover {
        background-color: #dddddd;
    }

    .dropdown:hover .dropdown-menu {
        display: block;
    }

    .img-model .closeImage13 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage13:hover,
    .img-model .closeImage13:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeImage12 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage12:hover,
    .img-model .closeImage12:focus {
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

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-size: 20px;font-weight: bold;color: black;padding: 13px 10px 14px;vertical-align: middle;border-right: 1px solid lightgray;">{{viewInfo.title}}</span>
        <button class="btn btn-sm btn-success" ng-click="contractorsVm.newContractor()"
                ng-disabled="!hasPermission('permission.contractors.new')">New Contractor
        </button>
        <button class="btn btn-sm btn-primary" ng-click="contractorsVm.showContractorAttributes()">Show Attributes
        </button>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width:150px;">Contractor Name</th>
                    <th style="width:150px;">Contact Person</th>
                    <th style="width:150px;text-align: center">Work Orders</th>
                    <th style="width:100px;text-align: center">Active</th>
                    <th style="width: 150px;" ng-repeat="reqObjectAttribute in contractorsVm.requiredObjectAttributes">
                        {{reqObjectAttribute.name}}
                    </th>

                    <th style="width: 150px;" class='added-column'
                        ng-repeat="objectAttribute in contractorsVm.objectAttributes">
                        {{objectAttribute.name}}
                        <i class="fa fa-times-circle" style="cursor: pointer;"
                           ng-click="contractorsVm.removeAttribute(objectAttribute)"
                           title="Remove this column"></i>
                    </th>
                    <th class="actions-col" style="width: 50px; text-align: center">Actions</th>
                </tr>
                </thead>
                <tbody>

                <tr ng-if="contractorsVm.loading == true">
                    <td colspan="12">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Contractors..
                        </span>
                    </td>
                </tr>

                <tr ng-if="contractorsVm.loading == false && contractorsVm.contractors.content.length == 0">
                    <td colspan="11">No Contractors are available to view</td>
                </tr>

                <tr ng-repeat="contractor in contractorsVm.contractors.content">
                    <td style="width: 150px;"><a href="" ng-click="contractorsVm.contractorDetails(contractor)"><span
                            title="Click to view contractor details"
                            ng-bind-html="contractor.name | highlightText: freeTextQuery"></span></a>
                    </td>
                    <td style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"><span
                            ng-bind-html="contractor.contactObject.fullName | highlightText: freeTextQuery"></span></td>
                    <td style="width: 150px; text-align: center; cursor: pointer">
                        <a title="click to view work orders"
                           ng-if="contractor.workOrders.length > 0"
                           uib-popover-template="contractorsVm.workOrderListPopover.templateUrl"
                           popover-append-to-body="true" popover-popup-delay="50"
                           popover-placement="bottom-left" popover-title="Work Orders"
                           popover-trigger="'outsideClick'"><span>{{contractor.workOrders.length}}</span></a>
                    </td>
                    <td style="text-align: center" ng-if="contractor.active">Yes</td>
                    <td style="text-align: center" ng-if="!contractor.active">No</td>
                    <%-- adding attribute values--%>
                    <td class="added-column" ng-repeat="objectAttribute in contractorsVm.requiredObjectAttributes">

                        <div class="attributeTooltip"
                             ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">
                            <p>
                                <a ng-if="contractor[attrName].length > 0" href="">
                                    {{contractor[attrName].length}} Attachments
                                </a>
                            </p>

                            <div class="attributeTooltiptext">
                                <ul>
                                    <li ng-repeat="attachment in contractor[attrName]">
                                        <a href="" ng-click="contractorsVm.openAttachment(attachment)"
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
                            <a href="" ng-click="contractorsVm.showImage(contractor[attrName])"
                               title="Click to show large Image">
                                <img ng-if="contractor[attrName] != null"
                                     ng-src="{{contractor[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                        </div>

                        <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name">
                            {{contractor[attrName]}}
                        </p>

                        <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                           ng-init="attrName = objectAttribute.name">
                            {{contractor[attrName]}}
                        </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="contractor[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{contractor[attrName]}}
                        </span>
                    </td>
                    <td class="added-column" ng-repeat="objectAttribute in contractorsVm.objectAttributes">

                        <div class="attributeTooltip"
                             ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">
                            <p>
                                <a ng-if="contractor[attrName].length > 0" href="">
                                    {{contractor[attrName].length}} Attachments
                                </a>
                            </p>

                            <div class="attributeTooltiptext">
                                <ul>
                                    <li ng-repeat="attachment in contractor[attrName]">
                                        <a href="" ng-click="contractorsVm.openAttachment(attachment)"
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
                            <a href="" ng-click="contractorsVm.showImage(contractor[attrName])"
                               title="Click to show large Image">
                                <img ng-if="contractor[attrName] != null"
                                     ng-src="{{contractor[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>
                        </div>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name" title="{{contractor[attrName]}}">
                            {{contractor[attrName]}}
                        </p>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                           ng-init="attrName = objectAttribute.name" title="{{contractor[attrName]}}">
                            {{contractor[attrName]}}
                        </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="contractor[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{contractor[attrName]}}
                        </span>

                        <div id="conModal" class="img-model modal">
                            <span class="closeImage7">&times;</span>
                            <img class="modal-content" id="conImg">
                        </div>
                    </td>
                    <%-- end attributes values--%>
                    <td class="text-center" style="width: 50px;">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                            <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-click="!hasPermission('permission.contractors.delete') || contractorsVm.deleteContractor(contractor)"
                                    ng-class="{'disabled':!hasPermission('permission.contractors.delete')}">
                                    <a class="dropdown-item" title="Delete Contractor">
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
                    <h5 style="font-weight: 700">Displaying {{contractorsVm.contractors.numberOfElements}} of
                        {{contractorsVm.contractors.totalElements}}</h5>
                </div>

                <div class="text-right">
                    <span class="mr10">Page {{contractorsVm.contractors.totalElements != 0 ? contractorsVm.contractors.number+1:0}} of {{contractorsVm.contractors.totalPages}}</span>
                    <a href="" ng-click="contractorsVm.previousPage()"
                       ng-class="{'disabled': contractorsVm.contractors.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="contractorsVm.nextPage()"
                       ng-class="{'disabled': contractorsVm.contractors.last}"><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>
