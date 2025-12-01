<div>
    <style scoped>
        .home-toolbar .new-button {
            background-color: #0060df !important;
            width: 125px;
            color: #fff;
            font-size: 14px !important;
            padding: 0 5px 3px 5px !important;
            height: 28px !important;
            text-align: left;
            font-weight: normal !important;
            border-radius: 3px;
        }

        .home-toolbar .btn-group.open .btn {
            background-color: #003eaa !important;
            color: #fff;
        }

        .home-toolbar .btn-group.open .btn i,
        .home-toolbar .new-button i.las {
            font-size: 16px !important;
            color: #fff !important;
        }

        .home-toolbar .new-button:hover {
            background-color: #003eaa !important;
        }

        .home-side-panel {
            position: absolute;
            top: 50px;
            bottom: 0;
            left: 0;
            width: 400px;
            /*border-right: 1px solid #ddd;*/
            overflow-y: auto;
            padding: 20px 10px 20px 20px;
        }

        .home-side-panel .sidepanel-widget {
            height: 350px;
            width: 100%;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .home-content-panel {
            position: absolute;
            top: 50px;
            bottom: 0;
            left: 401px;
            right: 0;
            overflow-y: auto;
            padding: 10px 10px 10px 0px;
        }

        .counts-chart-container {
            width: 400px;
            height: 300px;
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 10px;
        }

        .widgets-row {
            display: flex;
            justify-content: center;
        }

        .widgets-row > div {
            flex: 1;
            margin: 10px;
        }

        .home-widget {
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .second-row-second-col {
            display: flex;
            flex-direction: column;
        }

        .second-row-second-col > div {
            flex: 1;
        }

        .second-row-second-col > div:first-child {
            margin-bottom: 20px;
        }

        .widget-header {
            height: 40px;
            border-bottom: 1px solid #ddd;
            padding-left: 10px;
        }

        .widget-header > h5 {
            margin: 0;
            line-height: 40px;
        }

        .widget-body {
            overflow-y: auto;
            height: calc(100% - 40px);
        }

        .apexcharts-canvas .apexcharts-svg text {
            font-family: inherit !important;
        }

        .no-scroll {
            overflow: hidden;
        }

        .charts-column {
            display: flex;
            flex-direction: column;
            max-width: 400px;
        }

        .charts-column .home-widget {
            margin-bottom: 20px;
        }

        .charts-column .home-widget:last-child {
            margin-bottom: 10px;
        }

        .dropdown-menu a {
            text-align: left;
        }

        .dropdown-submenu {
            position: relative;
        }

        .dropdown-submenu > a::after {
            margin-right: 0px !important;
        }

        .dropdown-submenu:hover > a::after {
            border-left-color: #ccc !important;
        }

        .dropdown-submenu > .dropdown-menu {
            top: 0;
            left: 100%;
            margin-top: -6px;
            margin-left: -1px;
        }

        .dropdown-submenu:hover > .dropdown-menu {
            display: block;
        }

        .dropdown-submenu > a:after {
            display: block;
            content: " ";
            float: right;
            width: 0;
            height: 0;
            border-color: transparent;
            border-style: solid;
            border-width: 5px 0 5px 5px;
            border-left-color: #cccccc;
            margin-top: 5px;
            margin-right: -10px;
        }

        .dropdown-submenu:hover > a:after {
            border-left-color: #ffffff;
        }

        .dropdown-submenu.pull-left {
            float: none;
        }

        .dropdown-submenu.pull-left > .dropdown-menu {
            left: -100%;
            margin-left: 10px;
            -webkit-border-radius: 6px 0 6px 6px;
            -moz-border-radius: 6px 0 6px 6px;
            border-radius: 6px 0 6px 6px;
        }

        .tasks-summary .apexcharts-legend {
            left: 80px !important;
        }

        .home-toolbar .btn-group > .dropdown-menu {
            margin-left: 10px;
        }

        .home-toolbar .btn-group > .dropdown-menu .dropdown-submenu .dropdown-menu {
            margin-left: 2px;
            margin-top: 0px;
        }

    </style>

    <div class="view-toolbar home-toolbar">
        <span style="font-weight: 600;font-size: 20px;padding-right: 10px;" translate>HOME_TITLE</span>

        <div class="btn-group dropdown" ng-controller="NewObjectController as newObjectVm">
            <button class="btn btn-sm new-button dropdown-toggle" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                <i class="las la-plus"></i>
                <span translate>NEW_OBJECT</span>
                <span class="caret" style="margin-left: 5px;"></span>
            </button>
            <ul class="dropdown-menu">
                <li><a href="" ng-click="newObjectVm.newItem()" translate>ITEM</a></li>
                <li class="dropdown dropdown-submenu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" translate>ITEM_DETAILS_TAB_CHANGES</a>
                    <ul class="dropdown-menu">
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newECR()">ECR</a></li>
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newECO()">ECO</a></li>
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newDCR()">DCR</a></li>
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newDCO()">DCO</a></li>
                        <li class="dropdown dropdown-submenu">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">MCO</a>
                            <ul class="dropdown-menu">
                                <li class="menu-item "><a href="#" ng-click="newObjectVm.newMCO('ITEMMCO')" translate>ITEM_MCO</a>
                                </li>
                                <li class="menu-item "><a href="#" ng-click="newObjectVm.newMCO('OEMPARTMCO')"
                                                          translate>MFR_PART_MCO</a></li>
                            </ul>
                        </li>
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newDeviation()" translate>DEVIATION_SINGULAR</a>
                        </li>
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newWaiver()"
                                                  translate>WAIVER_SINGULAR</a></li>
                    </ul>
                </li>
                <li class="dropdown dropdown-submenu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" translate>QUALITY</a>
                    <ul class="dropdown-menu">
                        <li class="menu-item ">
                            <a href="#" ng-click="newObjectVm.newInspectionPlan('PRODUCTINSPECTIONPLAN')" translate>
                                PRODUCT_INSPECTION_PLAN
                            </a>
                        </li>
                        <li class="menu-item ">
                            <a href="#" ng-click="newObjectVm.newInspectionPlan('MATERIALINSPECTIONPLAN')" translate>
                                MATERIAL_INSPECTION_PLAN
                            </a>
                        </li>
                        <li class="menu-item ">
                            <a href="#" ng-click="newObjectVm.newInspection('ITEMINSPECTION')"
                               translate>ITEM_INSPECTION</a>
                        </li>
                        <li class="menu-item ">
                            <a href="#" ng-click="newObjectVm.newInspection('MATERIALINSPECTION')" translate>
                                MATERIAL_INSPECTION
                            </a>
                        </li>
                        <li class="menu-item ">
                            <a href="#" ng-click="newObjectVm.newProblemReport()" translate>PROBLEM_REPORT</a>
                        </li>
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newNCR()">NCR</a></li>
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newQCR()">QCR</a></li>
                    </ul>
                </li>
                <li class="dropdown dropdown-submenu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" translate>MANUFACTURING</a>
                    <ul class="dropdown-menu">
                        <li class="menu-item "><a href="#" translate>PLANT</a></li>
                        <li class="menu-item "><a href="#" translate>MACHINE</a></li>
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newWorkCenter()"
                                                  translate>WORKCENTER_TITLE</a></li>
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newTool()" translate>TOOL_TITLE</a>
                        </li>
                        <li class="menu-item "><a href="#" translate>JIG_FIXTURE</a></li>
                        <li class="menu-item "><a href="#" translate>MATERIAL</a></li>
                        <li class="menu-item "><a href="#" translate>MANPOWER</a></li>
                        <li class="menu-item "><a href="#" translate>SHIFT</a></li>
                        <li class="menu-item "><a href="#" translate>OPERATION_TITLE</a></li>
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newEquipment()" translate>EQUIPMENT</a>
                        </li>
                        <li class="menu-item "><a href="#" ng-click="newObjectVm.newInstrument()"
                                                  translate>INSTRUMENT</a></li>
                    </ul>
                </li>
                <li><a ui-sref="app.workflow.editor" translate>WORKFLOW</a></li>
                <li><a href="" ng-click="newObjectVm.newProject()" translate>NO_OF_PROJECTS</a></li>
                <li><a href="" ng-click="newObjectVm.newSpecification()" translate>SPECIFICATION</a></li>
                <li><a href="" ng-click="newObjectVm.newManufacturer()" translate>MANUFACTURER</a></li>
                <li><a href="" ng-click="newObjectVm.newManufacturerPart()" translate>MANUFACTURER_PART</a></li>
                <li><a href="" ng-click="newObjectVm.newCustomer()" translate>CUSTOMER</a></li>
            </ul>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;">
        <div class="home-side-panel">
            <div ng-include="'app/desktop/modules/home/widgets/myTasks/myTasksWidget.jsp'"
                 ng-controller="UserTasksWidgetController as userTasksVm"></div>

            <div ng-include="'app/desktop/modules/home/widgets/recentlyVisited/recentlyVisitedWidget.jsp'"
                 ng-controller="RecentlyVisitedController as recentlyVisitedVm"></div>

            <div ng-include="'app/desktop/modules/home/widgets/savedSearches/savedSearchesWidget.jsp'"
                 ng-controller="SavedSearchesWidgetController as savedSearchesVm"></div>
        </div>

        <div class="home-content-panel">
            <div class="widgets-row">
                <conversations-widget></conversations-widget>
                <div class="charts-column">
                    <div class="home-widget counts-chart-container">
                        <div id="itemCounts">

                        </div>
                    </div>
                    <div class="home-widget counts-chart-container">
                        <div id="changeCounts">

                        </div>
                    </div>
                    <div class="home-widget counts-chart-container">
                        <div id="qualityTypeCounts">

                        </div>
                    </div>
                    <div class="home-widget ">
                        <div class="widget-header">
                            <h5 translate>PROBLEMREPORTS</h5>
                        </div>
                        <div class="widget-body no-scroll">
                            <div id="problemReportCounts">

                            </div>
                        </div>
                    </div>
                    <div class="home-widget ">
                        <div class="widget-header">
                            <h5 translate>TASKS_SUMMARY</h5>
                        </div>
                        <div class="widget-body no-scroll">
                            <div id="tasksSummary" class="tasks-summary">

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>