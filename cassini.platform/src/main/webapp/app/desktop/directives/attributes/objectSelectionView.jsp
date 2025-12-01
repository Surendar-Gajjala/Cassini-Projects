<div class="view-container" fitcontent>
    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">
        <div>
            <div class="input-group input-group-sm mb15"
                 style="background-color: #FFF !important;margin-top: 18px;width: 77%;margin-left: 6px;position: absolute;">
            <span class="input-group-btn">
                <button type="button" ng-click="objectSelectionVm.resetPage()" class="btn"
                        title="Clear search"
                        style="height: 30px !important;background-color: #FFF !important;border: 1px solid #EADADA">
                    <i class="fa fa-times-circle" style="font-size:16px"></i>
                </button>
            </span>
                <input class="form-control" type="text" ng-enter="objectSelectionVm.freeTextSearch()"
                       ng-model="objectSelectionVm.searchTerm">
            <span class="input-group-btn">
                <button type="button" ng-click="objectSelectionVm.freeTextSearch()" class="btn"
                        title="Search"
                        style="height: 30px !important;background-color: #FFF !important;border: 1px solid #EADADA">
                    <i class="fa fa-search" style="font-size:15px"></i>
                </button>
            </span>
            </div>
            <div class="pull-right text-center">
                <span ng-if="objectSelectionVm.loading == false"><small><span>page</span>
                    {{objectSelectionVm.objects.totalElements != 0 ? objectSelectionVm.objects.number+1:0}} of
                    {{objectSelectionVm.objects.totalPages}}
                </small></span>
                <br>

                <div class="btn-group" style="margin-bottom: 0">
                    <button class="btn btn-xs btn-default" ng-click="objectSelectionVm.previousPage()"
                            ng-disabled="objectSelectionVm.objects.first">
                        <i class="fa fa-chevron-left"></i></button>
                    <button class="btn btn-xs btn-default" ng-click="objectSelectionVm.nextPage()"
                            ng-disabled="objectSelectionVm.objects.last">
                        <i class="fa fa-chevron-right"></i></button>
                </div>
                <br>
                <span ng-if="objectSelectionVm.objects.totalElements == 1"><small>
                    {{objectSelectionVm.objects.totalElements}} Mfr
                </small></span>
                <span ng-if="objectSelectionVm.objects.totalElements > 1"><small>
                    {{objectSelectionVm.objects.totalElements}}
                    Mfrs
                </small></span>
            </div>
        </div>

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px" translate>SELECT</th>
                <th style="width: 200px;" translate>MANUFACTURER_NAME</th>
                <th style="width: 200px;" translate>MANUFACTURER_TYPE</th>
                <th style="width: 200px;" translate>DESCRIPTION</th>
                <th style="width: 200px;" translate>PHONE_NUMBER</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="objectSelectionVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_MFRS</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="objectSelectionVm.loading == false && objectSelectionVm.objects.content.length == 0">
                <td colspan="12">
                    <span translate>NO_MFRS</span>
                </td>
            </tr>

            <tr ng-repeat="manufacturer in objectSelectionVm.objects.content"
                ng-click="manufacturer.isChecked = !manufacturer.isChecked; objectSelectionVm.radioChange(manufacturer, $event)"
                ng-dblClick="manufacturer.isChecked = !manufacturer.isChecked; objectSelectionVm.selectRadioChange(manufacturer, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="manufacturer.isChecked" name="manufacturer" value="manufacturer"
                           ng-dblClick="objectSelectionVm.selectRadioChange(manufacturer, $event)"
                           ng-click="objectSelectionVm.radioChange(manufacturer, $event)">
                </td>
                <td style="width: 200px;">
                    {{manufacturer.name}}
                </td>
                <td style="width: 200px;">{{manufacturer.mfrType.name}}</td>
                <td style="width: 200px;">{{manufacturer.description}}</td>
                <td style="width: 200px;">{{manufacturer.phoneNumber}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>