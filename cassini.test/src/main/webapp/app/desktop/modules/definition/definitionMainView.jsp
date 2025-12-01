<div class="view-container" fitcontent>


    <style scoped>
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
        <button class="btn btn-xs btn-success min-width" ng-click="definitionMainVm.onSave()">Save
        </button>
        <label class="btn fileContainer" style="margin-bottom: 0px !important;padding-top: 5px;"
               title="Click to Import scenario">Import
            <input type="file" id="scenarioFile" value="file" onchange="angular.element(this).scope().importScenario()"
                   style="display: none">
        </label>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px;">
        <div id="contextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="newScenario" ng-click="definitionMainVm.createType()">New Scenario</a>
                </li>
            </ul>
        </div>

        <div id="planContextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="export" ng-click="exportTestCases()">Export</a></li>
                <li><a tabindex="-1" href="" id="testPlan" ng-click="definitionMainVm.createType()">New Plan</a></li>
                <li><a tabindex="-1" href="" id="deleteScenario" ng-click="definitionMainVm.deleteType()">Delete
                    Scenario</a></li>
            </ul>
        </div>

        <div id="suiteContextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="testSuite" ng-click="definitionMainVm.createType()">New Suite</a></li>
                <li><a tabindex="-1" href="" id="deletePlan" ng-click="definitionMainVm.deleteType()">Delete
                    Plan</a></li>
            </ul>
        </div>

        <div id="caseContextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="testCase" ng-click="definitionMainVm.createType()">New Test Case</a>
                </li>
                <li><a tabindex="-1" href="" id="deleteSuite" ng-click="definitionMainVm.deleteType()">Delete Suite</a>
                </li>
            </ul>
        </div>
        <div id="delTcContextMenu" class="context-menu dropdown clearfix"
             style="position: fixed;display:none; z-index: 9999">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu" style="">
                <li><a tabindex="-1" href="" id="deleteTest" ng-click="definitionMainVm.deleteType()">Delete Test
                    Case</a>
                </li>
            </ul>
        </div>
        <div class="split-pane fixed-left">
            <div class="split-pane-component split-left-pane">
                <div id="classificationContainer" class="classification-pane" data-toggle="context"
                     data-target="#context-menu">
                    <div style="height: 40px;">
                        <input type="search" class="form-control input-sm search-form" placeholder="Search"
                               ng-model="definitionMainVm.searchValue" ng-change="definitionMainVm.searchTree()">
                    </div>
                    <ul id="testTree" class="easyui-tree">
                    </ul>
                </div>
            </div>
            <div class="split-pane-divider"></div>
            <div class="split-pane-component split-right-pane noselect" style="left:300px;overflow: hidden">
                <div ng-include="'app/desktop/modules/definition/details/definitionDetailsView.jsp'"
                     ng-controller="DefinitionDetailsController as definitionDetailsVm"></div>
            </div>
        </div>
    </div>
</div>
