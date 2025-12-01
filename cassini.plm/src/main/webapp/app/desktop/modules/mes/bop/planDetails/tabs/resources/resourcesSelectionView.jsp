<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #rightSidePanelContent {
        overflow: hidden !important;
    }

    .item-selection-table table th {
        position: -webkit-sticky;
        position: sticky;
        top: -1px !important;
        z-index: 5 !important;
    }

    .table-div {
        position: absolute;
        top: 145px !important;
        bottom: 50px !important;
        left: 0;
        right: 0;
    }

    /*.open > .dropdown-toggle.btn {
        color: #091007 !important;
    }*/
</style>
<div style="padding: 0px 10px;">
    <style scoped>
        .ui-select-bootstrap > .ui-select-match > .btn {
            padding: 5px 7px;
        }
    </style>
    <div class="row" style="padding: 5px 0;display: flex;">
        <div style="width: 150px;margin-right: 10px;">
            <ui-select ng-model="resourcesSelectionVm.selectedResource" theme="bootstrap"
                       style="width:100%"
                       on-select="resourcesSelectionVm.onSelectResource($item)">
                <ui-select-match placeholder="Select Resource">{{$select.selected.resource}}</ui-select-match>
                <ui-select-choices
                        repeat="source in resourcesSelectionVm.operationResources | filter: $select.search">
                    <div>{{source.resource}}</div>
                </ui-select-choices>
            </ui-select>
        </div>
        <div style="width: 175px;margin-right: 10px;">
            <ui-select ng-model="resourcesSelectionVm.selectedResourceType" theme="bootstrap"
                       style="width:100%"
                       on-select="resourcesSelectionVm.loadResources()">
                <ui-select-match placeholder="Select Type">{{$select.selected.resourceTypeName}}</ui-select-match>
                <ui-select-choices
                        repeat="resourceType in resourcesSelectionVm.selectedResource.resourceTypes | filter: $select.search">
                    <div>{{resourceType.resourceTypeName}}</div>
                </ui-select-choices>
            </ui-select>
        </div>
        <div ng-if="resourcesSelectionVm.selectedResourceType != null" style="padding: 5px">
            <span style="font-weight: bold">Allocated Qty :</span>
            {{resourcesSelectionVm.selectedResourceType.quantity}}
        </div>
        <div ng-if="resourcesSelectionVm.selectedResourceType != null" style="padding: 5px">
            <span style="font-weight: bold">Consumed Qty :</span>
            {{resourcesSelectionVm.selectedResourceType.consumedQty}}
        </div>
    </div>
    <div class="row">
        <div class="col-md-12" style="border: 1px solid lightgrey;">
            <div class="col-md-5" style="padding-left: 0;padding-top: 10px;">
                <div style="">
                    <span style="" translate>SELECTED_RESOURCES</span>
                    <span class="badge">{{resourcesSelectionVm.selectedResources.length}}</span>
                    <a href="" ng-click="resourcesSelectionVm.clearSelection()"
                       ng-if="resourcesSelectionVm.selectedResources.length > 0">Clear</a>
                </div>
            </div>
            <div class="col-md-7">
                <div class="pull-right text-center" style="padding: 10px;">
                    <div>
                        <span>
                            <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="resourcesSelectionVm.resources.numberOfElements ==0">
                            {{(resourcesSelectionVm.pageable.page*resourcesSelectionVm.pageable.size)}}
                        </span>

                                    <span ng-if="resourcesSelectionVm.resources.numberOfElements > 0">
                            {{(resourcesSelectionVm.pageable.page*resourcesSelectionVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="resourcesSelectionVm.resources.last ==false">{{((resourcesSelectionVm.pageable.page+1)*resourcesSelectionVm.pageable.size)}}</span>
                                    <span ng-if="resourcesSelectionVm.resources.last == true">{{resourcesSelectionVm.resources.totalElements}}</span>

                                 <span translate> OF </span>
                                {{resourcesSelectionVm.resources.totalElements}}<span
                                        translate>AN</span>
                                </span>
                            </medium>
                        </span>
                        <span class="mr10">  Page {{resourcesSelectionVm.resources.totalElements != 0 ?
                        resourcesSelectionVm.resources.number+1:0}} <span translate>OF</span> {{resourcesSelectionVm.resources.totalPages}}</span>
                        <a href="" ng-click="resourcesSelectionVm.previousPage()"
                           ng-class="{'disabled': resourcesSelectionVm.resources.first}"><i
                                class="fa fa-arrow-circle-left mr10"></i></a>
                        <a href="" ng-click="resourcesSelectionVm.nextPage()"
                           ng-class="{'disabled': resourcesSelectionVm.resources.last}"><i
                                class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="col-md-12 table-div" style="padding:0px; height: auto;overflow: auto;">
    <div class="resource-selection-table">
        <table class="table table-striped">
            <thead>
            <tr>
                <th style="text-align: center;width: 20px !important;">
                    <input name="resource"
                           type="checkbox" ng-model="resourcesSelectionVm.selectAllCheck"
                           ng-click="resourcesSelectionVm.selectAll()" ng-checked="check">
                </th>
                <th translate>NUMBER</th>
                <th translate>TYPE</th>
                <th translate>NAME</th>
                <%--<th translate>WORKCENTER</th>--%>
                <th translate>DESCRIPTION</th>

            </tr>
            </thead>
            <tbody>
            <tr ng-if="resourcesSelectionVm.loading == true">
                <td colspan="25">
                    <span style="font-size: 15px;">
                        <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                             class="mr5">
                        <span translate>LOADING_RESOURCES</span>
                    </span>
                </td>
            </tr>
            <tr ng-if="resourcesSelectionVm.loading == false && resourcesSelectionVm.resources.content.length == 0">
                <td colspan="25" style="background-color: #f9fbfe  !important;color: unset !important;">
                    <div class="no-data">
                        <img src="app/assets/no_data_images/MBOM.png" alt="" class="image">

                        <div class="message" translate>NO_RESOURCES</div>
                        <div class="message ng-scope" style="font-size: 14px;color: #8f979d;" translate>
                            NO_PERMISSION_MESSAGE
                        </div>
                    </div>
                </td>
            </tr>
            <tr ng-repeat="resource in resourcesSelectionVm.resources.content">
                <td style="vertical-align: middle;text-align: center;width: 20px !important;">
                    <input name="resource" type="checkbox"
                           ng-model="resource.selected"
                           ng-click="resourcesSelectionVm.selectCheck(resource)">
                </td>
                <td class="col-width-100">
                    <span ng-bind-html="resource.number | highlightText: resourcesSelectionVm.filters.number"></span>
                </td>

                <td class="col-width-150">
                    <span ng-if="resourcesSelectionVm.filters.resource != 'JIGS_FIXTURES'"
                          ng-bind-html="resource.type.name | highlightText: resourcesSelectionVm.filters.typeName"></span>
                    <span ng-if="resourcesSelectionVm.filters.resource == 'JIGS_FIXTURES'"
                          ng-bind-html="resource.type | highlightText: resourcesSelectionVm.filters.typeName"></span>
                </td>
                <td class="col-width-150" title="{{resource.name}}">
                    <span ng-bind-html="resource.name | highlightText: resourcesSelectionVm.filters.name"></span>
                </td>
                <%--<td class="col-width-150">{{resource.workCenterName}}</td>--%>
                <td class="col-width-250" title="{{resource.description}}">
                    <span ng-bind-html="resource.description  | highlightText: freeTextQuery"></span>
                </td>
            </tr>

            </tbody>
        </table>
    </div>
</div>
</div>