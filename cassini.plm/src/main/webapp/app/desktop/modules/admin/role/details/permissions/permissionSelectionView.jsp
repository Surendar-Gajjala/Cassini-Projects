<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

</style>
<div style="padding: 0px 20px;">

    <style scoped>
        .role-permission-search-container {
            width: 100% !important;
        }

        .role-permission-search-container .search-fields {
            width: 10%;
            float: left;
            margin: 9px;
        }

        .role-permission-search-container .clear-search {
            margin-top: 11px !important;
        }

        #rightSidePanelContent {
            overflow: hidden !important;
        }

        .permission-selection-table table th {
            position: -webkit-sticky;
            position: sticky;
            top: -1px !important;
            z-index: 5 !important;
        }

        .permission-table-container {
            position: absolute;
            top: 147px !important;
            bottom: 50px !important;
            left: 0;
            right: 0;
        }
    </style>
    <div class="role-permission-search-container">
        <div class="search-fields">
            <input type="text" ng-change="permissionSelectionVm.searchFilterItem()"
                   ng-model="permissionSelectionVm.filters.name"
                   placeholder="Name"
                   class="form-control"/>
        </div>
        <div class="search-fields"><input type="text" ng-model="permissionSelectionVm.filters.objectType"
                                          placeholder="ObjectType" ng-change="permissionSelectionVm.searchFilterItem()"
                                          class="form-control"/></div>

        <div class="search-fields"><input type="text" ng-model="permissionSelectionVm.filters.subType"
                                          placeholder="SubType" ng-change="permissionSelectionVm.searchFilterItem()"
                                          class="form-control"/></div>
        <div class="search-fields"><input type="text" ng-model="permissionSelectionVm.filters.attributeGroup"
                                          style="width: 118px !important;margin: 0 0 0 -7px !important;"
                                          placeholder="Attribute Group"
                                          ng-change="permissionSelectionVm.searchFilterItem()"
                                          class="form-control"/></div>
        <div class="search-fields"><input type="text" ng-model="permissionSelectionVm.filters.attribute"
                                          placeholder="Attribute" ng-change="permissionSelectionVm.searchFilterItem()"
                                          class="form-control"/></div>
        <div class="search-fields"><input type="text" ng-model="permissionSelectionVm.filters.privilege"
                                          placeholder="Privilege" ng-change="permissionSelectionVm.searchFilterItem()"
                                          class="form-control"/></div>
        <div class="search-fields">
            <ui-select ng-model="permissionSelectionVm.filters.module"
                       on-select="permissionSelectionVm.searchFilterItem()">
                <ui-select-match placeholder="{{newCriteriaVm.selectTitle}}">{{$select.selected}}
                </ui-select-match>
                <ui-select-choices repeat="type in moduleTypes | filter: $select.search">
                    <div ng-bind="type"></div>
                </ui-select-choices>
            </ui-select>
        </div>
        <div class="search-fields">
            <ui-select ng-model="permissionSelectionVm.filters.privilegeType"
                       on-select="permissionSelectionVm.searchFilterItem()">
                <ui-select-match placeholder="{{newCriteriaVm.selectTitle}}">{{$select.selected}}
                </ui-select-match>
                <ui-select-choices repeat="type in ['GRANTED','DENIED'] | filter: $select.search">
                    <div ng-bind="type"></div>
                </ui-select-choices>
            </ui-select>
        </div>
        <div class="pull-right clear-search">
            <button class="btn btn-danger btn-sm" ng-if="permissionSelectionVm.clear == true"
                    ng-click="permissionSelectionVm.clearFilter()" translate>CLEAR
            </button>
        </div>
    </div>

    <hr style="margin: 0px;">
    <div class="row">

        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="pull-right text-center" style="padding: 10px;">
                <div>
                    <span>
                        <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="permissionSelectionVm.items.numberOfElements ==0">
                            {{(permissionSelectionVm.pageable.page*permissionSelectionVm.pageable.size)}}
                        </span>

                                    <span ng-if="permissionSelectionVm.items.numberOfElements > 0">
                            {{(permissionSelectionVm.pageable.page*permissionSelectionVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="permissionSelectionVm.items.last ==false">{{((permissionSelectionVm.pageable.page+1)*permissionSelectionVm.pageable.size)}}</span>
                                    <span ng-if="permissionSelectionVm.items.last == true">{{permissionSelectionVm.items.totalElements}}</span>

                                 <span translate> OF </span>
                                {{permissionSelectionVm.items.totalElements}}<span
                                        translate>AN</span>
                                </span>
                        </medium>
                        
                        
                    </span>
                     <span class="mr10"> Page {{permissionSelectionVm.items.totalElements != 0 ?
                     permissionSelectionVm.items.number+1:0}} <span translate>OF</span> {{permissionSelectionVm.items.totalPages}}
                    </span>
                    <a href="" ng-click="permissionSelectionVm.previousPage()"
                       ng-class="{'disabled': permissionSelectionVm.items.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="permissionSelectionVm.nextPage()"
                       ng-class="{'disabled': permissionSelectionVm.items.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>

        <div class="col-md-12 permission-table-container" style="padding:0px; height: auto;overflow: auto;">
            <div class="permission-selection-table">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th style="text-align: center;width: 20px !important;z-index: 99 !important;">
                            <input ng-if="permissionSelectionVm.items.content.length != 0 "
                                   name="item"
                                   type="checkbox" ng-model="permissionSelectionVm.selectAllCheck"
                                   ng-click="permissionSelectionVm.selectAll(check);" ng-checked="check">
                        </th>
                        <th class="permission-col-width-200" translate>NAME</th>
                        <th translate>OBJECT_TYPE</th>
                        <th translate>SUB_TYPE</th>
                        <th translate>ATTRIBUTE_GROUP</th>
                        <th class="permission-col-width-200" translate>ATTRIBUTE</th>
                        <th translate>CRITERIA</th>
                        <th translate>PRIVILEGE</th>
                        <th translate>PRIVILEGE_TYPE</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-if="permissionSelectionVm.items.content.length == 0">
                        <td colspan="15" translate>NO_ITEMS</td>
                    </tr>
                    <tr ng-if="permissionSelectionVm.items.content.length > 0"
                        ng-repeat="item in permissionSelectionVm.items.content">
                        <th style="vertical-align: middle;text-align: center;width: 20px !important;">
                            <input type="checkbox" ng-model="item.selected" name="item" value="item"
                                   ng-click="permissionSelectionVm.selectCheck(item)"/>
                        </th>
                        <td class="permission-col-width-200">
                            <span ng-bind-html="item.name"></span></td>
                        <td style="vertical-align: middle;">{{item.objectType}}</td>
                        <td style="vertical-align: middle;">{{item.subType}}</td>
                        <td style="vertical-align: middle;">{{item.attributeGroup}}</td>
                        <td class="permission-col-width-200" style="vertical-align: middle;">{{item.attribute}}</td>
                        <td style="vertical-align: middle;">
                             <span style="padding: 20px;cursor: pointer;" ng-if="item.criteria != null">
                                        <i class="fa fa-pencil"
                                           ng-click="viewCriteria(item)"></i>
                             </span>
                        </td>
                        <td style="vertical-align: middle;">{{item.privilege}}</td>
                        <td style="vertical-align: middle;">
                            <privilege-type type="item.privilegeType"></privilege-type>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <br>
    <br>
</div>
