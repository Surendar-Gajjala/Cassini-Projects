<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>

<div style="padding: 0px 20px;">
    <h4 class="section-title" style="margin: 0px;">Filters</h4>

    <form class="form-inline" style="margin: 5px 0px;">

        <free-text-search style=" width: 300px; margin-right: 10px; position: absolute;top: 15px;right: 30px;"
                          on-clear="materialDialogueVm.resetPage"
                          on-search="materialDialogueVm.freeTextSearch"></free-text-search>


    </form>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4">
                <div style="padding: 10px;">
                    <span style="color:#1877f2e6">Selected Items</span>
                    <span class="badge">{{materialDialogueVm.selectedMaterials.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{materialDialogueVm.materials.numberOfElements}} of
                                            {{materialDialogueVm.materials.totalElements}}
                                    </span>
                            </medium>
                        </span>
                        <span class="mr10">Page {{materialDialogueVm.materials.totalElements != 0 ? materialDialogueVm.materials.number+1:0}} of {{materialDialogueVm.materials.totalPages}}</span>
                        <a href="" ng-click="materialDialogueVm.previousPage()"
                           ng-class="{'disabled': materialDialogueVm.materials.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="materialDialogueVm.nextPage()"
                           ng-class="{'disabled': materialDialogueVm.materials.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-12" style="padding:0px; height: auto;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: center;">
                        <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-model="materialDialogueVm.selectedAll"
                               ng-click="materialDialogueVm.checkAll()"
                        <%-- ng-if="materialDialogueVm.materials.content.length > 1"--%>>
                    </th>
                    <th style="vertical-align: middle;">Material Number</th>
                    <th style="vertical-align: middle;">Material Name</th>
                    <th style="vertical-align: middle;">Units</th>
                    <th style="vertical-align: middle;">Qty</th>
                    <th style="vertical-align: middle;">Group</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="materialDialogueVm.loading == true">
                    <td colspan="10">
                            <span style="font-size: 15px;">
                                <img src="app/assets/images/loaders/loader19.gif" class="mr5">Loading Materials..
                            </span>
                    </td>
                </tr>
                <tr ng-if="materialDialogueVm.materials.content.length == 0 && !materialDialogueVm.loading">
                    <td colspan="11" style="padding-left: 30px;">No Materials are available to view</td>
                </tr>
                <tr ng-repeat="material in materialDialogueVm.materials.content" ng-if="!materialDialogueVm.loading">
                    <th style="width: 50px; text-align: center">
                        <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-click="materialDialogueVm.select(material)"
                               ng-model="material.selected">
                    </th>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="material.itemNumber | highlightText: freeTextQuery"></span></td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="material.itemName | highlightText: freeTextQuery"></span></td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="material.units | highlightText: freeTextQuery"></span></td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="material.quantity"></span></td>
                    <td style="vertical-align: middle;"><span
                            ng-bind-html="material.boqName | highlightText: freeTextQuery"></span></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>





















