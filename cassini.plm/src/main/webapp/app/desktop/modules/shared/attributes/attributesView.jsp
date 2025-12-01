<style>
    #items-table thead th {
        position: sticky;
        top: 0px;
    }
</style>
<div style="padding: 20px; height: auto;">
    <style scoped>
        .ui-select-container .ui-select-match .btn {
            /*height: 33px !important;*/
        }

        .ui-select-container .ui-select-match .btn .btn {
            /*height: 20px !important;*/
        }
    </style>
    <div class="row">
        <div class="col-md-12" style="padding:0; height: auto;">
            <style scoped>
                table .ui-select-choices {
                    position: absolute !important;
                    top: auto !important;
                    left: auto !important;
                    width: 100% !important;
                    bottom: auto;
                }
            </style>
            <table id="items-table" class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 80px;text-align: center">
                        <input name="item" ng-show="customProperties.length > 1" type="checkbox"
                               ng-model="attributesVm.selectAllCheck"
                               ng-click="selectAll(check);" ng-checked="check">
                    </th>
                    <th translate>ATTRIBUTE_NAME</th>
                    <th translate>OBJECT_TYPE</th>
                    <th ng-if="selectionType == 'ROLLUP'">Unit of Measurement</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-show="customProperties.length == 0">
                    <td colspan="10" translate>NO_ATTRIBUTES</td>
                </tr>
                <tr ng-repeat="attribute in customProperties">
                    <td style="width: 80px; text-align: center">
                        <input name="attribute" type="checkbox" ng-model="attribute.checked"
                               ng-checked="attribute.checked" ng-click="selectCheck(attribute)">
                    </td>

                    <td style="vertical-align: middle;">
                        {{attribute.name}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{attribute.objectType}}
                    </td>
                    <td ng-if="selectionType == 'ROLLUP'">
                        <div class="">
                            <ui-select ng-show="attribute.measurement != null"
                                       ng-model="attribute.measurementUnit"
                                       theme="bootstrap" style="width:100%;">
                                <ui-select-match
                                        placeholder="Select unit">{{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="measurementUnit in attribute.measurement.measurementUnits | filter: $select.search">
                                    <div ng-bind-html="measurementUnit.name"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>
