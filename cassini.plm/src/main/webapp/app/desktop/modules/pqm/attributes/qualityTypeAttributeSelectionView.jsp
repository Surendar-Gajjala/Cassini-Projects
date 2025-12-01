<style>
    #items-table thead th {
        position: sticky;
        top: 0px;
    }
</style>
<div style="padding: 10px; height: auto;">
    <div class="row">
        <div class="col-md-12" >
            <table id="items-table" class="table table-striped">
                <thead>
                <tr>
                    <th style="width: 50px; text-align: center">
                        <input name="item" type="checkbox" ng-model="qualityTypeAttributeSelectionVm.selectAllCheck"
                               ng-click="qualityTypeAttributeSelectionVm.selectAll(check);" ng-checked="check">
                    </th>
                    <th translate>ATTRIBUTE_NAME</th>
                    <th translate>OBJECT_TYPE</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="attribute in qualityTypeAttributeSelectionVm.customItemAttributes">
                    <th style="width: 80px; text-align: center">
                        <input name="attribute" type="checkbox" ng-model="attribute.checked"
                               ng-checked="attribute.checked"
                               ng-click="qualityTypeAttributeSelectionVm.selectCheck(attribute)">
                    </th>

                    <td style="vertical-align: middle;">
                        {{attribute.name}}
                    </td>
                    <td style="vertical-align: middle;">
                        <span ng-if="attribute.objectType == parentObjectType">{{attribute.objectType}}</span>
                        <span ng-if="attribute.objectType == objectType || attribute.objectType == 'QUALITY_TYPE' || attribute.objectType == 'CHANGETYPE'">
                        {{objectType}}</span>
                        <span ng-if="attribute.objectType == object && objectType != object">{{object}}</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>
