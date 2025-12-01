<%@ page import="java.util.Calendar" %>
<div ng-show="mainVm.$application.login.person.firstName != null">
    <style scoped>
        .mask-panel {
            display: none;
            position: fixed;
            top: 50px;
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

        .modal1 {
            position: absolute !important;
            background: rgba(0, 0, 0, 0.53) !important;
        }

        .content1 {
            box-shadow: none !important;
        }

        .dark-font .breadcrumb-item + .breadcrumb-item::before {
            color: #000;
        }

        .dark-font .breadcrumb-item.active {
            color: #455a64;
        }

        .pageheader .breadcrumb li {
            font-size: 17px;
        }

        .contentpanel {
            padding: 0 !important;
        }

        button.close {
            position: absolute;
            top: 10px;
            right: 10px;
            z-index: 100;
            color: #000;
            cursor: pointer;
            opacity: .2;
            text-align: center;
            line-height: 20px;
            border-radius: 50%;
            background: 0 0;
            font-size: 16px;
            width: 25px;
            height: 20px;
            font-weight: 400;
            border: 1px solid #2c2b2b;
            padding-bottom: 22px;
        }

        .theme-switcher-btn {
            margin-right: 10px;
            cursor: pointer;
        }

        .theme-switcher-btn i {
            font-size: 14px !important;
        }

    </style>

    <!--<div ng-include="'app/desktop/modules/main/navigationPanel.jsp'"></div>-->
    <jsp:include page="navigationPanel.jsp"/>

    <section style="height:100%;" window-resized>
        <iframe id="download_file_iframe" style="display:none;"></iframe>
        <div id="mainPanel" class="mainpanel" style="height:100%;">

            <div id="headerbar" class="headerbar">
                <span ng-if="mainVm.user.external == false"
                      ng-include="'app/desktop/modules/main/topNavigation.jsp'"></span>
                <span ng-if="mainVm.user.external == true"
                      ng-include="'app/desktop/modules/main/topNavigationExternalUser.jsp'"></span>
            </div>
            <div id="workspace" style="overflow-x: hidden; overflow-y: hidden;">
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
                <div id="viewTitleContainer" class="pageheader" ng-if="viewInfo.showDetails == true"
                     style="padding:0 10px 0 10px;height: 55px !important;">

                    <style scoped>
                        table {
                            border-collapse: collapse;
                        }

                        .add-widgets {
                            font-size: 32px !important;
                            padding-top: 6px !important;
                            margin-top: 5px;
                            color: rgb(111, 110, 110) !important;
                        }

                        .add-widgets:hover {
                            color: #969696 !important;
                            border-color: #969696 !important;
                        }
                    </style>
                    <table style="width:100%; height: 100%;">
                        <tbody>
                        <tr>
                            <td valign="middle"
                                style="padding: 0 !important; width: 1px;white-space: nowrap;padding-top: 6px !important;">
                                <h3 style="margin: 0">
                                    <i class="fa" ng-class="viewInfo.icon" style="position: absolute;"></i>

                                    <div ng-class="{'fix-title': viewInfo.description == null || viewInfo.description == ''}"
                                         style="display: inline-block;margin-left: 55px;">
                                        <div ng-bind-html="viewInfo.title | translate"
                                             style="display: inline-block"></div>
                                        <div ng-if="viewInfo.description != null && viewInfo.description != ''"
                                             title="{{viewInfo.description}}"
                                             style="font-size: 14px; opacity: 0.7; font-weight: 400">
                                            {{viewInfo.description | limitTo: 100 | unsafe
                                            }}{{viewInfo.description.length >
                                            100 ? '...'
                                            : ''}}
                                        </div>
                                    </div>
                                </h3>
                            </td>
                            <td style="padding: 0 !important;text-align: center !important; vertical-align: middle !important;width: 70% !important;padding-bottom: 3px !important;">
                                <div ng-if="mainVm.lifecyclePhases.length > 0"
                                     style="width: 80%;margin-left: auto;margin-right: auto;">
                                    <ul id="breadcrumb" style="margin: 0px !important;">
                                        <li ng-repeat="phase in mainVm.lifecyclePhases"
                                            ng-if="phase.phaseType != 'OBSOLETE'">
                                            <a href="javascript:void(0);"
                                               ng-class="{'finished': phase.finished && !phase.rejected, 'current': phase.current && !phase.rejected, 'rejected': phase.current && phase.rejected}">
                                                <i ng-if="(phase.finished || phase.current) && !phase.rejected"
                                                   class="fa fa-check"
                                                   style="border: none;font-size: 16px;"></i>
                                                <i ng-if="(phase.finished || phase.current) && phase.rejected"
                                                   class="fa fa-times"
                                                   style="border: none;font-size: 16px;"></i>
                                                {{phase.name}}</a>
                                        </li>
                                    </ul>
                                </div>
                            </td>
                            <td valign="middle" class="text-right"
                                style="width: 1px;white-space: nowrap; padding: 0 !important;">
                                <nav aria-label="breadcrumb" style="margin-top: 19px;margin-left: -140px">
                                    <ol class="breadcrumb purple lighten-4">
                                        <li class="breadcrumb-item" style="cursor: pointer"
                                            ng-if="breadCrumb.project != null && breadCrumb.project != ''"><a
                                                class="black-text" ><span
                                                ng-bind-html="breadCrumb.project.name"></span></a></li>
                                        <li class="breadcrumb-item" style="cursor: pointer;"
                                            ng-if="breadCrumb.activity != null && breadCrumb.activity != ''"><a
                                                class="black-text"
                                                ng-click="mainVm.breadcrumbView(breadCrumb.activity)"><span
                                                ng-bind-html="breadCrumb.activity.name"></span></a></li>
                                        <li class="breadcrumb-item" style="cursor: pointer;"
                                            ng-if="breadCrumb.task != null && breadCrumb.task != ''">
                                            <a class="black-text"
                                               ng-click="mainVm.breadcrumbView(breadCrumb.task)"><span
                                                    ng-bind-html="breadCrumb.task.name"></span></a></li>
                                    </ol>
                                </nav>
                            </td>

                            <td valign="middle" class="text-right"
                                style="width: 1px;white-space: nowrap; padding: 0 !important;">
                                <tags-btn ng-if="mainVm.tags.show"
                                          object-type="mainVm.tags.objectType"
                                          object="mainVm.tags.object"
                                          tags-count="mainVm.tags.tagsCount"></tags-btn>
                                <comments-btn ng-if="mainVm.comments.show"
                                              object-type="mainVm.comments.objectType"
                                              object-id="mainVm.comments.objectId"
                                              comment-count="mainVm.comments.commentCount"></comments-btn>

                                <a href="">
                                    <i ng-if="mainVm.currentState == 'app.home' && loginPersonDetails.external == false"
                                       style="color: #ddd;border-color: #ddd;"
                                       class="la la-plus add-widgets"
                                       ng-click="mainVm.onClickAddWidgets()"
                                       title="{{ClickAddWidget}}"></i>
                                </a>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>

                <div id="appNotification" class="alert animated" ng-class="notification.type"
                     style="display: none;padding: 11px !important;margin-bottom: 0 !important;">
                    <span style="margin-right: 10px;"><i class="fa" ng-class="notification.class"></i></span>
                    <a href="" class="fancy-close-btn" ng-click="closeNotification()"></a>
                    <span id="alertMessage">{{notification.message}}</span>
                    <button class="btn btn-xs btn-danger" ng-show="notification.undo"
                            style="float: right;margin-right: 20px;" ng-click="undo()">
                        <span class="badge"
                              style="padding:3px 4px;border-radius: 50%;margin-right: 3px;">{{mainVm.seconds}}</span>UNDO
                    </button>
                </div>

                <div>
                    <div id="contentpanel" class="contentpanel" plmcontentpanel>
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
            <div style="line-height: 28px">&#169;
                <%=Calendar.getInstance().get(Calendar.YEAR)%> Cassini<span style="color: #00b5a4">PLM</span><span>. All Rights Reserved.</span>
                <span style="float: right;">
                    <span>v{{version}}&nbsp;-&nbsp;{{date}} &nbsp;&nbsp;</span>
                    <span class="theme-switcher-btn" ng-click="mainVm.switchTheme()">
                        <i class="las la-desktop" title="Switch application theme"></i>
                    </span>
                    <span style="float: right;cursor: pointer !important;" class="info"
                          ng-if="(hasPermission('about','view') || hasPermission('about','edit')) && !mainVm.user.external"
                          ng-click="mainVm.systemInfo()"><i
                            title="{{dataSpaceInformation}}" class="fa fa-info-circle" aria-hidden="true"></i>
                    </span>
                </span>
            </div>
        </div>

        <!-- Modal -->
        <div class="modal fade modal1" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
             data-backdrop="false"
             style="display: none!important;">
            <div class="modal-dialog" role="document">
                <div class="modal-content content1">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                ng-click="closeModal()">&times;</button>
                        <h4 class="modal-title" style="text-align: center !important;" id="myModalLabel" translate>
                            DATA_SPACE_HEADER</h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <canvas id="pie-chart" width="800" height="450"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <div ng-include="'app/desktop/modules/main/quickAccessPanel.jsp'"
         ng-controller="QuickAccessController as quickAccessVm"></div>

    <div ng-include="'app/desktop/modules/main/commandCenterPanel.jsp'"
         ng-controller="CommandCenterController as commandCenterVm"></div>

    <div ng-controller="PrintController"></div>

    <div ng-controller="ObjectRightsController"></div>

    <div ng-controller="CommonController"></div>

    <div id="objectSelectionView"
         ng-include="'app/desktop/modules/select/objectSelectionView.jsp'"
         ng-controller="ObjectSelectionController as objSelectionVm"></div>

    <div id="themeSwitcherView"
         ng-include="'app/assets/bower_components/cassini-platform/app/desktop/modules/login/themeSwitcherView.jsp'"
         ng-controller="ThemeSwitcherController as themeSwitcherVm"></div>
</div>

