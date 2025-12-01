<style scoped>
    .stickyheader {
        overflow: auto !important;
    }
</style>
<div class="stickyheader">

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
            /*justify-content: center;*/
        }

        .flex-row > .flex-col {
            margin-left: 30px;
        }

        .lov-container {
            width: 300px;
            min-width: 300px;
            border: 1px solid #dae6ea;
            border-radius: 5px;
            margin-bottom: 30px;
            background-image: var(--cassini-linear-gradient);
        }

        .lov-container > div > .lov-header {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            display: flex;
            word-break: break-all;
        }

        .lov-container > div > .lov-header > h5 {
            margin: 0 !important;
            font-size: 16px;
            display: inline-block;
            font-weight: 600;
            word-break: break-word;
        }

        .lov-container > div > .lov-header input {
            font-size: 16px;
        }

        .lov-container > div > .lov-header > input {
            flex-grow: 1;
        }

        .lov-container > div > .lov-header > .buttons {
            display: inline-block;
            flex-grow: 1;
            text-align: right;
            min-width: 50px;
        }

        .lov-container .buttons i:hover {
            color: #0390fd;
            cursor: pointer;
        }

        .lov-container > div > .lov-body {
            padding: 10px;
            height: 200px;
            max-height: 200px;
            overflow-y: auto;
        }

        .lov-container .lov-body .lov-value {
            cursor: pointer;
        }

        .lov-container > div > .lov-body > .lov-value > div {
            border-bottom: 1px dotted #6d9bbd;
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

        /*.lov-container .lov-body .lov-value:hover {
            background-color: #0081c2;
            color: #fff;
        }*/

        .lov-container .lov-body .lov-value.edit-mode {
            background-color: transparent;
            color: inherit;
        }

        .lov-container .lov-body .lov-value:hover .buttons i {
            cursor: pointer;
        }

        /*.lov-container .lov-body .lov-value:hover .buttons i {
            color: #fff;
        }*/

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
            background-image: none;
        }

        #lovAddButton:hover i {
            color: #fff !important;
        }

    </style>

    <div class="flex-row flex-row-center">
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
            <div>
                <div class="lov-header">
                    <h5 ng-if="!lov.editTitle" ng-dblclick="lovsVm.editLov(lov);
                $('#titleInput{{$index}}').setSelectionRange(0, this.value.length)" title="{{lov.newName}}">
                        {{lov.newName}}</h5>

                    <div ng-if="lov.editTitle" style="display: flex">
                        <input type="text" style="width: 230px"
                               id="titleInput{{$index}}"
                               ng-model="lov.newName"
                               ng-enter="lovsVm.applyChanges(lov)"
                               onfocus="this.setSelectionRange(0, this.value.length)">

                        <div class="buttons" style="display: inline-block; margin-left: 3px;">
                            <i title="{{saveChangesTitle}}" class="las la-check"
                               ng-click="lov.editTitle = false;lovsVm.applyChanges(lov)"></i>
                            <i title="{{lovCancel}}" class="la la-times"
                               ng-click="lovsVm.cancelLovChange(lov);lov.editTitle = false;"></i>
                        </div>
                    </div>
                    <div class="buttons" ng-if="!lov.editTitle" title="{{lov.usedLov == true ? lovAlreadyInUse : ''}}">
                        <i ng-if="lov.id != null && hasPermission('settings','edit')"
                           title="{{addNewValueTitle}}"
                           class="la la-plus"
                           ng-click="lovsVm.addLovValue(lov, $index)"></i>
                        <i class="la la-times"
                           ng-class="{'disabled': lov.usedLov == true}"
                           title="{{deleteLovTitle}}"
                           ng-if="hasPermission('settings','edit')"
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
                         ng-class="{'edit-mode': value.editMode}"
                         draglov lov="lov" values="lov.valueObjects" update-lov="updateLovSequence">
                        <div ng-if="value.editMode" style="display: flex">
                        <span class="name" style="flex-grow: 1">
                            <input type="text"
                                   ng-model="value.newString"
                                   ng-enter="value.editMode = false;value.string = value.newString;lovsVm.applyChangesList(value,lov)"
                                   onfocus="this.setSelectionRange(0, this.value.length)">
                        </span>
                        <span class="buttons">
                            <i title="{{saveChangesTitle}}" class="las la-check mr5"
                               ng-click="value.editMode = false;value.newMode = false;value.string = value.newString;lovsVm.applyChangesList(value,lov)">
                            </i>
                            <i title="{{lovCancel}}"
                               ng-click="value.editMode = false;value.newString = value.string; lovsVm.cancelChangesList(lov, value)"
                               class="la la-times"></i>
                        </span>
                        </div>

                        <div ng-if="!value.editMode">
                            <span class="name" ng-dblclick="lovsVm.editLovValue(value,lov)">{{value.string}}</span>
                        <span class="buttons"
                              title="{{lov.usedLov == true ? lovAlreadyInUse : ''}}" style="pointer-events: auto;">
                            <i title="{{'EDIT_VALUE' | translate}}" class="fa fa-edit"
                               ng-if="hasPermission('settings','edit')"
                               ng-click="lovsVm.editLovValue(value,lov)"></i>
                            <i title="{{deleteValueTitle}}" ng-if="hasPermission('settings','delete')"
                               ng-click="lovsVm.toDeleteValue = value;lovsVm.promptDeleteLovValue(value)"
                               class="la la-times"></i>
                            <i class="la la-cog" style="visibility: initial;" title="Default Value"
                               ng-if="value.string == lov.defaultValue"
                               ng-click="lovsVm.saveLov(lov)"></i>
                            <i class="la la-cog" ng-if="value.string != lov.defaultValue"
                               ng-click="lov.defaultValue = value.string;lovsVm.saveLov(lov)"
                               title="Set as default value"></i>
                        </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>