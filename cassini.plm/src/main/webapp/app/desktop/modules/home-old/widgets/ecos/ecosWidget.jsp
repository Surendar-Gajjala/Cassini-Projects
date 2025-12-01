<div class="panel panel-default panel-alt widget-messaging">
    <div class="panel-heading" style="">

        <div class="col-sm-6" style="margin-top: 15px;margin-left: 15px;color: #002451;font-size: 16px;">
            <span style="font-weight: bold;" translate>PENDING_ECO</span>
        </div>
        <div class="pull-right text-center" style="margin-top: 10px; padding: 2px;">
            <span ng-if="ecoWigVm.loading == false"><small>
                {{ecoWigVm.ecos.totalElements}}
                <span translate>PENDING_ECO</span>
            </small></span>

            <div class="btn-group">
                <button class="btn btn-xs btn-default"
                        ng-click="ecoWigVm.previousPage()"
                        ng-disabled="ecoWigVm.ecos.first">
                    <i class="fa fa-chevron-left"></i>
                </button>
                <button class="btn btn-xs btn-default"
                        ng-click="ecoWigVm.nextPage()"
                        ng-disabled="ecoWigVm.ecos.last">
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
                        <th style="width: 100px" translate>ECO_NUMBER</th>
                        <th style="width: 100px" translate>STATUS</th>
                        <th class="col-width-200" translate>WORKFLOW</th>
                        <th style="width: 100px" translate>CREATED_DATE</th>
                        <%--<th style="width: 100px" translate>RELEASED_DATE</th>--%>
                    </tr>
                    </thead>

                    <tbody>
                    <tr ng-if="ecoWigVm.loading == true">
                        <td colspan="7">
                           <span style="font-size: 15px;">
                               <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                    class="mr5"><span translate>LOADING_ITEMS</span>
                           </span>
                        </td>
                    </tr>

                    <tr ng-if="ecoWigVm.loading == false && ecoWigVm.ecos.content == 0">
                        <td colspan="7" translate>NO_ECO_PENDING</td>
                    </tr>
                    <tr ng-repeat="eco in ecoWigVm.ecos.content" style="font-size: 14px;">
                        <td style="width: 100px">
                            <a href=""
                               ng-click="ecoWigVm.showEcoDetails(eco)">{{eco.ecoNumber}}</a>
                        </td>
                        <td style="width: 100px">{{eco.status}}</td>
                        <td class="col-width-200">{{eco.workflowObject.name}}</td>
                        <td style="width: 100px">{{eco.createdDate}}</td>
                        <%--<td ng-if="currentLang == 'en'" style="width: 100px">{{eco.releasedDate}}</td>--%>
                        <%--<td ng-if="currentLang == 'de'" style="width: 100px">{{eco.releasedDatede}}</td>--%>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>