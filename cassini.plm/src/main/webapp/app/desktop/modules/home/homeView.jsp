<div ng-if="mainVm.user.external === false">
    <style scoped>
        .view-container .view-content {
            padding: 0;
            overflow-y: auto;
        }

        .view-container .view-content > div {
            height: 100%;
        }
    </style>
    <div class="view-container" fitcontent>
        <div class="view-content" ng-include="'app/desktop/modules/home/dashboard/homeDashboardView.jsp'"
             ng-resolve="app/desktop/modules/home/dashboard/homeDashboardController"
             ng-controller="HomeDashboardController as homeDashboardVm">
        </div>
    </div>
</div>

<div ng-if="mainVm.user.external == true">
    <div ng-include="'app/desktop/modules/home/external/externalUserView.jsp'"
         ng-resolve="app/desktop/modules/home/external/externalUserController"
         ng-controller="ExternalUserController as externalUserVm">
    </div>
</div>