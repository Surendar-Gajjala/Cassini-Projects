<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 10-04-2018
  Time: 12:18
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
            width: 550px;
            min-width: 550px;
            max-width: 550px;
            height: 500px;
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
            height: 50px;
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
            height: 455px;
            max-height: 455px;
            width: 550px;
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

            width: 550px;
            min-width: 550px;
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
            height: 100%;
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
            text-align: center;
            padding-right: 10px;
            height: 35px;
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
        <div class="text-right type-selector" ng-if="fListsVm.fLists.length == 0">
            <div id="listAddButton"
                 title="{{'NEW_FAILURE_LIST' | translate}}"
                 ng-click='fListsVm.addFailureList()'
                 class="flex-col list-add-container"
                 style="display:table;">
                <div style="display: table-cell;vertical-align: middle;text-align: center;">
                    <i class="fa fa-plus" style="font-size: 40px;vertical-align: middle;color: #adadad;"></i>
                </div>
            </div>
        </div>
        <div id="list{{$index}}" class="flex-col list-container"
             ng-repeat="list in fListsVm.fLists  | orderBy:'$index':true">
            <div id="listMask{{$index}}" class="list-container-mask centered-container">
                <div class="centered-child">
                    <h5>Are you sure you want to delete this list?</h5>

                    <div>
                        <button class='btn btn-md btn-default' ng-click='fListsVm.hideMask($index)'>No</button>
                        <button class='btn btn-md btn-danger' ng-click='fListsVm.deleteFailureList(list)'>Yes</button>
                    </div>
                </div>
            </div>
            <div id="listValueMask{{$index}}" class="list-container-mask centered-container">
                <div class="centered-child">
                    <h5>Are you sure you want to delete this value?</h5>

                    <div>
                        <button class='btn btn-md btn-default' ng-click='fListsVm.hideMask($index)'>No</button>
                        <button class='btn btn-md btn-danger' ng-click='fListsVm.deleteFailureListValue(list)'>Yes
                        </button>
                    </div>
                </div>
            </div>
            <div class="list-header">
                <span ng-if="!list.editTitle" class="classificationlables" ng-dblclick="list.editTitle = true;
                $('#titleInput{{$index}}').setSelectionRange(0, this.value.length)">{{list.newName}}</span>

                <div ng-if="list.editTitle" style="display: flex">
                    <input type="text" style="width: 520px"
                           id="titleInput{{$index}}"
                           ng-model="list.newName"
                           ng-enter="list.editTitle = false;fListsVm.applyChanges(list);"
                           onfocus="this.setSelectionRange(0, this.value.length)">

                    <div class="buttons" style="display: inline-block; margin-left: -35px;">
                        <i title="Save Failure Process" class="fa fa-check mr10"
                           ng-click="list.editTitle = false;fListsVm.applyChanges(list)"></i>
                        <i title="Cancel changes" class="fa fa-times"
                           ng-click="fListsVm.cancelFailureListChange(list);list.editTitle = false;"></i>
                    </div>
                </div>
                <div class="buttons" ng-if="!list.editTitle">
                    <i ng-if="list.id != null" title="Add Phase" class="fa fa-plus mr10"
                       ng-click="fListsVm.addFailureListValue(list, $index)"></i>
                    <i title="Delete Failure Process" class="fa fa-times"
                       ng-click="fListsVm.promptDeleteFailureList($index)"></i>
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
                        <th style="width: 330px;text-align: left;">Status</th>
                        <th>DataType</th>
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
                            <input type="text" style="width: 210px"
                                   ng-model="value.newStatus"
                                   ng-enter="fListsVm.applyChanges(list, value)"
                                   onfocus="this.setSelectionRange(0, this.value.length)">
                            </span>
                            </div>
                            <div ng-if="!value.editMode">
                                <span class="name" ng-dblclick="value.editMode = true">{{value.status}}</span>
                            </div>
                        </td>
                        <td style="width: 200px">
                                <span ng-if="value.editMode == false">
                                    <span>{{value.dataType}}</span>
                                </span>
                            <select ng-if="value.editMode == true" class="form-control input-sm"
                                    ng-model="value.newDataType"
                                    ng-options="dataType for dataType in fListsVm.dataTypes track by dataType">
                            </select>
                        </td>
                        <td>
                        <span ng-if="value.editMode" class="buttons">
                            <i title="Save" class="fa fa-check mr5"
                               ng-click="fListsVm.applyChanges(list,value)">
                            </i>
                            <i title="Cancel"
                               ng-click="fListsVm.cancelChanges(list, value)"
                               class="fa fa-times"></i>
                        </span>
                        <span ng-if="!value.editMode" class="buttons">
                            <i title="Edit Phase" class="fa fa-edit mr5"
                               ng-click="fListsVm.toDeleteValue = null;value.editMode = true"></i>
                            <i title="Delete Failure Process Phase"
                               ng-click="fListsVm.toDeleteValue = value;fListsVm.promptDeleteFailureListValue($parent.$parent.$index)"
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