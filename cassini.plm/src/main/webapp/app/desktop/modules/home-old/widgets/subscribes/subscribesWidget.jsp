<div>
<style scoped>
    #td {
        word-wrap: break-word;
        width: 200px;
        white-space: normal;
        text-align: left;
    }
</style>
<div class="panel panel-default panel-alt widget-messaging">
    <div class="panel-heading" style="">

        <div class="col-sm-6" style="margin-top: 15px;margin-left: 15px;color: #002451;font-size: 16px;">
            <span style="font-weight: bold;" translate>MY_SUBSCRIBES</span>
        </div>

        <div class="pull-right text-center" style="padding: 2px;">
            <span ng-if="subscribesWidgetVm.loading == false"><small>
                {{subscribesWidgetVm.subscribeObjects.totalElements}}
                <span translate>MY_SUBSCRIBES</span>
            </small></span>

            <div class="btn-group" style="">
                <button class="btn btn-xs btn-default"
                        ng-click="subscribesWidgetVm.previousPage()"
                        ng-disabled="subscribesWidgetVm.subscribeObjects.first">
                    <i class="fa fa-chevron-left"></i>
                </button>
                <button class="btn btn-xs btn-default"
                        ng-click="subscribesWidgetVm.nextPage()"
                        ng-disabled="subscribesWidgetVm.subscribeObjects.last">
                    <i class="fa fa-chevron-right"></i>
                </button>
            </div>
        </div>
    </div>

    <div class="panel-body">
        <div class="widget-panel" style="max-height: 400px; min-height: 400px;">
            <%--<div id="apexChart" style="max-height: 400px; min-height: 400px;"></div>--%>
            <div class="responsive-table" style="padding: 10px;">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr style="font-size: 14px;">
                        <th class="col-width-250" translate>NAME</th>
                        <th style="width: 125px;" translate>OBJECT_TYPE</th>
                        <th style="width: 150px;" translate>ACTIONS</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-if="subscribesWidgetVm.loading == false && subscribesWidgetVm.subscribeObjects.content.length == 0">
                        <td colspan="10" translate>NO_SUBSCRIBES</td>
                    </tr>
                    <tr ng-repeat="subscribe in subscribesWidgetVm.subscribeObjects.content"
                        style="font-size: 14px;">
                        <td style="vertical-align: middle;">
                            <a href="" title="{{subscribesWidgetVm.detailsTitle}}"
                               ng-click="subscribesWidgetVm.showObjectDetails(subscribe)">{{subscribe.name}} </a>
                        </td>
                        <td style="vertical-align: middle;">
                            <object-type-status object="subscribe"></object-type-status>
                        </td>
                        <td>
                            <div class="btn-group">

                                <button class="btn btn-xs btn-default" title="{{unsubscribeTitle}}"
                                        ng-if="subscribe.type=='SPECIFICATION' || subscribe.type=='REQUIREMENT'"
                                        ng-click="subscribesWidgetVm.subscribeSpecification(subscribe)">
                                    <i ng-if="subscribe != null"
                                       class="la la-bell-slash"
                                       style="font-size: 16px;"></i>
                                </button>

                                <button class="btn btn-xs btn-default" title="{{unsubscribeTitle}}"
                                        ng-if="subscribe.type=='ITEM'"
                                        ng-click="subscribesWidgetVm.subscribeItem(subscribe)">
                                    <i ng-if="subscribe != null"
                                       class="la la-bell-slash"
                                       style="font-size: 16px;"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</div>