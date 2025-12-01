<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="issueSelectVm.resetPage"
                          on-search="issueSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{issueSelectVm.stockIssues.numberOfElements}} of
                                            {{issueSelectVm.stockIssues.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{issueSelectVm.stockIssues.totalElements != 0 ? issueSelectVm.stockIssues.number+1:0}} of {{issueSelectVm.stockIssues.totalPages}}</span>
                <a href="" ng-click="issueSelectVm.previousPage()"
                   ng-class="{'disabled': issueSelectVm.stockIssues.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="issueSelectVm.nextPage()"
                   ng-class="{'disabled': issueSelectVm.stockIssues.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th>Issue Number</th>
                <th>Project</th>
                <th>Store</th>
                <th>Notes</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="issueSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Stock Issues..</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="issueSelectVm.loading == false && issueSelectVm.stockIssues.content.length == 0">
                <td colspan="12">
                    <span translate>No Stock Issues are available to view</span>
                </td>
            </tr>

            <tr ng-repeat="stockIssue in issueSelectVm.stockIssues.content | filter: search"
                ng-click="stockIssue.isChecked = !stockIssue.isChecked; issueSelectVm.radioChange(stockIssue, $event)"
                ng-dblClick="stockIssue.isChecked = !stockIssue.isChecked; issueSelectVm.selectRadioChange(stockIssue, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="stockIssue.isChecked" name="stockIssue" value="stockIssue"
                           ng-dblClick="issueSelectVm.selectRadioChange(stockIssue, $event)"
                           ng-click="issueSelectVm.radioChange(stockIssue, $event)">
                </td>
                <td>{{stockIssue.issueNumberSource}}</td>
                <td title="{{stockIssue.projectObject.name}}">{{stockIssue.projectObject.name | limitTo:
                    20}}{{stockIssue.projectObject.name.length > 20 ? '...' : ''}}
                </td>
                <td>{{stockIssue.storeObject.storeName}}</td>
                <td title="{{stockIssue.notes}}">{{stockIssue.notes | limitTo: 20}}{{stockIssue.notes.length > 20 ?
                    '...' : ''}}
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>