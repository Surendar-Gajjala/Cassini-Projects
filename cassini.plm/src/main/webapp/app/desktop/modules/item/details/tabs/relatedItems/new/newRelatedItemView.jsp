<div>
    <style scoped>
        table td {
            padding-top: 0px !important;
            padding-bottom: 0px !important;
        }

        #rightSidePanelContent {
            overflow: hidden !important;
        }

        .related-item-selection-table table th {
            position: -webkit-sticky;
            position: sticky;
            top: -1px !important;
            z-index: 5 !important;
        }

        .table-div {
            position: absolute;
            top: 142px;
            bottom: 50px !important;
            left: 0;
            right: 0;
        }

        .selectColumn {
            width: 80px;
            text-align: center;
            position: sticky;
            left: -10px;
        }


    </style>
    <div style="padding: 10px;">
        <div class="row" style="margin: 0;">
            <div class="col-md-12" style="padding-left: 0;">
                <form class="form-inline ng-pristine ng-valid" style="display: flex;flex-direction: row;">
                    <div class="form-group" style="margin-right: 5px;text-align: center;">
                        <input type="text" ng-model="newRelatedItemVm.filters.itemNumber"
                               ng-change="newRelatedItemVm.searchFilterItem()"
                               ng-enter="newRelatedItemVm.searchFilterItem()"
                               ng-disabled="newRelatedItemVm.selectedRelationship == null"
                               ng-model-options="{ debounce: 500 }"
                               style="width: 200px;height: 30px;margin-top: 0;"
                               title="{{newRelatedItemVm.selectedRelationship == null ? 'Select Relationship':''}}"
                               placeholder="{{'ITEM_NUMBER_TITLE' | translate}}"
                               class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
                    </div>
                    <div class="form-group" style="margin-right: 5px;text-align: center;">
                        <input type="text" ng-model="newRelatedItemVm.filters.itemName"
                               ng-change="newRelatedItemVm.searchFilterItem()"
                               ng-enter="newRelatedItemVm.searchFilterItem()"
                               ng-disabled="newRelatedItemVm.selectedRelationship == null"
                               title="{{newRelatedItemVm.selectedRelationship == null ? 'Select Relationship':''}}"
                               ng-model-options="{ debounce: 500 }"
                               style="height: 30px;width: 200px;margin-top: 0;"
                               placeholder="{{'ITEM_NAME_PLACEHOLDER_TITLE' | translate}}"
                               class="input-sm form-control ng-pristine ng-valid ng-touched">
                    </div>
                    <div class="form-group" style="margin-right: 5px;text-align: center;">
                        <input type="text" ng-model="newRelatedItemVm.filters.description"
                               ng-change="newRelatedItemVm.searchFilterItem()"
                               ng-enter="newRelatedItemVm.searchFilterItem()"
                               ng-disabled="newRelatedItemVm.selectedRelationship == null"
                               ng-model-options="{ debounce: 500 }"
                               style="height: 30px;width: 200px;margin-top: 0;"
                               title="{{newRelatedItemVm.selectedRelationship == null ? 'Select Relationship':''}}"
                               placeholder="{{'ENTER_DESCRIPTION_TITLE' | translate}}"
                               class="input-sm form-control ng-pristine ng-valid ng-touched">
                    </div>
                    <div style="margin-top: 0;flex-grow: 1;" class="">
                        <button ng-click="newRelatedItemVm.clearFilter()" translate="" style="height: 29px;width: 100%"
                                class="btn btn-xs btn-danger ng-scope" ng-if="newRelatedItemVm.clear">Clear
                        </button>
                    </div>
                </form>
            </div>
            <div class="col-md-12" style="margin-top: 10px;padding: 0;">
                <div class="col-md-5" style="padding: 2px;">
                    <div class="form-group" style="margin: 0;">
                        <div class="col-sm-12" style="padding-left: 0;">
                            <ui-select ng-model="newRelatedItemVm.relatedItem.relationship" theme="bootstrap"
                                       on-select="newRelatedItemVm.pageable.page = 0;newRelatedItemVm.onSelectRelationship($item)"
                                       style="width:100%">
                                <ui-select-match placeholder="{{newRelatedItemVm.SelectRelation}}">
                                    {{$select.selected.name}}
                                </ui-select-match>
                                <ui-select-choices
                                        repeat="relationship in newRelatedItemVm.relationships | filter: $select.search">
                                    <div ng-bind="relationship.name | highlight: $select.search"></div>
                                </ui-select-choices>
                            </ui-select>
                        </div>
                    </div>
                </div>
                <div class="col-md-7" style="padding: 0 10px;">
                    <div class="pull-right text-center" style="padding: 10px 0;">
                        <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="newRelatedItemVm.toTypeItems.numberOfElements ==0">
                            {{(newRelatedItemVm.pageable.page*newRelatedItemVm.pageable.size)}}
                        </span>

                                    <span ng-if="newRelatedItemVm.toTypeItems.numberOfElements > 0">
                            {{(newRelatedItemVm.pageable.page*newRelatedItemVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="newRelatedItemVm.toTypeItems.last ==false">{{((newRelatedItemVm.pageable.page+1)*newRelatedItemVm.pageable.size)}}</span>
                                    <span ng-if="newRelatedItemVm.toTypeItems.last == true">{{newRelatedItemVm.toTypeItems.totalElements}}</span>

                                 <span translate> OF </span>
                                {{newRelatedItemVm.toTypeItems.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10">  Page {{newRelatedItemVm.toTypeItems.totalElements != 0 ?
                        newRelatedItemVm.toTypeItems.number+1:0}} <span translate>OF</span> {{newRelatedItemVm.toTypeItems.totalPages}}</span>
                            <a href="" ng-click="newRelatedItemVm.previousPage()"
                               ng-class="{'disabled': newRelatedItemVm.toTypeItems.first}"><i
                                    class="fa fa-arrow-circle-left mr10"></i></a>
                            <a href="" ng-click="newRelatedItemVm.nextPage()"
                               ng-class="{'disabled': newRelatedItemVm.toTypeItems.last}"><i
                                    class="fa fa-arrow-circle-right"></i></a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-12 table-div"
                 style="padding:0 10px;height: auto;overflow: auto;border-top: 1px solid #ddd">
                <div class="related-item-selection-table">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th class="selectColumn" style="z-index: 6 !important;" translate>SELECT

                            </th>
                            <th style="vertical-align: middle;" translate="RELATIONSHIP">
                            </th>
                            <th style="vertical-align: middle;" translate="ITEM_NUMBER"></th>
                            <th style="vertical-align: middle;" translate="ITEM_TYPE">
                            </th>
                            <th style="vertical-align: middle;" translate="ITEM_NAME">

                            </th>
                            <th style="vertical-align: middle;" translate="DESCRIPTION">

                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-if="newRelatedItemVm.loading == true">
                            <td colspan="7">
                                        <span style="font-size: 15px;">
                                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                                 class="mr5">Loading toTypeItems...
                                        </span>
                            </td>
                        </tr>
                        <tr ng-if="newRelatedItemVm.toTypeItems.content.length == 0">
                            <td colspan="7" translate>NO_ITEMS</td>
                        </tr>
                        <tr ng-if="newRelatedItemVm.toTypeItems.content.length > 0"
                            ng-repeat="item in newRelatedItemVm.toTypeItems.content">
                            <td class="selectColumn" style="">
                                <input type="radio" ng-model="item.checked" name="item1" value="item"
                                       style="cursor: pointer"
                                       ng-click="newRelatedItemVm.selectItem(item)">
                            </td>
                            <td style="vertical-align: middle;" class="col-width-150">
                                <span title="{{item.relationship.name}}">{{item.relationship.name}}</span>
                            </td>
                            <td style="vertical-align: middle;" class="col-width-150">
                                <a ng-if="item.configurable == true" title="{{configurableItem}}" class="fa fa-cog"
                                   aria-hidden="true"></a>
                                <span ng-bind-html="item.itemNumber | highlightText: newRelatedItemVm.filters.itemNumber"></span>
                            </td>
                            <td style="vertical-align: middle;" class="col-width-150">
                                {{item.itemType.name}}
                            </td>
                            <td style="vertical-align: middle;" class="col-width-200">
                                <span ng-bind-html="item.itemName | highlightText: newRelatedItemVm.filters.itemName"></span>
                            </td>
                            <td style="vertical-align: middle;" class="col-width-250">
                                <span ng-bind-html="item.description | highlightText: newRelatedItemVm.filters.description"></span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div ng-if="newRelatedItemVm.relatedItem.relationship != null && newRelatedItemVm.showAttributes == true">
                    <h4 class="section-title" style="" translate>RELATIONSHIP_ATTRIBUTES
                    </h4>
                    <br>
                    <span ng-if="newRelatedItemVm.relationshipAttributes.length == 0" translate>NO_ATTRIBUTES</span>

                    <form class="form-horizontal" ng-if="newRelatedItemVm.relatedItem.relationship != null">
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newRelatedItemVm.requiredAttributes"></attributes-view>
                        <attributes-view show-objects="selectObjectValues"
                                         attributes="newRelatedItemVm.attributes"></attributes-view>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
