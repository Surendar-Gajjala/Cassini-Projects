<div>
<style scoped>
    .home-content {
        /*padding: 10px !important;*/
    }

    .home-content .home-widget {
        border-radius: 5px;
    }

    .home-content .home-widget .home-widget-heading {
        height: 50px;
        line-height: 50px;
        vertical-align: middle;
        padding-left: 20px;
        border-bottom: 1px solid #eee;
        font-size: 18px;
    }

    .home-content .home-widget .home-widget-body {
        padding: 10px;
        min-height: 300px;
        max-height: 300px;
        overflow-y: auto;
    }

    .home-content > .home-content-row {
        display: -webkit-flex;
        display: flex;
        margin-bottom: 10px;
    }

    .home-content > .home-content-row > .home-content-col {
        -webkit-flex-grow: 1;
        flex-grow: 1;
        margin-left: 10px;
    }

    .home-content > .home-content-row > .home-content-col:first-child {
        margin-left: 0;
    }

    .panel.panel-default .panel-heading {
        background-color: #fff !important;
        padding: 0;
        height: 50px;
        border: 1px solid #dddddd;
    }

    .panel .panel-heading .pull-right .btn-group {
        display: inline-block;
        margin: 0 10px 0 0;
    }

    .panel .panel-heading .pull-right .btn-group .btn {
        border: 0;
        background-color: #fff;
    }

    .panel .panel-heading .pull-right {
        margin-top: 10px;
        padding: 2px;
    }

    .panel .panel-heading .pull-right small {
        color: #ccc;
    }

</style>
<div class="view-container" ng-if="mainVm.user.external == false" fitcontent>
    <div class="view-content home-content no-padding" style="overflow-y: auto;">
        <div class="home-content-row">
            <div class="home-content-col">
                <div class="home-widget" style="height: 983px;">
                    <div class="view-content" id="addWidgetId">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="view-container" ng-if="mainVm.user.external == true" fitcontent>
    <div ng-include="'app/desktop/modules/home/external/externalUserView.jsp'"
         ng-resolve="app/desktop/modules/home/external/externalUserController"
         ng-controller="ExternalUserController as externalUserVm">
    </div>
</div>
</div>