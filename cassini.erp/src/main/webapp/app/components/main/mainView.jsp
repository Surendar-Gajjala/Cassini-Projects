<section style="height:100%;" window-resized>
    <!--
    <div class="leftpanel sticky-leftpanel">

        <div class="logopanel">
            <h1 style="text-align:center;">Cassini.<span style="color: #7BB7EB;">ERP</span></h1>
        </div>

        <div class="leftpanelinner">

            <h5 class="sidebartitle" style="text-align: center; padding-top: 8px;">Navigation</h5>

            <ul class="nav nav-pills nav-stacked nav-bracket" ng-controller="NavController">
                <li ng-repeat="nav in navigation" ng-class="getCss(nav)">
                    <a ng-if="nav.state != null" ui-sref="{{nav.state}}" ng-click="activate(nav); closeOtherNavs(nav);">
                        <i class="fa" ng-class="nav.icon"></i> <span>{{nav.text}}</span>
                    </a>

                    <a ng-if="nav.state == null" href="" ng-click="activate(nav); closeOtherNavs(nav);">
                        <i class="fa" ng-class="nav.icon"></i> <span>{{nav.text}}</span>
                    </a>

                    <ul ng-if="nav.children.length > 0" class="children">
                        <li ng-repeat="child in nav.children" ng-class="getChildCss(child)">
                            <a ng-if="child.state != null" ui-sref="{{child.state}}" ng-click="activate(child)">
                                <i class="fa" ng-class="child.icon"></i> {{child.text}}
                            </a>
                            <a ng-if="child.state == null" href="" ng-click="activate(child)">
                                <i class="fa" ng-class="child.icon"></i> {{child.text}}
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>

        </div>
    </div>
    -->

    <div id="mainPanel" class="mainpanel mainpanel-fullscreen" style="height:100%;">

        <div id="headerbar" class="headerbar" ng-controller="OrdersWidgetController">
            <span ng-include="templates.topnav"></span>
        </div>

        <div id="workspace" style="overflow-x: hidden; overflow-y: hidden; background-color: #F2F7F9">
            <div id="viewTitleContainer" class="pageheader"
                 style="background-color: #FFF !important; border-bottom: 1px solid #D0DDE1; padding-bottom:15px;">

                <div class="pull-right" ng-show="showHelpIcon">
                    <a href="" title="Help" ng-click="toggleHelp()">
                        <img style="width: 22px; height: 22px; margin-top: 5px;" src="app/assets/images/help3.png">
                    </a>
                </div>

                <h2><i class="fa" ng-class="iconClass"></i> {{viewTitle}} </h2>
            </div>

            <div id="appNotification" class="alert animated" ng-class="notification.type" style="display: none;">
                <span style="margin-right: 10px;"><i class="fa" ng-class="notification.class"></i></span>
                <button type="button" class="close" ng-click="closeNotification()">x</button>
                {{notification.message}}
            </div>

            <div style="display: none; height: 50px; padding-top: 8px; padding-left: 10px;
                    border-bottom: 1px solid #D0DDE1; background-color: #E4E7EA;">
                <button class="btn btn-sm btn-default">Button 1</button>
                <button class="btn btn-sm btn-primary">Button 1</button>
                <button class="btn btn-sm btn-success">Button 1</button>
                <button class="btn btn-sm btn-info">Button 1</button>
                <button class="btn btn-sm btn-warning">Button 1</button>
                <button class="btn btn-sm btn-danger">Button 1</button>
            </div>

            <div>
                <div id="helpPanel" ng-include="templates.help"></div>

                <div id="contentpanel" class="contentpanel" style="padding: 30px; overflow-y: auto" contentpanel>
                    <div id="appcontent" class="appcontent">
                        <div class="content-panel-toolbar">
                            <script type="text/ng-template" id="main-view-tb">
                                <div></div>
                            </script>

                            <div ng-include src="toolbarTemplate"></div>
                        </div>

                        <div ui-view style="padding: 20px">

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