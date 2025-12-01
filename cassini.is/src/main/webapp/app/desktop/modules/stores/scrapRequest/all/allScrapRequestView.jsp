<div>
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

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
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
            z-index: 5;
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

        .lockedProject {
            background: #ff6666 !important;
            color: black;
        }
    </style>


    <div class="view-container" fitcontent>
        <div class="view-toolbar">
            <button class="btn btn-sm btn-default min-width" ng-click="allScrapVm.back()">Back</button>
            <button class="btn btn-sm btn-default min-width" ng-click="allScrapVm.showScrapRequestAttributes()">Show
                Attributes
            </button>
        </div>


        <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
            <div class="responsive-table" style="padding: 10px;">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr>
                        <th style="min-width: 150px;">Scrap No</th>
                        <th style="min-width: 150px;">Requested By</th>
                        <th style="min-width: 150px;">Project</th>
                        <th style="min-width: 150px;">Store</th>
                        <th style="min-width: 150px;">Notes</th>
                        <th style="min-width: 150px;"
                            ng-repeat="reqObjectAttribute in allScrapVm.requiredObjectAttributes">
                            {{reqObjectAttribute.name}}
                        </th>

                        <th style="min-width: 150px;" class='added-column'
                            ng-repeat="objectAttribute in allScrapVm.objectAttributes">
                            {{objectAttribute.name}}
                            <i class="fa fa-times-circle" style="cursor: pointer;"
                               ng-click="allScrapVm.removeAttribute(objectAttribute)"
                               title="Remove this column"></i>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="allScrapVm.scraps.content.length == 0">
                        <td colspan="25">No Items available to view</td>
                    </tr>
                    <tr data-ng-repeat="scrap in allScrapVm.scraps.content">
                        <td><a href=""
                               ng-click="allScrapVm.showDetails(scrap)">
                            <span>{{scrap.scrapNumber}}</span></a>

                        </td>
                        <td style="vertical-align: middle;">
                            <span>{{scrap.requestedBy}}</span></td>
                        <td style="vertical-align: middle;">
                            <span>{{scrap.project}}</span></td>
                        <td style="vertical-align: middle;">
                            <span>{{scrap.store}}</span></td>
                        <td style="vertical-align: middle;" ng-if="item.description == null">
                            <span>{{scrap.notes}}</span>
                        </td>
                        <%-- adding attribute values--%>
                        <td class="added-column" ng-repeat="objectAttribute in allScrapVm.requiredObjectAttributes">

                            <div class="attributeTooltip"
                                 ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                                 ng-init="attrName = objectAttribute.name">
                                <p>
                                    <a ng-if="scrap[attrName].length > 0" href="">
                                        {{scrap[attrName].length}} Attachments
                                    </a>
                                </p>

                                <div class="attributeTooltiptext">
                                    <ul>
                                        <li ng-repeat="attachment in scrap[attrName]">
                                            <a href="" ng-click="allScrapVm.openAttachment(attachment)"
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
                                <a href="" ng-click="allScrapVm.showImage(scrap[attrName])"
                                   title="Click to show large Image">
                                    <img ng-if="scrap[attrName] != null"
                                         ng-src="{{scrap[attrName]}}"
                                         style="height: 30px;width: 40px;margin-bottom: 5px;">
                                </a>

                                <div id="scrapModal" class="img-model modal">
                                    <span class="closeImage">&times;</span>
                                    <img class="modal-content" id="scrapImg">
                                </div>
                            </div>

                            <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                               ng-init="attrName = objectAttribute.name">
                                {{scrap[attrName]}}
                            </p>

                            <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                               ng-init="attrName = objectAttribute.name">
                                {{scrap[attrName]}}
                            </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="scrap[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{scrap[attrName]}}
                        </span>
                        </td>
                        <td class="added-column" ng-repeat="objectAttribute in allScrapVm.objectAttributes">

                            <div class="attributeTooltip"
                                 ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                                 ng-init="attrName = objectAttribute.name">
                                <p>
                                    <a ng-if="scrap[attrName].length > 0" href="">
                                        {{scrap[attrName].length}} Attachments
                                    </a>
                                </p>

                                <div class="attributeTooltiptext">
                                    <ul>
                                        <li ng-repeat="attachment in scrap[attrName]">
                                            <a href="" ng-click="allScrapVm.openAttachment(attachment)"
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
                                <a href="" ng-click="allScrapVm.showImage(scrap[attrName])"
                                   title="Click to show large Image">
                                    <img ng-if="scrap[attrName] != null"
                                         ng-src="{{scrap[attrName]}}"
                                         style="height: 30px;width: 40px;margin-bottom: 5px;">
                                </a>

                                <div id="image" class="img-model modal">
                                    <span class="closeImage2">&times;</span>
                                    <img class="modal-content" id="img">
                                </div>
                            </div>

                            <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                               ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                               ng-init="attrName = objectAttribute.name" title="{{scrap[attrName]}}">
                                {{scrap[attrName]}}
                            </p>

                            <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                               ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                               ng-init="attrName = objectAttribute.name" title="{{scrap[attrName]}}">
                                {{scrap[attrName]}}
                            </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="scrap[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{scrap[attrName]}}
                        </span>
                        </td>
                        <%-- end attributes values--%>

                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="table-footer">
                <div>
                    <div>
                        <h5 style="font-weight: 700">Displaying {{allScrapVm.scraps.numberOfElements}} of
                            {{allScrapVm.scraps.totalElements}}</h5>
                    </div>

                    <div class="text-right">
                        <span class="mr10">Page {{allScrapVm.scraps.totalElements != 0 ? allScrapVm.scraps.number+1:0}} of {{allScrapVm.scraps.totalPages}}</span>
                        <a href="" ng-click="allScrapVm.previousPage()"
                           ng-class="{'disabled': allScrapVm.scraps.first}"><i class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="allScrapVm.nextPage()"
                           ng-class="{'disabled': allScrapVm.scraps.last}"><i class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
