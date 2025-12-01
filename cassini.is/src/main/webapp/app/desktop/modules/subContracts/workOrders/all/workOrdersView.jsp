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

</style>
<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <span style="font-size: 20px;font-weight: bold;color: black;padding: 13px 10px 14px;vertical-align: middle;border-right: 1px solid lightgray;">{{viewInfo.title}}</span>
        <button class="btn btn-sm btn-success" ng-click="workOrdersVm.newWorkOrder()"
                ng-disabled="!hasPermission('permission.workOrders.new')">New Work Order
        </button>
        <button class="btn btn-sm btn-primary" ng-click="workOrdersVm.showWorkOrderAttributes()">Show Attributes
        </button>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 150px;">Work Order Number</th>
                    <th style="width: 150px;">Contractor</th>
                    <th style="width: 150px;">Status</th>
                    <th style="width: 150px;">Project</th>
                    <th style="width: 150px;" ng-repeat="reqObjectAttribute in workOrdersVm.requiredObjectAttributes">
                        {{reqObjectAttribute.name}}
                    </th>

                    <th style="width: 150px;" class='added-column'
                        ng-repeat="objectAttribute in workOrdersVm.objectAttributes">
                        {{objectAttribute.name}}
                        <i class="fa fa-times-circle" style="cursor: pointer;"
                           ng-click="workOrdersVm.removeAttribute(objectAttribute)"
                           title="Remove this column"></i>
                    </th>
                    <th class="actions-col" style="width: 50px; text-align: center">Actions</th>
                </tr>
                </thead>
                <tbody>

                <tr ng-if="workOrdersVm.loading == true">
                    <td>
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Work Orders..
                        </span>
                    </td>
                </tr>

                <tr ng-if="workOrdersVm.loading == false && workOrdersVm.workOrders.content.length == 0">
                    <td colspan="12">No Work Orders are available to view</td>
                </tr>

                <tr ng-repeat="workOrder in workOrdersVm.workOrders.content" ng-if="workOrdersVm.loading == false">
                    <td style="width: 150px;"><a href="" ng-click="workOrdersVm.workOrderDetails(workOrder)"><span
                            title="Click to view workOrder details"
                            ng-bind-html="workOrder.number | highlightText: freeTextQuery"></span></a>
                    </td>
                    <td style="width: 150px;"><span
                            ng-bind-html="workOrder.contractorObject.name | highlightText: freeTextQuery"></span></td>
                    <td style="width: 150px;"><span class="label" style="color: white"
                                                    ng-class="{'label-warning': workOrder.status == 'PENDING','label-primary': workOrder.status == 'FINISHED'}">
                            {{workOrder.status}}  </span>
                    </td>
                    <td style="width: 150px;"><span title="{{workOrder.projectObject.name}}"> {{workOrder.projectObject.name | limitTo: 20}}{{workOrder.projectObject.name.length > 20 ? '...' : ''}}</span>
                    </td>
                    <%-- adding attribute values--%>
                    <td class="added-column" ng-repeat="objectAttribute in workOrdersVm.requiredObjectAttributes">

                        <div class="attributeTooltip"
                             ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">
                            <p>
                                <a ng-if="workOrder[attrName].length > 0" href="">
                                    {{workOrder[attrName].length}} Attachments
                                </a>
                            </p>

                            <div class="attributeTooltiptext">
                                <ul>
                                    <li ng-repeat="attachment in workOrder[attrName]">
                                        <a href="" ng-click="workOrdersVm.openAttachment(attachment)"
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
                            <a href="" ng-click="workOrdersVm.showImage(workOrder[attrName])"
                               title="Click to show large Image">
                                <img ng-if="workOrder[attrName] != null"
                                     ng-src="{{workOrder[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="woModal" class="img-model modal">
                                <span class="closeImage">&times;</span>
                                <img class="modal-content" id="woImg">
                            </div>

                        </div>

                        <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name">
                            {{workOrder[attrName]}}
                        </p>

                        <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                           ng-init="attrName = objectAttribute.name">
                            {{workOrder[attrName]}}
                        </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="workOrder[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{workOrder[attrName]}}
                        </span>
                    </td>
                    <td class="added-column" ng-repeat="objectAttribute in workOrdersVm.objectAttributes">

                        <div class="attributeTooltip"
                             ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                             ng-init="attrName = objectAttribute.name">
                            <p>
                                <a ng-if="workOrder[attrName].length > 0" href="">
                                    {{workOrder[attrName].length}} Attachments
                                </a>
                            </p>

                            <div class="attributeTooltiptext">
                                <ul>
                                    <li ng-repeat="attachment in workOrder[attrName]">
                                        <a href="" ng-click="workOrdersVm.openAttachment(attachment)"
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
                            <a href="" ng-click="workOrdersVm.showRequiredImage(workOrder[attrName])"
                               title="Click to show large image">
                                <img ng-if="workOrder[attrName] != null"
                                     ng-src="{{workOrder[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="woModal1" class="img-model modal">
                                <span class="closeImage1">&times;</span>
                                <img class="modal-content" id="woImg1">
                            </div>
                        </div>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name" title="{{workOrder[attrName]}}">
                            {{workOrder[attrName]}}
                        </p>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                           ng-init="attrName = objectAttribute.name" title="{{workOrder[attrName]}}">
                            {{workOrder[attrName]}}
                        </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="workOrder[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{workOrder[attrName]}}
                        </span>
                    </td>
                    <%-- end attributes values--%>
                    <td class="text-center" style="width: 50px;">
                        <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px"
                              ng-if=" workOrder.status != 'FINISHED' ">
                             <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                            <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                style="z-index: 9999 !important;">
                                <li ng-click="!hasPermission('permission.workOrders.delete') || workOrdersVm.deleteWorkOrder(workOrder)"
                                    ng-class="{'disabled':!hasPermission('permission.workOrders.delete')}">
                                    <a class="dropdown-item" title="Delete Work Order">
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
                    <h5 style="font-weight: 700">Displaying {{workOrdersVm.workOrders.numberOfElements}} of
                        {{workOrdersVm.workOrders.totalElements}}</h5>
                </div>

                <div class="text-right">
                    <span class="mr10">Page {{workOrdersVm.workOrders.totalElements != 0 ? workOrdersVm.workOrders.number+1:0}} of {{workOrdersVm.workOrders.totalPages}}</span>
                    <a href="" ng-click="workOrdersVm.previousPage()"
                       ng-class="{'disabled': workOrdersVm.workOrders.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="workOrdersVm.nextPage()"
                       ng-class="{'disabled': workOrdersVm.workOrders.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>
