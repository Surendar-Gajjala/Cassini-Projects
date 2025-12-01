<div class="view-container" fitcontent>
    <div class="view-toolbar">
        <div class="btn-group">
            <button class="btn btn-sm btn-default min-width" ng-click="materialEditVm.back()">Back</button>
            <button class="btn btn-sm btn-success min-width" ng-click="materialEditVm.saveMaterialItem()">Save</button>
        </div>
    </div>

    <div class="view-content no-padding" style="overflow-y: auto; overflow-x: hidden">
        <div class="row">
            <h4 class="section-title" style="margin-left: 170px;">Basic Info</h4>
            <br>

            <div class="row row-eq-height" style="margin: 0;">
                <div class="item-details col-sm-10 col-sm-offset-1" style="padding: 30px;">
                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Material Name: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <a href="" editable-text="materialEditVm.materialItem.itemName"
                               onaftersave="materialEditVm.update()">
                                {{materialEditVm.materialItem.itemName}}</a>
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Material Number: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <span>{{materialEditVm.materialItem.itemNumber}}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Material Type: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <span>{{materialEditVm.materialItem.itemType.name}}</span>
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Description: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">
                            <a href="" editable-text="materialEditVm.materialItem.description"
                               onaftersave="materialEditVm.update()">
                                {{materialEditVm.materialItem.description || 'Click to enter description'}}</a>
                        </div>
                    </div>

                    <div class="row">
                        <div class="label col-xs-4 col-sm-3 text-right">
                            <span>Units: </span>
                        </div>
                        <div class="value col-xs-8 col-sm-9">

                            <a href="" editable-text="materialEditVm.materialItem.units"
                               onaftersave="materialEditVm.update()">
                                {{materialEditVm.materialItem.units}}</a>
                        </div>
                    </div>
                </div>
            </div>

            <br>
            <h4 class="section-title" style="margin-left:170px;">Attributes</h4>
            <br>

            <div class="row">
                <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th style="width: 80px; text-align: center"></th>
                            <th>Attribute Name</th>
                            <th>Object Type</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td colspan="11"
                                ng-if="materialEditVm.materialAttributes.length == 0 && materialEditVm.materialTypeAttributes.length == 0">
                                No Attributes
                            </td>
                        </tr>
                        <tr ng-repeat="attribute in materialEditVm.materialAttributes">
                            <th style="width: 80px; text-align: center">
                                <input name="attribute" type="checkbox" ng-model="attribute.checked"
                                       ng-click="materialEditVm.selectCheck(attribute)">
                            </th>

                            <td style="vertical-align: middle;">
                                {{attribute.name}}
                            </td>
                            <td style="vertical-align: middle;">
                                {{attribute.objectType}}
                            </td>
                        </tr>
                        <tr ng-repeat="attribute in materialEditVm.materialTypeAttributes">
                            <th style="width: 80px; text-align: center">
                                <input name="attribute" type="checkbox" ng-model="attribute.checked"
                                       ng-click="materialEditVm.selectCheck(attribute)">
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
            <br><br>
        </div>
    </div>
</div>