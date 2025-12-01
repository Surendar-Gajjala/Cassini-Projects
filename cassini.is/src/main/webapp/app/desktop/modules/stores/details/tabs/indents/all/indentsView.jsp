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

    .img-model.modal {
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
            <th style="min-width: 150px;">Indent Number</th>
            <th style="min-width: 150px;">Project</th>
            <th style="min-width: 150px;">Raised Date</th>
            <th style="min-width: 150px;">Raised By</th>
            <th style="min-width: 150px;">Status</th>
            <th style="min-width: 150px;">Approved By</th>
            <th style="min-width: 150px;">Notes</th>
            <th style="min-width: 150px;"
                ng-repeat="requiredAttribute in indentsVm.requiredIndentAttributes">
                {{requiredAttribute.name}}
            </th>
            <th style="min-width: 150px;" class='added-column'
                ng-repeat="indentAttribute in indentsVm.indentAttributes">
                {{indentAttribute.name}}
                <i class="fa fa-times-circle" style="cursor: pointer;"
                   ng-click="indentsVm.removeAttribute(indentAttribute)"
                   title="Remove this column"></i>
            </th>
        </tr>
        </thead>

        <tbody>


        <tr ng-if="indentsVm.loading == true">
            <td colspan="12">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading indents...
                        </span>
            </td>
        </tr>

        <tr ng-if="indentsVm.loading == false && indentsVm.indents.content.length == 0">
            <td colspan="11">No Indents are available to view</td>
        </tr>
        <tr ng-repeat="indent in indentsVm.indents.content" ng-if="indentsVm.loading == false">
            <td>
                <a ng-click="indentsVm.openIndentDetails(indent)"
                   title="Click to show details">{{indent.indentNumber}}</a>
            </td>

            <td>
                {{indent.project.name}}
            </td>
            <td>
                {{indent.raisedDate}}
            </td>
            <td>
                {{indent.raisedBy}}
            </td>

            <td>
                        <span style="color: white" class="label" ng-class=" {
                                    'label-success': indent.status == 'NEW',
                                    'label-info': indent.status == 'APPROVED'}">
                            {{indent.status}}
                        </span>
            </td>

            <td>
                {{indent.approvedBy}}
            </td>
            <td>
                <p title="{{indent.notes}}">{{indent.notes | limitTo: 8}}{{indent.notes.length > 8 ? '...' : ''}}</p>
            </td>

            <%-- adding attribute values--%>
            <td class="added-column" ng-repeat="objectAttribute in indentsVm.requiredIndentAttributes">

                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                     ng-init="attrName = objectAttribute.name">
                    <p>
                        <a ng-if="indent[attrName].length > 0" href="">
                            {{indent[attrName].length}} Attachments
                        </a>
                    </p>

                    <div class="attributeTooltiptext">
                        <ul>
                            <li ng-repeat="attachment in indent[attrName]">
                                <a href="" ng-click="indentsVm.openAttachment(attachment)"
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
                    <a href="" ng-click="indentsVm.showImage(indent[attrName])"
                       title="Click to show large Image">
                        <img ng-if="indent[attrName] != null"
                             ng-src="{{indent[attrName]}}"
                             style="height: 30px;width: 40px;margin-bottom: 5px;">
                    </a>

                </div>

                <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.name">
                    {{indent[attrName]}}
                </p>

                <p ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                   ng-init="attrName = objectAttribute.name">
                    {{indent[attrName]}}
                </p>

                        <span ng-init="currencyType = objectAttribute.name+'type'"
                              ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="indent[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{indent[attrName]}}
                        </span>
            </td>
            <td class="added-column" ng-repeat="objectAttribute in indentsVm.indentAttributes">

                <div class="attributeTooltip"
                     ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                             && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'ATTACHMENT'"
                     ng-init="attrName = objectAttribute.name">
                    <p>
                        <a ng-if="indent[attrName].length > 0" href="">
                            {{indent[attrName].length}} Attachments
                        </a>
                    </p>

                    <div class="attributeTooltiptext">
                        <ul>
                            <li ng-repeat="attachment in indent[attrName]">
                                <a href="" ng-click="indentsVm.openAttachment(attachment)"
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
                    <a href="" ng-click="indentsVm.showImage(indent[attrName])"
                       title="Click to show large Image">
                        <img ng-if="indent[attrName] != null"
                             ng-src="{{indent[attrName]}}"
                             style="height: 30px;width: 40px;margin-bottom: 5px;">
                    </a>

                </div>

                <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                   ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                   ng-init="attrName = objectAttribute.name" title="{{indent[attrName]}}">
                    {{indent[attrName]}}
                </p>

                <p style="vertical-align: middle;width: 150px;max-width:200px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                   ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'
                                        && objectAttribute.dataType != 'OBJECT' && objectAttribute.dataType != 'ATTACHMENT'
                                        && objectAttribute.dataType != 'IMAGE' && objectAttribute.dataType != 'CURRENCY'"
                   ng-init="attrName = objectAttribute.name" title="{{indent[attrName]}}">
                    {{indent[attrName]}}
                </p>
                <span ng-init="currencyType = objectAttribute.name+'type'"
                      ng-if="objectAttribute.dataType == 'CURRENCY'" ng-bind-html="indent[currencyType]">
                        </span>
                        <span ng-init="attrName = objectAttribute.name"
                              ng-if="objectAttribute.dataType == 'CURRENCY'">
                            {{indent[attrName]}}
                        </span>

                <div id="indModal" class="img-model modal" style="z-index: 11">
                    <span class="closeImage2">&times;</span>
                    <img class="modal-content" id="indImg">
                </div>
            </td>

            <%-- end attributes values--%>
        </tr>
        </tbody>
    </table>
</div>
