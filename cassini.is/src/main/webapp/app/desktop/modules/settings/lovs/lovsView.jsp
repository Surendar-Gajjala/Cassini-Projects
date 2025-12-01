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

        .lov-container {
            width: 300px;
            min-width: 300px;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin-bottom: 10px;
        }

        .lov-container > .lov-header {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            display: flex;
            word-break: break-all;
        }

        .lov-container > .lov-header > h5 {
            margin: 0 !important;
            font-size: 16px;
            display: inline-block;
            font-weight: 500;
        }

        .lov-container > .lov-header input {
            font-size: 16px;
        }

        .lov-container > .lov-header > input {
            flex-grow: 1;
        }

        .lov-container > .lov-header > .buttons {
            display: inline-block;
            flex-grow: 1;
            text-align: right;
        }

        .lov-container .buttons i:hover {
            color: #0390fd;
            cursor: pointer;
        }

        .lov-container > .lov-body {
            padding: 10px;
            height: 200px;
            max-height: 200px;
            overflow-y: auto;
        }

        .lov-container .lov-body .lov-value {
            cursor: pointer;
        }

        .lov-container > .lov-body > .lov-value > div {
            border-bottom: 1px dotted #e3e3e3;
            padding: 5px;
            display: flex;
            justify-content: left;
        }

        .lov-container .lov-body .lov-value .name,
        .lov-container .lov-body .lov-value .type {
            flex-grow: 1;
            justify-content: left;
            text-align: left;
        }

        .lov-container .lov-body .lov-value .buttons {
            visibility: hidden;
            text-align: right;
        }

        .lov-container .lov-body .lov-value:hover .buttons {
            visibility: visible;
        }

        .lov-container .lov-body .lov-value.edit-mode .buttons {
            visibility: visible;
        }

        .lov-container input {
            border: 0;
            outline: 0;
            background: transparent;
            border-bottom: 1px dashed lightgrey;
        }

        .lov-container .lov-body .lov-value.edit-mode .type {
            border-bottom: 1px dashed lightgrey;
            margin-right: 10px;
            text-align: left;
        }

        .lov-container .lov-body .lov-value .type a {
            text-decoration: none;
            color: inherit;
        }

        .lov-container .lov-body .lov-value .type .caret {
            border-top: 5px dashed;
            border-right: 5px solid transparent;
            border-left: 5px solid transparent;
        }

        .lov-container .lov-body .lov-value:hover {
            background-color: #0081c2;
            color: #fff;
        }

        .lov-container .lov-body .lov-value.edit-mode {
            background-color: transparent;
            color: inherit;
        }

        .lov-container .lov-body .lov-value:hover .buttons i {
            cursor: pointer;
        }

        .lov-container .lov-body .lov-value:hover .buttons i {
            color: #fff;
        }

        .lov-container .lov-body .lov-value.edit-mode .buttons i:hover {
            color: #0390fd;
        }

        .lov-container .lov-body .lov-value.edit-mode .buttons i {
            color: inherit;
        }

        .lov-container .lov-body .lov-value .name input {
            width: 98%;
        }

        .lov-container .lov-container-mask {
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

        .lovp-dropdown {
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

        #lovAddButton:hover {
            background-color: #0081c2;
            cursor: pointer;
        }

        #lovAddButton:hover i {
            color: #fff !important;
        }

    </style>

    <div class="flex-row flex-row-center">
        <div id="lovAddButton" ng-if="hasPermission('permission.settings.new')"
             title="{{'NEW_LOV' | translate}}"
             ng-click='lovsVm.addLov()'
             class="flex-col lov-container"
             style="display:table;height: 250px;">
            <div style="display: table-cell;vertical-align: middle;text-align: center;">
                <i class="fa fa-plus" style="font-size: 100px;vertical-align: middle;color: #adadad;"></i>
            </div>
        </div>
        <div id="lov{{$index}}" class="flex-col lov-container" ng-repeat="lov in lovsVm.lovs">
            <div id="lovMask{{$index}}" class="lov-container-mask centered-container">
                <div class="centered-child">
                    <h5 translate>DELETE_LOV_MESSAGE</h5>

                    <div>
                        <button class='btn btn-xs btn-default' ng-click='lovsVm.hideMask($index)' translate>NO</button>
                        <button class='btn btn-xs btn-danger' ng-click='lovsVm.deleteLov(lov)' translate>YES</button>
                    </div>
                </div>
            </div>
            <div id="lovValueMask{{$index}}" class="lov-container-mask centered-container">
                <div class="centered-child">
                    <h5 translate>DELETE_LOV_VALUE_MESSAGE</h5>

                    <div>
                        <button class='btn btn-xs btn-default' ng-click='lovsVm.hideMask($index)' translate>NO</button>
                        <button class='btn btn-xs btn-danger' ng-click='lovsVm.deleteLovValue(lov)' translate>YES
                        </button>
                    </div>
                </div>
            </div>
            <div class="lov-header">
                <h5 ng-if="!lov.editTitle" ng-dblclick="lov.editTitle = true;
                $('#titleInput{{$index}}').setSelectionRange(0, this.value.length)" title="{{lov.newName}}">
                    {{lov.newName | limitTo: 24 }}{{lov.newName.length > 24 ? '...' : ''}}</h5>

                <div ng-if="lov.editTitle" style="display: flex">
                    <input type="text" style="width: 230px"
                           id="titleInput{{$index}}"
                           ng-model="lov.newName"
                           ng-enter="lovsVm.applyChanges(lov)"
                           onfocus="this.setSelectionRange(0, this.value.length)">

                    <div class="buttons" style="display: inline-block; margin-left: 3px;">
                        <i title="{{saveChangesTitle}}" class="fa fa-check mr10"
                           ng-click="lov.editTitle = false;lovsVm.applyChanges(lov)"></i>
                        <i title="{{lovCancel}}" class="fa fa-times"
                           ng-click="lovsVm.cancelLovChange(lov);lov.editTitle = false;"></i>
                    </div>
                </div>
                <div class="buttons" ng-if="!lov.editTitle">
                    <i ng-if="lov.id != null && hasPermission('permission.settings.edit')" title="{{addNewValueTitle}}"
                       class="fa fa-plus mr10"
                       ng-click="lovsVm.addLovValue(lov, $index)"></i>
                    <i title="{{deleteLovTitle}}" class="fa fa-times" ng-if="hasPermission('permission.settings.edit')"
                       ng-click="lovsVm.promptDeleteLov($index)"></i>
                </div>
            </div>
            <div class="progress progress-striped active"
                 style="border-radius: 0;height: 5px;"
                 ng-if="lov.showBusy">
                <div class="progress-bar"
                     role="progressbar" aria-valuenow="100" aria-valuemin="0"
                     aria-valuemax="100" style="width: 100%">
                </div>
            </div>
            <div class="lov-body" id="lov-body{{$index}}">
                <div class="lov-value" ng-repeat="value in lov.valueObjects" id="{{$index}}"
                     ng-mouseover="lovsVm.forLovIndex(lov,$index)" drag-me
                     drop-on-me ng-class="{'edit-mode': value.editMode}">
                    <div ng-if="value.editMode" style="display: flex">
                        <span class="name" style="flex-grow: 1">
                            <input type="text"
                                   ng-model="value.newString"
                                   ng-enter="value.editMode = false;value.string = value.newString;lovsVm.applyChangesList(value,lov)"
                                   onfocus="this.setSelectionRange(0, this.value.length)">
                        </span>
                        <span class="buttons">
                            <i title="{{saveChangesTitle}}" class="fa fa-check mr5"
                               ng-click="value.editMode = false;value.newMode = false;value.string = value.newString;lovsVm.applyChangesList(value,lov)">
                            </i>
                            <i title="{{lovCancel}}"
                               ng-click="value.editMode = false;value.newString = value.string; lovsVm.cancelChangesList(lov, value)"
                               class="fa fa-times"></i>
                        </span>
                    </div>

                    <div ng-if="!value.editMode">
                        <span class="name" ng-dblclick="value.editMode = true">{{value.string}}</span>
                        <span class="buttons">
                            <i title="{{'EDIT_VALUE' | translate}}" class="fa fa-edit mr5"
                               ng-if="hasPermission('permission.settings.edit')"
                               ng-click="lovsVm.toDeleteValue = null;value.editMode = true"></i>
                            <i title="{{deleteValueTitle}}" ng-if="hasPermission('permission.settings.delete')"
                               ng-click="lovsVm.toDeleteValue = value;lovsVm.deleteLovValue(lov)"
                               class="fa fa-times"></i>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>