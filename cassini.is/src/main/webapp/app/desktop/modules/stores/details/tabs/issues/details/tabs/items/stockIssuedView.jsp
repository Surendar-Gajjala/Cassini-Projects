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

    .table {
        border-spacing: 0;
        border-collapse: unset;
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

    .table {
        border-spacing: 0;
        border-collapse: unset;
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
<div class="responsive-table">
    <table class="table table-striped highlight-row">
        <thead>
        <tr>
            <th style="min-width: 150px;">Item Number</th>
            <th style="min-width: 150px;">Item Name</th>
            <th style="min-width: 150px;">Type</th>
            <th style="min-width: 250px;">Item Description</th>
            <th style="min-width: 100px;">Units</th>
            <th style="min-width: 150px;text-align: center">Issued Qty</th>
            <th style="min-width: 150px;" ng-repeat="reqObjectAttribute in storeStockIssuedVm.requiredObjectAttributes">
                {{reqObjectAttribute.name}}
            </th>

            <th style="min-width: 150px;" class='added-column'
                ng-repeat="objectAttribute in storeStockIssuedVm.objectAttributes">
                {{objectAttribute.name}}
                <i class="fa fa-times-circle" style="cursor: pointer;"
                   ng-click="storeStockIssuedVm.removeAttribute(objectAttribute)"
                   title="Remove this column"></i>
            </th>
            <th style="width: 50px; text-align: center">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-if="stockIssueItemsList.content.length == 0">
            <td colspan="11">No Items</td>
        </tr>
        <tr data-ng-repeat="item in stockIssueItemsList.content">
            <td style="width: 150px; text-align: left">
                <%-- <a href="" ng-click="storeStockIssuedVm.showItemDetails(item)" title="Click to show Details">--%>{{item.itemDTO.itemNumber}}<%--</a>--%>
            </td>
            <td style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.itemDTO.itemName}}">
                <span>{{item.itemDTO.itemName}}</span>

            </td>
            <td style="width: 150px; text-align: left">
                <span>{{item.itemDTO.itemType}}</span>

            </td>
            <td ng-if="item.itemDTO.description == null">
                {{item.itemDTO.description}}
            </td>
            <td ng-if="item.itemDTO.description != null"
                style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                title="{{item.itemDTO.description}}">
                <span>{{item.itemDTO.description}}</span>

            </td>

            <td style="width: 150px; text-align: left">
                {{item.itemDTO.units}}
            </td>

            <td style="width: 150px; text-align: center">
                <span>{{item.quantity}}</span>

            </td>

            <%-- adding attribute values--%>
            <td class="added-column" ng-repeat="objectAttribute in storeStockIssuedVm.requiredObjectAttributes">

                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                     ng-init="attrName = objectAttribute.name">
                    <p>
                        <a ng-if="item[attrName].length > 0" href="">
                            {{item[attrName].length}} Attachments
                        </a>
                    </p>

                    <div class="attributeTooltiptext">
                        <ul>
                            <li ng-repeat="attachment in item[attrName]">
                                <a href="" ng-click="storeStockIssuedVm.openAttachment(attachment)"
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
                    <a href="" ng-click="storeStockIssuedVm.showImage(item[attrName])"
                       title="Click to show large Image">
                        <img ng-if="item[attrName] != null"
                             ng-src="{{item[attrName]}}"
                             style="height: 30px;width: 40px;margin-bottom: 5px;">
                    </a>

                    <div id="issueItemModal" class="img-model modal" style="z-index: 11;">
                        <span class="closeImage">&times;</span>
                        <img class="modal-content" id="issueItemImg">
                    </div>
                </div>

                <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.name">
                    {{item[attrName]}}
                </p>

                <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                   ng-init="attrName = objectAttribute.name">
                    {{item[attrName]}}
                </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="item[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{item[attrName]}}
                        </span>
            </td>
            <td class="added-column" ng-repeat="objectAttribute in storeStockIssuedVm.objectAttributes">

                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                     ng-init="attrName = objectAttribute.name">
                    <p>
                        <a ng-if="item[attrName].length > 0" href="">
                            {{item[attrName].length}} Attachments
                        </a>
                    </p>

                    <div class="attributeTooltiptext">
                        <ul>
                            <li ng-repeat="attachment in item[attrName]">
                                <a href="" ng-click="storeStockIssuedVm.openAttachment(attachment)"
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
                    <a href="" ng-click="storeStockIssuedVm.showRequiredImage(item[attrName])"
                       title="Click to show large image">
                        <img ng-if="item[attrName] != null"
                             ng-src="{{item[attrName]}}"
                             style="height: 30px;width: 40px;margin-bottom: 5px;">
                    </a>

                    <div id="issueItemModal1" class="img-model modal" style="z-index: 11">
                        <span class="closeImage1" style="margin-left: 85%;">&times;</span>
                        <img class="modal-content" id="issueItemImg1">
                    </div>
                </div>

                <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                   ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.name" title="{{item[attrName]}}">
                    {{item[attrName]}}
                </p>

                <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                   ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                   ng-init="attrName = objectAttribute.name" title="{{item[attrName]}}">
                    {{item[attrName]}}
                </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="item[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{item[attrName]}}
                        </span>
            </td>
            <%-- end attributes values--%>

            <td style="text-align: center;">
                <i ng-if="!item.itemDTO.editAttribute && hasPermission('permission.stockIssues.edit')"
                   title="addAttribute" class="fa fa-pencil" style="color:#5e6258; font-size: 15px;"
                   ng-click="storeStockIssuedVm.showItemAttributes(item,'ISSUEITEM')" aria-hidden="true"></i>
                <i ng-if="item.itemDTO.editAttribute" style="color: #252721; font-size: 15px;" title="editAttribute"
                   class="fa fa-pencil" ng-click="storeStockIssuedVm.editItemAttributes(item,'ISSUEITEM')"
                   aria-hidden="true"></i>
            </td>

        </tr>
        </tbody>
    </table>
</div>
