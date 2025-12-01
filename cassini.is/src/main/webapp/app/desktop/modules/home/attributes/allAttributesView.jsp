<div style="padding: 20px; height: auto;">
    <div class="row">
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="vertical-align: middle;text-align: left;width: 10%">
                        <div class="ckbox ckbox-default" style="display: inline-block;"
                             ng-if="allAttributesVm.allAttributes.length > 1">
                            <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                                   ng-model="allAttributesVm.selectedAll"
                                   ng-click="allAttributesVm.selectAll()">
                            <label for="item{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th style="width: 45%;">Attribute Name</th>
                    <th style="width: 45%;">Object Type</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td colspan="11"
                        ng-if="allAttributesVm.allAttributes.length == 0 && allAttributesVm.allTypeAttributes.length == 0">
                        No Attributes
                    </td>
                </tr>
                <tr ng-repeat="attribute in allAttributesVm.allAttributes">

                    <th style="text-align: left;vertical-align: middle;width: 10%">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="item{{$index}}" name="attribute" ng-value="true" type="checkbox"
                                   ng-click="allAttributesVm.selectCheck(attribute)"
                                   ng-model="attribute.checked">
                            <label for="item{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>


                    <td style="vertical-align: middle;">
                        {{attribute.name}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{attribute.objectType}}<span ng-if="attribute.itemType != undefined"> ( {{attribute.itemTypeObject.name}} )</span>
                    </td>
                </tr>
                <tr ng-repeat="attribute in allAttributesVm.allTypeAttributes">


                    <th style="width: 80px; text-align: left">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="item{{$index}}" name="attribute" ng-value="true" type="checkbox"
                                   ng-click="allAttributesVm.selectCheck(attribute)"
                                   ng-model="attribute.checked">
                            <label for="item{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <td style="vertical-align: middle;">
                        {{attribute.name}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{attribute.itemTypeObject.name}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>
