<div style="padding: 20px; height: auto;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center">
                        <input name="item" type="checkbox" ng-model="glossayTypeAttributesVm.selectAllCheck"
                               ng-click="glossayTypeAttributesVm.selectAll(check);" ng-checked="check">
                    </th>
                    <th translate>ATTRIBUTE_NAME</th>
                    <th translate>OBJECT_TYPE</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="attribute in glossayTypeAttributesVm.customItemAttributes">
                    <th style="width: 80px; text-align: center">
                        <input name="attribute" type="checkbox" ng-model="attribute.checked"
                               ng-disabled="attribute.checked" ng-click="glossayTypeAttributesVm.selectCheck(attribute)">
                    </th>

                    <td style="vertical-align: middle;">
                        {{attribute.name}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{attribute.objectType}}
                    </td>
                </tr>

                <tr ng-repeat="attribute in glossayTypeAttributesVm.typeAttributes">
                    <th style="width: 80px; text-align: center">
                        <input name="attribute" type="checkbox" ng-model="attribute.checked"
                               ng-click="glossayTypeAttributesVm.selectCheck(attribute)">
                    </th>

                    <td style="vertical-align: middle;">
                        {{attribute.name}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{attribute.objectType}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>
