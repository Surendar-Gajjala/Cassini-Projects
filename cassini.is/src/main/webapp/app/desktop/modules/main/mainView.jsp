<style scoped>
    .mask-panel {
        display: none;
        position: fixed;
        top: 50px !important;
        left: 0;
        bottom: 30px;
        right: 0;
        opacity: 0.5;
        background-color: #0a0a0a;
        z-index: 9998;
    }

    .app-side-panel {
        top: 50px !important;
    }

    .mask-panel2 {
        display: none;
        position: fixed;
        top: 205px !important;
        left: 0;
        bottom: 30px;
        right: 0;
        opacity: 0.5;
        background-color: #0a0a0a;
        z-index: 9998;
    }

    .mask-panel3 {
        display: none;
        position: fixed;
        top: 125px !important;
        left: 0;
        bottom: 30px;
        right: 0;
        opacity: 0.5;
        background-color: #0a0a0a;
        z-index: 9998;
    }

    #sideNavigation {
        position: fixed;
        top: 50px;
        bottom: 30px;
        width: 240px;
        overflow-y: auto;
        z-index: 9999;
        left: -250px;

        background-color: #e4e7ea;
        border-right: 1px solid #d7d7d7;
        content: '';
        display: block;
    }

    #sideNavigation h5 {
        padding-left: 10px;
    }

    .sidePanel1 {
        position: fixed;
        top: 50px !important;
        bottom: 30px;
        background-color: #fff;
        z-index: 9999;
    }

    .sidePanel2 {
        position: fixed;
        top: 205px !important;
        bottom: 30px;
        background-color: #fff;
        z-index: 9999;
    }

    .sidePanel3 {
        position: fixed;
        top: 125px !important;
        bottom: 30px;
        background-color: #fff;
        z-index: 9999;
    }

    /*#rightSidePanelContent, #leftSidePanelContent {
        height: 100% !important;
    }*/
    /*
        .panels-container {
            margin-top: -4px;
        }

        .panels-container span:first-child {
            border-radius: 3px 0 0 3px;
        }

        .panel-summary {
            height: 34px;
            margin: 3px -20px 0px 8px;
            display: inline-block;
            width: 200px;
            border-radius: 0 3px 3px 0;
            padding-left: 10px;
            line-height: 34px;
        }

        .panel-summary span:first-child {
            width: 150px;
            height: 34px;
        }

        .panel-summary span:first-child h2 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 17px;
            display: inline-block;
            padding-right: 10px;
            border-right: 1px solid #fff;
            width: 120px;
        }

        .panel-summary span:nth-child(2) h1 {
            margin: 0;
            line-height: 34px;
            color: #fff;
            font-size: 18px;
            display: inline-block;
            width: 60px;
        }

        .panel-summary h2,
        .panel-summary h1 {
            text-align: center;
        }

        .panel-summary h1 {
            font-size: 16px;
        }

        .panel-total {
            background: #005C97; /!* fallback for old browsers *!/
            background: -webkit-linear-gradient(to left, #363795, #005C97); /!* Chrome 10-25, Safari 5.1-6 *!/
            background: linear-gradient(to left, #363795, #005C97); /!* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ *!/
        }

        .panel-finish {
            background: #159957; /!* fallback for old browsers *!/
            background: -webkit-linear-gradient(to right, #159957, #155799); /!* Chrome 10-25, Safari 5.1-6 *!/
            background: linear-gradient(to right, #159957, #155799); /!* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ *!/
        }

        .panel-inprogress {
            background: #fdc830; /!* fallback for old browsers *!/
            background: -webkit-linear-gradient(to right, #fdc830, #f37335); /!* Chrome 10-25, Safari 5.1-6 *!/
            background: linear-gradient(to right, #fdc830, #f37335); /!* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ *!/
        }*/

    .search-element {
        position: absolute;
        top: 14px;
        left: 30px;
        right: 0px;
        margin: auto;
        max-width: 580px;
        width: 50%;
    }

    .contentpanel {
        padding: 0 !important;
    }

    .headerbar {
        background-color: #00253f;
    }

</style>
<div id="sideNavigation" class="">
    <div class="leftpanelinner">
        <h5 class="sidebartitle">Navigation</h5>
        <ul class="nav nav-pills nav-stacked nav-bracket">

            <li id="nav-home" <%-- ng-if="hasPermission('permission.dashboard.view')==true || hasPermission('permission.dashboard.newProject')==true|| hasPermission('permission.dashboard.lockProject')==true  || hasPermission('permission.dashboard.addAttributes')==true" --%> >
                <a ui-sref="app.home" class="topmenu-item-link">
                    <i class="fa fa-home" style="font-size: 15px;"></i> <span
                        class="hidden-xs hidden-sm">Dashboard</span>
                </a>
            </li>
            <li id="nav-masterdata" <%--ng-if="hasPermission('permission.masterdata.view')" --%>
                ng-if="hasPermission('permission.masterdata.view') || hasPermission('permission.masterdata.classification') || hasPermission('permission.masterdata.editClassification') || hasPermission('permission.masterdata.machines') || hasPermission('permission.masterdata.manpower') || hasPermission('permission.materials.view') || hasPermission('permission.materials.newMaterial')==true  || hasPermission('permission.materials.edit')==true || hasPermission('permission.materials.delete') ||
                        hasPermission('permission.materials.addAttributes') || hasPermission('permission.machines.view') || hasPermission('permission.machines.newMachine') || hasPermission('permission.machines.edit') || hasPermission('permission.machines.delete') || hasPermission('permission.manpower.view') || hasPermission('permission.manpower.newManpower') || hasPermission('permission.manpower.edit') || hasPermission('permission.manpower.delete') || hasPermission('permission.masterdata.addType') || hasPermission('permission.masterdata.deleteType')">
                <a ui-sref="app.proc.classification" class="topmenu-item-link">
                    <i class="fa fa-snowflake-o" style="font-size: 15px;"></i> <span class="hidden-xs hidden-sm">Master Data</span>
                </a>
            </li>

            <li id="nav-documents" <%--ng-if="hasPermission('permission.topDocuments.view')"--%>
                ng-if="hasPermission('permission.topDocuments.all')==true || hasPermission('permission.topDocuments.view')==true || hasPermission('permission.topDocuments.addDocuments')==true || hasPermission('permission.topDocuments.addPermissions')==true">
                <a ui-sref="app.document.all" class="topmenu-item-link">
                    <i class="fa fa-files-o " style="font-size: 15px;"></i> <span
                        class="hidden-xs hidden-sm">Documents</span>
                </a>
            </li>

            <li class="nav-parent" ng-show="hasPermission('permission.topStores.view')==true">
                <a href=""><i class="fa fa-shopping-cart"></i> <span>Procurement</span></a>
                <ul class="children" style="">
                    <li>
                        <a ui-sref="app.store.all" ng-if="hasPermission('permission.topStores.view')==true  || hasPermission('permission.topStores.new')==true || hasPermission('permission.topStores.deleteStore')==true || hasPermission('permission.storeInventory.inventory')==true || hasPermission('permission.receives.receive') || hasPermission('permission.receives.editReceive') ||
                                                          hasPermission('permission.receives.viewReceive') || hasPermission('permission.receives.viewReceiveItems') || hasPermission('permission.topStores.viewIssue') || hasPermission('permission.stockIssues.issue')==true || hasPermission('permission.stockIssues.viewIssue') || hasPermission('permission.stockIssues.viewIssueItems') || hasPermission('permission.topStores.stockMovement') ||
                                                          hasPermission('permission.topStores.edit') || hasPermission('permission.loanIssued.loan')|| hasPermission('permission.loanIssued.addLoan') || hasPermission('permission.loanReceived.viewLoanReceive') || hasPermission('permission.loanReceived.viewReceiveLoanItems') || hasPermission('permission.loanReceived.returnLoan') || hasPermission('permission.loanReceived.edit') ||
                                                          hasPermission('permission.loanIssued.issueLoanItems') || hasPermission('permission.stockIssues.edit') || hasPermission('permission.loanIssued.viewLoanIssueItems') || hasPermission('permission.loanIssued.edit') || hasPermission('permission.loanReceived.returnLoanItems')==true || hasPermission('permission.topStores.scrapRequest') ||
                                                          hasPermission('permission.topStores.viewScrapRequestItems') || hasPermission('permission.storeInventory.addItemsToProject') ||  hasPermission('permission.indents.newIndent') || hasPermission('permission.indents.viewIndents') || hasPermission('permission.indents.viewIndentItems') || hasPermission('permission.indents.addIndentItems') || hasPermission('permission.indents.removeIndentItems') ||
                                                          hasPermission('permission.indents.editIndent') || hasPermission('permission.indents.editIndentItems') || hasPermission('permission.roadChallans.viewRoadChalanItems') ||  hasPermission('permission.indents.approveIndent') || hasPermission('permission.purchaseOrders.newPurchaseOrder') || hasPermission('permission.purchaseOrders.editPurchaseOrder') || hasPermission('permission.purchaseOrders.viewPurchaseOrder') ||
                                                          hasPermission('permission.purchaseOrders.viewPurchaseOrderItems') || hasPermission('permission.purchaseOrders.addPurchaseOrderItems') || hasPermission('permission.purchaseOrders.removePurchaseOrderItems') || hasPermission('permission.purchaseOrders.editPurchaseOrderItemQty') || hasPermission('permission.purchaseOrders.approvePurchaseOrder') || hasPermission('permission.roadChallans.createRoadChalan') ||
                                                          hasPermission('permission.roadChallans.viewRoadChalan') || hasPermission('permission.roadChallans.addRoadChalanItems') || hasPermission('permission.roadChallans.edit') ||  hasPermission('permission.requisitions.newRequest') || hasPermission('permission.requisitions.viewRequestItems') || hasPermission('permission.requisitions.addRequestItems') || hasPermission('permission.requisitions.approveRequest') || hasPermission('permission.requisitions.editRequest') ||
                                                          hasPermission('permission.stockReturn.view') || hasPermission('permission.stockReturn.create') || hasPermission('permission.stockReturn.edit')==true || hasPermission('permission.stockReturn.approve')">
                            <i class="fa fa-shopping-cart" style="font-size: 15px"></i>
                            <span style="margin-left: 6px;" class="hidden-xs hidden-sm">Stores</span>
                        </a>
                    </li>
                    <li>
                        <a ui-sref="app.store.inventory" ng-if="hasPermission('permission.inventory.all')">
                            <i class="glyphicon glyphicon-equalizer" style="font-size: 15px;"></i>
                            <span class="hidden-xs hidden-sm">Inventory</span>
                        </a>
                    </li>
                    <li ng-if="hasPermission('permission.suppliers.view') || hasPermission('permission.suppliers.new') || hasPermission('permission.suppliers.edit')">
                        <a ui-sref="app.store.supplier">
                            <i class="fa fa flaticon-businessman276" style="font-size: 15px;"></i>
                            <span class="hidden-xs hidden-sm">Suppliers</span>
                        </a>
                    </li>
                </ul>
            </li>

            <li class="nav-parent" ng-show="hasPermission('permission.contractors.view')">
                <a href=""><i class="fa flaticon-deadlines"></i> <span>Subcontracts</span></a>
                <ul class="children" style="">
                    <li ng-if="hasPermission('permission.contractors.view') || hasPermission('permission.contractors.new') || hasPermission('permission.contractors.editBasic') || hasPermission('permission.contractors.delete')">
                        <a ui-sref="app.contracts.contractors">
                            <i class="fa fa-users" style="font-size: 15px"></i>
                            <span class="hidden-xs hidden-sm">Contractors</span>
                        </a>
                    </li>
                    <li ng-if="hasPermission('permission.workOrders.view') || hasPermission('permission.workOrders.new') || hasPermission('permission.workOrders.edit') || hasPermission('permission.workOrders.delete') || hasPermission('permission.workOrders.items')">
                        <a ui-sref="app.contracts.workOrders">
                            <i class="fa fa-hourglass" style="font-size: 15px"></i>
                            <span class="hidden-xs hidden-sm">Work Orders</span>
                        </a>
                    </li>
                </ul>
            </li>

            <li class="nav-parent" ng-show="hasPermission('permission.projectMaterialReport') || hasPermission('permission.workStatusReport')
             || hasPermission('permission.storePositionReport') || hasPermission('permission.monthlyStockReport')
             || hasPermission('permission.stockReceiveReport') || hasPermission('permission.stockIssueReport')">
                <a href=""><i class="fa fa-file-text"></i> <span>Reports</span></a>
                <ul class="children" style="">
                    <li ng-if="hasPermission('permission.projectMaterialReport')">
                        <a ui-sref="app.reports.project">
                            <i class="fa fa-file-image-o" style="font-size: 15px"></i>
                            <span class="hidden-xs hidden-sm">Project Material Statement</span>
                        </a>
                    </li>
                    <li ng-if="hasPermission('permission.workStatusReport')">
                        <a ui-sref="app.reports.work">
                            <i class="fa fa-area-chart" style="font-size: 15px"></i>
                            <span class="hidden-xs hidden-sm">Work Status Report</span>
                        </a>
                    </li>
                    <li ng-if="hasPermission('permission.storePositionReport')">
                        <a ui-sref="app.reports.store">
                            <i class="fa fa-bar-chart" style="font-size: 15px"></i>
                            <span class="hidden-xs hidden-sm">Store Position Report</span>
                        </a>
                    </li>
                    <li ng-if="hasPermission('permission.monthlyStockReport')">
                        <a ui-sref="app.reports.stock">
                            <i class="fa fa-line-chart" style="font-size: 15px"></i>
                            <span class="hidden-xs hidden-sm">Monthly Stock Report</span>
                        </a>
                    </li>
                    <li ng-if="hasPermission('permission.stockReceiveReport')">
                        <a ui-sref="app.reports.receive">
                            <i class="fa fa-bar-chart" style="font-size: 15px"></i>
                            <span class="hidden-xs hidden-sm">Stock Receive Report</span>
                        </a>
                    </li>
                    <li ng-if="hasPermission('permission.stockIssueReport')">
                        <a ui-sref="app.reports.issue">
                            <i class="fa fa-bar-chart" style="font-size: 15px"></i>
                            <span class="hidden-xs hidden-sm">Stock Issue Report</span>
                        </a>
                    </li>
                </ul>
            </li>
            <li id="nav-workflow" ng-if="hasPermission('permission.workflow.all')">
                <a ui-sref="app.workflow.all" class="topmenu-item-link">
                    <i class="fa flaticon-plan2 nav-icon-font" style="font-size: 15px;!important;"></i> <span
                        class="hidden-xs hidden-sm">Workflows</span>
                </a>
            </li>
            <li id="nav-admin" <%--ng-if="hasPermission('permissions.admin.all')"--%>
                ng-if="hasPermission('permission.admin.user') || hasPermission('permission.admin.view') || hasPermission('permission.admin.group') || hasPermission('permission.admin.permissions')">
                <a ui-sref="app.admin.usersettings" class="topmenu-item-link">
                    <i class="fa fa-lock" style="font-size: 15px;!important;"></i> <span class="hidden-xs hidden-sm">Admin</span>
                </a>
            </li>
            <li id="nav-settings"
                ng-if="hasPermission('permissions.settings.view') || hasPermission('permission.settings.autoNumbers') || hasPermission('permission.settings.editAutoNumbers') || hasPermission('permission.settings.lovs') || hasPermission('permission.settings.editLovs') || hasPermission('permission.settings.customProperties') || hasPermission('permission.settings.editCustomProperties')">
                <a ui-sref="app.settings" class="topmenu-item-link">
                    <i class="fa fa-wrench nav-icon-font" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm">Settings</span>
                </a>
            </li>
        </ul>
    </div>
    <!-- leftpanelinner -->
</div>
<section style="height:100%;" window-resized>
    <div id="mainPanel" class="mainpanel" style="height:100%;">
        <div id="headerbar" class="headerbar">
            <div id="header-left" class="header-left" style="height: 50px;">
                <div class="topnav" id="app-navigation">
                    <ul class="nav nav-horizontal">
                        <span ng-controller="AppNavController as appNav"
                              ng-include="'app/desktop/modules/navigation/app/appNav.jsp'"></span>
                    </ul>
                </div>
            </div>

            <%--  <div id="project-heading" style="height: 50px;text-align: center;color: white;">Miyapur House Construction - Mathrusri
                  Nagar
              </div>--%>
            <%-- <div ng-include="'app/desktop/modules/main/searchBarView.jsp'"
                  ng-controller="SearchBarController as searchVm">
             </div>--%>
            <div class="search-element search-input-container">
                <div style="color: white !important;font-size: 19px;font-weight: inherit"
                     title="{{selectedProject.name}}"
                     ng-show="headerProjectName == true">
                    {{selectedProject.name | limitTo: 65}}{{selectedProject.name.length > 65 ? '...' : ''}}
                </div>
            </div>

            <div id="header-right" class="header-right">
                <ul class="headermenu">
                    <li style="height:50px;" ng-if="mainVm.groups.length > 0">
                        <div class="btn-group" style="height:100%; width: auto" uib-dropdown>
                            <button type="button" class="btn btn-default btn-administrator" uib-dropdown-toggle
                                    style="height:100%; width: auto;" ng-click="toggleAdmin1()">
                                <i class="fa fa-group nav-icon-font"
                                   style="font-size: 15px;"></i><span class="hidden-xs hidden-sm">
                        {{mainVm.defaultGroup.name}}</span>
                                <span class="caret" style="border-top-color:#f7fbff"></span>
                            </button>
                            <ul class="uib-dropdown-menu dropdown-menu-usermenu pull-right" ng-click="toggleAdmin1()"
                                uib-dropdown-menu>
                                <li><a href="" ng-repeat="group in mainVm.groups"
                                       ng-click="mainVm.changePermissionsByGroup(group)">
                                    <i class="fa fa-group nav-icon-font" style="font-size: 15px;"></i>
                                    {{group.name}}
                                </a></li>
                            </ul>
                        </div>
                    </li>

                    <li style="height:50px;">
                        <div class="btn-group" style="height:100%;" uib-dropdown>
                            <button type="button" class="btn btn-default" ng-click="toggleAdmin()" uib-dropdown-toggle
                                    style="height:100%;">
                                <i class="glyphicon glyphicon-user mr5"></i>
                                <span class="hidden-xs hidden-sm">{{mainVm.user.person.fullName}}</span>
                                <span class="caret"></span>
                            </button>
                            <ul class="uib-dropdown-menu1 dropdown-menu-usermenu pull-right" ng-click="toggleAdmin()"
                                uib-dropdown-menu>
                                <li><a href="" ng-click="mainVm.showProfile(mainVm.user)"><i
                                        class="glyphicon glyphicon-user"></i>My Profile</a></li>
                                <li><a href="" ng-click="mainVm.logout()"><i class="fa fa-power-off"></i>Log
                                    Out</a></li>
                                <li><a href="" ng-click="mainVm.feedback()"><i class="glyphicon glyphicon-list-alt"></i>Support</a>
                                </li>
                                <li><a href="" ng-click="mainVm.help()"><i
                                        class="glyphicon glyphicon-question-sign"></i>Help</a>
                                </li>
                            </ul>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
        <div ng-if="mainVm.viewType == 'PROJECT' && selectedProject.locked == true"
             style="height: 30px !important;background: white;border: 1px solid #CCC">
            <p style="text-align: center;color: red;font-size: 18px;margin-top: 2px;">
                <i class="fa fa-lock"></i>
                <span>Project Locked</span>
            </p>
        </div>
        <div id="workspace" style="overflow-x: hidden; overflow-y: hidden; background-color: #F2F7F9">
            <style scoped>
                #viewTitleContainer h3 {
                    font-size: 22px;
                }

                .fix-title {
                    line-height: 40px;
                }

                .status-in-title {
                    line-height: 20px;
                    display: inline-block;
                    vertical-align: middle;
                    margin-left: 5px;
                }
            </style>

            <div ng-if="mainVm.viewType == 'PROJECT'" id="subNavigation"
                 ng-include="'app/desktop/modules/navigation/project/projectNav.jsp'"
                 ng-controller="ProjectNavController as projNavVm"></div>

            <div ng-if="mainVm.viewType == 'PROCUREMENT'" id="subNavigation1"
                 ng-include="'app/desktop/modules/navigation/procurement/procurementNav.jsp'"
                 ng-controller="ProcurementNavController as procNavVm"></div>

            <%--<div id="viewTitleContainer" class="pageheader"
                 style="background-color: #FFF !important; border-bottom: 1px solid #D0DDE1; padding:5px !important;height: 50px !important;">
                <div class="row" style="margin: 0;height: 50px;">
                    <div class="col-sm-5">
                        <h3 style="margin: 0">
                            <i class="fa" ng-class="viewInfo.icon" style="position: absolute;"></i>

                            <div ng-class="{'fix-title': viewInfo.description == null || viewInfo.description == ''}"
                                 style="display: inline-block; margin-left: 45px;">
                                <div ng-bind-html="viewInfo.title" style="display: inline-block"></div>
                                <div ng-if="viewInfo.description != null && viewInfo.description != ''"
                                     ng-bind-html="viewInfo.description"
                                     style="font-size: 16px; color: rgb(143, 143, 143)">
                                </div>
                            </div>
                        </h3>
                    </div>
                    <div class="col-sm-6">
                        <span class="panels-container" ng-show="showTaskNotification == true">
                            <span class="panel-summary panel-total">
                              <span><h2>Total Tasks</h2></span>
                              <span><h1>{{totalTasks}}</h1></span>
                            </span>
                            <span class="panel-summary panel-finish">
                              <span><h2>Finished Tasks</h2></span>
                                <span><h1>{{finishedTasks}}</h1></span>
                             </span>
                             <span class="panel-summary panel-inprogress">
                              <span><h2>Delayed Tasks</h2></span>
                                <span><h1>{{pendingTasks}}</h1></span>
                            </span>
                        </span>
                    </div>
                    <div class="col-sm-1 text-right">
                        <comments-btn ng-if="mainVm.comments.show"
                                      object-type="mainVm.comments.objectType"
                                      object-id="mainVm.comments.objectId"></comments-btn>
                    </div>
                </div>
            </div>--%>
            <div>
                <div id="appNotification" class="alert animated" ng-class="notification.type" ng-cloak>
                    <span style="margin-right: 10px;"><i class="fa" ng-class="notification.class"></i></span>
                    <button type="button" class="close" ng-click="closeNotification()">x</button>
                    {{notification.message}}
                </div>
            </div>

            <div>
                <div id="contentpanel" class="contentpanel" contentpanel>
                    <div id=" appcontent" class="appcontent">
                        <div class="content-panel-toolbar">
                            <script type="text/ng-template" id="main-view-tb">
                                <div></div>
                            </script>

                            <div ng-include src="toolbarTemplate"></div>
                        </div>

                        <div ui-view>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    /* main panel end */

    <div class="mask-panel" id="contentpanel-mask">

    </div>

    <div id="rightSidePanel" class="app-side-panel right" sidepanelcontent>
        <div class="row" style="margin: 0">
            <div class="col-sm-12 text-right" style="padding: 0">
                <div class="side-panel-title">
                    <h3 style="text-align: center;margin: 12px 0 0 10px;">{{mainVm.rightSidePanelOptions.title}}</h3>
                    <a href="" ng-click="mainVm.hideSidePanel('right')" class="close pull-right"
                       style="display: inline-block"></a>
                </div>
                <div class="progress progress-striped active"
                     style="border-radius: 0;"
                     ng-if="mainVm.rightSidePanelOptions.showBusy">
                    <div class="progress-bar"
                         role="progressbar" aria-valuenow="100" aria-valuemin="0"
                         aria-valuemax="100" style="width: 100%">
                    </div>
                </div>
                <div id="rightSidePanelContent" class="text-left" style="overflow: auto;">

                </div>
                <%--<div id="rightSidePanelButtonsPanel"
                     class='buttons-panel text-right'
                     style="display: none"
                     ng-if="mainVm.rightSidePanelOptions.buttons != null &&
                            mainVm.rightSidePanelOptions.buttons != undefined &&
                            mainVm.rightSidePanelOptions.buttons.length > 0">
                    <button class="btn btn-sm {{button.btnClass}}"
                            style="margin-left: 10px;"
                            ng-disabled="mainVm.rightSidePanelOptions.showBusy"
                            ng-class="{'btn-success': button.btnClass == null || button.btnClass == undefined}"
                            ng-repeat="button in mainVm.rightSidePanelOptions.buttons"
                            ng-click="mainVm.broadcastButtonCallback(button)">{{button.text}}
                    </button>
                </div>--%>
                <div id="rightSidePanelButtonsPanel"
                     class="buttons-panel"
                     ng-if="mainVm.rightSidePanelOptions.buttons != null &&
                            mainVm.rightSidePanelOptions.buttons != undefined &&
                            mainVm.rightSidePanelOptions.buttons.length > 0"
                     ng-class="{'text-right':mainVm.rightSidePanelOptions.buttons.length == 1}">
                    <div ng-if="mainVm.rightSidePanelOptions.buttons.length == 2"
                         ng-repeat="button in mainVm.rightSidePanelOptions.buttons"
                         ng-class="{'text-left': $index/2 == 0,'text-right': $index/2 != 0}"
                         style="width: 50%;">
                        <button class="btn btn-sm {{button.btnClass}}"
                                style="margin-left: 10px;"
                                ng-disabled="mainVm.rightSidePanelOptions.showBusy"
                                ng-class="{'btn-success': button.btnClass == null || button.btnClass == undefined}"
                                ng-click="mainVm.broadcastButtonCallback(button)">
                            {{button.text}}
                        </button>
                    </div>
                    <button ng-if="mainVm.rightSidePanelOptions.buttons.length != 2"
                            class="btn btn-sm {{button.btnClass}}"
                            style="margin-left: 10px;"
                            ng-disabled="mainVm.rightSidePanelOptions.showBusy"
                            ng-class="{'btn-success': button.btnClass == null || button.btnClass == undefined}"
                            ng-repeat="button in mainVm.rightSidePanelOptions.buttons"
                            ng-click="mainVm.broadcastButtonCallback(button)">{{button.text}}
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div id="leftSidePanel" class="app-side-panel left" sidepanelcontent>
        <div class="row" style="margin: 0">
            <div class="col-sm-12 text-right" style="padding: 0">
                <div class="side-panel-title">
                    <h3 style="text-align: center;margin: 12px 0 0 10px;">{{mainVm.leftSidePanelOptions.title}}</h3>
                    <a href="" ng-click="mainVm.hideSidePanel('left')" class="close pull-right"
                       style="display: inline-block"></a>
                </div>
                <div class="progress progress-striped active"
                     style="border-radius: 0;"
                     ng-if="mainVm.leftSidePanelOptions.showBusy">
                    <div class="progress-bar"
                         role="progressbar" aria-valuenow="100" aria-valuemin="0"
                         aria-valuemax="100" style="width: 100%">
                    </div>
                </div>
                <div id="leftSidePanelContent" class="text-left" style="overflow: auto;">

                </div>

                <div id="leftSidePanelButtonsPanel"
                     class="buttons-panel"
                     ng-if="mainVm.leftSidePanelOptions.buttons != null &&
                            mainVm.leftSidePanelOptions.buttons != undefined &&
                            mainVm.leftSidePanelOptions.buttons.length > 0"
                     ng-class="{'text-right':mainVm.leftSidePanelOptions.buttons.length == 1}">
                    <div ng-if="mainVm.leftSidePanelOptions.buttons.length == 2"
                         ng-repeat="button in mainVm.leftSidePanelOptions.buttons"
                         ng-class="{'text-left': $index/2 == 0,'text-right': $index/2 != 0}"
                         style="width: 50%;">
                        <button class="btn btn-sm {{button.btnClass}}"
                                style="margin-left: 10px;"
                                ng-disabled="mainVm.leftSidePanelOptions.showBusy"
                                ng-class="{'btn-success': button.btnClass == null || button.btnClass == undefined}"
                                ng-click="mainVm.broadcastButtonCallback(button)">
                            {{button.text}}
                        </button>
                    </div>
                    <button ng-if="mainVm.leftSidePanelOptions.buttons.length != 2"
                            class="btn btn-sm {{button.btnClass}}"
                            style="margin-left: 10px;"
                            ng-disabled="mainVm.leftSidePanelOptions.showBusy"
                            ng-class="{'btn-success': button.btnClass == null || button.btnClass == undefined}"
                            ng-repeat="button in mainVm.leftSidePanelOptions.buttons"
                            ng-click="mainVm.broadcastButtonCallback(button)">{{button.text}}
                    </button>
                </div>
            </div>
        </div>
    </div>

    <div id="footer">
        <div>&#169;<span class="mr5"></span>CassiniPLM</div>
    </div>

</section>

<div id="objectSelectionView"
     ng-include="'app/desktop/modules/select/objectSelectionView.jsp'"
     ng-controller="ObjectSelectionController as objSelectionVm"></div>