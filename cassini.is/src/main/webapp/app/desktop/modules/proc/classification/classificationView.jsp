<div class="view-container" fitcontent>

    <style scoped>
        .material-node {
            background: transparent url("app/assets/images/cart.png") no-repeat !important;
            height: 16px;
        }

        .machine-node {
            background: transparent url("app/assets/images/machine2.png") no-repeat !important;
            height: 16px;
        }

        .manpower-node {
            background: transparent url("app/assets/images/group1.png") no-repeat !important;
            height: 16px;
        }
    </style>
    <div class="view-toolbar">

        <button class="btn btn-sm btn-success min-width" ng-click="classVm.onSave()"
                ng-disabled="classVm.checkPermission() == false">Save
        </button>

    </div>
    <div class="view-content no-padding" style="overflow-y: auto;overflow-x: hidden;">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li ng-click="!hasPermission('permission.masterdata.addType') || classVm.addType()"
                    ng-class="{'disabled':!hasPermission('permission.masterdata.addType')}">
                    <a tabindex="-1" id="addType" href="">Add Type</a>
                </li>
                <li ng-click="!hasPermission('permission.masterdata.deleteType') || classVm.deleteType()"
                    ng-class="{'disabled':!hasPermission('permission.masterdata.deleteType')}">
                    <a tabindex="-1" id="deleteType" href="">Delete Type</a>
                </li>
            </ul>
        </div>

        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane" style="width: 300px;">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <ul id="classificationTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane noselect" style="left:300px;">
                <div ng-if="classVm.type == 'MATERIALTYPE'"
                     ng-include="'app/desktop/modules/proc/classification/details/materialTypeDetailsView.jsp'"
                     ng-controller="MaterialTypeDetailsController as materialTypeVm"></div>
                <div ng-if="classVm.type == 'MACHINETYPE'"
                     ng-include="'app/desktop/modules/proc/classification/details/machineTypeDetailsView.jsp'"
                     ng-controller="MachineTypeDetailsController as machineTypeVm"></div>
                <div ng-if="classVm.type == 'MANPOWERTYPE'"
                     ng-include="'app/desktop/modules/proc/classification/details/manpowerTypeDetailsView.jsp'"
                     ng-controller="ManPowerTypeDetailsController as manpowerTypeVm"></div>
                <div ng-if="classVm.type == 'MATERIALRECEIVETYPE'"
                     ng-include="'app/desktop/modules/proc/classification/details/receiveTypeDetailsView.jsp'"
                     ng-controller="ReceiveTypeDetailsController as receiveTypeVm"></div>
                <div ng-if="classVm.type == 'MATERIALISSUETYPE'"
                     ng-include="'app/desktop/modules/proc/classification/details/issueTypeDetailsView.jsp'"
                     ng-controller="IssueTypeDetailsController as issueTypeVm"></div>
            </div>
        </div>
    </div>
</div>
