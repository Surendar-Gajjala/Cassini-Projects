<div>
<style scoped>
    .name-column2 {
        word-wrap: break-word !important;
        min-width: 250px !important;
        width: 250px !important;
        white-space: normal !important;
        text-align: left !important;
    }
</style>
<div class="panel panel-default panel-alt widget-messaging">
    <div class="panel-heading" style="">

        <div class="col-sm-6" style="margin-top: 15px;margin-left: 15px;color: #002451;font-size: 16px;">
            <span style="font-weight: bold;" translate>MY_REQUIREMENTS</span>
        </div>

        <div class="pull-right text-center" style="padding: 2px;">
             <span ng-if="requirementsWidgetVm.loading == false"><small>
                 {{requirementsWidgetVm.requirements.totalElements}}
                 <span translate>MY_REQUIREMENTS</span>
             </small></span>

            <div class="btn-group" style="">
                <button class="btn btn-xs btn-default"
                        ng-click="requirementsWidgetVm.previousPage()"
                        ng-disabled="requirementsWidgetVm.requirements.first">
                    <i class="fa fa-chevron-left"></i>
                </button>
                <button class="btn btn-xs btn-default"
                        ng-click="requirementsWidgetVm.nextPage()"
                        ng-disabled="requirementsWidgetVm.requirements.last">
                    <i class="fa fa-chevron-right"></i>
                </button>
            </div>

        </div>
    </div>

    <div class="panel-body">
        <div class="widget-panel" style="max-height: 400px; min-height: 400px;">

            <div class="responsive-table" style="padding: 10px;">
                <table class="table table-striped highlight-row">
                    <thead>
                    <tr style="font-size: 14px;">
                        <th style="width: 150px" translate>REQUIREMENT</th>
                        <th class="name2-column" translate>SPECIFICATION</th>
                        <th class="name2-column" translate>DESCRIPTION</th>
                        <th style="width: 150px" translate>STATUS</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-if="requirementsWidgetVm.loading == false && requirementsWidgetVm.requirements.content.length == 0">
                        <td colspan="7"><span translate>NO_REQUIREMENTS</span></td>
                    </tr>
                    <tr ng-repeat="req in requirementsWidgetVm.requirements.content" style="font-size: 14px;">
                        <td style="width: 150px">
                            <a href="" ng-click="requirementsWidgetVm.showItem(req)">{{req.objectNumber}}</a>
                        </td>
                        <td class="name2-column">{{req.specificationObject.objectNumber}}</td>
                        <td class="">

                            <span
                                    style="white-space: normal;word-wrap: break-word;">
                     <div style="-webkit-column-width: 250px;-moz-column-width: 250px;column-width: 250px;
                     webkit-column-count: 1;-moz-column-count: 1;column-count: 1;margin-top: 9px;">
                         <span style="max-height: 138px !important;display:block;overflow:auto !important;"
                               ng-bind-html="req.description"></span>
                     </div>
                   </span>

                        </td>
                        <td id="td">
                            <task-status task="req"></task-status>
                            </span>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</div>