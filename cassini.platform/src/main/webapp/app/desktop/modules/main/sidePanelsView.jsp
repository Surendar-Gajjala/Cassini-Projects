<div>
    <style scoped>
        /* Side Panel CSS */

        .app-side-panel {
            position: fixed;
            top: 127px;
            bottom: 30px;
            z-index: 9999;
        }

        .app-side-panel .side-panel-content {

        }

        .app-side-panel .side-panel-content > div {
            height: 100%;
        }

        .app-side-panel.right {
            right: 0;
            width: 500px;
            margin-right: -500px;
            border-left: 1px solid #ddd;
        }

        .app-side-panel.left {
            left: 0;
            width: 500px;
            margin-left: -500px;
            border-right: 1px solid #ddd;
        }

        .side-panel-close-btn {
            color: #949798;
            font-size: 30px;
            cursor: pointer;
        }

        .app-side-panel .panel-close {
            position: absolute;
            right: 10px;
            top: 12px;
            width: 32px;
            height: 32px;
            opacity: 0.3;
        }

        .app-side-panel .panel-close:hover {
            opacity: 1;
        }

        .app-side-panel .panel-close:before, .app-side-panel .panel-close:after {
            position: absolute;
            left: 15px;
            content: ' ';
            height: 25px;
            width: 2px;
            background-color: #333;
        }

        .app-side-panel .panel-close:before {
            transform: rotate(45deg);
        }

        .app-side-panel .panel-close:after {
            transform: rotate(-45deg);
        }

        .app-side-panel .buttons-panel {
            height: 50px;
            padding: 8px;
            width: 500px;
            background-color: #eee;
            border-top: 1px solid #ddd;
            border-left: 1px solid #ddd;
        }

        .app-side-panel .buttons-panel .btn-sm {
            min-width: 75px;
        }

        .app-side-panel.right .buttons-panel {
            border-left: 0;
        }

        .app-side-panel.left .buttons-panel {
            border-right: 0;
        }
    </style>
    <div id="rightSidePanel" class="app-side-panel right" sidepanelcontent>
        <div class="row" style="margin: 0">
            <div class="col-sm-12 text-right" style="padding: 0">
                <div>
                    <h3 style="text-align: center;margin: 12px 40px 0 10px;">{{sidePanelsVm.rightSidePanelOptions.title}}</h3>
                    <a href="" ng-click="sidePanelsVm.hideSidePanel('right')" class="panel-close pull-right" id="closeRightSidePanel"
                       style="display: inline-block"></a>
                </div>
                <hr style="margin: 10px 0 0 0;">
                <div class="progress progress-striped active"
                     style="border-radius: 0;"
                     ng-if="sidePanelsVm.rightSidePanelOptions.showBusy">
                    <div class="progress-bar"
                         role="progressbar" aria-valuenow="100" aria-valuemin="0"
                         aria-valuemax="100" style="width: 100%">
                    </div>
                </div>
                <div id="rightSidePanelContent" class="text-left side-panel-content" style="overflow: auto;">

                </div>
                <div id="rightSidePanelButtonsPanel"
                     class="buttons-panel"
                     ng-if="sidePanelsVm.rightSidePanelOptions.buttons != null &&
                            sidePanelsVm.rightSidePanelOptions.buttons != undefined &&
                            sidePanelsVm.rightSidePanelOptions.buttons.length > 0"
                     ng-class="{'text-right':sidePanelsVm.rightSidePanelOptions.buttons.length == 1}">
                    <div ng-if="sidePanelsVm.rightSidePanelOptions.buttons.length == 2"
                         ng-repeat="button in sidePanelsVm.rightSidePanelOptions.buttons"
                         ng-class="{'text-left': $index/2 == 0,'text-right': $index/2 != 0}"
                         style="width: 50%;">
                        <button class="btn btn-sm {{button.btnClass}}"
                                style="margin-left: 10px;"
                                ng-disabled="sidePanelsVm.rightSidePanelOptions.showBusy"
                                ng-class="{'btn-success': button.btnClass == null || button.btnClass == undefined}"
                                ng-click="sidePanelsVm.broadcastButtonCallback(button)">
                            {{button.text}}
                        </button>
                    </div>
                    <button ng-if="sidePanelsVm.rightSidePanelOptions.buttons.length != 2"
                            class="btn btn-sm {{button.btnClass}}"
                            style="margin-left: 10px;"
                            ng-disabled="sidePanelsVm.rightSidePanelOptions.showBusy"
                            ng-class="{'btn-success': button.btnClass == null || button.btnClass == undefined}"
                            ng-repeat="button in sidePanelsVm.rightSidePanelOptions.buttons"
                            ng-click="sidePanelsVm.broadcastButtonCallback(button)">{{button.text}}
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div id="leftSidePanel" class="app-side-panel left" sidepanelcontent>
        <div class="row" style="margin: 0">
            <div class="col-sm-12 text-right" style="padding: 0">
                <div>
                    <h3 style="text-align: center;margin: 12px 0 0 10px;">{{sidePanelsVm.leftSidePanelOptions.title}}</h3>
                    <a href="" ng-click="sidePanelsVm.hideSidePanel('left')" class="panel-close pull-right" id="closeLeftSidePanel"
                       style="display: inline-block"></a>
                </div>
                <hr style="margin: 10px 0 0 0;">
                <div class="progress progress-striped active"
                     style="border-radius: 0;"
                     ng-if="sidePanelsVm.leftSidePanelOptions.showBusy">
                    <div class="progress-bar"
                         role="progressbar" aria-valuenow="100" aria-valuemin="0"
                         aria-valuemax="100" style="width: 100%">
                    </div>
                </div>
                <div id="leftSidePanelContent" class="text-left side-panel-content" style="overflow: auto;">

                </div>

                <div id="leftSidePanelButtonsPanel"
                     class="buttons-panel"
                     ng-if="sidePanelsVm.leftSidePanelOptions.buttons != null &&
                            sidePanelsVm.leftSidePanelOptions.buttons != undefined &&
                            sidePanelsVm.leftSidePanelOptions.buttons.length > 0"
                     ng-class="{'text-right':sidePanelsVm.leftSidePanelOptions.buttons.length == 1}">
                    <div ng-if="sidePanelsVm.leftSidePanelOptions.buttons.length == 2"
                         ng-repeat="button in sidePanelsVm.leftSidePanelOptions.buttons"
                         ng-class="{'text-left': $index/2 == 0,'text-right': $index/2 != 0}"
                         style="width: 50%;">
                        <button class="btn btn-sm {{button.btnClass}}"
                                style="margin-left: 10px;"
                                ng-disabled="sidePanelsVm.leftSidePanelOptions.showBusy"
                                ng-class="{'btn-success': button.btnClass == null || button.btnClass == undefined}"
                                ng-click="sidePanelsVm.broadcastButtonCallback(button)">
                            {{button.text}}
                        </button>
                    </div>
                    <button ng-if="sidePanelsVm.leftSidePanelOptions.buttons.length != 2"
                            class="btn btn-sm {{button.btnClass}}"
                            style="margin-left: 10px;"
                            ng-disabled="sidePanelsVm.leftSidePanelOptions.showBusy"
                            ng-class="{'btn-success': button.btnClass == null || button.btnClass == undefined}"
                            ng-repeat="button in sidePanelsVm.leftSidePanelOptions.buttons"
                            ng-click="sidePanelsVm.broadcastButtonCallback(button)">{{button.text}}
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>