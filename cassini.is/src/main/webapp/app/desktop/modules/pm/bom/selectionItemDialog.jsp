<style scoped>
    .left_div {
        width: 200px;
        border: 1px solid #ddd;
        float: left;
        padding: 1%;
        position: absolute;
        top: 5px;
        bottom: 5px;
        left: 5px;
        overflow: auto;
    }

    .right_div {
        border: 1px solid #ddd;
        float: left;
        padding: 1%;
        position: absolute;
        left: 210px;
        bottom: 5px;
        top: 5px;
        right: 5px;
    }

    td {
        padding: 5px !important;
    }

    .material-node {
        background: transparent url("app/assets/images/cart.png") no-repeat !important;
        height: 16px;
    }

    .machine-node {
        background: transparent url("app/assets/images/machine2.png") no-repeat !important;
        height: 16px;
    }

    a.disabled {
        cursor: not-allowed !important;
        color: lightgrey !important;
    }
</style>


<div style="position: relative;min-height: 350px">
    <div style="padding: 0px 10px">
        <form class="col-md-12 form-inline" style="border: 1px solid lightgrey;">
            <div class="col-md-6" style="padding: 8px;">
                <div class="input-group">
                    <div class="input-group-btn" uib-dropdown>
                        <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                            <span>Select Type</span> <span class="caret" style="margin-left: 4px;"></span>
                        </button>
                        <div class="dropdown-menu" role="menu">
                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 275px;width: 300px;">
                                <classification-tree
                                        on-select-type="selectionItemVm.onSelectType"></classification-tree>
                            </div>
                        </div>
                    </div>
                    <input type="text" class="form-control" name="title" style="width: 200px;"
                           ng-model="selectionItemVm.itemType.attributes.typeObject.name" readonly>
                </div>
            </div>
            <div class="col-md-6 pull-right" style="text-align: right">
                <%-- <div>
                         <span>
                             <medium>
                                 <span style="margin-right: 10px;">
                                         Displaying {{selectionItemVm.items.numberOfElements}} of
                                             {{selectionItemVm.items.totalElements}}
                                     </span>
                             </medium>
                         </span>
                     <span class="mr10">Page {{selectionItemVm.items.totalElements != 0 ? selectionItemVm.items.number+1:0}} of {{selectionItemVm.items.totalPages}}</span>
                     <a href="" ng-click="selectionItemVm.previousPage()"
                        ng-class="{'disabled': selectionItemVm.items.first}"><i
                             class="fa fa-arrow-circle-left mr10"></i></a>
                     <a href="" ng-click="selectionItemVm.nextPage()"
                        ng-class="{'disabled': selectionItemVm.items.last}"><i
                             class="fa fa-arrow-circle-right"></i></a>
                 </div>

 --%>
                <div>
                    <div>
                        <div>
                            <h5 style="margin-right: 10px;text-align: right">Displaying
                                {{selectionItemVm.items.numberOfElements}} of
                                {{selectionItemVm.items.numberOfElements}}</h5>
                        </div>

                        <div class="text-right">
                            <span class="mr10">Page {{selectionItemVm.items.totalElements != 0 ? selectionItemVm.items.number+1:0}} of {{selectionItemVm.items.totalPages}}</span>
                            <a href="" ng-class="{'disabled': selectionItemVm.items.first}"
                               ng-click="selectionItemVm.previousPage()">
                                <i class="fa fa-arrow-circle-left mr10"></i>
                            </a>
                            <a href="" ng-click="selectionItemVm.nextPage()"
                               ng-class="{'disabled': selectionItemVm.items.last}"><i
                                    class="fa fa-arrow-circle-right"></i></a>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <div class="responsive-table" style="padding: 10px;overflow-y: auto;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>
                        <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-model="selectionItemVm.selectedAll" ng-click="selectionItemVm.checkAll()"
                               ng-if="selectionItemVm.items.content.length > 1">
                    </th>
                    <th>ItemNumber</th>
                    <th>ItemName</th>
                    <th>ItemType</th>
                    <th style="width: 100px;">Units</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectionItemVm.itemType.attributes.typeObject.objectType == 'MATERIALTYPE' && selectionItemVm.items.content.length == 0">
                    <td colspan="10">No materials</td>
                </tr>
                <tr ng-if="selectionItemVm.itemType.attributes.typeObject.objectType == 'MACHINETYPE' && selectionItemVm.items.content.length == 0">
                    <td colspan="10">No machines</td>
                </tr>

                <tr ng-repeat="item in selectionItemVm.items.content">
                    <td>
                        <input id="item{{$index}}" name="itemSelected" ng-value="true" type="checkbox"
                               ng-click="selectionItemVm.selectCheck(item)"
                               ng-model="item.selected">
                    </td>
                    <td>{{item.itemNumber}}</td>
                    <td style="vertical-align: middle;width: 200px;max-width:250px !important;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;"
                        title="{{item.itemName}}">{{item.itemName}}
                    </td>
                    <td>{{item.itemType.name}}</td>
                    <td>{{item.units}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

