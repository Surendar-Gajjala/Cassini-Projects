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

                .pageheader h2 span::before {
                    content: '';
                    margin: 0 10px 0 5px;
                    color: #ccc;
                }

                .panel-summary {
                    height: 38px;
                    margin: 3px -20px 0px 8px;
                }

                .panel-summary h2,
                .panel-summary h1 {
                    text-align: center;
                }

                .panel-summary h1 {
                    font-size: 18px;
                }

                .panel-finish {
                    background: #159957; /* fallback for old browsers */
                    background: -webkit-linear-gradient(to right, #159957, #155799); /* Chrome 10-25, Safari 5.1-6 */
                    background: linear-gradient(to right, #159957, #155799); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
                }

                .panel-inprogress {
                    background: #fdc830; /* fallback for old browsers */
                    background: -webkit-linear-gradient(to right, #fdc830, #f37335); /* Chrome 10-25, Safari 5.1-6 */
                    background: linear-gradient(to right, #fdc830, #f37335); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
                }

                .panel-notyetstarted {
                    background: #da4453; /* fallback for old browsers */
                    background: -webkit-linear-gradient(to right, #da4453, #89216b); /* Chrome 10-25, Safari 5.1-6 */
                    background: linear-gradient(to right, #da4453, #89216b); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
                }
            </style>
            <div id="viewTitleContainer" class="pageheader"
                 style="background-color: #FFF !important; border-bottom: 1px solid #D0DDE1; padding:10px 10px;">
                <div class="row" style="margin: 0">
                    <div class="col-sm-5" style="padding-left: 0;">
                        <h3 style="margin: 0">
                            <h2><i class="fa {{viewInfo.icon}}"></i> {{viewInfo.title}} <span
                                    style="font-size: 14px;display: inline-flex;margin-top: 9px;">
                                 <p ng-if="viewInfo.testrunDate != null" style="margin-right: 5px;color: black;">
                                     Date:<p style="color: black;margin-right: 7px;font-weight: bolder">
                                {{viewInfo.testrunDate}}</p>
                                </span></h2>
                        </h3>
                        <span style="margin-left: 37px;font-size: 14px;display: inline-flex;">
                             <p ng-if="viewInfo.runScenario != null"
                                style="margin-right: 5px;color: black;">Scenario:
                             <p style="color: blue;margin-right: 7px;color: black;font-weight: bolder">
                                 {{viewInfo.runScenario}}</p>&nbsp;&nbsp;&nbsp;

                             <p ng-if="viewInfo.testRunConfigurationName != null"
                                style="margin-right: 5px;color: black;">
                                 Run Configuration :<p style="color: black;font-weight: bolder">
                            {{viewInfo.testRunConfigurationName}}</p>

                            </p>

                         </span>
                    </div>

                    <%------- TestRunDetails starting Div -------%>


                    <div class="col-sm-2" ng-if="viewInfo.total != null">
                        <div class="panel panel-summary panel-finish">
                            <div class="row">
                                <div class="col-sm-7" style="height: 100% !important;border-right: 2px solid #fff;">
                                    <div style="width: 100%;height: 70px;">
                                        <h2 style="margin: 0;line-height: 42px;color: #fff;font-size: 18px;">Total</h2>
                                    </div>
                                </div>
                                <div class="col-sm-5">
                                    <div style="width: 100%;height: 70px;">
                                        <h1 style="margin: 0;line-height: 42px;color: #fff;font-size: 18px;">
                                            {{viewInfo.total}}</h1>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-2" ng-if="viewInfo.passed != null">
                        <div class="panel panel-summary panel-inprogress">
                            <div class="row">
                                <div class="col-sm-7" style="height: 100% !important;border-right: 2px solid #fff;">
                                    <div style="width: 100%;height: 70px;">
                                        <h2 style="margin: 0;line-height: 42px;color: #fff; font-size: 18px;">
                                            Passed</h2>
                                    </div>
                                </div>
                                <div class="col-sm-5">
                                    <div style="width: 100%;height: 70px;">
                                        <h1 style="margin: 0;line-height: 42px;color: #fff;font-size: 18px;">
                                            {{viewInfo.passed}}</h1>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-2" ng-if="viewInfo.failed != null">
                        <div class="panel panel-summary panel-notyetstarted">
                            <div class="row">
                                <div class="col-sm-7" style="height: 100% !important;border-right: 2px solid #fff;">
                                    <div style="width: 100%;height: 70px;">
                                        <h2 style="margin: 0;line-height: 42px;color: #fff;font-size: 18px;">Failed</h2>
                                    </div>
                                </div>
                                <div class="col-sm-5">
                                    <div style="width: 100%;height: 70px;">
                                        <h1 style="margin: 0;line-height: 42px;color: #fff;">{{viewInfo.failed}}</h1>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>


                    <%------- TestRunDetails Ending Div -------%>

                    <div class="col-sm-2 text-right" style="padding-right: 0;">
                        <comments-btn ng-if="mainVm.comments.show"
                                      object-type="mainVm.comments.objectType"
                                      object-id="mainVm.comments.objectId"></comments-btn>
                        <a href="">
                            <i ng-if="mainVm.currentState == 'app.home' && loginPersonDetails.external == false"
                               style="color: darkgrey;border-color: darkgrey;"
                               class="fa fa-plus"
                               ng-click="mainVm.addAndRemoveWidgets()"
                               title="{{ClickAddWidget}}"></i>
                        </a>
                        </button>
                    </div>
                </div>
            </div>

            <div id="appNotification" class="alert animated" ng-class="notification.type" style="display: none;">
                <span style="margin-right: 10px;"><i class="fa" ng-class="notification.class"></i></span>
                <button type="button" class="close" ng-click="closeNotification()">x</button>
                {{notification.message}}
            </div>

            <div>
                <div id="contentpanel" class="contentpanel" style="padding: 30px;" contentpanel>
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


