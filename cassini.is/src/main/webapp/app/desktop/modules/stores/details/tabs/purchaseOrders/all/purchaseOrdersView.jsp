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

    .img-model .closeImage3 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage3:hover,
    .img-model .closeImage3:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    .img-model .closeImage2 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage2:hover,
    .img-model .closeImage2:focus {
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
<div class="responsive-table" style="padding: 10px;">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="min-width: 150px;">Purchase Order Number</th>
            <th style="min-width: 150px;">Raised Date</th>
            <th style="min-width: 150px;">Supplier</th>
            <th style="min-width: 150px;">Status</th>
            <th style="min-width: 150px;">Approved By</th>
            <th style="min-width: 150px;">Notes</th>
            <th style="min-width: 150px;" ng-repeat="reqObjectAttribute in purchaseOrdersVm.requiredObjectAttributes">
                {{reqObjectAttribute.name}}
            </th>

            <th style="min-width: 150px;" class='added-column'
                ng-repeat="objectAttribute in purchaseOrdersVm.objectAttributes">
                {{objectAttribute.name}}
                <i class="fa fa-times-circle" style="cursor: pointer;"
                   ng-click="purchaseOrdersVm.removeAttribute(objectAttribute)"
                   title="Remove this column"></i>
            </th>
        </tr>
        </thead>

        <tbody>
        <tr ng-if="purchaseOrdersVm.loading == true">
            <td colspan="11">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Purchase Orders..
                        </span>
            </td>
        </tr>

        <tr ng-if="purchaseOrdersVm.loading == false && purchaseOrdersList.content.length == 0">
            <td colspan="11">No Purchase Orders available to view</td>
        </tr>
        <tr ng-repeat="purchaseOrder in purchaseOrdersList.content">
            <td>
                <a ng-click="purchaseOrdersVm.openPurchaseOrderDetails(purchaseOrder)"
                   title="Click to show details">{{purchaseOrder.poNumber}}</a>
            </td>
            <td>
                {{purchaseOrder.poDate}}
            </td>
            <td>
                {{purchaseOrder.supplier}}
            </td>

            <td>
                        <span style="color: white" class="label" ng-class=" {
                                    'label-success': purchaseOrder.status == 'NEW',
                                    'label-info': purchaseOrder.status == 'APPROVED'}">
                            {{purchaseOrder.status}}
                        </span>
            </td>


            <td>
                {{purchaseOrder.approvedBy}}
            </td>
            <td title="{{purchaseOrder.notes}}">
                {{purchaseOrder.notes | limitTo: 12}}{{purchaseOrder.notes.length > 12 ? '...' : ''}}
            </td>
            <%-- adding attribute values--%>
            <td class="added-column" ng-repeat="objectAttribute in purchaseOrdersVm.requiredObjectAttributes">

                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                     ng-init="attrName = objectAttribute.name">
                    <p>
                        <a ng-if="purchaseOrder[attrName].length > 0" href="">
                            {{purchaseOrder[attrName].length}} Attachments
                        </a>
                    </p>

                    <div class="attributeTooltiptext">
                        <ul>
                            <li ng-repeat="attachment in purchaseOrder[attrName]">
                                <a href="" ng-click="purchaseOrdersVm.openAttachment(attachment)"
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
                    <a href="" ng-click="purchaseOrdersVm.showImage(purchaseOrder[attrName])"
                       title="Click to show large Image">
                        <img ng-if="purchaseOrder[attrName] != null"
                             ng-src="{{purchaseOrder[attrName]}}"
                             style="height: 30px;width: 40px;margin-bottom: 5px;">
                    </a>

                    <div id="poModal" class="img-model modal" style="z-index: 11">
                        <span class="closeImage">&times;</span>
                        <img class="modal-content" id="poImg">
                    </div>
                </div>

                <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.name">
                    {{purchaseOrder[attrName]}}
                </p>

                <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                   ng-init="attrName = objectAttribute.name">
                    {{purchaseOrder[attrName]}}
                </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="purchaseOrder[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{purchaseOrder[attrName]}}
                        </span>
            </td>
            <td class="added-column" ng-repeat="objectAttribute in purchaseOrdersVm.objectAttributes">

                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                     ng-init="attrName = objectAttribute.name">
                    <p>
                        <a ng-if="purchaseOrder[attrName].length > 0" href="">
                            {{purchaseOrder[attrName].length}} Attachments
                        </a>
                    </p>

                    <div class="attributeTooltiptext">
                        <ul>
                            <li ng-repeat="attachment in purchaseOrder[attrName]">
                                <a href="" ng-click="purchaseOrdersVm.openAttachment(attachment)"
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
                    <a href="" ng-click="purchaseOrdersVm.showRequiredImage(purchaseOrder[attrName])"
                       title="Click to show large Image">
                        <img ng-if="purchaseOrder[attrName] != null"
                             ng-src="{{purchaseOrder[attrName]}}"
                             style="height: 30px;width: 40px;margin-bottom: 5px;">
                    </a>

                    <div id="poModal1" class="img-model modal" style="z-index: 11">
                        <span class="closeImage3" style="margin-left: 85%;">&times;</span>
                        <img class="modal-content" id="poImg1">
                    </div>
                </div>

                <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                   ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.name" title="{{purchaseOrder[attrName]}}">
                    {{purchaseOrder[attrName]}}
                </p>

                <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                   ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                   ng-init="attrName = objectAttribute.name" title="{{purchaseOrder[attrName]}}">
                    {{purchaseOrder[attrName]}}
                </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="purchaseOrder[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{purchaseOrder[attrName]}}
                        </span>
            </td>
            <%-- end attributes values--%>
        </tr>
        </tbody>
    </table>
</div>


