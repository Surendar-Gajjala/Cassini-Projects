<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 08-10-2018
  Time: 15:15
  To change this template use File | Settings | File Templates.
--%>
<div>
    <style scoped>
        .flex-row {
            display: -webkit-box;
            display: -moz-box;
            display: -ms-flexbox;
            display: -webkit-flex;
            display: flex;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }

        .flex-row-center {
            justify-content: center;
        }

        .flex-row > .flex-col {
            margin-left: 40px;
        }

        .list-container {
            width: 880px;
            min-width: 880px;
            max-width: 880px;
            /*height: 500px;*/
            max-height: 500px;
            border: 1px solid #ddd;
            border-radius: 5px;
            border-radius: 5px;
            margin-bottom: 50px;
        }

        .list-add-container {
            width: 50px;
            min-width: 50px;
            max-width: 50px;
            /*height: 50px;*/
            max-height: 50px;
            border: 4px solid #ddd;
            border-radius: 5px;
            border-radius: 5px;
            margin-bottom: 50px;
        }

        .list-container > .list-header {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            display: flex;
        }

        .list-container > .list-header > h5 {
            margin: 0 !important;
            display: inline-block;
        }

        .list-container > .list-header input {
        }

        .list-container > .list-header > input {
            flex-grow: 1;
        }

        .list-container > .list-header > .buttons {
            display: inline-block;
            flex-grow: 1;
            text-align: right;
        }

        .list-container .buttons i:hover {
            color: #0390fd;
            cursor: pointer;
        }

        .list-container > .list-body {
            padding: 10px;
            /*height: 455px;*/
            max-height: 455px;
            width: 880px;
            overflow-y: auto;
        }

        .list-container .list-body .list-value {
            cursor: pointer;
        }

        .list-container > .list-body > .list-value > div {
            border-bottom: 1px dotted #e3e3e3;
            padding: 10px;
            display: flex;
            justify-content: left;
        }

        .list-container .list-body .list-value .name,
        .list-container .list-body .list-value .type {
            flex-grow: 1;
            justify-content: left;
            text-align: left;
        }

        .list-container .list-body .list-value .buttons {
            visibility: hidden;
            text-align: right;
        }

        .list-container .list-body .list-value:hover .buttons {
            visibility: visible;
        }

        .list-container .list-body .list-value.edit-mode .buttons {
            visibility: visible;
        }

        .list-container input {
            border: 0;
            outline: 0;
            background: transparent;
            border-bottom: 1px dashed lightgrey;
        }

        .list-container .list-body .list-value.edit-mode .type {
            border-bottom: 1px dashed lightgrey;
            margin-right: 20px;
            text-align: left;
        }

        .list-container .list-body .list-value .type a {
            text-decoration: none;
            color: inherit;
        }

        .list-container .list-body .list-value .type .caret {
            border-top: 5px dashed;
            border-right: 5px solid transparent;
            border-left: 5px solid transparent;
        }

        .list-container .list-body .list-value:hover {
            background-color: #0081c2;
            color: #fff;
        }

        .list-container .list-body .list-value.edit-mode {
            background-color: transparent;
            color: inherit;
        }

        .list-container .list-body .list-value:hover .buttons i {
            cursor: pointer;
        }

        .list-container .list-body .list-value:hover .buttons i {
            color: #fff;
        }

        .list-container .list-body .list-value.edit-mode .buttons i:hover {
            color: #0390fd;
        }

        .list-container .list-body .list-value.edit-mode .buttons i {
            color: inherit;
        }

        .list-container .list-body .list-value .name input {
            width: 250%;
        }

        .list-container .list-container-mask {
            position: absolute;
            display: none;
            opacity: 1;
            background-color: #7d7d7d;;
            z-index: 9999;

            width: 880px;
            min-width: 880px;
            border: 1px solid #ddd;
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
            margin-bottom: 20px;
            margin-left: -1px;
            margin-top: -1px;
        }

        .listp-dropdown {
            position: fixed;
        }

        .centered-container {
            position: absolute;
            /*height: 100%;*/
            width: 100%;
            display: table;
            z-index: 9999;
        }

        .centered-child {
            display: table-cell;
            vertical-align: top;
            text-align: center;
            color: #fff;
            padding-top: 50px;
        }

        #listAddButton:hover {
            background-color: #0081c2;
            cursor: pointer;
        }

        #listAddButton:hover i {
            color: #fff !important;
        }

        h3 {
            color: black;
            font-weight: bolder;
        }

        th, td {
            /*text-align: center;*/
            padding-right: 10px;
        }

        .type-selector {
            position: absolute;
            right: 20px;
            top: 15px;
            cursor: pointer;
            font-size: 18px;
            color: #86a1b7;
        }
    </style>

    <div class="flex-row flex-row-center">
        <div class="text-right type-selector">
            <div id="listAddButton"
                 title="New Part Tracking"
                 ng-click='listsVm.addPartTracking()'
                 class="flex-col list-add-container"
                 style="display:table">
                <div style="display: table-cell;vertical-align: middle;text-align: center;">
                    <i class="fa fa-plus" style="font-size: 40px;vertical-align: middle;color: #adadad;"></i>
                </div>
            </div>
        </div>

        <div id="list{{$index}}" class="flex-col list-container"
             ng-repeat="list in listsVm.lists  | orderBy:'$index':true">
            <div id="listMask{{$index}}" class="list-container-mask centered-container">
                <div class="centered-child">
                    <h5>Are you sure you want to delete this list?</h5>

                    <div>
                        <button class='btn btn-md btn-default' ng-click='listsVm.hideMask($index)'>No</button>
                        <button class='btn btn-md btn-danger' ng-click='listsVm.deletePartTracking(list)'>Yes</button>
                    </div>
                </div>
            </div>
            <div id="listValueMask{{$index}}" class="list-container-mask centered-container">
                <div class="centered-child">
                    <h5>Are you sure you want to delete this value?</h5>

                    <div>
                        <button class='btn btn-md btn-default' ng-click='listsVm.hideMask($index)'>No</button>
                        <button class='btn btn-md btn-danger' ng-click='listsVm.deletePartTrackingValue(list, $index)'>
                            Yes
                        </button>
                    </div>
                </div>
            </div>
            <div class="list-header">
                <span ng-if="!list.editTitle" class="classificationlables" ng-dblclick="list.editTitle = true;
                $('#titleInput{{$index}}').setSelectionRange(0, this.value.length)">{{list.newName}}</span>

                <div ng-if="list.editTitle" style="display: flex">
                    <input type="text" style="width: 830px"
                           id="titleInput{{$index}}"
                           ng-model="list.newName"
                           ng-enter="list.editTitle = false;listsVm.applyChanges(list);"
                           onfocus="this.setSelectionRange(0, this.value.length)">

                    <div class="buttons" style="display: inline-block; margin-left: -35px;">
                        <i title="Save Part Tracking" class="fa fa-check mr10"
                           ng-click="list.editTitle = false;listsVm.applyChanges(list)"></i>
                        <i title="Cancel changes" class="fa fa-times"
                           ng-click="listsVm.cancelPartTrackingChange(list);list.editTitle = false;"></i>
                    </div>
                </div>
                <div class="buttons" ng-if="!list.editTitle">
                    <i ng-if="list.id != null" title="Add Part Tracking Phase" class="fa fa-plus mr10"
                       ng-click="listsVm.addPartTrackingValue(list, $index)"></i>
                    <i title="Delete Part Tracking" class="fa fa-times"
                       ng-click="listsVm.promptDeletePartTracking($index)"></i>
                </div>
            </div>
            <div class="progress progress-striped active"
                 style="border-radius: 0;height: 5px;"
                 ng-if="list.showBusy">
                <div class="progress-bar"
                     role="progressbar" aria-valuenow="100" aria-valuemin="0"
                     aria-valuemax="100" style="width: 100%">
                </div>
            </div>
            <div class="list-body" id="lov-body{{$index}}">
                <table style="margin-left:10px;margin-right:10px">
                    <thead>
                    <tr>
                        <th>S.No.</th>
                        <th style="width: 250px;text-align: left;">Status</th>
                        <th style="width: 300px;text-align: left;">Description</th>
                        <th style="width: 70px;text-align: left;">Percentage</th>
                        <th>Attachment</th>
                        <th>Scan</th>
                        <th>BDL</th>
                        <th>SSQAG</th>
                        <th>CAS</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="value in list.steps">
                        <td>
                            <span ng-if="!value.editMode">{{value.serialNo}}</span>
                            <input style="width:20px;" ng-if="value.editMode" type="text" ng-model="value.serialNo">
                        </td>
                        <td style="text-align: left !important;">
                            <div ng-if="value.editMode" style="display: flex">
                        <span style="flex-grow: 1;">
                            <input type="text"
                                   ng-model="value.newStatus"
                                   ng-enter="listsVm.applyChanges(list, value)"
                                   onfocus="this.setSelectionRange(0, this.value.length)">
                        </span>
                            </div>
                            <div ng-if="!value.editMode">
                                <span class="name" ng-dblclick="value.editMode = true">{{value.status}}</span>
                            </div>
                        </td>
                        <td style="text-align: left !important;width: 300px;">
                            <div ng-if="value.editMode" style="display: flex">
                        <span style="flex-grow: 1;">
                            <input type="text" style="width: 250px"
                                   ng-model="value.newDescription"
                                   ng-enter="listsVm.applyChanges(list, value)"
                                   onfocus="this.setSelectionRange(0, this.value.length)">
                        </span>
                            </div>
                            <div ng-if="!value.editMode">
                                <span class="name" ng-dblclick="value.editMode = true">{{value.description}}</span>
                            </div>
                        </td>
                        <td style="text-align: left !important;width: 70px;">
                            <div ng-if="value.editMode" style="display: flex">
                        <span style="flex-grow: 1;">
                            <input type="text" style="width: 70px"
                                   ng-model="value.newPercentage"
                                   ng-enter="listsVm.applyChanges(list, value)"
                                   onfocus="this.setSelectionRange(0, this.value.length)">
                        </span>
                            </div>
                            <div ng-if="!value.editMode">
                                <span class="name" ng-dblclick="value.editMode = true">{{value.percentage}}</span>
                            </div>
                        </td>
                        <td>
                            <input type="checkbox" ng-model="value.newAttachment" ng-disabled="!value.editMode">
                        </td>
                        <td>
                            <input type="checkbox" ng-model="value.newScan" ng-disabled="!value.editMode">
                        </td>
                        <td>
                            <input type="checkbox" ng-model="value.newBdl" ng-disabled="true">
                        </td>
                        <td>
                            <input type="checkbox" ng-model="value.newSsqag" ng-disabled="!value.editMode">
                        </td>
                        <td>
                            <input type="checkbox" ng-model="value.newCas" ng-disabled="!value.editMode">
                        </td>
                        <td>
                        <span ng-if="value.editMode" class="buttons">
                            <i title="Save" class="fa fa-check mr5"
                               ng-click="listsVm.applyChanges(list,value)">
                            </i>
                            <i title="Cancel"
                               ng-click="listsVm.cancelChanges(list, value)"
                               class="fa fa-times"></i>
                        </span>
                        <span ng-if="!value.editMode" class="buttons">
                            <i title="Edit Phase" class="fa fa-edit mr5"
                               ng-click="listsVm.toDeleteValue = null;value.editMode = true"></i>
                            <i title="Delete Part Tracking Phase"
                               ng-click="listsVm.toDeleteValue = value;listsVm.promptDeletePartTrackingValue($parent.$parent.$index)"
                               class="fa fa-times"></i>
                        </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>