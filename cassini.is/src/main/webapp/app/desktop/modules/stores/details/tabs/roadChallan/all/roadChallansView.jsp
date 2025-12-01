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

    .img-model .closeImage4 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage4:hover,
    .img-model .closeImage4:focus {
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
<div>
    <div class="row" style="padding:20px;">
        <div <%--class="responsive-table"--%>>
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="min-width: 50px;">S.No</th>
                    <th style="min-width: 150px;">Road Challan No</th>
                    <th style="min-width: 150px;">Created On</th>
                    <th style="min-width: 150px;">Going From</th>
                    <th style="min-width: 150px;">Going To</th>
                    <th style="min-width: 150px;">Means of Transport</th>
                    <th style="min-width: 150px;">Vehicle Details</th>
                    <th style="min-width: 150px;">Issuing Authority</th>

                    <th style="min-width: 150px;"
                        ng-repeat="reqObjectAttribute in roadChallansVm.requiredObjectAttributes">
                        {{reqObjectAttribute.name}}
                    </th>

                    <th class='added-column' style="min-width: 150px;"
                        ng-repeat="objectAttribute in roadChallansVm.objectAttributes">
                        {{objectAttribute.name}}
                        <i class="fa fa-times-circle" style="cursor: pointer;"
                           ng-click="roadChallansVm.removeAttribute(objectAttribute)"
                           title="Remove this column"></i>
                    </th>

                </tr>
                </thead>
                <tbody>
                <tr ng-if="roadChallansVm.loading == true">
                    <td colspan="12">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Road Challans..
                        </span>
                    </td>
                </tr>
                <tr ng-if="storeRoadChallanList.content.length == 0 && !roadChallansVm.loading">
                    <td colspan="25">No Road Challans are available to view</td>
                </tr>
                <tr data-ng-repeat="item in storeRoadChallanList.content" ng-if="roadChallansVm.loading == false">
                    <td style="vertical-align: middle;">
                        <span>{{$index+1}}</span>
                    </td>
                    <td><a href=""
                           ng-click="roadChallansVm.showRoadChallanDetails(item)">
                        <span>{{item.chalanNumber}}</span></a>
                    </td>

                    <td style="vertical-align: middle;">
                        <span>{{item.chalanDate}}</span>
                    </td>

                    <td style="vertical-align: middle;">
                        <span>{{item.goingFrom}}</span>
                    </td>

                    <td style="vertical-align: middle;">
                        <span>{{item.goingTo}}</span>
                    </td>

                    <td style="vertical-align: middle;">
                        <span>{{item.meansOfTrans}}</span>
                    </td>

                    <td style="vertical-align: middle;">
                        <span>{{item.vehicleDetails}}</span>
                    </td>

                    <td style="vertical-align: middle;">
                        <span>{{item.issuingAuthority}}</span>
                    </td>

                    <%-- adding attribute values--%>
                    <td class="added-column" ng-repeat="objectAttribute in roadChallansVm.requiredObjectAttributes">

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
                                        <a href="" ng-click="roadChallansVm.openAttachment(attachment)"
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
                            <a href="" ng-click="roadChallansVm.showImage(item[attrName])"
                               title="Click to show large Image">
                                <img ng-if="item[attrName] != null"
                                     ng-src="{{item[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>
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
                    <td class="added-column" ng-repeat="objectAttribute in roadChallansVm.objectAttributes">

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
                                        <a href="" ng-click="roadChallansVm.openAttachment(attachment)"
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
                            <a href="" ng-click="roadChallansVm.showImage(item[attrName])"
                               title="Click to show large Image">
                                <img ng-if="item[attrName] != null"
                                     ng-src="{{item[attrName]}}"
                                     style="height: 30px;width: 40px;margin-bottom: 5px;">
                            </a>

                        </div>

                        <p style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                           ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                           ng-init="attrName = objectAttribute.name" title="{{item[attrName]}}">
                            {{item[attrName]}}
                        </p>

                        <p style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
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

                    <div id="rcModal" class="img-model modal">
                        <span class="closeImage4">&times;</span>
                        <img class="modal-content" id="rcImg">
                    </div>
                    <%-- end attributes values--%>


                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
