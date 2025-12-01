<style scoped>

    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div class="view-container" fitcontent>
    <div class="view-toolbar selector" style="top: 30px; background-color: white">
        <free-text-search on-clear="customIndentSelectVm.resetPage"
                          on-search="customIndentSelectVm.freeTextSearch"></free-text-search>
        <div class="pull-right text-center" style="padding: 10px;margin-top: -5px;">
            <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{customIndentSelectVm.customIndents.numberOfElements}} of
                                            {{customIndentSelectVm.customIndents.numberOfElements}}
                                    </span>
                            </medium>
                        </span>
                <span class="mr10">Page {{customIndentSelectVm.customIndents.totalElements != 0 ? customIndentSelectVm.customIndents.number+1:0}} of {{customIndentSelectVm.customIndents.totalPages}}</span>
                <a href="" ng-click="customIndentSelectVm.previousPage()"
                   ng-class="{'disabled': customIndentSelectVm.customIndents.first}"><i
                        class="fa fa-arrow-circle-left mr10"></i></a>
                <a href="" ng-click="customIndentSelectVm.nextPage()"
                   ng-class="{'disabled': customIndentSelectVm.customIndents.last}"><i
                        class="fa fa-arrow-circle-right"></i></a>
            </div>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto;padding: 10px !important;">

        <table class="table table-striped highlight-row">
            <thead>
            <tr>
                <th style="width: 40px;">Select</th>
                <th>Indent Number</th>
                <th>Project</th>
                <th>Store</th>
                <th>Status</th>
                <th>Notes</th>
            </tr>
            </thead>

            <tbody>
            <tr ng-if="customIndentSelectVm.loading == true">
                <td colspan="12">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>Loading Custom Indents...</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="customIndentSelectVm.loading == false && customIndentSelectVm.customIndents.content.length == 0">
                <td colspan="12">
                    <span translate>No Custom Indents</span>
                </td>
            </tr>

            <tr ng-repeat="customIndent in customIndentSelectVm.customIndents.content | filter: search"
                ng-click="customIndent.isChecked = !customIndent.isChecked; customIndentSelectVm.radioChange(customIndent, $event)"
                ng-dblClick="customIndent.isChecked = !customIndent.isChecked; customIndentSelectVm.selectRadioChange(customIndent, $event)">
                <td style="width: 40px;">
                    <input type="radio" ng-checked="customIndent.isChecked" name="customIndent" value="customIndent"
                           ng-dblClick="customIndentSelectVm.selectRadioChange(customIndent, $event)"
                           ng-click="customIndentSelectVm.radioChange(customIndent, $event)">
                </td>
                <td>{{customIndent.indentNumber}}</td>
                <td>{{customIndent.project.name}}</td>
                <td>{{customIndent.storeObject.storeName}}</td>
                <td>
          <span style="color: white;font-size: 12px;" class="label" ng-class=" {
                                    'label-success': customIndent.status == 'NEW',
                                    'label-info': customIndent.status == 'APPROVED'}">
                            {{customIndent.status}}
                        </span>
                </td>
                <td>
                    <p title="{{customIndent.notes}}">{{customIndent.notes | limitTo: 10}}{{customIndent.notes.length >
                        10 ? '...' : ''}}</p>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>