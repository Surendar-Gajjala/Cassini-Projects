<style scoped>
    .mask-panel {
        display: none;
        position: fixed;
        top: 125px;
        left: 0;
        bottom: 0px;
        right: 0;
        opacity: 0.5;
        background-color: #0a0a0a;
        z-index: 9998;
    }

    .folder-btn {
        position: absolute;
        left: 0;
        top: 50%;
        margin-top: -15px;
        font-size: 18px;
        color: #ffffff;
        border-radius: 0 4px 4px 0;
        text-align: center;
        z-index: 9999;
        border: 1px solid #357ebd;
        cursor: pointer;
        height: 50px;
        line-height: 50px;
        width: 28px;
        background-color: #428bca;
    }

    .folder-btn:hover {
        color: #ffffff !important;
        background-color: #3071a9;
        border-color: #285e8e
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

    .headerbar {
        height: 50px;
        background-color: #343A40 !important;
    }

    .headerbar .topnav > ul > li .dropdown-menu i {
        display: inline-block;
        margin-right: 5px;
    }

    .label-important {
        background: #007BFF !important;
    }

    .label-success {
        background: #28A745 !important;
    }

    .label-information {
        background: #17A2B8 !important
    }

    .label-warning {
        background: #FFC107 !important;
    }

    .label-danger {
        background: #DC3545 !important;
    }

    .label-secondary {
        background: #6C757D !important
    }

    .label-secondary1 {
        background: #343A40 !important
    }

    .label-lightblue {
        background-color: #0390fd;
    }

    .label-inverse {
        background-color: #333333;
    }

    .sidePanel1 {
        position: fixed;
        top: 146px !important;
        bottom: 30px;
        background-color: #fff;
        z-index: 9999;
    }

    .app-side-panel {
        position: fixed;
        top: 125px !important;
        bottom: 0px !important;
        background-color: #FFF;
        z-index: 9999;
    }

    .app-side-panel .buttons-panel {
        position: fixed;
        bottom: 0px !important;
        height: 50px;
        padding: 8px;
        width: 500px;
        background-color: #EEE;
        border-top: 1px solid #DDD;
    }

    .contentpanel {
        padding: 0px !important;
    }

    .appcontent {
        margin-bottom: 0px !important;
    }

    .alert-success {
        background: rgba(12, 13, 14, 0.77) !important;
        color: white;
    }
</style>
<div id="sideNavigation" class="">
    <div class="leftpanelinner">
        <h5 class="sidebartitle">Navigation</h5>
        <ul class="nav nav-pills nav-stacked nav-bracket">
            <li id="nav-home">
                <a ui-sref="app.home">
                    <i class="fa fa-home" style="font-size: 15px;"></i>
                    <span class="hidden-xs hidden-sm" translate="Home"></span>
                </a>
            </li>
        </ul>
    </div>
    <!-- leftpanelinner -->
</div>
<section style="height:100%;" window-resized>
    <div id="mainPanel" class="mainpanel" style="height:100%;">
        <%--<div id="irsteHeaderImage" style="background-color: #343A40;">
            <img src="app/assets/images/irsteBanner1.png" style="width: 40% !important;margin-left:25%">
        </div>--%>

        <div id="headerbar" class="headerbar">
            <span ng-include="'app/desktop/modules/main/topNavigation.jsp'"></span>
        </div>
        <%-- <div class="headerbar">
             <span ng-include="'app/desktop/modules/welcome/welcomeView.jsp'"></span>
         </div>--%>
        <div id="workspace" style="overflow-x: hidden; overflow-y: hidden; background-color: #F2F7F9">
            <style scoped>
                #viewTitleContainer h3 {
                    font-size: 22px;
                }
            </style>
            <div id="viewTitleContainer" class="pageheader"
                 style="background-color: #FFF !important; border-bottom: 1px solid #D0DDE1; padding:15px 15px;">
                <div class="row" style="margin: 0">
                    <div class="col-sm-10">
                        <h2>
                            <i class="fa {{viewInfo.icon}}"></i>{{viewInfo.title}}
                        </h2>
                    </div>

                    <div class="col-sm-2 text-right" style="padding-right: 0;">
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
                <div id="contentpanel" class="contentpanel" style="overflow-y: auto" irstecontentpanel>
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
         ng-controller="SidePanelsController as sidePanelsVm" style="top:146px !important;">

    </div>

</section>


