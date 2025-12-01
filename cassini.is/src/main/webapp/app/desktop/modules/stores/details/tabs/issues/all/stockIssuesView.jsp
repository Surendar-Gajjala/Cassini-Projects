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

    .img-model .closeImage8 {
        position: absolute;
        top: 50px;
        right: 50px;
        color: black;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
    }

    .img-model .closeImage8:hover,
    .img-model .closeImage8:focus {
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
<div class="item">
    <div class="responsive-table">
        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="min-width: 150px;">Number</th>
                <th style="min-width: 150px;">Issue Type</th>
                <th style="min-width: 150px;">Project</th>
                <th style="min-width: 150px;">Issued To</th>
                <th style="min-width: 200px;">Issue Date</th>
                <th style="min-width: 200px;">Notes</th>
                <th style="min-width: 150px; text-align: left" class='added-column'
                    ng-repeat="objectAttribute in stockIssuesVm.issueAttributes">
                    {{objectAttribute.name}}
                    <i class="fa fa-times-circle" style="cursor: pointer;"
                       ng-click="stockIssuesVm.removeAttribute(objectAttribute)"
                       title="Remove this column"></i>
                </th>
                <th style="min-width: 60px;"></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td style="text-align: center; width: 150px;">
                    <input type="text" style="height: 30px;" class="btn-sm form-control" name="Number"
                           ng-change="stockIssuesVm.applyFilters()" placeholder="Number"
                           ng-model="stockIssuesVm.emptyFilters.issueNumberSource">
                </td>

                <td style="text-align: center; width: 200px;">
                    <div class="input-group">
                        <div class="input-group-btn" uib-dropdown>
                            <button uib-dropdown-toggle class="btn-sm btn-default dropdown-toggle"
                                    style="background-color: white; border: 1px #bbbbbb solid">
                                IssueType <span class="caret" style="margin-left: 4px;"></span>
                            </button>
                            <div class="dropdown-menu" role="menu">
                                <div style="padding: 5px; border: 1px solid #FFF;height: auto;height: auto; max-height:200px; overflow-x: auto">
                                    <div class="classification-pane">
                                        <ul id="issueTypeTree" class="easyui-tree"
                                            on-select="stockIssuesVm.onSelectType"></ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <input type="text" class="btn-sm form-control" name="title"
                               style="width: auto; background-color: white; height:30px;"
                               ng-model="stockIssuesVm.itemType.attributes.itemType.name"
                               readonly>
                        <%--<i ng-if="stockIssuesVm.itemType.attributes.itemType.name != null" class="fa fa-times-circle clear"--%>
                        <%--style="cursor: pointer;" onMouseOver="this.style.color='black' "--%>
                        <%--ng-click="stockIssuesVm.undoIssueTypeSelection()"--%>
                        <%--title="Remove this column"></i>--%>
                    </div>
                </td>
                <td style="text-align: center; width: 150px;">
                    <input type="text" style="height: 30px;" class="btn-sm form-control" name="Project"
                           ng-change="stockIssuesVm.applyFilters()" placeholder="Project Name"
                           ng-model="stockIssuesVm.emptyFilters.project">
                </td>
                <td></td>
                <td></td>
                <td style="text-align: center; width: 150px;">
                    <input type="text" style="height: 30px;" class="btn-sm form-control" name="Notes"
                           ng-change="stockIssuesVm.applyFilters()" placeholder="Notes"
                           ng-model="stockIssuesVm.emptyFilters.notes">
                </td>
                <td ng-repeat="attribute in stockIssuesVm.issueAttributes">
                    <input ng-if="attribute.dataType == 'TEXT' " type="text" style="height: 30px;"
                           class="btn-sm form-control"
                           ng-change="stockIssuesVm.attributeFilter(attribute)"
                           ng-model="attribute.textValue">
                    <input ng-if="attribute.dataType == 'INTEGER' " type="number" style="height: 30px;"
                           class="btn-sm form-control"
                           ng-change="stockIssuesVm.attributeFilter(attribute)"
                           ng-model="attribute.integerValue">
                    <input type="text" ng-if="attribute.dataType == 'DOUBLE'"
                           ng-model="attribute.doubleValue" class="form-control" style="padding: 7px"
                           ng-change="stockIssuesVm.attributeFilter(attribute)">
                    <%--<input type="date" ng-if="attribute.dataType == 'DATE'"--%>
                    <%--style="padding: 0 !important; line-height: 32px;"--%>
                    <%--ng-model="attribute.dateValue"--%>
                    <%--class="form-control" id="inpDate" name="inpDate"--%>
                    <%--ng-change="stockIssuesVm.attributeFilter(attribute)">--%>
                    <%--<input type="text" ng-if="attribute.dataType == 'TIME'" time-picker--%>
                    <%--style="padding: 0 !important; line-height: 32px;"--%>
                    <%--ng-model="attribute.timeValue"--%>
                    <%--class="form-control"--%>
                    <%--ng-change="stockIssuesVm.attributeFilter(attribute)">--%>

                    <div ng-if="attribute.dataType == 'LIST'"
                         style="padding-right: 0px;">
                        <select class="form-control" ng-model="attribute.listValue"
                                placeholder="select" style="padding: 6px !important;"
                                ng-change="stockIssuesVm.attributeFilter(attribute)"
                                ng-options="value for value in attribute.lov.values">
                        </select>
                    </div>
                    <div ng-if="attribute.dataType == 'BOOLEAN'">
                        <select class="form-control" ng-model="attribute.booleanValue"
                                style="padding: 6px !important;"
                                placeholder="select" ng-change="stockIssuesVm.attributeFilter(attribute)"
                                ng-options="value for value in ['true', 'false']">
                        </select>
                    </div>
                    <div ng-if="attribute.dataType == 'CURRENCY'">
                        <input class="form-control" name="currencyValue" type="number"
                               placeholder="Enter currency value"
                               ng-change="stockIssuesVm.attributeFilter(attribute)"
                               ng-model="attribute.currencyValue"/>
                    </div>
                </td>

                <td class="actions-col">
                    <div class="btn-group btn-group-md">

                        <button title="Apply Filters" type="button" class="btn btn-xs btn-success"
                                ng-click="stockIssuesVm.applyFilters()">
                            <i class="fa fa-search"></i>
                        </button>

                        <button title="Clear Filters" type="button" class="btn btn-xs btn-default"
                                ng-click="stockIssuesVm.resetPage()">
                            <i class="fa fa-times"></i>
                        </button>
                    </div>
                </td>

            </tr>
            <tr ng-if="stockIssuesVm.loading == true">
                <td colspan="12">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">Loading Stock Issues..
                        </span>
                </td>
            </tr>
            <tr ng-if="stockIssuesVm.stockIssues.content.length == 0 && !stockIssuesVm.loading">
                <td colspan="25">No Stock Issues</td>
            </tr>
            <tr data-ng-repeat="item in stockIssuesVm.stockIssues.content" ng-if="stockIssuesVm.loading == false">
                <td style="width: 150px; text-align: left"><a href=""
                                                              ng-click="stockIssuesVm.showDetails(item)">
                    <span>{{item.issueNumberSource}}</span></a>

                </td>
                <td style="width: 150px; text-align: left">{{item.materialIssueType.name}}</td>
                <td style="width: 150px; text-align: left">
                    <span>{{item.projectObject.name}}</span>
                </td>
                <td style="width: 150px; text-align: left">
                    <span>{{item.issuedToObject.fullName}}</span>
                </td>
                <td style="width: 150px; text-align: left">
                    <span>{{item.issueDate}}</span>
                </td>
                <td style="width: 200px; text-align: left" title="{{item.notes}}">
                    <span>{{item.notes | limitTo: 15}}{{item.notes.length > 15 ? '...' : ''}}</span>
                </td>

                <%-- adding attribute values--%>
                <td class="added-column" ng-repeat="objectAttribute in stockIssuesVm.issueAttributes">

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
                                    <a href="" ng-click="stockIssuesVm.openAttachment(attachment)"
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
                        <a href="" ng-click="stockIssuesVm.showImage(item[attrName])"
                           title="Click to show large Image">
                            <img ng-if="item[attrName] != null"
                                 ng-src="{{item[attrName]}}"
                                 style="height: 30px;width: 40px;margin-bottom: 5px;">
                        </a>

                    </div>

                    <p style="vertical-align: middle;width: 150px;max-width:150px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                       ng-if="objectAttribute.name != 'modifiedBy' && objectAttribute.name != 'createdBy'&& objectAttribute.dataType != 'LIST'
                                        && objectAttribute.dataType != 'CURRENCY' && objectAttribute.dataType == 'OBJECT' && objectAttribute.dataType != 'RICHTEXT'"
                       ng-init="attrName = objectAttribute.name">
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
                <td></td>

                <div id="myModal8" class="img-model modal" style="z-index: 11">
                    <span class="closeImage8">&times;</span>
                    <img class="modal-content" id="img08">
                </div>
                <%-- end attributes values--%>
            </tr>
            </tbody>
        </table>
    </div>
</div>
