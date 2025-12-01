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

    .sidePanel1 {
        position: fixed;
        top: 175px !important;
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

    #rightSidePanelContent, #leftSidePanelContent {
        height: 100% !important;
    }


</style>
<div id="sideNavigation" class="">
    <div class="leftpanelinner">
        <h5 class="sidebartitle">Navigation</h5>
        <ul class="nav nav-pills nav-stacked nav-bracket">

            <li id="nav-home">
                <a ui-sref="app.home" class="topmenu-item-link">
                    <i class="fa fa-home" style="font-size: 15px;"></i> <span
                        class="hidden-xs hidden-sm">Dashboard</span>
                </a>
            </li>
            <li id="nav-admin"
                ng-if="hasPermission('permission.admin.user')==true || hasPermission('permission.admin.view')==true || hasPermission('permission.admin.group')==true">
                <a ui-sref="app.admin.usersettings" class="topmenu-item-link">
                    <i class="fa fa-lock" style="font-size: 15px;!important;"></i> <span class="hidden-xs hidden-sm">Admin</span>
                </a>
            </li>
        </ul>
    </div>
    <!-- leftpanelinner -->
</div>
<section style="height:100%;" window-resized>
    <div id="mainPanel" class="mainpanel" style="height:100%;">
        <div id="headerbar" class="headerbar">
            <div class="header-left" style="height: 50px;">
                <div class="topnav" id="app-navigation">
                    <ul class="nav nav-horizontal">
                        <span ng-controller="AppNavController as appNav"
                              ng-include="'app/desktop/modules/main/appNav.jsp'"></span>
                    </ul>
                </div>
            </div>
            <div class="header-right">
                <ul class="headermenu">
                    <li style="height:50px;" ng-if="mainVm.groups.length > 0">
                        <div class="btn-group" style="height:100%; width: auto" uib-dropdown>
                            <button type="button" class="btn btn-default btn-administrator" uib-dropdown-toggle
                                    style="height:100%; width: auto;">
                                <i class="fa fa-group nav-icon-font"
                                   style="font-size: 15px;"></i><span class="hidden-xs hidden-sm">
                        {{mainVm.defaultGroup.name}}</span>
                                <span class="caret" style="border-top-color:#f7fbff"></span>
                            </button>
                            <ul class="uib-dropdown-menu dropdown-menu-usermenu pull-right" uib-dropdown-menu>
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
                            <button type="button" class="btn btn-default" uib-dropdown-toggle
                                    style="height:100%;">
                                <i class="glyphicon glyphicon-user mr5"></i>
                                <span class="hidden-xs hidden-sm">{{mainVm.user.person.fullName}}</span>
                                <span class="caret"></span>
                            </button>
                            <ul class="uib-dropdown-menu dropdown-menu-usermenu pull-right" uib-dropdown-menu>
                                <li><a href="" ng-click="mainVm.showProfile(mainVm.user)"><i
                                        class="glyphicon glyphicon-user"></i>My Profile</a></li>
                                <li><a href="" ng-click="mainVm.logout()"><i class="fa fa-power-off"></i>Log
                                    Out</a></li>
                                <li><a href="" ng-click="mainVm.feedback()"><i class="glyphicon glyphicon-list-alt"></i>Feedback</a>
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

            <div id="viewTitleContainer" class="pageheader"
                 style="background-color: #FFF !important; border-bottom: 1px solid #D0DDE1; padding-bottom:15px;">
                <div class="row" style="margin: 0">
                    <div class="col-sm-11">
                        <h3 style="margin: 0">
                            <i class="fa" ng-class="viewInfo.icon" style="position: absolute; color: black;"></i>

                            <div ng-class="{'fix-title': viewInfo.description == null || viewInfo.description == ''}"
                                 style="display: inline-block;color: black; margin-left: 45px;">
                                <div ng-bind-html="viewInfo.title" style="display: inline-block"></div>
                                <div ng-if="viewInfo.description != null && viewInfo.description != ''"
                                     ng-bind-html="viewInfo.description"
                                     style="font-size: 16px; color: rgb(143, 143, 143)">
                                </div>
                            </div>
                        </h3>
                    </div>
                    <div class="col-sm-1 text-right">
                        <comments-btn ng-if="mainVm.comments.show"
                                      object-type="mainVm.comments.objectType"
                                      object-id="mainVm.comments.objectId"></comments-btn>
                    </div>
                </div>
            </div>
            <div>
                <div id="appNotification" class="alert animated" ng-class="notification.type" ng-cloak>
                    <span style="margin-right: 10px;"><i class="fa" ng-class="notification.class"></i></span>
                    <button type="button" class="close" ng-click="closeNotification()">x</button>
                    {{notification.message}}
                </div>
            </div>

            <div>
                <div id="contentpanel" class="contentpanel" style="padding: 30px;" contentpanel>
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

    <div class="mask-panel" id="contentpanel-mask"></div>

    <div ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/main/sidePanelsView.jsp'"
         ng-controller="SidePanelsController as sidePanelsVm"></div>

    <div id="footer">
        <div>&#169;<span class="mr5"></span>Cassini Systems</div>
    </div>

</section>

