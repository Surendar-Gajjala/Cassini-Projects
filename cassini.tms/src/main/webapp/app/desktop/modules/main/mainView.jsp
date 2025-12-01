<section style="height: 100%" windoe-resized>
    <div id="mainPanel" class="mainPanel" style="height: 100%">
        <div id="headerbar" class="headerbar">
            <span ng-include="'app/desktop/modules/main/topNavigation.jsp'"></span>
        </div>
        <div id="workspace" style="overflow-x: hidden;overflow-y: hidden;background-color: #F2F7F9">
            <div id="viewTitleContainer" class="pageheader"
                 style="background-color: #FFF !important; border-bottom: 1px solid #D0DDE1; padding-bottom:15px;">
                <h2><i class="fa {{viewInfo.icon}}"></i>{{viewInfo.title}}</h2>
            </div>

            <div id="appNotification" class="alert-animated" ng-class="notification.type" style="display: none">
                <span style="margin-right: 10px"><i class="fa" ng-class="notification.class"></i></span>
                <button type="button" class="close" ng-click="closeNotification()">x</button>
                {{notification.message}}
            </div>
            <div>
                <div id="contentpanel" class="contentpanel" style="padding: 30px; overflow-y: auto" contentpanel>
                    <div id="appcontent" class="appcontent">
                        <div ui-view>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="footer">
        <div>&#169;<span class="mr5"></span>Cassini Systems</div>
    </div>
</section>