<div>
    <style scoped>
        .tabs {
            width: 100%;
            height: 50px;
            z-index: 1000;
            background-color: #fff;
            border-bottom: 1px solid #ddd;
        }

        .tabopen {
            border-bottom: 5px #3DA0D6 solid;
            color: #3DA0D6 !important;
        }

        .tab {
            font-family: Roboto;
            float: left;
            height: 50px !important;
            padding-top:15px;
            color: #000;
            text-align: center;
            cursor: pointer;
            padding-right: 10px;
            padding-left: 10px;
        }

        #communication-tab-content {
        }
        #communication-tab-content > div {
            height: 100%;
            padding: 10px;
        }

        .full-height {
            height: 100%;
        }
    </style>

    <div class="tabs">
        <div class="tab" ng-repeat="tab in securityVm.tabs" id="{{tab.view}}" ng-click="securityVm.setTabActive(tab)" ng-class="{'tabopen':tab.view == securityVm.currentTab}" data="tab.state">
            <i class="{{('fa mr5 '+ tab.icon)}}"></i>{{tab.view}}
        </div>
    </div>
    <div ui-view></div>
</div>

