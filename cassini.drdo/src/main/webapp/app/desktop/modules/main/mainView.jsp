<style scoped>
    .mask-panel {
        display: none;
        position: fixed;
        top: 125px;
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

    .appcontent {
        margin-bottom: 0 !important;
    }

    .contentpanel {
        padding: 0 !important;
    }
</style>
<div id="sideNavigation" class="">
    <div class="leftpanelinner">
        <h5 class="sidebartitle">Navigation</h5>
        <ul class="nav nav-pills nav-stacked nav-bracket">
            <li id="nav-home">
                <a ui-sref="app.home">
                    <i class="fa fa-home"></i>
                    <span class="hidden-xs hidden-sm">Home</span>
                </a>
            </li>
            <li id="nav-classification" ng-if="hasPermission('permission.classification.view')">
                <a ui-sref="app.classification">
                    <i class="fa flaticon-marketing8 nav-icon-font"></i>
                    <span class="hidden-xs hidden-sm">Classification</span>
                </a>
            </li>
            <li id="nav-items" ng-if="hasPermission('permission.items.view')">
                <a ui-sref="app.items.all">
                    <i class="fa fa-th nav-icon-font"></i>
                    <span class="hidden-xs hidden-sm">Items</span>
                </a>
            </li>
            <li id="nav-bom" ng-if="hasPermission('permission.bom.view')">
                <a ui-sref="app.bom.view">
                    <i class="fa fa-sitemap nav-icon-font"></i>
                    <span class="hidden-xs hidden-sm">BOM</span>
                </a>
            </li>
            <li id="nav-storage" ng-if="hasPermission('permission.storage.view')">
                <a ui-sref="app.storage">
                    <i class="fa fa-bank"></i>
                    <span class="hidden-xs hidden-sm">Storage</span>
                </a>
            </li>
            <li id="nav-inventory" ng-if="hasPermission('permission.inventory.view')">
                <a ui-sref="app.inventory.all">
                    <i class="fa fa-bars nav-icon-font"></i>
                    <span class="hidden-xs hidden-sm">Inventory</span>
                </a>
            </li>
            <li id="nav-planning" ng-if="hasPermission('permission.planning.view')">
                <a ui-sref="app.planning.all">
                    <i class="fa flaticon-contract11 nav-icon-font"></i>
                    <span class="hidden-xs hidden-sm">Planning</span>
                </a>
            </li>
            <li class="nav-parent" ng-if="hasPermission('permission.reports.view')">
                <a href=""><i class="fa fa-file-text"></i> <span>Reports</span></a>
                <ul class="children" style="display: block !important;">
                    <li>
                        <a ui-sref="app.reports.shortage">
                            <i class="fa fa-file-image-o" style="font-size: 15px"></i>
                            <span class="hidden-xs hidden-sm">Material Status Report</span>
                        </a>
                    </li>
                </ul>
            </li>
            <li class="nav-parent">
                <a href=""><i class="fa fa-sliders nav-icon-font"></i> <span>Transactions</span></a>
                <ul class="children" style="">
                    <li ng-if="hasPermission('permission.gatePass.view')">
                        <a href="" ui-sref="app.gatePass.all">
                            <i class="fa fa-sign-in nav-icon-font"></i>
                            <span class="hidden-xs hidden-sm">GatePasses</span>
                        </a>
                    </li>
                    <li ng-if="hasPermission('permission.inward.view')">
                        <a href="" ui-sref="app.inwards.all">
                            <i class="fa fa-sign-in nav-icon-font"></i>
                            <span class="hidden-xs hidden-sm">Inwards</span>
                        </a>
                    </li>
                    <li ng-if="hasPermission('permission.requests.view')">
                        <a ui-sref="app.requests.all">
                            <i class="fa flaticon-stamp13 nav-icon-font"></i>
                            <span class="hidden-xs hidden-sm">Requests</span>
                        </a>
                    </li>
                    <li ng-if="hasPermission('permission.issued.view')">
                        <a ui-sref="app.issues.all">
                            <i class="fa fa-sign-out nav-icon-font"></i>
                            <span class="hidden-xs hidden-sm">Issues</span>
                        </a>
                    </li>
                    <li ng-if="hasPermission('permission.dispatch.view')">
                        <a ui-sref="app.dispatch.all">
                            <i class="fa fa-truck nav-icon-font"></i>
                            <span class="hidden-xs hidden-sm">Dispatches</span>
                        </a>
                    </li>
                </ul>
            </li>
            <li id="nav-procurement"
                ng-if="hasPermission('permission.suppliers.all') || hasPermission('permission.manufacturers.all') || hasPermission('permission.admin.all')">
                <a ui-sref="app.procurement.all">
                    <i class="fa fa-wrench nav-icon-font"></i>
                    <span class="hidden-xs hidden-sm">Procurement</span>
                </a>
            </li>
            <li id="nav-sumary" ng-if="hasPermission('permission.summary.view')">
                <a ui-sref="app.summary">
                    <i class="fa fa-newspaper-o"></i>
                    <span class="hidden-xs hidden-sm">Executive Summary</span>
                </a>
            </li>
            <li id="nav-admin" ng-if="hasPermission('permission.admin.all')">
                <a ui-sref="app.admin.usersettings">
                    <i class="fa fa-lock nav-icon-font"></i>
                    <span class="hidden-xs hidden-sm">Admin</span>
                </a>
            </li>
            <li id="nav-settings" ng-if="hasPermission('permission.admin.all')">
                <a ui-sref="app.settings">
                    <i class="fa fa-wrench nav-icon-font"></i>
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
            <span ng-include="'app/desktop/modules/main/topNavigation.jsp'"></span>
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
            <div id="viewTitleContainer" class="pageheader"
                 style="background-color: #FFF !important; border-bottom: 1px solid #D0DDE1; padding:0 10px 0 10px;">

                <style scoped>
                    table {
                        border-collapse: collapse;
                    }
                </style>
                <table style="width:100%; height: 75px;">
                    <tbody>
                    <tr>
                        <td valign="middle" style="padding: 0 !important; width: 1px;white-space: nowrap;">
                            <h3 style="margin: 0">
                                <i class="fa" ng-class="viewInfo.icon" style="position: absolute;"></i>

                                <div ng-class="{'fix-title': viewInfo.description == null || viewInfo.description == ''}"
                                     style="display: inline-block;margin-left: 45px;">
                                    <div ng-bind-html="viewInfo.title | translate" style="display: inline-block"></div>
                                    <div ng-if="viewInfo.description != null && viewInfo.description != ''"
                                         ng-bind-html="viewInfo.description | unsafe"
                                         style="font-size: 16px; color: rgb(143, 143, 143)">
                                    </div>
                                </div>
                            </h3>
                        </td>
                        <td valign="middle" align="center" style="padding: 0 !important;">
                            <div ng-if="mainVm.statuses.length > 0" style="width: 80%">
                                <ul id="breadcrumb" style="margin: 0">
                                    <li ng-repeat="status in mainVm.statuses">
                                        <a href="javascript:void(0);"
                                           ng-class="{'finished': status.finished, 'current': status.current && !status.underReview,
                                           'under-review':status.current && status.underReview}">
                                            <i ng-if="status.finished" class="fa fa-check"
                                               style="border: none;font-size: 18px;"></i>
                                            <i ng-if="status.current && status.underReview"
                                               class="fa fa-eye"
                                               style="border: none;font-size: 18px;"></i>
                                            {{status.name}}</a>
                                    </li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div id="appNotification" class="alert animated" ng-class="notification.type" style="display: none;">
                <span style="margin-right: 10px;"><i class="fa" ng-class="notification.class"></i></span>
                <button type="button" class="close" ng-click="closeNotification()">x</button>
                {{notification.message}}
            </div>

            <div>
                <div id="contentpanel" class="contentpanel" contentpanel>
                    <div id="appcontent" class="appcontent">
                        <div ui-view>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="mask-panel" id="contentpanel-mask"></div>

    <div ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/main/sidePanelsView.jsp'"
         ng-controller="SidePanelsController as sidePanelsVm"></div>

    <div id="footer">
        <div>&#169;<span class="mr5"></span>Cassini Systems</div>
    </div>

</section>
