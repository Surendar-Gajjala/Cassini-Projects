<div style="padding: 0 10px;">
    <style scoped>
        #td {
            word-wrap: break-word;
            width: 200px;
            white-space: normal;
            text-align: left;
        }

        a.disabled {
            cursor: not-allowed;
            color: lightgrey;
        }
        .open > .dropdown-toggle.btn {
            color: #444442 !important;
        }
    </style>
    <h4 class="section-title" style="margin: 0px;" translate>FILTERS</h4>

    <form class="form-inline" style="margin: 5px 0px">
        <div class="form-group">
            <div class="input-group">
                <div class="input-group-btn" uib-dropdown>
                    <div class="dropdown-menu" role="menu">
                        <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                            <classification-tree
                                    on-select-type="selectItemReferenceVm.onSelectType"></classification-tree>
                        </div>
                    </div>
                    <button uib-dropdown-toggle class="btn btn-default dropdown-toggle" type="button">
                        <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                    </button>
                </div>
                <input type="text" class="form-control" name="title"
                       style="width: 140px !important;height: 41px !important;"
                       ng-model="selectItemReferenceVm.selectedType.name" readonly>

            </div>
        </div>


        <div class="form-group">
            <input type="text" ng-model="selectItemReferenceVm.filters.itemNumber" style="width: 165px;"
                   ng-change="selectItemReferenceVm.searchItems()"
                   placeholder="{{'ITEM_NUMBER' | translate}}"
                   class="form-control">
        </div>
        <div class="form-group">
            <input type="text" ng-model="selectItemReferenceVm.filters.itemName"
                   ng-change="selectItemReferenceVm.searchItems()"
                   style="width: 165px;"
                   placeholder="{{'ITEM_NAME' | translate}}"
                   class="form-control">
        </div>

        <div class="pull-right" style="margin-top: 5px;">
            <button class="btn btn-sm btn-danger" ng-if="selectItemReferenceVm.clear == true"
                    ng-click="selectItemReferenceVm.clearFilter()" translate>CLEAR
            </button>
        </div>

    </form>
    <hr style="margin: 0px;">
    <div class="row">

        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-4">
                <div style="padding: 10px;">
                    <span style="color:#1877f2e6" translate>SELECTED_ITEMS</span>
                    <span class="badge">{{selectItemReferenceVm.selectedItems.length}}</span>
                </div>
            </div>
            <div class="col-md-8">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectItemReferenceVm.items.numberOfElements ==0">
                            {{(selectItemReferenceVm.pageable.page*selectItemReferenceVm.pageable.size)}}
                        </span>

                                    <span ng-if="selectItemReferenceVm.items.numberOfElements > 0">
                            {{(selectItemReferenceVm.pageable.page*selectItemReferenceVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="selectItemReferenceVm.items.last ==false">{{((selectItemReferenceVm.pageable.page+1)*selectItemReferenceVm.pageable.size)}}</span>
                                    <span ng-if="selectItemReferenceVm.items.last == true">{{selectItemReferenceVm.items.totalElements}}</span>

                                 <span translate> OF </span>
                                {{selectItemReferenceVm.items.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10"> Page  {{selectItemReferenceVm.items.totalElements != 0 ? selectItemReferenceVm.items.number+1:0}} <span
                                translate> OF </span> {{selectItemReferenceVm.items.totalPages}} </span>
                        <a href="" ng-click="selectItemReferenceVm.previousPage()"
                           ng-class="{'disabled': selectItemReferenceVm.items.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="selectItemReferenceVm.nextPage()"
                           ng-class="{'disabled': selectItemReferenceVm.items.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-12" style="padding:0px;">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th style="text-align: left">
                        <input name="itemSelected" ng-value="true" type="checkbox"
                               ng-model="selectItemReferenceVm.selectAllCheck"
                               ng-if="selectItemReferenceVm.items.content.length != 0"
                               ng-click="selectItemReferenceVm.selectAll()">
                    </th>
                    <th class="col-width-100"  translate>ITEM_NUMBER</th>
                    <th class="col-width-150" translate>ITEM_NAME</th>
                    <th  translate>ITEM_TYPE</th>
                    <th class="col-width-250" translate>DESCRIPTION</th>
                    <th style="width: 70px;text-align: center" translate>REVISION</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectItemReferenceVm.items.content.length == 0">
                    <td colspan="15" translate>NO_ITEMS</td>
                </tr>
                <tr ng-if="selectItemReferenceVm.items.content.length > 0"
                    ng-repeat="item in selectItemReferenceVm.items.content">
                    <th style="vertical-align: top; text-align: left">
                        <input name="item" type="checkbox" ng-model="item.selected"
                               ng-click="selectItemReferenceVm.selectCheck(item)">
                    </th>

                    <td class="col-width-100">
                        <a ng-if="item.configurable == true" title="{{configurableItem}}" class="fa fa-cog"
                           aria-hidden="true"></a>
                        <span ng-bind-html="item.itemNumber | highlightText: selectItemReferenceVm.filters.itemNumber"></span>
                    </td>
                    <td class="col-width-150">
                        <span ng-bind-html="item.itemName | highlightText: selectItemReferenceVm.filters.itemName"></span>
                    </td>
                    <td >
                        <span ng-bind-html="item.itemType.name | highlightText: selectItemReferenceVm.filters.typeName"></span>
                    </td>
                    <td class="col-width-250" id="td" title="{{item.description}}"><span
                            ng-bind-html="item.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="vertical-align: top;width: 70px;text-align: center">
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
