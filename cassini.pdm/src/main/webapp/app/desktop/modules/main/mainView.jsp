<%@ page import="java.util.Calendar" %>
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
    .contentpanel {
        padding: 0 !important;
    }

    .headerbar {
        background-color: #00253f;
    }

</style>
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
            <div id="viewTitleContainer" class="pageheader" ng-if="viewInfo.showDetails == true"
                 style="background-color: #FFF !important; padding:0 10px 0 10px;height: 60px !important;">
                <div class="row" style="margin: 0;padding-top: 8px;">
                    <div class="col-sm-11">
                        <h3 style="margin: 0">
                            <i class="fa" ng-class="viewInfo.icon" style="position: absolute;"></i>

                            <div ng-class="{'fix-title': viewInfo.description == null || viewInfo.description == ''}"
                                 style="display: inline-block;margin-left: 45px;">
                                <div ng-bind-html="viewInfo.title | unsafe" style="display: inline-block"></div>
                                <div ng-if="viewInfo.description != null && viewInfo.description != ''"
                                     ng-bind-html="viewInfo.description | unsafe"
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

            <div id="appNotification" class="alert animated" ng-class="notification.type" style="display: none;">
                <span style="margin-right: 10px;"><i class="fa" ng-class="notification.class"></i></span>
                <button type="button" class="close" ng-click="closeNotification()">x</button>
                {{notification.message}}
            </div>

            <div>
                <div id="contentpanel" class="contentpanel" pdmcontentpanel>
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

    <%--<div id="footer">
        <div>&#169;<span class="mr5"></span>Cassini Systems</div>
    </div>--%>
    <div id="footer">
        <div style="line-height: 28px">&#169;
            <%=Calendar.getInstance().get(Calendar.YEAR)%> Cassini<span style="color: #00b5a4">PLM</span><span>. All Rights Reserved.</span>
        </div>
    </div>

</section>

