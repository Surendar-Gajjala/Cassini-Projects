<div class="view-container" fitcontent>

    <style scoped>
        .configuration-node {
            background: transparent url("app/assets/images/configuration.png") no-repeat !important;
            height: 16px;
        }

        .scenario-node {
            background: transparent url("app/assets/images/scenario (2).png") no-repeat !important;
            height: 16px;
        }

        .plan-node {
            background: transparent url("app/assets/images/sketch.png") no-repeat !important;
            height: 16px;
        }

        .suite-node {
            background: transparent url("app/assets/images/settings.png") no-repeat !important;
            height: 16px;
        }

        .case-node {
            background: transparent url("app/assets/images/file-case.png") no-repeat !important;
            height: 16px;
        }

        .toggleColor1 {
            color: #A8A2A2
        }

        .toggleColor2 {
            color: white;
        }

        .my-search {
            border: 0;
            outline: 0;
            background: transparent;
            box-shadow: 0px 0px 0px #d3d3d3;
            border-bottom: 1px solid #d3d3d3;
            margin: 5px 0px;
            width: 210px;
            text-align: center;
        }

        .search-form {
            border-radius: 30px;
        }
    </style>

    <div class="view-toolbar">
        <button class="btn btn-xs btn-success min-width" ng-click="configVm.onSave()">Save
        </button>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="newRunConfiguration" ng-click="configVm.createRunConfig()">
                    New Run Configuration</a></li>
            </ul>
        </div>
        <div id="runContextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="deleteRun" ng-click="configVm.deleteConfiguration()">Delete
                    Configuration</a>
                </li>
            </ul>
        </div>

        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <div style="height: 40px;">
                        <input type="search" class="form-control input-sm search-form" placeholder="Search"
                               ng-model="configVm.searchValue" ng-change="configVm.searchTree()">
                    </div>

                    <ul id="runConfigTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane noselect" style="left:300px;overflow: hidden">
                <div ng-include="'app/desktop/modules/config/details/configurationDetailsView.jsp'"
                     ng-controller="ConfigurationDetailsController as configurationDetailsVm"></div>
            </div>
        </div>
    </div>
</div>
