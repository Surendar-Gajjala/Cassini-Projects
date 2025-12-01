<div>
    <style>

        #freeTextSearchDirective {
            top: 7px !important;
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
            height: 180%;
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

        .img-model .closeImage:hover,
        .img-model .closeImage:focus {
            color: #bbb;
            text-decoration: none;
            cursor: pointer;
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
            word-wrap: break-word;
            width: 300px;
            white-space: normal;
            text-align: left;
        }

        #name {
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

        .dropdown-menu li:hover {
            background-color: #dddddd;
        }

        .dropdown:hover .dropdown-menu {
            display: block;
        }

        .dropdown-content {
            position: fixed !important;
        }

        .responsive-table .dropdown-content {
            margin-left: 25px !important;
        }

        th.actions-column, td.actions-column {
            width: 150px;
            text-align: center;
        }

        .responsive-table .dropdown:hover .dropdown-content {
            display: block;
            color: black !important;
        }

        .dropdown {
            /* position: relative;
             display: inline-block;*/
            text-align: center !important;
            margin-left: -2px !important;
            cursor: pointer;
        }

        .dropbtn {
            background-color: #4CAF50;
            color: black;
            padding: 16px;
            font-size: 16px;
            border: none;
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

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }
    </style>

    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <span style="font-size: 20px;font-weight: bold;color: black;padding: 13px 10px 14px;vertical-align: middle;border-right: 1px solid lightgray;">{{viewInfo.title}}</span>
            <button class="min-width btn btn-sm btn-success" ng-disabled="!hasPermission('permission.suppliers.new')"
                    ng-click="suppliersVm.newSupplier()">New
                Supplier
            </button>
            <button class="btn btn-sm btn-primary" ng-click="suppliersVm.showSupplierAttributes()">Show Attributes
            </button>


            <%--<p class="blink" ng-if="suppliersVm.showSearchMode == true"
               style="color: blue;text-align: center;margin-top: -25px;font-size: 16px;">View is in search mode</p>--%>
            <free-text-search id="freeTextId" class="img-model modal1" on-clear="suppliersVm.resetPage"
                              on-search="suppliersVm.freeTextSearch"></free-text-search>
            <%-- <free-text-search on-clear="suppliersVm.resetPage" title="search only supplierName and description"
                               on-search="suppliersVm.freeTextSearch"></free-text-search>--%>
        </div>

        <div id="applicationView" class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div class="responsive-table" style="padding: 10px;">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th style="min-width: 150px;">Supplier Name</th>
                        <th style="min-width: 150px;">Description</th>
                        <th style="min-width: 150px;">Contact Person</th>
                        <th style="min-width: 150px;">Contact Phone</th>

                        <th style="min-width: 150px;"
                            ng-repeat="attribute in suppliersVm.requiredSupplierAttributes">
                            {{attribute.name}}
                        </th>

                        <th style="min-width: 150px;" class='added-column'
                            ng-repeat="attribute in suppliersVm.supplierAttributes">
                            {{attribute.name}}
                            <i class="fa fa-times-circle" style="cursor: pointer;"
                               ng-click="suppliersVm.removeAttribute(attribute)"
                               title="Remove this column"></i>
                        </th>

                        <th style="min-width: 150px;" class="actions-column">Actions</th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr ng-if="suppliersVm.supplierList.content.length == 0">
                        <td colspan="10" style="padding-left: 11px;">No Suppliers are available to view</td>
                    </tr>

                    <tr ng-repeat="supplier in suppliersVm.supplierList.content">
                        <td><a href="" ng-click="suppliersVm.showSupplier(supplier)"><span
                                title="Click to view supplier details"
                                ng-bind-html="supplier.name | highlightText: freeTextQuery"></span></a>
                        </td>

                        <td><span ng-bind-html="supplier.description | highlightText: freeTextQuery"></span>
                        </td>
                        <td><span ng-bind-html="supplier.contactPerson | highlightText: freeTextQuery"
                                  style="margin-left: 5px;"></span>
                        </td>
                        <td><span ng-bind-html="supplier.contactPhone | highlightText: freeTextQuery"
                                  style="margin-left: 5px;"></span>
                        </td>


                        <td class="added-column" ng-class="{'lockedProject':project.locked == true}"
                            ng-repeat="objectAttribute in suppliersVm.requiredSupplierAttributes">

                            <div class="attributeTooltip"
                                 ng-if="objectAttribute.name == 'modifiedBy' && objectAttribute.name != 'createdBy'
                                                    && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                                 ng-init="attrName = objectAttribute.name">
                                <p>
                                    <a ng-if="supplier[attrName].length > 0" href="">
                                        {{supplier[attrName].length}} Attachments
                                    </a>
                                </p>

                                <div class="attributeTooltiptext">
                                    <ul>
                                        <li ng-repeat="attachment in supplier[attrName]">
                                            <a href="" ng-click="suppliersVm.openAttachment(attachment)"
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
                                <a href="" ng-click="suppliersVm.showRequiredImage(supplier[attrName])"
                                   title="Click to show large image">
                                    <img ng-if="supplier[attrName] != null"
                                         ng-src="{{supplier[attrName]}}"
                                         style="height: 30px;width: 40px;margin-bottom: 5px;">
                                </a>

                                <div id="supModal1" class="img-model modal">
                                    <span class="closeImage1">&times;</span>
                                    <img class="modal-content" id="supImg1">
                                </div>
                            </div>

                            <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                               ng-init="attrName = objectAttribute.name">
                                {{supplier[attrName]}}
                            </p>

                            <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                               ng-init="attrName = objectAttribute.name">
                                {{supplier[attrName]}}
                            </p>

                                        <span ng-init="currencyType = objectAttribute.name+'type'"
                                              ng-if="objectAttribute.dataType == 'CURRENCY'"
                                              ng-bind-html="supplier[currencyType]">
                        </span>
                                        <span ng-init="attrName = objectAttribute.name"
                                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{supplier[attrName]}}
                        </span>
                        </td>


                        <td class="added-column" ng-class="{'lockedProject':project.locked == true}"
                            ng-repeat="objectAttribute in suppliersVm.supplierAttributes">

                            <div class="attributeTooltip"
                                 ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                                 ng-init="attrName = objectAttribute.name">
                                <p>
                                    <a ng-if="supplier[attrName].length > 0" href="">
                                        {{supplier[attrName].length}} Attachments
                                    </a>
                                </p>

                                <div class="attributeTooltiptext">
                                    <ul>
                                        <li ng-repeat="attachment in supplier[attrName]">
                                            <a href="" ng-click="suppliersVm.openAttachment(attachment)"
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
                                <a href="" ng-click="suppliersVm.showImage(supplier[attrName])"
                                   title="Click to show large image">
                                    <img ng-if="supplier[attrName] != null"
                                         ng-src="{{supplier[attrName]}}"
                                         style="height: 30px;width: 40px;margin-bottom: 5px;">
                                </a>

                                <div id="supModal" class="img-model modal">
                                    <span class="closeImage">&times;</span>
                                    <img class="modal-content" id="supImg">
                                </div>
                            </div>
                            <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                               ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                               ng-init="attrName = objectAttribute.name" title="{{supplier[attrName]}}">
                                {{supplier[attrName]}}
                            </p>

                            <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                               ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                               ng-init="attrName = objectAttribute.name" title="{{supplier[attrName]}}">
                                {{supplier[attrName]}}
                            </p>

                                        <span ng-init="currencyType = objectAttribute.name+'type'"
                                              ng-if="objectAttribute.dataType == 'CURRENCY'"
                                              ng-bind-html="supplier[currencyType]">
                        </span>
                                        <span ng-init="attrName = objectAttribute.name"
                                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{supplier[attrName]}}
                        </span>
                        </td>
                        <td class="text-center" style="text-align: center;">
                            <span class="row-menu" uib-dropdown dropdown-append-to-body style="min-width: 50px">
                                        <i class="fa fa-ellipsis-h dropdown-toggle" uib-dropdown-toggle></i>
                                    <ul uib-dropdown-menu class="dropdown-menu dropdown-menu-right" role="menu"
                                        style="z-index: 9999 !important;">
                                        <li ng-click="!hasPermission('permission.supplier.delete') || suppliersVm.deleteSupplier(supplier)"
                                            ng-class="{'disabled':!hasPermission('permission.supplier.delete')}">
                                            <a class="dropdown-item" title="Delete Supplier">
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
                        <h5 style="font-weight: 700">Displaying {{suppliersVm.supplierList.numberOfElements}} of
                            {{suppliersVm.supplierList.totalElements}}</h5>
                    </div>

                    <div class="text-right">
                        <span class="mr10">Page {{suppliersVm.supplierList.totalElements != 0 ? suppliersVm.supplierList.number+1:0}} of {{suppliersVm.supplierList.totalPages}}</span>
                        <a href="" ng-click="suppliersVm.previousPage()"
                           ng-class="{'disabled': suppliersVm.supplierList.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="suppliersVm.nextPage()"
                           ng-class="{'disabled': suppliersVm.supplierList.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>