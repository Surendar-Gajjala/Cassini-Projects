<style>
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
        top: -15px;
        background-color: #fff;
    }

    .view-content .responsive-table table tbody #search {
        position: -webkit-sticky;
        position: sticky;
        z-index: 5;
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

    .responsive-table .dropdown-content {
        display: none;
        position: absolute;
        background-color: #fff !important;
        min-width: 100px;
        box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
        z-index: 1 !important;
        border-radius: 5px;
        text-align: left;
        margin-left: -50px;
        color: black !important;
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
        margin-left: 1px !important;
    }

    #freeTextSearchDirective {
        top: 7px !important;
    }

</style>
<div class="view-container" fitcontent>
    <style scoped>
        .dropdown, .dropup {
            position: inherit;
        }
    </style>
    <div class="view-toolbar">
        <button class="btn btn-sm btn-success" ng-click="materialsVm.showNewMaterial()"
                ng-disabled="hasPermission('permission.materials.newMaterial') == false">New Material
        </button>
        <button class="btn btn-sm btn-primary" ng-click="materialsVm.showMaterialAttributes()"
                ng-disabled="hasPermission('permission.materials.addAttributes') == false">Show Attributes
        </button>
        <button class="min-width btn btn-sm btn-success"
                ng-click="materialsVm.materialAttributeSearch()">Attribute Search
        </button>
        <button ng-show="materialsVm.attributeSearch"
                class="min-width btn btn-sm btn-success"
                ng-click="materialsVm.clearMaterialAttributeSearch()">Clear Search
        </button>
        <%-- <p class="blink" ng-if="materialsVm.showSearchMode == true"
            style="color: blue;text-align: center;margin-top: -25px;font-size: 16px;">View is in search mode</p>--%>
        <free-text-search id="freeTextId" class="img-model modal1" on-clear="materialsVm.resetPage"
                          on-search="materialsVm.freeTextSearch"></free-text-search>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div class="responsive-table" style="padding: 10px;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="min-width: 150px;">Material Number</th>
                    <th style="min-width: 150px;">Material Type</th>
                    <th style="min-width: 150px;">Material Name</th>
                    <th style="min-width: 150px;">Description</th>
                    <th style="min-width: 100px">Units</th>
                    <th style="min-width: 150px;"
                        ng-repeat="materialAttribute in materialsVm.requiredTrueMaterialAttributes">
                        {{materialAttribute.name}}
                    </th>
                    <th style="min-width: 150px;"
                        ng-repeat="materialAttribute in materialsVm.requiredTrueMaterialTypeAttributes">
                        {{materialAttribute.name}}
                    </th>
                    <th style="min-width: 150px;" class='added-column'
                        ng-repeat="materialAttribute in materialsVm.materialAttributes">
                        {{materialAttribute.name}}
                        <i class="fa fa-times-circle" style="cursor: pointer;"
                           ng-click="materialsVm.removeAttribute(materialAttribute)"
                           title="Remove this column"></i>
                    </th>
                    <th<%-- class="actions-col"--%> style="width: 150px;" class="actions-column">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="materialsVm.loading == true">
                    <td colspan="25">
                                    <span style="font-size: 15px;">
                                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                             class="mr5">Loading items...
                                    </span>
                    </td>
                </tr>

                <tr ng-if="materialsVm.loading == false && materialsVm.materials.content.length == 0">
                    <td colspan="25">No Materials are available to view</td>
                </tr>

                <tr ng-repeat="item in materialsVm.materials.content" ng-if="!materialsVm.loading">
                    <td style="vertical-align: middle;">
                        <a href="" ng-click="materialsVm.showMaterialDetails(item)"
                           title="Click to view Material details">
                            <span ng-bind-html="item.itemNumber | highlightText: freeTextQuery"></span></a>
                    </td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="item.itemType.name | highlightText: freeTextQuery"></span></td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="item.itemName.length > 30 ? item.itemName.trunc(30, true) : item.itemName | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="item.description.length > 30 ? item.description.trunc(30, true) : item.description | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="item.units | highlightText: freeTextQuery"></span></td>
                    <%--<td style="width: 150px">{{item.createdByObject.firstName}}</td>--%>
                    <%--<td style="width: 150px">{{item.createdDate}}</td>--%>
                    <%--<td style="width: 150px">{{item.modifiedByObject.firstName}}</td>--%>
                    <%--<td style="width: 150px">{{item.modifiedDate}}</td>--%>

                    <td class="added-column"
                        ng-repeat="objectAttribute in materialsVm.requiredTrueMaterialAttributes">

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
                                        <a href="" ng-click="materialsVm.openAttachment(attachment)"
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
                            <a href="" ng-click="materialsVm.showImage(item[attrName])"
                               title="Click to show large Image">
                                <img ng-if="item[attrName] != null"
                                     ng-src="{{item[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>


                        </div>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name">
                            {{item[attrName]}}
                        </p>

                        <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
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
                    <td class="added-column"
                        ng-repeat="objectAttribute in materialsVm.requiredTrueMaterialTypeAttributes">

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
                                        <a href="" ng-click="materialsVm.openAttachment(attachment)"
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
                            <a href="" ng-click="materialsVm.showImage(item[attrName].typeImage)"
                               title="Click to show large Image">
                                <img ng-if="item[attrName].typeImage != null"
                                     ng-src="{{item[attrName].typeImage}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="matShowModal" class="img-model modal">
                                <span class="closeImage">&times;</span>
                                <img class="modal-content" id="matShowImg">
                            </div>

                        </div>

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
                    <td class="added-column"
                        ng-repeat="objectAttribute in materialsVm.materialAttributes">

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
                                        <a href="" ng-click="materialsVm.openAttachment(attachment)"
                                           title="Click to download file">
                                            {{attachment.name}}
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'IMAGE'"
                             ng-init="attrName = objectAttribute.name">
                            <a href="" ng-click="materialsVm.showMaterialImage(item[attrName])"
                               title="Click to show large Image">
                                <img ng-if="item[attrName] != null"
                                     ng-src="{{item[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                            <div id="matShowModal1" class="img-model modal">
                                <span class="closeImage1">&times;</span>
                                <img class="modal-content" id="matShowImg1">
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
                    <td class="text-center">
                        <button title="Add item to cart" class="btn btn-xs btn-info"
                                ng-if="materialsVm.mode == 'purchasing'"
                                ng-click="materialsVm.addItemToPurchaseOrder(item)">
                            <i class="fa fa-shopping-cart"></i>
                        </button>

                        <button title="Add item to cart" class="btn btn-xs btn-info"
                                ng-if="materialsVm.mode == 'requisition'"
                                ng-click="materialsVm.addItemToRequisition(item)">
                            <i class="fa fa-shopping-cart"></i>
                        </button>
                                  <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-click="!hasPermission('permission.materials.delete') || materialsVm.deleteItem(item)"
                                            ng-class="{'disabled':!hasPermission('permission.materials.delete')}">
                                            <a class="dropdown-item" title="Delete this Material">
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
                    <h5 style="font-weight: 700">Displaying {{materialsVm.materials.numberOfElements}} of
                        {{materialsVm.materials.totalElements}}</h5>
                </div>
                <div class="text-right">
                    <span class="mr10">Page {{materialsVm.materials.totalElements != 0 ? materialsVm.materials.number+1:0}} of {{materialsVm.materials.totalPages}}</span>
                    <a href="" ng-click="materialsVm.previousPage()"
                       ng-class="{'disabled': materialsVm.materials.first}"><i class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="materialsVm.nextPage()"
                       ng-class="{'disabled': materialsVm.materials.last}"><i class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>
</div>
