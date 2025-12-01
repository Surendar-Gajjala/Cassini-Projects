<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="siteSelectVm.resetPage"
                          on-search="siteSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{siteSelectVm.sites.numberOfElements}} of
                                            {{siteSelectVm.sites.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{siteSelectVm.sites.totalElements != 0 ? siteSelectVm.sites.number+1:0}} of {{siteSelectVm.sites.totalPages}}</span>
                <a href="" ng-click="siteSelectVm.previousPage()"
                   ng-class="{'disabled': siteSelectVm.sites.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="siteSelectVm.nextPage()"
                   ng-class="{'disabled': siteSelectVm.sites.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th style="min-width: 150px;">Name</th>
                <th style="min-width: 150px;">Description</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="siteSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading sites</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="siteSelectVm.loading == false && siteSelectVm.sites.content.length == 0">
                <td colspan="12">
                    <span translate>No sites</span>
                </td>
            </tr>

            <tr ng-repeat="site in siteSelectVm.sites.content | filter: search"
                ng-click="site.isChecked = !site.isChecked; siteSelectVm.radioChange(site, $event)"
                ng-dblClick="site.isChecked = !site.isChecked; siteSelectVm.selectRadioChange(site, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="site.isChecked" name="site" value="site"
                           ng-dblClick="siteSelectVm.selectRadioChange(site, $event)"
                           ng-click="siteSelectVm.radioChange(site, $event)">
                </td>
                <td style="width: 200px;">{{site.name}}</td>
                <td style="width: 200px;">{{site.description}}</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>