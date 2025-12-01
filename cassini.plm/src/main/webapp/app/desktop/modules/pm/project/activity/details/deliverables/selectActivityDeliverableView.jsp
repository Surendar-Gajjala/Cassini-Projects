<style>
    a.disabled {
        cursor: not-allowed;
        color: lightgrey;
    }

    #description {
        word-wrap: break-word;
        width: 200px;
        white-space: normal;
        text-align: left;
    }

    .radius {
        width: 64%;
        min-width: 64%;
        border-radius: 26px;
        padding: 4px 5px 7px 179px;
        top: 0;
        left: 0;
        z-index: 5;
        border: 1px #e6e8ed solid;
        background-color: rgb(241, 243, 244);
        outline: none;
    }

    .radius > input:focus {
        border: 1px solid #ddd !important;
        box-shadow: none;
    }

    .fa-search {
        position: relative;
        margin-top: 11px;
        color: grey;
        opacity: 0.5;
        margin-right: -28px !important;
    }

    #footer-content {
        border-bottom: 1px solid lightgrey;
        padding: 2px;
        text-align: center;
        height: 50px;
        width: 100%;
        display: flex;
    }

</style>


<div id="deliverablesView">
    <style scoped>
        #rightSidePanelContent {
            overflow: hidden !important;
        }

        .ui-select-bootstrap > .ui-select-match > .btn {
            padding-bottom: 1px !important;
        }
        .open > .dropdown-toggle.btn {
            color: #444442 !important;
        }
    </style>

    <div id="form-content" style="height: 40px;border-bottom: 1px solid lightgrey;">
        <form class="form-inline ng-pristine ng-valid" style="padding: 5px;display: flex;flex-direction: row;"
              ng-if="selectActivityDeliverableVm.showItemDeliverable == true">
            <div class="form-group" style="flex-grow: 1;margin-right: 0;">
                <div class="input-group input-group-sm" style="">
                    <div class="input-group-btn dropdown" uib-dropdown="" style="">
                        <button class="btn btn-default dropdown-toggle" uib-dropdown-toggle>
                            <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                        </button>
                        <div class="dropdown-menu" role="menu"
                             style="z-index: 9999 !important; margin-right: -56px !important;left: 0;">
                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                                <classification-tree
                                        on-select-type="selectActivityDeliverableVm.onSelectType"></classification-tree>
                            </div>
                        </div>
                    </div>
                    <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                           ng-model="selectActivityDeliverableVm.selectedType.name" readonly>

                </div>
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
                <input type="text" ng-model="selectActivityDeliverableVm.deliverableFilter.itemNumber"
                       ng-change="selectActivityDeliverableVm.searchItems()"
                       style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                       placeholder="{{'ITEM_NUMBER' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
                <input type="text" ng-model="selectActivityDeliverableVm.deliverableFilter.name"
                       ng-change="selectActivityDeliverableVm.searchItems()"
                       style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                       placeholder="{{'ITEM_NAME' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched">
            </div>
            <div style="margin-top: 0;flex-grow: 1;" class="">
                <button ng-click="selectActivityDeliverableVm.clearFilter()" style="height: 29px;width: 100%"
                        class="btn btn-xs btn-danger ng-scope" translate>CLEAR
                </button>
            </div>
        </form>

        <form class="form-inline ng-pristine ng-valid" style="padding: 5px;display: flex;flex-direction: row;"
              ng-if="selectActivityDeliverableVm.specification == true">
            <div class="form-group" style="flex-grow: 1;margin-right: 0;">
                <div class="input-group input-group-sm" style="">
                    <div class="input-group-btn dropdown" uib-dropdown="" style="">
                        <button class="btn btn-default dropdown-toggle" uib-dropdown-toggle>
                            <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                        </button>
                        <div class="dropdown-menu" role="menu"
                             style="z-index: 9999 !important; margin-right: -56px !important;left: 0;">
                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                                <specification-tree
                                        on-select-type="selectActivityDeliverableVm.onSpecificationSelectType"></specification-tree>
                            </div>
                        </div>
                    </div>
                    <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                           ng-model="selectActivityDeliverableVm.selectedType.name" readonly>

                </div>
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
                <input type="text" ng-model="selectActivityDeliverableVm.SpecReqfilters.name"
                       ng-change="selectActivityDeliverableVm.searchSpecifications()"
                       style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                       placeholder="{{'NAME' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
                <input type="text" ng-model="selectActivityDeliverableVm.SpecReqfilters.description"
                       ng-change="selectActivityDeliverableVm.searchSpecifications()"
                       style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                       placeholder="{{'DESCRIPTION' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched">
            </div>
            <div style="margin-top: 0;flex-grow: 1;" class="">
                <button ng-click="selectActivityDeliverableVm.clearSpecificationFilter()"
                        ng-if="selectActivityDeliverableVm.clearSpecification == true" style="height: 29px;width: 100%"
                        class="btn btn-xs btn-danger ng-scope" translate>CLEAR
                </button>
            </div>
        </form>

        <form class="form-inline ng-pristine ng-valid" style="padding: 5px;display: flex;flex-direction: row;"
              ng-if="selectActivityDeliverableVm.requirement == true">
            <div class="form-group" style="flex-grow: 1;margin-right: 0;">
                <div class="input-group input-group-sm" style="">
                    <div class="input-group-btn dropdown" uib-dropdown="" style="">
                        <button class="btn btn-default dropdown-toggle" uib-dropdown-toggle>
                            <span translate>SELECT</span> <span class="caret" style="margin-left: 4px;"></span>
                        </button>
                        <div class="dropdown-menu" role="menu"
                             style="z-index: 9999 !important; margin-right: -56px !important;left: 0;">
                            <div style="overflow: auto;padding: 5px; border: 1px solid #FFF;height: 200px;">
                                <requirement-tree
                                        on-select-type="selectActivityDeliverableVm.onRequirementSelectType"></requirement-tree>
                            </div>
                        </div>
                    </div>
                    <input type="text" class="form-control ng-pristine ng-valid ng-touched ng-untouched" name="title"
                           ng-model="selectActivityDeliverableVm.selectedType.name" readonly>

                </div>
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 5px;text-align: center;">
                <input type="text" ng-model="selectActivityDeliverableVm.SpecReqfilters.name"
                       ng-change="selectActivityDeliverableVm.searchRequirements()"
                       style="margin-left: -16px; width: 100%;height: 30px;margin-top: 0;"
                       placeholder="{{'ITEM_NAME' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched ng-untouched">
            </div>
            <div class="form-group" style="flex-grow: 2;margin-right: 0;text-align: center;">
                <input type="text" ng-model="selectActivityDeliverableVm.SpecReqfilters.description"
                       ng-change="selectActivityDeliverableVm.searchRequirements()"
                       style="margin-left: -16px; height: 30px;width: 100%;margin-top: 0;"
                       placeholder="{{'DESCRIPTION' | translate}}"
                       class="input-sm form-control ng-pristine ng-valid ng-touched">
            </div>
            <div style="margin-top: 0;flex-grow: 1;" class="">
                <button ng-click="selectActivityDeliverableVm.clearRequirementFilter()"
                        ng-if="selectActivityDeliverableVm.clearRequirement == true" style="height: 29px;width: 100%"
                        class="btn btn-xs btn-danger ng-scope" translate>CLEAR
                </button>
            </div>
        </form>
    </div>

    <div id="footer-content">
        <div class="form-group" style="width: 100%;margin-top: 2px;padding-bottom: 0 !important;">
            <div class="col-sm-4">
                <%--<ui-select ng-model="selected" theme="bootstrap"
                           style="width:100%;padding-top: 2px;" on-select="switchType($item)">
                    <ui-select-match placeholder="Select">{{$select.selected}}
                    </ui-select-match>
                    <ui-select-choices
                            repeat="list in selectActivityDeliverableVm.deliverableList">
                        <div ng-bind-html="list"></div>
                    </ui-select-choices>
                </ui-select>--%>
            </div>
            <div class="col-sm-8">
                <div ng-if="selectActivityDeliverableVm.showItemDeliverable == true" class="pull-right"
                     style="padding: 10px;">
                    <span>

                        <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectActivityDeliverableVm.items.numberOfElements ==0">
                            {{(selectActivityDeliverableVm.pageable.page*selectActivityDeliverableVm.pageable.size)}}
                        </span>

                                    <span ng-if="selectActivityDeliverableVm.items.numberOfElements > 0">
                            {{(selectActivityDeliverableVm.pageable.page*selectActivityDeliverableVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="selectActivityDeliverableVm.items.last ==false">{{((selectActivityDeliverableVm.pageable.page+1)*selectActivityDeliverableVm.pageable.size)}}</span>
                                    <span ng-if="selectActivityDeliverableVm.items.last == true">{{selectActivityDeliverableVm.items.totalElements}}</span>

                                 <span translate> OF </span>
                                {{selectActivityDeliverableVm.items.totalElements}}<span
                                        translate>AN</span>
                                </span>
                        </medium>
                        <%--
                        <medium>
                            <span style="margin-right: 10px;">
                                Displaying {{selectActivityDeliverableVm.items.numberOfElements}} of {{selectActivityDeliverableVm.items.totalElements}}
                            </span>
                        </medium>--%>
                    </span>
                    <span class="mr10">Page {{selectActivityDeliverableVm.items.totalElements != 0 ? selectActivityDeliverableVm.items.number+1:0}} of {{selectActivityDeliverableVm.items.totalPages}}</span>
                    <a href="" ng-click="selectActivityDeliverableVm.previousPage()"
                       ng-class="{'disabled': selectActivityDeliverableVm.items.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="selectActivityDeliverableVm.nextPage()"
                       ng-class="{'disabled': selectActivityDeliverableVm.items.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
                <div ng-if="selectActivityDeliverableVm.showGlossaryDeliverable == true" class="pull-right"
                     style="padding: 10px;">
                <span>

                     <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectActivityDeliverableVm.glossaries.numberOfElements ==0">
                            {{(selectActivityDeliverableVm.pageable.page*selectActivityDeliverableVm.pageable.size)}}
                        </span>

                                    <span ng-if="selectActivityDeliverableVm.glossaries.numberOfElements > 0">
                            {{(selectActivityDeliverableVm.pageable.page*selectActivityDeliverableVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="selectActivityDeliverableVm.glossaries.last ==false">{{((selectActivityDeliverableVm.pageable.page+1)*selectActivityDeliverableVm.pageable.size)}}</span>
                                    <span ng-if="selectActivityDeliverableVm.glossaries.last == true">{{selectActivityDeliverableVm.glossaries.totalElements}}</span>

                                 <span translate> OF </span>
                                {{selectActivityDeliverableVm.glossaries.totalElements}}<span
                                        translate>AN</span>
                                </span>
                     </medium>
                    <%--
                    <medium>
                        <span style="margin-right: 10px;">
                                        Displaying {{selectActivityDeliverableVm.glossaries.numberOfElements}} of
                                            {{selectActivityDeliverableVm.glossaries.totalElements}}
                        </span>
                    </medium>--%>
                </span>
                    <span class="mr10">Page {{selectActivityDeliverableVm.glossaries.totalElements != 0 ? selectActivityDeliverableVm.glossaries.number+1:0}} of {{selectActivityDeliverableVm.glossaries.totalPages}}</span>
                    <a href="" ng-click="selectActivityDeliverableVm.previousPage()"
                       ng-class="{'disabled': selectActivityDeliverableVm.glossaries.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="selectActivityDeliverableVm.nextPage()"
                       ng-class="{'disabled': selectActivityDeliverableVm.glossaries.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
                <div ng-if="selectActivityDeliverableVm.specification == true" class="pull-right"
                     style="padding: 10px;">
                        <span>

                               <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectActivityDeliverableVm.projectSpecifications.numberOfElements ==0">
                            {{(selectActivityDeliverableVm.pageable.page*selectActivityDeliverableVm.pageable.size)}}
                        </span>

                                    <span ng-if="selectActivityDeliverableVm.projectSpecifications.numberOfElements > 0">
                            {{(selectActivityDeliverableVm.pageable.page*selectActivityDeliverableVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="selectActivityDeliverableVm.projectSpecifications.last ==false">{{((selectActivityDeliverableVm.pageable.page+1)*selectActivityDeliverableVm.pageable.size)}}</span>
                                    <span ng-if="selectActivityDeliverableVm.projectSpecifications.last == true">{{selectActivityDeliverableVm.projectSpecifications.totalElements}}</span>

                                 <span translate> OF </span>
                                {{selectActivityDeliverableVm.projectSpecifications.totalElements}}<span
                                        translate>AN</span>
                                </span>
                               </medium>

                            <%--<medium>
                                <span style="margin-right: 10px;">
                                        Displaying {{selectActivityDeliverableVm.projectSpecifications.numberOfElements}} of
                                            {{selectActivityDeliverableVm.projectSpecifications.totalElements}}
                                    </span>
                            </medium>--%>
                        </span>
                    <span class="mr10">Page {{selectActivityDeliverableVm.projectSpecifications.totalElements != 0 ? selectActivityDeliverableVm.projectSpecifications.number+1:0}} of {{selectActivityDeliverableVm.projectSpecifications.totalPages}}</span>
                    <a href="" ng-click="selectActivityDeliverableVm.previousSpecificationPage()"
                       ng-class="{'disabled': selectActivityDeliverableVm.projectSpecifications.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="selectActivityDeliverableVm.nextSpecificationPage()"
                       ng-class="{'disabled': selectActivityDeliverableVm.projectSpecifications.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
                <div ng-if="selectActivityDeliverableVm.requirement == true">
                    <span>

                         <medium>
                                <span style="margin-right: 10px;">
                                    <span translate>DISPLAYING</span>
                                    <span ng-if="selectActivityDeliverableVm.projectRequirements.numberOfElements ==0">
                            {{(selectActivityDeliverableVm.pageable.page*selectActivityDeliverableVm.pageable.size)}}
                        </span>

                                    <span ng-if="selectActivityDeliverableVm.projectRequirements.numberOfElements > 0">
                            {{(selectActivityDeliverableVm.pageable.page*selectActivityDeliverableVm.pageable.size)+1}}
                        </span>
                                -
                                    <span ng-if="selectActivityDeliverableVm.projectRequirements.last ==false">{{((selectActivityDeliverableVm.pageable.page+1)*selectActivityDeliverableVm.pageable.size)}}</span>
                                    <span ng-if="selectActivityDeliverableVm.projectRequirements.last == true">{{selectActivityDeliverableVm.projectRequirements.totalElements}}</span>

                                 <span translate> OF </span>
                                {{selectActivityDeliverableVm.projectRequirements.totalElements}}<span
                                        translate>AN</span>
                                </span>
                         </medium>

                       <%-- <medium>
                            <span style="margin-right: 10px;">
                                        Displaying {{selectActivityDeliverableVm.projectRequirements.numberOfElements}} of
                                            {{selectActivityDeliverableVm.projectRequirements.totalElements}}
                            </span>
                        </medium>--%>
                    </span>
                    <span class="mr10">Page {{selectActivityDeliverableVm.projectRequirements.totalElements != 0 ? selectActivityDeliverableVm.projectRequirements.number+1:0}} of {{selectActivityDeliverableVm.projectRequirements.totalPages}}</span>
                    <a href="" ng-click="selectActivityDeliverableVm.previousRequirementPage()"
                       ng-class="{'disabled': selectActivityDeliverableVm.projectRequirements.first}"><i
                            class="fa fa-arrow-circle-left mr10"></i></a>
                    <a href="" ng-click="selectActivityDeliverableVm.nextRequirementPage()"
                       ng-class="{'disabled': selectActivityDeliverableVm.projectRequirements.last}"><i
                            class="fa fa-arrow-circle-right"></i></a>
                </div>
            </div>
        </div>
    </div>

    <div id="search-content">
        <div class="responsive-table" ng-if="selectActivityDeliverableVm.showItemDeliverable == true"
             style="height: 100%;overflow: auto;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px">
                        <div class="ckbox ckbox-default" style="display: inline-block;">
                            <input id="items{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="selectActivityDeliverableVm.selectedAllItems"
                                   ng-if="selectActivityDeliverableVm.items.content.length != 0"
                                   ng-click="selectActivityDeliverableVm.selectAllItems(check)">
                            <label for="items{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th style="width: 1% !important;white-space: nowrap;" translate="ITEM_ALL_ITEM_NUMBER"></th>
                    <th style="width: 150px" translate="ITEM_ALL_ITEM_TYPE"></th>
                    <th style="width: 150px" translate="ITEM_ALL_ITEM_NAME"></th>
                    <th style="" translate="ITEM_ALL_DESCRIPTION"></th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_REVISION"></th>
                    <th style="width: 150px" translate="ITEM_ALL_LIFECYCLE"></th>
                    <th style="width: 150px" translate="ITEM_ALL_RELEASED_DATE"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectActivityDeliverableVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_ITEMS</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="selectActivityDeliverableVm.loading == false && selectActivityDeliverableVm.items.content.length == 0">
                    <td colspan="25" translate>NO_ITEMS</td>
                </tr>

                <tr ng-repeat="item in selectActivityDeliverableVm.items.content">
                    <td>
                        <div class="ckbox ckbox-default"
                             style="display: inline-block;">
                            <input id="item{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="item.selected"
                                   ng-click="selectActivityDeliverableVm.selectItem(item)">
                            <label for="item{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>

                    <td style="width: 1% !important;white-space: nowrap;">
                        <a href="" ng-if="item.configurable == true" title="{{configurableItem}}" class="fa fa-cog">
                        </a>
                    <span ng-click="selectActivityDeliverableVm.showItem(item)"
                          title="{{'CLICK_TO_SHOW_DETAILS' | translate}}" ng-bind-html=" item.itemNumber | highlightText:
                          freeTextQuery"></span>
                    </td>
                    <td style="width: 150px"><span
                            ng-bind-html="item.itemType.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="width: 150px"><span ng-bind-html="item.itemName | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="description-column" title="{{item.description}}"><span
                            ng-bind-html="item.description  | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="width: 150px; text-align: center;">{{item.latestRevisionObject.revision}}</td>
                    <td style="width: 150px">
                        <item-status item="item.latestRevisionObject"></item-status>
                    </td>
                    <td style="width: 150px">
                        <span>{{item.latestRevisionObject.releasedDate}}</span>
                    </td>

                </tr>
                </tbody>
            </table>
        </div>

        <div class="responsive-table" ng-if="selectActivityDeliverableVm.showGlossaryDeliverable == true"
             style="height: 100%;overflow: auto;">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px">
                        <div class="ckbox ckbox-default"
                             style="display: inline-block;">
                            <input id="glossaries{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="selectActivityDeliverableVm.selectedAllGlossary"
                                   ng-if="selectActivityDeliverableVm.glossaries.content.length != 0"
                                   ng-click="selectActivityDeliverableVm.checkAllGlossary()">
                            <label for="glossaries{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th translate>NAME</th>
                    <th translate>DESCRIPTION</th>
                    <th translate style="text-align: center">REVISION</th>
                    <th translate>LIFE_CYCLE</th>
                    <th translate>RELEASED_DATE</th>
                </tr>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectActivityDeliverableVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="selectActivityDeliverableVm.loading == false && selectActivityDeliverableVm.glossaries.content.length == 0">
                    <td colspan="25" translate>NO_DELIVERABLES</td>
                </tr>
                <tr ng-repeat="glossary in selectActivityDeliverableVm.glossaries.content">
                    <td>
                        <div class="ckbox ckbox-default"
                             style="display: inline-block;">
                            <input id="glossary{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="glossary.glossarySelected"
                                   ng-click="selectActivityDeliverableVm.selectGlossary(glossary)">
                            <label for="glossary{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>
                    <td>
                        <a href=""
                           ng-click="selectActivityDeliverableVm.openGlossaryDetails(glossary)"
                           title="{{selectActivityDeliverableVm.glossaryOpenTitle}}">
                            <span>{{glossary.defaultDetail.name}}</span>
                        </a>
                    </td>
                    <td><span id="td">{{glossary.defaultDetail.description}}</span></td>
                    <td class="revision-column">{{glossary.revision}}</td>
                    <td>
                        <item-status item="glossary"></item-status>
                    </td>
                    <td>{{glossary.releasedDate}}</td>

                </tr>
                </tbody>
            </table>
        </div>


        <div class="responsive-table" ng-if="selectActivityDeliverableVm.specification == true"
             style="height: 100%;overflow: auto">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px">
                        <div class="ckbox ckbox-default"
                             style="display: inline-block;">
                            <input id="specifications{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="selectActivityDeliverableVm.selectedSpecAll"
                                   ng-if="selectActivityDeliverableVm.projectSpecifications.content.length != 0"
                                   ng-click="selectActivityDeliverableVm.checkAllSpecifications(check)">
                            <label for="specifications{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th style="width: 150px" translate="NUMBER"></th>
                    <th style="width: 150px" translate="TYPE"></th>
                    <th style="width: 150px" translate="NAME"></th>
                    <th style="" translate="ITEM_ALL_DESCRIPTION"></th>
                    <th style="width: 150px; text-align: center;" translate="ITEM_ALL_REVISION"></th>
                    <th style="width: 150px" translate="ITEM_ALL_LIFECYCLE"></th>
                    <th style="width: 150px" translate="ITEM_ALL_RELEASED_DATE"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectActivityDeliverableVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="selectActivityDeliverableVm.loading == false && selectActivityDeliverableVm.projectSpecifications.content.length == 0">
                    <td colspan="25" translate>NO_DELIVERABLES</td>
                </tr>

                <tr ng-repeat="specification in selectActivityDeliverableVm.projectSpecifications.content">
                    <td>
                        <div class="ckbox ckbox-default"
                             style="display: inline-block;">
                            <input id="specification{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="specification.specSelected"
                                   ng-click="selectActivityDeliverableVm.selectSpecification(specification)">
                            <label for="specification{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>

                    <td style="width: 150px">
                        <a href="" ng-click="selectActivityDeliverableVm.showSpecificationDetails(specification)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="specification.objectNumber | highlightText: freeTextQuery"></span></a>
                    </td>
                    <td style="width: 150px"><span
                            ng-bind-html="specification.type.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="width: 150px" class="col-width-250"><span
                            ng-bind-html="specification.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="description-column"><span
                            ng-bind-html="specification.description | highlightText: freeTextQuery"></span></td>
                    <td style="width: 150px; text-align: center;">{{specification.revision}}</td>
                    <td style="width: 150px">
                        {{specification.lifecyclePhase.phase}}
                    </td>
                    <td style="width: 150px">
                        <span>{{specification.releasedDate}}</span>
                    </td>

                </tr>
                </tbody>
            </table>
        </div>

        <div class="responsive-table" ng-if="selectActivityDeliverableVm.requirement == true"
             style="height: 100%;overflow: auto">
            <table class="table table-striped highlight-row">
                <thead>
                <tr>
                    <th style="width: 30px">
                        <div class="ckbox ckbox-default"
                             style="display: inline-block;">
                            <input id="requirements{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="selectActivityDeliverableVm.selectedReqAll"
                                   ng-if="selectActivityDeliverableVm.projectRequirements.content.length != 0"
                                   ng-click="selectActivityDeliverableVm.checkAllRequirements()">
                            <label for="requirements{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </th>
                    <th style="width: 150px" translate="NUMBER"></th>
                    <th style="width: 150px" translate="TYPE"></th>
                    <th style="width: 150px" translate="NAME"></th>
                    <th style="" translate="ITEM_ALL_DESCRIPTION"></th>
                    <th style="width: 150px; text-align: center;" translate="VERSION"></th>
                    <th style="width: 150px" translate="STATUS"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="selectActivityDeliverableVm.loading == true">
                    <td colspan="25">
                        <span style="font-size: 15px;">
                            <img src="app/assets/bower_components/cassini-platform/images/loaders/loader19.gif"
                                 class="mr5">
                            <span translate>LOADING_DELIVERABLES</span>
                        </span>
                    </td>
                </tr>

                <tr ng-if="selectActivityDeliverableVm.loading == false && selectActivityDeliverableVm.projectRequirements.content.length == 0">
                    <td colspan="25" translate>NO_DELIVERABLES</td>
                </tr>

                <tr ng-repeat="requirement in selectActivityDeliverableVm.projectRequirements.content">
                    <td>
                        <div class="ckbox ckbox-default"
                             style="display: inline-block;">
                            <input id="requirement{{$index}}" name="itemSelected" type="checkbox"
                                   ng-model="requirement.reqSelected"
                                   ng-click="selectActivityDeliverableVm.selectRequirement(requirement)">
                            <label for="requirement{{$index}}" class="item-selection-checkbox"></label>
                        </div>
                    </td>

                    <td style="width: 150px">
                        <a href="" ng-click="selectActivityDeliverableVm.showRequirementDetails(requirement)"
                           title="{{'CLICK_TO_SHOW_DETAILS' | translate}}">
                            <span ng-bind-html="requirement.objectNumber | highlightText: freeTextQuery"></span></a>
                    </td>
                    <td style="width: 150px"><span
                            ng-bind-html="requirement.type.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td style="width: 150px" class="col-width-250"><span
                            ng-bind-html="requirement.name | highlightText: freeTextQuery"></span>
                    </td>
                    <td class="description-column"><span
                            ng-bind-html="requirement.description | highlightText: freeTextQuery"></span></td>
                    <td style="width: 150px; text-align: center;">
                        <span ng-if="requirement.version == 0">-</span>
                        <span ng-if="requirement.version > 0">{{requirement.version}}</span>
                    </td>
                    <td style="width: 150px">
                        <span><task-status task="requirement"></task-status></span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

