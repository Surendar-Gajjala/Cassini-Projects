<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }
</style>
<div style="padding: 0px 20px;">
    <h4 class="section-title" style="margin: 0px;" translate>FILTERS</h4>

    <form class="form-inline" style="margin: 5px 0px;">
        <div class="form-group">
            <div class="input-group">

                <div class="input-group-btn" uib-dropdown>
                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                        <span translate>SELECT</span>
                        <span class="caret" style="margin-left: 4px;"></span>
                    </button>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <classification-tree
                                    on-select-type="itemSelectionVm.onSelectType"></classification-tree>
                        </div>
                    </div>
                </div>
                <input type="text" class="form-control" name="title" style="width: 125px;"
                       ng-model="itemSelectionVm.selectedType.name" readonly>

            </div>
        </div>
        <div class="form-group">
            <input type="text" ng-change="itemSelectionVm.searchFilterItem()"
                   ng-model="itemSelectionVm.itemsFilters.itemNumber" style="margin-left: -16px;width: 140px;"
                   placeholder="{{'ITEM_NUMBER' | translate}}"
                   class="form-control"/>
        </div>
        <div class="form-group">
            <input type="text" ng-model="itemSelectionVm.itemsFilters.itemName" style="margin-left: -16px;width: 140px;"
                   placeholder="{{'ITEM_NAME' | translate}}" ng-change="itemSelectionVm.searchFilterItem()"
                   class="form-control"/>
        </div>
        <div class="pull-right" style="margin-top: 5px;">
            <%-- <button class="btn btn-success btn-sm"
                     ng-click="itemSelectionVm.searchFilterItem()" translate>SEARCH
             </button>--%>
            <button class="btn btn-danger btn-sm" ng-if="itemSelectionVm.clear == true"
                    ng-click="itemSelectionVm.clearFilter()" translate>CLEAR
            </button>
        </div>

    </form>
    <hr style="margin: 0px;">
    <div class="row">

        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="pull-right text-center" style="padding: 10px;">
                <div>
                    <span>
                        <medium>
                            <span style="margin-right: 10px;">
                                <span translate>DISPLAYING</span>
                                 {{itemSelectionVm.items.numberOfElements}} <span
                                    translate> OF </span>
                                            {{itemSelectionVm.items.totalElements}}
                            </span>
                        </medium>
                    </span>
                     <span class="mr10"> Page {{itemSelectionVm.items.totalElements != 0 ?
                     itemSelectionVm.items.number+1:0}} <span translate>OF</span> {{itemSelectionVm.items.totalPages}}
                    </span>
                    <a href="" ng-click="itemSelectionVm.previousPage()"
                       ng-class="{'disabled': itemSelectionVm.items.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="itemSelectionVm.nextPage()"
                       ng-class="{'disabled': itemSelectionVm.items.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>

        <div class="col-md-12" style="padding:0px;overflow: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th></th>
                    <th translate>ITEM_NUMBER</th>
                    <th translate>ITEM_NAME</th>
                    <th translate>ITEM_TYPE</th>
                    <th translate>DESCRIPTION</th>
                    <th translate style="text-align: center">REVISION</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="itemSelectionVm.items.content.length == 0">
                    <td colspan="15" translate>NO_ITEMS</td>
                </tr>
                <tr ng-if="itemSelectionVm.items.content.length > 0"
                    ng-repeat="item in itemSelectionVm.items.content">
                    <th style="vertical-align: middle;text-align: left">
                        <input type="radio" ng-model="item.checked" name="item" value="item"
                               ng-click="itemSelectionVm.radioChange(item)"/>
                    </th>

                    <td style="vertical-align: middle;">
                        <a ng-if="item.configurable == true" title="{{itemsVm.configurableItem}}" class="fa fa-cog"
                           aria-hidden="true"></a>
                        {{item.itemNumber}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{item.itemName}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{item.itemType.name}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{item.description}}
                    </td>
                    <td style="vertical-align: middle;">
                        {{item.latestRevisionObject.revision}}
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <br>
    <br>
</div>

<%--<div id="appSidePanelButtonsPanel" class='buttons-panel' style="display: none">
    <button ng-if="itemSelectionVm.item != null" class="btn btn-sm btn-success pull-right"
            ng-click="itemSelectionVm.onOk()">Add
    </button>
</div>--%>
</div>