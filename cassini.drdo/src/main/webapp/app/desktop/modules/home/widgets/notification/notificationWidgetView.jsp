<style scoped>
    .heading {
        margin-bottom: 5px !important;
        margin-top: 5px !important;
    }

    .panel-heading {
        height: 40px !important;
    }

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    .col-md-4 {
        padding-left: 0px;
    }
</style>
<div class="panel panel-default panel-alt widget-messaging" style="padding: 20px 5px 10px;">
    <div class="panel-heading">
        <div class="col-md-12">
            <div class="col-md-4">
                <h4 class="heading">Latest Notifications</h4>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="margin-top: 5px !important;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{notificationWidgetVm.notifications.numberOfElements}} of
                                            {{notificationWidgetVm.notifications.totalElements}}
                                    </span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{notificationWidgetVm.notifications.totalElements != 0 ? notificationWidgetVm.notifications.number+1:0}} of {{notificationWidgetVm.notifications.totalPages}}</span>
                        <a href="" ng-click="notificationWidgetVm.previousPage()"
                           ng-class="{'disabled': notificationWidgetVm.notifications.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="notificationWidgetVm.nextPage()"
                           ng-class="{'disabled': notificationWidgetVm.notifications.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="panel-body" style="overflow-x: hidden">
        <div class="widget-panel" style="max-height: 400px; min-height: 400px;overflow-x: auto">

        </div>
    </div>
</div>