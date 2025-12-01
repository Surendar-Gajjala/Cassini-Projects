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

        .lc-container {
            width: 300px;
            min-width: 300px;
            border: 1px solid #dae6ea;
            border-radius: 5px;
            margin-bottom: 30px;
            background-image: var(--cassini-linear-gradient);
        }

        .lc-container > div > .lc-header {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            display: flex;
            word-break: break-all;
        }

        .lc-container > div > .lc-header > h5 {
            margin: 0 !important;
            font-size: 16px;
            font-weight: 600;
            display: inline-block;
            word-break: break-word;
        }

        .lc-container > div > .lc-header input {
            font-size: 16px;
        }

        .lc-container > div > .lc-header > input {
            flex-grow: 1;
        }

        .lc-container > div > .lc-header > .buttons {
            display: inline-block;
            flex-grow: 1;
            text-align: right;
            min-width: 50px;
        }

        .lc-container .buttons i:hover {
            color: #0390fd;
            cursor: pointer;
        }

        .lc-container > div > .lc-body {
            padding: 10px;
            height: 200px;
            max-height: 200px;
            overflow-y: auto;
        }

        .lc-container .lc-body .lc-phase {
            cursor: pointer;
        }

        .lc-container > div > .lc-body > .lc-phase > div {
            border-bottom: 1px dotted #6d9bbd;
            padding: 5px;
            display: flex;
            justify-content: left;
            flex: 0 0;
        }

        .lc-container .lc-body .lc-phase .name,
        .lc-container .lc-body .lc-phase .type {
            flex-grow: 1;
            justify-content: left;
            text-align: left;
            width: 78px;
        }

        .lc-container .lc-body .type .dropdown {
            text-align: left !important;
            margin-left: 4px !important;
        }

        .lc-container .lc-body .lc-phase .buttons {
            visibility: hidden;
            text-align: right;
            width: 50px;
        }

        .lc-container .lc-body .lc-phase:hover .buttons {
            visibility: visible;
        }

        .lc-container .lc-body .lc-phase.edit-mode .buttons {
            visibility: visible;
        }

        .lc-container input {
            border: 0;
            outline: 0;
            background: transparent;
            border-bottom: 1px dashed lightgrey;
        }

        .lc-container .lc-body .lc-phase.edit-mode .type {
            border-bottom: 1px dashed lightgrey;
            margin-right: 10px;
            text-align: left;
        }

        .lc-container .lc-body .lc-phase .type a {
            text-decoration: none;
            color: inherit;
        }

        .lc-container .lc-body .lc-phase .type .caret {
            border-top: 5px dashed;
            border-right: 5px solid transparent;
            border-left: 5px solid transparent;
        }

        .lc-container .lc-body .lc-phase:hover {
            /*background-color: #0081c2;*/
            /*color: #fff;*/
        }

        .lc-container .lc-body .lc-phase.edit-mode {
            background-color: transparent;
            color: inherit;
        }

        .lc-container .lc-body .lc-phase:hover .buttons i {
            cursor: pointer;
        }

        .lc-container .lc-body .lc-phase:hover .buttons i {
            /*color: #fff;*/
        }

        .lc-container .lc-body .lc-phase.edit-mode .buttons i:hover {
            color: #0390fd;
        }

        .lc-container .lc-body .lc-phase.edit-mode .buttons i {
            color: inherit;
        }

        .lc-container .lc-container-mask {
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

        .lcp-dropdown {
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

        #lifecycleAddButton:hover {
            background-color: #0081c2;
            cursor: pointer;
            background-image: none;
        }

        #lifecycleAddButton:hover i {
            color: #fff !important;
        }
    </style>
    <!--
    <div class="text-right">
        <button class='btn btn-sm btn-white'
                title="New Lifecycle"
                style="position: absolute;right: 20px;top: 30px;width: 40px;"
                ng-click='lcsVm.addLifecycle()'>
            <i class="la la-plus" style="font-size: 20px;vertical-align: middle;color: #adadad;"></i>
        </button>
    </div>
    -->
    <div class="flex-row flex-row-center">
        <div id="lifecycle{{$index}}" class="flex-col lc-container" ng-repeat="lc in lcsVm.lifecycles">
            <div id="lifecycleMask{{$index}}" class="lc-container-mask centered-container">
                <div class="centered-child">
                    <h5 translate>DELETE_LIFE_CYCLE_MESSAGE</h5>

                    <div>
                        <button class='btn btn-xs btn-default' ng-click='lcsVm.hideMask($index)' translate>NO</button>
                        <button class='btn btn-xs btn-danger' ng-click='lcsVm.deleteLifecycle(lc)' translate>YES
                        </button>
                    </div>
                </div>
            </div>
            <div id="lifecyclePhaseMask{{$index}}" class="lc-container-mask centered-container">
                <div class="centered-child">
                    <h5 translate>DELETE_PHASE_MESSAGE</h5>

                    <div>
                        <button class='btn btn-xs btn-default' ng-click='lcsVm.hideMask($index)' translate>NO</button>
                        <button class='btn btn-xs btn-danger'
                                ng-click='lcsVm.deleteLifecyclePhase(lc, lcsVm.toDeletePhase)' translate>YES
                        </button>
                    </div>
                </div>
            </div>
            <div>
                <div class="lc-header">
                    <h5 ng-if="!lc.editTitle" ng-dblclick="lc.editTitle = true;
                $('#titleInput{{$index}}').setSelectionRange(0, this.value.length)" title="{{lc.newName}}">
                        {{lc.newName}}</h5>

                    <div ng-if="lc.editTitle" style="display: flex">
                        <input type="text" style="width: 230px"
                               id="titleInput{{$index}}"
                               ng-model="lc.newName"
                               onfocus="this.setSelectionRange(0, this.value.length)">

                        <div class="buttons" style="display: inline-block; margin-left: 3px;">
                            <i title="{{saveLifecycleTitle}}" class="las la-check"
                               ng-click="lc.editTitle = false;lcsVm.saveLifecycle(lc)"></i>
                            <i title="{{cancelChanges}}" class="la la-times"
                               ng-click="lcsVm.checkAndRemoveLifecycle(lc);lc.editTitle = false;"></i>
                        </div>
                    </div>
                    <div class="buttons" ng-if="!lc.editTitle"
                         title="{{lc.usedLifeCycle == true ? lifecycleAlreadyInUse:''}}">
                        <i ng-if="lc.id != null && hasPermission('settings','edit')"
                           title="{{addLifecycleTitle}}"
                           class="la la-plus"
                           ng-click="lcsVm.addLifecyclePhase(lc, $index)"></i>
                        <i class="la la-times"
                           title="{{deletLifecyclePhaseTitle}}"
                           ng-if="hasPermission('settings','delete')"
                           ng-class="{'disabled': lc.usedLifeCycle == true}"
                           ng-click="lcsVm.promptDeleteLifecycle($index)"></i>
                    </div>
                </div>
                <div class="progress progress-striped active"
                     style="border-radius: 0;height: 5px;"
                     ng-if="lc.showBusy">
                    <div class="progress-bar"
                         role="progressbar" aria-valuenow="100" aria-valuemin="0"
                         aria-valuemax="100" style="width: 100%">
                    </div>
                </div>
                <div class="lc-body" id="lc-body{{$index}}">
                    <div class="lc-phase" ng-repeat="phase in lc.phases" ng-class="{'edit-mode': phase.editMode}">
                        <div ng-if="phase.editMode">
                        <span class="name">
                            <input type="text" style="width: 95px;"
                                   ng-model="phase.newPhase"
                                   onfocus="this.setSelectionRange(0, this.value.length)">
                        </span>
                        <span class="type">
                            <div class="dropdown dropdown-append-to-body">
                                <a href="" type="button" id="dropdownMenuButton{{$parent.$parent.$index}}{{$index}}"
                                   ng-click="lcsVm.initDropdown($parent.$parent.$index, $index)"
                                   data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <span ng-if="phase.newPhaseType != null">{{phase.newPhaseType}}</span>
                                    <span ng-if="phase.newPhaseType == null" translate>SELECT_TYPE</span>
                                    <span class="caret"></span>
                                </a>
                                <ul id="dropdownMenu{{$parent.$parent.$index}}{{$index}}"
                                    class="dropdown-menu lcp-dropdown" aria-labelledby="dropdownMenuButton{{$index}}">
                                    <li ng-repeat="pType in lcsVm.phaseTypes">
                                        <a class="dropdown-item" href=""
                                           ng-click="phase.newPhaseType = pType">{{pType}}</a>
                                    </li>
                                </ul>
                            </div>
                        </span>
                        <span class="buttons">
                            <i title="{{saveLifecycleTitle}}" class="las la-check mr5"
                               ng-click="lcsVm.saveLifecyclePhase(lc, phase)">
                            </i>
                            <i title="{{deleteLifecycleTitle}}"
                               ng-click="phase.editMode = false;lcsVm.deleteLifecyclePhase(lc, phase)"
                               class="la la-times"></i>
                        </span>
                        </div>

                        <div ng-if="!phase.editMode">
                            <span class="name">{{phase.phase}}</span>
                            <span class="type">{{phase.phaseType}}</span>
                        <span class="buttons"
                              title="{{lc.usedLifeCycle == true ? lifecycleAlreadyInUse:''}}">
                            <i title="{{'EDIT_LIFE_CYCLE_PHASE' | translate}}" class="la la-edit"
                               ng-if="hasPermission('settings','edit')"
                               ng-click="lcsVm.toDeletePhase = null; phase.editMode = true"
                               ng-class="{'disabled': lc.usedLifeCycle == true}"></i>
                            <i title="{{deleteLifecycleTitle}}" ng-if="hasPermission('settings','delete')"
                               ng-click="lcsVm.toDeletePhase = phase; lcsVm.deleteLifecyclePhase(lc,phase)"
                               ng-class="{'disabled': lc.usedLifeCycle == true}"
                               class="la la-times"></i>
                        </span>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>