<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="problemSelectVm.resetPage"
                          on-search="problemSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{problemSelectVm.problems.numberOfElements}} of
                                            {{problemSelectVm.problems.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{problemSelectVm.problems.totalElements != 0 ? problemSelectVm.problems.number+1:0}} of {{problemSelectVm.problems.totalPages}}</span>
                <a href="" ng-click="problemSelectVm.previousPage()"
                   ng-class="{'disabled': problemSelectVm.problems.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="problemSelectVm.nextPage()"
                   ng-class="{'disabled': problemSelectVm.problems.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px">Select</th>
                <th>Title</th>
                <th>Description</th>
                <th>Assigned To</th>
                <th>Status</th>
                <th>Task</th>
                <th>Priority</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="problemSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Problems</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="problemSelectVm.loading == false && problemSelectVm.problems.content.length == 0">
                <td colspan="12">
                    <span translate>No Problems</span>
                </td>
            </tr>

            <tr ng-repeat="issue in problemSelectVm.problems.content | filter: search"
                ng-click="issue.isChecked = !issue.isChecked; problemSelectVm.radioChange(issue, $event)"
                ng-dblClick="issue.isChecked = !issue.isChecked; problemSelectVm.selectRadioChange(issue, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="issue.isChecked" name="issue" value="issue"
                           ng-dblClick="problemSelectVm.selectRadioChange(issue, $event)"
                           ng-click="problemSelectVm.radioChange(issue, $event)">
                </td>
                <td>{{issue.title}}</td>
                <td><span
                        ng-bind-html="issue.description.length > 50 ? issue.description.trunc(50,true) : issue.description | highlightText: freeTextQuery"></span>
                </td>
                <td>{{issue.assignedToObject.fullName}}</td>
                <td>
                        <span class="label" style="color: white; font-size: 12px;" ng-class="{
                                    'label-success': issue.status == 'NEW',
                                    'label-info': issue.status == 'ASSIGNED',
                                    'label-warning': issue.status == 'INPROGRESS',
                                    'label-danger': issue.status == 'CLOSED'}">
                            {{issue.status}}
                        </span>
                </td>
                <td>{{issue.taskObject.name}}</td>
                <td>
              <span class="label" style="color: white; font-size: 12px;" ng-class="{
                                    'label-info': issue.priority == 'LOW',
                                    'label-warning': issue.priority == 'MEDIUM',
                                    'label-danger': issue.priority == 'HIGH'}">
                            {{issue.priority}}
                        </span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>