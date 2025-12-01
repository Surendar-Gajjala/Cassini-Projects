<%--
  Created by IntelliJ IDEA.
  User: Nageshreddy
  Date: 13-11-2018
  Time: 14:03
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
            margin-bottom: 10px;
            flex-wrap: wrap;
        }

        .flex-row-center {
            justify-content: center;
        }

        .flex-row > .flex-col {
            margin-left: 10px;
        }

        .groupLocation-container {
            width: 300px;
            min-width: 305px;
            border: 1px solid #ddd;
            border-radius: 5px;
            border-radius: 5px;
            margin-bottom: 10px;
        }

        .groupLocation-container > .groupLocation-header {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            display: flex;
        }

        .groupLocation-container > .groupLocation-header > h5 {
            margin: 0 !important;
            display: inline-block;
        }

        .groupLocation-container > .groupLocation-header input {
        }

        .groupLocation-container > .groupLocation-header > input {
            flex-grow: 1;
        }

        .groupLocation-container > .groupLocation-header > .buttons {
            display: inline-block;
            flex-grow: 1;
            text-align: right;
        }

        .groupLocation-container .buttons i:hover {
            color: #0390fd;
            cursor: pointer;
        }

        .groupLocation-container > .groupLocation-body {
            padding: 10px;
            height: 300px;
            max-height: 300px;
            overflow-y: auto;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location {
            cursor: pointer;
        }

        .groupLocation-container > .groupLocation-body > .groupLocation-location > div {
            border-bottom: 1px dotted #e3e3e3;
            padding: 5px;
            display: flex;
            justify-content: left;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location .name,
        .groupLocation-container .groupLocation-body .groupLocation-location .type {
            flex-grow: 1;
            justify-content: left;
            text-align: left;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location .buttons {
            visibility: hidden;
            text-align: right;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location:hover .buttons {
            visibility: visible;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location.edit-mode .buttons {
            visibility: visible;
        }

        .groupLocation-container input {
            border: 0;
            outline: 0;
            background: transparent;
            border-bottom: 1px dashed lightgrey;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location.edit-mode .type {
            border-bottom: 1px dashed lightgrey;
            margin-right: 10px;
            text-align: left;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location .type a {
            text-decoration: none;
            color: inherit;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location .type .caret {
            border-top: 5px dashed;
            border-right: 5px solid transparent;
            border-left: 5px solid transparent;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location:hover {
            background-color: #0081c2;
            color: #fff;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location.edit-mode {
            background-color: transparent;
            color: inherit;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location:hover .buttons i {
            cursor: pointer;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location:hover .buttons i {
            color: #fff;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location.edit-mode .buttons i:hover {
            color: #0390fd;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location.edit-mode .buttons i {
            color: inherit;
        }

        .groupLocation-container .groupLocation-body .groupLocation-location .name input {
            width: 98%;
        }

        .groupLocation-container .groupLocation-container-mask {
            position: absolute;
            display: none;
            opacity: 1;
            background-color: #7d7d7d;;
            z-index: 9999;

            width: 300px;
            min-width: 300px;
            border: 1px solid #ddd;
            border-top-left-radius: 5px;
            border-top-right-radius: 5px;
            margin-bottom: 10px;
            margin-left: -1px;
            margin-top: -1px;
        }

        .groupLocationp-dropdown {
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
            vertical-align: middle;
            text-align: center;
            color: #fff;
        }

        #groupLocationAddButton:hover {
            background-color: #0081c2;
            cursor: pointer;
        }

        #groupLocationAddButton:hover i {
            color: #fff !important;
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
            <div id="groupLocationAddButton"
                 title="New Group Location"
                 ng-click='groupVm.addGroupLocation()'
                 class="flex-col list-add-container"
                 style="display:table">
                <div style="display: table-cell;vertical-align: middle;text-align: center;">
                    <i class="fa fa-plus" style="font-size: 40px;vertical-align: middle;color: #adadad;"></i>
                </div>
            </div>
        </div>
        <div id="groupLocation{{$index}}" class="flex-col groupLocation-container"
             ng-repeat="groupLocation in groupVm.groupLocations">
            <div id="groupLocationMask{{$index}}" class="groupLocation-container-mask centered-container">
                <div class="centered-child">
                    <h5>Please confirm to delete this groupLocation</h5>

                    <div>
                        <button class='btn btn-xs btn-default' ng-click='groupVm.hideMask($index)'>No</button>
                        <button class='btn btn-xs btn-danger' ng-click='groupVm.deleteGroupLocation(groupLocation)'>
                            Yes
                        </button>
                    </div>
                </div>
            </div>
            <div id="groupLocationValueMask{{$index}}" class="groupLocation-container-mask centered-container">
                <div class="centered-child">
                    <h5>Please confirm to delete this location</h5>

                    <div>
                        <button class='btn btn-xs btn-default' ng-click='groupVm.hideMask($index)'>No</button>
                        <button class='btn btn-xs btn-danger' ng-click='groupVm.deleteLocation(groupLocation)'>Yes
                        </button>
                    </div>
                </div>
            </div>
            <div class="groupLocation-header">
                <span ng-if="!groupLocation.editTitle" ng-dblclick="groupLocation.editTitle = true;
                $('#titleInput{{$index}}').setSelectionRange(0, this.location.length)">{{groupLocation.newName}}</span>

                <div ng-if="groupLocation.editTitle" style="display: flex">
                    <input type="text" style="width: 270px"
                           id="titleInput{{$index}}"
                           ng-model="groupLocation.newName"
                           ng-enter="groupVm.applyChanges(groupLocation)"
                           onfocus="this.setSelectionRange(0, this.location.length)">

                    <div class="buttons" style="display: inline-block; margin-left: -35px;">
                        <i title="Save Group Location" class="fa fa-check mr10"
                           ng-click="groupLocation.editTitle = false;groupVm.applyChanges(groupLocation)"></i>
                        <i title="Cancel Changes" class="fa fa-times"
                           ng-click="groupVm.cancelGroupLocationChange(groupLocation);groupLocation.editTitle = false;"></i>
                    </div>
                </div>
                <div class="buttons" ng-if="!groupLocation.editTitle">
                    <i ng-if="groupLocation.id != null<%-- && hasPermission('permission.settings.edit')--%>"
                       title="Add New Value"
                       class="fa fa-plus mr10"
                       ng-click="groupVm.addLocation(groupLocation, $index)"></i>
                    <i title="Delete this groupLocation"
                       class="fa fa-times" <%--ng-if="hasPermission('permission.settings.edit')"--%>
                       ng-click="groupVm.promptDeleteGroupLocation($index)"></i>
                </div>
            </div>
            <div class="progress progress-striped active"
                 style="border-radius: 0;height: 5px;"
                 ng-if="groupLocation.showBusy">
                <div class="progress-bar"
                     role="progressbar" aria-valuenow="100" aria-valuemin="0"
                     aria-valuemax="100" style="width: 100%">
                </div>
            </div>
            <div class="groupLocation-body" id="groupLocation-body{{$index}}">
                <div class="groupLocation-location" ng-repeat="location in groupLocation.locationObjects"
                     ng-class="{'edit-mode': location.editMode}">
                    <div ng-if="location.editMode" style="display: flex">
                        <span class="name" style="flex-grow: 1">
                            <input type="text"
                                   ng-model="location.newName"
                                   ng-enter="location.editMode = false;location.newMode = false;location.name = location.newName;groupVm.applyChanges(groupLocation, location)"
                                   onfocus="this.setSelectionRange(0, this.location.length)">
                        </span>
                        <span class="buttons">
                            <i title="Save Changes" class="fa fa-check mr5"
                               ng-click="location.editMode = false;location.newMode = false;location.name = location.newName;groupVm.applyChanges(groupLocation, location)">
                            </i>
                            <i title="Cancel Changes"
                               ng-click="location.editMode = false;location.newName = location.name; groupVm.cancelChanges(groupLocation, location)"
                               class="fa fa-times"></i>
                        </span>
                    </div>

                    <div ng-if="!location.editMode">
                        <span class="name" ng-dblclick="location.editMode = true">{{location.name}}</span>
                        <span class="buttons">
                            <i title="Edit" class="fa fa-edit mr5"
                            <%--ng-if="hasPermission('permission.settings.edit')"--%>
                               ng-click="groupVm.toDeleteValue = null;location.editMode = true"></i>
                            <i title="Delete Value" <%--ng-if="hasPermission('permission.settings.delete')"--%>
                               ng-click="groupVm.toDeleteValue = location;groupVm.promptDeleteLocation($parent.$parent.$index)"
                               class="fa fa-times"></i>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>